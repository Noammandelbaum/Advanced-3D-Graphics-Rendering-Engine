package lighting;

import static java.awt.Color.*;

import org.junit.jupiter.api.Test;

import geometries.*;
import primitives.*;
import renderer.Camera;
import renderer.ImageWriter;
import renderer.SimpleRayTracer;
import scene.Scene;

/**
 * Test rendering a basic image
 *
 * @author Dan
 */
public class LightsTests {
    /**
     * First scene for some of the tests
     */
    private final Scene scene1 = new Scene("Test scene");
    /**
     * Second scene for some of the tests
     */
    private final Scene scene2 = new Scene("Test scene")
            .setAmbientLight(new AmbientLight(new Color(WHITE), new Double3(0.15)));

    /**
     * First camera builder for some of the tests
     */
    private final Camera.Builder camera1 = Camera.getBuilder()
            .setRayTracer(new SimpleRayTracer(scene1))
            .setLocation(new Point(0, 0, 1000))
            .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
            .setVPSize(150, 150).setVPDistance(1000);
    /**
     * Second camera builder for some of the tests
     */
    private final Camera.Builder camera2 = Camera.getBuilder()
            .setRayTracer(new SimpleRayTracer(scene2))
            .setLocation(new Point(0, 0, 1000))
            .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
            .setVPSize(200, 200).setVPDistance(1000);

    /**
     * Shininess value for most of the geometries in the tests
     */
    private static final int SHININESS = 301;
    /**
     * Diffusion attenuation factor for some of the geometries in the tests
     */
    private static final double KD = 0.5;
    /**
     * Diffusion attenuation factor for some of the geometries in the tests
     */
    private static final Double3 KD3 = new Double3(0.2, 0.6, 0.4);

    /**
     * Specular attenuation factor for some of the geometries in the tests
     */
    private static final double KS = 0.5;
    /**
     * Specular attenuation factor for some of the geometries in the tests
     */
    private static final Double3 KS3 = new Double3(0.2, 0.4, 0.3);

    /**
     * Material for some of the geometries in the tests
     */
    private final Material material = new Material().setKD(KD3).setKS(KS3).setShininess(SHININESS);
    /**
     * Light color for tests with triangles
     */
    private final Color trianglesLightColor = new Color(800, 500, 250);
    /**
     * Light color for tests with sphere
     */
    private final Color sphereLightColor = new Color(800, 500, 0);
    /**
     * Color of the sphere
     */
    private final Color sphereColor = new Color(BLUE).reduce(2);

    /**
     * Center of the sphere
     */
    private final Point sphereCenter = new Point(0, 0, -50);
    /**
     * Radius of the sphere
     */
    private static final double SPHERE_RADIUS = 50d;

    /**
     * The triangles' vertices for the tests with triangles
     */
    private final Point[] vertices =
            {
                    // the shared left-bottom:
                    new Point(-110, -110, -150),
                    // the shared right-top:
                    new Point(95, 100, -150),
                    // the right-bottom
                    new Point(110, -110, -150),
                    // the left-top
                    new Point(-75, 78, 100)
            };
    /**
     * Position of the light in tests with sphere
     */
    private final Point sphereLightPosition = new Point(-50, -50, 25);
    /**
     * Light direction (directional and spot) in tests with sphere
     */
    private final Vector sphereLightDirection = new Vector(1, 1, -0.5);
    /**
     * Position of the light in tests with triangles
     */
    private final Point trianglesLightPosition = new Point(30, 10, -100);
    /**
     * Light direction (directional and spot) in tests with triangles
     */
    private final Vector trianglesLightDirection = new Vector(-2, -2, -2);

    /**
     * The sphere in appropriate tests
     */
    private final Geometry sphere = new Sphere(SPHERE_RADIUS, sphereCenter)
            .setEmission(sphereColor).setMaterial(new Material().setKD(KD).setKS(KS).setShininess(SHININESS));
    /**
     * The first triangle in appropriate tests
     */
    private final Geometry triangle1 = new Triangle(vertices[0], vertices[1], vertices[2])
            .setMaterial(material);
    /**
     * The first triangle in appropriate tests
     */
    private final Geometry triangle2 = new Triangle(vertices[0], vertices[1], vertices[3])
            .setMaterial(material);

    /**
     * Produce a picture of a sphere lighted by a directional light
     */
    @Test
    public void sphereDirectional() {
        scene1.geometries.add(sphere);
        scene1.lights.add(new DirectionalLight(sphereLightColor, sphereLightDirection));

        camera1.setImageWriter(new ImageWriter("Lights/lightSphereDirectional", 500, 500))
                .build()
                .generateRenderedImage()
                .writeToImage();
    }

    /**
     * Produce a picture of a sphere lighted by a point light
     */
    @Test
    public void spherePoint() {
        scene1.geometries.add(sphere);
        scene1.lights.add(new PointLight(sphereLightColor, sphereLightPosition)
                .setKL(0.001).setKQ(0.0002));

        camera1.setImageWriter(new ImageWriter("Lights/lightSpherePoint", 500, 500))
                .build()
                .generateRenderedImage()
                .writeToImage();
    }

