package renderer;

import org.junit.jupiter.api.Test;
import primitives.Point;
import primitives.Vector;
import geometries.*;
import scene.Scene;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Integration tests for ray construction from camera and intersection calculation with geometrical shapes.
 */
public class CameraRayIntersectionsIntegrationTests {

    // Initialize the camera for some sphere tests
    private final Camera cameraForSphere = Camera.getBuilder()
            .setRayTracer(new SimpleRayTracer(new Scene("Test")))
            .setImageWriter(new ImageWriter("Test", 3, 3))
            .setLocation(new Point(0, 0, 0.5)) // Camera location updated to (0,0,0.5) for some sphere tests
            .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
            .setVPDistance(1)
            .setVPSize(3, 3)
            .build();

    // Initialize the camera for other tests (default location at (0, 0, 0))
    private final Camera camera = Camera.getBuilder()
            .setRayTracer(new SimpleRayTracer(new Scene("Test")))
            .setImageWriter(new ImageWriter("Test", 3, 3))
            .setLocation(Point.ZERO) // Camera location at (0,0,0)
            .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
            .setVPDistance(1)
            .setVPSize(3, 3)
            .build();

    /**
     * Helper method to count intersections of rays from camera with a given geometry.
     *
     * @param camera   the camera object
     * @param geometry the intersectable geometry
     * @return the number of intersection points
     */
    private int countIntersections(Camera camera, Intersectable geometry) {
        int count = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (geometry.findIntersections(camera.generateRayThroughPixel(3, 3, j, i)) != null) {
                    count += geometry.findIntersections(camera.generateRayThroughPixel(3, 3, j, i)).size();
                }
            }
        }
        return count;
    }

    /**
     * Test method for integration of ray construction with sphere intersections.
     */
    @Test
    public void testRaySphereIntersections() {
        // TC01: Small Sphere (2 intersection points)
        Sphere sphere1 = new Sphere(1, new Point(0, 0, -3));
        assertEquals(2, countIntersections(camera, sphere1), "Wrong number of intersections for small sphere");

        // TC02: Large Sphere (18 intersection points)
        Sphere sphere2 = new Sphere(2.5, new Point(0, 0, -2.5));
        assertEquals(18, countIntersections(cameraForSphere, sphere2), "Wrong number of intersections for large sphere");

        // TC03: Medium Sphere (10 intersection points)
        Sphere sphere3 = new Sphere(2, new Point(0, 0, -2));
        assertEquals(10, countIntersections(cameraForSphere, sphere3), "Wrong number of intersections for medium sphere");

        // TC04: Sphere with larger radius and closer to camera (9 intersection points)
        Sphere sphere4 = new Sphere(4, new Point(0, 0, -0.5));
        assertEquals(9, countIntersections(camera, sphere4), "Wrong number of intersections for larger sphere");

        // TC05: Sphere behind the camera (0 intersection points)
        Sphere sphere5 = new Sphere(0.5, new Point(0, 0, 1));
        assertEquals(0, countIntersections(cameraForSphere, sphere5), "Wrong number of intersections for sphere behind camera");
    }

    /**
     * Test method for integration of ray construction with plane intersections.
     */
    @Test
    public void testRayPlaneIntersections() {
        // TC01: Plane parallel to the view plane (9 intersection points)
        Plane plane1 = new Plane(new Point(0, 0, -5), new Vector(0, 0, 1));
        assertEquals(9, countIntersections(camera, plane1), "Wrong number of intersections for plane parallel to view plane");

        // TC02: Plane with small angle to the view plane (9 intersection points)
        Plane plane2 = new Plane(new Point(0, 0, -5), new Vector(0, 1, 2));
        assertEquals(9, countIntersections(camera, plane2), "Wrong number of intersections for plane with small angle");

        // TC03: Plane with big angle to the view plane (6 intersection points)
        Plane plane3 = new Plane(new Point(0, 0, -5), new Vector(-1, 1, 1));
        assertEquals(6, countIntersections(camera, plane3), "Wrong number of intersections for plane with big angle");
    }

    /**
     * Test method for integration of ray construction with triangle intersections.
     */
    @Test
    public void testRayTriangleIntersections() {
        // TC01: Small triangle (1 intersection point)
        Triangle triangle1 = new Triangle(new Point(0, 1, -2), new Point(1, -1, -2), new Point(-1, -1, -2));
        assertEquals(1, countIntersections(camera, triangle1), "Wrong number of intersections for small triangle");

        // TC02: Large triangle (2 intersection points)
        Triangle triangle2 = new Triangle(new Point(0, 20, -2), new Point(1, -1, -2), new Point(-1, -1, -2));
        assertEquals(2, countIntersections(camera, triangle2), "Wrong number of intersections for large triangle");
    }
}
