package renderer;

import primitives.Color;
import primitives.Point;
import primitives.Ray;
import primitives.Vector;
import renderer.superSampling.SamplingConfig;

import java.util.MissingResourceException;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * Represents a camera in a 3D space.
 * The Camera class manages the view plane and constructs rays for rendering.
 * It ensures proper configuration and enforces parameter validation.
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

    // Getters
    public Vector getvUp() {
        return vUp;
    }

    public Vector getvTo() {
        return vTo;
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
     * Generates a ray from the camera through a specific pixel (pixelColumnIndex, pixelRowIndex).
     *
     * @param pixelColumns Number of pixels in the x direction.
     * @param pixelRows Number of pixels in the y direction.
     * @param pixelColumnIndex The column index of the pixel.
     * @param pixelRowIndex The row index of the pixel.
     * @return The constructed ray passing through the specified pixel.
     */
    public Ray generateRayThroughPixel(int pixelColumns, int pixelRows, int pixelColumnIndex, int pixelRowIndex) {
        Point viewPlaneCenter = this.VPCenter; // Center point of the view plane
        double pixelHeight = height / pixelRows; // Pixel height
        double pixelWidth = width / pixelColumns; // Pixel width

        double yShift = alignZero(-(pixelRowIndex - (pixelRows - 1) / 2d) * pixelHeight); // Vertical shift
        double xShift = alignZero((pixelColumnIndex - (pixelColumns - 1) / 2d) * pixelWidth); // Horizontal shift

        Point pixelPosition = viewPlaneCenter;
        if (!isZero(xShift)) pixelPosition = pixelPosition.add(vRight.scale(xShift));
        if (!isZero(yShift)) pixelPosition = pixelPosition.add(vUp.scale(yShift));

        return new Ray(p0, pixelPosition.subtract(p0));
    }

    /**
     * Generates the rendered image by tracing rays through each pixel and computing their color.
     * @return The Camera object itself for method chaining.
     */
    public Camera generateRenderedImage() {
        int pixelColumns = imageWriter.getImageHeight();
        int pixelRows = imageWriter.getImageWidth();

        for (int pixelRowIndex = 0; pixelRowIndex < pixelRows; pixelRowIndex++) {
            for (int pixelColumnIndex = 0; pixelColumnIndex < pixelColumns; pixelColumnIndex++) {
                shootRayAndComputeColor(pixelColumns, pixelRows, pixelColumnIndex, pixelRowIndex);
            }
        }
        return this;
    }

    /**
     * Shoots a ray through the center of the specified pixel, computes its color,
     * and writes the color to the image.
     *
     * @param pixelColumns Number of pixels in the x direction.
     * @param pixelRows Number of pixels in the y direction.
     * @param pixelColumnIndex The column index of the pixel.
     * @param pixelRowIndex The row index of the pixel.
     */
    private void shootRayAndComputeColor(int pixelColumns, int pixelRows, int pixelColumnIndex, int pixelRowIndex) {
        imageWriter.setPixelColor(
                pixelColumnIndex, pixelRowIndex,
                rayTracer.traceRay(generateRayThroughPixel(pixelColumns, pixelRows, pixelColumnIndex, pixelRowIndex))
        );
    }

    /**
     * Overlays a grid on the rendered image by coloring specific pixels at regular intervals.
     * @param gridSpacing The spacing between grid lines (in pixels).
     * @param gridColor The color of the grid lines.
     * @return The Camera object itself for method chaining.
     */
    public Camera overlayGridOnImage(int gridSpacing, Color gridColor) {
        int pixelColumns = imageWriter.getImageHeight();
        int pixelRows = imageWriter.getImageWidth();

        for (int pixelRowIndex = 0; pixelRowIndex < pixelRows; pixelRowIndex++) {
            for (int pixelColumnIndex = 0; pixelColumnIndex < pixelColumns; pixelColumnIndex++) {
                if (pixelRowIndex % gridSpacing == 0 || pixelColumnIndex % gridSpacing == 0) {
                    imageWriter.setPixelColor(pixelColumnIndex, pixelRowIndex, gridColor);
                }
            }
        }
        return this;
    }

    /**
     * Writes the image to a file.
     */
    public void writeToImage() {
        imageWriter.saveImageToFile();
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

        public Builder setSamplingConfig(SamplingConfig config) {
            if (camera.rayTracer instanceof SimpleRayTracer) {
                ((SimpleRayTracer) camera.rayTracer).setSamplingConfig(config);
            }
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
                // This should never happen because Camera implements Cloneable
                throw new RuntimeException(e);
            }
        }
    }
}
