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

import net.rim.blackberry.api.mail.Message;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.component.TextField;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.decor.BackgroundFactory;
import net.rim.device.api.ui.text.TextFilter;
import ch.garl.i18n.Localization;
import ch.garl.mail.MailImporterListener;
import ch.garl.persistent.Storage;
import ch.garl.persistent.TokenData;
import ch.garl.ui.component.Header;

/**
 * EditToken screen
 * @author Emanuele Gambaro
 */
public class EditTokenScreen extends MainScreen implements FieldChangeListener {
	
	Header header;
	
	ObjectChoiceField type;
	LabelField title;
	TextField alias;
	TextField seed;
	TextField length;
	TextField step;
	
	TokenData savedToken;

	private MailImporterListener listener;

	private Message m;
	
	public EditTokenScreen(TokenData t) {
		super(MainScreen.VERTICAL_SCROLL | MainScreen.VERTICAL_SCROLLBAR);
		header = new Header();
		add(header);
		
		title = new LabelField(Localization.editScreenTitle(),LabelField.HCENTER | Field.FIELD_BOTTOM  | Field.USE_ALL_WIDTH) {
			protected void paint(Graphics graphics) {
				graphics.setColor(Color.WHITE);
				super.paint(graphics);
			};
		};
		
		title.setBackground(BackgroundFactory.createSolidBackground(0x398AFF));
		
		add(title);
		add(new SeparatorField() {
			protected void paint(Graphics graphics) {

			}

			public int getPreferredHeight() {
				return 5;
			}
		});
		
		type =  ScreenBuilder.createChoiceField(this, Localization.editScreenTokenTypeField(), this, new Object[] {
			"TOTP", "HOTP"
		});
		add(new SeparatorField());
		alias = ScreenBuilder.createTextField(this, Localization.editScreenAliasField(),this,TextFilter.DEFAULT);
		add(new SeparatorField());
		seed = ScreenBuilder.createTextField(this, Localization.editScreenSeedField(),this,TextFilter.HEXADECIMAL);
		seed.setMaxSize(TokenData.DEFAULT_SEED_LENGTH);
		add(new SeparatorField());
		length = ScreenBuilder.createTextField(this, Localization.editScreenLengthField(),this,TextFilter.INTEGER);
		length.setText(Integer.toString(TokenData.DEFAULT_PASSWORD_LENGTH));
		add(new SeparatorField());
		step = ScreenBuilder.createTextField(this, Localization.editScreenTotpStepField(),this,TextFilter.INTEGER);
		step.setText(Integer.toString(TokenData.DEFAULT_STEP));

		
		savedToken = t;
		if (savedToken != null)
			initField();
	}
	
	private void initField() {
		if (savedToken.getType() == TokenData.TYPE_TOTP) {
			type.setSelectedIndex(0);
			step.setLabel(Localization.editScreenTotpStepField() + " ");
		} else {
			type.setSelectedIndex(1);
			step.setLabel(Localization.editScreenHotpStepField() + " ");
		}
		
		alias.setText(savedToken.getAlias());
		
		if (!savedToken.isLockdown())
			seed.setText(savedToken.getSeed());
		else {
			seed.setFilter(TextFilter.get(TextFilter.DEFAULT));
			seed.setEditable(false);
			seed.setText(Localization.editScreenSeedLockdown());
		}
		
		length.setText(Integer.toString(savedToken.getLength()));
		step.setText(Integer.toString(savedToken.getStep()));
	}

	protected void makeMenu(Menu menu, int context) {
	        menu.add(new MenuItem(Localization.editTokenMenuSave(), 1, 1) {
	        	public void run() {
	        		if (onSave()) {
	        			UiApplication uiapp = UiApplication.getUiApplication();
	                    uiapp.popScreen(uiapp.getActiveScreen());
	        		} 
	        	}
	        });
	        
	 }
	 
	protected boolean keyChar(char c, int status, int time) {
		return super.keyChar(c, status, time);
	}
	
	public boolean onSave() {
		if (savedToken == null) {
			// create new tokenData();
			savedToken = new TokenData();
		}
		
		if (type.getSelectedIndex() == 0)
			savedToken.setType(TokenData.TYPE_TOTP);
		else
			savedToken.setType(TokenData.TYPE_HOTP);
		
		savedToken.setAlias(alias.getText());
		if (!savedToken.isLockdown())
			savedToken.setSeed(seed.getText());
		savedToken.setLength(Integer.parseInt(length.getText()));
		savedToken.setStep(Integer.parseInt(step.getText()));
		
		if (!savedToken.validate()) {
			Dialog d = new Dialog(Dialog.D_OK, Localization.validationError(),
					Dialog.OK, Bitmap.getPredefinedBitmap(Bitmap.EXCLAMATION), 0);
			d.show();
			return false;
		}
	
		// check for duplicate
		TokenData duplicateToken = Storage.getInstance().findToken(savedToken.getAlias());
		if (duplicateToken != null &&
			duplicateToken != savedToken) {
			Dialog d = new Dialog(Dialog.D_YES_NO, 
					Localization.editScreenDuplicateWarning(savedToken.getAlias()),
					Dialog.YES, Bitmap.getPredefinedBitmap(Bitmap.QUESTION), 0);
			int resp = d.doModal();
			
			if (resp == Dialog.NO)
				return false;
		}
		
		Storage.getInstance().addTokenData(savedToken);
		Storage.getInstance().save();
		
		ScreenBuilder.showNewTokenSavedDialog(savedToken);
		
		if(listener != null)
			listener.onMessageImported(m);
		
		return true;
	}

	public void fieldChanged(Field field, int context) {
		if (field == type) {
			if (type.getSelectedIndex() == 0) {
				step.setLabel(Localization.editScreenTotpStepField()+ " ");
			} else {
				step.setLabel(Localization.editScreenHotpStepField()+ " ");
			}
		}
	}
	    

	protected boolean onSavePrompt() {
		boolean needSave = super.onSavePrompt();
		return needSave;
	}
	
	public void addListener(MailImporterListener listener) {
		this.listener = listener;		
	}

	public void setMessage(Message m) {
		this.m = m;		
	}	
}
