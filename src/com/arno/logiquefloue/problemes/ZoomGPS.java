package com.arno.logiquefloue.problemes;

import java.io.File;
import java.util.List;

import com.arno.logiquefloue.beans.*;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

public class ZoomGPS {

    public static void main(String[] args) {
        // Création du système
        FuzzySystem system = new FuzzySystem("Gestion du Zoom GPS");

        // ajout des variables
        LinguisticVariable distance = new LinguisticVariable("Distance", 0, 500000);
        distance.add(new LinguisticValue("Faible", new LeftFuzzySet(0, 500000, 30, 50)));
        distance.add(new LinguisticValue("Moyenne", new TrapezoidalFuzzySet(0, 500000, 40, 50, 100, 150)));
        distance.add(new LinguisticValue("Grande", new RightFuzzySet(0, 500000, 100, 150)));
        system.addInputVariable(distance);

        LinguisticVariable vitesse = new LinguisticVariable("Vitesse", 0, 200);
        vitesse.add(new LinguisticValue("Lente", new LeftFuzzySet(0, 200, 20, 30)));
        vitesse.add(new LinguisticValue("PeuRapide", new TrapezoidalFuzzySet(0, 200, 20, 30, 70, 80)));
        vitesse.add(new LinguisticValue("Rapide", new TrapezoidalFuzzySet(0, 200, 70, 80, 90, 110)));
        vitesse.add(new LinguisticValue("TresRapide", new RightFuzzySet(0, 200, 90, 110)));
        system.addInputVariable(vitesse);

        LinguisticVariable zoom = new LinguisticVariable("Zoom", 0, 5);
        zoom.add(new LinguisticValue("Petit", new LeftFuzzySet(0, 5, 1, 2)));
        zoom.add(new LinguisticValue("Normal", new TrapezoidalFuzzySet(0, 5, 1, 2, 3, 4)));
        zoom.add(new LinguisticValue("Gros", new RightFuzzySet(0, 5, 3, 4)));
        system.addOutputVariable(zoom);

        // Creation du generateur de graphique
        GraphBuilder zoomGraph = new GraphBuilder(
                "Graph Zoom",
                "Zoom",
                zoom,
                "PETIT",
                "NORMAL",
                "GROS"
        );
        zoomGraph.generateLine();


        // ajout des règles
        try {
            SAXBuilder sxb = new SAXBuilder();
            Document doc = sxb.build(new File("gps.xml"));
            List<Element> lrules = doc.getRootElement().getChild("rules").getChildren();
            for (Element rule : lrules) {
                system.addFuzzyRule(rule);
            }
        } catch (Exception exc) {
            System.err.println(exc.getMessage());
            System.exit(1);
        }

        double results;

        // First case
        System.out.println("Résolution de cas pratiques :");
        System.out.println("Cas 1 : ");
        system.setInputVariable(vitesse, 35);
        system.setInputVariable(distance, 70);

        results = system.solve();

        System.out.format("\tRésultat : %.3f\n", results);

        // Second case
        System.out.println("Cas 2 : ");
        system.resetCase();
        system.setInputVariable(vitesse, 25);
        system.setInputVariable(distance, 70);

        results = system.solve();
        zoomGraph.addCentroid("Second case", results);
        zoomGraph.addLocalArea(system.getResult());
        zoomGraph.showGraph();
        System.out.format("\tRésultat : %.3f\n", results);

        // Third case
        System.out.println("Cas 3 : ");
        system.resetCase();
        system.setInputVariable(vitesse, 72.5);
        system.setInputVariable(distance, 40);

        results = system.solve();
        System.out.format("\tRésultat : %.3f\n", results);


        // Fifth case
        System.out.println("Cas 5 : ");
        system.resetCase();
        system.setInputVariable(vitesse, 45);
        system.setInputVariable(distance, 160);

        results = system.solve();
        System.out.format("\tRésultat : %.3f\n", results);


    }

}
