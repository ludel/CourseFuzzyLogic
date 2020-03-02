package com.arno.logiquefloue.beans;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.DoubleBinaryOperator;

import com.arno.logiquefloue.misc.MyMaths;

public class FuzzySet {
    protected List<Point2D> m_points;
    protected double m_min, m_max;

    public FuzzySet(double min, double max) {
        m_points = new ArrayList<Point2D>();
        m_min = min;
        m_max = max;
    }

    public double getMin() {
        return m_min;
    }

    public void setMin(double min) {
        m_min = min;
    }

    public double getMax() {
        return m_max;
    }

    public void setMax(double max) {
        m_max = max;
    }

    public void add(Point2D pt) {
        m_points.add(pt);
        m_points.sort(Comparator.comparing(Point2D::getX));
    }

    public void add(double x, double y) {
        Point2D pt = new Point2D(x, y);
        add(pt);
    }

    public List<Point2D> getMPoints() {
        return m_points;
    }

    @Override
    public String toString() {
        String result = getClass().getSimpleName() + " : [" + m_min + "-" + m_max + "]:";
        for (Point2D pt : m_points) {
            result += pt;
        }
        return result;
    }

    public static boolean egale(FuzzySet fs1, FuzzySet fs2) {
        return fs1.toString().equals(fs2.toString());
    }

    public static boolean different(FuzzySet fs1, FuzzySet fs2) {
        return !fs1.toString().equals(fs2.toString());
    }

    public static FuzzySet multiplie(FuzzySet fs, double valeur) {
        FuzzySet result = new FuzzySet(fs.getMin(), fs.getMax());
        for (Point2D pt : fs.m_points) {
            result.add(new Point2D(pt.getX(), pt.getY() * valeur));
        }
        return result;
    }

    public static FuzzySet non(FuzzySet fs) {
        FuzzySet result = new FuzzySet(fs.getMin(), fs.getMax());
        for (Point2D pt : fs.m_points) {
            result.add(new Point2D(pt.getX(), 1 - pt.getY()));
        }
        return result;
    }

    private Point2D lastOrDefaultLE(double value) {
        Point2D res = new Point2D();
        boolean trouve = false;
        for (Point2D pt : m_points) {
            if (pt.getX() <= value) {
                if (!trouve) {
                    res = pt;
                    trouve = true;
                } else {
                    if (pt.getX() > res.getX())
                        res = pt;
                }
            }
        }
        return res;
    }

    private Point2D firstOrDefaultGE(double value) {
        Point2D res = new Point2D();
        boolean trouve = false;
        for (Point2D pt : m_points) {
            if (pt.getX() >= value) {
                if (!trouve) {
                    res = pt;
                    trouve = true;
                } else {
                    if (pt.getX() < res.getX())
                        res = pt;
                }
            }
        }
        return res;
    }

    public double degreeAtValue(double xValue) {
        // Cas 1 : en dehors
        if (xValue < getMin() || xValue > getMax())
            return 0;

        Point2D before = lastOrDefaultLE(xValue);
        Point2D after = firstOrDefaultGE(xValue);

        // Cas 2 : on a un point à la valeur cherchée
        if (before.compareTo(after) == 0)
            return before.getY();

        // Cas 3 : théorème de Thalès
        return (((before.getY() - after.getY()) * (after.getX() - xValue) / (after.getX() - before.getX())) + after.getY());
    }

    public static FuzzySet AND(FuzzySet fs1, FuzzySet fs2) {
        return merge(fs1, fs2, MyMaths::MIN);
    }

    public static FuzzySet OR(FuzzySet fs1, FuzzySet fs2) {
        return merge(fs1, fs2, MyMaths::MAX);
    }

