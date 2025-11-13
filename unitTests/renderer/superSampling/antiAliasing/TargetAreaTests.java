package renderer.superSampling.antiAliasing;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;
import renderer.superSampling.SamplingPattern;
import renderer.superSampling.TargetArea;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link TargetArea} class.
 * <p>
 * These tests verify the correct behavior of the sample generation process
 * using different sampling patterns.
 * </p>
 */
class TargetAreaTests {

    /**
     * Tests the {@code generateSamplePoints} method to ensure it correctly
     * generates the expected number of sample points for different sampling patterns.
     * <p>
     * The test verifies:
     * <ul>
     *     <li>That the correct number of samples is generated.</li>
     *     <li>That different sampling patterns produce the expected results.</li>
     * </ul>
     * </p>
     */
    @Test
    void testGenerateSamplePoints() {
        Point center = new Point(0, 0, 0);
        double size = 1.0;
        Vector normal = new Vector(0, 0, 1); // Ray pointing upward

        // TC01: Verify Jittered Sampling generates the correct number of points
        TargetArea jitteredArea = new TargetArea(center, size, SamplingPattern.JITTERED);
        List<Point> jitteredSamples = jitteredArea.generateSamplePoints(9, normal);
        assertEquals(9, jitteredSamples.size(), "Jittered sampling returned incorrect number of samples");

        // TC02: Verify Random Sampling generates the correct number of points
        TargetArea randomArea = new TargetArea(center, size, SamplingPattern.RANDOM);
        List<Point> randomSamples = randomArea.generateSamplePoints(16, normal);
        assertEquals(16, randomSamples.size(), "Random sampling returned incorrect number of samples");
    }

    /**
     * Ensures that generated sample points are within the expected sampling area.
     */
    @Test
    void testSamplesStayWithinBounds() {
        Point center = new Point(0, 0, 0);
        double size = 2.0;
        Vector normal = new Vector(0, 0, 1);

        TargetArea area = new TargetArea(center, size, SamplingPattern.RANDOM);
        List<Point> samples = area.generateSamplePoints(100, normal);

        for (Point sample : samples) {
            double dx = Math.abs(sample.getX() - center.getX());
            double dy = Math.abs(sample.getY() - center.getY());

            assertTrue(dx <= size / 2, "Sample out of bounds in X direction");
            assertTrue(dy <= size / 2, "Sample out of bounds in Y direction");
        }
    }

    /**
     * Ensures that Random Sampling does not generate too many duplicate points.
     */
    @Test
    void testRandomSamplingUniqueness() {
        Point center = new Point(0, 0, 0);
        double size = 2.0;
        Vector normal = new Vector(0, 0, 1);

        TargetArea area = new TargetArea(center, size, SamplingPattern.RANDOM);
        List<Point> samples = area.generateSamplePoints(50, normal);

        Set<Point> uniqueSamples = new HashSet<>(samples);
        assertTrue(uniqueSamples.size() > 45, "Too many duplicate points in Random Sampling");
    }

    /**
     * Ensures that increasing the sampling area size affects the spread of generated points.
     */
    @Test
    void testSamplingAreaSizeEffect() {
        Point center = new Point(0, 0, 0);
        Vector normal = new Vector(0, 0, 1);

        TargetArea smallArea = new TargetArea(center, 1.0, SamplingPattern.JITTERED);
        List<Point> smallSamples = smallArea.generateSamplePoints(16, normal);

        TargetArea largeArea = new TargetArea(center, 3.0, SamplingPattern.JITTERED);
        List<Point> largeSamples = largeArea.generateSamplePoints(16, normal);

        double smallMaxDistance = smallSamples.stream()
                .mapToDouble(p -> p.distance(center))
                .max().orElse(0);

        double largeMaxDistance = largeSamples.stream()
                .mapToDouble(p -> p.distance(center))
                .max().orElse(0);

        assertTrue(largeMaxDistance > smallMaxDistance, "Larger sampling area did not increase spread");
    }
}
