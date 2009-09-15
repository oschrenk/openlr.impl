package org.hhu.c2c.openlr.core;

import java.util.List;

/**
 * A <b>location reference</b> is a description of a designated part of a
 * digital map or a sequence of geographical positions. For this description the
 * model of {@link LocationReferencePoint}s is used.
 * 
 * A location reference for line locations contains at least two LR-points but
 * there is no maximum number of LR-points defined. The location reference path
 * is the path in the digital map described by the LR-points and can be found by
 * a shortest-path calculation between each consecutive pair of LR- points.
 * 
 * @author Oliver Schrenk <oliver.schrenk@uni-duesseldorf.de>
 * @version 1.0, 2009-09-20
 * 
 */
public class LocationReference {

	/**
	 * Declares the default value for the area flag. As of right now, the
	 * protocol doesn't support location references describing areas, so this
	 * flag is by default <code>false</code>
	 */
	private static final boolean AREA_FLAG_DEFAULT = false;

	/**
	 * Declares the default value for the attribute flag. As of right now, the
	 * protocol only supports location references, that are describes with the
	 * additional data of attributes, so this is by default <code>true></code>
	 */
	private static final boolean ATTRIBUTE_FLAG_DEFAULT = true;

	/**
	 * As of right now theOpen LR Standard has only one protocol. By defintion
	 * the standard version number is <code>2</code>.
	 */
	protected static final byte VERSION_NUMBER_DEFAULT = 2;

	/**
	 * {@link #hasAreaFlag()}
	 */
	private boolean areaFlag = AREA_FLAG_DEFAULT;

	/**
	 * {@link #hasAttributeFlag()}
	 */
	private boolean attributeFlag = ATTRIBUTE_FLAG_DEFAULT;

	/**
	 * {@link #getVersion()}
	 */
	private byte version = VERSION_NUMBER_DEFAULT;

	/**
	 * {@link #getPositiveOffset()}
	 */
	private Distance positiveOffset;

	/**
	 * {@link #getNegativeOffset()}
	 */
	private Distance negativeOffset;

	/**
	 * Holds a list of location reference points.
	 */
	private List<LocationReferencePoint> points;

	/**
	 * Constructs a new {@link LocationReference} using the default values for
	 * the area flag, attribute flag and version number. It is described by the
	 * list of location reference points. The start (end) point has no offset
	 * value.
	 * 
	 * @param points
	 *            the list of location reference points
	 */
	protected LocationReference(List<LocationReferencePoint> points) {
		this(points, new Distance(0), new Distance(0));
	}

	/**
	 * Constructs a new {@link LocationReference} using the default values for
	 * the area flag, attribute flag and version number. It is described by the
	 * list of location reference points and assigns the start and end point
	 * offset values.
	 * 
	 * @param points
	 *            the list of location reference points
	 * @param positiveOffset
	 *            the distance from the starting point
	 * @param negativeOffset
	 *            the distance to the end point
	 */
	protected LocationReference(List<LocationReferencePoint> points,
			Distance positiveOffset, Distance negativeOffset) {
		this(AREA_FLAG_DEFAULT, ATTRIBUTE_FLAG_DEFAULT, VERSION_NUMBER_DEFAULT,
				points, positiveOffset, negativeOffset);
	}

	/**
	 * Constructs a new {@link LocationReference} using the given values for the
	 * area flag, attributeFlag and version number. It is described by the list
	 * of location reference points. The start (end) point has no offset value.
	 * 
	 * @param areaFlag
	 *            <code>true</code> if the location reference describes an
	 *            enclosed area, <code>false</code> otherwise
	 * @param attributeFlag
	 *            <code>true</code> if the location reference uses attributes
	 *            for additional data, <code>false</code> otherwise
	 * @param version
	 *            the version number, represented by the three least significant
	 *            bits of the byte
	 * @param points
	 *            the list of location reference points
	 */
	protected LocationReference(boolean areaFlag, boolean attributeFlag,
			byte version, List<LocationReferencePoint> points) {
		this(areaFlag, attributeFlag, version, points, new Distance(0),
				new Distance(0));
	}

