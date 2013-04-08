/**
 * @author Carlton Whitehead
 */
package com.twotoasters.clusterkraf.sample;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.twotoasters.clusterkraf.InputPoint;

class GenerateRandomMarkersTask extends AsyncTask<Integer, Void, ArrayList<InputPoint>> {

	private final WeakReference<Host> hostRef;

	GenerateRandomMarkersTask(Host host) {
		this.hostRef = new WeakReference<Host>(host);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected ArrayList<InputPoint> doInBackground(Integer... params) {
		ArrayList<InputPoint> inputPoints = new ArrayList<InputPoint>(params[0]);
		for (int i = 0; i < params[0]; i++) {
			RandomMarker randomMarker = new RandomMarker(getRandomLatLng(), i);
			InputPoint point = new InputPoint(randomMarker.getLatLng(), randomMarker);
			inputPoints.add(point);
		}
		return inputPoints;
	}

	private LatLng getRandomLatLng() {
		return new LatLng((Math.random() * 180) - 90, (Math.random() * 360) - 180);
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