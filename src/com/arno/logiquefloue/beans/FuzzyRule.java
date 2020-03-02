package com.arno.logiquefloue.beans;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;

import com.arno.logiquefloue.misc.MyMaths;

public class FuzzyRule {
	public List<FuzzyExpression> m_premisses;
	public FuzzyExpression m_conclusion;
	
	public FuzzyRule(List<FuzzyExpression> premisses, FuzzyExpression conclusion) {
		m_premisses = premisses;
		m_conclusion = conclusion;
	}
	
	public FuzzyRule(Element eRule, FuzzySystem fuzzySystem) {
		m_premisses=new ArrayList<>();
		try {
			// les prémisses
			List<Element> lPremisses=eRule.getChild("premisses").getChildren();
			for(Element ePremisse:lPremisses) {
				String name=ePremisse.getAttributeValue("name");
				String value=ePremisse.getAttributeValue("value");
				LinguisticVariable lv=fuzzySystem.linguisticVariableByName(name);
				FuzzyExpression fe=new FuzzyExpression(lv, value);
				m_premisses.add(fe);
			}
			// la conclusion
			Element eConclusion=eRule.getChild("conclusion");
			String name=eConclusion.getAttributeValue("name");
			String value=eConclusion.getAttributeValue("value");
			LinguisticVariable lv=fuzzySystem.linguisticVariableByName(name);
			m_conclusion=new FuzzyExpression(lv, value);
		} catch(Exception exc) {
			System.err.println("Constructeur FuzzyRule : "+exc.getMessage());
		}
	}

	FuzzySet apply(List<FuzzyValue> problem) {
		double degree=1;
		for (FuzzyExpression premisse : m_premisses) {
			double localDegree=0;
			LinguisticValue val=null;
			for (FuzzyValue pb : problem) {
				if(premisse.getLv()==pb.m_lv) {
					val=premisse.getLv().linguisticValueByName(premisse.getLinguisticValueName());
					if(val!=null) {
						localDegree=val.degreeAtValue(pb.m_value);
						break;
					}
				}
			}
			if(val==null)
				return null;
			degree=MyMaths.MIN(degree, localDegree);
		}
		FuzzySet res= m_conclusion.getLv().linguisticValueByName(m_conclusion.getLinguisticValueName()).getFs();
		res=FuzzySet.multiplie(res, degree);
		return res;
	}
	public String toString() {
		String sRule="IF ";
		for(int i=0;i<m_premisses.size();i++) {
			sRule+=(i!=0?"AND ":"")+m_premisses.get(i)+" ";
		}
		sRule+="THEN ";
		sRule+=m_conclusion.toString();
		return sRule;
	}
}
