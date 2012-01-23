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
package ch.garl.ui;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Characters;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.ListFieldCallback;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.container.MainScreen;
import ch.garl.MainClass;
import ch.garl.i18n.Localization;
import ch.garl.persistent.Storage;
import ch.garl.persistent.TokenData;
import ch.garl.token.IToken;
import ch.garl.token.TokenFactory;
import ch.garl.ui.component.AboutDialog;
import ch.garl.ui.component.Footer;
import ch.garl.ui.component.Header;

/**
 * Main application Screen
 * @author Emanuele Gambaro
 */
public class StartupScreen extends MainScreen implements ListFieldCallback {
	
	private final ListField tokenList;
	private final Header header;
	private final Footer footer;
	
	public StartupScreen() {
		super(MainScreen.VERTICAL_SCROLL | MainScreen.VERTICAL_SCROLLBAR);
		header = new Header();
		add(header);
		
		tokenList = new ListField(Storage.getInstance().getAllTokens().size(), 
				ListField.USE_ALL_HEIGHT);
		tokenList.setCallback(this);
	
		footer = new Footer(Localization.mainScreenFooter());
		
		add(tokenList);
		setStatus(footer);
	}
	
	protected void onVisibilityChange(boolean visible) {
		super.onVisibilityChange(visible);
		tokenList.setSize(Storage.getInstance().getAllTokens().size());
	}
	
    protected void makeMenu(Menu menu, int context) {
    	if (tokenList.getSize() > 0) {
            menu.add(new MenuItem(Localization.mainMenuGeneratePassword(),200000,1) {
    			public void run() {
    				getPassword();
    			}
    		});
            
            menu.add(new MenuItem(Localization.mainMenuEditToken(), 300000, 1) {
            	public void run() {
            		int index = tokenList.getSelectedIndex();
            		TokenData t = (TokenData) Storage.getInstance().getAllTokens().elementAt(index);
            		if (t.isLockdown()) {
            			Dialog.alert(Localization.editTokenWithLockdownWarning());
            		} else {
            			ScreenBuilder.showScreen(new EditTokenScreen(t));
            		}
            	}
            });
            
            menu.add(new MenuItem(Localization.mainMenuDeleteToken(),300000,1) {
    			public void run() {
    				deleteToken();
    			}
    		});
    	}
    	
        menu.add(new MenuItem(Localization.mainMenuNewToken(), 400000, 1) {
        	public void run() {
        		ScreenBuilder.showScreen(new EditTokenScreen(null));
        	}
        });
    	

    	menu.add(new MenuItem(Localization.mainMenuAbout(),500000, 1) {
			public void run() {
				AboutDialog.show();
			}
		});
        
    }
    
	private void deleteToken() {
		Dialog d = new Dialog(Dialog.D_YES_NO, Localization.deleteTokenDialog(),
				Dialog.NO, Bitmap.getPredefinedBitmap(Bitmap.QUESTION), 0);
		
		int resp = d.doModal();
		if (resp == Dialog.YES) {
			int selectedIndex = tokenList.getSelectedIndex();
			Storage.getInstance().removeTokenData(selectedIndex);
			tokenList.delete(selectedIndex);
		}
	}

	public boolean onSavePrompt() {
        return true;
    }
    
    /* ListFieldCallback */
	public void drawListRow(ListField listField, Graphics graphics, int index,
			int y, int width) {
		TokenData t = (TokenData) Storage.getInstance().getAllTokens().elementAt(index);
        Bitmap icon = ScreenBuilder.getInstance().getTokenListIcon();
		
        graphics.drawBitmap(0, y,  icon.getWidth(),
				icon.getHeight(),
				icon, 0, 0);
		
		graphics.setColor(Color.BLACK);
		graphics.setFont(ScreenBuilder.getInstance().getFont());
		graphics.drawText(t.getAlias(), icon.getWidth() + ScreenBuilder.getInstance().getTokenListHorizontalMargin(), 
				y + ScreenBuilder.getInstance().getTokenListVerticalMargin(), 0, width);
	}
    /* ListFieldCallback */
	public Object get(ListField listField, int index) {
		return Storage.getInstance().getAllTokens().elementAt(index);
	}
    /* ListFieldCallback */
	public int getPreferredWidth(ListField listField) {
		return Display.getWidth();
	}
    /* ListFieldCallback */
	public int indexOfList(ListField listField, String prefix, int start) {
		return -1;
	}
	
	/**
	 * Generate password
	 */
	private void getPassword() {
		int index = tokenList.getSelectedIndex();
		TokenData t = (TokenData) Storage.getInstance().getAllTokens().elementAt(index);
		IToken token = TokenFactory.CreateToken(t);
		String password;
		Dialog d;
		try {
			password = token.GenerateOtp();
			d = new Dialog(Dialog.D_OK, Localization.passwordDialog() + "\n" + password,
					Dialog.OK, Bitmap.getPredefinedBitmap(Bitmap.INFORMATION), 0);
		} catch (Exception e) {
			d = new Dialog(Dialog.D_OK, Localization.passwordErrorDialog() + "\n" + e.getMessage(),
					Dialog.OK, Bitmap.getPredefinedBitmap(Bitmap.INFORMATION), 0);
		}
		d.doModal();
	}

	protected boolean keyChar(char c, int s, int t) {
		switch (c) {
		case Characters.ENTER:
			getPassword();
			break;
		case Characters.DELETE:
		case Characters.BACKSPACE:
			if (tokenList.getSize() != 0)
				deleteToken();
			break;
		case Characters.ESCAPE:
			MainClass.getInstance().requestBackground();
			break;
		default:
			return super.keyChar(c, s, t);
		}
		
		return true;
	}
	
	/**
	 * Refresh token list
	 */
	public void refreshTokenList() {
		tokenList.setSize(Storage.getInstance().getAllTokens().size());
	}
    
}
