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

	private final boolean transition;

	public ClusterPoint(InputPoint initialPoint, Projection projection, boolean transition) {
		this.mapPosition = initialPoint.getMapPosition();
		this.transition = transition;
		add(initialPoint);
		buildScreenPosition(projection);
	}

	ClusterPoint(InputPoint initialPoint, Projection projection, boolean transition, LatLng overridePosition) {
		this(initialPoint, projection, transition);
		this.mapPosition = overridePosition;
	}

	void add(InputPoint point) {
		pointsInClusterList.add(point);
		pointsInClusterSet.add(point);
	}

	ArrayList<InputPoint> getPointsInCluster() {
		return pointsInClusterList;
	}
	
	public InputPoint getPointAtOffset(int index) {
		return pointsInClusterList.get(index);
	}

	public int size() {
		return pointsInClusterList.size();
	}

	public boolean containsInputPoint(InputPoint point) {
		return pointsInClusterSet.contains(point);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.twotoasters.clusterkraf.BasePoint#clearScreenPosition()
	 */
	@Override
	void clearScreenPosition() {
		super.clearScreenPosition();
		for(InputPoint inputPoint : pointsInClusterList) {
			inputPoint.clearScreenPosition();
		}
	}

	/**
	 * @return whether this object is part of a transition
	 */
	public boolean isTransition() {
		return transition;
	}

}
