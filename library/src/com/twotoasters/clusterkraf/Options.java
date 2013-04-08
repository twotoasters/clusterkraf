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
	
	private int pixelBoundsPadding = 0;

	private MarkerIconChooser markerIconChooser;
	
	private boolean useInitialOnCameraChangeListener;

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

	/**
	 * @return the useInitialOnCameraChangeListener
	 */
	boolean isUseInitialOnCameraChangeListener() {
		return useInitialOnCameraChangeListener;
	}

	/**
	 * @param useInitialOnCameraChangeListener the useInitialOnCameraChangeListener to set
	 */
	public void setUseInitialOnCameraChangeListener(boolean useInitialOnCameraChangeListener) {
		this.useInitialOnCameraChangeListener = useInitialOnCameraChangeListener;
	}

	/**
	 * @return the pixelBoundsPadding
	 */
	public int getPixelBoundsPadding() {
		return pixelBoundsPadding;
	}

	/**
	 * @param pixelBoundsPadding the pixelBoundsPadding to set
	 */
	public void setPixelBoundsPadding(int pixelBoundsPadding) {
		this.pixelBoundsPadding = pixelBoundsPadding;
	}
}