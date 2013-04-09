/**
 * @author Carlton Whitehead
 */
package com.twotoasters.clusterkraf.sample;

import java.lang.ref.WeakReference;
import java.text.NumberFormat;
import java.util.ArrayList;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.twotoasters.clusterkraf.InputPoint;

class GenerateRandomMarkersTask extends AsyncTask<Integer, Void, ArrayList<InputPoint>> {

	private final WeakReference<Host> hostRef;
	private final LatLngBounds bounds;

	GenerateRandomMarkersTask(Host host, LatLngBounds bounds) {
		this.hostRef = new WeakReference<Host>(host);
		this.bounds = bounds;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected ArrayList<InputPoint> doInBackground(Integer... params) {
		ArrayList<InputPoint> inputPoints = new ArrayList<InputPoint>(params[0]);
		NumberFormat nf = NumberFormat.getInstance();
		for (int i = 0; i < params[0]; i++) {
			MarkerData marker;
			if (i == 0) {
				marker = MarkerData.TwoToasters;
			} else {
				marker = new MarkerData(getRandomLatLng(), nf.format(i));
			}
			InputPoint point = new InputPoint(marker.getLatLng(), marker);
			inputPoints.add(point);
		}
		return inputPoints;
	}

	private LatLng getRandomLatLng() {
		double latitude = getRandomBetween(bounds.southwest.latitude, bounds.northeast.latitude);
		double longitude = getRandomBetween(bounds.southwest.longitude, bounds.northeast.longitude);
		return new LatLng(latitude, longitude);
	}

	private double getRandomBetween(double min, double max) {
		return min + (Math.random() * ((max - min)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(ArrayList<InputPoint> result) {
		super.onPostExecute(result);
		Host host = hostRef.get();
		if (host != null) {
			host.onGenerateRandomMarkersTaskPostExecute(result);
		}
	}

	public interface Host {
		void onGenerateRandomMarkersTaskPostExecute(ArrayList<InputPoint> result);
	}

}