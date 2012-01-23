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

package ch.garl.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;

import net.rim.device.api.io.LineReader;

/**
 * Class that represent the provisioning data parsed from a provisioning mail.
 * This is a "custom" implementation of an Hashtable
 * @author Emanuele Gambaro
 */
public class MailProvisioningData {

	private Hashtable hashtable;

	private static final String SEPARATOR = "=";

	public MailProvisioningData() {
		hashtable = new Hashtable();
	}
	
	/**
	 * Load the provisioning data
	 * @param stream InputStream from which read the provisiong data
	 * @throws IOException
	 */
	public final void load(InputStream stream) throws IOException {
		String line;

		LineReader reader = new LineReader(stream);
		byte[] data = null;
		int separatorIndex;
		
		// first line
		data = reader.readLine();
		line = new String(data);
		int index = line.indexOf(SEPARATOR);
		if (index != -1) // -1 == empty line or line without "separator" - skip it
			hashtable.put(line.substring(0, index), line.substring(index+1));
		
		while (reader.lengthUnreadData() > 0) {
			data = reader.readLine();
			line = new String(data);
			separatorIndex = line.indexOf(SEPARATOR);
			if (separatorIndex == -1) continue; 	 // -1 == empty line or line without "separator" - skip it
			hashtable.put(line.substring(0, separatorIndex), line.substring(separatorIndex+1));
		}
	}

	/**
	 * Get all the provisioning data
	 * @return an Hashtable <String,String>
	 */
	public final Hashtable getProperties() {
		return hashtable;
	}
	
	/**
	 * Get a single provisioning data from its key
	 * defined in MessageUtils class
	 * @param key one of "OTP-XXXX" key defined in MessageUtils class
	 * @return provisioing data
	 */
	public final Object get(String key) {
		return hashtable.get(key);
	}

}
