package com.twotoasters.clusterkraf;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import com.google.android.gms.maps.Projection;

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

		Builder(Projection projection, ArrayList<ClusterPoint> previousClusterPoints) {
			this.projectionRef = new WeakReference<Projection>(projection);
			this.previousClusterPoints = previousClusterPoints;
		}

		void add(ClusterPoint currentClusterPoint) {
			Projection projection = projectionRef != null ? projectionRef.get() : null;
			if (currentClusterPoint != null && projection != null) {
				boolean animated = false;
				if (previousClusterPoints != null) {
					for (ClusterPoint previousClusterPoint : previousClusterPoints) {
						for (InputPoint previousInputPoint : previousClusterPoint.getPointsInCluster()) {
							if (currentClusterPoint.containsInputPoint(previousInputPoint)) {
								AnimatedTransition transition = getTransition(previousInputPoint);
								if (transition != null) {
									transition.addOriginClusterRelevantInputPoint(previousInputPoint);
								} else {
									transition = new AnimatedTransition(projection, previousClusterPoint, previousInputPoint, currentClusterPoint);
									animatedTransitions.add(transition);
									animated = true;
								}
							}
						}
					}
				}
				if (animated == false) {
					stationaryTransitions.add(currentClusterPoint);
				}
			}
		}

		private AnimatedTransition getTransition(InputPoint relevantInputPoint) {
			for (AnimatedTransition at : animatedTransitions) {
				if (at.destinationContains(relevantInputPoint) && at.originContains(relevantInputPoint)) {
					return at;
				}
			}
			return null;
		}

		ClusterTransitions build() {
			return new ClusterTransitions(this);
		}
	}

}
