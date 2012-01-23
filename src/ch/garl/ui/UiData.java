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

import net.rim.device.api.system.Display;

/**
 * Define the different User Interface size according to device screen size
 * @author Emanuele Gambaro
 */
class UiData {

    /**
     * Main font height
     */
    public int fontHeightPixels;
    /**
     * Horizontal margin used in token list view
     */
    public int tokenListHorizontalMargin;
    /**
     * Vertical margin used in token list view
     */
    public int tokenListVerticalMargin;
    /**
     * Footer font height
     */    
    public int fontFooterPixels;
    /**
     * Footer veretical margin
     */
	public int footerVerticalMargin;
    
	private UiData(int fontHeightPixels, int fontFooterPixels,
			int tokenListHorizontalMargin, int tokenListVerticalMargin,
			int footerVerticalMargin) {
		this.fontHeightPixels = fontHeightPixels;
		this.tokenListVerticalMargin = tokenListHorizontalMargin;
		this.tokenListVerticalMargin = tokenListVerticalMargin;
		this.fontFooterPixels = fontFooterPixels;
		this.footerVerticalMargin = footerVerticalMargin;
	}
	
	// font, footer, horizontal_margin, vertical_margin
	public static final UiData RES_8520 = new UiData(15,12,6,3,2); //320x240: 8520
	public static final UiData RES_8100 = new UiData(15,12,6,3,2); //240x260: 8100 
	public static final UiData RES_9500 = new UiData(19,15,8,4,2); //360x480: 9500 
	public static final UiData RES_9000 = new UiData(17,14,8,3,3); //480x320: 9000 
	public static final UiData RES_9700 = new UiData(19,15,6,3,3); //480x360: 9700 - 8900
	public static final UiData RES_9900 = new UiData(25,20,8,4,4); //640x480: 9900
	public static final UiData RES_9810 = new UiData(25,20,8,4,4); //480x640: 9810
	public static final UiData RES_9860 = new UiData(25,20,8,4,4); //480x800: 9860
	
	/**
	 * Get the right UI size from device Display info
	 * @return an UiData with font and margin dimensions
	 */
	public static UiData getUiData() {
		
		int screenWidth = Display.getWidth();
        int screenHeight = Display.getHeight();
        
        if (screenWidth == 320 && screenHeight == 240)
        	return RES_8520;
        if (screenWidth == 240 && screenHeight == 260)
        	return RES_8100;
        if (screenWidth == 360 && screenHeight == 480)
        	return RES_9500;
        if (screenWidth == 480 && screenHeight == 320)
        	return RES_9000;
        if (screenWidth == 320 && screenHeight == 360)
        	return RES_9700;
        if (screenWidth == 640 && screenHeight == 480)
        	return RES_9900;
        if (screenWidth == 480 && screenHeight == 640)
        	return RES_9810;
        if (screenWidth == 480 && screenHeight == 800)
        	return RES_9860;
        
        return RES_9700;
	}
	
	

}