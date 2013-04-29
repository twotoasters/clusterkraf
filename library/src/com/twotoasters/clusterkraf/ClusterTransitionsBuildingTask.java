package com.twotoasters.clusterkraf;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Process;

import com.google.android.gms.maps.Projection;
import com.twotoasters.clusterkraf.ClusterTransitionsBuildingTask.Argument;
import com.twotoasters.clusterkraf.ClusterTransitionsBuildingTask.Result;

/**
 *
 */
public class ClusterTransitionsBuildingTask extends AsyncTask<Argument, Void, Result> {

	private final Host host;

	ClusterTransitionsBuildingTask(Host host) {
		this.host = host;
	}

	/**
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected Result doInBackground(Argument... args) {
		Result result = new Result();
		if (args != null && args.length == 1) {
			Process.setThreadPriority(Process.THREAD_PRIORITY_DEFAULT + Process.THREAD_PRIORITY_LESS_FAVORABLE);

			Argument arg = args[0];
			ClusterTransitions.Builder ctb = new ClusterTransitions.Builder(arg.projection, arg.previousClusters);
			if (arg.currentClusters != null) {
				for (ClusterPoint currentClusterPoint : arg.currentClusters) {
					ctb.add(currentClusterPoint);
				}
			}
			result.clusterTransitions = ctb.build();

			Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
		}
		return result;
	}

	/**
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(Result result) {
		super.onPostExecute(result);
		if (isCancelled() == false && result != null && host != null) {
			host.onClusterTransitionsBuildingTaskPostExecute(result);
		}
	}

	static class Argument {
		Projection projection;
		ArrayList<ClusterPoint> previousClusters;
		ArrayList<ClusterPoint> currentClusters;
	}

	static class Result {
		ClusterTransitions clusterTransitions;
	}

	interface Host {
		void onClusterTransitionsBuildingTaskPostExecute(Result result);
	}

}
