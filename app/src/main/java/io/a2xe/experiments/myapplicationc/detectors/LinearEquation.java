package io.a2xe.experiments.myapplicationc.detectors;

import org.opencv.core.Rect;

/**
 * Created by Roee on 24/12/2015.
 */
public class LinearEquation {
    public double a;
    public double b;
    public Point point1;
    public Point point2;
    public Point edge1;
    public Point edge2;


    public LinearEquation(double a, double b) {
        this.a = a;
        this.b = b;
        calcEdges();
        point1 = edge1;
        point2 = edge2;
    }

    public LinearEquation(double a, double b, Point center) {
        this.a = a;
        this.b = b;
        calcEdges();
        point1 = center;
        point2 = center;
    }

    public LinearEquation(double x1, double y1, double x2, double y2) {
        createFromPoints(x1, y1, x2, y2);
        calcEdges();
        point1 = new Point(x1, y1);
        point2 = new Point(x2, y2);
    }

//
//    public LinearEquation(Point p1, Point p2){
//        createFromPoints(p1.x, p1.y, p2.x, p2.y);
//
//        this.point1 = p1;
//        this.point2 = p2;
//    }
//
//    public LinearEquation(double a, Point p){
//        double b = p.y - a * p.x;
//        this.a = a;
//        this.b = b;
//    }

    /**
     * Finds the intersection point of two lines
     * @param line1 first line
     * @param line2 second line
     * @return intersection point
     */
    public static Point intersect(LinearEquation line1, LinearEquation line2) {
        double x = (line1.b - line2.b) / (line2.a - line1.a),
                y = line1.a * x + line1.b;
        return new Point(x, y);
    }

    /**
     * Receives two lines and calculate an angle bisector
     * @param line1 first line
     * @param line2 second line
     * @return bisector line
     */
    public static LinearEquation calculateAngleBisector(LinearEquation line1, LinearEquation line2) {
        double a1 = line1.a,
                b1 = line1.b,
                a2 = line2.a,
                b2 = line2.b,
                A1 = -a1,
                A2 = -a2,
                B12 = 1,
                C1 = -b1,
                C2 = -b2,
                R1 = Math.sqrt(A1 * A1 + 1),
                R2 = Math.sqrt(A2 * A2 + 1),
                A = A1 / R1 + A2 / R2,
                B = B12 / R1 + B12 / R2,
                C = C1 / R1 + C2 / R2,
                a = -A / B,
                b = -C / B;

        return new LinearEquation(a, b);
    }

    private void createFromPoints(double x1, double y1, double x2, double y2) {
        // Find the linear equation parameters (y = a*x + b)
        double a = (y1 - y2) / (x1 - x2);
        double b = y1 - a * x1;

        this.a = a;
        this.b = b;
    }

    /**
     * Returns the value of y by x
     * @param x
     * @return y
     */
    public double y(double x) {
        return a * x + b;
    }

    /**
     * Returns the value of x by y
     * @param y
     * @return x
     */
    public double x(double y) {
        return (y - b) / a;
    }

    /**
     * Returns the euclidean distance of the line from a point
     * @param p point
     * @return euclidean distance
     */
    public double distanceFromPoint(Point p) {
        double d = (-a * p.x + p.y - b) / Math.sqrt(Math.pow(a, 2) + Math.pow(1, 2));
        return d;
    }

    /**
     * Returns the center of the line
     * @return the center point
     */
    public Point center() {
        return new Point((point1.x + point2.x) / 2, (point1.y + point2.y) / 2);
    }

    /**
     * Returns the center of the intersections with the edges of the screen
     * @return the center point
     */
    public Point edgesCenter() {
        return Point.center(edge1, edge2);
    }

    /**
     * Finds the intersection points with the edges of the screen
     */
    public void calcEdges() {
        Point[] edges = new Point[2];
        int[] xEdges = {0, LaneDetector.mFrameHeight};
        int[] yEdges = {0, LaneDetector.mFrameWidth};
        Rect screenRect = new Rect(0, 0, LaneDetector.mFrameHeight + 1, LaneDetector.mFrameWidth + 1);
        int index = 0;
        for (int i = 0; index < 2 && i < 2; i++) {
            Point p = new Point(xEdges[i], y(xEdges[i]));
            if (p.inside(screenRect)) {
                edges[index] = p;
                index++;
            }
        }
        for (int i = 0; index < 2 && i < 2; i++) {
            Point p = new Point(x(yEdges[i]), yEdges[i]);
            if (p.inside(screenRect)) {
                edges[index] = p;
                index++;
            }
        }

        edge1 = (edges[0] != null) ? edges[0] : new Point(0, y(0));
        edge2 = (edges[1] != null) ? edges[1] : new Point(LaneDetector.mFrameHeight, y(LaneDetector.mFrameHeight));

    }

    /**
     * Returns the length of the line
     * @return length of line
     */
    public double length() {
        return point1.distance(point2);
    }

    /**
     * Returns the normal to the line in point p
     * @param p a point on the line
     * @return the normal line
     */
    public LinearEquation normal(Point p) {
        double a = -1 / this.a;
        double b = -a * p.x + p.y;
        return new LinearEquation(a, b);
    }


}