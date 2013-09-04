package com.twotoasters.clusterkraf;

import android.view.View;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by rafa on 03/09/13.
 */
public interface InfoWindowDownstreamAdapter {

    public View getInfoContents(Marker marker, ClusterPoint clusterPoint);

    public View getInfoWindow(Marker marker, ClusterPoint clusterPoint);
}
