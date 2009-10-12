package org.hhu.c2c.openlr.core;

import java.util.ArrayList;
import java.util.List;

import org.hhu.c2c.openlr.geo.Coordinate;
import org.hhu.c2c.openlr.l10n.Messages;
import org.hhu.c2c.openlr.util.Builder;
import org.hhu.c2c.openlr.util.LocationReferenceException;

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
	 * A counter to see, if the location reference was close properly by calling
	 * {@link #close(float, float, FunctionalRoadClass, FormOfWay, float)}
	 */
	private int closeCounter;

	/**
	 * Constructs a new {@link LocationReferenceBuilder} that helps building new
	 * location references.
	 */
	public LocationReferenceBuilder() {
		init();
	}

	/**
	 * Adds a new location reference point to the location reference.
	 * 
	 * @param point
	 *            a location reference point
	 * @return the same instance of this {@link LocationReferenceBuilder} for
	 *         use in a fluid interface
	 */
	public LocationReferenceBuilder addLocationReferencePoint(
			final LocationReferencePoint point) {
		points.add(point);
		return this;
	}

	/**
	 * Adds a new location reference point to the location reference.
	 * 
	 * @param longitude
	 *            the longitude of the coordinate
	 * @param latitude
	 *            the latitude of the coordinate
	 * @param frc
	 *            the functional road class
	 * @param fow
	 *            the form of way
	 * @param lfrcnp
	 *            the lowest functional road class to the next point
	 * @param bearing
	 *            the bearing the distance
	 * @param distance
	 *            the distance to the next point
	 * @return the same instance of this {@link LocationReferenceBuilder} for
	 *         use in a fluid interface
	 * @throws LocationReferenceException
	 *             if the angular measurements for the coordinates are misformed
	 *             or the distance doesn't follow the data rules
	 */
	public LocationReferenceBuilder addLocationReferencePoint(
			final float longitude, final float latitude,
			final FunctionalRoadClass frc, final FormOfWay fow,
			final FunctionalRoadClass lfrcnp, final float bearing,
			final int distance) throws LocationReferenceException {

		points.add(new LocationReferencePoint(Coordinate.newCoordinate(
				longitude, latitude), frc, fow, frc, new Bearing(bearing),
				Distance.newDistance(distance)));
		return this;
	}

	/**
	 * Adds the last location reference point
	 * 
	 * @param longitude
	 *            the longitude of the coordinate
	 * @param latitude
	 *            the latitude of the coordinate
	 * @param frc
	 *            the functional road class
	 * @param fow
	 *            the form of way
	 * @param bearing
	 *            the bearing the distance
	 * @return the same instance of this {@link LocationReferenceBuilder} for
	 *         use in a fluid interface
	 * @throws LocationReferenceException
	 *             if the angular measurements for the coordinates are misformed
	 */
	public LocationReferenceBuilder close(final float longitude,
			final float latitude, final FunctionalRoadClass frc,
			final FormOfWay fow, final float bearing)
			throws LocationReferenceException {

		closeCounter++;

		points.add(new LocationReferencePoint(Coordinate.newCoordinate(
				longitude, latitude), frc, fow, new Bearing(bearing)));
		return this;
	}

	/**
	 * Adds the last location reference point, ignoring the lowest functional
	 * road classs and distance if given by the point
	 * 
	 * @see LocationReferenceBuilder#close(float, float, FunctionalRoadClass,
	 *      FormOfWay, float)
	 * 
	 * @param point
	 *            the last location reference point
	 * 
	 * @return the same instance of this {@link LocationReferenceBuilder} for
	 *         use in a fluid interface
	 * @throws LocationReferenceException
	 *             if the angular measurements for the coordinates are misformed
	 */
	public LocationReferenceBuilder close(LocationReferencePoint point)
			throws LocationReferenceException {
		return close(point.getCoordinate().getLongitude(), point
				.getCoordinate().getLatitude(), point.getFunctionalRoadClass(),
				point.getFormOfWay(), point.getBearing().getBearing());
	}

	/**
	 * {@link Builder#build()}
	 */
	@Override
	public LocationReference build() throws LocationReferenceException {
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
		this.closeCounter = 0;
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
	public void validate() throws LocationReferenceException {
		if (points.size() < Rules.MINIMUM_NUMBER_OF_LR_POINTS) {
			throw new LocationReferenceException(
					Messages
							.getString(
									"LocationReferenceBuilder.Exception.MINIMUM_NUMBER_OF_POINTS", Rules.MINIMUM_NUMBER_OF_LR_POINTS)); //$NON-NLS-1$
		}

		if (attributeFlag != ATTRIBUTE_FLAG_DEFAULT) {
			throw new LocationReferenceException(
					Messages
							.getString("LocationReferenceBuilder.Exception.ATTRIBUTE_FLAG_IS_NOT_SUPPORTED")); //$NON-NLS-1$
		}

		if (areaFlag != AREA_FLAG_DEFAULT) {
			throw new LocationReferenceException(
					Messages
							.getString("LocationReferenceBuilder.Exception.AREA_FLAG_IS_NOT_SUPPORTED")); //$NON-NLS-1$
		}

		if (version != VERSION_NUMBER_DEFAULT) {
			throw new LocationReferenceException(
					Messages
							.getString("LocationReferenceBuilder.Exception.PROTOCOL_VERSION_NOT_SUPPORTED")); //$NON-NLS-1$
		}

		LocationReferencePoint lastPoint = points.get(points.size() - 1);
		if (lastPoint.getDistanceToNextPoint() != null
				&& lastPoint.getDistanceToNextPoint().getDistance() != 0) {
			throw new LocationReferenceException(
					Messages
							.getString("LocationReferenceBuilder.Exception.LAST_POINT_NO_DISTANCE")); //$NON-NLS-1$
		}

		if (lastPoint.getLowestFRCToNextPoint() != null) {
			throw new LocationReferenceException(
					Messages
							.getString("LocationReferenceBuilder.Exception.LAST_POINT_NO_LFRCNP")); //$NON-NLS-1$
		}

		if (closeCounter == 0) {
			throw new LocationReferenceException(
					Messages
							.getString("LocationReferenceBuilder.Exception.LAST_POINT_MISSING")); //$NON-NLS-1$
		}

		if (closeCounter > 1) {
			throw new LocationReferenceException(
					Messages
							.getString("LocationReferenceBuilder.Exception.MULTIPLE_LAST_POINTS")); //$NON-NLS-1$
		}

	}
}
