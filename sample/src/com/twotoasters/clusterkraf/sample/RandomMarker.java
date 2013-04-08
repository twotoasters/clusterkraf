/**
 * @author Carlton Whitehead
 */
package com.twotoasters.clusterkraf.sample;

import com.google.android.gms.maps.model.LatLng;

/**
 *
 */
public class RandomMarker {
	private final LatLng latLng;
	private final int number;

	public RandomMarker(LatLng latLng, int number) {
		this.latLng = latLng;
		this.number = number;
	}

	/**
	 * @return the latLng
	 */
	public LatLng getLatLng() {
		return latLng;
	}

	/**
	 * @return the number
	 */
	public int getNumber() {
		return number;
	}
}
