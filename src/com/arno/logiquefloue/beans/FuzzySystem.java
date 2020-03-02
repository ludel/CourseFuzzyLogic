package com.arno.logiquefloue.beans;

import java.util.ArrayList;
import java.util.List;

import org.jdom2.Element;

public class FuzzySystem {
    private String m_name;
    private List<LinguisticVariable> m_inputs;
    private LinguisticVariable m_output;
    private List<FuzzyRule> m_rules;
    private List<FuzzyValue> m_problem;

    public FuzzySystem(String nom) {
        m_name = nom;
        m_inputs = new ArrayList<>();
        m_rules = new ArrayList<>();
        m_problem = new ArrayList<>();
    }

    public void addInputVariable(LinguisticVariable lv) {
        m_inputs.add(lv);
    }

    public void addOutputVariable(LinguisticVariable lv) {
        m_output = lv;
    }

    public void addFuzzyRule(FuzzyRule fr) {
        m_rules.add(fr);
    }

    // ajout d'une valeur numérique en entrée
    public void setInputVariable(LinguisticVariable inputVar, double value) {
        m_problem.add(new FuzzyValue(inputVar, value));
    }

    // remise à zéro des valeurs en entrée pour changer de cas
    public void resetCase() {
        m_problem.clear();
    }

    public LinguisticVariable linguisticVariableByName(String name) {
        for (LinguisticVariable input : m_inputs) {
            if (input.getName().equals(name))
                return input;
        }
        if (m_output.getName().equals(name))
            return m_output;
        return null;
    }

    public double solve() {
        return this.getResult().centroid();
    }

    public FuzzySet getResult(){
        // initialisation du résultat

        FuzzySet res = new FuzzySet(m_output.getMinValue(), m_output.getMaxValue());
        res.m_points.add(new Point2D(m_output.getMinValue(), 0));
        res.m_points.add(new Point2D(m_output.getMaxValue(), 0));
        for (FuzzyRule rule : m_rules) {
            res = FuzzySet.OR(res, rule.apply(m_problem));
        }

        return res;
    }

    public String getName() {
        return m_name;
    }

    public void setName(String name) {
        m_name = name;
    }

    public void addFuzzyRule(Element eRule) {
        FuzzyRule rule = new FuzzyRule(eRule, this);
        m_rules.add(rule);
    }

}
