/**
 * @author Carlton Whitehead
 */
package com.twotoasters.clusterkraf;

import java.lang.ref.WeakReference;

import android.os.Handler;

import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.model.CameraPosition;

/**
 *
 */
class ClusteringOnCameraChangeListener implements OnCameraChangeListener {

	private final WeakReference<Host> hostRef;

	private Handler handler = new Handler();

	private static final int CALLBACK_DELAY_MILLISECONDS = 100;
	private CallbackRunnable pendingCallbackRunnable;

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
				if(pendingCallbackRunnable == null) {
					pendingCallbackRunnable = new CallbackRunnable(this);
					handler.postDelayed(pendingCallbackRunnable, CALLBACK_DELAY_MILLISECONDS);
				} else {
					handler.removeCallbacks(pendingCallbackRunnable);
					handler.postDelayed(pendingCallbackRunnable, CALLBACK_DELAY_MILLISECONDS);
				}
			}
		}
	}

	private void onRunnableCallback() {
		pendingCallbackRunnable = null;
		Host host = hostRef.get();
		if(host != null) {
			host.onClusteringCameraChange();
		}
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	interface Host {
		void onClusteringCameraChange();
	}

	private static class CallbackRunnable implements Runnable {

		private final WeakReference<ClusteringOnCameraChangeListener> listenerRef;

		private CallbackRunnable(ClusteringOnCameraChangeListener listener) {
			this.listenerRef = new WeakReference<ClusteringOnCameraChangeListener>(listener);
		}

		@Override
		public void run() {
			ClusteringOnCameraChangeListener listener = listenerRef.get();
			if(listener != null) {
				listener.onRunnableCallback();
			}
		}

	}

}
