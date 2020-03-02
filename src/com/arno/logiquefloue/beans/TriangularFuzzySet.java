package com.arno.logiquefloue.beans;

public class TriangularFuzzySet extends FuzzySet {
	public TriangularFuzzySet(double min,double max,double triangleBegin, double triangleCenter, double triangleEnd) {
		super(min,max);
		m_points.add(new Point2D(min,0));
		m_points.add(new Point2D(triangleBegin, 0));
		m_points.add(new Point2D(triangleCenter, 1));
		m_points.add(new Point2D(triangleEnd, 0));
		m_points.add(new Point2D(max, 0));
	}
}
