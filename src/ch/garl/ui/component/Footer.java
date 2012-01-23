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

import net.rim.device.api.system.Display;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import ch.garl.ui.ScreenBuilder;

/**
 * Footer information with a custm text
 * @author Emanuele Gambaro
 */
public class Footer extends Field {

	String footerText;
	Font f = ScreenBuilder.getInstance().getFooterFont();
	int verticalMargin = ScreenBuilder.getInstance().getFooterVerticalMargin();
	
	public Footer(String text) {
		footerText = text;
	}

	protected void layout(int width, int height) {
		setExtent(Display.getWidth(), f.getHeight() + (verticalMargin*2));
	}

	protected void paint(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawLine(0, 0, Display.getWidth(), 0);
		
		g.setFont(f);
		int startPosition = (Display.getWidth() - f.getAdvance(footerText)) /2;
		g.drawText(footerText, startPosition, verticalMargin);
	}


}
