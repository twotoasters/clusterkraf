package com.twotoasters.clusterkraf.sample;

import java.text.NumberFormat;
import java.util.Arrays;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;

import com.twotoasters.clusterkraf.Options.ClusterClickBehavior;
import com.twotoasters.clusterkraf.Options.ClusterInfoWindowClickBehavior;
import com.twotoasters.clusterkraf.Options.SinglePointClickBehavior;
import com.twotoasters.clusterkraf.sample.GenerateRandomMarkersTask.GeographicDistribution;

public class MainActivity extends FragmentActivity implements OnChildClickListener, SingleChoiceDialogFragment.Host {

	private static final int GROUP_DEFAULTS = 0;
	private static final int GROUP_ADVANCED = 1;

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

	private final int[] pointsCountNearTwoToasters = new int[] { 1, 10, 25, 50, 100, 250, 500, 1000, 2500 };
	private final int[] pointsCountWorldwide = new int[] { 1, 100, 250, 500, 1000, 2500, 5000, 10000, 25000 };
	private final int[] animationDurations = new int[] { 300, 500, 700, 1000, 2000, 5000, 10000 };
	private final Class[] interpolators = new Class[] { AccelerateDecelerateInterpolator.class, AccelerateInterpolator.class, AnticipateInterpolator.class,
			AnticipateOvershootInterpolator.class, BounceInterpolator.class, DecelerateInterpolator.class, LinearInterpolator.class,
			OvershootInterpolator.class };
	private final int[] pixelDistanceToJoinCluster = new int[] { 100, 150, 200, 250, 300 };
	private final double[] expandBoundsFactors = new double[] { 0d, 0.25d, 0.33d, 0.5d, 0.67d, 0.75d, 1.0d };

	private RandomMarkerActivity.Options advancedOptions;

	private String[] advancedLabels;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		advancedLabels = getResources().getStringArray(R.array.advanced_labels);

		if (savedInstanceState != null) {
			advancedOptions = (RandomMarkerActivity.Options)savedInstanceState.getSerializable(RandomMarkerActivity.EXTRA_OPTIONS);
		} else {
			if (advancedOptions == null) {
				advancedOptions = new RandomMarkerActivity.Options();
			}
		}

		setContentView(android.R.layout.expandable_list_content);

		ExpandableListView listView = (ExpandableListView)findViewById(android.R.id.list);
		listView.setAdapter(new Adapter());
		listView.setOnChildClickListener(this);
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
		if (isStartButton(groupPosition, childPosition)) {
			Intent i = new Intent(this, RandomMarkerActivity.class);
			if (isAdvancedStartButton(groupPosition, childPosition)) {
				i.putExtra(RandomMarkerActivity.EXTRA_OPTIONS, advancedOptions);
			}

			startActivity(i);
			return true;
		} else if (isAdvancedChooser(groupPosition, childPosition)) {
			switch(childPosition) {
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

			return true;
		}
		return false;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putSerializable(RandomMarkerActivity.EXTRA_OPTIONS, advancedOptions);
	}

	private void showGeographicDistributionDialog() {
		Bundle args = new Bundle();
		args.putString(SingleChoiceDialogFragment.KEY_TITLE, getString(R.string.advanced_points_geographic_distribution_label));
		args.putStringArray(SingleChoiceDialogFragment.KEY_OPTIONS, getResources().getStringArray(R.array.advanced_points_geographic_distribution_options));
		args.putInt(SingleChoiceDialogFragment.KEY_SELECTED_OPTION, advancedOptions.geographicDistribution == GeographicDistribution.NearTwoToasters ? 0 : 1);
		SingleChoiceDialogFragment fragment = SingleChoiceDialogFragment.newInstance(this, args);
		fragment.show(getSupportFragmentManager(), getString(R.string.advanced_points_geographic_distribution_label));
	}

