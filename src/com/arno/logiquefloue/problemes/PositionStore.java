package com.arno.logiquefloue.problemes;

import java.io.File;
import java.util.List;

import com.arno.logiquefloue.beans.*;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.jfree.ui.RefineryUtilities;

public class PositionStore {

    public static void main(String[] args) {
        // Création du système
        FuzzySystem system = new FuzzySystem("Gestion du Zoom GPS");


        // ajout des variables
        LinguisticVariable temperature = new LinguisticVariable("Temperature", 5, 30);
        temperature.add(new LinguisticValue("Froid", new LeftFuzzySet(5, 30, 10, 12)));
        temperature.add(new LinguisticValue("Frais", new TrapezoidalFuzzySet(5, 30, 10, 12, 15, 17)));
        temperature.add(new LinguisticValue("Bon", new TrapezoidalFuzzySet(5, 30, 15, 17, 20, 25)));
        temperature.add(new LinguisticValue("Chaud", new RightFuzzySet(5, 30, 20, 25)));
        system.addInputVariable(temperature);


        LinguisticVariable eclairage = new LinguisticVariable("Eclairage", 0, 100000);
        eclairage.add(new LinguisticValue("Sombre", new LeftFuzzySet(0, 100000, 20000, 30000)));
        eclairage.add(new LinguisticValue("Moyen", new TrapezoidalFuzzySet(0, 100000, 20000, 30000, 65000, 84000)));
        eclairage.add(new LinguisticValue("Fort", new RightFuzzySet(0, 100000, 65000, 84000)));
        system.addInputVariable(eclairage);

        LinguisticVariable hauteur = new LinguisticVariable("Hauteur", 0, 150);
        hauteur.add(new LinguisticValue("Ferme", new LeftFuzzySet(0, 150, 25, 40)));
        hauteur.add(new LinguisticValue("MiHauteur", new TrapezoidalFuzzySet(0, 150, 25, 40, 85, 100)));
        hauteur.add(new LinguisticValue("Remonte", new RightFuzzySet(0, 150, 85, 100)));
        system.addOutputVariable(hauteur);

        // ajout des règles
        try {
            SAXBuilder sxb = new SAXBuilder();
            Document doc = sxb.build(new File("stores.xml"));
            List<Element> lrules = doc.getRootElement().getChild("rules").getChildren();
            for (Element rule : lrules) {
                system.addFuzzyRule(rule);
            }
        } catch (Exception exc) {
            System.err.println(exc.getMessage());
            System.exit(1);
        }

        System.out.println("Résolution de cas pratiques :");
        System.out.println("Cas 1 : ");
        system.setInputVariable(eclairage, 80000);
        system.setInputVariable(temperature, 21);
        System.out.format("\tRésultat : %.3f\n", system.solve());
    }

}
