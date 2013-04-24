package com.twotoasters.clusterkraf.sample;

import java.text.NumberFormat;
import java.util.Arrays;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.twotoasters.clusterkraf.Options.ClusterClickBehavior;
import com.twotoasters.clusterkraf.Options.ClusterInfoWindowClickBehavior;
import com.twotoasters.clusterkraf.Options.SinglePointClickBehavior;
import com.twotoasters.clusterkraf.sample.RandomPointsProvider.GeographicDistribution;
import com.twotoasters.clusterkraf.sample.SampleActivity.Options;

public class AdvancedModeFragment extends Fragment implements OnItemClickListener, SingleChoiceDialogFragment.Host {

	private static final int CHILD_GEOGRAPHIC_DISTRIBUTION = 0;
	private static final int CHILD_POINTS_COUNT = 1;
	private static final int CHILD_CLUSTER_TRANSITION_ANIMATION_DURATION = 2;
	private static final int CHILD_CLUSTER_TRANSITION_ANIMATION_INTERPOLATOR = 3;
	private static final int CHILD_ZOOM_TO_BOUNDS_ANIMATION_DURATION = 4;
	private static final int CHILD_SHOW_INFO_WINDOW_ANIMATION_DURATION = 5;
	private static final int CHILD_PIXEL_DISTANCE_TO_JOIN_CLUSTER = 6;
	private static final int CHILD_EXPAND_BOUNDS_FACTOR = 7;
	private static final int CHILD_SINGLE_POINT_CLICK_BEHAVIOR = 8;
	private static final int CHILD_CLUSTER_CLICK_BEHAVIOR = 9;
	private static final int CHILD_CLUSTER_INFO_WINDOW_CLICK_BEHAVIOR = 10;

	private static final String TAG_SINGLE_CHOICE_DIALOG_FRAGMENT = SingleChoiceDialogFragment.class.getSimpleName();

	/**
	 * When your data set IS NOT distributed evenly throughout the world, the
	 * user is going to spend more time with all of the points on screen or
	 * within the overdrawn bounds, so you would want to reduce the number of
	 * those points in order to maintain performance.
	 */
	private final int[] pointsCountNearTwoToasters = new int[] { 1, 10, 25, 50, 100, 250, 500, 1000, 2500 };
	/**
	 * When your data set IS distributed evenly throughout the world,
	 * Clusterkraf will optimize out points that are not in view
	 * (expandBoundsFactor == 0), or are far off screen (expandBoundsFactor > 0)
	 */
	private final int[] pointsCountWorldwide = new int[] { 1, 100, 250, 500, 1000, 2500, 5000, 10000, 25000 };
	/**
	 * Some interpolators look better with slightly longer durations.
	 */
	private final int[] animationDurations = new int[] { 300, 500, 700, 1000, 2000, 5000, 10000 };
	/**
	 * Influence the progress of the animation by choosing a different
	 * interpolator than the standard LinearInterpolator.
	 */
	private final Class<?>[] interpolators = new Class[] { AccelerateDecelerateInterpolator.class, AccelerateInterpolator.class, AnticipateInterpolator.class,
			AnticipateOvershootInterpolator.class, BounceInterpolator.class, DecelerateInterpolator.class, LinearInterpolator.class,
			OvershootInterpolator.class };
	/**
	 * Device Independent Pixels distance to join cluster
	 */
	private final int[] dipDistanceToJoinCluster = new int[] { 75, 100, 125, 150, 200, 250 };
	/**
	 * Controls how far off screen to draw markers. 0 offers drastically better
	 * performance with very large data sets when the map is zoomed all the way
	 * out, but markers will often pop into view when panning and zooming out. 1
	 * offers smoother panning and zooming out (markers don't pop into view),
	 * but with some performance expense.
	 */
	private final double[] expandBoundsFactors = new double[] { 0d, 0.25d, 0.33d, 0.5d, 0.67d, 0.75d, 1.0d };

