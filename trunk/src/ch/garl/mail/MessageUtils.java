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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import net.rim.blackberry.api.mail.Folder;
import net.rim.blackberry.api.mail.Message;
import net.rim.blackberry.api.mail.NoSuchServiceException;
import net.rim.blackberry.api.mail.Session;
import net.rim.blackberry.api.mail.Store;
import net.rim.blackberry.api.mail.event.FolderListener;
import ch.garl.ui.component.Logger;
import ch.garl.util.MailProvisioningData;

/**
 * Utility class for message handling
 * @author Emanuele Gambaro
 */
public class MessageUtils {
	
	/**
	 * Provisioning data definition - Seed parameter
	 */
	public static final String SEED = "OTP-SEED";
	/**
	 * Provisioning data definition - step parameter
	 */
	public static final String STEP = "OTP-STEP";
	/**
	 * Provisioning data definition - length parameter
	 */
	public static final String LENGTH = "OTP-LENGTH";
	/**
	 * Provisioning data definition - OTP type (only TOTP or HOTP supported)
	 * 
	 */
	public static final String TYPE = "OTP-TYPE";
	/**
	 * Provisioning data definition - autodestroy option 
	 * (accepts "true" or "false" values)
	 */
	public static final String HEADER_AUTODESTROY = "OTP-AUTODESTROY";
	/**
	 * Provisioning data definition - username parameter
	 */
	public static final String USERNAME = "OTP-USERNAME";
	/**
	 * Provisioning data definition - lockdown option 
	 * (accepts "true" or "false" values)
	 */
	public static final String LOCKDOWN = "OTP-LOCKDOWN";
	/**
	 * Provisioning data definition - start header
	 */
	public static final String PROVISIONING_HEADER = "---OTP-PROVISIONING-DATA---";
	
	/**
	 * Register a folderListener on the inbox folder
	 * @param l FolderListener
	 * @throws NoSuchServiceException
	 */
	public static void registerInboxListener(FolderListener l ) throws NoSuchServiceException {
		Store store = Session.waitForDefaultSession().getStore();
        Folder[] folders = store.list(Folder.INBOX);
       	folders[0].addFolderListener(l);
	}
	
	/**
	 * Check if the message have the Autodestroy option
	 * 
	 * @param m Message to check
	 * @return true if is an Autodestroy message, false otherwise
	 */
	public static boolean isAutodestroyMessage(Message m) {
		MailProvisioningData p = new MailProvisioningData();
		try {
			p.load(getProvisioningData(m));
		} catch (IOException e) {
		}
		String autoDestroyString = (String) p.get(HEADER_AUTODESTROY);
		
		if (autoDestroyString == null) return false;
		
		if (autoDestroyString.toLowerCase().equals("true")) return true;
		
		return false;
	}
	
	/**
	 * Check if the message have any OTP provisioning data
	 * @param m Message
	 * @return true if the message have OTP data, false otherwise
	 */
	public static boolean haveProvisioningData(Message m) {
		String messageBody=m.getBodyText();
		boolean resp = messageBody.indexOf(PROVISIONING_HEADER) > 0;
		Logger.log("[haveProvisioningData] resp is " + resp);
		return resp;
	}
	
	/**
	 * Extract the provisioning data section
	 * @param m Message to be imported
	 * @return InputStream for the provisiong data section
	 */
	public static InputStream getProvisioningData(Message m) {
		String messageBody=m.getBodyText();
		int startProvisioningData =  messageBody.indexOf(PROVISIONING_HEADER);

		String provisioningData = messageBody.substring(startProvisioningData + PROVISIONING_HEADER.length()+1);
		// BAH!
		provisioningData = provisioningData.replace(' ', '\n');

		return new ByteArrayInputStream(provisioningData.getBytes());
	}


	/**
	 * Check if is a valid OTP provisioning mail
	 * 
	 * @param m Message to check
	 * @return true if the message is valid
	 */
	public static boolean isSecurePassMail(Message m) {
	
		try {
			if (haveProvisioningData(m)) return true;
		} catch (Exception e) {
			return false;
		}

		return false;
	}

	/**
	 * Delete the message from device folder.
	 * This method doesn't delete the message on the remote folder, due to a
	 * RIM api limitation
	 * @param m Message
	 */
	public static void deleteMessage(Message m) {
		try {
			Store store = Session.waitForDefaultSession().getStore();
			Folder[] folders = store.list(Folder.INBOX);
        	folders[0].deleteMessage(m, true);
		} catch (NoSuchServiceException e) {
			e.printStackTrace();
		}
	}
}
