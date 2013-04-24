package com.twotoasters.clusterkraf;

import com.google.android.gms.maps.model.LatLng;

/**
 * Models a single unique point that is to be processed for clustering
 */
public class InputPoint extends BasePoint {

	/**
	 * An arbitrary Object associated with this InputPoint
	 */
	private Object tag;

	/**
	 * Construct an InputPoint with only a mapPosition
	 * 
	 * @param mapPosition
	 */
	public InputPoint(LatLng mapPosition) {
		this.mapPosition = mapPosition;
	}

	/**
	 * Construct an InputPoint with both a mapPosition and a tag
	 * 
	 * @param mapPosition
	 * @param tag
	 */
	public InputPoint(LatLng mapPosition, Object tag) {
		this(mapPosition);
		this.tag = tag;
	}

	/**
	 * Get the tag associated with this InputPoint
	 */
	public Object getTag() {
		return tag;
	}

	/**
	 * Set the tag associated with this InputPoint
	 * 
	 * @param tag
	 */
	public void setTag(Object tag) {
		this.tag = tag;
	}

}
