package com.arno.logiquefloue.beans;

public class RightFuzzySet extends FuzzySet {
    public RightFuzzySet(double min, double max, double heightMin, double baseMax) {
        super(min, max);
        m_points.add(new Point2D(min, 0));
        m_points.add(new Point2D(heightMin, 0));
        m_points.add(new Point2D(baseMax, 1));
        m_points.add(new Point2D(max, 1));
    }
}
