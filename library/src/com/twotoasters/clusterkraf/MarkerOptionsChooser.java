package com.twotoasters.clusterkraf;

import java.lang.ref.WeakReference;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Implementations specify the icon and any other options to use on a
 * MarkerOptions object.
 * 
 * You can use one of the provided concrete classes and just implement its Host
 * interface, or if you need more sophisticated marker logic, you can extend
 * MarkerOptionsChooser.
 * 
 * For example, if you need to sometimes choose a bitmap and other times choose
 * a resource, you would need to extend the class.
 * 
 * If you always want to choose the same resource ID, you would implement the
 * ResourceChooser.Host such that it just returns the desired ID from your
 * R.drawable. `options.setMarkerOptionsChooser(new ResourceChooser(host));`
 * 
 * If you want to specify any other properties of the MarkerOptions beyond the
 * icon, such as the title or anchor, you will need to extend
 * MarkerOptionsChooser.
 * 
 * Subclasses do not need to set the position of the MarkerOptions object.
 * Clusterkraf handles that.
 */
public abstract class MarkerOptionsChooser {

	/**
	 * Implementations set up the MarkerOptions object according to the
	 * ClusterPoint
	 * 
	 * @param markerOptions
	 *            The MarkerOptions object which will be used to add the Marker
	 * @param clusterPoint
	 *            The ClusterPoint object representing the InputPoint objects
	 *            that have been clustered here due to pixel proximity
	 */
	public abstract void choose(MarkerOptions markerOptions, ClusterPoint clusterPoint);

	/**
	 * An example MarkerOptionsChooser that sets the MarkerOptions object's icon
	 * using a bitmap from its host.
	 */
	public static class BitmapChooser extends MarkerOptionsChooser {

		private final WeakReference<Host> hostRef;

		public BitmapChooser(Host host) {
			this.hostRef = new WeakReference<Host>(host);
		}

		@Override
		public void choose(MarkerOptions markerOptions, ClusterPoint clusterPoint) {
			Host host = hostRef.get();
			if (host != null) {
				Bitmap bitmap = host.getIconBitmap(clusterPoint);
				markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmap));
			}
		}

		public interface Host {
			Bitmap getIconBitmap(ClusterPoint clusterPoint);
		}
	}

	/**
	 * An example MarkerOptionsChooser that sets the MarkerOptions object's icon
	 * using a resource ID from its host.
	 */
	public static class ResourceChooser extends MarkerOptionsChooser {

		private final WeakReference<Host> hostRef;

		public ResourceChooser(Host host) {
			this.hostRef = new WeakReference<Host>(host);
		}

		@Override
		public void choose(MarkerOptions markerOptions, ClusterPoint clusterPoint) {
			Host host = hostRef.get();
			if (host != null) {
				int resource = host.getIconResource(clusterPoint);
				markerOptions.icon(BitmapDescriptorFactory.fromResource(resource));
			}
		}

		public interface Host {
			int getIconResource(ClusterPoint clusterPoint);
		}
	}

}
