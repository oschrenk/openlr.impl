package org.hhu.c2c.openlr.io;

import org.hhu.c2c.openlr.core.Distance;
import org.hhu.c2c.openlr.core.FormOfWay;
import org.hhu.c2c.openlr.core.FunctionalRoadClass;

/**
 * This class offers static definitions shared between the {@link Encoder} and
 * the {@link Decoder}
 * 
 * @author Oliver Schrenk <oliver.schrenk@uni-duesseldorf.de>
 * 
 */
public class PhysicalDataFormat {

	/**
	 * The area flag in the header byte uses bit 5:<code>0001 0000</code>
	 */
	protected static final byte AREA_FLAG_BITMASK = 16;

	/**
	 * The attribute flag in the header byte uses bit 4:<code>0000 1000</code>
	 */
	protected static final byte ATTRIBUTE_FLAG_BITMASK = 8;

	/**
	 * The three least significant bits are used in the first attribute to
	 * describe to form of way.
	 */
	protected static final byte FORM_OF_WAY_BITMASK = 1 + 2 + 4;

	/**
	 * The functional road class is encoded as aq 3 bit value, when transmitted
	 * over wire it uses bit 5-3 (in order from most to least significant bit).
	 * As the datatype uses the value 0 to 7 as a normalized internal value, the
	 * values have to be shifted 3 bits to the right when reading from the
	 * encoded byte.
	 */
	protected static final int FRC_BITSHIFT = 3;

	/**
	 * The fifth to third least significant bits represent the functional road
	 * class in the first attribute. In order to have the value of the
	 * functional road class in the range of 0 to 7, the bits are shifted to the
	 * three least significant bits.
	 */
	protected static final byte FUNCTIONAL_ROAD_CLASS_BITMASK = 1 + 2 + 4;

	/**
	 * The least funcional road to the next point is represented by the same
	 * three bit value and thus by the same underlying class
	 * FunctionalRoadClass, which uses a normalized vlaue 0 0 to 7. As the
	 * LFRCNP is encoded as biz 8 -8 (from most to least significant bit) the
	 * value is shifted 5 bits to the right when read from an encoded byte.
	 */
	protected static final int LFRCNP_BITSHIFT = 5;

	/**
	 * As the header uses 1 byte and the protcol demands at least two location
	 * reference points, the first one, as an absolute point using 9 bytes and
	 * the last one using 6 bytes.
	 */
	protected static final int MINIMUM_NUMBER_OF_BYTES = 16;

	/**
	 * The number of bytes used to describe the closing location reference
	 * point. It also makes use of the relative coordinate format (4 bytes), but
	 * as it is the last it only describes in one byte its own functional road
	 * class and form of way as the other location reference points, but the
	 * last byte is used to indicate if the location reference uses a positive
	 * offset (bit 6) or a negative offset (bit 5) and the last 5 bits for the
	 * bearing of the incoming line.
	 */
	protected static final int MINIMUM_NUMBER_OF_BYTES_FOR_LAST_LRP = 6;

	/**
	 * The bitmask used by the negative offset flag. The last location reference
	 * point send over the wire also includes information whether the location
	 * reference starts (or ends) with an offset. The flag uses the 3nd most
	 * significant bit.
	 */
	protected static final byte NEGATIVE_OFFSET_FLAG_BITMASK = 32;

	/**
	 * An angular measurement uses three bytes using big endian notation.
	 */
	protected static final int NUMBER_OF_BYTES_FOR_ABSOLUTE_ANGULAR_MEASUREMENT = 3;

	/**
	 * Number of bytes for an absolute coordinate, three bytes for the longitude
	 * and three bytes for latitude
	 */
	protected static final int NUMBER_OF_BYTES_FOR_ABSOLUTE_COORDINATE = 6;

	/**
	 * The number of bytes used to describe the first location reference point
	 * of a location reference, including an absolute coordinate represewnted by
	 * 6 bytes (3 bytes longitude, 3 bytes latitude), one byte describing
	 * {@link FunctionalRoadClass} (bit 5 to 3) as well as the {@link FormOfWay}
	 * (bit 2 to 0), followed by the second attribute describing the lowest FRC
	 * to the next point (bit 7 to 5) as well as the bearing (bit 4 to 0) and
	 * finally by the {@link Distance} to the next location reference point
	 * described by a complete byte.
	 */
	protected static final int NUMBER_OF_BYTES_FOR_ABSOLUTE_LRP = 9;

	/**
	 * Number of bytes needed for a relative coordinate part, like longitude or
	 * latitude
	 */
	protected static final int NUMBER_OF_BYTES_FOR_RELATIVE_ANGULAR_MEASUREMENT = 2;

	/**
	 * The coordinates of the following LR-points and the last LR-point are
	 * transmitted in a relative format and therefore each value (longitude and
	 * latitude) will use 2 bytes, each in big endian starting with the
	 * longitude.
	 */
	protected static final int NUMBER_OF_BYTES_FOR_RELATIVE_COORDINATE = 4;

	/**
	 * The number of bytes used to describe following location reference points
	 * with relative coordinate values. The relative coordinate is described by
	 * the first 4 bytes (2 for longitude, 2 for latitude). The following three
	 * bytes are used as in the first absolute location reference point.
	 */
	protected static final int NUMBER_OF_BYTES_FOR_RELATIVE_LRP = 7;

	/**
	 * The bitmask used by the positive offset flag. The last location reference
	 * point send over the wire also includes information whether the location
	 * reference starts (or ends) with an offset. The flag uses the 2nd most
	 * significant bit.
	 */
	protected static final byte POSITIVE_OFFSET_FLAG_BITMASK = 64;

	/**
	 * Used to inflate the integer value when converting a relative coordinate
	 * to its integer representation
	 */
	protected static final int RELATIVE_FORMAT_INT_MULTIPLIER = 100000;

	/**
	 * The resolution parameter is used to convert the float representation of
	 * the longiitude (or latitude) into the respective long representation
	 */
	protected static final byte RESOLUTION_PARAMETER = 24;

	/**
	 * The three least significant bits of the header byte represent the version
	 * number
	 */
	protected static final byte VERSION_NUMBER_BITMASK = 1 + 2 + 4;

	/**
	 * This class should not be instantiated
	 */
	private PhysicalDataFormat() {
	}

}
