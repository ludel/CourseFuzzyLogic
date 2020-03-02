package com.arno.logiquefloue.beans;

public class LeftFuzzySet extends FuzzySet {
    public LeftFuzzySet(double min, double max, double heightMax, double baseMin) {
        super(min, max);
        m_points.add(new Point2D(min, 1));
        m_points.add(new Point2D(heightMax, 1));
        m_points.add(new Point2D(baseMin, 0));
        m_points.add(new Point2D(max, 0));
    }

}
