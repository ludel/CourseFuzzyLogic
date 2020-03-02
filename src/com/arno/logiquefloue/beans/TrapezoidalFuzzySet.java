package com.arno.logiquefloue.beans;


public class TrapezoidalFuzzySet extends FuzzySet {
    public TrapezoidalFuzzySet(double min, double max, double baseLeft, double heightLeft, double heightRight, double baseRight) {
        super(min, max);
        m_points.add(new Point2D(min, 0));
        m_points.add(new Point2D(baseLeft, 0));
        m_points.add(new Point2D(heightLeft, 1));
        m_points.add(new Point2D(heightRight, 1));
        m_points.add(new Point2D(baseRight, 0));
        m_points.add(new Point2D(max, 0));
    }
}
