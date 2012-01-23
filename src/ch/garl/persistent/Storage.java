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

import java.util.Vector;

import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;

/**
 * Persistent storage class
 * All the token data are saved in a Vector object and used in a 
 * PersistentObject instance
 * @author Emanuele Gambaro
 */
public class Storage {

	private PersistentObject persistentStorage; 
	private Vector tokens; 
	
	private static final long PERSISTENT_KEY = 0x863d12963ce85bd1L;
	private static Storage instance;
	
	private Storage() {
		persistentStorage = PersistentStore.getPersistentObject( PERSISTENT_KEY );
		tokens = (Vector) persistentStorage.getContents();
		
		if (tokens == null) {
			tokens = new Vector();
			persistentStorage.setContents(tokens);
			save();
		}
		
	}
	
	public static Storage getInstance() {
		if (instance == null) {
			instance = new Storage();
		}
		return instance;
	}
	
	/**
	 * Save token data in a system persistent object
	 */
	public void save() {
		persistentStorage.setContents(tokens);
		persistentStorage.commit();
		
	}
	
	/**
	 * Get all token data
	 * @return Vector with all the tokens (in TokenData object)
	 */
	public Vector getAllTokens() {
		return tokens;
	}
	
	
	/**
	 * Add a TokenData object to the storage and save
	 * @param t TokenData
	 */
	public void addTokenData(TokenData t) {
		TokenData oldData = findToken(t.getAlias());
		if (oldData != null) {
			tokens.removeElement(oldData);
		}
		
		if (!tokens.contains(t))
			tokens.addElement(t);
				
		save();
	}
	
	/**
	 * Remove a saved token and save
	 * @param index TokenData index in Vector object
	 */
	public void removeTokenData(int index) {
		tokens.removeElementAt(index);
		save();
	}
	
	/**
	 * Find a token data
	 * @param alias Search data
	 * @return TokenData object if the token exists, null otherwise
	 */
	public TokenData findToken(String alias) {
		for (int i = 0; i != tokens.size(); i++) {
			TokenData t = (TokenData) tokens.elementAt(i);
			if (t.getAlias().equals(alias)) return t;
		}
		
		return null;
	}
	
}
