package primitives;

import java.util.Objects;
import java.lang.Math;

/**
 * Represents a point in a 3D space.
 * The Point class encapsulates the coordinates of a point and provides methods
 * for common geometric operations such as addition, subtraction, and distance calculations.
 */
public class Point {
    protected final Double3 _xyz;

    public static final Point ZERO = new Point(0, 0, 0);

    /**
     * Primary constructor for Point.
     *
     * @param d1 value for x-axis.
     * @param d2 value for y-axis.
     * @param d3 value for z-axis.
     */
    public Point(double d1, double d2, double d3) {
        _xyz = new Double3(d1, d2, d3);
    }

    /**
     * Constructor to initialize Point based on Double3 object.
     *
     * @param xyz the Double3 object containing x, y, and z values.
     */
    public Point(Double3 xyz) {
        _xyz = xyz;
    }

    /**
     * Copy constructor for Point.
     * Creates a new Point with the same coordinates as the given Point.
     *
     * @param other the Point to copy
     */
    public Point(Point other) {
        this._xyz = new Double3(other._xyz);
    }


    public double getX() { return _xyz.d1; }
    public double getY() { return _xyz.d2; }
    public double getZ() { return _xyz.d3; }

    /**
     * Compares two points for equality.
     *
     * @param o the object to be compared for equality with this point.
     * @return true if the specified object is equal to this point; false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return (o instanceof Point point) && _xyz.equals(point._xyz);
    }

    /**
     * Returns a string representation of the point.
     *
     * @return a string representation of the point.
     */
    @Override
    public String toString() {
        return "Point{" +
                "_xyz=" + _xyz +
                '}';
    }

    /**
     * Adds a vector to this point to get a new point.
     *
     * @param vec the vector to be added.
     * @return a new point that is the result of the addition.
     */
    public Point add(Vector vec) {
        return new Point(_xyz.add(vec._xyz));
    }

    /**
     * Subtracts another point from this point to get a new vector.
     *
     * @param p the point to be subtracted.
     * @return a new vector that is the result of the subtraction.
     */
    public Vector subtract(Point p) {
        return new Vector(_xyz.subtract(p._xyz));
    }

    /**
     * Calculates the squared distance between this point and another point.
     *
     * @param p the other point.
     * @return the squared distance between this point and the specified point.
     */
    public double distanceSquared(Point p) {
        return (_xyz.d1 - p._xyz.d1) * (_xyz.d1 - p._xyz.d1) +
                (_xyz.d2 - p._xyz.d2) * (_xyz.d2 - p._xyz.d2) +
                (_xyz.d3 - p._xyz.d3) * (_xyz.d3 - p._xyz.d3);
    }

    /**
     * Calculates the distance between this point and another point.
     *
     * @param p the other point.
     * @return the distance between this point and the specified point.
     */
    public double distance(Point p) {
        return Math.sqrt(distanceSquared(p));
    }
}
