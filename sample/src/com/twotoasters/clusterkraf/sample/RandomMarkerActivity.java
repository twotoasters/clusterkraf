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

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.twotoasters.clusterkraf.ClusterPoint;
import com.twotoasters.clusterkraf.ClusterkrafMapHelper;
import com.twotoasters.clusterkraf.InputPoint;
import com.twotoasters.clusterkraf.MarkerIconChooser;
import com.twotoasters.clusterkraf.Options;

public class RandomMarkerActivity extends FragmentActivity implements GenerateRandomMarkersTask.Host {

	public static final String EXTRA_MARKERS = "markers";

	private int markerCount;
	private GoogleMap map;
	private ClusterkrafMapHelper clusterkraf;
	private ArrayList<InputPoint> inputPoints;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.activity_random_marker);

		Intent i = getIntent();
		if (i != null) {
			setProgressBarIndeterminate(true);
			setProgressBarIndeterminateVisibility(true);

			markerCount = i.getIntExtra(EXTRA_MARKERS, 1);
			new GenerateRandomMarkersTask(this).execute(markerCount);
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
			actionBar.setTitle(getResources().getQuantityString(R.plurals.random_markers, markerCount, NumberFormat.getInstance().format(markerCount)));
			actionBar.setDisplayHomeAsUpEnabled(true);
		}
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
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		init();
	}

	private void init() {
		initMap();
		initClusterkraf();
	}

	private void initMap() {
		SupportMapFragment mapFragment = (SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.map);
		if (mapFragment != null) {
			map = mapFragment.getMap();
			if (map != null) {
				UiSettings uiSettings = map.getUiSettings();
				uiSettings.setAllGesturesEnabled(false);
				uiSettings.setScrollGesturesEnabled(true);
				uiSettings.setZoomGesturesEnabled(true);

			}
		}
	}

	private void initClusterkraf() {
		if (map != null && inputPoints != null) {
			Options options = new Options();
			options.setPixelDistanceToJoinCluster(200);
			options.setTransitionDuration(600);
			options.setMarkerIconChooser(new MyMarkerIconChooser());
			clusterkraf = new ClusterkrafMapHelper(map, options, inputPoints);
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
		init();
	}

	public static class MyMarkerIconChooser extends MarkerIconChooser {

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
			if (clusterPoint.size() > 1) {
				markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
				markerOptions.title(String.valueOf(clusterPoint.size()) + " markers");
			} else {
				InputPoint point = clusterPoint.getPointAtOffset(0);
				markerOptions.icon(BitmapDescriptorFactory.defaultMarker());
				RandomMarker randomMarker = (RandomMarker)point.getTag();
				markerOptions.title("Marker #" + String.valueOf(randomMarker.getNumber()));
			}
		}
	}
}
