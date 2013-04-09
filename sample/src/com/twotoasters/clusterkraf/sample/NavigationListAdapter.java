/**
 * @author Carlton Whitehead
 */
package com.twotoasters.clusterkraf.sample;

import java.lang.ref.WeakReference;
import java.text.NumberFormat;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

class NavigationListAdapter extends BaseAdapter {

	private final WeakReference<Host> hostRef;
	private final WeakReference<LayoutInflater> inflaterRef;
	private final NumberFormat numberFormatter;
	private final int[] values = new int[] { 1, 10, 25, 50, 100, 500, 1000, 2500, 5000 };

	NavigationListAdapter(Host host) {
		hostRef = new WeakReference<Host>(host);
		inflaterRef = new WeakReference<LayoutInflater>(host.getLayoutInflater());
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

		NavigationListAdapter.Holder holder = (NavigationListAdapter.Holder)view.getTag();

		Host host = hostRef.get();
		if (host != null) {
			// quantity
			Integer quantity = (Integer)getItem(position);
			String quantityAsString = numberFormatter.format(quantity);
			holder.label.setText(host.getResources().getQuantityString(R.plurals.count_points, quantity, quantityAsString));
		}

		return view;
	}

	private static class Holder {
		private TextView label;

		private Holder(View view) {
			label = (TextView)view.findViewById(android.R.id.text1);
		}
	}

	public interface Host {
		public Resources getResources();

		public LayoutInflater getLayoutInflater();
	}
}