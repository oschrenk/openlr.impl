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
	private final boolean areaFlag;

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
	private final boolean attributeFlag;

	/**
	 * The <code>NOFF</code> (<b>negative offset</b>) value indicates the
	 * distance between the end of the location reference path and the “real”
	 * end of the location.
	 * 
	 * @see #getNegativeOffset()
	 */
	private final Distance negativeOffset;

	/**
	 * Holds a list of location reference points.
	 */
	private final List<LocationReferencePoint> points;

	/**
	 * The <code>POFF</code> (<b>positive offset</b>) value indicates the
	 * distance between the start of the location reference path and the “real”
	 * start of the location.
	 * 
	 * @see #getPositiveOffset()
	 */
	private final Distance positiveOffset;

	/**
	 * The version is used to distinguish between several physical and data
	 * formats for location references. The version number is represented by 3
	 * bits. <br>
	 * <i>Note: The actual version of the physical data format is 2 so that the
	 * <code>VER</code> field is constantly set to binary 010.</i>
	 * 
	 * @see #getVersion()
	 */
	private final byte version;

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
	protected LocationReference(final boolean areaFlag,
			final boolean attributeFlag, final byte version,
			final List<LocationReferencePoint> points,
			final Distance positiveOffset, final Distance negativeOffset) {
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
	 * Adds a new location reference point to this location reference
	 * 
	 * @param point
	 *            the new location reference point
	 */
	protected void add(final LocationReferencePoint point) {
		points.add(point);
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (areaFlag ? 1231 : 1237);
		result = prime * result + (attributeFlag ? 1231 : 1237);
		result = prime * result
				+ ((negativeOffset == null) ? 0 : negativeOffset.hashCode());
		result = prime * result + ((points == null) ? 0 : points.hashCode());
		result = prime * result
				+ ((positiveOffset == null) ? 0 : positiveOffset.hashCode());
		result = prime * result + version;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LocationReference other = (LocationReference) obj;
		if (areaFlag != other.areaFlag)
			return false;
		if (attributeFlag != other.attributeFlag)
			return false;
		if (negativeOffset == null) {
			if (other.negativeOffset != null)
				return false;
		} else if (!negativeOffset.equals(other.negativeOffset))
			return false;
		if (points == null) {
			if (other.points != null)
				return false;
		} else if (!points.equals(other.points))
			return false;
		if (positiveOffset == null) {
			if (other.positiveOffset != null)
				return false;
		} else if (!positiveOffset.equals(other.positiveOffset))
			return false;
		if (version != other.version)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LocationReference: \n" + "\tAF: " + areaFlag + "\n" + "\tAtF: "
				+ attributeFlag + "\n" + "\tVER: " + version + "\n"
				+ "\tPOFF: " + positiveOffset + "\n" + "\tNOFF: "
				+ negativeOffset + "\n"

				+ "\tPoints: \n" + points + "\n";

	}

}
