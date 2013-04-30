package com.twotoasters.clusterkraf.sample;

import java.lang.ref.WeakReference;
import java.text.NumberFormat;
import java.util.ArrayList;

import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;
import com.twotoasters.clusterkraf.InputPoint;

class RandomPointsProvider {

	private static RandomPointsProvider instance;

	private WeakReference<GenerateCallback> generateCallback;
	private ArrayList<InputPoint> points;

	static RandomPointsProvider getInstance() {
		if (instance == null) {
			instance = new RandomPointsProvider();
		}
		return instance;
	}

	void generate(GenerateCallback callback, GeographicDistribution geographicDistribution, int count) {
		this.points = null;
		this.generateCallback = new WeakReference<GenerateCallback>(callback);
		new GenerateTask(geographicDistribution).execute(count);
	}

	private void onGenerateTaskFinished(ArrayList<InputPoint> points) {
		this.points = points;
		if (generateCallback != null) {
			GenerateCallback callback = generateCallback.get();
			if (callback != null) {
				callback.onRandomMarkersGenerated(points);
			}
			generateCallback = null;
		}
	}

	boolean hasPoints() {
		return points != null;
	}

	ArrayList<InputPoint> getPoints() {
		return points;
	}

	class GenerateTask extends AsyncTask<Integer, Void, ArrayList<InputPoint>> {

		private final double northeastLat;
		private final double northeastLng;
		private final double southwestLat;
		private final double southwestLng;

		GenerateTask(GeographicDistribution geographicDistribution) {
			switch(geographicDistribution) {
				case NearTwoToasters:
					LatLng ttLatLng = MarkerData.TwoToasters.getLatLng();
					northeastLat = ttLatLng.latitude + 0.2d;
					northeastLng = ttLatLng.longitude + 0.2d;
					southwestLat = ttLatLng.latitude - 0.2d;
					southwestLng = ttLatLng.longitude - 0.2d;
					break;
				case Worldwide:
				default:
					northeastLat = 90d;
					northeastLng = 180d;
					southwestLat = -90d;
					southwestLng = -180d;
					break;
			}
		}

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
			double latitude = getRandomBetween(southwestLat, northeastLat);
			double longitude = getRandomBetween(southwestLng, northeastLng);
			return new LatLng(latitude, longitude);
		}

		private double getRandomBetween(double min, double max) {
			return min + (Math.random() * ((max - min)));
		}

		@Override
		protected void onPostExecute(ArrayList<InputPoint> result) {
			super.onPostExecute(result);
			onGenerateTaskFinished(result);
		}

	}

	enum GeographicDistribution {
		NearTwoToasters, Worldwide
	}

	interface GenerateCallback {
		void onRandomMarkersGenerated(ArrayList<InputPoint> result);
	}
}
