package com.twotoasters.clusterkraf.sample;

import java.lang.ref.WeakReference;

import android.app.Activity;

/**
 *
 */
public class DelayedIndeterminateProgressBarRunnable implements Runnable {

	private final WeakReference<Activity> activityRef;

	public DelayedIndeterminateProgressBarRunnable(Activity activity) {
		this.activityRef = new WeakReference<Activity>(activity);
	}

	@Override
	public void run() {
		Activity activity = activityRef.get();
		if (activity != null) {
			activity.setProgressBarIndeterminateVisibility(true);
		}
	}

}
