package edu.hm.cs.sam.mc.searcharea.task;

import edu.hm.sam.location.LocationWp;

/**
 * This class represents a Line between two coordinates. It consists of 2
 * coordinates, the length, the gradient and the y_intercept.
 *
 * @author Maximilian Bayer
 */
public class Line {
    double gradient;
    double yIntercept;
    double length;
    LocationWp firstCoordinate;
    LocationWp secondCoordinate;

    public Line(final double gradient, final double yIntercept, final LocationWp firstCoordinate,
            final LocationWp secondCoordinate, final double length) {
        this.gradient = gradient;
        this.yIntercept = yIntercept;
        this.firstCoordinate = firstCoordinate;
        this.secondCoordinate = secondCoordinate;
        this.length = length;
    }

    public double getGradient() {
        return gradient;
    }

    public double getyIntercept() {
        return yIntercept;
    }

    public LocationWp getFirstCoordinate() {
        return firstCoordinate;
    }

    public LocationWp getSecondCoordinate() {
        return secondCoordinate;
    }

    public double getLength() {
        return length;
    }

    @Override
    public String toString() {
        return "Line[" + firstCoordinate + "," + secondCoordinate + "]";
    }
}