	private SampleActivity.Options advancedOptions = new SampleActivity.Options();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_advanced_mode, null);

		if (savedInstanceState != null) {
			advancedOptions = (Options)savedInstanceState.getSerializable(SampleActivity.EXTRA_OPTIONS);
		}

		ListView list = (ListView)view.findViewById(R.id.list);
		list.setAdapter(new Adapter(getActivity(), getLayoutInflater(savedInstanceState)));
		list.setOnItemClickListener(this);

		view.findViewById(R.id.button).setOnClickListener(new StartButtonOnClickListener());

		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(SampleActivity.EXTRA_OPTIONS, advancedOptions);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		switch(position) {
			case CHILD_GEOGRAPHIC_DISTRIBUTION:
				showGeographicDistributionDialog();
				break;
			case CHILD_POINTS_COUNT:
				showPointsCountDialog();
				break;
			case CHILD_CLUSTER_TRANSITION_ANIMATION_DURATION:
				showClusterTransitionAnimationDurationDialog();
				break;
			case CHILD_CLUSTER_TRANSITION_ANIMATION_INTERPOLATOR:
				showClusterTransitionAnimationInterpolatorDialog();
				break;
			case CHILD_ZOOM_TO_BOUNDS_ANIMATION_DURATION:
				showZoomToBoundsAnimationDurationDialog();
				break;
			case CHILD_SHOW_INFO_WINDOW_ANIMATION_DURATION:
				showInfoWindowAnimationDurationDialog();
				break;
			case CHILD_PIXEL_DISTANCE_TO_JOIN_CLUSTER:
				showPixelDistanceToJoinClusterDialog();
				break;
			case CHILD_EXPAND_BOUNDS_FACTOR:
				showExpandBoundsFactorDialog();
				break;
			case CHILD_SINGLE_POINT_CLICK_BEHAVIOR:
				showSinglePointClickBehaviorDialog();
				break;
			case CHILD_CLUSTER_CLICK_BEHAVIOR:
				showClusterClickBehaviorDialog();
				break;
			case CHILD_CLUSTER_INFO_WINDOW_CLICK_BEHAVIOR:
				showClusterInfoWindowClickBehavior();
				break;
		}
	}

	private String getShortenedInterpolatorName(String interpolatorCanonicalName) {
		String shortenedInterpolatorName = "null";
		try {
			shortenedInterpolatorName = Class.forName(interpolatorCanonicalName).getSimpleName().replace("Interpolator", "");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return shortenedInterpolatorName;
	}

	private void showGeographicDistributionDialog() {
		showSingleChoiceDialogFragment(R.string.advanced_points_geographic_distribution_label,
				getResources().getStringArray(R.array.advanced_points_geographic_distribution_options),
				advancedOptions.geographicDistribution == GeographicDistribution.NearTwoToasters ? 0 : 1);
	}

	private void showPointsCountDialog() {
		int[] options = advancedOptions.geographicDistribution == GeographicDistribution.NearTwoToasters ? pointsCountNearTwoToasters : pointsCountWorldwide;
		showSingleChoiceDialogFragment(R.string.advanced_points_count_label, options, advancedOptions.pointCount);
	}

	private void showClusterTransitionAnimationDurationDialog() {
		showSingleChoiceDialogFragment(R.string.advanced_cluster_transition_animation_duration_label, animationDurations, advancedOptions.transitionDuration);
	}

	private void showClusterTransitionAnimationInterpolatorDialog() {
		String[] options = new String[interpolators.length];
		for (int i = 0; i < interpolators.length; i++) {
			options[i] = getShortenedInterpolatorName(interpolators[i].getCanonicalName());
		}
		showSingleChoiceDialogFragment(R.string.advanced_cluster_transition_animation_interpolator_label, options,
				Arrays.asList(options).indexOf(getShortenedInterpolatorName(advancedOptions.transitionInterpolator)));
	}

	private void showZoomToBoundsAnimationDurationDialog() {
		showSingleChoiceDialogFragment(R.string.advanced_zoom_to_bounds_animation_duration_label, animationDurations,
				advancedOptions.zoomToBoundsAnimationDuration);
	}

	private void showInfoWindowAnimationDurationDialog() {
		showSingleChoiceDialogFragment(R.string.advanced_show_info_window_animation_duration_label, animationDurations,
				advancedOptions.showInfoWindowAnimationDuration);
	}

	private void showPixelDistanceToJoinClusterDialog() {
		showSingleChoiceDialogFragment(R.string.advanced_pixel_distance_to_join_cluster_label, dipDistanceToJoinCluster,
				advancedOptions.dipDistanceToJoinCluster);
	}

	private void showExpandBoundsFactorDialog() {
		String[] options = new String[expandBoundsFactors.length];
		for (int i = 0; i < expandBoundsFactors.length; i++) {
			options[i] = String.valueOf(expandBoundsFactors[i]);
		}
		showSingleChoiceDialogFragment(R.string.advanced_expand_bounds_factor_label, options,
				Arrays.binarySearch(expandBoundsFactors, advancedOptions.expandBoundsFactor));
	}

	private void showSinglePointClickBehaviorDialog() {
		showSingleChoiceDialogFragment(R.string.advanced_single_point_click_behavior_label,
				getResources().getStringArray(R.array.advanced_single_point_click_behavior_options),
				Arrays.binarySearch(SinglePointClickBehavior.values(), advancedOptions.singlePointClickBehavior));
	}

	private void showClusterClickBehaviorDialog() {
		showSingleChoiceDialogFragment(R.string.advanced_cluster_click_behavior_label,
				getResources().getStringArray(R.array.advanced_cluster_click_behavior_options),
				Arrays.binarySearch(ClusterClickBehavior.values(), advancedOptions.clusterClickBehavior));
	}

	private void showClusterInfoWindowClickBehavior() {
		showSingleChoiceDialogFragment(R.string.advanced_cluster_info_window_click_behavior_label,
				getResources().getStringArray(R.array.advanced_cluster_info_window_click_behavior_options),
				Arrays.binarySearch(ClusterInfoWindowClickBehavior.values(), advancedOptions.clusterInfoWindowClickBehavior));
	}

	private void showSingleChoiceDialogFragment(int titleId, int[] choices, int selection) {
		Bundle args = new Bundle();
		args.putString(SingleChoiceDialogFragment.KEY_TITLE, getString(titleId));
		args.putInt(SingleChoiceDialogFragment.KEY_OPTION, titleId);
		args.putIntArray(SingleChoiceDialogFragment.KEY_CHOICES_INTS, choices);
		args.putInt(SingleChoiceDialogFragment.KEY_SELECTION, Arrays.binarySearch(choices, selection));
		SingleChoiceDialogFragment fragment = SingleChoiceDialogFragment.newInstance(args);
		fragment.show(getChildFragmentManager(), TAG_SINGLE_CHOICE_DIALOG_FRAGMENT);
	}

	private void showSingleChoiceDialogFragment(int titleId, String[] choices, int selectionIndex) {
		Bundle args = new Bundle();
		args.putString(SingleChoiceDialogFragment.KEY_TITLE, getString(titleId));
		args.putInt(SingleChoiceDialogFragment.KEY_OPTION, titleId);
		args.putStringArray(SingleChoiceDialogFragment.KEY_CHOICES_STRINGS, choices);
		args.putInt(SingleChoiceDialogFragment.KEY_SELECTION, selectionIndex);
		SingleChoiceDialogFragment fragment = SingleChoiceDialogFragment.newInstance(args);
		fragment.show(getChildFragmentManager(), TAG_SINGLE_CHOICE_DIALOG_FRAGMENT);
	}

	@Override
	public void onOptionChosen(int option, int index) {
		switch(option) {
			case R.string.advanced_points_geographic_distribution_label:
				onChangeGeographicDistribution(index);
				break;
			case R.string.advanced_points_count_label:
				onChangePointsCount(index);
				break;
			case R.string.advanced_cluster_transition_animation_duration_label:
				onChangeClusterTransitionAnimationDuration(index);
				break;
			case R.string.advanced_cluster_transition_animation_interpolator_label:
				onChangeClusterTransitionAnimationInterpolator(index);
				break;
			case R.string.advanced_zoom_to_bounds_animation_duration_label:
				onChangeZoomToBoundsAnimationDuration(index);
				break;
			case R.string.advanced_show_info_window_animation_duration_label:
				onChangeShowInfoWindowAnimationDuration(index);
				break;
			case R.string.advanced_pixel_distance_to_join_cluster_label:
				onChangePixelDistanceToJoinCluster(index);
				break;
			case R.string.advanced_expand_bounds_factor_label:
				onChangeExpandBoundsFactor(index);
				break;
			case R.string.advanced_single_point_click_behavior_label:
				onChangeSinglePointClickBehavior(index);
				break;
			case R.string.advanced_cluster_click_behavior_label:
				onChangeClusterClickBehavior(index);
				break;
			case R.string.advanced_cluster_info_window_click_behavior_label:
				onChangeClusterInfoWindowClickBehavior(index);
				break;

		}
		View view = getView();
		if (view != null) {
			ListView listView = (ListView)view.findViewById(R.id.list);
			Adapter adapter = (Adapter)listView.getAdapter();
			adapter.notifyDataSetChanged();
		}
	}

	private void onChangeGeographicDistribution(int index) {
		if (index == 0) {
			advancedOptions.geographicDistribution = GeographicDistribution.NearTwoToasters;
			if (Arrays.binarySearch(pointsCountNearTwoToasters, advancedOptions.pointCount) < 0) {
				advancedOptions.pointCount = pointsCountNearTwoToasters[4];
			}
		} else {
			advancedOptions.geographicDistribution = GeographicDistribution.Worldwide;
			if (Arrays.binarySearch(pointsCountWorldwide, advancedOptions.pointCount) < 0) {
				advancedOptions.pointCount = pointsCountWorldwide[4];
			}
		}
	}

	private void onChangePointsCount(int index) {
		switch(advancedOptions.geographicDistribution) {
			case NearTwoToasters:
				advancedOptions.pointCount = pointsCountNearTwoToasters[index];
				break;
			case Worldwide:
			default:
				advancedOptions.pointCount = pointsCountWorldwide[index];
				break;
		}
	}

	private void onChangeClusterTransitionAnimationDuration(int index) {
		advancedOptions.transitionDuration = animationDurations[index];
	}

	private void onChangeClusterTransitionAnimationInterpolator(int index) {
		advancedOptions.transitionInterpolator = interpolators[index].getCanonicalName();
	}

	private void onChangeZoomToBoundsAnimationDuration(int index) {
		advancedOptions.zoomToBoundsAnimationDuration = animationDurations[index];
	}

	private void onChangeShowInfoWindowAnimationDuration(int index) {
		advancedOptions.showInfoWindowAnimationDuration = animationDurations[index];
	}

	private void onChangePixelDistanceToJoinCluster(int index) {
		advancedOptions.dipDistanceToJoinCluster = dipDistanceToJoinCluster[index];
	}

	private void onChangeExpandBoundsFactor(int index) {
		advancedOptions.expandBoundsFactor = expandBoundsFactors[index];
	}

	private void onChangeSinglePointClickBehavior(int index) {
		advancedOptions.singlePointClickBehavior = SinglePointClickBehavior.values()[index];
	}

	private void onChangeClusterClickBehavior(int index) {
		advancedOptions.clusterClickBehavior = ClusterClickBehavior.values()[index];
	}

	private void onChangeClusterInfoWindowClickBehavior(int index) {
		advancedOptions.clusterInfoWindowClickBehavior = ClusterInfoWindowClickBehavior.values()[index];
	}

	private class Adapter extends BaseAdapter {

		private final LayoutInflater inflater;
		private final String[] advancedLabels;

		private Adapter(Context context, LayoutInflater inflater) {
			this.inflater = inflater;
			advancedLabels = context.getResources().getStringArray(R.array.advanced_labels);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				view = inflater.inflate(R.layout.item_advanced, null);
			}

			TextView label = (TextView)view.findViewById(R.id.label);
			TextView value = (TextView)view.findViewById(R.id.value);
			label.setText(advancedLabels[position]);
			String setting = "TODO: setting goes here";
			NumberFormat nf = NumberFormat.getInstance();
			switch(position) {
				case CHILD_GEOGRAPHIC_DISTRIBUTION:
					setting = getResources().getStringArray(R.array.advanced_points_geographic_distribution_options)[Arrays.binarySearch(
							GeographicDistribution.values(), advancedOptions.geographicDistribution)];
					break;
				case CHILD_POINTS_COUNT:
					setting = nf.format(advancedOptions.pointCount);
					break;
				case CHILD_CLUSTER_TRANSITION_ANIMATION_DURATION:
					setting = nf.format(advancedOptions.transitionDuration);
					break;
				case CHILD_CLUSTER_TRANSITION_ANIMATION_INTERPOLATOR:
					setting = getShortenedInterpolatorName(advancedOptions.transitionInterpolator);
					break;
				case CHILD_ZOOM_TO_BOUNDS_ANIMATION_DURATION:
					setting = nf.format(advancedOptions.zoomToBoundsAnimationDuration);
					break;
				case CHILD_SHOW_INFO_WINDOW_ANIMATION_DURATION:
					setting = nf.format(advancedOptions.showInfoWindowAnimationDuration);
					break;
				case CHILD_PIXEL_DISTANCE_TO_JOIN_CLUSTER:
					setting = nf.format(advancedOptions.dipDistanceToJoinCluster);
					break;
				case CHILD_EXPAND_BOUNDS_FACTOR:
					setting = nf.format(advancedOptions.expandBoundsFactor);
					break;
				case CHILD_SINGLE_POINT_CLICK_BEHAVIOR:
					setting = getResources().getStringArray(R.array.advanced_single_point_click_behavior_options)[Arrays.binarySearch(
							SinglePointClickBehavior.values(), advancedOptions.singlePointClickBehavior)];
					break;
				case CHILD_CLUSTER_CLICK_BEHAVIOR:
					setting = getResources().getStringArray(R.array.advanced_cluster_click_behavior_options)[Arrays.binarySearch(ClusterClickBehavior.values(),
							advancedOptions.clusterClickBehavior)];
					break;
				case CHILD_CLUSTER_INFO_WINDOW_CLICK_BEHAVIOR:
					setting = getResources().getStringArray(R.array.advanced_cluster_info_window_click_behavior_options)[Arrays.binarySearch(
							ClusterInfoWindowClickBehavior.values(), advancedOptions.clusterInfoWindowClickBehavior)];
					break;
			}
			value.setText(setting);

			return view;
		}

		@Override
		public int getCount() {
			return advancedLabels.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
	}

	private class StartButtonOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent i = new Intent(getActivity(), SampleActivity.class);
			i.putExtra(SampleActivity.EXTRA_OPTIONS, advancedOptions);
			startActivity(i);
		}

	}

}
