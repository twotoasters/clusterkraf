/**
 * @author Carlton Whitehead
 */
package com.twotoasters.clusterkraf;

import android.view.animation.Interpolator;

/**
 * specify options for the clusterkraf instance
 */
public class Options {

	private static final int DEFAULT_TRANSITION_DURATION = 300;
	private int transitionDuration = DEFAULT_TRANSITION_DURATION;

	private Interpolator transitionInterpolator;

	private static final int DEFAULT_PIXEL_DISTANCE_TO_JOIN_CLUSTER = 150;
	private int pixelDistanceToJoinCluster = DEFAULT_PIXEL_DISTANCE_TO_JOIN_CLUSTER;

	private static final double DEFAULT_EXPAND_BOUNDS_FACTOR = 0;
	private double expandBoundsFactor = DEFAULT_EXPAND_BOUNDS_FACTOR;

	private MarkerOptionsChooser markerOptionsChooser;

	private OnMarkerClickDownstreamListener onMarkerClickDownstreamListener;

	private OnInfoWindowClickDownstreamListener onInfoWindowClickDownstreamListener;

	private int zoomToBoundsPadding = 75;

	private final int DEFAULT_ZOOM_TO_BOUNDS_ANIMATION_DURATION = 300;
	private int zoomToBoundsAnimationDuration = DEFAULT_ZOOM_TO_BOUNDS_ANIMATION_DURATION;

	private final int DEFAULT_SHOW_INFO_WINDOW_ANIMATION_DURATION = 300;
	private int showInfoWindowAnimationDuration = DEFAULT_SHOW_INFO_WINDOW_ANIMATION_DURATION;

	private ClusterClickBehavior clusterClickBehavior = ClusterClickBehavior.ZOOM_TO_BOUNDS;

	private ClusterInfoWindowClickBehavior clusterInfoWindowClickBehavior = ClusterInfoWindowClickBehavior.HIDE_INFO_WINDOW;

	private SinglePointClickBehavior singlePointClickBehavior = SinglePointClickBehavior.SHOW_INFO_WINDOW;

	private SinglePointInfoWindowClickBehavior singlePointInfoWindowClickBehavior = SinglePointInfoWindowClickBehavior.HIDE_INFO_WINDOW;

	private long clusteringOnCameraChangeListenerDirtyLifetimeMillis = 200l;

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
	 * @return the transitionInterpolator
	 */
	Interpolator getTransitionInterpolator() {
		return transitionInterpolator;
	}

