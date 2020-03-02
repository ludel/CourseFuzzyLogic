package com.arno.logiquefloue.beans;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.*;
import org.jfree.chart.renderer.xy.XYAreaRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.util.ShapeUtilities;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GraphBuilder extends ApplicationFrame {
    private List<LinguisticValue> inputVariables;
    private String title;
    private String xLabel;
    private List<XYSeries> series;
    private List<XYSeries> areaSeries;
    private XYSeriesCollection seriesCollection;
    private XYSeriesCollection seriesAreaCollection;

    public GraphBuilder(String title, String xLabel, List<LinguisticValue> inputVariables, String Series1Label, String Series2Label, String Series3Label) {
        super(title);
        this.inputVariables = inputVariables;
        this.title = title;
        this.xLabel = xLabel;

        this.series = Arrays.asList(
                new XYSeries(Series1Label),
                new XYSeries(Series2Label),
                new XYSeries(Series3Label)
        );
        this.areaSeries = Arrays.asList(
                new XYSeries("1"),
                new XYSeries("2"),
                new XYSeries("3")
        );
        this.seriesCollection = new XYSeriesCollection();
        this.seriesAreaCollection = new XYSeriesCollection();
    }

    public void addCentroid(String name, double x) {
        XYSeries centroid = new XYSeries(name);
        centroid.add(x, 0.0);
        centroid.add(x, 1.5);
        this.seriesCollection.addSeries(centroid);
    }

    public void addLocalArea(FuzzySet fuzzySet) {
        double localArea;
        Point2D oldPt = null;
        List<Double> factors = new ArrayList<>();

        for (Point2D newPt : fuzzySet.getMPoints()) {
            if (oldPt != null) {
                if (oldPt.getY() == newPt.getY()) {
                    localArea = oldPt.getY() * (newPt.getX() - oldPt.getX());
                    factors.add(localArea);
                }
            }
            oldPt = newPt;
        }

        this.generateArea(factors);
    }

    private void generateArea(List<Double> factors) {
        this.areaSeries.get(0).add(0.0, 1.0 * factors.get(0));

        int index = 0;
        for (LinguisticValue linguisticValue : this.inputVariables) {
            List<Point2D> points = linguisticValue.getFs().getMPoints();

            for (Point2D point : points.subList(1, points.size() - 1))
                this.areaSeries.get(index).add(point.getX(), point.getY() * factors.get(index));

            index++;
        }

        for (XYSeries seriesItem : this.areaSeries)
            this.seriesAreaCollection.addSeries(seriesItem);
    }


    public void generateLine() {
        this.series.get(0).add(0.0, 1.0);

        int index = 0;
        for (LinguisticValue linguisticValue : this.inputVariables) {
            List<Point2D> points = linguisticValue.getFs().getMPoints();

            for (Point2D point : points.subList(1, points.size() - 1))
                this.series.get(index).add(point.getX(), point.getY());

            index++;
        }

        for (XYSeries seriesItem : this.series)
            this.seriesCollection.addSeries(seriesItem);
    }

    public void showGraph() {
        // Line
        JFreeChart chartLine = ChartFactory.createXYLineChart(
                this.title, this.xLabel,
                "Degré d'appartenance",
                this.seriesCollection,
                PlotOrientation.VERTICAL,
                true, true, false
        );

        XYPlot plotLine = chartLine.getXYPlot();
        XYLineAndShapeRenderer rendererLine = new XYLineAndShapeRenderer();
        rendererLine.setSeriesPaint(0, Color.RED);
        rendererLine.setSeriesPaint(1, Color.BLUE);
        rendererLine.setSeriesPaint(2, Color.YELLOW);
        rendererLine.setSeriesPaint(3, Color.BLACK);
        plotLine.setRenderer(0, rendererLine);

        // Area
        XYAreaRenderer rendererArea = new XYAreaRenderer();
        rendererArea.setSeriesPaint(0, Color.gray);
        rendererArea.setSeriesPaint(1, Color.gray);
        rendererArea.setSeriesPaint(2, Color.gray);

        // Combine
        plotLine.setDataset(1, this.seriesAreaCollection);
        plotLine.setRenderer(1, rendererArea);

        ChartPanel chartPanel = new ChartPanel(chartLine);

        this.getContentPane().add(chartPanel);
        this.pack();
        this.setVisible(true);
    }

}
