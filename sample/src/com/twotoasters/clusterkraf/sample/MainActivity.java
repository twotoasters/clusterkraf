/**
 * @author Carlton Whitehead
 */
package com.twotoasters.clusterkraf.sample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.viewpagerindicator.TabPageIndicator;

public class MainActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ViewPager pager = (ViewPager)findViewById(R.id.pager);
		pager.setAdapter(new ModeAdapter(getSupportFragmentManager()));

		TabPageIndicator tabPageIndicator = (TabPageIndicator)findViewById(R.id.indicator);
		tabPageIndicator.setViewPager(pager);

	}

	private class ModeAdapter extends FragmentPagerAdapter {

		private final String[] modesLabels;

		public ModeAdapter(FragmentManager fm) {
			super(fm);
			modesLabels = getResources().getStringArray(R.array.modes_labels);
		}

		@Override
		public Fragment getItem(int position) {
			switch(position) {
				case 0:
					return new NormalModeFragment();
				case 1:
				default:
					return new AdvancedModeFragment();
			}
		}

		@Override
		public int getCount() {
			return modesLabels.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return modesLabels[position];
		}
	}

}
