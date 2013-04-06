/**
 * @author Carlton Whitehead
 */
package com.twotoasters.clusterkraf;

public enum MarkerDrawingStrategy {
	/**
	 * Clusterkraf will only draw Markers that are within the view bounds.
	 * Pro: Depending on the size of your data set and its geographic distribution,
	 * this can greatly help performance. Con: When panning and zooming the map, 
	 * Markers that weren't previously within the view bounds will suddenly appear,
	 * which might be visually jarring to the user.
	 */
	WITHIN_BOUNDS_ONLY
	// TODO: MarkerDrawingStategy.ALL_MARKERS
}