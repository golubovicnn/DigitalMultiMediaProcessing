package itm.util;

/*******************************************************************************
 This file is part of the ITM course 2017
 (c) University of Vienna 2009-2017
 *******************************************************************************/

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;

/**
 * Utility class the implements some basic I/O methods.
 * 
 * example:
 * 
 * String data = IOUtil.readFile( "myfile.txt" ); // loads the UTF-8 encoded
 * contents from myfile.txt into the string
 */
public class IOUtil {

	/**
	 * Reads a (character UTF-8) file into a string.
	 */
	public static String readFile(File f) throws IOException, FileNotFoundException {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(f);
			return readFile(fis);
		} finally {
			if (fis != null)
				try {
					fis.close();
				} catch (Exception e0) {
					System.err.println("IOUtil ERROR: cannot close stream on " + f);
				}
			fis = null;
		}
	}

	/**
	 * Reads (character) data from the passed file input stream into a string.
	 */
	public static String readFile(FileInputStream stream) throws IOException, FileNotFoundException {
		return readFile(stream, "UTF-8");
	}

	/**
	 * Reads (character) data from the passed file input stream into a string.
	 * 
	 * @param stream
	 *            the file input stream
	 * @param charsetString
	 *            a character set identifier (see
	 *            http://java.sun.com/javase/6/docs
	 *            /api/java/nio/charset/Charset.html)
	 */
	public static String readFile(FileInputStream stream, String charsetString) throws IOException,
			FileNotFoundException {
		try {
			FileChannel fc = stream.getChannel();

			int sz = (int) fc.size();

			ByteBuffer bb = ByteBuffer.allocate(sz);
			bb.order(ByteOrder.nativeOrder());

			fc.read(bb);

			/**
			 * the selection of charSet is crucial for performance e.g.:
			 * ISO-8859-1 is 'faster' than cp1252 or ISO-8859-15 or ...
			 */
			Charset charset = Charset.forName(charsetString);

			CharsetDecoder decoder = charset.newDecoder();

			bb.rewind();

			CharBuffer cb = decoder.decode((ByteBuffer) bb);

			fc.close();

			return cb.toString();
		} finally {
			if (stream != null)
				try {
					stream.close();
				} catch (Exception e0) {
					System.err.println("IOUtil ERROR: cannot close stream");
				}
		}
	}

	/**
	 * Writes a StringBuffer to the passed file (using UTF-8 encoding).
	 * 
	 * @param outString
	 *            the stringbuffer to write
	 * @param outFile
	 *            the file to write to - will be overwritten if it already
	 *            exists.
	 */
	public static void writeFile(StringBuffer outString, File outFile) throws IOException {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(outFile);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, "UTF-8"));
			bw.write(outString.toString(), 0, outString.length());
			bw.close();
		} finally {
			if (fos != null)
				try {
					fos.close();
				} catch (IOException e0) {
					System.err.println("IOUtil ERROR: cannot close stream on " + outFile);
				}
		}
	}

}
