package org.hhu.c2c.test.util;

/**
 * A variety of bit twiddling routines
 * 
 * @author Oliver Schrenk <oliver.schrenk@uni-duesseldorf.de>
 * @version %I%, %G%
 * 
 */
public class BitUtils {

	/**
	 * Returns the given byte (array) as a string of 0s and 1s, whereas each
	 * nibble, as well the byte can be seperated by different strings.
	 * 
	 * @param nibbleSeperator
	 *            a string that seperates a nibble
	 * @param byteSeperator
	 *            a string that serpates a byte
	 * @param data
	 *            the geiven byte (array)
	 * @return a bit encoded reprentation of 0s and1 of the given byte (array)
	 */
	public static String toString(String nibbleSeperator, String byteSeperator,
			byte... data) {
		if (nibbleSeperator == null) {
			nibbleSeperator = "";
		}
		if (byteSeperator == null) {
			byteSeperator = "";
		}
		StringBuilder out = new StringBuilder();
		for (int i = 0; i < data.length; ++i) {
			for (int j = 7; j >= 0; --j) {
				out.append((data[i] >>> j) & 1);
				if (j % 4 == 0)
					out.append(nibbleSeperator);
			}
			out.append(byteSeperator);
		}
		return out.toString();
	}

	/**
	 * A convenience method to {@link #toString(String, String, byte...)} that
	 * returns the given byte (array) as a string of 0s and 1s, whereas each
	 * nibble is separated by a whitespace and each byte by a newline.
	 * 
	 * @param data
	 *            the geiven byte (array)
	 * @return a bit encoded reprentation of 0s and1 of the given byte (array)
	 */
	public static String toString(byte... data) {
		return toString(" ", "\n", data);
	}

}
