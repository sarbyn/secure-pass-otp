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
package ch.garl.token;

import java.util.Calendar;
import java.util.TimeZone;

import net.rim.device.api.crypto.CryptoTokenException;
import net.rim.device.api.crypto.CryptoUnsupportedOperationException;


/**
 * TOTP Token
 * 
 * Generates an OTP based on the time, for more information see
 * http://tools.ietf.org/html/draft-mraihi-totp-timebased-00
 * 
 * @author Emanuele Gambaro
 */
public class TotpToken extends HotpToken {

	private int mTimeStep;
	
	public TotpToken(String seed, int timeStep, int otpLength){
		super(seed, 0, otpLength);
		
		mTimeStep = timeStep;
	}

	public String GenerateOtp() throws CryptoTokenException, CryptoUnsupportedOperationException {
		
		//calculate the moving counter using the time
		return GenerateOtp(Calendar.getInstance(TimeZone.getTimeZone("GMT")));	
	}
	
	public String GenerateOtp(Calendar currentTime) throws CryptoTokenException, CryptoUnsupportedOperationException{
		long time =currentTime.getTime().getTime()/1000;		
		super.setEventCount(time/mTimeStep);
		
		return super.GenerateOtp();
	}
	
	
}
