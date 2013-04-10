/**
 * @author Carlton Whitehead
 */
package com.twotoasters.clusterkraf;

import android.graphics.Point;

import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;
import com.twotoasters.clusterkraf.util.Distance;

/**
 *
 */
public abstract class BasePoint {

	protected LatLng mapPosition;
	private Point screenPosition;

	public LatLng getMapPosition() {
		return mapPosition;
	}

	public Point getScreenPosition() {
		return screenPosition;
	}

	public boolean hasScreenPosition() {
		return screenPosition != null;
	}

	void clearScreenPosition() {
		this.screenPosition = null;
	}

	void buildScreenPosition(Projection projection) {
		if (projection != null && mapPosition != null) {
			screenPosition = projection.toScreenLocation(mapPosition);
		}
	}

	void setScreenPosition(Point screenPosition) {
		this.screenPosition = screenPosition;
	}

	double getPixelDistanceFrom(BasePoint otherPoint) {
		return Distance.from(screenPosition, otherPoint.screenPosition);
	}

}
