/**
 * @author Carlton Whitehead
 */
package com.twotoasters.clusterkraf;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;

/**
 *
 */
class ClusterTransitions {

	final ArrayList<AnimatedTransition> animated;
	final ArrayList<ClusterPoint> stationary;

	private ClusterTransitions(Builder builder) {
		this.animated = builder.animatedTransitions;
		this.stationary = builder.stationaryTransitions;
	}

	static class Builder {

		private final WeakReference<Projection> projectionRef;
		private final ArrayList<ClusterPoint> previousClusterPoints;

		private final ArrayList<AnimatedTransition> animatedTransitions = new ArrayList<AnimatedTransition>();
		private final ArrayList<ClusterPoint> stationaryTransitions = new ArrayList<ClusterPoint>();

		private final HashMap<InputPoint, AnimatedTransition> animatedTransitionsByInputPoint = new HashMap<InputPoint, AnimatedTransition>();

		Builder(GoogleMap map, ArrayList<ClusterPoint> previousClusterPoints) {
			this.projectionRef = new WeakReference<Projection>(map.getProjection());
			this.previousClusterPoints = previousClusterPoints;
		}

		void add(ClusterPoint currentClusterPoint) {
			Projection projection = projectionRef != null ? projectionRef.get() : null;
			if (currentClusterPoint != null && projection != null) {
				for (ClusterPoint previousClusterPoint : previousClusterPoints) {
					for (InputPoint previousInputPoint : previousClusterPoint.getPointsInCluster()) {
						if (currentClusterPoint.containsInputPoint(previousInputPoint)) {
							AnimatedTransition transition = animatedTransitionsByInputPoint.get(previousInputPoint);
							if (transition != null) {
								transition.addOriginClusterRelevantInputPoint(previousInputPoint);
								animatedTransitionsByInputPoint.put(previousInputPoint, transition);
								return;
							} else {
								transition = new AnimatedTransition(projection, previousClusterPoint, previousInputPoint, currentClusterPoint);
								animatedTransitions.add(transition);
								animatedTransitionsByInputPoint.put(previousInputPoint, transition);
								return;
							}
						}
					}
				}
				stationaryTransitions.add(currentClusterPoint);
			}
		}

		ClusterTransitions build() {
			return new ClusterTransitions(this);
		}
	}

}
