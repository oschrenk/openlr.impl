package org.hhu.c2c.openlr.core;

/**
 * The <b>functional road class</b> (<code>FRC</code>) is a road classification
 * based on the importance of a road. The possible values cover the range of
 * navigable roads from highest to lowest importance. If there are fewer or more
 * FRC values defined in the encoder map, or decoder map respectively, than
 * these 8 values used for location referencing, then a proper mapping needs to
 * be done or less important classes needs to be ignored.
 * 
 * @author Oliver Schrenk <oliver.schrenk@uni-duesseldorf.de>
 * @version %I%, %G%
 * 
 */
public enum FunctionalRoadClass {

	/**
	 * Main Road
	 */
	MAIN_ROAD(0),

	/**
	 * First class road
	 */
	FIRST_CLASS_ROAD(1),

	/**
	 * Second class road
	 */
	SECOND_CLASS_ROAD(2),

	/**
	 * Third class road
	 */
	THIRD_CLASS_ROAD(3),

	/**
	 * Fourth class road
	 */
	FOURTH_CLASS_ROAD(4),

	/**
	 * Fifth class road
	 */
	FIFTH_CLASS_ROAD(5),

	/**
	 * Sixth class road
	 */
	SIXTH_CLASS_ROAD(6),

	/**
	 * Other class road
	 */
	OTHER_CLASS_ROAD(7);

	/**
	 * Describes a bitmask, masking the three least significant bits:
	 * "0000 0100"
	 */
	private static final byte THREE_BIT_BITMASK = 7;

	/**
	 * Holds the byte value (the three least significant bits) of the functional
	 * road class
	 */
	private final byte functionalRoadClass;

	/**
	 * Creating a new {@link FunctionalRoadClass}. The lower the number, the
	 * more important the road
	 * 
	 * @param i
	 *            the importance of the road, the lower, the more important,
	 *            ranging from 0 to 7
	 */
	FunctionalRoadClass(int i) {
		this.functionalRoadClass = (byte) (i);
	}

	/**
	 * Returns the byte representation of the functional road class. The
	 * <code>FRC</code> is encoded using three bits, storing 8 values from 0 to
	 * 7.
	 * 
	 * @return the byte representation using the three least significant bits
	 */
	public byte getByteRepresentation() {
		return functionalRoadClass;
	}

	/*
	 * Breaks "Single Responsibility Principle" as the class is now also,
	 * responsible for its own creation. But as this class is very static by
	 * definition (it is an enum) and by contract (the protocol definition of
	 * Open LR won't change that fast), we can accept that.
	 */
	/**
	 * Creates a new functional road class with the passed byte. Only the three
	 * least significant bits are used.
	 * 
	 * @param b
	 *            the byte containing the functional road class encoded as the
	 *            last three significant bits of a byte
	 * @return the functional road class corrsesponding to the byte value of the
	 *         three least significant bits
	 */
	public static FunctionalRoadClass getFunctionalRoadClass(byte b) {
		b = (byte) (b & THREE_BIT_BITMASK);

		switch (b) {
		case 0:
			return FunctionalRoadClass.MAIN_ROAD;
		case 1:
			return FunctionalRoadClass.FIRST_CLASS_ROAD;
		case 2:
			return FunctionalRoadClass.SECOND_CLASS_ROAD;
		case 3:
			return FunctionalRoadClass.THIRD_CLASS_ROAD;
		case 4:
			return FunctionalRoadClass.FOURTH_CLASS_ROAD;
		case 5:
			return FunctionalRoadClass.FIFTH_CLASS_ROAD;
		case 6:
			return FunctionalRoadClass.SIXTH_CLASS_ROAD;
		case 7:
			return FunctionalRoadClass.OTHER_CLASS_ROAD;

		default:
			throw new RuntimeException("GuruMeditationFailure: Reached default case, which should never happen.");
		}
	}

}
