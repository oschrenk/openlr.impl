package org.hhu.c2c.openlr.core;

import org.hhu.c2c.openlr.util.ValidationException;

public class Example {

	protected static byte[] EXAMPLE = new byte[]{10,4,91,91,35,70,(byte)244,26,108,9,0,(byte)155,(byte)254,59,27,(byte)180,4,(byte)255,(byte)235,(byte)255,(byte)163,43,89,2}; 
	
	protected static LocationReference getExample() throws ValidationException {
		LocationReferenceBuilder lrb = new LocationReferenceBuilder();

		lrb.start();
			lrb.addLocationReferencePoint(new LocationReferencePointBuilder()
				.start()
				.setCoordinate(6.12683f, 49.60851f)
				.setFrc(FunctionalRoadClass.THIRD_CLASS_ROAD)
				.setFow(FormOfWay.MULTIPLE_CARRIAGEWAY)
				.setLfrcnp(FunctionalRoadClass.THIRD_CLASS_ROAD)
				.setBearing(135f)
				.setDnp(561)
				.get()
			);
			lrb.addLocationReferencePoint(new LocationReferencePointBuilder()
				.start()
				.setCoordinate(6.12838f, 49.60398f)
				.setFrc(FunctionalRoadClass.THIRD_CLASS_ROAD)
				.setFow(FormOfWay.SINGLE_CARRIAGEWAY)
				.setLfrcnp(FunctionalRoadClass.FIFTH_CLASS_ROAD)
				.setBearing(227f)
				.setDnp(274)
				.get()
			);
			lrb.addLocationReferencePoint(new LocationReferencePointBuilder()
				.start()
				.setCoordinate(6.12817f, 49.60305f)
				.setFrc(FunctionalRoadClass.FIFTH_CLASS_ROAD)
				.setFow(FormOfWay.SINGLE_CARRIAGEWAY)
				.setBearing(290f)
				.get()
			);
			lrb.setPositiveOffset(150);
			return lrb.get();
	}
	
}