	private void showPointsCountDialog() {
		Bundle args = new Bundle();
		args.putString(SingleChoiceDialogFragment.KEY_TITLE, getString(R.string.advanced_points_count_label));
		switch(advancedOptions.geographicDistribution) {
			case NearTwoToasters:
				args.putIntArray(SingleChoiceDialogFragment.KEY_OPTIONS, pointsCountNearTwoToasters);
				args.putInt(SingleChoiceDialogFragment.KEY_SELECTED_OPTION, Arrays.binarySearch(pointsCountNearTwoToasters, advancedOptions.pointCount));
				break;
			case Worldwide:
			default:
				args.putIntArray(SingleChoiceDialogFragment.KEY_OPTIONS, pointsCountWorldwide);
				args.putInt(SingleChoiceDialogFragment.KEY_SELECTED_OPTION, Arrays.binarySearch(pointsCountWorldwide, advancedOptions.pointCount));
				break;
		}
		SingleChoiceDialogFragment fragment = SingleChoiceDialogFragment.newInstance(this, args);
		fragment.show(getSupportFragmentManager(), getString(R.string.advanced_points_count_label));
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
		showSingleChoiceDialogFragment(R.string.advanced_pixel_distance_to_join_cluster_label, pixelDistanceToJoinCluster,
				advancedOptions.pixelDistanceToJoinCluster);
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

	private void showSingleChoiceDialogFragment(int titleId, int[] options, int currentChoice) {
		Bundle args = new Bundle();
		String title = getString(titleId);
		args.putString(SingleChoiceDialogFragment.KEY_TITLE, title);
		args.putIntArray(SingleChoiceDialogFragment.KEY_OPTIONS, options);
		args.putInt(SingleChoiceDialogFragment.KEY_SELECTED_OPTION, Arrays.binarySearch(options, currentChoice));
		SingleChoiceDialogFragment fragment = SingleChoiceDialogFragment.newInstance(this, args);
		fragment.show(getSupportFragmentManager(), title);
	}

	private void showSingleChoiceDialogFragment(int titleId, String[] options, int currentChoiceIndex) {
		Bundle args = new Bundle();
		String title = getString(titleId);
		args.putString(SingleChoiceDialogFragment.KEY_TITLE, title);
		args.putStringArray(SingleChoiceDialogFragment.KEY_OPTIONS, options);
		args.putInt(SingleChoiceDialogFragment.KEY_SELECTED_OPTION, currentChoiceIndex);
		SingleChoiceDialogFragment fragment = SingleChoiceDialogFragment.newInstance(this, args);
		fragment.show(getSupportFragmentManager(), title);
	}

	private boolean isStartButton(int groupPosition, int childPosition) {
		return isDefaultStartButton(groupPosition, childPosition) || isAdvancedStartButton(groupPosition, childPosition);
	}

	private boolean isDefaultStartButton(int groupPosition, int childPosition) {
		return groupPosition == GROUP_DEFAULTS;
	}

	private boolean isAdvancedChooser(int groupPosition, int childPosition) {
		return groupPosition == GROUP_ADVANCED && childPosition >= 0 && childPosition < advancedLabels.length;
	}

	private boolean isAdvancedStartButton(int groupPosition, int childPosition) {
		return groupPosition == GROUP_ADVANCED && childPosition == advancedLabels.length;
	}

	@Override
	public void onOptionChosen(String tag, int index) {
		if (getString(R.string.advanced_points_geographic_distribution_label).equals(tag)) {
			onChangeGeographicDistribution(index);
		} else if (getString(R.string.advanced_points_count_label).equals(tag)) {
			onChangePointsCount(index);
		} else if (getString(R.string.advanced_cluster_transition_animation_duration_label).equals(tag)) {
			onChangeClusterTransitionAnimationDuration(index);
		} else if (getString(R.string.advanced_cluster_transition_animation_interpolator_label).equals(tag)) {
			onChangeClusterTransitionAnimationInterpolator(index);
		} else if (getString(R.string.advanced_zoom_to_bounds_animation_duration_label).equals(tag)) {
			onChangeZoomToBoundsAnimationDuration(index);
		} else if (getString(R.string.advanced_show_info_window_animation_duration_label).equals(tag)) {
			onChangeShowInfoWindowAnimationDuration(index);
		} else if (getString(R.string.advanced_pixel_distance_to_join_cluster_label).equals(tag)) {
			onChangePixelDistanceToJoinCluster(index);
		} else if (getString(R.string.advanced_expand_bounds_factor_label).equals(tag)) {
			onChangeExpandBoundsFactor(index);
		} else if (getString(R.string.advanced_single_point_click_behavior_label).equals(tag)) {
			onChangeSinglePointClickBehavior(index);
		} else if (getString(R.string.advanced_cluster_click_behavior_label).equals(tag)) {
			onChangeClusterClickBehavior(index);
		} else if (getString(R.string.advanced_cluster_info_window_click_behavior_label).equals(tag)) {
			onChangeClusterInfoWindowClickBehavior(index);
		}
		ExpandableListView listView = (ExpandableListView)findViewById(android.R.id.list);
		Adapter adapter = (Adapter)listView.getExpandableListAdapter();
		adapter.notifyDataSetChanged();
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
		advancedOptions.pixelDistanceToJoinCluster = pixelDistanceToJoinCluster[index];
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

	private String getShortenedInterpolatorName(String interpolatorCanonicalName) {
		String shortenedInterpolatorName = "null";
		try {
			shortenedInterpolatorName = Class.forName(interpolatorCanonicalName).getSimpleName().replace("Interpolator", "");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return shortenedInterpolatorName;
	}

	private class Adapter extends BaseExpandableListAdapter {

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return null;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return (groupPosition * 100) + childPosition;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
			boolean isAdvancedChooser = isAdvancedChooser(groupPosition, childPosition);
			View view = null;
			LayoutInflater inflater = getLayoutInflater();
			if (isAdvancedChooser) {
				view = inflater.inflate(android.R.layout.simple_list_item_2, null);
			} else {
				view = inflater.inflate(android.R.layout.simple_list_item_1, null);
			}

			TextView text1 = (TextView)view.findViewById(android.R.id.text1);
			if (isAdvancedChooser) {
				TextView text2 = (TextView)view.findViewById(android.R.id.text2);
				text1.setText(advancedLabels[childPosition]);
				String setting = "TODO: setting goes here";
				NumberFormat nf = NumberFormat.getInstance();
				switch(childPosition) {
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
						setting = nf.format(advancedOptions.pixelDistanceToJoinCluster);
						break;
					case CHILD_EXPAND_BOUNDS_FACTOR:
						setting = nf.format(advancedOptions.expandBoundsFactor);
						break;
					case CHILD_SINGLE_POINT_CLICK_BEHAVIOR:
						setting = getResources().getStringArray(R.array.advanced_single_point_click_behavior_options)[Arrays.binarySearch(
								SinglePointClickBehavior.values(), advancedOptions.singlePointClickBehavior)];
						break;
					case CHILD_CLUSTER_CLICK_BEHAVIOR:
						setting = getResources().getStringArray(R.array.advanced_cluster_click_behavior_options)[Arrays.binarySearch(
								ClusterClickBehavior.values(), advancedOptions.clusterClickBehavior)];
						break;
					case CHILD_CLUSTER_INFO_WINDOW_CLICK_BEHAVIOR:
						setting = getResources().getStringArray(R.array.advanced_cluster_info_window_click_behavior_options)[Arrays.binarySearch(
								ClusterInfoWindowClickBehavior.values(), advancedOptions.clusterInfoWindowClickBehavior)];
						break;
				}
				text2.setText(setting);
			} else {
				text1.setText(getResources().getStringArray(R.array.modes_start)[groupPosition]);
			}

			return view;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return groupPosition == GROUP_DEFAULTS ? 1 : advancedLabels.length + 1;
		}

		@Override
		public Object getGroup(int groupPosition) {
			return null;
		}

		@Override
		public int getGroupCount() {
			return 2;
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				view = getLayoutInflater().inflate(android.R.layout.simple_expandable_list_item_2, null);
			}

			TextView text1 = (TextView)view.findViewById(android.R.id.text1);
			text1.setText(getResources().getStringArray(R.array.modes_labels)[groupPosition]);

			TextView text2 = (TextView)view.findViewById(android.R.id.text2);
			text2.setText(getResources().getStringArray(R.array.modes_descriptions)[groupPosition]);

			return view;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

	}

}
