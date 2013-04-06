/**
 * @author Carlton Whitehead
 */
package com.twotoasters.clusterkraf;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.VisibleRegion;

/**
 *
 */
class ClusterTransition {

	private final ClusterPoint originClusterPoint;
	private final ClusterPoint originRelevantInputPointsCluster;
	private final ClusterPoint destinationClusterPoint;

	private ClusterTransition(Projection projection, ClusterPoint originClusterPoint, InputPoint firstRelevantInputPointFromOriginClusterPoint,
			ClusterPoint destinationClusterPoint) {
		originClusterPoint.clearScreenPosition();
		originClusterPoint.buildScreenPosition(projection);
		this.originClusterPoint = originClusterPoint;

		this.originRelevantInputPointsCluster = new ClusterPoint(firstRelevantInputPointFromOriginClusterPoint, projection, true,
				originClusterPoint.getMapPosition());
		this.originRelevantInputPointsCluster.setScreenPosition(originClusterPoint.getScreenPosition());

		this.destinationClusterPoint = destinationClusterPoint;
	}

	private boolean originContains(InputPoint point) {
		return originClusterPoint != null && originClusterPoint.containsInputPoint(point);
	}

	private boolean destinationContains(InputPoint point) {
		return destinationClusterPoint != null && destinationClusterPoint.containsInputPoint(point);
	}

	ClusterPoint getOriginRelevantInputPointsCluster() {
		return originRelevantInputPointsCluster;
	}

	ClusterPoint getDestinationClusterPoint() {
		return destinationClusterPoint;
	}

	static class Builder {

		private WeakReference<Projection> projectionRef;
		private WeakReference<VisibleRegion> visibleRegionRef;

		private final ArrayList<ClusterPoint> previousClusterPointsList;
		private final ArrayList<ClusterTransition> clusterTransitionsList = new ArrayList<ClusterTransition>();

		public Builder(GoogleMap map, ArrayList<ClusterPoint> previousClusterPoints) {
			setMapProjection(map);
			setMapProjectionVisibleRegion(map);

			this.previousClusterPointsList = previousClusterPoints;
		}

		private void setMapProjection(GoogleMap map) {
			if(map != null) {
				projectionRef = new WeakReference<Projection>(map.getProjection());
			}
		}

		private void setMapProjectionVisibleRegion(GoogleMap map) {
			if(map != null) {
				Projection projection = map.getProjection();
				if(projection != null) {
					visibleRegionRef = new WeakReference<VisibleRegion>(projection.getVisibleRegion());
				}
			}
		}

		public void add(ClusterPoint currentClusterPoint) {
			Projection projection = projectionRef != null ? projectionRef.get() : null;
			if(currentClusterPoint != null && projection != null) {
				for(ClusterPoint previousClusterPoint : previousClusterPointsList) {
					for(InputPoint previousInputPoint : previousClusterPoint.getPointsInCluster()) {
						if(currentClusterPoint.containsInputPoint(previousInputPoint)) {

							ClusterTransition transition = getTransition(previousClusterPoint, previousInputPoint, currentClusterPoint);
							if(transition != null) {
								transition.originRelevantInputPointsCluster.add(previousInputPoint);
							} else {
								transition = new ClusterTransition(projection, previousClusterPoint, previousInputPoint, currentClusterPoint);
								clusterTransitionsList.add(transition);
							}
						}
					}
				}
			}
		}

		private ClusterTransition getTransition(ClusterPoint originClusterPoint, InputPoint relevantInputPoint, ClusterPoint destinationClusterPoint) {
			for(ClusterTransition ct : clusterTransitionsList) {
				if(ct.destinationContains(relevantInputPoint) && ct.originContains(relevantInputPoint)) {
					return ct;
				}
			}
			return null;
		}

		public ArrayList<ClusterTransition> build() {
			ArrayList<ClusterTransition> finalClusterTransitions = new ArrayList<ClusterTransition>(clusterTransitionsList.size());
			finalClusterTransitions.addAll(clusterTransitionsList);
			return finalClusterTransitions;
		}
	}
}
