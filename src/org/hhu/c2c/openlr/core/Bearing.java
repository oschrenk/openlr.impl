package org.hhu.c2c.openlr.core;

/**
 * The <b>bearing</b> (<code>BEAR</code>) describes the angle between the true
 * North and a line which is defined by the coordinate of the LR-point and a
 * coordinate which is BEARDIST along the line defined by the LR- point
 * attributes. If the line length is less than BEARDIST then the opposite point
 * of the line is used (regardless of BEARDIST). The bearing is measured in
 * degrees and always positive (measuring clockwise from North).
 * 
 * The physical data format defines 32 sectors whereby each sector covers 11.25°
 * of the circle. These 32 sectors are represented by 5 bits. The sectors are
 * counted clockwise.
 * 
 * <table>
 * <tr>
 * <th>Value</th>
 * <th>Sector</th>
 * </tr>
 * <tr>
 * <td>0</td>
 * <td>000.00&#xB0; &lt;= x &lt; 011.25&#xB0;</td>
 * </tr>
 * <tr>
 * <td>1</td>
 * <td>011.25&#xB0; &lt;= x &lt; 022.50&#xB0;</td>
 * </tr>
 * <tr>
 * <td>2</td>
 * <td>022.50&#xB0; &lt;= x &lt; 033.75&#xB0;</td>
 * </tr>
 * <tr>
 * <td>3</td>
 * <td>033.75&#xB0; &lt;= x &lt; 045.00&#xB0;</td>
 * </tr>
 * <tr>
 * <td>4</td>
 * <td>045.00&#xB0; &lt;= x &lt; 056.25&#xB0;</td>
 * </tr>
 * <tr>
 * <td>5</td>
 * <td>056.25&#xB0; &lt;= x &lt; 067.50&#xB0;</td>
 * </tr>
 * <tr>
 * <td>6</td>
 * <td>067.50&#xB0; &lt;= x &lt; 078.75&#xB0;</td>
 * </tr>
 * <tr>
 * <td>7</td>
 * <td>078.75&#xB0; &lt;= x &lt; 090.00&#xB0;</td>
 * </tr>
 * <tr>
 * <td>8</td>
 * <td>090.00&#xB0; &lt;= x &lt; 101.25&#xB0;</td>
 * </tr>
 * <tr>
 * <td>9</td>
 * <td>101.25&#xB0; &lt;= x &lt; 112.50&#xB0;</td>
 * </tr>
 * <tr>
 * <td>10</td>
 * <td>112.50&#xB0; &lt;= x &lt; 123.75&#xB0;</td>
 * </tr>
 * <tr>
 * <td>11</td>
 * <td>123.75&#xB0; &lt;= x &lt; 135.00&#xB0;</td>
 * </tr>
 * <tr>
 * <td>12</td>
 * <td>135.00&#xB0; &lt;= x &lt; 146.25&#xB0;</td>
 * </tr>
 * <tr>
 * <td>13</td>
 * <td>146.25&#xB0; &lt;= x &lt; 157.50&#xB0;</td>
 * </tr>
 * <tr>
 * <td>14</td>
 * <td>157.50&#xB0; &lt;= x &lt; 168.75&#xB0;</td>
 * </tr>
 * <tr>
 * <td>15</td>
 * <td>168.75&#xB0; &lt;= x &lt; 180.00&#xB0;</td>
 * </tr>
 * <tr>
 * <td>16</td>
 * <td>180.00&#xB0; &lt;= x &lt; 191.25&#xB0;</td>
 * </tr>
 * <tr>
 * <td>17</td>
 * <td>191.25&#xB0; &lt;= x &lt; 202.50&#xB0;</td>
 * </tr>
 * <tr>
 * <td>18</td>
 * <td>202.50&#xB0; &lt;= x &lt; 213.75&#xB0;</td>
 * </tr>
 * <tr>
 * <td>19</td>
 * <td>213.75&#xB0; &lt;= x &lt; 225.00&#xB0;</td>
 * </tr>
 * <tr>
 * <td>20</td>
 * <td>225.00&#xB0; &lt;= x &lt; 236.25&#xB0;</td>
 * </tr>
 * <tr>
 * <td>21</td>
 * <td>236.25&#xB0; &lt;= x &lt; 247.50&#xB0;</td>
 * </tr>
 * <tr>
 * <td>22</td>
 * <td>247.50&#xB0; &lt;= x &lt; 258.75&#xB0;</td>
 * </tr>
 * <tr>
 * <td>23</td>
 * <td>258.75&#xB0; &lt;= x &lt; 270.00&#xB0;</td>
 * </tr>
 * <tr>
 * <td>24</td>
 * <td>270.00&#xB0; &lt;= x &lt; 281.25&#xB0;</td>
 * </tr>
 * <tr>
 * <td>25</td>
 * <td>281.25&#xB0; &lt;= x &lt; 292.50&#xB0;</td>
 * </tr>
 * <tr>
 * <td>26</td>
 * <td>292.50&#xB0; &lt;= x &lt; 303.75&#xB0;</td>
 * </tr>
 * <tr>
 * <td>27</td>
 * <td>303.75&#xB0; &lt;= x &lt; 315.00&#xB0;</td>
 * </tr>
 * <tr>
 * <td>28</td>
 * <td>315.00&#xB0; &lt;= x &lt; 326.25&#xB0;</td>
 * </tr>
 * <tr>
 * <td>29</td>
 * <td>326.25&#xB0; &lt;= x &lt; 337.50&#xB0;</td>
 * </tr>
 * <tr>
 * <td>30</td>
 * <td>337.50&#xB0; &lt;= x &lt; 348.75&#xB0;</td>
 * </tr>
 * <tr>
 * <td>31</td>
 * <td>348.75&#xB0;&lt;= x &lt; 360.00&#xB0;</td>
 * </tr>
 * </table>
 * 
 * 
 * @author Oliver Schrenk <oliver.schrenk@uni-duesseldorf.de>
 * @version %I%, %G%
 * 
 */
