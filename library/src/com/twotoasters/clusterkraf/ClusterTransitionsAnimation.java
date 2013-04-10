/**
 * @author Carlton Whitehead
 */
package com.twotoasters.clusterkraf;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;

/**
 *
 */
class ClusterTransitionsAnimation implements AnimatorListener, AnimatorUpdateListener {

	private final WeakReference<GoogleMap> mapRef;
	private final WeakReference<Options> optionsRef;
	private final WeakReference<Host> hostRef;

	private AnimatedTransitionState state;
	private ClusterTransitions transitions;

	private Marker[] animatedMarkers;
	private Marker[] stationaryMarkers;

	ClusterTransitionsAnimation(GoogleMap map, Options options, Host host) {
		mapRef = new WeakReference<GoogleMap>(map);
		optionsRef = new WeakReference<Options>(options);
		hostRef = new WeakReference<Host>(host);
	}

	void animate(ClusterTransitions transitions) {
		if (this.state == null) {
			Options options = optionsRef.get();
			Host host = hostRef.get();
			if (options != null && host != null) {
				this.state = new AnimatedTransitionState(transitions.animated);
				this.transitions = transitions;
				ObjectAnimator animator = ObjectAnimator.ofFloat(this.state, "value", 0f, 1f);
				animator.addListener(this);
				animator.addUpdateListener(this);
				animator.setDuration(optionsRef.get().getTransitionDuration());
				host.onClusterTransitionStarting();
				animator.start();
			}
		}
	}

	private class AnimatedTransitionState {

		private final ArrayList<AnimatedTransition> transitions;

		private float value;

		private AnimatedTransitionState(ArrayList<AnimatedTransition> transitions) {
			this.transitions = transitions;
		}

		@SuppressWarnings("unused")
		public void setValue(float value) {
			this.value = value;
		}

		public ArrayList<AnimatedTransition> getTransitions() {
			return transitions;
		}

		private LatLng[] getPositions() {
			LatLng[] positions = new LatLng[transitions.size()];
			int i = 0;
			for (AnimatedTransition transition : transitions) {
				LatLng start = transition.getOriginClusterRelevantInputPoints().getMapPosition();
				LatLng end = transition.getDestinationClusterPoint().getMapPosition();
				double currentLat = start.latitude + (value * (end.latitude - start.latitude));
				double currentLon = start.longitude + (value * (end.longitude - start.longitude));
				positions[i++] = new LatLng(currentLat, currentLon);
			}
			return positions;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener#
	 * onAnimationUpdate(com.nineoldandroids.animation.ValueAnimator)
	 */
	@Override
	public void onAnimationUpdate(ValueAnimator animator) {
		if (state != null && animatedMarkers != null) {
			LatLng[] positions = state.getPositions();
			for (int i = 0; i < animatedMarkers.length; i++) {
				animatedMarkers[i].setPosition(positions[i]);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nineoldandroids.animation.Animator.AnimatorListener#onAnimationCancel
	 * (com.nineoldandroids.animation.Animator)
	 */
	@Override
	public void onAnimationCancel(Animator animator) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nineoldandroids.animation.Animator.AnimatorListener#onAnimationEnd
	 * (com.nineoldandroids.animation.Animator)
	 */
	@Override
	public void onAnimationEnd(Animator animator) {
		Host host = hostRef.get();
		if (host != null) {
			host.onClusterTransitionFinished();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nineoldandroids.animation.Animator.AnimatorListener#onAnimationRepeat
	 * (com.nineoldandroids.animation.Animator)
	 */
	@Override
	public void onAnimationRepeat(Animator animator) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.nineoldandroids.animation.Animator.AnimatorListener#onAnimationStart
	 * (com.nineoldandroids.animation.Animator)
	 */
	@Override
	public void onAnimationStart(Animator animator) {
		GoogleMap map = mapRef.get();
		Options options = optionsRef.get();
		if (map != null && options != null) {
			MarkerOptionsChooser moc = options.getMarkerOptionsChooser();

			// plot animated transitions at starting point
			ArrayList<AnimatedTransition> animatedTransitions = state.getTransitions();
			int animatedTransitionCount = animatedTransitions.size();
			animatedMarkers = new Marker[animatedTransitionCount];
			for (int i = 0; i < animatedTransitionCount; i++) {
				ClusterPoint origin = animatedTransitions.get(i).getOriginClusterRelevantInputPoints();
				animatedMarkers[i] = addMarker(map, moc, origin);
			}

			// plot stationary clusters
			ArrayList<ClusterPoint> stationaryClusters = transitions.stationary;
			int stationaryClusterCount = stationaryClusters.size();
			stationaryMarkers = new Marker[stationaryClusterCount];
			for (int i = 0; i < stationaryClusterCount; i++) {
				ClusterPoint stationaryCluster = stationaryClusters.get(i);
				stationaryMarkers[i] = addMarker(map, moc, stationaryCluster);
			}
		}

		Host host = hostRef.get();
		host.onClusterTransitionStarted();
	}

	void onHostPlottedDestinationClusterPoints() {
		for (Marker marker : animatedMarkers) {
			marker.remove();
		}

		for (Marker marker : stationaryMarkers) {
			marker.remove();
		}

		state = null;
		transitions = null;
		animatedMarkers = null;
		stationaryMarkers = null;
	}

	private Marker addMarker(GoogleMap map, MarkerOptionsChooser moc, ClusterPoint clusterPoint) {
		MarkerOptions mo = new MarkerOptions();
		mo.position(clusterPoint.getMapPosition());
		if (moc != null) {
			moc.choose(mo, clusterPoint);
		}
		return map.addMarker(mo);
	}

	interface Host {
		void onClusterTransitionStarting();

		void onClusterTransitionStarted();

		void onClusterTransitionFinished();
	}
}
