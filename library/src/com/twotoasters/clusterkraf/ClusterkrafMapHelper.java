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

	private final ArrayList<InputPoint> points = new ArrayList<InputPoint>();
	private final ArrayList<Marker> markers = new ArrayList<Marker>();
	private final HashMap<Marker, ClusterPoint> clusterPointsByMarker = new HashMap<Marker, ClusterPoint>();

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
		for(Marker marker : markers) {
			marker.remove();
		}
		markers.clear();
		points.clear();
		clusterPointsByMarker.clear();
	}

	// TODO: support removing individual markers by InputPoint

}
