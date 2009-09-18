package org.hhu.c2c.openlr.core;

import org.hhu.c2c.openlr.l10n.Messages;

/**
 * The <b>functional road class</b> (<code>FRC</code>) is a road classification
 * based on the importance of a road. The possible values cover the range of
 * navigable roads from highest to lowest importance. If there are fewer or more
 * FRC values defined in the encoder map, or decoder map respectively, than
 * these 8 values used for location referencing, then a proper mapping needs to
 * be done or less important classes needs to be ignored.
 * 
 * @author Oliver Schrenk <oliver.schrenk@uni-duesseldorf.de>
 * 
 */
public enum FunctionalRoadClass {

	/**
	 * Main Road
	 */
	MAIN_ROAD(0, Messages.getString("FunctionalRoadClass.Text.MAIN_ROAD")), //$NON-NLS-1$

	/**
	 * First class road
	 */
	FIRST_CLASS_ROAD(1, Messages.getString("FunctionalRoadClass.Text.FIRST_CLASS_ROAD")), //$NON-NLS-1$

	/**
	 * Second class road
	 */
	SECOND_CLASS_ROAD(2, Messages.getString("FunctionalRoadClass.Text.SECOND_CLASS_ROAD")), //$NON-NLS-1$

	/**
	 * Third class road
	 */
	THIRD_CLASS_ROAD(3, Messages.getString("FunctionalRoadClass.Text.THIRD_CLASS_ROAD")), //$NON-NLS-1$

	/**
	 * Fourth class road
	 */
	FOURTH_CLASS_ROAD(4, Messages.getString("FunctionalRoadClass.Text.FOURTH_CLASS_ROAD")), //$NON-NLS-1$

	/**
	 * Fifth class road
	 */
	FIFTH_CLASS_ROAD(5, Messages.getString("FunctionalRoadClass.Text.FIFTH_CLASS_ROAD")), //$NON-NLS-1$

	/**
	 * Sixth class road
	 */
	SIXTH_CLASS_ROAD(6, Messages.getString("FunctionalRoadClass.Text.SIXTH_CLASS_ROAD")), //$NON-NLS-1$

	/**
	 * Other class road
	 */
	OTHER_CLASS_ROAD(7, Messages.getString("FunctionalRoadClass.Text.OTHER_CLASS_ROAD")); //$NON-NLS-1$

	/**
	 * Describes a bitmask, masking the three least significant bits:
	 * "0000 0100"
	 */
	private static final byte THREE_BIT_BITMASK = 7;

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
	 * @param frc
	 *            the byte containing the functional road class encoded as the
	 *            last three significant bits of a byte
	 * @return the functional road class corresponding to the byte value of the
	 *         three least significant bits
	 */
	public static FunctionalRoadClass getFunctionalRoadClass(byte frc) {
		frc = (byte) (frc & THREE_BIT_BITMASK);

		switch (frc) {
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
			throw new RuntimeException(Messages
					.getString("General.Error.GURU_MEDITATION_FAILURE")); //$NON-NLS-1$
		}
	}

	/**
	 * Holds the byte value (the three least significant bits) of the functional
	 * road class
	 */
	private final byte functionalRoadClass;

	/**
	 * Holds the name of the functional road class
	 */
	private final String name;

	/**
	 * Creating a new {@link FunctionalRoadClass}. The lower the number, the
	 * more important the road
	 * 
	 * @param frc
	 *            the importance of the road, the lower, the more important,
	 *            ranging from 0 to 7
	 */
	FunctionalRoadClass(final int frc, final String name) {
		this.functionalRoadClass = (byte) (frc);
		this.name = name;
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

	@Override
	public String toString() {
		return name;
	}

}
