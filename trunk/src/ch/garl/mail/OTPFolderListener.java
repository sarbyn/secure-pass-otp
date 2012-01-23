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

import net.rim.blackberry.api.mail.Message;
import net.rim.blackberry.api.mail.event.FolderEvent;
import net.rim.blackberry.api.mail.event.FolderListener;
import net.rim.device.api.system.ApplicationDescriptor;
import net.rim.device.api.system.ApplicationManager;
import net.rim.device.api.system.Backlight;
import net.rim.device.api.system.CodeModuleManager;
import ch.garl.MainClass;
import ch.garl.ui.component.Logger;

/**
 * Folder listener used for automatic mail import
 * @author Emanuele Gambaro
 */
public class OTPFolderListener implements FolderListener {

	/**
	 * Application module name. If the application is not in 
	 * background, ApplicationManager use this string in order to
	 * run the application and do the import
	 */
	private static final String MODULE_NAME = "SecurePassOTP";
	
	public void messagesAdded(FolderEvent e) {
		final Message m = e.getMessage();
		
		Logger.log("[OTPFolderListener] is ok? " + MessageUtils.isSecurePassMail(m));

		if (MessageUtils.isSecurePassMail(m)) {
			Logger.log("[OTPFolderListener] NEW SECURE PASS MAIL!");
			
			ApplicationManager manager = ApplicationManager.getApplicationManager();
			ApplicationDescriptor descriptor = null;
			
			// check if the application is running in background
			ApplicationDescriptor descriptors[] = manager.getVisibleApplications();
			for (int i = 0; i != descriptors.length; i++) {
				if (descriptors[i].getModuleName().equals(MODULE_NAME)) {
					descriptor = descriptors[i];
					break;
				}
			}
			
			if (descriptor == null) {
				Logger.log("App not running start it...sleep 1000 millis...");
				
				int handler = CodeModuleManager.getModuleHandle(MODULE_NAME);
				descriptor = CodeModuleManager.getApplicationDescriptors(handler)[0];  
				try {
					manager.runApplication(descriptor, false);
					Thread.sleep(1000);
				} catch (Exception e1) {
					Logger.log("Error starting new application instance " + e1.getMessage());
				}
			} else {
				Logger.log("App running with descriptor " + descriptor + " - Fire global event");
			}
			
			// application is running - fire global event and wake up the device 
			
			ApplicationManager.getApplicationManager().unlockSystem();
			Backlight.enable(true);
			ApplicationManager.getApplicationManager().postGlobalEvent(MainClass.GLOBAL_EVENT_ID,
            		0, 0, m, null);
		}	
	}

	public void messagesRemoved(FolderEvent e) {
	}
}
