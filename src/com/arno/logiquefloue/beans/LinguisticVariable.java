package com.arno.logiquefloue.beans;

import java.util.ArrayList;

@SuppressWarnings("serial")
public class LinguisticVariable extends ArrayList<LinguisticValue>{
	private String m_name;
	private double m_minValue,m_maxValue;
	public LinguisticVariable(String name, double minValue, double maxValue) {
		m_name = name;
		m_minValue = minValue;
		m_maxValue = maxValue;
	}
	public void add(String name, FuzzySet fs) {
		add(new LinguisticValue(name, fs));
	}
	public LinguisticValue linguisticValueByName(String name) {
		name=name.toUpperCase();
		for(LinguisticValue val:this) {
			if(val.getName().toUpperCase().equals(name))
				return val;
		}
		return null;
	}
	public String getName() {
		return m_name;
	}
	public void setName(String name) {
		m_name = name;
	}
	public double getMinValue() {
		return m_minValue;
	}
	public void setMinValue(double minValue) {
		m_minValue = minValue;
	}
	public double getMaxValue() {
		return m_maxValue;
	}
	public void setMaxValue(double maxValue) {
		m_maxValue = maxValue;
	}
	public String toString() {
		String res= "[LinguisticVariable] : "+m_name+" ("+m_minValue+"-"+m_maxValue+")";
		for(LinguisticValue lv:this) {
			res+="\n\t- "+lv;
		}
		return res;
	}
}
