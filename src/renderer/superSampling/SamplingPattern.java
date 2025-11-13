package renderer.superSampling;

import primitives.Point;
import primitives.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Defines sampling patterns for super-sampling techniques.
 * <p>
 * This enum provides two sampling strategies for generating sample points
 * in an area to improve image quality:
 * <ul>
 *     <li>{@code JITTERED} - Generates sample points in a grid with random offsets.</li>
 *     <li>{@code RANDOM} - Generates completely random sample points.</li>
 * </ul>
 * These patterns help distribute rays more effectively in the rendering process.
 * </p>
 */
public enum SamplingPattern {
    JITTERED, RANDOM;

    /**
     * Generates a list of sample points according to the selected sampling pattern.
     * <p>
     * The samples are distributed in a 2D area perpendicular to the given normal vector.
     * The distribution method depends on the sampling pattern:
     * <ul>
     *     <li>{@code JITTERED} - Samples are placed in a grid with slight random offsets.</li>
     *     <li>{@code RANDOM} - Samples are distributed randomly across the area.</li>
     * </ul>
     * </p>
     *
     * @param numSamples The number of sample points to generate (must be positive).
     * @param center The center point of the sampling area.
     * @param size The size of the sampling area (must be positive).
     * @param normal The normal vector defining the sampling plane.
     * @return A list of generated sample points.
     * @throws IllegalArgumentException if `numSamples` or `size` is not positive, or if `center` or `normal` is null.
     */
    public List<Point> generateSamples(int numSamples, Point center, double size, Vector normal) {
        if (numSamples < 1) {
            throw new IllegalArgumentException("Number of samples must be positive");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Sampling size must be positive");
        }
        if (center == null || normal == null) {
            throw new IllegalArgumentException("Center and normal cannot be null");
        }

        // Create perpendicular vectors to the normal
        Vector xAxis = normal.createPerpendicular();
        Vector yAxis = normal.crossProduct(xAxis).normalize();

        return switch (this) {
            case JITTERED -> generateJitteredSamples(numSamples, center, size, xAxis, yAxis);
            case RANDOM -> generateRandomSamples(numSamples, center, size, xAxis, yAxis);
        };
    }

    /**
     * Generates sample points using a jittered grid pattern.
     * <p>
     * The area is divided into a grid, and each sample is placed within a random
     * offset inside its respective grid cell, ensuring an even but non-uniform distribution.
     * </p>
     *
     * @param numSamples The total number of sample points.
     * @param center The center of the sampling area.
     * @param size The size of the area in which samples are generated.
     * @param xAxis The x-axis vector defining the plane.
     * @param yAxis The y-axis vector defining the plane.
     * @return A list of sample points generated using jittered sampling.
     */
    private List<Point> generateJitteredSamples(int numSamples, Point center, double size, Vector xAxis, Vector yAxis) {
        List<Point> points = new ArrayList<>();
        Random rand = new Random();
        int gridSize = (int) Math.sqrt(numSamples);
        double step = size / gridSize;

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                double xOffset = (i + rand.nextDouble()) * step - size / 2;
                double yOffset = (j + rand.nextDouble()) * step - size / 2;

                Point samplePoint = center.add(xAxis.scale(xOffset)).add(yAxis.scale(yOffset));
                points.add(samplePoint);
            }
        }
        return points;
    }

    /**
     * Generates sample points using a random distribution.
     * <p>
     * Each sample is placed randomly within the defined area, resulting in a
     * completely non-uniform distribution.
     * </p>
     *
     * @param numSamples The total number of sample points.
     * @param center The center of the sampling area.
     * @param size The size of the area in which samples are generated.
     * @param xAxis The x-axis vector defining the plane.
     * @param yAxis The y-axis vector defining the plane.
     * @return A list of sample points generated using random sampling.
     */
    private List<Point> generateRandomSamples(int numSamples, Point center, double size, Vector xAxis, Vector yAxis) {
        List<Point> points = new ArrayList<>();
        Random rand = new Random();

        for (int i = 0; i < numSamples; i++) {
            double xOffset = (rand.nextDouble() - 0.5) * size;
            double yOffset = (rand.nextDouble() - 0.5) * size;

            Point samplePoint = center.add(xAxis.scale(xOffset)).add(yAxis.scale(yOffset));
            points.add(samplePoint);
        }
        return points;
    }
}
