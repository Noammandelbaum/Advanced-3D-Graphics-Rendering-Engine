package renderer;

import geometries.Plane;
import geometries.Sphere;
import geometries.Triangle;
import lighting.AmbientLight;
import lighting.SpotLight;
import org.junit.jupiter.api.Test;
import primitives.*;
import scene.Scene;

import static java.awt.Color.*;


public class AntiAliasingTests {
    /**
     * Scene for the tests
     */
    private final Scene scene = new Scene("Test scene");

    /**
     * Camera builder for the tests with triangles
     */
    private final Camera.Builder cameraBuilder = Camera.getBuilder()
            .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
            .setRayTracer(new SimpleRayTracer(scene));


    /**
     * Test for rendering a scene with a plane, a sphere on the plane, and a triangle partially covering the sphere.
     */
    @Test
    public void planeSphereTriangleTest() {
        scene.setBackground(new Color(173, 216, 230));

        // Adding geometries
        // Plane
        scene.geometries.add(new Plane(new Point(0, -50, 0), new Vector(0, 1, 0))
                .setEmission(new Color(GRAY))
                .setMaterial(new Material().setKD(0.5).setKS(0.3).setKT(0.4).setShininess(20)));

        // Spheres on the plane
        scene.geometries.add(new Sphere(60, new Point(0, -20, -100))
                .setEmission(new Color(BLUE))
                .setMaterial(new Material().setKD(0.3).setKS(0.5).setKR(0.17).setKT(0.4).setShininess(100)));

        scene.geometries.add(new Sphere(20, new Point(30, -10, -130))
                .setEmission(new Color(255, 150, 45))
                .setMaterial(new Material().setKD(0.1).setKS(0.1).setKR(0.1).setKT(0.1).setShininess(10)));

        // Triangle in front of the sphere to partially cover it
        scene.geometries.add(new Triangle(new Point(-30, -50, -80), new Point(30, -50, -80), new Point(0, 30, -80))
                .setEmission(new Color(150, 75, 0))
                .setMaterial(new Material().setKD(0.1).setKS(0.5).setKR(0.4).setKT(0.2).setShininess(50)));

        // Adding lights
        scene.lights.add(new SpotLight(new Color(500, 300, 300), new Point(50, 50, 50), new Vector(-1, -1, -2))
                .setKL(0.0001).setKQ(0.000005));

        // Camera setup and render
        Camera camera = Camera.getBuilder()
                .setLocation(new Point(0, 0, 200))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVPDistance(200)
                .setVPSize(200, 200)
                .setImageWriter(new ImageWriter("planeSphereTriangleScene", 1000, 1000))
                .setRayTracer(new SimpleRayTracer(scene))
                .build();

        // Render and write image
        camera.renderImage();
        camera.writeToImage();
    }

    /**
     * Test for rendering a scene with a plane, a sphere on the plane, and a triangle partially covering the sphere.
     */
    @Test
    public void planeSphereTriangleAntiAliasingTest() {
        scene.setBackground(new Color(173, 216, 230));

        // Adding geometries
        // Plane
        scene.geometries.add(new Plane(new Point(0, -50, 0), new Vector(0, 1, 0))
                .setEmission(new Color(GRAY))
                .setMaterial(new Material().setKD(0.5).setKS(0.3).setKT(0.4).setShininess(20)));

        // Spheres on the plane
        scene.geometries.add(new Sphere(60, new Point(0, -20, -100))
                .setEmission(new Color(BLUE))
                .setMaterial(new Material().setKD(0.3).setKS(0.5).setKR(0.17).setKT(0.4).setShininess(100)));

        scene.geometries.add(new Sphere(20, new Point(30, -10, -130))
                .setEmission(new Color(255, 150, 45))
                .setMaterial(new Material().setKD(0.1).setKS(0.1).setKR(0.1).setKT(0.1).setShininess(10)));

        // Triangle in front of the sphere to partially cover it
        scene.geometries.add(new Triangle(new Point(-30, -50, -80), new Point(30, -50, -80), new Point(0, 30, -80))
                .setEmission(new Color(150, 75, 0))
                .setMaterial(new Material().setKD(0.1).setKS(0.5).setKR(0.4).setKT(0.2).setShininess(50)));

        // Adding lights
        scene.lights.add(new SpotLight(new Color(500, 300, 300), new Point(50, 50, 50), new Vector(-1, -1, -2))
                .setKL(0.0001).setKQ(0.000005));

        // Camera setup and render
        Camera camera = Camera.getBuilder()
                .setLocation(new Point(0, 0, 200))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVPDistance(200)
                .setVPSize(200, 200)
                .setImageWriter(new ImageWriter("planeSphereTriangleAntiAliasingRingRays", 1000, 1000))
                .setRayTracer(new SimpleRayTracer(scene))
                .enableAntiAliasing(30)
                .build();

        // Render and write image
        camera.renderImage();
        camera.writeToImage();
    }

    @Test
    public void antiAliasingComparisonTest() {
        scene.setBackground(new Color(173, 216, 230));

        // Adding geometries
        // Plane
        scene.geometries.add(new Plane(new Point(0, -50, 0), new Vector(0, 1, 0))
                .setEmission(new Color(GRAY))
                .setMaterial(new Material().setKD(0.5).setKS(0.3).setShininess(20)));

        // Sphere
        scene.geometries.add(new Sphere(40, new Point(-30, 10, -100))
                .setEmission(new Color(BLUE))
                .setMaterial(new Material().setKD(0.2).setKS(0.5).setShininess(30)));

        // Triangle
        scene.geometries.add(new Triangle(new Point(30, -50, -100), new Point(60, -50, -100), new Point(45, 20, -100))
                .setEmission(new Color(RED))
                .setMaterial(new Material().setKD(0.3).setKS(0.5).setShininess(20)));

        // Adding lights
        scene.lights.add(new SpotLight(new Color(500, 300, 300), new Point(50, 50, 50), new Vector(-1, -1, -2))
                .setKL(0.0001).setKQ(0.000005));

        // Camera setup for no anti-aliasing
        Camera noAntiAliasingCamera = Camera.getBuilder()
                .setLocation(new Point(0, 0, 200))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVPDistance(200)
                .setVPSize(200, 200)
                .setImageWriter(new ImageWriter("antiAliasingNO", 1000, 1000))
                .setRayTracer(new SimpleRayTracer(scene))
                .build();

        // Camera setup for anti-aliasing with grid pattern
        Camera gridAntiAliasingCamera = Camera.getBuilder()
                .setLocation(new Point(0, 0, 200))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVPDistance(200)
                .setVPSize(200, 200)
                .setImageWriter(new ImageWriter("antiAliasingRing", 1000, 1000))
                .setRayTracer(new SimpleRayTracer(scene))
                .enableAntiAliasing(30) // Grid pattern
                .build();


        // Render images
        noAntiAliasingCamera.renderImage();
        noAntiAliasingCamera.writeToImage();

        gridAntiAliasingCamera.renderImage();
        gridAntiAliasingCamera.writeToImage();

    }

}
