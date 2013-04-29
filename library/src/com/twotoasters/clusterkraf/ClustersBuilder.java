package com.twotoasters.clusterkraf;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;

import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.VisibleRegion;

class ClustersBuilder {

	private final Options options;

	private final ArrayList<InputPoint> relevantInputPointsList = new ArrayList<InputPoint>();
	private final HashSet<InputPoint> releventInputPointsSet = new HashSet<InputPoint>();

	private final WeakReference<Projection> projectionRef;
	private final WeakReference<VisibleRegion> visibleRegionRef;

	ClustersBuilder(Projection projection, Options options, ArrayList<ClusterPoint> initialClusteredPoints) {
		this.options = options;

		this.projectionRef = new WeakReference<Projection>(projection);
		this.visibleRegionRef = new WeakReference<VisibleRegion>(projection.getVisibleRegion());

		if (initialClusteredPoints != null) {
			addRelevantInitialInputPoints(initialClusteredPoints);
		}
	}

	private void addRelevantInitialInputPoints(ArrayList<ClusterPoint> initialClusteredPoints) {
		for (ClusterPoint clusterPoint : initialClusteredPoints) {
			clusterPoint.clearScreenPosition();
			addAll(clusterPoint.getPointsInCluster());
		}
	}

	private Projection getProjection() {
		return projectionRef.get();
	}

	private VisibleRegion getVisibleRegion() {
		return visibleRegionRef.get();
	}

	void addAll(ArrayList<InputPoint> points) {
		if (points != null) {
			Projection projection = getProjection();
			VisibleRegion visibleRegion = getVisibleRegion();
			if (projection != null && visibleRegion != null) {
				LatLngBounds bounds = getExpandedBounds(visibleRegion.latLngBounds);
				for (InputPoint point : points) {
					addIfNecessary(point, projection, bounds);
				}
			}
		}
	}

	private LatLngBounds getExpandedBounds(LatLngBounds bounds) {
		if (bounds != null && options != null) {
			double expandBoundsFactor = options.getExpandBoundsFactor();

			boolean spans180Meridian = bounds.northeast.longitude < bounds.southwest.longitude;

			double distanceFromNorthToSouth = bounds.northeast.latitude - bounds.southwest.latitude;
			double distanceFromEastToWest;
			if (spans180Meridian == false) {
				distanceFromEastToWest = bounds.northeast.longitude - bounds.southwest.longitude;
			} else {
				distanceFromEastToWest = (180 + bounds.northeast.longitude) + (180 - bounds.southwest.longitude);
			}

			double expandLatitude = distanceFromNorthToSouth * expandBoundsFactor;
			double expandLongitude = distanceFromEastToWest * expandBoundsFactor;

			LatLng newNortheast = new LatLng(bounds.northeast.latitude + expandLatitude, bounds.northeast.longitude + expandLongitude);
			LatLng newSouthwest = new LatLng(bounds.southwest.latitude - expandLatitude, bounds.southwest.longitude - expandLongitude);

			return new LatLngBounds(newSouthwest, newNortheast);
		}
		return null;
	}

	private void addIfNecessary(InputPoint point, Projection projection, LatLngBounds bounds) {
		if (bounds != null && bounds.contains(point.getMapPosition()) && !releventInputPointsSet.contains(point)) {
			point.buildScreenPosition(projection);
			relevantInputPointsList.add(point);
			releventInputPointsSet.add(point);
		}
	}

	ArrayList<ClusterPoint> build() {
		Projection projection = getProjection();
		ArrayList<ClusterPoint> clusteredPoints = null;
		if (projection != null) {
			clusteredPoints = new ArrayList<ClusterPoint>(relevantInputPointsList.size());
			for (InputPoint point : relevantInputPointsList) {
				boolean addedToExistingCluster = false;
				for (ClusterPoint clusterPoint : clusteredPoints) {
					if (clusterPoint.getPixelDistanceFrom(point) <= options.getPixelDistanceToJoinCluster()) {
						clusterPoint.add(point);
						addedToExistingCluster = true;
						break;
					}
				}
				if (addedToExistingCluster == false) {
					clusteredPoints.add(new ClusterPoint(point, projection, false));
				}
			}
		}
		return clusteredPoints;
	}

}
