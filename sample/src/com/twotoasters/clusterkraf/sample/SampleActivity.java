/**
 * @author Carlton Whitehead
 */
package com.twotoasters.clusterkraf.sample;

import java.io.Serializable;
import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.Window;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.twotoasters.clusterkraf.Clusterkraf;
import com.twotoasters.clusterkraf.InputPoint;
import com.twotoasters.clusterkraf.Options.ClusterClickBehavior;
import com.twotoasters.clusterkraf.Options.ClusterInfoWindowClickBehavior;
import com.twotoasters.clusterkraf.Options.SinglePointClickBehavior;
import com.twotoasters.clusterkraf.sample.GenerateRandomMarkersTask.GeographicDistribution;

public class SampleActivity extends FragmentActivity implements GenerateRandomMarkersTask.Host {

	public static final String EXTRA_OPTIONS = "options";

	private Options options;

	private GoogleMap map;
	private CameraPosition restoreCameraPosition;
	private Clusterkraf clusterkraf;
	private ArrayList<InputPoint> inputPoints;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

		setContentView(R.layout.activity_sample);

		Intent i = getIntent();
		if (i != null) {
			Object options = i.getSerializableExtra(EXTRA_OPTIONS);
			if (options instanceof Options) {
				this.options = (Options)options;
			}
		}
		if (this.options == null) {
			this.options = new Options();
		}

		if (options != null) {
			setProgressBarIndeterminate(true);
			setProgressBarIndeterminateVisibility(true);

			new GenerateRandomMarkersTask(this, options.geographicDistribution).execute(options.pointCount);
		}

		initMap();

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()) {
			case android.R.id.home:
				finish();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (clusterkraf != null) {
			clusterkraf.clear();
			clusterkraf = null;
			map.clear();
		}
	}

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
			moveMapCameraToBoundsAndInitClusterkraf();
		}
	}

	private void moveMapCameraToBoundsAndInitClusterkraf() {
		if (map != null && options != null && inputPoints != null) {
			try {
				if (restoreCameraPosition != null) {
					map.moveCamera(CameraUpdateFactory.newCameraPosition(restoreCameraPosition));
					restoreCameraPosition = null;
				} else {
					map.moveCamera(CameraUpdateFactory.newLatLngZoom(inputPoints.get(0).getMapPosition(), 11));
				}
				initClusterkraf();
			} catch (IllegalStateException ise) {
				// no-op
			}
		}
	}

	private void initClusterkraf() {
		if (map != null && inputPoints != null && inputPoints.size() > 0) {
			com.twotoasters.clusterkraf.Options options = new com.twotoasters.clusterkraf.Options();
			applyDemoOptionsToClusterkrafOptions(options);
			clusterkraf = new Clusterkraf(map, options, inputPoints);
		}
	}

	private void applyDemoOptionsToClusterkrafOptions(com.twotoasters.clusterkraf.Options options) {
		options.setTransitionDuration(this.options.transitionDuration);
		Interpolator interpolator = null;
		try {
			interpolator = (Interpolator)Class.forName(this.options.transitionInterpolator).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		options.setTransitionInterpolator(interpolator);
		options.setPixelDistanceToJoinCluster(this.options.pixelDistanceToJoinCluster);
		options.setZoomToBoundsAnimationDuration(this.options.zoomToBoundsAnimationDuration);
		options.setShowInfoWindowAnimationDuration(this.options.showInfoWindowAnimationDuration);
		options.setExpandBoundsFactor(this.options.expandBoundsFactor);
		options.setSinglePointClickBehavior(this.options.singlePointClickBehavior);
		options.setClusterClickBehavior(this.options.clusterClickBehavior);
		options.setClusterInfoWindowClickBehavior(this.options.clusterInfoWindowClickBehavior);

		options.setMarkerOptionsChooser(new ToastedMarkerOptionsChooser(this, inputPoints.get(0)));
		options.setOnMarkerClickDownstreamListener(new ToastedOnMarkerClickDownstreamListener(this));
	}

	@Override
	public void onGenerateRandomMarkersTaskPostExecute(ArrayList<InputPoint> inputPoints) {
		setProgressBarIndeterminateVisibility(false);
		this.inputPoints = inputPoints;
		initMap();
	}

	static class Options implements Serializable {

		private static final long serialVersionUID = 7492713360265465944L;
		// sample app-specific options
		int pointCount = 100;
		GeographicDistribution geographicDistribution = GeographicDistribution.NearTwoToasters;

		// clusterkraf library options
		int transitionDuration = 500;
		String transitionInterpolator = LinearInterpolator.class.getCanonicalName();
		int pixelDistanceToJoinCluster = 150;
		int zoomToBoundsAnimationDuration = 500;
		int showInfoWindowAnimationDuration = 500;
		double expandBoundsFactor = 0.67d;
		SinglePointClickBehavior singlePointClickBehavior = SinglePointClickBehavior.SHOW_INFO_WINDOW;
		ClusterClickBehavior clusterClickBehavior = ClusterClickBehavior.ZOOM_TO_BOUNDS;
		ClusterInfoWindowClickBehavior clusterInfoWindowClickBehavior = ClusterInfoWindowClickBehavior.ZOOM_TO_BOUNDS;
	}

}
