package renderer.superSampling.antiAliasing;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;
import renderer.superSampling.SamplingPattern;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link SamplingPattern} class.
 * <p>
 * These tests verify the correct behavior of the sampling patterns used
 * in the super-sampling process, ensuring that the generated sample points
 * adhere to the expected distribution.
 * </p>
 */
class SamplingPatternTests {

    /**
     * Tests the {@code generateSamples} method with the RANDOM sampling pattern.
     * <p>
     * This test verifies that the random sampling process:
     * <ul>
     *     <li>Generates points within the defined sampling area.</li>
     *     <li>Maintains a reasonable level of randomness without excessive clustering.</li>
     *     <li>Ensures a balanced spread of points rather than over-concentration in a single region.</li>
     * </ul>
     * </p>
     */
    @Test
    void testRandomSamplingDistribution() {
        Point center = new Point(0, 0, 0);
        double size = 2.0; // Sampling area size
        int numSamples = 100; // Number of samples to generate
        SamplingPattern pattern = SamplingPattern.RANDOM;
        Vector normal = new Vector(0, 0, 1); // Normal vector defining the sampling plane

        List<Point> samples = pattern.generateSamples(numSamples, center, size, normal);

        // 🔹 Validate that all points remain within the defined bounds 🔹
        for (Point p : samples) {
            assertTrue(Math.abs(p.getX()) <= size / 2, "Point X is out of bounds");
            assertTrue(Math.abs(p.getY()) <= size / 2, "Point Y is out of bounds");
        }

        // 🔹 Verify that the distribution is sufficiently random and not overly concentrated 🔹
        double sumX = 0, sumY = 0;
        for (Point p : samples) {
            sumX += p.getX();
            sumY += p.getY();
        }
        double meanX = sumX / numSamples;
        double meanY = sumY / numSamples;

        double varianceX = 0, varianceY = 0;
        for (Point p : samples) {
            varianceX += Math.pow(p.getX() - meanX, 2);
            varianceY += Math.pow(p.getY() - meanY, 2);
        }
        varianceX /= numSamples;
        varianceY /= numSamples;

        double stdDevX = Math.sqrt(varianceX);
        double stdDevY = Math.sqrt(varianceY);

        double expectedSpread = size / 4; // General estimation of expected spread

        assertTrue(stdDevX > expectedSpread * 0.5, "X distribution is too concentrated");
        assertTrue(stdDevY > expectedSpread * 0.5, "Y distribution is too concentrated");
    }
}
