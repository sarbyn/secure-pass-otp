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

import ch.garl.persistent.TokenData;

/**
 * @author Emanuele Gambaro
 */
public class TokenFactory {

	public static IToken CreateToken(TokenData t){
		if (t.getType() == TokenData.TYPE_TOTP)
			return CreateTotpToken(t);
		
		return CreateHotpToken(t);
	}
	
	private static IToken CreateHotpToken(TokenData t){
		return new HotpToken(t.getSeed(), t.getStep(), t.getLength());
	}
	
	private static IToken CreateTotpToken(TokenData t){
		return new TotpToken(t.getSeed(), t.getStep(), t.getLength());
	}
}
