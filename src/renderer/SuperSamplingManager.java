package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Manages all super sampling techniques for image enhancement.
 */
public class SuperSamplingManager {
    private final Camera camera;

    private boolean antiAliasingEnabled = false;
    private int antiAliasingSamples;
    private double pixelRadius;

    private static final int MIN_SAMPLES = 30;
    private static final int MAX_SAMPLES = 1000;

    public SuperSamplingManager(Camera camera) {
        this.camera = camera;
    }

    public boolean isAntiAliasingEnabled() {
        return antiAliasingEnabled;
    }

    public void setAntiAliasingSamples(int numSamples) {
        if (numSamples < MIN_SAMPLES || numSamples > MAX_SAMPLES) {
            throw new IllegalArgumentException("numSamples must be between " + MIN_SAMPLES + " and " + MAX_SAMPLES);
        }

        this.antiAliasingEnabled = true;
        this.antiAliasingSamples = numSamples;
        this.pixelRadius = 0.5 * Math.min(camera.getWidth() /
                camera.getImageWriter().getNx(), camera.getHeight() / camera.getImageWriter().getNy());
    }

    /**
     * Gets the number of samples used for anti-aliasing.
     *
     * @return the number of anti-aliasing samples
     */
    public int getAntiAliasingSamples() {
        return antiAliasingSamples;
    }

    /**
     * General method to calculate the average color from a list of rays.
     *
     * @param rays                     the list of rays to calculate color for
     * @param colorCalculationFunction function to calculate color for each ray
     * @return the averaged color
     */
    private Color calculateAverageColor(List<Ray> rays, Function<Ray, Color> colorCalculationFunction) {
        Color averageColor = Color.BLACK;
        for (Ray ray : rays) {
            averageColor = averageColor.add(colorCalculationFunction.apply(ray));
        }
        return averageColor.reduce(rays.size());
    }

    /**
     * Applies anti-aliasing by generating rays and calculating average color.
     *
     * @return the averaged color
     */
    public Color applyAntiAliasing(int nX, int nY, int j, int i) {
        List<Ray> rays = camera.rayGenerator.generateAntiAliasingRays(nX, nY, j, i, pixelRadius, getAntiAliasingSamples());
        return calculateAverageColor(rays, camera.getRayTracer()::traceRay);
    }
}
