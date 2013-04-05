/**
 * @author Carlton Whitehead
 */
package com.twotoasters.clusterkraf;

import com.google.android.gms.maps.GoogleMap;

public class ClusterkrafMapHelper {
	
	private GoogleMap map;
	private final Options options;
	
	/**
	 * construct a Clusterkraf instance to manage your map with customized options.
	 * @param map the GoogleMap to be managed by Clusterkraf
	 * @param options customized options
	 */
	public ClusterkrafMapHelper(GoogleMap map, Options options) {
		this.map = map;
		this.options = options;
	}
	
	/**
	 * construct a Clusterkraf instance to manage your map with default options.
	 * this will help you confirm the library is working, but you probably should
	 * use the overload with custom options that suit your app.
	 * @param map
	 */
	public ClusterkrafMapHelper(GoogleMap map) {
		this(map, new Options());
	}
	
	/**
	 * specify options for the clusterkraf instance
	 */
	public static class Options {
		
		private static final int DEFAULT_TRANSITION_DURATION = 300;
		private int transitionDuration = DEFAULT_TRANSITION_DURATION;
		
		private int pixelDistanceToJoinCluster;
		
		
	}
	
}
