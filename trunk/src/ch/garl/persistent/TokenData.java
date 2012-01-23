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
package ch.garl.persistent;

import net.rim.device.api.util.Persistable;

/**
 * TokenData class
 * @author Emanuele Gambaro
 */
public class TokenData  implements Persistable {
	private String alias = "";
	private String seed = "";
	private int step   = DEFAULT_STEP;
	private int length = DEFAULT_PASSWORD_LENGTH;
	private int type   = TYPE_TOTP;
	private boolean lockdown = false;
	
	public static final int TYPE_HOTP = 0;
	public static final int TYPE_TOTP = 1;
	
	private static final String TYPE_HOTP_STRING = "HOTP";

	public static final int DEFAULT_STEP = 60;
	public static final int DEFAULT_PASSWORD_LENGTH = 6;
	public static final int DEFAULT_SEED_LENGTH = 40;


	public TokenData(String alias, String seed, int step, int length,
			int type, boolean lockdown) {
		super();
		this.alias = alias;
		this.seed = seed;
		this.step = step;
		this.length = length;
		this.type = type;
		this.lockdown = lockdown;
	}
	
	public TokenData() {
		super();
	}

	public String getAlias() {
		return alias;
	}

	public String getSeed() {
		return seed.toUpperCase();
	}

	public int getStep() {
		return step;
	}

	public int getLength() {
		return length;
	}

	public int getType() {
		return type;
	}
	
	public boolean isLockdown() {
		return lockdown;
	}
 
	public void setAlias(String alias) {
		this.alias = alias;
	}

	public void setSeed(String seed) {
		this.seed = seed;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public void setStep(String step) {
		try { this.step = Integer.parseInt(step); } catch (NumberFormatException e) {}
	}
	
	public void setLength(int length) {
		this.length = length;
	}
	
	public void setLength(String length) {
		try { this.length = Integer.parseInt(length); } catch (NumberFormatException e) {}
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public String toString() {
		return "Seed :" + seed + "\n" + 
		"alias : " + alias + "\n" + 
		"type :" + type;
	}

	/**
	 * Validate the token data
	 * @return true if the data are valid, false otherwise
	 */
	public boolean validate() {
		if (alias.length() < 1) {
    		return false;
    	}
    	
    	if (seed.length() != TokenData.DEFAULT_SEED_LENGTH) {
    		return false;
    	}
    	
    	if (length < 1 ||
    		step < 1)
    		return false;
		
    	return true;
	}
	
	/**
	 * Set token type
	 * @param stringType Type (as string) from provisioning Message
	 */
	public void setType(String stringType) {
		if ( stringType != null && stringType.equals(TYPE_HOTP_STRING)) {
			type = TYPE_HOTP;
		}
		
		type = TYPE_TOTP;
	}

	/**
	 * Set lockdown option
	 * @param lockdownString "true" or "false" (as string) from provisioning Message
	 */
	public void setLockdown(String lockdownString) {
		if (lockdownString == null) {
			lockdown = false;
			return;
		}
		
		if (lockdownString.toLowerCase().equals("true")) {
			lockdown = true;
		} else {
			lockdown = false;
		}
	}	
	
}
