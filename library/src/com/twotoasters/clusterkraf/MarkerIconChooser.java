/**
 * @author Carlton Whitehead
 */
package com.twotoasters.clusterkraf;

import java.lang.ref.WeakReference;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Implementations specify the icon to use against a MarkerOptions object.
 * 
 * You can use one of the provided concrete classes and just implement its Host
 * interface, or if you need more sophisticated marker logic, you can extend
 * MarkerIconChooser.
 * 
 * For example, if you need to sometimes choose a bitmap and other times choose
 * a resource, you would need to extend the class.
 * 
 * If you always want to choose the same resource ID, you would implement the
 * ResourceChooser.Host such that it just returns the desired ID from your
 * R.drawable. `options.setMarkerIconChooser(new ResourceChooser(host));`
 */
public abstract class MarkerIconChooser {
	public abstract void choose(MarkerOptions markerOptions, ClusterPoint clusterPoint);

	public static class BitmapChooser extends MarkerIconChooser {

		private final WeakReference<Host> hostRef;

		BitmapChooser(Host host) {
			this.hostRef = new WeakReference<Host>(host);
		}

		@Override
		public void choose(MarkerOptions markerOptions, ClusterPoint clusterPoint) {
			Host host = hostRef.get();
			if(host != null) {
				Bitmap bitmap = host.getIconBitmap(clusterPoint);
				markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
			}
		}

		public interface Host {
			Bitmap getIconBitmap(ClusterPoint clusterPoint);
		}
	}

	public static class ResourceChooser extends MarkerIconChooser {

		private final WeakReference<Host> hostRef;

		ResourceChooser(Host host) {
			this.hostRef = new WeakReference<Host>(host);
		}

		@Override
		public void choose(MarkerOptions markerOptions, ClusterPoint clusterPoint) {
			Host host = hostRef.get();
			if(host != null) {
				int resource = host.getIconResource(clusterPoint);
				markerOptions.icon(BitmapDescriptorFactory.fromResource(resource));
			}
		}

		public interface Host {
			int getIconResource(ClusterPoint clusterPoint);
		}
	}

}
