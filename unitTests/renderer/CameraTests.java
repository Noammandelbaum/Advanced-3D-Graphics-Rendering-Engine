package renderer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import primitives.*;
import scene.Scene;

/**
 * Testing Camera Class
 *
 * @author Dan
 */
class CameraTests {

    /**
     * Camera builder for the tests
     */
    private final Camera.Builder cameraBuilder = Camera.getBuilder()
            .setRayTracer(new SimpleRayTracer(new Scene("Test")))
            .setImageWriter(new ImageWriter("Test", 1, 1))
            .setLocation(Point.ZERO)
            .setDirection(new Vector(0, 0, -1), new Vector(0, -1, 0))
            .setVPDistance(10);

    /**
     * Test method for
     * {@link renderer.Camera#generateRayThroughPixel(int, int, int, int)}.
     */
    @Test
    void testGenerateRayThroughPixel() {
        final String badRay = "Bad ray";

        // ============ Equivalence Partitions Tests ==============
        // EP01: 4X4 Inside (1,1)
        Camera camera1 = cameraBuilder.setVPSize(8, 8).build();
        assertEquals(new Ray(Point.ZERO, new Vector(1, -1, -10)), camera1.generateRayThroughPixel(4, 4, 1, 1), badRay);

        // =============== Boundary Values Tests ==================
        // BV01: 4X4 Corner (0,0)
        assertEquals(new Ray(Point.ZERO, new Vector(3, -3, -10)), camera1.generateRayThroughPixel(4, 4, 0, 0), badRay);

        // BV02: 4X4 Side (0,1)
        assertEquals(new Ray(Point.ZERO, new Vector(1, -3, -10)), camera1.generateRayThroughPixel(4, 4, 1, 0), badRay);

        // BV03: 3X3 Center (1,1)
        Camera camera2 = cameraBuilder.setVPSize(6, 6).build();
        assertEquals(new Ray(Point.ZERO, new Vector(0, 0, -10)), camera2.generateRayThroughPixel(3, 3, 1, 1), badRay);

        // BV04: 3X3 Center of Upper Side (0,1)
        assertEquals(new Ray(Point.ZERO, new Vector(0, -2, -10)), camera2.generateRayThroughPixel(3, 3, 1, 0), badRay);

        // BV05: 3X3 Center of Left Side (1,0)
        assertEquals(new Ray(Point.ZERO, new Vector(2, 0, -10)), camera2.generateRayThroughPixel(3, 3, 0, 1), badRay);

        // BV06: 3X3 Corner (0,0)
        assertEquals(new Ray(Point.ZERO, new Vector(2, -2, -10)), camera2.generateRayThroughPixel(3, 3, 0, 0), badRay);

    }

}
