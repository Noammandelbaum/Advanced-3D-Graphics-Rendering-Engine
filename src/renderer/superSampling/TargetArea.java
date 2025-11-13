package renderer.superSampling;

import primitives.Point;
import primitives.Vector;

import java.util.List;

/**
 * Represents a target area for super-sampling in a rendering process.
 *
 * <p>The `TargetArea` class is used to define a specific area in space where
 * multiple rays will be sampled to achieve anti-aliasing or other rendering enhancements.</p>
 *
 * <p>The class holds the center point of the area, its size, and a sampling pattern
 * that determines how the sampling points will be distributed.</p>
 */
public class TargetArea {
    private Point center;
    private final double size;
    private final SamplingPattern pattern; // Jittered, Random

    /**
     * Constructs a `TargetArea` object with a specified center, size, and sampling pattern.
     *
     * <p>This constructor ensures that the provided center is valid and that the size is positive.
     * The sampling pattern determines how sample points will be distributed in this area.</p>
     *
     * @param center  The center point of the target area (must not be null).
     * @param size    The size of the target area (must be positive).
     * @param pattern The sampling pattern used (e.g. Jittered, Random).
     * @throws IllegalArgumentException if the center is null or if size is non-positive.
     */
    public TargetArea(Point center, double size, SamplingPattern pattern) {
        if (center == null) {
            throw new IllegalArgumentException("Center cannot be null");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be positive");
        }
        this.center = center;
        this.size = size;
        this.pattern = pattern;
    }

    /**
     * Sets a new center for the target area.
     *
     * <p>This method updates the center point of the target area. The new center must be a valid point.</p>
     *
     * @param newCenter The new center point (must not be null).
     * @throws IllegalArgumentException if the new center is null.
     */
    public void setCenter(Point newCenter) {
        if (newCenter == null) {
            throw new IllegalArgumentException("Center cannot be null");
        }
        this.center = newCenter;
    }

    /**
     * Generates a list of sample points within the target area based on the sampling pattern.
     *
     * <p>This method distributes points inside the target area according to the specified
     * sampling pattern, which can be a jittered or random distribution.</p>
     *
     * <p>The generated sample points will be used in the rendering process to create multiple
     * rays for anti-aliasing or other effects.</p>
     *
     * @param numSamples   The number of sample points to generate (should be a positive integer).
     * @param rayDirection The direction of the ray being sampled (used for alignment).
     * @return A list of sampled points distributed within the target area.
     * @throws IllegalArgumentException if numSamples is non-positive.
     */
    public List<Point> generateSamplePoints(int numSamples, Vector rayDirection) {
        if (numSamples <= 0) {
            throw new IllegalArgumentException("Number of samples must be positive.");
        }
        return pattern.generateSamples(numSamples, center, size, rayDirection);
    }
}


