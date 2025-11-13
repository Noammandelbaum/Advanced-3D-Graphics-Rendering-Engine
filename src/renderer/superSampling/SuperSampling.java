package renderer.superSampling;

import primitives.Color;
import primitives.Point;
import primitives.Ray;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements the SuperSampling technique for rendering.
 * <p>
 * SuperSampling is a method used in computer graphics to reduce aliasing
 * by averaging multiple sample points within each pixel. This class
 * facilitates the generation of additional sample rays to improve
 * rendering quality.
 * </p>
 */
public class SuperSampling {
    protected int numSamples;
    protected TargetArea targetArea;

    /**
     * Constructs a `SuperSampling` instance with a given number of samples, sampling area size, and pattern.
     * <p>
     * This constructor initializes the sampling configuration, ensuring that the input parameters
     * are valid and defining the area where supersampling will be applied.
     * </p>
     *
     * @param numSamples The number of samples to generate per pixel (must be at least 1).
     * @param size The size of the sampling area (must be positive).
     * @param pattern The sampling pattern to use (e.g., Grid, Jittered, Poisson, Random).
     * @throws IllegalArgumentException if `numSamples` is less than 1 or `size` is not positive.
     */
    public SuperSampling(int numSamples, double size, SamplingPattern pattern) {
        if (numSamples < 1) {
            throw new IllegalArgumentException("Number of samples must be at least 1");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Sampling size must be positive");
        }

        this.numSamples = numSamples;
        this.targetArea = new TargetArea(new Point(0, 0, 0), size, pattern);
    }

    /**
     * Generates multiple sample rays for a given center point and ray direction.
     * <p>
     * This method distributes sample points within the target area, generates
     * corresponding rays from the given center point, and returns a list of those rays.
     * The rays are used to compute multiple color samples per pixel, improving anti-aliasing quality.
     * </p>
     *
     * @param newCenter The center of the sampling area where the rays will originate (must not be null).
     * @param ray The original ray being sampled (must not be null).
     * @return A list of sampled rays originating from different points within the target area.
     * @throws IllegalArgumentException if `newCenter` or `ray` is null.
     */
    public List<Ray> generateSampleRays(Point newCenter, Ray ray) {
        if (newCenter == null || ray == null) {
            throw new IllegalArgumentException("Center point and ray cannot be null");
        }

        targetArea.setCenter(newCenter);
        List<Point> samplePoints = targetArea.generateSamplePoints(numSamples, ray.getDir());
        List<Ray> sampleRays = new ArrayList<>();

        for (Point p : samplePoints) {
            sampleRays.add(new Ray(ray.getP0(), p.subtract(ray.getP0())));
        }

        return sampleRays;
    }

    /**
     * Computes the average color from a list of color samples.
     *
     * @param colors A list of colors to average.
     * @return The averaged color.
     * @throws IllegalArgumentException if the list is empty.
     */
    public Color calculateAverageColor(List<Color> colors) {
        if (colors.isEmpty()) {
            throw new IllegalArgumentException("Color list must not be empty");
        }

        Color accumulatedColor = new Color(0, 0, 0);
        for (Color color : colors) {
            accumulatedColor = accumulatedColor.add(color);
        }

        return accumulatedColor.reduce(colors.size());
    }

}
