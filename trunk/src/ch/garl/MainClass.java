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
package ch.garl;

import net.rim.blackberry.api.mail.Message;
import net.rim.blackberry.api.mail.NoSuchServiceException;
import net.rim.blackberry.api.mail.event.FolderListener;
import net.rim.blackberry.api.menuitem.ApplicationMenuItem;
import net.rim.blackberry.api.menuitem.ApplicationMenuItemRepository;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.GlobalEventListener;
import net.rim.device.api.system.RuntimeStore;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import ch.garl.i18n.Localization;
import ch.garl.mail.MailImporter;
import ch.garl.mail.MessageUtils;
import ch.garl.mail.OTPFolderListener;
import ch.garl.ui.StartupScreen;
import ch.garl.ui.component.Logger;

/**
 * This class extends the UiApplication class, providing a
 * graphical user interface.
 * @author Emanuele Gambaro
 */
public class MainClass extends UiApplication implements GlobalEventListener {
	
	private static MainClass instance;
	
	/**
	 * Folder Listener ID for Runtime Store
	 */
	public static final long FOLDER_LISTENER_ID = 0xe73efb476dab78aL;
	/**
	 * Message Application menu item ID for Runtime Store
	 */
	public static final long MENU_ITEM_ID       = 0x63daec17e3b275e5L;
	/**
	 * Global Event ID used by GlobalEventListener
	 */
	public static final long GLOBAL_EVENT_ID    = 0x32be84de177c00cL;
	

	public MainClass() throws NoSuchServiceException {
        addGlobalEventListener(this);
        pushScreen(new StartupScreen());
    }

	public static MainClass getInstance() {
		return instance;
	}

    public static void main(String[] args) throws NoSuchServiceException {
        instance = new MainClass();  
        
        initListener();
        initSystemMenuItem();
        
        instance.enterEventDispatcher();
    }
    
	/**
	 * Init the custom menu item for RIM Message Application
	 */
	private static void initSystemMenuItem() {
		
		RuntimeStore rs = RuntimeStore.getRuntimeStore();

		synchronized (rs) {
			ApplicationMenuItem ami = (ApplicationMenuItem) rs.get(MENU_ITEM_ID);
			if (ami != null) {
				return;
			}

			ami = new ApplicationMenuItem(0x350100) { 
				public Object run(Object context) { 
					Message m = (Message) context;
					if (m != null && MessageUtils.isSecurePassMail(m)) {
						new MailImporter().importMessage(m);
					}  else {
						Dialog.inform(Localization.isNotSecurePassMail());
					}
					return null; 
				} 

				public String toString() { 
					return Localization.systemMenuImportMail(); 
				} 
			}; 

			ApplicationMenuItemRepository amir = ApplicationMenuItemRepository.getInstance();

			amir.addMenuItem(ApplicationMenuItemRepository.MENUITEM_MESSAGE_LIST, ami);
			amir.addMenuItem(ApplicationMenuItemRepository.MENUITEM_EMAIL_VIEW, ami);		
			rs.put(MENU_ITEM_ID, ami);
		}
	}

	/**
	 * Init the message folder listener
	 */
	private static void initListener() {
		RuntimeStore rs = RuntimeStore.getRuntimeStore();
		
		synchronized (rs) {
			FolderListener l = (FolderListener) rs.get(FOLDER_LISTENER_ID );
			
			if (l == null) {
				l = new OTPFolderListener();
				
				try {
					MessageUtils.registerInboxListener(l);
				} catch (Exception e) {
					Dialog.alert("Error during SecurePass init\n" 
						+ e.getClass() + "\n" + e.getMessage());
				}
				rs.put(FOLDER_LISTENER_ID, l);
			}
		}
	}

	public void eventOccurred(long guid, int data0, int data1, Object object0,
			Object object1) {
		
		Logger.log("Event occurred with guid " + guid + " - guid is " + GLOBAL_EVENT_ID);
		if (guid != GLOBAL_EVENT_ID) return;
		Logger.log("Event occurred");
		getInstance().requestForeground();
		
		Dialog alertDialog = new Dialog(Dialog.D_YES_NO,
				Localization.newSeedReceivedDialog(),
				Dialog.YES,
				Bitmap.getPredefinedBitmap(Bitmap.QUESTION),
				Dialog.GLOBAL_STATUS);
		
		int choice = alertDialog.doModal();
		if (choice == Dialog.YES) {
			Message m = (Message) object0;
			new MailImporter().importMessage(m);
		}
	}
    
}
