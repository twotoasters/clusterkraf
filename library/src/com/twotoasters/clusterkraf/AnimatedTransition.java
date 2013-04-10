/**
 * @author Carlton Whitehead
 */
package com.twotoasters.clusterkraf;

import com.google.android.gms.maps.Projection;

/**
 *
 */
class AnimatedTransition {

	private final ClusterPoint originClusterPoint;
	private final ClusterPoint originClusterRelevantInputPoints;
	private final ClusterPoint destinationClusterPoint;

	AnimatedTransition(Projection projection, ClusterPoint originClusterPoint, InputPoint firstRelevantInputPointFromOriginClusterPoint,
			ClusterPoint destinationClusterPoint) {
		originClusterPoint.clearScreenPosition();
		originClusterPoint.buildScreenPosition(projection);
		this.originClusterPoint = originClusterPoint;

		this.originClusterRelevantInputPoints = new ClusterPoint(firstRelevantInputPointFromOriginClusterPoint, projection, true,
				originClusterPoint.getMapPosition());
		this.originClusterRelevantInputPoints.setScreenPosition(originClusterPoint.getScreenPosition());

		this.destinationClusterPoint = destinationClusterPoint;
	}

	boolean originContains(InputPoint point) {
		return originClusterPoint != null && originClusterPoint.containsInputPoint(point);
	}

	boolean destinationContains(InputPoint point) {
		return destinationClusterPoint != null && destinationClusterPoint.containsInputPoint(point);
	}

	ClusterPoint getOriginClusterRelevantInputPoints() {
		return originClusterRelevantInputPoints;
	}

	ClusterPoint getDestinationClusterPoint() {
		return destinationClusterPoint;
	}

	void addOriginClusterRelevantInputPoint(InputPoint previousInputPoint) {
		originClusterRelevantInputPoints.add(previousInputPoint);
	}
}
