/**
 * @author Carlton Whitehead
 */
package com.twotoasters.clusterkraf;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class ClusterkrafMapHelper {

	private final WeakReference<GoogleMap> mapRef;
	private final Options options;
	private final ClusterTransitionsAnimation transitionsAnimation;

	private InnerCallbackListener innerCallbackListener = new InnerCallbackListener();

	private final ArrayList<InputPoint> points = new ArrayList<InputPoint>();
	private final ArrayList<Marker> markers = new ArrayList<Marker>();
	private final HashMap<Marker, ClusterPoint> clusterPointsByMarker = new HashMap<Marker, ClusterPoint>();
	private ArrayList<ClusterTransition> clusterTransitions;
	private ArrayList<ClusterPoint> currentClusters;

	/**
	 * construct a Clusterkraf instance to manage your map with customized
	 * options.
	 * 
	 * @param map
	 *            the GoogleMap to be managed by Clusterkraf
	 * @param options
	 *            customized options
	 */
	public ClusterkrafMapHelper(GoogleMap map, Options options) {
		this.mapRef = new WeakReference<GoogleMap>(map);
		this.options = options;
		this.transitionsAnimation = new ClusterTransitionsAnimation(map, options, innerCallbackListener);
	}

	public void add(InputPoint inputPoint) {
		points.add(inputPoint);
	}

	public void addAll(ArrayList<InputPoint> inputPoints) {
		points.addAll(inputPoints);
	}

	public void replace(ArrayList<InputPoint> inputPoints) {
		clear();
		points.addAll(inputPoints);
		// TODO: optionally animate camera to new points
	}

	/**
	 * remove markers from the map
	 */
	void clear() {
		// we avoid GoogleMap.clear() because the current SDK leaks custom
		// bitmaps, see:
		// http://code.google.com/p/gmaps-api-issues/issues/detail?id=4703
		for (Marker marker : markers) {
			marker.remove();
		}
		markers.clear();
		points.clear();
		clusterPointsByMarker.clear();
	}

	// TODO: support removing individual markers by InputPoint

	private void buildClusters() {
		switch(options.getMarkerDrawingStrategy()) {
			case WITHIN_BOUNDS_ONLY:
				buildClustersWithinBoundsOnly();
				break;
		}
	}

	private void buildClustersWithinBoundsOnly() {
		GoogleMap map = mapRef.get();
		if (map != null) {
			ArrayList<ClusterPoint> previousClusters = currentClusters;
			ClustersBuilder builder = new ClustersBuilder(map, options, previousClusters);
			builder.addAll(points);
			currentClusters = builder.build();

			if (currentClusters != null && previousClusters != null) {
				ClusterTransition.Builder ctb = new ClusterTransition.Builder(map, previousClusters);
				for (ClusterPoint currentClusterPoint : currentClusters) {
					ctb.add(currentClusterPoint);
				}
				clusterTransitions = ctb.build();
			}
		}
	}

	private void updateClustersAndTransition() {

	}

	private class InnerCallbackListener implements ClusteringOnCameraChangeListener.Host, ClusterTransitionsAnimation.Host {

		private final ClusteringOnCameraChangeListener clusteringOnCameraChangeListener = new ClusteringOnCameraChangeListener(this);

		@Override
		public void onClusteringCameraChange() {
			switch(options.getMarkerDrawingStrategy()) {
				case WITHIN_BOUNDS_ONLY:
					updateClustersAndTransition();
					break;
			}
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.twotoasters.clusterkraf.ClusterTransitionsAnimation.Host#
		 * onClusterTransitionStarting()
		 */
		@Override
		public void onClusterTransitionStarting() {
			clusteringOnCameraChangeListener.setDirty(true);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see com.twotoasters.clusterkraf.ClusterTransitionsAnimation.Host#
		 * onClusterTransitionFinished()
		 */
		@Override
		public void onClusterTransitionFinished() {
			clusteringOnCameraChangeListener.setDirty(false);
			// TODO: draw markers at new location and hide remnants of
			// transition
		}

	}

}
