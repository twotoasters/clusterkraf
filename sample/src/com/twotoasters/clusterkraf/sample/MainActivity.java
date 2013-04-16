package com.twotoasters.clusterkraf.sample;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;

import com.twotoasters.clusterkraf.Options.ClusterClickBehavior;
import com.twotoasters.clusterkraf.Options.ClusterInfoWindowClickBehavior;
import com.twotoasters.clusterkraf.sample.GenerateRandomMarkersTask.GeographicDistribution;

public class MainActivity extends ExpandableListActivity implements OnChildClickListener {

	private static final int GROUP_DEFAULTS = 0;
	private static final int GROUP_ADVANCED = 1;

	private final RandomMarkerActivity.Options advancedOptions = new RandomMarkerActivity.Options();

	private String[] advancedLabels;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		advancedLabels = getResources().getStringArray(R.array.advanced_labels);
		setListAdapter(new Adapter());
		ExpandableListView elv = getExpandableListView();
		elv.setOnChildClickListener(this);
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
		if (isStartButton(groupPosition, childPosition)) {
			Intent i = new Intent(this, RandomMarkerActivity.class);
			if (isAdvancedStartButton(groupPosition, childPosition)) {
				// TODO: chosen instead of mock options
				advancedOptions.markerCount = 25000;
				advancedOptions.clusterClickBehavior = ClusterClickBehavior.SHOW_INFO_WINDOW;
				advancedOptions.clusterInfoWindowClickBehavior = ClusterInfoWindowClickBehavior.ZOOM_TO_BOUNDS;
				advancedOptions.transitionDuration = 900;
				advancedOptions.geographicDistribution = GeographicDistribution.Worldwide;
				advancedOptions.expandBoundsFactor = 0;
				i.putExtra(RandomMarkerActivity.EXTRA_OPTIONS, advancedOptions);
			}
			startActivity(i);
			return true;
		} else if (isAdvancedChooser(groupPosition, childPosition)) {
			// TODO: open appropriate dialog for option
			return false;
		}
		return false;
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
				text2.setText("TODO: setting goes here");
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
