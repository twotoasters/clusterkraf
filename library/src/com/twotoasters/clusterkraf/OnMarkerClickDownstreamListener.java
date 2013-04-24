package com.twotoasters.clusterkraf;

import com.google.android.gms.maps.model.Marker;

/**
 * Because Clusterkraf must set its own OnMarkerClickListener on the GoogleMap
 * it is managing, and because the GoogleMap can only have one
 * OnMarkerClickListener, Clusterkraf passes the event downstream to its users.
 */
public interface OnMarkerClickDownstreamListener {
	/**
	 * @param marker
	 *            The Marker object that was clicked.
	 * @param clusterPoint
	 *            The ClusterPoint object representing the Marker that was
	 *            clicked. In case you have manually added Marker objects
	 *            directly to the map, bypassing Clusterkraf, this will be null
	 *            if the clicked Marker object was added manually.
	 * @return true if you have fully consumed the event, or false if
	 *         Clusterkraf still needs to carry out the configured action.
	 */
	boolean onMarkerClick(Marker marker, ClusterPoint clusterPoint);
}
