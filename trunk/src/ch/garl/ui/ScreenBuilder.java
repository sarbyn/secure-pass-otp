package ch.garl.ui;

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

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.Screen;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.component.TextField;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.text.TextFilter;
import ch.garl.MainClass;
import ch.garl.i18n.Localization;
import ch.garl.persistent.TokenData;


/**
 * Utility class for UI rendering
 * @author Emanuele Gambaro
  */
public class ScreenBuilder {


    private static ScreenBuilder instance = null;

    private UiData uiData;

    public static ScreenBuilder getInstance() {
        if (instance == null) {
            instance = new ScreenBuilder();
        }
        return instance;
    }

    private ScreenBuilder() {
        uiData = UiData.getUiData();
  }
    
    /**
     * Get the right font for the device screen size
     * @return Font used in the application
     */
    public Font getFont() {
        return Font.getDefault().getFontFamily().getFont(FontFamily.SCALABLE_FONT, uiData.fontHeightPixels);
    }
    /**
     * Get the right footer font for the device screen size
     * @return Font used in the application footer
     */
	public Font getFooterFont() {
		return Font.getDefault().getFontFamily().getFont(FontFamily.SCALABLE_FONT, uiData.fontFooterPixels);
	}
    /**
     * Get the header image from application "res" folder
     * @return Application Logo
     */
    public Bitmap getLogo() {
    	return Bitmap.getBitmapResource("securepass-logo.jpg");
    }
    /**
     * Get the token list icon
     * @return
     */
    public Bitmap getTokenListIcon() {
    	return Bitmap.getBitmapResource("token-list-icon.png");
    }
    /**
     * Get the right token list horizontal margin for the device screen size
     * @return Horizontal Margin 
     */
    public int getTokenListHorizontalMargin() {
    	return uiData.tokenListHorizontalMargin;
    }
    /**
     * Get the right token list vertical margin for the device screen size
     * @return vertical Margin 
     */
    public int getTokenListVerticalMargin() {
    	return uiData.tokenListVerticalMargin;
    }
    /**
     * Get the right footer vertical margin for the device screen size
     * @return Footer vertical Margin 
     */
    public int getFooterVerticalMargin() {
    	return uiData.footerVerticalMargin;
    }
    /**
     * Show a screen in top of the application screen stack
     */
	public static void showScreen(final Screen s) {
		MainClass.getUiApplication().invokeLater(new Runnable() {
			public void run() {
				MainClass.getUiApplication().pushScreen(s);
			}
		});		
	}

	/**
	 * Create a TextField and add to the screen
	 * @param screen
	 * @param label TextField label
	 * @param listener FieldChangeListener
	 * @param filter Text Filter
	 * @return
	 */
	public static TextField createTextField(MainScreen screen,
			String label, FieldChangeListener listener,
			int filter) {
		TextField f = new TextField();
		f.setFilter(TextFilter.get(filter));
		f.setLabel(label + " ");
		f.setChangeListener(listener);
		f.setFont(getInstance().getFont());
		//(int top, int right, int bottom, int left) 
		f.setMargin(getInstance().getTokenListVerticalMargin(), 0, getInstance().getTokenListVerticalMargin(), getInstance().getTokenListVerticalMargin());
		screen.add(f);
		return f;
	}
	
	/**
	 * Create an objectChoice field and add to the screen
	 * @param screen
	 * @param label object choice label
	 * @param listener FieldChangeListener
	 * @param choices Object[] choices
	 * @return
	 */
	public static ObjectChoiceField createChoiceField(MainScreen screen,
			String label, FieldChangeListener listener,
			Object[] choices) {
		ObjectChoiceField field = new ObjectChoiceField(label, choices);
		field.setChangeListener(listener);
		field.setFont(ScreenBuilder.getInstance().getFont());
		field.setMargin(getInstance().getTokenListVerticalMargin(), 0, getInstance().getTokenListVerticalMargin(), getInstance().getTokenListVerticalMargin());
		screen.add(field);
		return field;
	}

	/**
	 * Display the "New token saved" dialog
	 * @param t Token saved
	 */
	public static void showNewTokenSavedDialog(TokenData t) {
		Dialog.inform("[" +t.getAlias() + "]\n" + Localization.newTokenSavedDialog());		
	}

	/**
	 * Refresh the startup token list
	 */
	public static void refreshTokenListScreen() {
		Screen s = MainClass.getUiApplication().getActiveScreen();
		
		if (s instanceof StartupScreen) {
			((StartupScreen) s).refreshTokenList();
		}
	}


}
