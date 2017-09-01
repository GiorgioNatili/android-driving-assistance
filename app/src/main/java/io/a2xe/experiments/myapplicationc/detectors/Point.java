package io.a2xe.experiments.myapplicationc.detectors;

/**
 * Created by Roee on 17/02/2016.
 */
public class Point extends org.opencv.core.Point {

    public Point(double x, double y) {
        super(x, y);
    }

    public static Point center(Point p1, Point p2) {
        return new Point((p1.x + p2.x) / 2, (p1.y + p2.y) / 2);
    }

    /**
     * Euclidean distance to another point
     * @param p another point
     * @return euclidean distance
     */
    public double distance(Point p) {
        return Math.sqrt(Math.pow(this.x - p.x, 2) + Math.pow(this.y - p.y, 2));
    }

    /**
     * @return new point with reversed x and y values
     */
    public Point reversed() {
        return new Point(y, x);
    }
}
