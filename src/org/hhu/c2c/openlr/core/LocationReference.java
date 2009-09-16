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
 * @version %I%, %G%
 * 
 */
public class LocationReference {

	/**
	 * The <code>ArF</code> (<b>area flag</b>) indicates whether the location
	 * reference describes an area or not. If this flag is set then the location
	 * shall be connected and we describe an area. <br>
	 * <i>Note: The current physical data format supports only line locations so
	 * that the <code>ArF</code> is constantly set to false.</i>
	 * 
	 * @see #hasAreaFlag()
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
	 * @see #hasAttributeFlag()
	 */
	private boolean attributeFlag;

	/**
	 * The version is used to distinguish between several physical and data
	 * formats for location references. The version number is represented by 3
	 * bits. <br>
	 * <i>Note: The actual version of the physical data format is 2 so that the
	 * <code>VER</code> field is constantly set to binary 010.</i>
	 * 
	 * @see #getVersion()
	 */
	private byte version;

	/**
	 * The <code>POFF</code> (<b>positive offset</b>) value indicates the
	 * distance between the start of the location reference path and the “real”
	 * start of the location.
	 * 
	 * @see #getPositiveOffset()
	 */
	private Distance positiveOffset;

	/**
	 * The <code>NOFF</code> (<b>negative offset</b>) value indicates the
	 * distance between the end of the location reference path and the “real”
	 * end of the location.
	 * 
	 * @see #getNegativeOffset()
	 */
	private Distance negativeOffset;

	/**
	 * Holds a list of location reference points.
	 */
	private List<LocationReferencePoint> points;

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
