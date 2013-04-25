package com.twotoasters.clusterkraf.util;

import android.graphics.Point;

/**
 * utility for calculating distances
 */
public class Distance {
	/**
	 * calculate the distance between two points; pythagorean theorem
	 * 
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return the distance between two points
	 */
	public static double from(double x1, double y1, double x2, double y2) {
		double a = Math.abs(x1 - x2);
		double b = Math.abs(y1 - y2);
		return Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
	}

	/**
	 * convenience for calculating the distance between two
	 * android.graphics.Point objects
	 * 
	 * @param a
	 * @param b
	 * @return the distance between two Points
	 */
	public static double from(Point a, Point b) {
		if (a != null && b != null) {
			return from(a.x, a.y, b.x, b.y);
		}
		return 0;
	}
}
