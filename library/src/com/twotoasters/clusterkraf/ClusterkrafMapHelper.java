/**
 * @author Carlton Whitehead
 */
package com.twotoasters.clusterkraf;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class ClusterkrafMapHelper {
	
	private GoogleMap map;
	private final Options options;
	private final ArrayList<InputPoint> points = new ArrayList<InputPoint>();
	private final ArrayList<Marker> markers = new ArrayList<Marker>();
	private final HashMap<Marker, ClusterPoint> clusterPointsByMarker = new HashMap<Marker, ClusterPoint>();
	
	/**
	 * construct a Clusterkraf instance to manage your map with customized options.
	 * @param map the GoogleMap to be managed by Clusterkraf
	 * @param options customized options
	 */
	public ClusterkrafMapHelper(GoogleMap map, Options options) {
		this.map = map;
		this.options = options;
	}
	
	/**
	 * construct a Clusterkraf instance to manage your map with default options.
	 * this will help you confirm the library is working, but you probably should
	 * use the overload with custom options that suit your app.
	 * @param map
	 */
	public ClusterkrafMapHelper(GoogleMap map) {
		this(map, new Options());
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
	}
	
	private void clear() {
		for (Marker marker : markers) {
			marker.remove();
		}
		points.clear();
		markers.clear();
		clusterPointsByMarker.clear();
	}
	
}
