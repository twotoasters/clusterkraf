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

	private static final int DEFAULT_PIXEL_DISTANCE_TO_JOIN_CLUSTER = 150;
	private int pixelDistanceToJoinCluster = DEFAULT_PIXEL_DISTANCE_TO_JOIN_CLUSTER;

	private int pixelBoundsPadding = 0;

	private static final double DEFAULT_EXPAND_BOUNDS_FACTOR = 0;
	private double expandBoundsFactor = DEFAULT_EXPAND_BOUNDS_FACTOR;

	private MarkerOptionsChooser markerOptionsChooser;

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
	 * @return the expandBoundsFactor
	 */
	double getExpandBoundsFactor() {
		return expandBoundsFactor;
	}

	/**
	 * @param expandBoundsFactor
	 */
	public void setExpandBoundsFactor(double expandBoundsFactor) {
		this.expandBoundsFactor = expandBoundsFactor;
	}

	/**
	 * @return the markerOptionsChooser
	 */
	MarkerOptionsChooser getMarkerOptionsChooser() {
		return markerOptionsChooser;
	}

	/**
	 * @param markerOptionsChooser
	 *            the markerIconChooser to set
	 */
	public void setMarkerOptionsChooser(MarkerOptionsChooser markerOptionsChooser) {
		this.markerOptionsChooser = markerOptionsChooser;
	}

	/**
	 * @return the pixelBoundsPadding
	 */
	int getPixelBoundsPadding() {
		return pixelBoundsPadding;
	}

	/**
	 * @param pixelBoundsPadding
	 *            the pixelBoundsPadding to set
	 */
	public void setPixelBoundsPadding(int pixelBoundsPadding) {
		this.pixelBoundsPadding = pixelBoundsPadding;
	}
}