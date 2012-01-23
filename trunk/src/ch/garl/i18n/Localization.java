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
package ch.garl.i18n;

import net.rim.device.api.i18n.ResourceBundle;

/**
 * Utility class for i18n resources
 * @author Emanuele Gambaro
 */
public class Localization implements ch.garl.i18nResource {

	static ResourceBundle resources = ResourceBundle.getBundle(BUNDLE_ID, BUNDLE_NAME);

	public static String mainMenuDeleteToken() {
		return resources.getString(MAIN_SCREEN_MENU_DELETE_TOKEN);
	}
	
	public static String mainMenuEditToken() {
		return resources.getString(MAIN_SCREEN_MENU_EDIT_TOKEN);
	}
	
	public static String mainMenuNewToken() {
		return resources.getString(MAIN_SCREEN_MENU_NEW_TOKEN);
	}
	
	public static String mainMenuGeneratePassword() {
		return resources.getString(MAIN_SCREEN_MENU_GENERATE_PASSWORD);
	}
		
	public static String mainMenuAbout() {
		return resources.getString(MAIN_SCREEN_MENU_ABOUT);
	}
	
	public static String mainScreenFooter() {
		return resources.getString(MAIN_SCREEN_FOOTER);
	}
	
	public static String editTokenWithLockdownWarning() {
		return resources.getString(MAIN_SCREEN_EDIT_LOCKDOWN_WARNING_POPUP);
	}
	
	public static String aboutDialogVersion() {
		return resources.getString(ABOUT_DIALOG_VERSION);
	}
	
	public static String deleteTokenDialog() {
		return resources.getString(DELETE_TOKEN_CONFIRM_DIALOG);
	}
	
	public static String editTokenMenuSave() {
		return resources.getString(EDIT_TOKEN_MENU_SAVE);
	}
	
	public static String editScreenTitle() {
		return resources.getString(EDIT_SCREEN_TITLE);
	}
	
	public static String editScreenTokenTypeField() {
		return resources.getString(EDIT_SCREEN_TYPE_FIELD);
	}
	
	public static String editScreenAliasField() {
		return resources.getString(EDIT_SCREEN_ALIAS_FIELD);
	}
	
	public static String editScreenLengthField() {
		return resources.getString(EDIT_SCREEN_LENGTH_FIELD);
	}
	
	public static String editScreenSeedField() {
		return resources.getString(EDIT_SCREEN_SEED_FIELD);
	}
	
	public static String editScreenTotpStepField() {
		return resources.getString(EDIT_SCREEN_TOTP_STEP_FIELD);
	}
	
	public static String editScreenHotpStepField() {
		return resources.getString(EDIT_SCREEN_HOTP_STEP_FIELD);
	}
	
	public static String editScreenSeedLockdown() {
		return resources.getString(EDIT_TOKEN_SEED_LOCKDOWN);
	}

	public static String validationError() {
		return resources.getString(EDIT_SCREEN_VALIDATION_ERROR);
	}
	
	public static String editScreenDuplicateWarning(String aliasName) {
		return replaceParams(resources.getString(EDIT_SCREEN_DUPLICATE_WARNING),
				aliasName);
	}

	public static String passwordDialog() {
		return resources.getString(MAIN_SCREEN_PASSWORD_DIALOG);
	}
	
	public static String passwordErrorDialog() {
		return resources.getString(MAIN_SCREEN_ERROR_DIALOG);
	}

	public static String newSeedReceivedDialog() {
		return resources.getString(NEW_SEED_RECEIVED_POPUP);
	}

	public static String systemMenuImportMail() {
		return resources.getString(SYSTEM_MENU_IMPORT_MAIL);
	}

	public static String isNotSecurePassMail() {
		return resources.getString(IS_NOT_SECURE_PASS_MAIL);
	}
	
	public static String newTokenSavedDialog() {
		return resources.getString(NEW_TOKEN_SAVED);
	}

	private static String replaceParams(String original, String arg) {
		
		return original.substring(0, original.indexOf("%1")) + arg + 
		original.substring(original.indexOf("%1") + "%1".length());
	}
}
