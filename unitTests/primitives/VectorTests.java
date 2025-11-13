package primitives;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for {@link primitives.Vector}.
 */
class VectorTests {

    private static final double DELTA = 0.000001;

    /**
     * Test method for {@link primitives.Vector#Vector(double, double, double)}.
     */
    @Test
    void testConstructorWithXYZ() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test creating a non-zero vector
        assertDoesNotThrow(() -> new Vector(1, 2, 3), "Failed constructing a non-zero vector");

        // =============== Boundary Values Tests ==================
        // TC10: Test creating a zero vector
        assertThrows(IllegalArgumentException.class, () -> new Vector(0, 0, 0), "Constructed a zero vector");
    }

    /**
     * Test method for {@link primitives.Vector#Vector(Double3)}.
     */
    @Test
    void testConstructorWithDouble3() {
        // ============ Equivalence Partitions Tests ==============
        // TC01: Test creating a non-zero vector with Double3
        assertDoesNotThrow(() -> new Vector(new Double3(1, 2, 3)), "Failed constructing a non-zero vector with Double3");

        // =============== Boundary Values Tests ==================
        // TC10: Test creating a zero vector with Double3
        assertThrows(IllegalArgumentException.class, () -> new Vector(Double3.ZERO), "Constructed a zero vector with Double3");
    }


    /**
     * Test method for {@link primitives.Vector#equals(Object)}.
     */
    @Test
    void testEquals() {
        // ============ Equivalence Partitions Tests ==============
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(1, 2, 3);
        Vector v3 = new Vector(4, 5, 6);

        // TC01: Test equality of identical vectors
        assertEquals(v1, v2, "equals() failed for identical vectors");

        // TC02: Test inequality of different vectors
        assertNotEquals(v1, v3, "equals() failed for different vectors");
    }

    /**
     * Test method for {@link primitives.Vector#toString()}.
     */
    @Test
    void testToString() {
        // ============ Equivalence Partitions Tests ==============
        Vector v = new Vector(1, 2, 3);
        assertEquals("Vector{_xyz=(1.0,2.0,3.0)}", v.toString(), "toString() failed");
    }

    /**
     * Test method for {@link primitives.Vector#lengthSquared()}.
     */
    @Test
    void testLengthSquared() {
        // ============ Equivalence Partitions Tests ==============
        Vector v = new Vector(1, 2, 3);
        assertEquals(14, v.lengthSquared(), "lengthSquared() failed");
    }

    /**
     * Test method for {@link primitives.Vector#length()}.
     */
    @Test
    void testLength() {
        // ============ Equivalence Partitions Tests ==============
        Vector v = new Vector(1, 2, 2);
        assertEquals(3, v.length(), "length() failed");
    }

    /**
     * Test method for {@link primitives.Vector#add(Vector)}.
     */
    @Test
    void testAdd() {
        // ============ Equivalence Partitions Tests ==============
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(4, 5, 6);
        Vector result = v1.add(v2);
        assertEquals(new Vector(5, 7, 9), result, "add() failed");
    }

    //להוסיף טסט לחיסור

    /**
     * Test method for {@link primitives.Vector#scale(double)}.
     */
    @Test
    void testScale() {
        // ============ Equivalence Partitions Tests ==============
        Vector v = new Vector(1, 2, 3);
        Vector result = v.scale(2);
        assertEquals(new Vector(2, 4, 6), result, "scale() failed");

        // =============== Boundary Values Tests ==================
        // TC10: Test scaling by zero
        assertThrows(IllegalArgumentException.class, () -> v.scale(0), "scale() by zero does not throw an exception");
    }

    /**
     * Test method for {@link primitives.Vector#dotProduct(Vector)}.
     */
    @Test
    void testDotProduct() {
        // ============ Equivalence Partitions Tests ==============
        Vector v1 = new Vector(1, 2, 3);
        Vector v2 = new Vector(4, -5, 6);
        assertEquals(12, v1.dotProduct(v2), "dotProduct() failed");
    }

    /**
     * Test method for {@link primitives.Vector#crossProduct(Vector)}.
     */
    @Test
    void testCrossProduct() {
        // ============ Equivalence Partitions Tests ==============
        Vector v1 = new Vector(0, 0, 1);
        Vector v2 = new Vector(1, 0, 0);
        Vector result = v1.crossProduct(v2);

        // TC01: Test that length of cross-product is proper (orthogonal vectors taken for simplicity)
        assertEquals(v1.length() * v2.length(), result.length(), DELTA, "crossProduct() wrong result length");

        // TC02: Test cross-product result orthogonality to its operands
        assertEquals(0, result.dotProduct(v1), "crossProduct() result is not orthogonal to 1st operand");
        assertEquals(0, result.dotProduct(v2), "crossProduct() result is not orthogonal to 2nd operand");

        // =============== Boundary Values Tests ==================
        // TC10: test zero vector from cross-product of parallel vectors
        Vector v3 = new Vector(0, 0, 6);
        assertThrows(IllegalArgumentException.class, () -> v1.crossProduct(v3), "crossProduct() for parallel vectors does not throw an exception");
    }

    /**
     * Test method for {@link primitives.Vector#normalize()}.
     */
    @Test
    void testNormalize() {
        // ============ Equivalence Partitions Tests ==============
        Vector v = new Vector(3, 0, 4);
        Vector result = v.normalize();
        assertEquals(new Vector(0.6, 0, 0.8), result, "normalize() failed");
        assertEquals(1, result.length(), DELTA, "normalized vector is not of length 1");
    }

    /**
     * Test method for {@link primitives.Vector#createPerpendicular()}.
     */
    @Test
    void testCreatePerpendicular() {
        // ============ Equivalence Partitions Tests ==============

        // TC01: Perpendicular to a vector in the X-axis
        Vector v1 = new Vector(1, 0, 0);
        Vector perp1 = v1.createPerpendicular();
        assertEquals(0, v1.dotProduct(perp1), DELTA, "createPerpendicular() failed - vector is not perpendicular");

        // TC02: Perpendicular to a vector in the Y-axis
        Vector v2 = new Vector(0, 1, 0);
        Vector perp2 = v2.createPerpendicular();
        assertEquals(0, v2.dotProduct(perp2), DELTA, "createPerpendicular() failed - vector is not perpendicular");

        // TC03: Perpendicular to a vector in the Z-axis
        Vector v3 = new Vector(0, 0, 1);
        Vector perp3 = v3.createPerpendicular();
        assertEquals(0, v3.dotProduct(perp3), DELTA, "createPerpendicular() failed - vector is not perpendicular");

        // TC04: Perpendicular to a general vector
        Vector v4 = new Vector(1, 2, 3);
        Vector perp4 = v4.createPerpendicular();
        assertEquals(0, v4.dotProduct(perp4), DELTA, "createPerpendicular() failed - vector is not perpendicular");

        // =============== Boundary Values Tests ==================

        // TC10: Perpendicular to a unit vector along Z-axis
        Vector v5 = new Vector(0, 0, 1);
        Vector perp5 = v5.createPerpendicular();
        assertEquals(0, v5.dotProduct(perp5), DELTA, "createPerpendicular() failed - vector is not perpendicular");

        // TC11: Checking the perpendicular vector is normalized
        assertEquals(1, perp5.length(), DELTA, "createPerpendicular() failed - perpendicular vector is not normalized");
    }


}


