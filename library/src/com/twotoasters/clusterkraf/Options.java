/**
 * @author Carlton Whitehead
 */
package com.twotoasters.clusterkraf;

/**
 * specify options for the clusterkraf instance
 */
public class Options {

	private static final int DEFAULT_TRANSITION_DURATION = 300;
	private int transitionDuration = DEFAULT_TRANSITION_DURATION;

	private int pixelDistanceToJoinCluster;

	private static final MarkerDrawingStrategy DEFAULT_MARKER_DRAWING_STRATEGY = MarkerDrawingStrategy.WITHIN_BOUNDS_ONLY;
	private MarkerDrawingStrategy markerDrawingStrategy = DEFAULT_MARKER_DRAWING_STRATEGY;

	private MarkerIconChooser markerIconChooser;

	MarkerDrawingStrategy getMarkerDrawingStrategy() {
		return markerDrawingStrategy;
	}

	/**
	 * @param markerDrawingStrategy
	 *            the markerDrawingStrategy to set
	 */
	public void setMarkerDrawingStrategy(MarkerDrawingStrategy markerDrawingStrategy) {
		this.markerDrawingStrategy = markerDrawingStrategy;
	}

	/**
	 * @return the transitionDuration
	 */
	int getTransitionDuration() {
		return transitionDuration;
	}

	/**
	 * @param transitionDuration
	 *            the transitionDuration to set
	 */
	public void setTransitionDuration(int transitionDuration) {
		this.transitionDuration = transitionDuration;
	}

	/**
	 * @return the pixelDistanceToJoinCluster
	 */
	int getPixelDistanceToJoinCluster() {
		return pixelDistanceToJoinCluster;
	}

	/**
	 * @param pixelDistanceToJoinCluster
	 *            the pixelDistanceToJoinCluster to set
	 */
	public void setPixelDistanceToJoinCluster(int pixelDistanceToJoinCluster) {
		this.pixelDistanceToJoinCluster = pixelDistanceToJoinCluster;
	}

	/**
	 * @return the markerIconChooser
	 */
	public MarkerIconChooser getMarkerIconChooser() {
		return markerIconChooser;
	}

	/**
	 * @param markerIconChooser the markerIconChooser to set
	 */
	public void setMarkerIconChooser(MarkerIconChooser markerIconChooser) {
		this.markerIconChooser = markerIconChooser;
	}
}