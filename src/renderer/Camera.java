//package renderer;
//
//import primitives.Color;
//import primitives.Point;
//import primitives.Ray;
//import primitives.Vector;
//
//import java.util.MissingResourceException;
//
//import static primitives.Util.alignZero;
//import static primitives.Util.isZero;
//
///**
// * Represents a camera in a 3D space.
// */
//public class Camera implements Cloneable {
//    private Point p0;
//    private Point VPCenter;
//    private Vector vUp;
//    private Vector vTo;
//    private Vector vRight;
//    private double width = 0.0;
//    private double height = 0.0;
//    private double distance = 0.0;
//
//    private ImageWriter imageWriter;
//    private RayTracerBase rayTracer;
////    private RayGenerator rayGenerator = new RayGenerator();
//    public RayGenerator rayGenerator = new RayGenerator();
//    private SuperSamplingManager samplingManager;
//
//    // Getters
//    public Vector getvUp() {
//        return vUp;
//    }
//
//    public Vector getvTo() {
//        return vTo;
//    }
//
//    public double getWidth() {
//        return width;
//    }
//
//    public double getHeight() {
//        return height;
//    }
//
//    public double getDistance() {
//        return distance;
//    }
//
//
//    /**
//     * Private default constructor (no parameters)
//     */
//    private Camera() {
//    }
//
//
//    /**
//     * Returns a new Builder instance.
//     *
//     * @return a new Builder instance.
//     */
//    public static Builder getBuilder() {
//        return new Builder();
//    }
//
//
//    /**
//     * Renders the image by casting rays through each pixel and computing the color.
//     *
//     * @return the Camera object itself for chaining
//     */
//    public Camera renderImage() {
//        int nx = imageWriter.getNx();
//        int ny = imageWriter.getNy();
//
//        for (int i = 0; i < ny; i++) {
//            for (int j = 0; j < nx; j++) {
//
////                castRay(nx, ny, j, i);
//                imageWriter.writePixel(j, i, rayTracer.traceRay(rayGenerator.generateCentralRayOfPixel(nx, ny, j, i)));
//            }
//        }
//        return this;
//    }
////    /**
////     * Renders the image by casting rays through each pixel and computing the color.
////     * @return the Camera object itself for chaining
////     */
////    public Camera renderImage() {
////        int nx = imageWriter.getNx();
////        int ny = imageWriter.getNy();
////
////        for (int i = 0; i < ny; i++) {
////            for (int j = 0; j < nx; j++) {
////                Color color;
////
////                if (samplingManager.isAntiAliasingEnabled()) {
////                    // Apply anti-aliasing and calculate the average color using SuperSamplingManager
////                    imageWriter.writePixel(j, i, samplingManager.applyAntiAliasing(
////                            this.getCameraPosition(),
////                            calculatePixelCenter(nx, ny, j, i),
////                            getPixelWidth(nx),
////                            getPixelHeight(ny),
////                            rayTracer
////                    ));
////                } else {
////                    // Cast a single ray without anti-aliasing
////
////                    imageWriter.writePixel(j, i, rayTracer.traceRay(samplingManager.generateCentralRayOfPixel(int nx, int ny, int j, int i)));
////                }
////
////
////            }
////        }
////        return this;
////    }
//
////    /**
////     * Constructs a ray from the camera through a pixel (i, j).
////     *
////     * @param nX number of pixels in the x direction
////     * @param nY number of pixels in the y direction
////     * @param j  the pixel column index
////     * @param i  the pixel row index
////     * @return the constructed ray
////     */
////    public Ray constructRay(int nX, int nY, int j, int i) {
////        Point pc = this.VPCenter; // Center point of the view plane
////        double Ry = height / nY; // Pixel height
////        double Rx = width / nX; // Pixel width
////
////        double yI = alignZero(-(i - (nY - 1) / 2d) * Ry); // Vertical shift
////        double xJ = alignZero((j - (nX - 1) / 2d) * Rx); // Horizontal shift
////
////        Point pIJ = pc;
////        if (!isZero(xJ)) pIJ = pIJ.add(vRight.scale(xJ));
////        if (!isZero(yI)) pIJ = pIJ.add(vUp.scale(yI));
////
////        return new Ray(p0, pIJ.subtract(p0));
////    }
//
////    /**
////     * Casts a ray through the center of the specified pixel, computes the color of the ray,
////     * and writes the color to the pixel.
////     *
////     * @param nx number of pixels in the x direction
////     * @param ny number of pixels in the y direction
////     * @param j  the x coordinate of the pixel
////     * @param i  the y coordinate of the pixel
////     */
////    private void castRay(int nx, int ny, int j, int i) {
////        imageWriter.writePixel(j, i, rayTracer.traceRay(constructRay(nx, ny, j, i)));
////    }
//
//    /**
//     * Prints a grid over the rendered image.
//     *
//     * @param interval the interval between grid lines
//     * @param color    the color of the grid lines
//     * @return the Camera object itself for chaining
//     */
//    public Camera printGrid(int interval, Color color) {
//        int nx = imageWriter.getNx();
//        int ny = imageWriter.getNy();
//
//        for (int i = 0; i < ny; i++) {
//            for (int j = 0; j < nx; j++) {
//                if (i % interval == 0 || j % interval == 0) {
//                    imageWriter.writePixel(j, i, color);
//                }
//            }
//        }
//        return this;
//    }
//
//    /**
//     * Writes the image to a file.
//     */
//    public void writeToImage() {
//        imageWriter.writeToImage();
//    }
//
//    @Override
//    public Object clone() throws CloneNotSupportedException {
//        Camera cloned = (Camera) super.clone();
//        cloned.p0 = new Point(this.p0);
//        cloned.VPCenter = new Point(this.VPCenter);
//        cloned.vUp = new Vector(this.vUp);
//        cloned.vTo = new Vector(this.vTo);
//        cloned.vRight = new Vector(this.vRight);
//        return cloned;
//    }
//
//
//    /**
//     * Inner static Builder class for constructing Camera objects.
//     */
//    public static class Builder {
//        private final Camera camera;
//
//        /**
//         * Default constructor for Builder initializes a new Camera object.
//         */
//        public Builder() {
//            this.camera = new Camera();
//        }
//
//        /**
//         * Constructor that initializes the Builder with an existing Camera object.
//         *
//         * @param camera the Camera object to initialize the Builder with
//         */
//        public Builder(Camera camera) {
//            this.camera = camera;
//        }
//
//
//        /**
//         * Sets the location of the camera.
//         *
//         * @param location the new location point
//         * @return the Builder object itself
//         * @throws IllegalArgumentException if the location is null
//         */
//        public Builder setLocation(Point location) {
//            if (location == null) {
//                throw new IllegalArgumentException("Location cannot be null");
//            }
//            camera.p0 = location;
//            return this;
//        }
//
//        /**
//         * Sets the direction vectors of the camera.
//         *
//         * @param vTo the direction vector towards the view plane
//         * @param vUp the direction vector upwards
//         * @return the Builder object itself
//         * @throws IllegalArgumentException if vTo and vUp are not orthogonal or if either is null
//         */
//        public Builder setDirection(Vector vTo, Vector vUp) {
//            if (vTo == null || vUp == null) {
//                throw new IllegalArgumentException("vTo and vUp cannot be null");
//            }
//            if (!isZero(vTo.dotProduct(vUp))) {
//                throw new IllegalArgumentException("vUp and vTo are not orthogonal");
//            }
//            camera.vTo = vTo.normalize();
//            camera.vUp = vUp.normalize();
//            return this;
//        }
//
//        /**
//         * Sets the view plane size.
//         *
//         * @param width  the width of the view plane
//         * @param height the height of the view plane
//         * @return the Builder object itself
//         * @throws IllegalArgumentException if width or height is not positive
//         */
//        public Builder setVPSize(double width, double height) {
//            if (alignZero(width) <= 0 || alignZero(height) <= 0) {
//                throw new IllegalArgumentException("Width and height must be positive");
//            }
//            camera.width = width;
//            camera.height = height;
//            return this;
//        }
//
//        /**
//         * Sets the view plane distance.
//         *
//         * @param distance the distance to the view plane
//         * @return the Builder object itself
//         * @throws IllegalArgumentException if distance is not positive
//         */
//        public Builder setVPDistance(double distance) {
//            if (alignZero(distance) <= 0) {
//                throw new IllegalArgumentException("Distance must be positive");
//            }
//            camera.distance = distance;
//            return this;
//        }
//
//        /**
//         * Sets the ImageWriter for the camera.
//         *
//         * @param imageWriter the ImageWriter object
//         * @return the Builder object itself
//         * @throws IllegalArgumentException if the imageWriter is null
//         */
//        public Builder setImageWriter(ImageWriter imageWriter) {
//            if (imageWriter == null) {
//                throw new IllegalArgumentException("imageWriter cannot be null");
//            }
//            camera.imageWriter = imageWriter;
//            return this;
//        }
//
//        /**
//         * Sets the RayTracer for the camera.
//         *
//         * @param rayTracer the RayTracerBase object
//         * @return the Builder object itself
//         * @throws IllegalArgumentException if the rayTracer is null
//         */
//        public Builder setRayTracer(RayTracerBase rayTracer) {
//            if (rayTracer == null) {
//                throw new IllegalArgumentException("rayTracer cannot be null");
//            }
//            camera.rayTracer = rayTracer;
//            return this;
//        }
//
//        /**
//         * Sets the SuperSamplingManager for the camera.
//         *
//         * @param samplingManager the SuperSamplingManager object
//         * @return the Builder object itself for method chaining
//         */
//        public Builder setSuperSamplingManager(SuperSamplingManager samplingManager) {
//            if (samplingManager == null) {
//                throw new IllegalArgumentException("SuperSamplingManager cannot be null");
//            }
//            camera.samplingManager = samplingManager;
//            return this;
//        }
//
//
//        /**
//         * Builds the Camera object after checking that all necessary fields are set.
//         *
//         * @return the constructed Camera object
//         * @throws MissingResourceException if any required field is not set
//         * @throws IllegalArgumentException if any field values are invalid
//         */
//        public Camera build() {
//            final String MISSING_RESOURCE_MESSAGE = "Missing rendering data";
//            final String CAMERA_CLASS_NAME = "Camera";
//
//            if (isZero(camera.width)) {
//                throw new MissingResourceException(MISSING_RESOURCE_MESSAGE, CAMERA_CLASS_NAME, "width");
//            }
//            if (isZero(camera.height)) {
//                throw new MissingResourceException(MISSING_RESOURCE_MESSAGE, CAMERA_CLASS_NAME, "height");
//            }
//            if (isZero(camera.distance)) {
//                throw new MissingResourceException(MISSING_RESOURCE_MESSAGE, CAMERA_CLASS_NAME, "distance");
//            }
//            if (camera.vUp == null) {
//                throw new MissingResourceException(MISSING_RESOURCE_MESSAGE, CAMERA_CLASS_NAME, "vUp");
//            }
//            if (camera.vTo == null) {
//                throw new MissingResourceException(MISSING_RESOURCE_MESSAGE, CAMERA_CLASS_NAME, "vTo");
//            }
//            if (camera.p0 == null) {
//                throw new MissingResourceException(MISSING_RESOURCE_MESSAGE, CAMERA_CLASS_NAME, "p0");
//            }
//            if (camera.imageWriter == null) {
//                throw new MissingResourceException(MISSING_RESOURCE_MESSAGE, CAMERA_CLASS_NAME, "imageWriter");
//            }
//            if (camera.rayTracer == null) {
//                throw new MissingResourceException(MISSING_RESOURCE_MESSAGE, CAMERA_CLASS_NAME, "rayTracer");
//            }
//
//            // Check for invalid values
//            if (!isZero(camera.vTo.dotProduct(camera.vUp))) {
//                throw new IllegalArgumentException("vTo and vUp are not orthogonal");
//            }
//
//            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();
//            camera.VPCenter = camera.p0.add(camera.vTo.scale(camera.distance));
//            try {
//                return (Camera) camera.clone();
//            } catch (CloneNotSupportedException e) {
//                // This should never happen because Camera implements Cloneable
//                throw new RuntimeException(e);
//            }
//        }
//    }
//}
//
package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.MissingResourceException;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Represents a camera in a 3D space.
 */
