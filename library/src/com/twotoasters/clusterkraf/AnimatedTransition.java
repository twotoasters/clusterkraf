package com.twotoasters.clusterkraf;

import com.google.android.gms.maps.Projection;

/**
 * Models the animated transition of a cluster
 */
class AnimatedTransition {

	private final ClusterPoint originClusterPoint;
	private final ClusterPoint originClusterRelevantInputPoints;
	private final ClusterPoint destinationClusterPoint;
	private final boolean spans180Meridian;

	AnimatedTransition(Projection projection, ClusterPoint originClusterPoint, InputPoint firstRelevantInputPointFromOriginClusterPoint,
			ClusterPoint destinationClusterPoint) {
		originClusterPoint.clearScreenPosition();
		originClusterPoint.buildScreenPosition(projection);
		this.originClusterPoint = originClusterPoint;

		this.originClusterRelevantInputPoints = new ClusterPoint(firstRelevantInputPointFromOriginClusterPoint, projection, true,
				originClusterPoint.getMapPosition());
		this.originClusterRelevantInputPoints.setScreenPosition(originClusterPoint.getScreenPosition());

		this.destinationClusterPoint = destinationClusterPoint;

		this.spans180Meridian = Math.abs((originClusterPoint.getMapPosition().longitude) - (destinationClusterPoint.getMapPosition().longitude)) > 180;
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

	/**
	 * @return whether (true) or not (false) the origin and destination
	 *         longitudes are on opposite sides of the 180 meridian
	 */
	boolean spans180Meridian() {
		return spans180Meridian;
	}
}
