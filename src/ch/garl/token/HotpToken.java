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

import net.rim.device.api.crypto.CryptoTokenException;
import net.rim.device.api.crypto.CryptoUnsupportedOperationException;
import net.rim.device.api.crypto.HMAC;
import net.rim.device.api.crypto.HMACKey;
import net.rim.device.api.crypto.SHA1Digest;


/**
 * Hotp Token
 * 
 * This is an event based OATH token, for further details
 * see the RFC http://tools.ietf.org/html/rfc4226
 * @author Emanuele Gambaro
 */
public class HotpToken implements IToken {

	private String mSeed;
	private long mEventCount;
	private int mOtpLength;
		
	
	private static final int[] DIGITS_POWER
    // 0 1  2   3    4     5      6       7        8
    = {1,10,100,1000,10000,100000,1000000,10000000,100000000};

	
	public HotpToken(String seed, long eventCount, int otpLength){
		mSeed = seed;
		mEventCount = eventCount;
		mOtpLength = otpLength;
	}
	
	protected void setEventCount(long eventCount) {
		this.mEventCount = eventCount;
	}

	public String GenerateOtp() throws CryptoTokenException, CryptoUnsupportedOperationException {
		
		byte[] counter = new byte[8];
		long movingFactor = mEventCount;
		
		for(int i = counter.length - 1; i >= 0; i--){
			counter[i] = (byte)(movingFactor & 0xff);
			movingFactor >>= 8;
		}
		
		byte[] hash = hmacSha(stringToHex(mSeed), counter);
		int offset = hash[hash.length - 1] & 0xf;
		
		int otpBinary = ((hash[offset] & 0x7f) << 24)
						|((hash[offset + 1] & 0xff) << 16)
						|((hash[offset + 2] & 0xff) << 8)
						|(hash[offset + 3] & 0xff);
		
		int otp = otpBinary % DIGITS_POWER[mOtpLength];
		String result = Integer.toString(otp);
		
		
		while(result.length() < mOtpLength){
			result = "0" + result;
		}
		
		return result;		
	}

	private byte[] stringToHex(String hexInputString){
		
		byte[] bts = new byte[hexInputString.length() / 2];
		
		for (int i = 0; i < bts.length; i++) {
			bts[i] = (byte) Integer.parseInt(hexInputString.substring(2*i, 2*i+2), 16);
		}
		
		return bts;
	}

	private byte[] hmacSha(byte[] seed, byte[] counter) throws CryptoTokenException, CryptoUnsupportedOperationException {
		HMACKey key = new HMACKey( seed );
	    SHA1Digest digest = new SHA1Digest();
	    HMAC hMac = new HMAC( key, digest );

	    hMac.update( counter );
		   
	    byte[] macValue = hMac.getMAC();

	    return macValue;
	}

}
