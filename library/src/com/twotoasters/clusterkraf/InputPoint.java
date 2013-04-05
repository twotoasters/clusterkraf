/**
 * @author Carlton Whitehead
 */
package com.twotoasters.clusterkraf;

import com.google.android.gms.maps.model.LatLng;

/**
 *
 */
public class InputPoint extends BasePoint {
	
	private Object tag;
	
	/**
	 * 
	 * @param mapPosition
	 */
	public InputPoint(LatLng mapPosition) {
		this.mapPosition = mapPosition;
	}
	
	/**
	 * 
	 * @param mapPosition
	 * @param tag
	 */
	public InputPoint(LatLng mapPosition, Object tag) {
		this(mapPosition);
		this.tag = tag;
	}
	
	public Object getTag() {
		return tag;
	}
	
	public void setTag(Object tag) {
		this.tag = tag;
	}
}