public class Bearing {
	// TODO add function to calculate bearing as tex to javadoc

	/** Defines one thirty-second of a full circle */
	private static final float ONE_32TH_CIRCLE = 11.25f;

	/**
	 * Describes a bitmask masking the least five significant bits: "0001 1111"
	 */
	private static final byte BITMASK = 1 + 2 + 4 + 8 + 16;

	/**
	 * {@link #getBearing()}
	 */
	private float bearing;

	/**
	 * Creates a new bearing from the given degree. This degree can have a value
	 * between 0&#xB0; and 360&#xB0;. In fact it can be any value as the bearing
	 * is normalized (mod 360&#xB0; and adding 360&#xB0; if the value is less
	 * than 0&#xB0;)
	 * 
	 * @param degree
	 *            the angle between the true north and the road, counting
	 *            clockwise
	 */
	public Bearing(float degree) {
		this.bearing = normalize(degree);
	}

	/**
	 * Returns the bearing as the regular float value
	 * 
	 * @return the bearing
	 */
	public float getBearing() {
		return bearing;
	}

	/**
	 * Returns the byte representation of the bearing. The <code>FRC</code> is
	 * encoded using five bit, storing 32 values from 0 to 31.
	 * 
	 * //TODO add tex formula for calculation of bearing
	 * 
	 * @return the byte representation using the five least significant bits
	 */
	public byte getByteRepresentation() {
		return (byte) (getBearing(bearing) & BITMASK);
	}

	/**
	 * Normalizing the degree value by calculating modulo 360 and by adding 360,
	 * if the value is below 0, as we count clockwise.
	 * 
	 * @param degree
	 *            the degree
	 * @return normalized degree
	 */
	private float normalize(float degree) {
		degree = degree % 360;
		if (degree < 0) {
			degree = degree + 360.0f;
		}
		return degree;
	}

	/**
	 * Returns the byte value of the bearing using only the five least
	 * significant bits.
	 * 
	 * @param degree
	 *            the degree
	 * @return the byte value of the bearing using only the five least
	 *         significant bits.
	 */
	private byte getBearing(float degree) {
		// can only be between 0 and 31
		return (byte) (degree / ONE_32TH_CIRCLE);
	}

	/*
	 * Breaks "Single Responsibility Principle" as the class is now also,
	 * responsible for its own creation. But as this class is very static by
	 * contract (the protocol definition of Open LR won't change that fast), we
	 * can accept that. Also we prohibit casses outside this package to access
	 * the method.
	 */
	/**
	 * Returns a new Bearing for the given byte value. Only the five least
	 * significant bits are used
	 * 
	 * @param b
	 *            a byte,
	 * @return a new bearing
	 */
	public static Bearing newBearing(byte b) {
		return new Bearing((b & BITMASK) * ONE_32TH_CIRCLE);
	}
}