	/**
	 * Constructs a new {@link LocationReference} using the given values for the
	 * area flag, attributeFlag and version number. It is described by the list
	 * of location reference points and by offset values for the start and end
	 * point.
	 * 
	 * @param areaFlag
	 *            <code>true</code> if the location reference describes an
	 *            enclosed area, <code>false</code> otherwise
	 * @param attributeFlag
	 *            <code>true</code> if the location reference uses attributes
	 *            for additional data, <code>false</code> otherwise
	 * @param version
	 *            the version number, represented by the three least significant
	 *            bits of the byte
	 * @param points
	 *            the list of location reference points
	 * @param positiveOffset
	 *            the distance from the starting point
	 * @param negativeOffset
	 *            the distance to the end point
	 */
	protected LocationReference(boolean areaFlag, boolean attributeFlag,
			byte version, List<LocationReferencePoint> points,
			Distance positiveOffset, Distance negativeOffset) {
		this.areaFlag = areaFlag;
		this.attributeFlag = attributeFlag;
		this.version = version;
		this.points = points;

		this.positiveOffset = positiveOffset == null ? new Distance(0)
				: positiveOffset;
		this.negativeOffset = negativeOffset == null ? new Distance(0)
				: negativeOffset;
	}

	/**
	 * The <code>ArF</code> (<b>area flag</b>) indicates whether the location
	 * reference describes an area or not. If this flag is set then the location
	 * shall be connected and we describe an area. <br>
	 * <i>Note: The current physical data format supports only line locations so
	 * that the <code>ArF</code> is constantly set to false.</i>
	 * 
	 * @return <code>true</code> if the location reference points describe an
	 *         area, <code>false</code> otherwise.
	 */
	public boolean hasAreaFlag() {
		return areaFlag;
	}

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
	 * @return <code>true</code> if attributes are appended to the location
	 *         reference points, <code>false</code> otherwise.
	 */
	public boolean hasAttributeFlag() {
		return attributeFlag;
	}

	/**
	 * The version is used to distinguish between several physical and data
	 * formats for location references. The version number is represented by 3
	 * bits. <br>
	 * <i>Note: The actual version of the physical data format is 2 so that the
	 * <code>VER</code> field is constantly set to binary 010.</i>
	 * 
	 * @return the version number
	 */
	public byte getVersion() {
		return version;
	}

	/**
	 * Returns a list of location reference points describing the location
	 * reference
	 * 
	 * @return a list of location reference points describing the location
	 *         reference
	 */
	public List<LocationReferencePoint> getLocationReferencePoints() {
		return points;
	}

	/**
	 * The <code>PoffF</code> (<b>positive offset flag</b>) indicates whether
	 * the data includes a specific positive offset information or not.
	 * 
	 * @see #getPositiveOffset()
	 * @see #hasNegativeOffset()
	 * @return <code>true</code> if the data has a positive offset,
	 *         <code>false</code> otherwise
	 */
	public boolean hasPositiveOffset() {
		return positiveOffset.getDistance() != 0;
	}

	/**
	 * The <code>NoffF</code> (<b>negative offset flag</b>) indicates whether
	 * the data includes a specific negative offset information or not.
	 * 
	 * @see #getNegativeOffset()
	 * @see #hasPositiveOffset()
	 * 
	 * @return <code>true</code> if the data has a negative offset,
	 *         <code>false</code> otherwise
	 */
	public boolean hasNegativeOffset() {
		return negativeOffset.getDistance() != 0;
	}

	/**
	 * The <code>POFF</code> (<b>positive offset</b>) value indicates the
	 * distance between the start of the location reference path and the “real”
	 * start of the location.
	 * 
	 * @return the positive offset
	 */
	public Distance getPositiveOffset() {
		return positiveOffset;
	}

	/**
	 * The <code>NOFF</code> (<b>negative offset</b>) value indicates the
	 * distance between the end of the location reference path and the “real”
	 * end of the location.
	 * 
	 * @return the negative offset
	 */
	public Distance getNegativeOffset() {
		return negativeOffset;
	}

	/**
	 * Adds a new location reference point to this location reference
	 * 
	 * @param p
	 *            the new location reference point
	 */
	protected void add(LocationReferencePoint p) {
		points.add(p);
	}
}
