package renderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import primitives.Color;

/**
 * Image writer class combines accumulation of pixel color matrix and finally
 * producing a non-optimized jpeg image from this matrix. The class although is
 * responsible of holding image related parameters of View Plane - pixel matrix
 * size and resolution
 */
public class ImageWriter {
    /**
     * Image width in pixels.
     */
    private int imageWidth;

    /**
     * Image height in pixels.
     */
    private int imageHeight;

    /**
     * Directory path where the image file will be saved.
     */
    private static final String OUTPUT_DIRECTORY = System.getProperty("user.dir") + "/images";

    /**
     * Pixel color matrix storing the image data.
     */
    private BufferedImage pixelMatrix;

    /**
     * Image file name (without extension).
     */
    private String fileName;

    /**
     * Logger for reporting I/O failures.
     */
    private Logger logWriter = Logger.getLogger("ImageWriter");

    /**
     * Constructs an `ImageWriter` with the given parameters.
     *
     * @param fileName    The name of the output file (without extension).
     * @param imageWidth  The width of the image in pixels.
     * @param imageHeight The height of the image in pixels.
     */
    public ImageWriter(String fileName, int imageWidth, int imageHeight) {
        if (imageWidth <= 0 || imageHeight <= 0) {
            throw new IllegalArgumentException("Image resolution must be positive");
        }

        this.fileName = fileName;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;

        pixelMatrix = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_RGB);
    }

    /**
     * Gets the image width in pixels.
     *
     * @return The width of the image.
     */
    public int getImageWidth() {
        return imageWidth;
    }

    /**
     * Gets the image height in pixels.
     *
     * @return The height of the image.
     */
    public int getImageHeight() {
        return imageHeight;
    }

    /**
     * Saves the image as a PNG file in the output directory.
     * <p>
     * If the directory does not exist, an error will be logged.
     * </p>
     */
    public void saveImageToFile() {
        try {
            File file = new File(OUTPUT_DIRECTORY + '/' + fileName + ".png");
            ImageIO.write(pixelMatrix, "png", file);
        } catch (IOException e) {
            logWriter.log(Level.SEVERE, "I/O error", e);
            throw new IllegalStateException("I/O error - directory might be missing: " + OUTPUT_DIRECTORY, e);
        }
    }

    /**
     * Sets the color of a specific pixel in the image.
     *
     * @param x     The X-coordinate of the pixel.
     * @param y     The Y-coordinate of the pixel.
     * @param color The final color of the pixel.
     */
    public void setPixelColor(int x, int y, Color color) {
        pixelMatrix.setRGB(x, y, color.getColor().getRGB());
    }
}

