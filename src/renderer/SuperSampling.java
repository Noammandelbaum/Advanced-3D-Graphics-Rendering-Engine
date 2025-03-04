//package renderer;
//
//import primitives.Color;
//import primitives.Ray;
//import java.util.List;
//
///**
// * Abstract class for super sampling enhancements.
// */
//public abstract class SuperSampling {
//    protected int numSamples;
//
//    public SuperSampling(int numSamples) {
//        this.numSamples = numSamples;
//    }
//
//    /**
//     * Abstract method to generate sample rays without specific parameters.
//     * Each subclass defines its own parameters and implementation.
//     *
//     * @return a list of sample rays for the super sampling effect
//     */
//    protected abstract List<Ray> generateSampleRays();
//
////    /**
////     * Calculates the enhanced color by averaging the colors of multiple sample rays.
////     *
////     * @return the averaged color from the sampled rays
////     */
////    public abstract Color calculateEnhancedColor();
//}
