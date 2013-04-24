package com.twotoasters.clusterkraf.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

public class NormalModeFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_normal_mode, null);

		view.findViewById(R.id.button).setOnClickListener(new StartButtonOnClickListener());

		return view;
	}

	private class StartButtonOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			Intent i = new Intent(getActivity(), SampleActivity.class);
			startActivity(i);
		}

	}

}
