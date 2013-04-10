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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mapPosition == null) ? 0 : mapPosition.hashCode());
		result = prime * result + ((screenPosition == null) ? 0 : screenPosition.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BasePoint other = (BasePoint)obj;
		if (mapPosition == null) {
			if (other.mapPosition != null)
				return false;
		} else if (!mapPosition.equals(other.mapPosition))
			return false;
		if (screenPosition == null) {
			if (other.screenPosition != null)
				return false;
		} else if (!screenPosition.equals(other.screenPosition))
			return false;
		return true;
	}

}
