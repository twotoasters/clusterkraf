package com.twotoasters.clusterkraf.sample;

import java.text.NumberFormat;
import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.Window;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.twotoasters.clusterkraf.ClusterPoint;
import com.twotoasters.clusterkraf.Clusterkraf;
import com.twotoasters.clusterkraf.InputPoint;
import com.twotoasters.clusterkraf.MarkerOptionsChooser;
import com.twotoasters.clusterkraf.Options;

public class RandomMarkerActivity extends FragmentActivity implements GenerateRandomMarkersTask.Host {

	public static final String EXTRA_POINTS = "points";

	private static final String KEY_INPUT_POINTS = "clusterkraf input points";
	private static final String KEY_CAMERA_POSITION = "google map camera position";

	private int markerCount;
	private LatLngBounds bounds;
	private GoogleMap map;
	private CameraPosition restoreCameraPosition;
	private Clusterkraf clusterkraf;
	private ArrayList<InputPoint> inputPoints;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.activity_random_marker);

		initMap();

		setupLatLngBounds();

		Intent i = getIntent();
		if (i != null) {
			markerCount = i.getIntExtra(EXTRA_POINTS, 1);
		}

		if (bounds != null && markerCount >= 0) {
			setProgressBarIndeterminate(true);
			setProgressBarIndeterminateVisibility(true);

			new GenerateRandomMarkersTask(this, bounds).execute(markerCount);
		}

		setupActionBar();
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			ActionBar actionBar = getActionBar();
			actionBar.setTitle(getResources().getQuantityString(R.plurals.count_points, markerCount, NumberFormat.getInstance().format(markerCount)));
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
	}

	private void setupLatLngBounds() {
		LatLng northeastBound = new LatLng(36.086044326935806d, -78.81977666169405d);
		LatLng southwestBound = new LatLng(35.91362426994587d, -78.9645716920495d);
		bounds = new LatLngBounds(southwestBound, northeastBound);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case android.R.id.home:
				// This ID represents the Home or Up button. In the case of this
				// activity, the Up button is shown. Use NavUtils to allow users
				// to navigate up one level in the application structure. For
				// more details, see the Navigation pattern on Android Design:
				//
				// http://developer.android.com/design/patterns/navigation.html#up-vs-back
				//
				NavUtils.navigateUpFromSameTask(this);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onPause()
	 */
	@Override
	protected void onPause() {
		super.onPause();
		if (clusterkraf != null) {
			clusterkraf.clear();
			clusterkraf = null;
			map.clear();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		initMap();
	}

	private void initMap() {
		if (map == null) {
			SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
			if (mapFragment != null) {
				map = mapFragment.getMap();
				if (map != null) {
					UiSettings uiSettings = map.getUiSettings();
					uiSettings.setAllGesturesEnabled(false);
					uiSettings.setScrollGesturesEnabled(true);
					uiSettings.setZoomGesturesEnabled(true);
					map.setOnCameraChangeListener(new OnCameraChangeListener() {
						@Override
						public void onCameraChange(CameraPosition arg0) {
							moveMapCameraToBoundsAndInitClusterkraf();
						}
					});
				}
			}
		} else {
			// map.setOnCameraChangeListener(null);
			moveMapCameraToBoundsAndInitClusterkraf();
		}
	}

	private void moveMapCameraToBoundsAndInitClusterkraf() {
		if (map != null && bounds != null) {
			try {
				if (restoreCameraPosition != null) {
					map.moveCamera(CameraUpdateFactory.newCameraPosition(restoreCameraPosition));
					restoreCameraPosition = null;
				} else {
					map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 0));
				}
				initClusterkraf();
			} catch (IllegalStateException ise) {
				// no-op
			}
		}
	}

	private void initClusterkraf() {
		if (map != null && inputPoints != null && inputPoints.size() > 0) {
			Options options = new Options();
			options.setPixelDistanceToJoinCluster(150);
			options.setTransitionDuration(600);
			options.setMarkerOptionsChooser(new ToastedMarkerOptionsChooser());
			clusterkraf = new Clusterkraf(map, options, inputPoints);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.twotoasters.clusterkraf.sample.GenerateRandomMarkersTask.Host#
	 * onGenerateRandomMarkersTaskPostExecute()
	 */
	@Override
	public void onGenerateRandomMarkersTaskPostExecute(ArrayList<InputPoint> inputPoints) {
		setProgressBarIndeterminateVisibility(false);
		this.inputPoints = inputPoints;
		initMap();
	}

	public class ToastedMarkerOptionsChooser extends MarkerOptionsChooser {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.twotoasters.clusterkraf.MarkerIconChooser#choose(com.google.android
		 * .gms.maps.model.MarkerOptions,
		 * com.twotoasters.clusterkraf.ClusterPoint)
		 */
		@Override
		public void choose(MarkerOptions markerOptions, ClusterPoint clusterPoint) {
			boolean isCluster = clusterPoint.size() > 1;
			boolean hasTwoToasters = clusterPoint.containsInputPoint(inputPoints.get(0));
			BitmapDescriptor icon;
			String title;
			if (isCluster) {
				title = getResources().getQuantityString(R.plurals.count_points, clusterPoint.size(), clusterPoint.size());
				if (hasTwoToasters) {
					icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE);
					title = getString(R.string.including_two_toasters, title);
				} else {
					icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
					title = getResources().getQuantityString(R.plurals.count_points, clusterPoint.size(), clusterPoint.size());
				}
			} else {
				MarkerData data = (MarkerData)clusterPoint.getPointAtOffset(0).getTag();
				if (hasTwoToasters) {
					icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
					title = data.getLabel();
				} else {
					icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE);
					title = getString(R.string.point_number_x, data.getLabel());
				}
			}
			markerOptions.title(title);
			markerOptions.icon(icon);
		}
	}

}
