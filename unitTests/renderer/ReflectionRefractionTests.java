package renderer;

import static java.awt.Color.*;

import geometries.Cylinder;
import geometries.Plane;
import org.junit.jupiter.api.Test;

import geometries.Sphere;
import geometries.Triangle;
import lighting.AmbientLight;
import lighting.SpotLight;
import primitives.*;
import scene.Scene;

/**
 * Tests for reflection and transparency functionality, test for partial
 * shadows
 * (with transparency)
 *
 * @author dzilb
 */
public class ReflectionRefractionTests {
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
     * Produce a picture of a sphere lighted by a spot light
     */
    @Test
    public void twoSpheres() {
        scene.geometries.add(
                new Sphere(50d, new Point(0, 0, -50)).setEmission(new Color(BLUE))
                        .setMaterial(new Material().setKD(0.4).setKS(0.3).setShininess(100).setKT(0.3)),
                new Sphere(25d, new Point(0, 0, -50)).setEmission(new Color(RED))
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(100)));
        scene.lights.add(
                new SpotLight(new Color(1000, 600, 0), new Point(-100, -100, 500), new Vector(-1, -1, -2))
                        .setKL(0.0004).setKQ(0.0000006));

        cameraBuilder.setLocation(new Point(0, 0, 1000)).setVPDistance(1000)
                .setVPSize(150, 150)
                .setImageWriter(new ImageWriter("refractionTwoSpheres", 500, 500))
                .build()
                .renderImage()
                .writeToImage();
    }

    /**
     * Produce a picture of a sphere lighted by a spot light
     */
    @Test
    public void twoSpheresOnMirrors() {
        scene.geometries.add(
                new Sphere(400d, new Point(-950, -900, -1000)).setEmission(new Color(0, 50, 100))
                        .setMaterial(new Material().setKD(0.25).setKS(0.25).setShininess(20)
                                .setKT(new Double3(0.5, 0, 0))),
                new Sphere(200d, new Point(-950, -900, -1000)).setEmission(new Color(100, 50, 20))
                        .setMaterial(new Material().setKD(0.25).setKS(0.25).setShininess(20)),
                new Triangle(new Point(1500, -1500, -1500), new Point(-1500, 1500, -1500),
                        new Point(670, 670, 3000))
                        .setEmission(new Color(20, 20, 20))
                        .setMaterial(new Material().setKR(1)),
                new Triangle(new Point(1500, -1500, -1500), new Point(-1500, 1500, -1500),
                        new Point(-1500, -1500, -2000))
                        .setEmission(new Color(20, 20, 20))
                        .setMaterial(new Material().setKR(new Double3(0.5, 0, 0.4))));
        scene.setAmbientLight(new AmbientLight(new Color(255, 255, 255), new Double3(0.1)));
        scene.lights.add(new SpotLight(new Color(1020, 400, 400), new Point(-750, -750, -150), new Vector(-1, -1, -4))
                .setKL(0.00001).setKQ(0.000005));

        cameraBuilder.setLocation(new Point(0, 0, 10000)).setVPDistance(10000)
                .setVPSize(2500, 2500)
                .setImageWriter(new ImageWriter("reflectionTwoSpheresMirrored", 500, 500))
                .build()
                .renderImage()
                .writeToImage();
    }

    /**
     * Produce a picture of a two triangles lighted by a spot light with a
     * partially
     * transparent Sphere producing partial shadow
     */
    @Test
    public void trianglesTransparentSphere() {
        scene.geometries.add(
                new Triangle(new Point(-150, -150, -115), new Point(150, -150, -135),
                        new Point(75, 75, -150))
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(60)),
                new Triangle(new Point(-150, -150, -115), new Point(-70, 70, -140), new Point(75, 75, -150))
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(60)),
                new Sphere(30d, new Point(60, 50, -50)).setEmission(new Color(BLUE))
                        .setMaterial(new Material().setKD(0.2).setKS(0.2).setShininess(30).setKT(0.6)));
        scene.setAmbientLight(new AmbientLight(new Color(WHITE), new Double3(0.15)));
        scene.lights.add(
                new SpotLight(new Color(700, 400, 400), new Point(60, 50, 0), new Vector(0, 0, -1))
                        .setKL(4E-5).setKQ(2E-7));

        cameraBuilder.setLocation(new Point(0, 0, 1000)).setVPDistance(1000)
                .setVPSize(200, 200)
                .setImageWriter(new ImageWriter("refractionShadow", 600, 600))
                .build()
                .renderImage()
                .writeToImage();
    }

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
                .setImageWriter(new ImageWriter("planeSphereTriangleScene", 500, 500))
                .setRayTracer(new SimpleRayTracer(scene))
                .build();

        // Render and write image
        camera.renderImage();
        camera.writeToImage();
    }
}