    /**
     * Produce a picture of a sphere lighted by a spotlight
     */
    @Test
    public void sphereSpot() {
        scene1.geometries.add(sphere);
        scene1.lights.add(new SpotLight(sphereLightColor, sphereLightPosition, sphereLightDirection)
                .setKL(0.001).setKQ(0.0001));

        camera1.setImageWriter(new ImageWriter("Lights/lightSphereSpot", 500, 500))
                .build()
                .generateRenderedImage()
                .writeToImage();
    }

    /**
     * Produce a picture of two triangles lighted by a directional light
     */
    @Test
    public void trianglesDirectional() {
        scene2.geometries.add(triangle1, triangle2);
        scene2.lights.add(new DirectionalLight(trianglesLightColor, trianglesLightDirection));

        camera2.setImageWriter(new ImageWriter("Lights/lightTrianglesDirectional", 500, 500)) //
                .build()
                .generateRenderedImage()
                .writeToImage();
    }

    /**
     * Produce a picture of two triangles lighted by a point light
     */
    @Test
    public void trianglesPoint() {
        scene2.geometries.add(triangle1, triangle2);
        scene2.lights.add(new PointLight(trianglesLightColor, trianglesLightPosition)
                .setKL(0.001).setKQ(0.0002));

        camera2.setImageWriter(new ImageWriter("Lights/lightTrianglesPoint", 500, 500)) //
                .build() //
                .generateRenderedImage() //
                .writeToImage(); //
    }

    /**
     * Produce a picture of two triangles lighted by a spotlight
     */
    @Test
    public void trianglesSpot() {
        scene2.geometries.add(triangle1, triangle2);
        scene2.lights.add(new SpotLight(trianglesLightColor, trianglesLightPosition, trianglesLightDirection)
                .setKL(0.001).setKQ(0.0001));

        camera2.setImageWriter(new ImageWriter("Lights/lightTrianglesSpot", 500, 500))
                .build()
                .generateRenderedImage()
                .writeToImage();
    }

    /** Produce a picture of a sphere with a rainbow effect using multiple types of light sources */
    @Test
    public void sphereRainbowEffect() {
        // Define colors for the lights (colors of the rainbow)
        Color red = new Color(255, 0, 0); // Red
        Color orange = new Color(255, 165, 0); // Orange
        Color yellow = new Color(255, 255, 0); // Yellow
        Color green = new Color(0, 255, 0); // Green
        Color blue = new Color(0, 0, 255); // Blue
        Color indigo = new Color(75, 0, 130); // Indigo
        Color violet = new Color(238, 130, 238); // Violet

        // Add the sphere
        Geometry sphere = new Sphere(50, new Point(0, 0, 0))
                .setEmission(new Color(25, 25, 112)) // Dark blue base color
                .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(30));

        scene1.geometries.add(sphere);

        // Add multiple light sources for the rainbow effect
        scene1.lights.add(new SpotLight(red, new Point(60, 60, 100), new Vector(-1, -1, -2))
                .setKL(0.0001).setKQ(0.00001)); // Red spotlight
        scene1.lights.add(new SpotLight(orange, new Point(60, -60, 100), new Vector(-1, 1, -2))
                .setKL(0.0001).setKQ(0.00001)); // Orange spotlight
        scene1.lights.add(new SpotLight(yellow, new Point(-60, 60, 100), new Vector(1, -1, -2))
                .setKL(0.0001).setKQ(0.00001)); // Yellow spotlight
        scene1.lights.add(new SpotLight(green, new Point(-60, -60, 100), new Vector(1, 1, -2))
                .setKL(0.0001).setKQ(0.00001)); // Green spotlight
        scene1.lights.add(new PointLight(blue, new Point(0, 60, 50))
                .setKL(0.0001).setKQ(0.00001)); // Blue point light
        scene1.lights.add(new PointLight(indigo, new Point(60, 0, 50))
                .setKL(0.0001).setKQ(0.00001)); // Indigo point light
        scene1.lights.add(new DirectionalLight(violet, new Vector(1, -1, -1))); // Violet directional light

        // Set up the camera and render the image
        camera1.setImageWriter(new ImageWriter("Lights/sphereRainbowEffect", 500, 500))
                .build()
                .generateRenderedImage()
                .writeToImage();
    }

    /** Produce a picture of a triangles using multiple types of light sources  */
    @Test
    public void trianglesThreeLights() {
        scene2.geometries.add(triangle1, triangle2);

        scene2.lights.add(new DirectionalLight(new Color(0, 20, 255),
                trianglesLightDirection));

        scene2.lights.add(new PointLight(new Color(255, 20, 0),
                new Point(40, 5, -100)));

        scene2.lights.add(new SpotLight(new Color(255, 255, 0),
                trianglesLightPosition,
                new Vector(1, 1, -0.5)));

        ImageWriter imageWriter = new ImageWriter("Lights/trianglesThreeLightBlueYellow", 500, 500);
        camera2.setImageWriter(imageWriter)
                .build()
                .generateRenderedImage()
                .writeToImage();
    }
}
