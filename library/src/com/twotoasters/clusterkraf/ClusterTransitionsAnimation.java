/**
 * @author Carlton Whitehead
 */
package com.twotoasters.clusterkraf;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.MarkerOptionsCreator;
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

	private State state;

	private Marker[] markers;

	ClusterTransitionsAnimation(GoogleMap map, Options options, Host host) {
		mapRef = new WeakReference<GoogleMap>(map);
		optionsRef = new WeakReference<Options>(options);
		hostRef = new WeakReference<Host>(host);
	}

	void animate(ArrayList<ClusterTransition> transitions) {
		if (state == null) {
			Options options = optionsRef.get();
			Host host = hostRef.get();
			if (options != null && host != null) {
				state = new State(transitions);
				ObjectAnimator animator = ObjectAnimator.ofFloat(state, "value", 0f, 1f);
				animator.addListener(this);
				animator.addUpdateListener(this);
				animator.setDuration(optionsRef.get().getTransitionDuration());
				host.onClusterTransitionStarting();
				animator.start();
			}
		}
	}

	private class State {

		private final ArrayList<ClusterTransition> transitions;

		private float value;

		private State(ArrayList<ClusterTransition> transitions) {
			this.transitions = transitions;
		}

		public void setValue(float value) {
			this.value = value;
		}

		public ArrayList<ClusterTransition> getTransitions() {
			return transitions;
		}

		private LatLng[] getPositions() {
			LatLng[] positions = new LatLng[transitions.size()];
			int i = 0;
			for (ClusterTransition transition : transitions) {
				LatLng start = transition.getOriginRelevantInputPointsCluster().getMapPosition();
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
		LatLng[] positions = state.getPositions();
		for (int i = 0; i < markers.length; i++) {
			markers[i].setPosition(positions[i]);
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
			ArrayList<ClusterTransition> transitions = state.getTransitions();
			int transitionCount = transitions.size();
			markers = new Marker[transitionCount];
			MarkerIconChooser mic = options.getMarkerIconChooser();
			
			for (int i = 0; i < transitionCount; i++) {
				
				ClusterTransition transition = transitions.get(i);
				ClusterPoint origin = transition.getOriginRelevantInputPointsCluster();
				
				MarkerOptions mo = new MarkerOptions();
				mo.position(origin.getMapPosition());
				if (mic != null) {
					mic.choose(mo, origin);
				}
				
				Marker marker = map.addMarker(mo);
				
				markers[i] = marker;
			}
		}
	}
	
	void onHostPlottedDestinationClusterPoints() {
		for (Marker marker : markers) {
			marker.remove();
		}
		
		state = null;
		markers = null;
	}

	interface Host {
		void onClusterTransitionStarting();
		void onClusterTransitionStarted();
		void onClusterTransitionFinished();
	}
}
