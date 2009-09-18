package org.hhu.c2c.openlr.core;

import java.util.ArrayList;
import java.util.List;

import org.hhu.c2c.openlr.l10n.Messages;
import org.hhu.c2c.openlr.util.Builder;
import org.hhu.c2c.openlr.util.ValidationException;

/**
 * The <b>location reference factory</b> is used for creating and receiving byte
 * arrays describing location references. It does so by following the physical
 * data structure and not imposing any further logic on it.
 * 
 * @author Oliver Schrenk <oliver.schrenk@uni-duesseldorf.de>
 * 
 */
public class LocationReferenceBuilder implements
		Builder<LocationReferenceBuilder, LocationReference> {

	/**
	 * Declares the default value for the area flag. As of right now, the
	 * protocol doesn't support location references describing areas, so this
	 * flag is by default <code>false</code>
	 * 
	 * @see #areaFlag
	 * @see LocationReference#hasAreaFlag()
	 */
	private static final boolean AREA_FLAG_DEFAULT = false;

	/**
	 * Declares the default value for the attribute flag. As of right now, the
	 * protocol only supports location references, that are describes with the
	 * additional data of attributes, so this is by default <code>true></code>
	 * 
	 * @see #attributeFlag
	 * @see LocationReference#hasAttributeFlag()
	 */
	private static final boolean ATTRIBUTE_FLAG_DEFAULT = true;

	/**
	 * As of right now the Open LR Standard has only one protocol. By definition
	 * the standard version number is <code>2</code>.
	 * 
	 * @see #version
	 * @see LocationReference#getVersion()
	 */
	protected static final byte VERSION_NUMBER_DEFAULT = 2;

	/**
	 * The <code>ArF</code> (<b>area flag</b>) indicates whether the location
	 * reference describes an area or not. If this flag is set then the location
	 * shall be connected and we describe an area. <br>
	 * <i>Note: The current physical data format supports only line locations so
	 * that the <code>ArF</code> is constantly set to false.</i>
	 * 
	 * @see LocationReference#hasAreaFlag()
	 */
	private boolean areaFlag;

	/**
	 * The <code>AF</code> (<b>attribute flag</b>) indicates whether there are
	 * attributes appended to each LR-point or not. The <code>AF</code> value is
	 * <code>false</code> if no attributes are appended and therefore the
	 * location reference only consists of coordinates. Otherwise a value of
	 * <code>true</code> indicates that attributes are appended to each
	 * LR-point. <br>
	 * <i>Note: Since the current version of the physical format supports only
	 * location references including attributes the <code>AF</code> flag will
	 * constantly be set to <code>true</code>. </i>
	 * 
	 * @see LocationReference#hasAttributeFlag()
	 **/
	private boolean attributeFlag;

	/**
	 * The <code>NOFF</code> (<b>negative offset</b>) value indicates the
	 * distance between the end of the location reference path and the “real”
	 * end of the location.
	 * 
	 * @see LocationReference#getNegativeOffset()
	 */
	private Distance negativeOffset;

	/**
	 * Holds a list of location reference points.
	 */
	private List<LocationReferencePoint> points;

	/**
	 * The <code>POFF</code> (<b>positive offset</b>) value indicates the
	 * distance between the start of the location reference path and the “real”
	 * start of the location.
	 * 
	 * @see LocationReference#getPositiveOffset()
	 */
	private Distance positiveOffset;

	/**
	 * The version is used to distinguish between several physical and data
	 * formats for location references. The version number is represented by 3
	 * bits. <br>
	 * <i>Note: The actual version of the physical data format is 2 so that the
	 * <code>VER</code> field is constantly set to binary 010.</i>
	 * 
	 * @see LocationReference#getVersion()
	 */
	private byte version;

	/**
	 * Constructs a new {@link LocationReferenceBuilder} that helps building new
	 * location references.
	 */
	public LocationReferenceBuilder() {
		init();
	}

	/**
	 * 
	 * @param point
	 * @return the same instance of this {@link LocationReferenceBuilder} for
	 *         use in a fluid interface
	 */
	public LocationReferenceBuilder addLocationReferencePoint(
			final LocationReferencePoint point) {
		points.add(point);
		return this;
	}

	/**
	 * {@link Builder#build()}
	 */
	@Override
	public LocationReference build() throws ValidationException {
		validate();
		return new LocationReference(areaFlag, attributeFlag, version, points,
				positiveOffset, negativeOffset);
	}

	/**
	 * Resets the the location reference
	 */
	private void init() {
		this.areaFlag = AREA_FLAG_DEFAULT;
		this.attributeFlag = ATTRIBUTE_FLAG_DEFAULT;
		this.version = VERSION_NUMBER_DEFAULT;
		this.points = new ArrayList<LocationReferencePoint>();
		this.positiveOffset = new Distance(0);
		this.negativeOffset = new Distance(0);
	}

	/**
	 * Sets the area flag
	 * 
	 * @see LocationReference#hasAreaFlag()
	 * 
	 * @param areaFlag
	 *            the new area flag
	 * @return the same instance of this {@link LocationReferenceBuilder} for
	 *         use in a fluid interface
	 */
	public LocationReferenceBuilder setAreaFlag(final boolean areaFlag) {
		this.areaFlag = areaFlag;
		return this;
	}

	/**
	 * Sets the attribute flag
	 * 
	 * @see LocationReference#hasAttributeFlag()
	 * 
	 * @param attributeFlag
	 *            the new attribute flag
	 * @return the same instance of this {@link LocationReferenceBuilder} for
	 *         use in a fluid interface
	 */
	public LocationReferenceBuilder setAttributeFlag(final boolean attributeFlag) {
		this.attributeFlag = attributeFlag;
		return this;
	}

	/**
	 * Sets the <b>negative offset value</b> (<code>NOFF</code>), indicating the
	 * distance between the start of the location reference path and the “real”
	 * start of the location.
	 * 
	 * @param negativeOffset
	 *            the negative offset
	 * @return the same instance of this {@link LocationReferenceBuilder} for
	 *         use in a fluid interface
	 */
	public LocationReferenceBuilder setNegativeOffset(
			final Distance negativeOffset) {
		this.negativeOffset = negativeOffset;
		return this;
	}

	/**
	 * A convenience methid to set the <b>negative offset value</b> (
	 * <code>NOFF</code>), indicating the distance between the start of the
	 * location reference path and the “real” start of the location.
	 * 
	 * @param negativeOffset
	 *            the negative offset
	 * @return the same instance of this {@link LocationReferenceBuilder} for
	 *         use in a fluid interface
	 */
	public LocationReferenceBuilder setNegativeOffset(final int negativeOffset) {
		return setNegativeOffset(new Distance(negativeOffset));
	}

	/**
	 * Sets the <b>positive offset value</b> (<code>POFF</code>), indicating the
	 * distance between the start of the location reference path and the “real”
	 * start of the location.
	 * 
	 * @param positiveOffset
	 *            the positive offset
	 * @return the same instance of this {@link LocationReferenceBuilder} for
	 *         use in a fluid interface
	 */
	public LocationReferenceBuilder setPositiveOffset(
			final Distance positiveOffset) {
		this.positiveOffset = positiveOffset;
		return this;
	}

	/**
	 * A convenience method the set the <b>positive offset value</b> (
	 * <code>POFF</code>), indicating the distance between the start of the
	 * location reference path and the “real” start of the location.
	 * 
	 * @param positiveOffset
	 *            the positive offset in meter
	 * @return the same instance of this {@link LocationReferenceBuilder} for
	 *         use in a fluid interface
	 */
	public LocationReferenceBuilder setPositiveOffset(final int positiveOffset) {
		return setPositiveOffset(new Distance(positiveOffset));
	}

	/**
	 * Sets the version number
	 * 
	 * @param version
	 *            the version number of the new location reference
	 * @return the same instance of this {@link LocationReferenceBuilder} for
	 *         use in a fluid interface
	 */
	public LocationReferenceBuilder setVersion(final byte version) {
		this.version = version;
		return this;
	}

	/**
	 * {@link Builder#reset()}
	 */
	@Override
	public LocationReferenceBuilder reset() {
		init();
		return this;
	}

	/**
	 * Validates the instance of the location reference that is currently being
	 * built.
	 */
	private void validate() throws ValidationException {
		// TODO move to a new class
		if (points.size() < Rules.MINIMUM_NUMBER_OF_LR_POINTS) {
			throw new ValidationException(
					Messages.getString("LocationReferenceBuilder.Exception.MINIMUM_NUMBER_OF_POINTS", Rules.MINIMUM_NUMBER_OF_LR_POINTS)); //$NON-NLS-1$
		}

		if (attributeFlag != ATTRIBUTE_FLAG_DEFAULT) {
			throw new ValidationException(Messages.getString("LocationReferenceBuilder.Exception.ATTRIBUTE_FLAG_IS_NOT_SUPPORTED")); //$NON-NLS-1$
		}

		if (areaFlag != AREA_FLAG_DEFAULT) {
			throw new ValidationException(Messages.getString("LocationReferenceBuilder.Exception.AREA_FLAG_IS_NOT_SUPPORTED")); //$NON-NLS-1$
		}

		if (version != VERSION_NUMBER_DEFAULT) {
			throw new ValidationException(Messages.getString("LocationReferenceBuilder.Exception.PROTOCOL_VERSION_NOT_SUPPORTED")); //$NON-NLS-1$
		}

		boolean malFormedPoints = false;
		for (LocationReferencePoint point : points) {
			// TODO check points
		}
		// TODO another method for last point?
	}

	/**
	 * {@link Builder#validates()}
	 */
	@Override
	public boolean validates() {
		try {
			validate();
		} catch (ValidationException e) {
			return false;
		}
		return true;
	}
}
