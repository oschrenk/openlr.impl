package org.hhu.c2c.openlr.core;

import org.hhu.c2c.openlr.l10n.Messages;

/**
 * The <b>form of way</b> (<code>FOW</code>) describes the physical road type.
 * 
 * @author Oliver Schrenk <oliver.schrenk@uni-duesseldorf.de>
 * @version %I%, %G%
 * 
 */
public enum FormOfWay {

	/**
	 * The physical road type is unknown.
	 */
	UNDEFINED(0, Messages.getString("FormOfWay.Text.UNDEFINED")), //$NON-NLS-1$

	/**
	 * A Motorway is defined as a road permitted for motorized vehicles only in
	 * combination with a prescribed minimum speed. It has two or more
	 * physically separated carriageways and no single level-crossings.
	 */
	MOTORWAY(1, Messages.getString("FormOfWay.Text.MOTORWAY")), //$NON-NLS-1$

	/**
	 * A multiple carriageway is defined as a road with physically separated
	 * carriageways regardless of the number of lanes. If a road is also a
	 * motorway, it should be coded as such and not as a multiple carriageway.
	 */
	MULTIPLE_CARRIAGEWAY(2, Messages.getString("FormOfWay.Text.MULTIPLE_CARRIAGEWAY")), //$NON-NLS-1$

	/**
	 * All roads without separate carriageways are considered as roads with a
	 * single carriageway.
	 */
	SINGLE_CARRIAGEWAY(3, Messages.getString("FormOfWay.Text.SINGLE_CARRIAGEWAY")), //$NON-NLS-1$

	/**
	 * A Roundabout is a road which forms a ring on which traffic travelling in
	 * only one direction is allowed.
	 */
	ROUNDABOUT(4, Messages.getString("FormOfWay.Text.ROUNDABOUT")), //$NON-NLS-1$

	/**
	 * A Traffic Square is an open area (partly) enclosed by roads which is used
	 * for non-traffic purposes and which is not a Roundabout.
	 */
	TRAFFICSQUARE(5, Messages.getString("FormOfWay.Text.TRAFFICSQUARE")), //$NON-NLS-1$

	/**
	 * A Slip Road is a road especially designed to enter or leave a line.
	 */
	SLIPROAD(6, Messages.getString("FormOfWay.Text.SLIPROAD")), //$NON-NLS-1$

	/**
	 * The physical road type is known but does not fit into one of the other
	 * categories.
	 */
	OTHER(7, Messages.getString("FormOfWay.Text.OTHER")); //$NON-NLS-1$

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
	 * Creates a new form of way with the passed byte. Only the three least
	 * significant bits are used.
	 * 
	 * @param fow
	 *            the byte containing the form of way encoded as the last three
	 *            significant bits of a byte
	 * @return the form of way corrsesponding to the byte value of the three
	 *         least significant bits
	 */
	public static FormOfWay getFormOfWay(byte fow) {
		fow = (byte) (fow & THREE_BIT_BITMASK);
		switch (fow) {
		case 0:
			return UNDEFINED;
		case 1:
			return MOTORWAY;
		case 2:
			return MULTIPLE_CARRIAGEWAY;
		case 3:
			return SINGLE_CARRIAGEWAY;
		case 4:
			return ROUNDABOUT;
		case 5:
			return TRAFFICSQUARE;
		case 6:
			return SLIPROAD;
		case 7:
			return OTHER;
		default:
			throw new RuntimeException(Messages
					.getString("General.Error.GURU_MEDITATION_FAILURE")); //$NON-NLS-1$
		}
	}

	/**
	 * Holds the byte value (the three least significant bits) of the form of
	 * way
	 */
	private byte formOfWay;

	/**
	 * Holds the name of the form of way
	 */
	private String name;

	/**
	 * Creating a new {@link FormOfWay}.
	 * 
	 * @param fow
	 *            the form of way
	 */
	FormOfWay(final int fow, final String name) {
		this.formOfWay = (byte) fow;
		this.name = name;
	}

	/**
	 * Returns the byte representation of the form of way. The <code>FOW</code>
	 * is encoded using three bits, storing 8 values from 0 to 7.
	 * 
	 * @return the byte representation using the three least significant bits
	 */
	public byte getByteRepresentation() {
		return formOfWay;
	}

	@Override
	public String toString() {
		return name;
	}
}
