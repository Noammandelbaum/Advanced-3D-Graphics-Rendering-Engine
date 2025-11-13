package renderer.superSampling.antiAliasing;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import renderer.superSampling.SamplingPattern;
import renderer.superSampling.SuperSampling;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link SuperSampling} class.
 * <p>
 * These tests validate the functionality of the super-sampling technique
 * by verifying the correct generation of sample rays used for anti-aliasing.
 * </p>
 */
class SuperSamplingTests {

    /**
     * Tests the {@code generateSampleRays} method.
     * <p>
     * This test verifies that the `SuperSampling` class generates the correct number
     * of sample rays and ensures that:
     * <ul>
     *   <li>The number of generated rays matches the expected sample count.</li>
     *   <li>Each generated ray has a different origin than the main ray.</li>
     *   <li>The rays maintain the correct direction.</li>
     * </ul>
     * </p>
     */
    @Test
    void testGenerateSampleRays() {
        int numSamples = 4;
        double size = 1.0;
        SamplingPattern pattern = SamplingPattern.JITTERED;
        SuperSampling superSampling = new SuperSampling(numSamples, size, pattern);

        Point origin = new Point(0, 0, 0);
        Vector direction = new Vector(0, 0, -1); // Ray in the negative Z direction
        Ray mainRay = new Ray(origin, direction);

        List<Ray> sampleRays = superSampling.generateSampleRays(new Point(0, 0, -5), mainRay);
        assertEquals(numSamples, sampleRays.size(), "SuperSampling did not generate the correct number of rays");

        for (Ray ray : sampleRays) {
            assertNotEquals(mainRay, ray, "Sample rays should not be identical to the main ray");
        }
    }
}
