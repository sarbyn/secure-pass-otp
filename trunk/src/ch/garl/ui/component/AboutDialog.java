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
package ch.garl.ui.component;

import ch.garl.i18n.Localization;
import ch.garl.ui.ScreenBuilder;
import net.rim.device.api.system.ApplicationDescriptor;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;


/**
 * About dialog
 * @author Emanuele Gambaro
 */
public class AboutDialog {

	/**
	 * Show the Application info
	 * Do not remove application credits - See license for more info
	 */
	public static void show() {
		final StringBuffer b = new StringBuffer();
		b.append(ApplicationDescriptor.currentApplicationDescriptor().getName() + "\n");
		b.append(Localization.aboutDialogVersion() + " : " + ApplicationDescriptor.currentApplicationDescriptor().getVersion()+ "\n");
		b.append("Author: Emanuele Gambaro\n");
		b.append("Email: gambaro.emanuele@gmail.com\n");
		b.append(Localization.mainScreenFooter());

		
		UiApplication.getUiApplication().invokeLater(new Runnable(){
			public void run(){
				Dialog dialog = new Dialog(Dialog.D_OK,
						b.toString(), Dialog.OK, 
						Bitmap.getPredefinedBitmap(Bitmap.INFORMATION), 0);
				dialog.setFont(ScreenBuilder.getInstance().getFooterFont());
				dialog.doModal();
			}
		});
	}

}
