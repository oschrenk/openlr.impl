package org.hhu.c2c.openlr.core;

/**
 * The OpenLR<sup><small>TM</small></sup> rules describe additional regulations
 * for OpenLR<sup><small>TM</small></sup> location references, supplementary to
 * the general “shortest-path” rule for sub-dividing the location reference
 * path. These rules are used to simplify the encoding and decoding process and
 * to increase the accuracy of the results.
 * 
 * <dl>
 * <dt>Rule - 1</dt>
 * <dd>The maximum distance between two location reference points shall not
 * exceed 15km. The distance is measured along the location reference path. If
 * this condition is not fulfilled for a location reference then a sufficient
 * number of additional LR-points shall be inserted.</dd>
 * </dl>
 * 
 * The maximum distance between two consecutive location reference points is
 * restricted in order to speed up shortest-path computation because several
 * short routes can be computed quicker than one large route if the routing
 * algorithm has to take the whole network into account. The restriction also
 * provides the opportunity to form a compact binary format with an acceptable
 * accuracy.
 * 
 * <dl>
 * <dt>Rule - 2</dt>
 * <dd>All lengths are integer values. If there are float values available then
 * we will round these values to get an integer representation.</dd>
 * </dl>
 * 
 * Different maps might store the length values in different formats and also
 * with different precision and the uniform basis for all is the usage of
 * integer values. It is also more compact to transmit integer values in a
 * binary format than using float values.
 * 
 * <dl>
 * <dt>Rule - 3</dt>
 * <dd>Two LR-points are mandatory and the number of intermediate LR-points is
 * not limited.</dd>
 * </dl>
 * 
 * A line location reference must always have at least two location reference
 * points indicating the start and the end of the location. In addition, further
 * (mandatory) intermediate location reference points are to be included if
 * conditions of shortest-path or Rule 1 are met. If the encoder detects
 * critical situations where the decoder (on a different map) might get into
 * trouble, the location reference might be enhanced with additional (optional)
 * intermediate LR-points.
 * 
 * <dl>
 * <dt>Rule - 2</dt>
 * <dd>The coordinates of the LR-points shall be chosen on valid network nodes.</dd>
 * </dl>
 * 
 * These valid network nodes shall be junctions in the real world and it is
 * expected that these junctions can be found in different maps with a higher
 * probability than positions somewhere on a line. Additional nodes other than
 * junctions (invalid nodes) shall be avoided which can be easily skipped during
 * a route search. At these invalid nodes it is not possible to deviate from a
 * route.
 * 
 * Nodes having only one incoming and one outgoing line (as directed
 * connectivity) shall be invalid and therefore avoided since these nodes are
 * not related to junctions and can be stepped over during route search. Nodes
 * which have two incoming and two outgoing lines and there are only two
 * adjacent nodes shall also be invalid.
 * 
 * If one of these nodes is selected for a LR-point then this LR-point should be
 * shifted along the location reference path in order to find a suitable node.
 * This can be done since a route calculation will step over such invalid nodes
 * without leaving the desired path.
 * 
 * If the start or the end of a location is placed on invalid nodes then the
 * encoder should expand the location uniquely and should find a suitable node
 * outside of the location. This expansion must never go into the location
 * because this will shorten the location’s spatial extent.
 * 
 * 
 * @author Oliver Schrenk <oliver.schrenk@uni-duesseldorf.de>
 * @version %I%, %G%
 * 
 */
public class Rules {
	/**
	 * <b>Rule 1:</b>The maximum distance between two location reference points
	 * shall not exceed 15km. The distance is measured along the location
	 * reference path. If this condition is not fulfilled for a location
	 * reference then a sufficient number of additional LR-points shall be
	 * inserted.
	 */
	protected static final int MAXIMUM_DISTANCE_BETWEEN_TWO_LR_POINTS = 15000;

	/**
	 * <b>Lemma 1.1:</b> As distances are encoded as a byte, using all 8 bit
	 * values, each bit value represents an interval length of 58.6 meters.
	 */
	protected static final float ONE_BIT_DISTANCE = MAXIMUM_DISTANCE_BETWEEN_TWO_LR_POINTS / 255;

	/**
	 * <b>Rule 3:</b> Two LR-points are mandatory and the number of intermediate
	 * LR-points is not limited.
	 */
	protected static final int MINIMUM_NUMBER_OF_LR_POINTS = 2;
}