	/**
	 * @param transitionInterpolator
	 *            the transitionInterpolator to set
	 */
	public void setTransitionInterpolator(Interpolator transitionInterpolator) {
		this.transitionInterpolator = transitionInterpolator;
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
	 * @return the onMarkerClickDownstreamListener
	 */
	OnMarkerClickDownstreamListener getOnMarkerClickDownstreamListener() {
		return onMarkerClickDownstreamListener;
	}

	/**
	 * @param onMarkerClickDownstreamListener
	 *            the onMarkerClickDownstreamListener to set
	 */
	public void setOnMarkerClickDownstreamListener(OnMarkerClickDownstreamListener onMarkerClickDownstreamListener) {
		this.onMarkerClickDownstreamListener = onMarkerClickDownstreamListener;
	}

	/**
	 * @return the onInfoWindowClickDownstreamListener
	 */
	OnInfoWindowClickDownstreamListener getOnInfoWindowClickDownstreamListener() {
		return onInfoWindowClickDownstreamListener;
	}

	/**
	 * @param onInfoWindowClickDownstreamListener
	 *            the onInfoWindowClickDownstreamListener to set
	 */
	public void setOnInfoWindowClickDownstreamListener(OnInfoWindowClickDownstreamListener onInfoWindowClickDownstreamListener) {
		this.onInfoWindowClickDownstreamListener = onInfoWindowClickDownstreamListener;
	}

	/**
	 * @return the clusterClickBehavior
	 */
	ClusterClickBehavior getClusterClickBehavior() {
		return clusterClickBehavior;
	}

	/**
	 * @param clusterClickBehavior
	 *            the clusterClickBehavior to set
	 */
	public void setClusterClickBehavior(ClusterClickBehavior clusterClickBehavior) {
		this.clusterClickBehavior = clusterClickBehavior;
	}

	/**
	 * @return the clusterInfoWindowClickBehavior
	 */
	ClusterInfoWindowClickBehavior getClusterInfoWindowClickBehavior() {
		return clusterInfoWindowClickBehavior;
	}

	/**
	 * @param clusterInfoWindowClickBehavior
	 *            the clusterInfoWindowClickBehavior to set
	 */
	public void setClusterInfoWindowClickBehavior(ClusterInfoWindowClickBehavior clusterInfoWindowClickBehavior) {
		this.clusterInfoWindowClickBehavior = clusterInfoWindowClickBehavior;
	}

	/**
	 * @return the zoomToBoundsPadding
	 */
	int getZoomToBoundsPadding() {
		return zoomToBoundsPadding;
	}

	/**
	 * @param zoomToBoundsPadding
	 *            the zoomToBoundsPadding to set
	 */
	public void setZoomToBoundsPadding(int zoomToBoundsPadding) {
		this.zoomToBoundsPadding = zoomToBoundsPadding;
	}

	/**
	 * @return the zoomToBoundsAnimationDuration
	 */
	int getZoomToBoundsAnimationDuration() {
		return zoomToBoundsAnimationDuration;
	}

	/**
	 * @param zoomToBoundsAnimationDuration
	 *            the zoomToBoundsAnimationDuration to set
	 */
	public void setZoomToBoundsAnimationDuration(int zoomToBoundsAnimationDuration) {
		this.zoomToBoundsAnimationDuration = zoomToBoundsAnimationDuration;
	}

	/**
	 * @return the showInfoWindowAnimationDuration
	 */
	int getShowInfoWindowAnimationDuration() {
		return showInfoWindowAnimationDuration;
	}

	/**
	 * @param showInfoWindowAnimationDuration
	 *            the showInfoWindowAnimationDuration to set
	 */
	public void setShowInfoWindowAnimationDuration(int showInfoWindowAnimationDuration) {
		this.showInfoWindowAnimationDuration = showInfoWindowAnimationDuration;
	}

	/**
	 * @return the singlePointClickBehavior
	 */
	SinglePointClickBehavior getSinglePointClickBehavior() {
		return singlePointClickBehavior;
	}

	/**
	 * @param singlePointClickBehavior
	 *            the singlePointClickBehavior to set
	 */
	public void setSinglePointClickBehavior(SinglePointClickBehavior singlePointClickBehavior) {
		this.singlePointClickBehavior = singlePointClickBehavior;
	}

	/**
	 * @return the singlePointInfoWindowClickBehavior
	 */
	SinglePointInfoWindowClickBehavior getSinglePointInfoWindowClickBehavior() {
		return singlePointInfoWindowClickBehavior;
	}

	/**
	 * @param singlePointInfoWindowClickBehavior
	 *            the singlePointInfoWindowClickBehavior to set
	 */
	public void setSinglePointInfoWindowClickBehavior(SinglePointInfoWindowClickBehavior singlePointInfoWindowClickBehavior) {
		this.singlePointInfoWindowClickBehavior = singlePointInfoWindowClickBehavior;
	}

	/**
	 * @return the clusteringOnCameraChangeListenerDirtyLifetimeMillis
	 */
	long getClusteringOnCameraChangeListenerDirtyLifetimeMillis() {
		return clusteringOnCameraChangeListenerDirtyLifetimeMillis;
	}

	public void setClusteringOnCameraChangeListenerDirtyLifetimeMillis(long clusteringOnCameraChangeListenerDirtyLifetimeMillis) {
		this.clusteringOnCameraChangeListenerDirtyLifetimeMillis = clusteringOnCameraChangeListenerDirtyLifetimeMillis;
	}

	public enum ClusterClickBehavior {
		ZOOM_TO_BOUNDS, SHOW_INFO_WINDOW, NO_OP
	}

	public enum ClusterInfoWindowClickBehavior {
		ZOOM_TO_BOUNDS, HIDE_INFO_WINDOW, NO_OP
	}

	public enum SinglePointClickBehavior {
		SHOW_INFO_WINDOW, NO_OP
	}

	public enum SinglePointInfoWindowClickBehavior {
		HIDE_INFO_WINDOW, NO_OP
	}
}