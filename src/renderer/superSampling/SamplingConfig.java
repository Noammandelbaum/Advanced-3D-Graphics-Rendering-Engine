package renderer.superSampling;

/**
 * Configuration class for multi-sampling techniques.
 * <p>
 * This class defines the settings used for multi-sampling operations,
 * such as anti-aliasing and future sampling-based improvements in the rendering process.
 * </p>
 * <p>
 * Currently, it is used specifically for configuring anti-aliasing settings,
 * but additional sampling techniques can be added in the future.
 * </p>
 */
public class SamplingConfig {
    private int antiAliasingSamples = 0;
    private double antiAliasingSize = 0.5;
    private SamplingPattern antiAliasingPattern = SamplingPattern.JITTERED;

    /**
     * Enables anti-aliasing with the specified sampling parameters.
     * <p>
     * This method configures anti-aliasing by setting the number of samples,
     * the sampling area size, and the sampling pattern.
     * </p>
     * <p>
     * Although currently used only for anti-aliasing, this configuration
     * can be expanded for additional multi-sampling techniques in the future.
     * </p>
     *
     * @param samples The number of samples per pixel (must be at least 1).
     * @param size    The size of the sampling area (must be positive).
     * @param pattern The sampling pattern to use.
     * @return The updated {@code SamplingConfig} instance (for method chaining).
     * @throws IllegalArgumentException if `samples` is less than 1 or `size` is not positive.
     */
    public SamplingConfig enableAntiAliasing(int samples, double size, SamplingPattern pattern) {
        if (samples < 1) {
            throw new IllegalArgumentException("Anti-aliasing samples must be at least 1");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Sampling size must be positive");
        }

        this.antiAliasingSamples = samples;
        this.antiAliasingSize = size;
        this.antiAliasingPattern = pattern;
        return this;
    }

    /**
     * Checks if anti-aliasing is enabled.
     *
     * @return {@code true} if anti-aliasing is enabled, {@code false} otherwise.
     */
    public boolean isAntiAliasingEnabled() {
        return antiAliasingSamples > 1;
    }

    /**
     * Gets the number of anti-aliasing samples per pixel.
     *
     * @return The number of samples.
     */
    public int getAntiAliasingSamples() {
        return antiAliasingSamples;
    }

    /**
     * Gets the size of the sampling area used for anti-aliasing.
     *
     * @return The sampling size.
     */
    public double getAntiAliasingSize() {
        return antiAliasingSize;
    }

    /**
     * Gets the sampling pattern used for anti-aliasing.
     *
     * @return The selected sampling pattern.
     */
    public SamplingPattern getAntiAliasingPattern() {
        return antiAliasingPattern;
    }
}
