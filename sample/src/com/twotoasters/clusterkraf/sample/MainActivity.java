package com.twotoasters.clusterkraf.sample;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class MainActivity extends ListActivity implements NavigationListAdapter.Host {

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
		int points = (Integer)getListView().getItemAtPosition(position);
		Intent i = new Intent(this, RandomMarkerActivity.class);
		i.putExtra(RandomMarkerActivity.EXTRA_POINTS, points);
		startActivity(i);
	}

}
