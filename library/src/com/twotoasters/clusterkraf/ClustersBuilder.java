/**
 * @author Carlton Whitehead
 */
package com.twotoasters.clusterkraf;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.VisibleRegion;

/**
 *
 */
class ClustersBuilder {

	private final Options options;

	private final ArrayList<InputPoint> relevantInputPointsList = new ArrayList<InputPoint>();
	private final HashSet<InputPoint> releventInputPointsSet = new HashSet<InputPoint>();

	private final WeakReference<Projection> projectionRef;
	private final WeakReference<VisibleRegion> visibleRegionRef;

	ClustersBuilder(GoogleMap map, Options options, ArrayList<ClusterPoint> initialClusteredPoints) {
		this.options = options;

		Projection projection = map.getProjection();
		this.projectionRef = new WeakReference<Projection>(projection);
		this.visibleRegionRef = new WeakReference<VisibleRegion>(projection.getVisibleRegion());

		if(initialClusteredPoints != null) {
			addRelevantInitialInputPoints(initialClusteredPoints);
		}
	}

	private void addRelevantInitialInputPoints(ArrayList<ClusterPoint> initialClusteredPoints) {
		for(ClusterPoint clusterPoint : initialClusteredPoints) {
			clusterPoint.clearScreenPosition();
			for(InputPoint point : clusterPoint.getPointsInCluster()) {
				add(point);
			}
		}
	}

	private Projection getProjection() {
		return projectionRef.get();
	}

	private VisibleRegion getVisibleRegion() {
		return visibleRegionRef.get();
	}

	void add(InputPoint point) {
		if(point != null) {
			Projection projection = getProjection();
			VisibleRegion visibleRegion = getVisibleRegion();
			if(visibleRegion != null) {
				LatLngBounds bounds = visibleRegion.latLngBounds;
				if(bounds != null && bounds.contains(point.getMapPosition()) && !releventInputPointsSet.contains(point)) {
					point.buildScreenPosition(projection);
					relevantInputPointsList.add(point);
					releventInputPointsSet.add(point);
				}
			}
		}
	}

	ArrayList<ClusterPoint> build() {
		Projection projection = getProjection();
		ArrayList<ClusterPoint> clusteredPoints = null;
		if(projection != null) {
			clusteredPoints = new ArrayList<ClusterPoint>(relevantInputPointsList.size());
			for(InputPoint point : relevantInputPointsList) {
				boolean addedToExistingCluster = false;
				for(ClusterPoint clusterPoint : clusteredPoints) {
					if(clusterPoint.getPixelDistanceFrom(point) <= options.getPixelDistanceToJoinCluster()) {
						clusterPoint.add(point);
						addedToExistingCluster = true;
						break;
					}
				}
				if(addedToExistingCluster == false) {
					clusteredPoints.add(new ClusterPoint(point, projection, false));
				}
			}
		}
		return clusteredPoints;
	}

}
