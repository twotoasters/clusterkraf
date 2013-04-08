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
class InitialOnCameraChangeListener implements OnCameraChangeListener {
	
	private final WeakReference<Host> hostRef;
	
	InitialOnCameraChangeListener(Host host) {
		hostRef = new WeakReference<Host>(host);
	}

	/* (non-Javadoc)
	 * @see com.google.android.gms.maps.GoogleMap.OnCameraChangeListener#onCameraChange(com.google.android.gms.maps.model.CameraPosition)
	 */
	@Override
	public void onCameraChange(CameraPosition newPosition) {
		Host host = hostRef.get();
		if (host != null) {
			host.onInitialCameraChange();
		}
	}
	
	interface Host {
		void onInitialCameraChange();
	}

}
