package org.hhu.c2c.openlr.core;

import org.hhu.c2c.openlr.util.ValidationException;

/**
 * This example data is taken from the OpenLR (v1.0) white paper pp. 36-40
 * 
 * @author Oliver Schrenk <oliver.schrenk@uni-duesseldorf.de>
 * @version %I%, %G%
 * 
 */
public class Example {

	/**
	 * This is the byte representation of the given example
	 */
	private static byte[] BINARY_INPUT_STREAM = new byte[] { 10, 4, 91, 91, 35,
			70, (byte) 244, 26, 108, 9, 0, (byte) 155, (byte) 254, 59, 27,
			(byte) 180, 4, (byte) 255, (byte) 235, (byte) 255, (byte) 163, 43,
			89, 2 };

	/**
	 * Returns the location reference represented as byt array
	 * 
	 * @return the location reference represented as byt array
	 */
	protected static byte[] asBinaryInputStream() {
		return BINARY_INPUT_STREAM;
	}

	/**
	 * Returns the location reference constructed with the help of the builder
	 * classes, {@link LocationReferenceBuilder} and
	 * {@link LocationReferencePointBuilder} respectively.
	 * 
	 * @return the example as a location reference
	 * @throws ValidationException
	 *             if the location reference can't be properly decoded
	 */
	protected static LocationReference asLocationReference()
			throws ValidationException {
		LocationReferenceBuilder lrb = new LocationReferenceBuilder();

		lrb.addLocationReferencePoint(new LocationReferencePointBuilder()
				.setCoordinate(6.12683f, 49.60851f).setFrc(
						FunctionalRoadClass.THIRD_CLASS_ROAD).setFow(
						FormOfWay.MULTIPLE_CARRIAGEWAY).setLfrcnp(
						FunctionalRoadClass.THIRD_CLASS_ROAD).setBearing(135f)
				.setDnp(561).build());
		lrb.addLocationReferencePoint(new LocationReferencePointBuilder()
				.setCoordinate(6.12838f, 49.60398f).setFrc(
						FunctionalRoadClass.THIRD_CLASS_ROAD).setFow(
						FormOfWay.SINGLE_CARRIAGEWAY).setLfrcnp(
						FunctionalRoadClass.FIFTH_CLASS_ROAD).setBearing(227f)
				.setDnp(274).build());
		lrb.addLocationReferencePoint(new LocationReferencePointBuilder()
				.setCoordinate(6.12817f, 49.60305f).setFrc(
						FunctionalRoadClass.FIFTH_CLASS_ROAD).setFow(
						FormOfWay.SINGLE_CARRIAGEWAY).setBearing(290f).build());
		lrb.setPositiveOffset(150);
		return lrb.build();
	}

}
