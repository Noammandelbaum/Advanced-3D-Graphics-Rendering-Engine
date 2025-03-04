//package renderer;
//
//import org.junit.jupiter.api.Test;
//import primitives.Color;
//import primitives.Point;
//import primitives.Ray;
//import primitives.Vector;
//import geometries.Plane;
//import scene.Scene;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//class RayGeneratorTests {
//    /**
//     * Camera builder for the tests
//     */
//    private final Camera.Builder cameraBuilder = Camera.getBuilder()
//            .setRayTracer(new SimpleRayTracer(new Scene("Test")))
//            .setImageWriter(new ImageWriter("Test", 1, 1))
//            .setLocation(Point.ZERO)
//            .setDirection(new Vector(0, 0, -1), new Vector(0, -1, 0))
//            .setVPDistance(10);
//
//    /**
//     * Test method for
//     * {@link renderer.RayGenerator#generateCentralRayOfPixel(int, int, int, int)}.
//     */
//    @Test
//    void generateCentralRayOfPixel() {
//        final String badRay = "Bad ray";
//
//        // ============ Equivalence Partitions Tests ==============
//        // EP01: 4X4 Inside (1,1)
//        Camera camera1 = cameraBuilder.setVPSize(8, 8).build();
//        assertEquals(new Ray(Point.ZERO, new Vector(1, -1, -10)), camera1.rayGenerator.generateCentralRayOfPixel(4, 4, 1, 1), badRay);
//
//        // =============== Boundary Values Tests ==================
//        // BV01: 4X4 Corner (0,0)
//        assertEquals(new Ray(Point.ZERO, new Vector(3, -3, -10)), camera1.rayGenerator.generateCentralRayOfPixel(4, 4, 0, 0), badRay);
//
//        // BV02: 4X4 Side (0,1)
//        assertEquals(new Ray(Point.ZERO, new Vector(1, -3, -10)), camera1.rayGenerator.generateCentralRayOfPixel(4, 4, 1, 0), badRay);
//
//        // BV03: 3X3 Center (1,1)
//        Camera camera2 = cameraBuilder.setVPSize(6, 6).build();
//        assertEquals(new Ray(Point.ZERO, new Vector(0, 0, -10)), camera2.rayGenerator.generateCentralRayOfPixel(3, 3, 1, 1), badRay);
//
//        // BV04: 3X3 Center of Upper Side (0,1)
//        assertEquals(new Ray(Point.ZERO, new Vector(0, -2, -10)), camera2.rayGenerator.generateCentralRayOfPixel(3, 3, 1, 0), badRay);
//
//        // BV05: 3X3 Center of Left Side (1,0)
//        assertEquals(new Ray(Point.ZERO, new Vector(2, 0, -10)), camera2.rayGenerator.generateCentralRayOfPixel(3, 3, 0, 1), badRay);
//
//        // BV06: 3X3 Corner (0,0)
//        assertEquals(new Ray(Point.ZERO, new Vector(2, -2, -10)), camera2.rayGenerator.generateCentralRayOfPixel(3, 3, 0, 0), badRay);
//
//    }
//
//    /**
//     * Test for generating Grid sampling rays
//     */
//    @Test
//    void testGenerateGridSamplingRays() {
//        Camera camera = cameraBuilder.setVPSize(10, 10).build();
//        Point centerOfPixel = new Point(0, 0, 0);
//        double pixelRadius = 5.0;
//        int numSamples = 16;
//
//        List<Ray> rays = camera.rayGenerator.generateAntiAliasingGridRays(numSamples, pixelRadius, camera.getVRight(), camera.getVUp(), centerOfPixel);
//        assertEquals(numSamples, rays.size(), "Wrong number of rays generated for Grid sampling");
//    }
//
//    @Test
//    public void testGenerateAntiAliasingGridRaysWithCorrectIntersection() {
//        int imageSize = 500; // גודל התמונה
//        int pixelSize = 400; // גודל הפיקסל המדומה
//        double pixelRadius = pixelSize / 2.0; // רדיוס הפיקסל
//        int numSamples = 25; // מספר הדגימות
//
//        // יצירת ImageWriter לתמונה
//        ImageWriter imageWriter = new ImageWriter("gridSamplingWithCorrectIntersection", imageSize, imageSize);
//
//        // יצירת Camera
//        Camera camera = Camera.getBuilder()
//                .setLocation(new Point(0, 0, 10))
//                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
//                .setVPDistance(10)
//                .setVPSize(pixelSize, pixelSize)
//                .build();
//        RayGenerator rayGenerator = camera.rayGenerator;
//
//        // חישוב מרכז הפיקסל
//        Point centerOfPixel = new Point(0, 0, 0);
//        Vector vRight = camera.getVRight();
//        Vector vUp = camera.getVUp();
//
//        // יצירת מישור שמייצג את משטח הצפייה
//        Plane viewPlane = new Plane(new Point(0, 0, 0), new Vector(0, 0, -1));
//
//        // קריאה לפונקציה
//        List<Ray> gridRays = rayGenerator.generateAntiAliasingGridRays(numSamples, pixelRadius, vRight, vUp, centerOfPixel);
//
//        // ציור הרקע (שחור)
//        for (int x = 0; x < imageSize; x++) {
//            for (int y = 0; y < imageSize; y++) {
//                imageWriter.writePixel(x, y, new Color(java.awt.Color.BLACK));
//            }
//        }
//
//        // חישוב חיתוך הקרניים עם המישור וציור הנקודות
//        for (Ray ray : gridRays) {
//            Point intersection = viewPlane.findIntersections(ray).getFirst(); // חיתוך הקרן עם המישור
//            double xRatio = intersection.getX() / pixelRadius; // יחס היסט בציר X
//            double yRatio = intersection.getY() / pixelRadius; // יחס היסט בציר Y
//
//            // המרה לקואורדינטות בתמונה
//            int x = (int) ((xRatio + 1) * (pixelSize / 2)) + (imageSize - pixelSize) / 2;
//            int y = (int) ((yRatio + 1) * (pixelSize / 2)) + (imageSize - pixelSize) / 2;
//
//            if (x >= 0 && x < imageSize && y >= 0 && y < imageSize) {
//                imageWriter.writePixel(x, y, new Color(java.awt.Color.RED)); // ציור נקודות בצבע אדום
//            }
//        }
//
//        // שמירת התמונה
//        imageWriter.writeToImage();
//    }
//
//}