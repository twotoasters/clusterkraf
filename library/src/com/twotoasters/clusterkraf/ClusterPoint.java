/**
 * @author Carlton Whitehead
 */
package com.twotoasters.clusterkraf;

import java.util.ArrayList;
import java.util.HashSet;

import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.LatLng;

/**
 *
 */
public class ClusterPoint extends BasePoint {
	
	private final ArrayList<InputPoint> pointsInClusterList = new ArrayList<InputPoint>();
	private final HashSet<InputPoint> pointsInClusterSet = new HashSet<InputPoint>();
	
	public ClusterPoint(InputPoint initialPoint, Projection projection) {
		this.mapPosition = initialPoint.getMapPosition();
		add(initialPoint);
		buildScreenPosition(projection);
	}
	
	ClusterPoint(InputPoint initialPoint, Projection projection, LatLng overridePosition) {
		this(initialPoint, projection);
		this.mapPosition = overridePosition;
	}
	
	void add(InputPoint point) {
		pointsInClusterList.add(point);
		pointsInClusterSet.add(point);
	}
	
	ArrayList<InputPoint> getPointsInCluster() {
		return pointsInClusterList;
	}
	
	public int size() {
		return pointsInClusterList.size();
	}
	
	public boolean containsInputPoint(InputPoint point) {
		return pointsInClusterSet.contains(point);
	}

	/* (non-Javadoc)
	 * @see com.twotoasters.clusterkraf.BasePoint#clearScreenPosition()
	 */
	@Override
	void clearScreenPosition() {
		super.clearScreenPosition();
		for (InputPoint inputPoint : pointsInClusterList) {
			inputPoint.clearScreenPosition();
		}
	}
	
	
}
