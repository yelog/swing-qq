package com.yyj.hide;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

/** */
/**
 *
 * Title: LoonFramework
 *
 * Description: ????bmp???????????????????
 *
 * Copyright: Copyright (c) 2007
 *
 * Company: LoonFramework
 *
 *
 * @author chenpeng
 * @email??ceponline@yahoo.com.cn
 * @version 0.1
 */
public class BitmapExecute {
	/** */
	/**
	 * ??BufferedImage????bmp????????????��??
	 *
	 * @param image
	 * @param file
	 * @return
	 */
	private static boolean saveBMP(BufferedImage image, File file) {
		// ??????bmp???

		Iterator writers = ImageIO.getImageWritersByFormatName("bmp");
		ImageWriter writer = (ImageWriter) writers.next();
		ImageOutputStream ios = null;
		try {
			ios = ImageIO.createImageOutputStream(new FileOutputStream(file));
		} catch (IOException ioe) {
			return false;
		}
		writer.setOutput(ios);
		try {
			writer.write(image);
		} catch (IOException ioe) {
			return false;
		}
		return true;
	}

	/** */
	/**
	 * ??????????????bmp?????
	 *
	 * @param dataFileName
	 * @param bmpFileName
	 * @param outFileName
	 * @return
	 * @throws IOException
	 */
	public static boolean DataSourceToBMP(String dataFileName,
										  String bmpFileName, String outFileName) throws IOException {
		return DataSourceToBMP(new File(dataFileName), new File(bmpFileName),
				outFileName);
	}

	/** */
	/**
	 * ??????????????bmp?????
	 *
	 * @param dataFileName
	 * @param bmpFileName
	 * @param outFileName
	 * @return
	 * @throws IOException
	 */
	public static boolean DataSourceToBMP(File dataFile, File bmpFile,
										  String outFileName) throws IOException {
		FileInputStream dataStream = new FileInputStream(dataFile);
		BufferedImage bmp;
		try {
			bmp = ImageIO.read(bmpFile);
		} catch (Exception ex) {
			return false;
		}
		if (dataStream.available() == 0) {
			return false;
		}
		int maxByteStorage = (bmp.getHeight() * bmp.getWidth() * 3) / 8;
		// bmp????????????????????????????????

		if (maxByteStorage < dataStream.available() + 500) {
			return false;
		}
		BitmapOutput bmpWriter = new BitmapOutput(bmp);
		int dataSize = dataStream.available();
		try {
			for (int u = 0; u < 500; u++) {
				bmpWriter.writeByte(dataSize);
			}
			// ???????????

			bmpWriter.writeByte(91);
			for (int u = 0; u < dataSize; u++) {
				int result = dataStream.read();
				if (result == 91) {
					bmpWriter.writeByte(123);
				} else if (result == 93) {
					bmpWriter.writeByte(125);
				} else {
					bmpWriter.writeByte(result);
				}
			}
			bmpWriter.writeByte(93);
		} catch (Exception ex) {
			ex.getStackTrace();
			return false;
		}
		try {
			File file = new File(outFileName);
			if (file.exists()) {
				file.delete();
			}
			// ????BufferedImage?bmp???

			saveBMP(bmpWriter.getBufferedImage(), new File(outFileName));
		} catch (Exception ex) {
			ex.getStackTrace();
			return false;
		}
		return true;
	}

	/** */
	/**
	 * ??bmp????��??????????(????????????????????????????????��)
	 *
	 * @param bmpFileName
	 * @param outFName
	 * @return
	 * @throws IOException
	 */
	public static boolean BMPToDataSource(String bmpFileName, String outFName)
			throws IOException {
		return BMPToDataSource(new File(bmpFileName), outFName);
	}

	/** */
	/**
	 * ??bmp????��??????????(????????????????????????????????��)
	 *
	 * @param bmpFile
	 * @param outFName
	 * @return
	 * @throws IOException
	 */
	public static boolean BMPToDataSource(File bmpFile, String outFName)
			throws IOException {
		BufferedImage image = ImageIO.read(bmpFile);
		BitmapInput bmpReader;
		try {
			bmpReader = new BitmapInput(image);
		} catch (Exception ex) {
			return false;
		}
		FileOutputStream outStream;
		try {
			File file = new File(outFName);
			if (!file.exists()) {
				file.createNewFile();
			}
			outStream = new FileOutputStream(file);
		} catch (Exception ex) {
			return false;
		}
		int dataSize = 0;
		int outByte = 0;
		int count = 0;
		try {
			for (int u = 0; u < 500; u++) {
				// ????????��??body??????????

				Object[] object = bmpReader.readByte(outByte);
				boolean header = Boolean.parseBoolean((String) object[0]);
				outByte = Integer.parseInt((String) object[1]);
				if (!header) {
					throw new Exception();
				}
				dataSize |= (int) (outByte << 8 * 3);
				if (u != 3) {
					dataSize >>= 8;
				}
			}
			for (int u = 0; u < dataSize; u++) {
				Object[] object = bmpReader.readByte(outByte);
				boolean header = Boolean.parseBoolean((String) object[0]);
				outByte = Integer.parseInt((String) object[1]);
				if (!header) {
					throw new Exception();
				}
				if (outByte == 93) {
					return true;
				}
				if (outByte == 91) {
					count += 1;
				}
				if (count > 0) {
					if (outByte == 123) {
						outStream.write(91);
					} else if (outByte != 91) {
						outStream.write(outByte);
					}
				}
			}
		} catch (Exception ex) {
			return false;
		} finally {
			try {
				outStream.flush();
				outStream.close();
				outStream = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return true;
	}
}
