package com.twotoasters.clusterkraf.sample;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
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
import com.twotoasters.clusterkraf.Options.ClusterClickBehavior;
import com.twotoasters.clusterkraf.Options.ClusterInfoWindowClickBehavior;
import com.twotoasters.clusterkraf.Options.SinglePointClickBehavior;

public class RandomMarkerActivity extends FragmentActivity implements GenerateRandomMarkersTask.Host {

	public static final String EXTRA_OPTIONS = "options";

	private Options options;

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
			Object options = i.getSerializableExtra(EXTRA_OPTIONS);
			if (options instanceof Options) {
				this.options = (Options)options;
			}
		}
		if (this.options == null) {
			this.options = new Options();
		}

		if (bounds != null && options != null) {
			setProgressBarIndeterminate(true);
			setProgressBarIndeterminateVisibility(true);

			new GenerateRandomMarkersTask(this, bounds).execute(options.markerCount);
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
			actionBar.setTitle(getResources().getQuantityString(R.plurals.count_points, options.markerCount,
					NumberFormat.getInstance().format(options.markerCount)));
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
			com.twotoasters.clusterkraf.Options options = new com.twotoasters.clusterkraf.Options();
			// TODO: copy settings from this.options
			options.setTransitionDuration(this.options.transitionDuration);
			options.setPixelDistanceToJoinCluster(this.options.pixelDistanceToJoinCluster);
			options.setZoomToBoundsAnimationDuration(this.options.zoomToBoundsAnimationDuration);
			options.setShowInfoWindowAnimationDuration(this.options.showInfoWindowAnimationDuration);
			options.setExpandBoundsFactor(this.options.expandBoundsFactor);
			options.setSinglePointClickBehavior(this.options.singlePointClickBehavior);
			options.setClusterClickBehavior(this.options.clusterClickBehavior);
			options.setClusterInfoWindowClickBehavior(this.options.clusterInfoWindowClickBehavior);

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

		private final Paint clusterPaintLarge;
		private final Paint clusterPaintMedium;
		private final Paint clusterPaintSmall;

		public ToastedMarkerOptionsChooser() {
			clusterPaintMedium = new Paint();
			clusterPaintMedium.setColor(Color.WHITE);
			clusterPaintMedium.setAlpha(255);
			clusterPaintMedium.setTextAlign(Paint.Align.CENTER);
			clusterPaintMedium.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD_ITALIC));
			clusterPaintMedium.setTextSize(getResources().getDimension(R.dimen.cluster_text_size_medium));

			clusterPaintSmall = new Paint(clusterPaintMedium);
			clusterPaintSmall.setTextSize(getResources().getDimension(R.dimen.cluster_text_size_small));

			clusterPaintLarge = new Paint(clusterPaintMedium);
			clusterPaintLarge.setTextSize(getResources().getDimension(R.dimen.cluster_text_size_large));
		}

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
				int clusterSize = clusterPoint.size();
				if (hasTwoToasters) {
					icon = BitmapDescriptorFactory.fromBitmap(getClusterBitmap(R.drawable.ic_map_pin_cluster_toaster, clusterSize));
					title = getString(R.string.including_two_toasters, title);
				} else {
					icon = BitmapDescriptorFactory.fromBitmap(getClusterBitmap(R.drawable.ic_map_pin_cluster, clusterSize));
					title = getResources().getQuantityString(R.plurals.count_points, clusterSize, clusterSize);
				}
			} else {
				MarkerData data = (MarkerData)clusterPoint.getPointAtOffset(0).getTag();
				if (hasTwoToasters) {
					icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_map_pin_toaster);
					title = data.getLabel();
				} else {
					icon = BitmapDescriptorFactory.fromResource(R.drawable.ic_map_pin);
					title = getString(R.string.point_number_x, data.getLabel());
				}
			}
			markerOptions.icon(icon);
			markerOptions.title(title);
			markerOptions.anchor(0.5f, 1.0f);
		}

		@SuppressLint("NewApi")
		private Bitmap getClusterBitmap(int resourceId, int clusterSize) {
			BitmapFactory.Options options = new BitmapFactory.Options();

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
				options.inMutable = true;
			}
			Bitmap bitmap = BitmapFactory.decodeResource(getResources(), resourceId, options);
			if (bitmap.isMutable() == false) {
				bitmap = bitmap.copy(bitmap.getConfig(), true);
			}

			Canvas canvas = new Canvas(bitmap);

			Paint paint = null;
			float originY;
			if (clusterSize < 100) {
				paint = clusterPaintLarge;
				originY = bitmap.getHeight() * 0.64f;
			} else if (clusterSize < 1000) {
				paint = clusterPaintMedium;
				originY = bitmap.getHeight() * 0.6f;
			} else {
				paint = clusterPaintSmall;
				originY = bitmap.getHeight() * 0.56f;
			}

			canvas.drawText(String.valueOf(clusterSize), bitmap.getWidth() * 0.5f, originY, paint);

			return bitmap;
		}
	}

	static class Options implements Serializable {

		private static final long serialVersionUID = 2802382185317730662L;

		int markerCount = 100;

		int transitionDuration = 500;
		int pixelDistanceToJoinCluster = 100;
		int zoomToBoundsAnimationDuration = 500;
		int showInfoWindowAnimationDuration = 500;
		double expandBoundsFactor = 0.67d;

		SinglePointClickBehavior singlePointClickBehavior = SinglePointClickBehavior.SHOW_INFO_WINDOW;
		ClusterClickBehavior clusterClickBehavior = ClusterClickBehavior.ZOOM_TO_BOUNDS;
		ClusterInfoWindowClickBehavior clusterInfoWindowClickBehavior = ClusterInfoWindowClickBehavior.ZOOM_TO_BOUNDS;
	}

}
