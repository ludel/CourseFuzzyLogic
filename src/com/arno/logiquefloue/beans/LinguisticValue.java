package com.arno.logiquefloue.beans;

public class LinguisticValue {
	private String m_name;
	private FuzzySet m_fs;
	
	public LinguisticValue(String name, FuzzySet fs) {
		m_name = name;
		m_fs = fs;
	}

	public String getName() {
		return m_name;
	}

	public FuzzySet getFs() {
		return m_fs;
	}
	
	public double degreeAtValue(double val) {
		return m_fs.degreeAtValue(val);
	}
	
	public String toString() {
		return m_name+" : "+m_fs;
	}
}
