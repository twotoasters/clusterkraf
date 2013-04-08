package com.twotoasters.clusterkraf.sample;

import java.lang.ref.WeakReference;
import java.text.NumberFormat;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends ListActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setListAdapter(new NavigationListAdapter(this));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView,
	 * android.view.View, int, long)
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		int markers = (Integer)getListView().getItemAtPosition(position);
		Intent i = new Intent(this, RandomMarkerActivity.class);
		i.putExtra(RandomMarkerActivity.EXTRA_MARKERS, markers);
		startActivity(i);
	}

	private static class NavigationListAdapter extends BaseAdapter {

		private final WeakReference<MainActivity> activityRef;
		private final WeakReference<LayoutInflater> inflaterRef;

		private final NumberFormat numberFormatter;

		private int[] values = new int[] { 50, 500, 5000, 50000 };

		private NavigationListAdapter(MainActivity activity) {
			activityRef = new WeakReference<MainActivity>(activity);
			inflaterRef = new WeakReference<LayoutInflater>(activity.getLayoutInflater());
			numberFormatter = NumberFormat.getInstance();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getCount()
		 */
		@Override
		public int getCount() {
			return values.length;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getItem(int)
		 */
		@Override
		public Integer getItem(int position) {
			return values[position];
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getItemId(int)
		 */
		@Override
		public long getItemId(int position) {
			return position;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.widget.Adapter#getView(int, android.view.View,
		 * android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				LayoutInflater inflater = inflaterRef.get();
				if (inflater != null) {
					view = inflater.inflate(android.R.layout.simple_list_item_1, null);
					view.setTag(new Holder(view));
				}
			}

			MainActivity ma = activityRef.get();

			Holder holder = (Holder)view.getTag();

			// quantity
			Integer quantity = (Integer)getItem(position);
			String quantityAsString = numberFormatter.format(quantity);
			holder.label.setText(ma.getResources().getQuantityString(R.plurals.random_markers, quantity, quantityAsString));

			return view;
		}

		private static class Holder {
			private TextView label;

			private Holder(View view) {
				label = (TextView)view.findViewById(android.R.id.text1);
			}
		}
	}

}
