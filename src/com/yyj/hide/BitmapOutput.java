package com.yyj.hide;

import java.awt.image.BufferedImage;

/** */
/**
 * Title: LoonFramework
 *
 * Description:
 *
 * Copyright: Copyright (c) 2007
 *
 * Company: LoonFramework
 *
 * @author chenpeng
 * @email��ceponline@yahoo.com.cn
 * @version 0.1
 */
public class BitmapOutput {
	private BufferedImage _bmp;
	private int curX, curY, iRGB;
	private int bitsLeft;
	private int r, g, b;

	public BitmapOutput(BufferedImage bmp) {
		this._bmp = bmp;
		curX = curY = iRGB = 0;
		bitsLeft = (bmp.getHeight() * bmp.getWidth() * 3);
	}

	public BufferedImage getBufferedImage() {
		return _bmp;
	}

	public synchronized boolean writeByte(int body) {
		if (bitsLeft < 8)
			return false;
		int bits2Do = 8;
		for (; curX < _bmp.getWidth(); curX++) {
			if (curY >= _bmp.getHeight()) {
				curY = 0;
			}
			for (; curY < _bmp.getHeight(); curY++) {
				if (bits2Do == 0)
					return true;
				int rgb = _bmp.getRGB(curX, curY);
				// ת��Ϊr,g,b��ʽ

				r = (rgb & 0x00ff0000) >> 16;
				g = (rgb & 0x0000ff00) >> 8;
				b = (rgb & 0x000000ff);
				while (true) {
					int curBit = (body & 1);
					switch (iRGB) {
						case 0:
							r = (r & 0xFE);
							r |= curBit;
							break;
						case 1:
							g = (g & 0xFE);
							g |= curBit;
							break;
						case 2:
							b = (b & 0xFE);
							b |= curBit;
							break;
					}
					--bits2Do;
					--bitsLeft;
					body >>= 1;
					// ��ԭ

					rgb = (r << 16) | (g << 8) | b;
					// ����ע��

					_bmp.setRGB(curX, curY, rgb);
					if (iRGB == 2) {
						iRGB = 0;
						break;
					}
					iRGB++;
					if (bits2Do == 0)
						return true;
				}
			}
		}
		return true;
	}
}
