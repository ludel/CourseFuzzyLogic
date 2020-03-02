package com.arno.logiquefloue.beans;

public class FuzzyExpression {
	private LinguisticVariable m_lv;
	private String m_linguisticValueName;
	
	public FuzzyExpression(LinguisticVariable lv, String linguisticValueName) {
		m_lv = lv;
		m_linguisticValueName = linguisticValueName;
	}

	public LinguisticVariable getLv() {
		return m_lv;
	}

	public void setLv(LinguisticVariable lv) {
		m_lv = lv;
	}

	public String getLinguisticValueName() {
		return m_linguisticValueName;
	}

	public void setLinguisticValueName(String linguisticValueName) {
		m_linguisticValueName = linguisticValueName;
	}
	public String toString() {
		return m_lv.getName()+" IS "+m_linguisticValueName;
	}
}
