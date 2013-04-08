/**
 * @author Carlton Whitehead
 */
package com.twotoasters.clusterkraf;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.model.CameraPosition;

/**
 *
 */
class RegisterClusteringOnCameraChangeListener implements OnCameraChangeListener {
	
	private final WeakReference<Host> hostRef;
	
	public RegisterClusteringOnCameraChangeListener(Host host) {
		this.hostRef = new WeakReference<Host>(host);
	}

	/* (non-Javadoc)
	 * @see com.google.android.gms.maps.GoogleMap.OnCameraChangeListener#onCameraChange(com.google.android.gms.maps.model.CameraPosition)
	 */
	@Override
	public void onCameraChange(CameraPosition arg0) {
		Host host = hostRef.get();
		if (host != null) {
			host.onRegisterClusteringOnCameraChange();
		}
	}
	
	interface Host {
		void onRegisterClusteringOnCameraChange();
	}

}
