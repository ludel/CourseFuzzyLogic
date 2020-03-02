package com.arno.logiquefloue.beans;

public class Point2D implements Comparable<Point2D>{
	private double m_x, m_y;

	public Point2D(double x, double y) {
		m_x = x;
		m_y = y;
	}
	
	public Point2D() {
		m_x=m_y=0;
	}

	public double getX() {
		return m_x;
	}

	public void setX(double x) {
		m_x = x;
	}

	public double getY() {
		return m_y;
	}

	public void setY(double y) {
		m_y = y;
	}

	@Override
	public int compareTo(Point2D o) {
		return (int) (m_x-o.m_x);
	}

	@Override
	public String toString() {
		return "("+m_x+";"+m_y+")";
	}
	
}
