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

import net.rim.device.api.system.EventLogger;

/**
 * Debug logger linked to EventLogger 
 * @author Emanuele Gambaro
 */
public class Logger {
	
	private static long EVENTLOGGER_ID = 0xd4ab929390cf0803L;
	
	static {
		EventLogger.register(EVENTLOGGER_ID,"SecurePassLogger",EventLogger.VIEWER_STRING);
	}

	public static void log(String message) {
		 System.out.println(message);
		 EventLogger.logEvent(EVENTLOGGER_ID, message.getBytes());
	}
	
}