    private static FuzzySet merge(FuzzySet fs1, FuzzySet fs2, DoubleBinaryOperator function) {
        FuzzySet result = new FuzzySet(MyMaths.MIN(fs1.getMin(), fs2.getMin()), MyMaths.MAX(fs1.getMax(), fs2.getMax()));
        int ind1 = 0, ind2 = 0;
        Point2D oldPt1 = fs1.m_points.get(ind1);

        // position relative des 2 courbes
        int relativePosition = 0;
        int newRelativePosition = MyMaths.SIGNE(fs1.m_points.get(ind1).getY() - fs2.m_points.get(ind2).getY());

        // on boucle tant qu'il y a des points dans les collections
        boolean endOfList1 = false;
        boolean endOfList2 = false;
        while (!endOfList1 && !endOfList2) {
            // récupération des valeurs x des points courants
            double x1 = fs1.m_points.get(ind1).getX();
            double x2 = fs2.m_points.get(ind2).getX();
            // calcul des positions relatives
            relativePosition = newRelativePosition;
            newRelativePosition = MyMaths.SIGNE(fs1.m_points.get(ind1).getY() - fs2.m_points.get(ind2).getY());

            if (relativePosition != newRelativePosition && relativePosition != 0 && newRelativePosition != 0) {
                // les positions ont changé :
                // on doit trouver l'intersection
                // calcul des coordonnées des points extrèmes
                double x = (x1 == x2 ? (oldPt1.getX()) : (MyMaths.MIN(x1, x2)));
                double xprime = MyMaths.MAX(x1, x2);
                // calcul des pentes et du delta
                double slope1 = (fs1.degreeAtValue(xprime) - fs1.degreeAtValue(x)) / (xprime - x);
                double slope2 = (fs2.degreeAtValue(xprime) - fs2.degreeAtValue(x)) / (xprime - x);
                double delta = (fs2.degreeAtValue(x) - fs1.degreeAtValue(x)) / (slope1 - slope2);
                // on ajoute le point d'intersection
                result.m_points.add(new Point2D(x + delta, fs1.degreeAtValue(x + delta)));
                // et on passe aux points suivants
                if (x1 < x2) {
                    oldPt1 = fs1.m_points.get(ind1);
                    ind1++;
                    endOfList1 = !(ind1 < fs1.m_points.size());
                } else if (x1 > x2) {
                    ind2++;
                    endOfList2 = !(ind2 < fs2.m_points.size());
                }
            } else if (x1 == x2) {
                // les 2 points sont au même x. on garde le bon
                result.m_points.add(new Point2D(x1, function.applyAsDouble(fs1.m_points.get(ind1).getY(), fs2.m_points.get(ind2).getY())));
                oldPt1 = fs1.m_points.get(ind1);
                ind1++;
                endOfList1 = !(ind1 < fs1.m_points.size());
                ind2++;
                endOfList2 = !(ind2 < fs2.m_points.size());
            } else if (x1 < x2) {
                // la courbe 1 a un point avant.
                // on calcule le degré pour la 2ème courbe et on garde la bonne valeur
                result.m_points.add(new Point2D(x1, function.applyAsDouble(fs1.m_points.get(ind1).getY(), fs2.degreeAtValue(x1))));
                oldPt1 = fs1.m_points.get(ind1);
                ind1++;
                endOfList1 = !(ind1 < fs1.m_points.size());
            } else {
                // cette fois, c'est la courbe 2
                result.m_points.add(new Point2D(x2, function.applyAsDouble(fs1.degreeAtValue(x2), fs2.m_points.get(ind2).getY())));
                ind2++;
                endOfList2 = !(ind2 < fs2.m_points.size());
            }
        }
        // une des deux listes est finie. On ajoute les points restants
        if (!endOfList1) {
            while (!endOfList1) {
                result.m_points.add(new Point2D(fs1.m_points.get(ind1).getX(), function.applyAsDouble(0, fs1.m_points.get(ind1).getY())));
                ind1++;
                endOfList1 = !(ind1 < fs1.m_points.size());
            }
        } else if (!endOfList2) {
            while (!endOfList2) {
                result.m_points.add(new Point2D(fs2.m_points.get(ind2).getX(), function.applyAsDouble(0, fs2.m_points.get(ind2).getY())));
                ind2++;
                endOfList2 = !(ind2 < fs2.m_points.size());
            }
        }
        return result;
    }

    public double centroid() {
        // si moins de 2 points, pas de centroide
        if (m_points.size() < 2)
            return 0;
        else {
            double ponderatedArea = 0;
            double totalArea = 0;
            double localArea;
            Point2D oldPt = null;
            // parcours de chaque couple de points (oldPt et newPt)
            for (Point2D newPt : m_points) {
                if (oldPt != null) {
                    // calcul du centroide local
                    if (oldPt.getY() == newPt.getY()) {
                        // c'est un rectangle (même hauteur). Donc au centre
                        localArea = oldPt.getY() * (newPt.getX() - oldPt.getX());
                        totalArea += localArea;
                        ponderatedArea += ((newPt.getX() - oldPt.getX()) / 2 + oldPt.getX()) * localArea;
                    } else {
                        // on a une forme composée d'un rectangle (dessous) et d'un triangle (dessus)
                        // - le rectangle
                        localArea = MyMaths.MIN(oldPt.getY(), newPt.getY()) * (newPt.getX() - oldPt.getX());
                        totalArea += localArea;
                        ponderatedArea += ((newPt.getX() - oldPt.getX()) / 2 + oldPt.getX()) * localArea;
                        // - triangle : centroïde à 1/3, du côté de l'angle droit
                        localArea = (newPt.getX() - oldPt.getX()) * (MyMaths.ABS(newPt.getY() - oldPt.getY())) / 2;
                        totalArea += localArea;
                        if (newPt.getY() > oldPt.getY())
                            ponderatedArea += (2.0 / 3.0 * (newPt.getX() - oldPt.getX()) + oldPt.getX()) * localArea;
                        else
                            ponderatedArea += (1.0 / 3.0 * (newPt.getX() - oldPt.getX()) + oldPt.getX()) * localArea;

                    }

                }
                oldPt = newPt;
            }
            return ponderatedArea / totalArea;
        }
    }
}
