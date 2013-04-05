/**
 * @author Carlton Whitehead
 */
package com.twotoasters.clusterkraf;

import android.graphics.Bitmap;

/**
 *
 */
public interface MarkerBitmapHost {
	Bitmap getMarkerBitmap(ClusterPoint clusterPoint);
}
