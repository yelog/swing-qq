package com.yyj.hide;

import java.awt.image.BufferedImage;

/** */
/**
 * * Title: LoonFramework Description:
 *
 * Copyright: Copyright (c) 2007
 *
 * Company: LoonFramework
 *
 * @author chenpeng
 * @email��ceponline@yahoo.com.cn
 * @version 0.1
 */
public class BitmapInput {
	private BufferedImage _bmp;
	private int curX, curY, iRGB;
	private int bitsLeft;

	public BitmapInput(BufferedImage bmp) {
		curX = curY = iRGB = 0;
		this._bmp = bmp;
		bitsLeft = bmp.getHeight() * bmp.getWidth() * 3;
	}

	public BufferedImage getBufferedImage() {
		return _bmp;
	}

	public synchronized Object[] readByte(int body) {
		body = 0;
		if (bitsLeft < 8) {
			return new Object[] { "false", "0" };
		}
		int bit = 0;
		int bits2Do = 8;
		for (; curX < _bmp.getWidth(); curX++) {
			if (curY >= _bmp.getHeight())
				curY = 0;
			for (; curY < _bmp.getHeight(); curY++) {
				if (bits2Do == 0) {
					return new Object[] { "true", String.valueOf(body) };
				}
				int rgb = _bmp.getRGB(curX, curY);
				int r = (rgb & 0x00ff0000) >> 16;
				int g = (rgb & 0x0000ff00) >> 8;
				int b = (rgb & 0x000000ff);
				while (true) {
					switch (iRGB) {
						case 0:
							bit = (r & 1);
							break;
						case 1:
							bit = (g & 1);
							break;
						case 2:
							bit = (b & 1);
							break;
					}
					--bits2Do;
					--bitsLeft;
					body |= (int) (bit << 7);
					if (bits2Do != 0) {
						body >>= 1;
					}
					if (iRGB == 2) {
						iRGB = 0;
						break;
					}
					iRGB++;
					if (bits2Do == 0) {
						return new Object[] { "true", String.valueOf(body) };
					}
				}
			}
		}
		return new Object[] { "true", String.valueOf(body) };
	}
}
