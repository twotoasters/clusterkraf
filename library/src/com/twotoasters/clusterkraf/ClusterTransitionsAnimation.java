package com.twotoasters.clusterkraf;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;

class ClusterTransitionsAnimation implements AnimatorListener, AnimatorUpdateListener {

	private final WeakReference<GoogleMap> mapRef;
	private final WeakReference<Options> optionsRef;
	private final WeakReference<Host> hostRef;

	private ObjectAnimator animator;
	private AnimatedTransitionState state;
	private ClusterTransitions transitions;

	private Marker[] animatedMarkers;
	private Marker[] stationaryMarkers;

	private final HashMap<Marker, AnimatedTransition> animatedTransitionsByMarker = new HashMap<Marker, AnimatedTransition>();
	private final HashMap<Marker, ClusterPoint> stationaryTransitionsByMarker = new HashMap<Marker, ClusterPoint>();

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
				animator = ObjectAnimator.ofFloat(this.state, "value", 0f, 1f);
				animator.addListener(this);
				animator.addUpdateListener(this);
				animator.setDuration(options.getTransitionDuration());
				animator.setInterpolator(options.getTransitionInterpolator());
				host.onClusterTransitionStarting();
				animator.start();
			}
		}
	}

	ClusterPoint getAnimatedDestinationClusterPoint(Marker marker) {
		AnimatedTransition animatedTransition = animatedTransitionsByMarker.get(marker);
		if (animatedTransition != null) {
			return animatedTransition.getDestinationClusterPoint();
		}
		return null;
	}

	ClusterPoint getStationaryClusterPoint(Marker marker) {
		return stationaryTransitionsByMarker.get(marker);
	}

	void cancel() {
		if (animator != null) {
			animator.cancel();
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
				double currentLon;
				if (transition.spans180Meridian() == false) {
					currentLon = start.longitude + (value * (end.longitude - start.longitude));
				} else {
					/**
					 * transitions that span the 180 meridian cannot be animated
					 * directly from their start longitude to end longitude
					 * (they would travel the long way around the globe), so we
					 * shift their longitude so their trajectory crosses the 180
					 * meridian (instead of the prime meridian).
					 */
					double shiftedStartLon = start.longitude < 0 ? start.longitude + 360 : start.longitude;
					double shiftedEndLon = end.longitude < 0 ? end.longitude + 360 : end.longitude;
					double shiftedCurrentLon = shiftedStartLon + (value * (shiftedEndLon - shiftedStartLon));
					currentLon = shiftedCurrentLon - 360;
				}
				positions[i++] = new LatLng(currentLat, currentLon);
			}
			return positions;
		}
	}

	/**
	 * @see com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener#
	 *      onAnimationUpdate(com.nineoldandroids.animation.ValueAnimator)
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

	/**
	 * @see com.nineoldandroids.animation.Animator.AnimatorListener#onAnimationCancel
	 *      (com.nineoldandroids.animation.Animator)
	 */
	@Override
	public void onAnimationCancel(Animator animator) {
		// no-op
	}

	/**
	 * @see com.nineoldandroids.animation.Animator.AnimatorListener#onAnimationEnd
	 *      (com.nineoldandroids.animation.Animator)
	 */
	@Override
	public void onAnimationEnd(Animator animator) {
		Host host = hostRef.get();
		if (host != null) {
			host.onClusterTransitionFinished();
		}
	}

	/**
	 * @see com.nineoldandroids.animation.Animator.AnimatorListener#onAnimationRepeat
	 *      (com.nineoldandroids.animation.Animator)
	 */
	@Override
	public void onAnimationRepeat(Animator animator) {
		// no-op
	}

	/**
	 * Add temporary stationary and animated transition markers, holds onto
	 * references, and calls back to the Host when finished
	 * 
	 * @see com.nineoldandroids.animation.Animator.AnimatorListener#onAnimationStart
	 *      (com.nineoldandroids.animation.Animator)
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
				AnimatedTransition animatedTransition = animatedTransitions.get(i);
				ClusterPoint origin = animatedTransition.getOriginClusterRelevantInputPoints();
				Marker marker = addMarker(map, moc, origin);

				animatedMarkers[i] = marker;
				animatedTransitionsByMarker.put(marker, animatedTransition);
			}

			// plot stationary clusters
			ArrayList<ClusterPoint> stationaryClusters = transitions.stationary;
			int stationaryClusterCount = stationaryClusters.size();
			if (stationaryClusterCount > 0) {
				stationaryMarkers = new Marker[stationaryClusterCount];
				for (int i = 0; i < stationaryClusterCount; i++) {
					ClusterPoint stationaryCluster = stationaryClusters.get(i);
					Marker marker = addMarker(map, moc, stationaryCluster);

					stationaryMarkers[i] = marker;
					stationaryTransitionsByMarker.put(marker, stationaryCluster);
				}
			}
		}

		Host host = hostRef.get();
		host.onClusterTransitionStarted();
	}

	/**
	 * The Host must call this after it plots its cluster points so that the
	 * stationary and animated transition markers can be removed.
	 */
	void onHostPlottedDestinationClusterPoints() {
		if (animatedMarkers != null && animatedMarkers.length > 0) {
			for (Marker marker : animatedMarkers) {
				marker.remove();
			}
			animatedMarkers = null;
		}

		if (stationaryMarkers != null && stationaryMarkers.length > 0) {
			for (Marker marker : stationaryMarkers) {
				marker.remove();
			}
			stationaryMarkers = null;
		}

		state = null;
		transitions = null;
		animatedTransitionsByMarker.clear();
		stationaryTransitionsByMarker.clear();
		animator = null;
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
		/**
		 * Called immediately prior to a cluster transition's animation starting
		 */
		void onClusterTransitionStarting();

		/**
		 * Called when the cluster transition's animation has started
		 */
		void onClusterTransitionStarted();

		/**
		 * Called when the cluster transition's animation has finished
		 */
		void onClusterTransitionFinished();
	}
}
