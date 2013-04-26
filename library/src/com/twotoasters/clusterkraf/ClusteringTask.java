package com.twotoasters.clusterkraf;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Process;

import com.google.android.gms.maps.Projection;
import com.twotoasters.clusterkraf.ClusteringTask.Argument;
import com.twotoasters.clusterkraf.ClusteringTask.Result;

/**
 *
 */
public class ClusteringTask extends AsyncTask<Argument, Void, Result> {

	private final Host host;

	ClusteringTask(Host host) {
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
			ClustersBuilder builder = new ClustersBuilder(arg.projection, arg.options, arg.previousClusters);
			builder.addAll(arg.points);
			result.currentClusters = builder.build();
			result.projection = arg.projection;

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
			host.onClusteringTaskPostExecute(result);
		}
	}

	static class Argument {
		Projection projection;
		Options options;
		ArrayList<InputPoint> points;
		ArrayList<ClusterPoint> previousClusters;
	}

	static class Result {
		Projection projection;
		ArrayList<ClusterPoint> currentClusters;
	}

	interface Host {
		void onClusteringTaskPostExecute(Result result);
	}

}
