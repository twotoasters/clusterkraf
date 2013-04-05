/**
 * @author Carlton Whitehead
 */
package com.twotoasters.clusterkraf;

/**
 *
 */
public interface ClusterTransitionsListener {
	void onClusterTransitionsStarted();
	void onClusterTransitionsFinished();
	void onClusterTransitionsCanceled();
	void onClusterTransitionsNeedMarkersDrawn();
}
