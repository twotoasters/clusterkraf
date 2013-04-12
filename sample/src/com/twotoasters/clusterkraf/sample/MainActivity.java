package com.twotoasters.clusterkraf.sample;

import android.app.ExpandableListActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

public class MainActivity extends ExpandableListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setListAdapter(new Adapter());
	}

	private class Adapter extends BaseExpandableListAdapter {

		private static final int GROUP_DEFAULTS = 0;
		private static final int GROUP_ADVANCED = 1;

		@Override
		public Object getChild(int groupPosition, int childPosition) {
			Object child = null;
			switch(groupPosition) {
				case GROUP_DEFAULTS:
					// TODO
					break;
				case GROUP_ADVANCED:
					// TODO
					break;
			}
			if (groupPosition == GROUP_ADVANCED) {

			}
			return child;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return groupPosition == GROUP_DEFAULTS ? 1 : 1;
			// TODO: actual GROUP_ADVANCED children count (dynamic?)
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
				switch(groupPosition) {
					case GROUP_DEFAULTS:

						break;
					case GROUP_ADVANCED:
						// TODO: inflate advanced
						break;
				}
			}
			return view;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return false;
		}

	}

}
