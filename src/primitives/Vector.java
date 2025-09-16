package primitives;

import java.util.Objects;

import static primitives.Util.isZero;


/**
 * Represents a vector in a 3D space.
 * The Vector class extends the Point class and provides additional methods
 * for vector operations such as addition, subtraction, scaling, dot product, and cross product.
 */
public class Vector extends Point {

    /**
     * Primary constructor for Vector.
     * @param x value for x-axis.
     * @param y value for y-axis.
     * @param z value for z-axis.
     */
    public Vector(double x, double y, double z) {
        this(new Double3(x, y, z));
    }

    /**
     * Constructor to initialize a Vector based on a Double3 object.
     * This constructor checks if the provided Double3 object represents a zero vector
     * and throws an exception if it does, as a zero vector is not allowed.
     *
     * @param xyz the Double3 object containing x, y, and z values.
     * @throws IllegalArgumentException if the provided Double3 object represents a zero vector.
     */
    public Vector(Double3 xyz) {
        super(xyz);
        if (xyz.equals(Double3.ZERO))
            throw new IllegalArgumentException("Vector Zero not allowable");
    }

    /**
     * Copy constructor for Vector.
     * Creates a new Vector with the same coordinates as the given Vector.
     *
     * @param other the Vector to copy
     */
    public Vector(Vector other) {
        super(other._xyz);
        if (other._xyz.equals(Double3.ZERO)) {
            throw new IllegalArgumentException("Vector Zero not allowable");
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        return (o instanceof Vector vec) && super.equals(vec);
    }

    @Override
    public String toString() {
        return "Vector{" +
                "_xyz=" + _xyz +
                '}';
    }

    /**
     * Calculates the squared length of the vector.
     * @return the squared length of the vector.
     */
    public double lengthSquared() {
        return dotProduct(this);
    }

    /**
     * Calculates the length of the vector.
     * @return the length of the vector.
     */
    public double length() {
        return Math.sqrt(lengthSquared());
    }

    /**
     * Adds two vectors to form a new vector.
     * @param other the vector to be added.
     * @return a new vector that is the result of the addition.
     */
    public Vector add(Vector other) {
        return new Vector(_xyz.add(other._xyz));
    }

    /**
     * Multiplies the vector by a scalar.
     * @param scalar the scalar value to multiply by.
     * @return a new vector that is the result of the scalar multiplication.
     */
    public Vector scale(double scalar){
        if(isZero(scalar))
            throw new IllegalArgumentException("cannot scale with 0");
        return new Vector(_xyz.scale(scalar));
    }



    /**
     * Computes the dot product of this vector and another vector.
     * @param other the other vector.
     * @return the result of the dot product.
     */
    public double dotProduct(Vector other) {
        return (_xyz.d1 * other._xyz.d1) +
                (_xyz.d2 * other._xyz.d2) +
                (_xyz.d3 * other._xyz.d3);
    }

    /**
     * Computes the cross product of this vector and another vector.
     * @param other the other vector.
     * @return a new vector that is the result of the cross product.
     */
    public Vector crossProduct(Vector other) {
        return new Vector((_xyz.d2 * other._xyz.d3) - (_xyz.d3 * other._xyz.d2),
                (_xyz.d3 * other._xyz.d1) - (_xyz.d1 * other._xyz.d3),
                (_xyz.d1 * other._xyz.d2) - (_xyz.d2 * other._xyz.d1));
    }

    /**
     * Normalizes the vector.
     * @return a new vector that is the normalized form of this vector.
     */
    public Vector normalize() {
        return scale(1 / length());
    }

    /**
     * Computes a perpendicular vector to this vector.
     * <p>
     * This method returns a normalized vector that is perpendicular to the current vector.
     * The perpendicular vector is chosen based on the following rules:
     * <ul>
     * <li>If the current vector is not aligned with the Z-axis, the perpendicular vector is computed
     *     using the XY-plane.</li>
     * <li>If the current vector is parallel to the Z-axis, the perpendicular vector is computed
     *     using the YZ-plane to avoid degeneracy.</li>
     * </ul>
     * The result is always a unit vector.
     *
     * @return a new vector that is perpendicular to this vector.
     */
    public Vector createPerpendicular() {
        // If the vector is not parallel to the Z-axis, use the XY-plane to find a perpendicular vector
        if (Math.abs(this._xyz.d3) < 1) {
            return new Vector(-this._xyz.d2, this._xyz.d1, 0).normalize();
        }
        // If the vector is parallel to the Z-axis, use the YZ-plane instead
        return new Vector(0, -this._xyz.d3, this._xyz.d2).normalize();
    }

}
