package renderer;

import org.junit.jupiter.api.Test;
import primitives.Color;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for ImageWriter class.
 */
public class ImageWriterTests {

    /**
     * Test method for creating an initial image with a grid pattern.
     */
    @Test
    void testWriteGridImage() {
        // Define image properties
        int nX = 800; // width of the image
        int nY = 500; // height of the image
        ImageWriter imageWriter = new ImageWriter("Base/test_grid_image", nX, nY);

        // Define colors
        Color backgroundColor = new Color(255, 255, 255); // white color
        Color gridColor = new Color(0, 0, 0); // black color

        // Define grid properties
        int gridSpacingX = nX / 16; // horizontal spacing (16 squares in width)
        int gridSpacingY = nY / 10; // vertical spacing (10 squares in height)

        // Write grid
        for (int i = 0; i < nX; i++) {
            for (int j = 0; j < nY; j++) {
                if (i % gridSpacingX == 0 || j % gridSpacingY == 0) {
                    imageWriter.setPixelColor(i, j, gridColor);
                } else {
                    imageWriter.setPixelColor(i, j, backgroundColor);
                }
            }
        }

        // Write the image to a file
        imageWriter.saveImageToFile();

        // Verify that the image file was created
        File file = new File(System.getProperty("user.dir") + "/images/Base/test_grid_image.png");
        assertTrue(file.exists(), "Image file was not created");
    }
}
