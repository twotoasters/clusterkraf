/**
 * @author Carlton Whitehead
 */
package com.twotoasters.clusterkraf;

import java.lang.ref.WeakReference;

import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.model.CameraPosition;

/**
 *
 */
class ClusteringOnCameraChangeListener implements OnCameraChangeListener {

	private final WeakReference<Host> hostRef;

	private boolean dirty = false;

	public ClusteringOnCameraChangeListener(Host host) {
		hostRef = new WeakReference<Host>(host);
	}

	@Override
	public void onCameraChange(CameraPosition newPosition) {
		if(dirty == false) {
			Host host = hostRef.get();
			if(host != null) {
				dirty = true;
				host.onClusteringCameraChange();
			}
		}
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	interface Host {
		void onClusteringCameraChange();
	}

}