public class Camera implements Cloneable {
    private Point p0;
    private Point VPCenter;
    private Vector vUp;
    private Vector vTo;
    private Vector vRight;
    private double width = 0.0;
    private double height = 0.0;
    private double distance = 0.0;

    private ImageWriter imageWriter;
    private RayTracerBase rayTracer;
    private final SuperSamplingManager samplingManager = new SuperSamplingManager(this);
    public RayGenerator rayGenerator = new RayGenerator(this);

    // Getters
    public Point getP0() {
        return p0;
    }

    public Point getVPCenter() {
        return VPCenter;
    }

    public Vector getVUp() {
        return vUp;
    }

    public Vector getVTo() {
        return vTo;
    }

    public Vector getVRight() {
        return vRight;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getDistance() {
        return distance;
    }

    public RayTracerBase getRayTracer() {
        return rayTracer;
    }

    public ImageWriter getImageWriter() {
        return imageWriter;
    }

    /**
     * Private default constructor (no parameters)
     */
    private Camera() {
    }

    /**
     * Returns a new Builder instance.
     *
     * @return a new Builder instance.
     */
    public static Builder getBuilder() {
        return new Builder();
    }

    /**
     * Renders the image by casting rays through each pixel and computing the color.
     *
     * @return the Camera object itself for chaining
     */
    public Camera renderImage() {
        int nx = imageWriter.getNx();
        int ny = imageWriter.getNy();
        Ray centralRay;
        for (int i = 0; i < ny; i++) {
            for (int j = 0; j < nx; j++) {
                centralRay = rayGenerator.generateCentralRayOfPixel(nx, ny, j, i);
                imageWriter.writePixel(j, i, samplingManager.isAntiAliasingEnabled()
                        ? samplingManager.applyAntiAliasing(nx, ny, j, i)
                        : rayTracer.traceRay(centralRay)
                );
            }
        }
        return this;
    }


    /**
     * Prints a grid over the rendered image.
     *
     * @param interval the interval between grid lines
     * @param color    the color of the grid lines
     * @return the Camera object itself for chaining
     */
    public Camera printGrid(int interval, Color color) {
        int nx = imageWriter.getNx();
        int ny = imageWriter.getNy();

        for (int i = 0; i < ny; i++) {
            for (int j = 0; j < nx; j++) {
                if (i % interval == 0 || j % interval == 0) {
                    imageWriter.writePixel(j, i, color);
                }
            }
        }
        return this;
    }

    /**
     * Writes the image to a file.
     */
    public void writeToImage() {
        imageWriter.writeToImage();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Camera cloned = (Camera) super.clone();
        cloned.p0 = new Point(this.p0);
        cloned.VPCenter = new Point(this.VPCenter);
        cloned.vUp = new Vector(this.vUp);
        cloned.vTo = new Vector(this.vTo);
        cloned.vRight = new Vector(this.vRight);
        return cloned;
    }


    /**
     * Inner static Builder class for constructing Camera objects.
     */
    public static class Builder {
        private final Camera camera;

        /**
         * Default constructor for Builder initializes a new Camera object.
         */
        public Builder() {
            this.camera = new Camera();
        }

        /**
         * Constructor that initializes the Builder with an existing Camera object.
         *
         * @param camera the Camera object to initialize the Builder with
         */
        public Builder(Camera camera) {
            this.camera = camera;
        }


        /**
         * Sets the location of the camera.
         *
         * @param location the new location point
         * @return the Builder object itself
         * @throws IllegalArgumentException if the location is null
         */
        public Builder setLocation(Point location) {
            if (location == null) {
                throw new IllegalArgumentException("Location cannot be null");
            }
            camera.p0 = location;
            return this;
        }

        /**
         * Sets the direction vectors of the camera.
         *
         * @param vTo the direction vector towards the view plane
         * @param vUp the direction vector upwards
         * @return the Builder object itself
         * @throws IllegalArgumentException if vTo and vUp are not orthogonal or if either is null
         */
        public Builder setDirection(Vector vTo, Vector vUp) {
            if (vTo == null || vUp == null) {
                throw new IllegalArgumentException("vTo and vUp cannot be null");
            }
            if (!isZero(vTo.dotProduct(vUp))) {
                throw new IllegalArgumentException("vUp and vTo are not orthogonal");
            }
            camera.vTo = vTo.normalize();
            camera.vUp = vUp.normalize();
            return this;
        }

        /**
         * Sets the view plane size.
         *
         * @param width  the width of the view plane
         * @param height the height of the view plane
         * @return the Builder object itself
         * @throws IllegalArgumentException if width or height is not positive
         */
        public Builder setVPSize(double width, double height) {
            if (alignZero(width) <= 0 || alignZero(height) <= 0) {
                throw new IllegalArgumentException("Width and height must be positive");
            }
            camera.width = width;
            camera.height = height;
            return this;
        }

        /**
         * Sets the view plane distance.
         *
         * @param distance the distance to the view plane
         * @return the Builder object itself
         * @throws IllegalArgumentException if distance is not positive
         */
        public Builder setVPDistance(double distance) {
            if (alignZero(distance) <= 0) {
                throw new IllegalArgumentException("Distance must be positive");
            }
            camera.distance = distance;
            return this;
        }

        /**
         * Sets the ImageWriter for the camera.
         *
         * @param imageWriter the ImageWriter object
         * @return the Builder object itself
         * @throws IllegalArgumentException if the imageWriter is null
         */
        public Builder setImageWriter(ImageWriter imageWriter) {
            if (imageWriter == null) {
                throw new IllegalArgumentException("imageWriter cannot be null");
            }
            camera.imageWriter = imageWriter;
            return this;
        }

        /**
         * Sets the RayTracer for the camera.
         *
         * @param rayTracer the RayTracerBase object
         * @return the Builder object itself
         * @throws IllegalArgumentException if the rayTracer is null
         */
        public Builder setRayTracer(RayTracerBase rayTracer) {
            if (rayTracer == null) {
                throw new IllegalArgumentException("rayTracer cannot be null");
            }
            camera.rayTracer = rayTracer;
            return this;
        }


        public Builder enableAntiAliasing(int numSamples) {
            camera.samplingManager.setAntiAliasingSamples(numSamples);
            return this;
        }


        /**
         * Builds the Camera object after checking that all necessary fields are set.
         *
         * @return the constructed Camera object
         * @throws MissingResourceException if any required field is not set
         * @throws IllegalArgumentException if any field values are invalid
         */
        public Camera build() {
            final String MISSING_RESOURCE_MESSAGE = "Missing rendering data";
            final String CAMERA_CLASS_NAME = "Camera";

            if (isZero(camera.width)) {
                throw new MissingResourceException(MISSING_RESOURCE_MESSAGE, CAMERA_CLASS_NAME, "width");
            }
            if (isZero(camera.height)) {
                throw new MissingResourceException(MISSING_RESOURCE_MESSAGE, CAMERA_CLASS_NAME, "height");
            }
            if (isZero(camera.distance)) {
                throw new MissingResourceException(MISSING_RESOURCE_MESSAGE, CAMERA_CLASS_NAME, "distance");
            }
            if (camera.vUp == null) {
                throw new MissingResourceException(MISSING_RESOURCE_MESSAGE, CAMERA_CLASS_NAME, "vUp");
            }
            if (camera.vTo == null) {
                throw new MissingResourceException(MISSING_RESOURCE_MESSAGE, CAMERA_CLASS_NAME, "vTo");
            }
            if (camera.p0 == null) {
                throw new MissingResourceException(MISSING_RESOURCE_MESSAGE, CAMERA_CLASS_NAME, "p0");
            }
            if (camera.imageWriter == null) {
                throw new MissingResourceException(MISSING_RESOURCE_MESSAGE, CAMERA_CLASS_NAME, "imageWriter");
            }
            if (camera.rayTracer == null) {
                throw new MissingResourceException(MISSING_RESOURCE_MESSAGE, CAMERA_CLASS_NAME, "rayTracer");
            }

            // Check for invalid values
            if (!isZero(camera.vTo.dotProduct(camera.vUp))) {
                throw new IllegalArgumentException("vTo and vUp are not orthogonal");
            }

            camera.vRight = camera.vTo.crossProduct(camera.vUp).normalize();
            camera.VPCenter = camera.p0.add(camera.vTo.scale(camera.distance));
            try {
                return (Camera) camera.clone();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
