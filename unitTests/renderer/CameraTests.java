//package renderer;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//import org.junit.jupiter.api.Test;
//
//import primitives.*;
//import scene.Scene;
//
///**
// * Testing Camera Class
// *
// * @author Dan
// */
//class CameraTests {
//
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
//
//}
