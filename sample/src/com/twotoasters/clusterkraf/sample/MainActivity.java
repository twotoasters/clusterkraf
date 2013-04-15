package com.twotoasters.clusterkraf.sample;

import android.app.ExpandableListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.twotoasters.clusterkraf.Options.ClusterClickBehavior;
import com.twotoasters.clusterkraf.Options.ClusterInfoWindowClickBehavior;

public class MainActivity extends ExpandableListActivity {

	private static final int GROUP_DEFAULTS = 0;
	private static final int GROUP_ADVANCED = 1;

	private final RandomMarkerActivity.Options advancedOptions = new RandomMarkerActivity.Options();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setListAdapter(new Adapter());
	}

	@Override
	public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
		Intent i = new Intent(this, RandomMarkerActivity.class);
		if (isAdvancedStartButton(groupPosition, childPosition)) {
			// mock options
			advancedOptions.clusterClickBehavior = ClusterClickBehavior.SHOW_INFO_WINDOW;
			advancedOptions.clusterInfoWindowClickBehavior = ClusterInfoWindowClickBehavior.ZOOM_TO_BOUNDS;
			advancedOptions.transitionDuration = 900;

			i.putExtra(RandomMarkerActivity.EXTRA_OPTIONS, advancedOptions);
		}
		startActivity(i);
		return true;
	}

	private boolean isAdvancedChooser(int groupPosition, int childPosition) {
		return groupPosition == GROUP_ADVANCED && childPosition == 0;
	}

	private boolean isAdvancedStartButton(int groupPosition, int childPosition) {
		return groupPosition == GROUP_ADVANCED && childPosition == 1;
	}

	private class Adapter extends BaseExpandableListAdapter {

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return null;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return (groupPosition * 10) + childPosition;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
			View view = convertView;
			boolean isAdvancedChooser = isAdvancedChooser(groupPosition, childPosition);
			if (view == null) {
				LayoutInflater inflater = getLayoutInflater();
				if (isAdvancedChooser) {
					view = inflater.inflate(android.R.layout.simple_list_item_1, null);
					// TODO advanced chooser layout
				} else {
					view = inflater.inflate(android.R.layout.simple_list_item_1, null);
				}
			}

			if (isAdvancedChooser) {
				TextView tv = (TextView)view.findViewById(android.R.id.text1);
				tv.setText("TODO: advanced chooser");
				// TODO: advanced chooser
			} else {
				TextView text1 = (TextView)view.findViewById(android.R.id.text1);
				text1.setText(getResources().getStringArray(R.array.modes_start)[groupPosition]);
			}

			return view;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return groupPosition == GROUP_DEFAULTS ? 1 : 2;
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
			return isAdvancedChooser(groupPosition, childPosition) == false;
		}

	}

}
