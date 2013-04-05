/**
 * @author Carlton Whitehead
 */
package com.twotoasters.clusterkraf;

/**
 * specify options for the clusterkraf instance
 */
public class Options {
	
	private static final int DEFAULT_TRANSITION_DURATION = 300;
	private int transitionDuration = DEFAULT_TRANSITION_DURATION;
	
	private int pixelDistanceToJoinCluster;

	/**
	 * @return the transitionDuration
	 */
	public int getTransitionDuration() {
		return transitionDuration;
	}

	/**
	 * @param transitionDuration the transitionDuration to set
	 */
	public void setTransitionDuration(int transitionDuration) {
		this.transitionDuration = transitionDuration;
	}

	/**
	 * @return the pixelDistanceToJoinCluster
	 */
	public int getPixelDistanceToJoinCluster() {
		return pixelDistanceToJoinCluster;
	}

	/**
	 * @param pixelDistanceToJoinCluster the pixelDistanceToJoinCluster to set
	 */
	public void setPixelDistanceToJoinCluster(int pixelDistanceToJoinCluster) {
		this.pixelDistanceToJoinCluster = pixelDistanceToJoinCluster;
	}
	
	
}