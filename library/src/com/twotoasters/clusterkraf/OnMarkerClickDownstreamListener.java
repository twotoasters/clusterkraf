/**
 * @author Carlton Whitehead
 */
package com.twotoasters.clusterkraf;

import com.google.android.gms.maps.model.Marker;

/**
 *
 */
public interface OnMarkerClickDownstreamListener {
	boolean onMarkerClick(Marker marker, ClusterPoint clusterPoint);
}
