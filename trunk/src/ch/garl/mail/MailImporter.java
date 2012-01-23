/*
 * Copyright Emanuele Gambaro - 2011
 * 
 * This file is part of SecurePass-OTP.
 *
 * SecurePass-OTP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SecurePass-OTP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SecurePass-OTP.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package ch.garl.mail;

import java.io.IOException;
import java.io.InputStream;

import net.rim.blackberry.api.mail.Message;
import ch.garl.MainClass;
import ch.garl.persistent.Storage;
import ch.garl.persistent.TokenData;
import ch.garl.ui.EditTokenScreen;
import ch.garl.ui.ScreenBuilder;
import ch.garl.ui.component.Logger;
import ch.garl.util.MailProvisioningData;

/**
 * Import a mail from the user account and extract all the provisioning data
 * @author Emanuele Gambaro
 */
public class MailImporter implements MailImporterListener {

	/**
	 * Parse the message and import provisioning data
	 * @param m Message object
	 */
	public void importMessage(Message m) {
		MailProvisioningData p = getProvisioningData(m);
		
		Logger.log("[MailImporter] Provisioning data size " + p.getProperties().size());

		String seed     = (String) p.get(MessageUtils.SEED);
		String step     = (String) p.get(MessageUtils.STEP);
		String length   = (String) p.get(MessageUtils.LENGTH);
		String type     = (String) p.get(MessageUtils.TYPE);
		String alias    = (String) p.get(MessageUtils.USERNAME);
		String lockdown = (String) p.get(MessageUtils.LOCKDOWN);

		TokenData t = new TokenData();

		t.setSeed(seed);
		t.setType(type);
		t.setStep(step);
		t.setLength(length);
		t.setAlias(alias == null ? "" : alias);
		t.setLockdown(lockdown);

		if (t.validate()) {
			Storage.getInstance().addTokenData(t);
			Storage.getInstance().save();
			onMessageImported(m);
			ScreenBuilder.showNewTokenSavedDialog(t);
			
			ScreenBuilder.refreshTokenListScreen();
			return;
		}
		
		EditTokenScreen screen = new EditTokenScreen(t);
		
		screen.addListener(this);
		screen.setMessage(m);
		
		ScreenBuilder.showScreen(screen);
		
		MainClass.getInstance().requestForeground();
	}
	
	/**
	 * Parse message body and return a MailProvisioningData object
	 * with all the token data
	 * 
	 * @param m Message to parse
	 * @return token data in a MailProvisioningData object
	 */
	public MailProvisioningData getProvisioningData(Message m) {
		MailProvisioningData p = new MailProvisioningData();

		try {
			InputStream is = MessageUtils.getProvisioningData(m);
			p.load(is);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		Logger.log(""+p.getProperties().size());
		return p;
	}

	public void onMessageImported(Message m) {
		if (!MessageUtils.isAutodestroyMessage(m)) return;
		
		MessageUtils.deleteMessage(m);
	}

}
