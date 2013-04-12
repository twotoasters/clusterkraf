/**
 * @author Carlton Whitehead
 */
package com.twotoasters.clusterkraf;

import com.google.android.gms.maps.model.Marker;

/**
 *
 */
public interface OnInfoWindowClickDownstreamListener {
	boolean onInfoWindowClick(Marker marker, ClusterPoint clusterPoint);
}
