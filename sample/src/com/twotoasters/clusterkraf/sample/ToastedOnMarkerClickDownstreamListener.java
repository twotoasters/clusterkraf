package com.twotoasters.clusterkraf.sample;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.maps.model.Marker;
import com.twotoasters.clusterkraf.ClusterPoint;
import com.twotoasters.clusterkraf.OnMarkerClickDownstreamListener;

public class ToastedOnMarkerClickDownstreamListener implements OnMarkerClickDownstreamListener {

	private final WeakReference<Context> contextRef;

	public ToastedOnMarkerClickDownstreamListener(Context context) {
		this.contextRef = new WeakReference<Context>(context);
	}

	@Override
	public boolean onMarkerClick(Marker marker, ClusterPoint clusterPoint) {
		Context context = contextRef.get();
		if (context != null && marker != null && clusterPoint != null && clusterPoint.size() == 1
				&& clusterPoint.getPointAtOffset(0).getTag() == MarkerData.TwoToasters) {
			Intent i = new Intent(context, TwoToastersActivity.class);
			context.startActivity(i);
			return true;
		}
		return false;
	}

}
