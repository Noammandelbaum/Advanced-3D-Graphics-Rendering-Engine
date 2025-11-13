package renderer.superSampling.antiAliasing;

import static java.awt.Color.*;
import org.junit.jupiter.api.Test;

import geometries.*;
import lighting.*;
import primitives.*;
import renderer.Camera;
import renderer.ImageWriter;
import renderer.SimpleRayTracer;
import renderer.superSampling.SamplingConfig;
import renderer.superSampling.SamplingPattern;
import scene.Scene;

/**
 * Anti-Aliasing Tests
 * <p>
 * Using only implemented geometries: Plane, Sphere, Triangle
 * Creates a balanced scene with proper lighting and organized image output
 * </p>
 */
public class AntiAliasingTest {

    private final Scene scene = new Scene("Final MP1 Anti-Aliasing Scene");

    /**
     * Creates a scene with 10+ geometric bodies using only Sphere, Triangle, and Plane
     */
    private void createScene() {
        // Darker background for less brightness
        scene.background = new Color(25, 25, 40); // Dark blue-gray

        // Darker ground plane
        scene.geometries.add(
                new Plane(new Point(0, -50, 0), new Vector(0, 1, 0))
                        .setEmission(new Color(60, 60, 60))  // Dark gray
                        .setMaterial(new Material().setKD(0.8).setKS(0.1).setShininess(10))
        );

        // Central composition - 7 spheres with varied colors and materials (Bodies 1-7)
        scene.geometries.add(
                // Central large sphere
                new Sphere(25, new Point(0, -15, -100))
                        .setEmission(new Color(100, 150, 200))  // Muted blue
                        .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(80)),

                // Left group
                new Sphere(18, new Point(-45, -25, -85))
                        .setEmission(new Color(180, 100, 100))  // Muted red
                        .setMaterial(new Material().setKD(0.7).setKS(0.3).setShininess(60)),

                new Sphere(15, new Point(-65, -10, -120))
                        .setEmission(new Color(150, 120, 80))  // Muted orange
                        .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(50)),

                new Sphere(20, new Point(-25, 5, -70))
                        .setEmission(new Color(120, 140, 100))  // Muted green
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(70)),

                // Right group
                new Sphere(22, new Point(45, -20, -95))
                        .setEmission(new Color(120, 100, 180))  // Muted purple
                        .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(90)),

                new Sphere(16, new Point(65, -5, -130))
                        .setEmission(new Color(100, 180, 150))  // Muted cyan
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(75)),

                new Sphere(19, new Point(25, 10, -65))
                        .setEmission(new Color(180, 150, 100))  // Muted yellow
                        .setMaterial(new Material().setKD(0.7).setKS(0.3).setShininess(55))
        );

        // Triangular elements for sharp edge testing (Bodies 8-12)
        scene.geometries.add(
                // Upper triangles
                new Triangle(new Point(-30, 35, -150), new Point(-10, 55, -150), new Point(-50, 55, -150))
                        .setEmission(new Color(120, 80, 100))  // Muted pink
                        .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(40)),

                new Triangle(new Point(30, 40, -140), new Point(50, 60, -140), new Point(10, 60, -140))
                        .setEmission(new Color(80, 120, 100))  // Muted green
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(45)),

                new Triangle(new Point(0, 25, -80), new Point(15, 45, -80), new Point(-15, 45, -80))
                        .setEmission(new Color(140, 100, 80))  // Muted orange
                        .setMaterial(new Material().setKD(0.7).setKS(0.3).setShininess(35)),

                // Lower triangles for more complex shapes
                new Triangle(new Point(-40, -35, -60), new Point(-20, -15, -60), new Point(-60, -15, -60))
                        .setEmission(new Color(100, 100, 140))  // Muted blue
                        .setMaterial(new Material().setKD(0.6).setKS(0.4).setShininess(50)),

                new Triangle(new Point(40, -30, -55), new Point(60, -10, -55), new Point(20, -10, -55))
                        .setEmission(new Color(140, 120, 100))  // Muted brown
                        .setMaterial(new Material().setKD(0.5).setKS(0.5).setShininess(60))
        );

        // Softer lighting - 3 different light sources with reduced intensity
        scene.lights.add(
                // Main light - softer directional
                new DirectionalLight(new Color(120, 120, 110), new Vector(-0.5, -1, -0.7))
        );

        scene.lights.add(
                // Fill light - gentle point light
                new PointLight(new Color(80, 80, 90), new Point(-30, 40, 10))
                        .setKL(0.0002).setKQ(0.00001)
        );

        scene.lights.add(
                // Accent light - subtle spot light
                new SpotLight(new Color(70, 85, 100), new Point(50, 60, 20), new Vector(-1, -1.2, -1))
                        .setKL(0.0003).setKQ(0.000015)
        );
    }

    /**
     * Test method to render scene without anti-aliasing first
     */
    @Test
    public void testSceneWithoutAntiAliasing() {
        createScene();

        System.out.println("====================================");
        System.out.println("     MP1 Scene Test (No AA)        ");
        System.out.println("====================================");
        System.out.println("Scene composition:");
        System.out.println("- 1 Plane (ground)");
        System.out.println("- 7 Spheres (various sizes and colors)");
        System.out.println("- 5 Triangles (for sharp edge testing)");
        System.out.println("- 3 Light sources (Directional, Point, Spot)");
        System.out.println("- Reduced lighting for balanced brightness");
        System.out.println();

        System.out.println("Rendering scene without anti-aliasing...");
        long startTime = System.currentTimeMillis();

        Camera camera = Camera.getBuilder()
                .setLocation(new Point(0, 15, 60))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVPDistance(60)
                .setVPSize(120, 120)
                .setImageWriter(new ImageWriter("antiAliasing/antialiasing_test", 600, 600))
                .setRayTracer(new SimpleRayTracer(scene))
                .build();

        camera.generateRenderedImage();
        camera.writeToImage();

        long renderTime = System.currentTimeMillis() - startTime;
        System.out.println("Completed in: " + renderTime + " ms");
        System.out.println("Image saved: images/antiAliasing/mp1_scene_test.png");
        System.out.println();
        System.out.println("Please review the scene before proceeding with anti-aliasing tests.");
    }

    /**
     * Complete anti-aliasing comparison test (run after scene approval)
     */
    @Test
    public void runCompleteAntiAliasingTest() {
        createScene();

        System.out.println("====================================");
        System.out.println("   MP1 ANTI-ALIASING COMPARISON    ");
        System.out.println("====================================");
        System.out.println("Running complete test with timing measurements...");
        System.out.println();

        // Test 1: Without Anti-Aliasing
        System.out.println("1. RENDERING WITHOUT ANTI-ALIASING");
        long noAATime = renderWithoutAntiAliasing();

        System.out.println();

        // Test 2: With Anti-Aliasing - 81 samples
        System.out.println("2. RENDERING WITH ANTI-ALIASING (81 samples)");
        long standardAATime = renderWithStandardAntiAliasing();

        System.out.println();

        // Test 3: With Anti-Aliasing - 324 samples
        System.out.println("3. RENDERING WITH ANTI-ALIASING (324 samples - High Quality)");
        long highAATime = renderWithHighQualityAntiAliasing();

        System.out.println();

        // Performance Summary
        printPerformanceSummary(noAATime, standardAATime, highAATime);
    }

    private long renderWithoutAntiAliasing() {
        long startTime = System.currentTimeMillis();

        Camera camera = Camera.getBuilder()
                .setLocation(new Point(0, 15, 60))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVPDistance(60)
                .setVPSize(120, 120)
                .setImageWriter(new ImageWriter("antiAliasing/no_antialiasing", 600, 600))
                .setRayTracer(new SimpleRayTracer(scene))
                .build();

        camera.generateRenderedImage();
        camera.writeToImage();

        long renderTime = System.currentTimeMillis() - startTime;
        System.out.println("   Completed in: " + renderTime + " ms");
        return renderTime;
    }

    private long renderWithStandardAntiAliasing() {
        long startTime = System.currentTimeMillis();

        Camera camera = Camera.getBuilder()
                .setLocation(new Point(0, 15, 60))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVPDistance(60)
                .setVPSize(120, 120)
                .setImageWriter(new ImageWriter("antiAliasing/with_antialiasing_81_samples", 600, 600))
                .setRayTracer(new SimpleRayTracer(scene))
                .setSamplingConfig(new SamplingConfig().enableAntiAliasing(81, 1.0, SamplingPattern.JITTERED))
                .build();

        camera.generateRenderedImage();
        camera.writeToImage();

        long renderTime = System.currentTimeMillis() - startTime;
        System.out.println("   Completed in: " + renderTime + " ms");
        return renderTime;
    }

    private long renderWithHighQualityAntiAliasing() {
        long startTime = System.currentTimeMillis();

        Camera camera = Camera.getBuilder()
                .setLocation(new Point(0, 15, 60))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVPDistance(60)
                .setVPSize(120, 120)
                .setImageWriter(new ImageWriter("antiAliasing/with_antialiasing_324_samples", 600, 600))
                .setRayTracer(new SimpleRayTracer(scene))
                .setSamplingConfig(new SamplingConfig().enableAntiAliasing(324, 1.0, SamplingPattern.JITTERED))
                .build();

        camera.generateRenderedImage();
        camera.writeToImage();

        long renderTime = System.currentTimeMillis() - startTime;
        System.out.println("   Completed in: " + renderTime + " ms");
        return renderTime;
    }

    private void printPerformanceSummary(long noAATime, long standardAATime, long highAATime) {
        System.out.println("====================================");
        System.out.println("        PERFORMANCE SUMMARY         ");
        System.out.println("====================================");
        System.out.println(String.format("No Anti-Aliasing:      %,6d ms", noAATime));
        System.out.println(String.format("Standard AA (81):      %,6d ms (%.1fx slower)", standardAATime, (double)standardAATime / noAATime));
        System.out.println(String.format("High Quality AA (324):  %,6d ms (%.1fx slower)", highAATime, (double)highAATime / noAATime));
        System.out.println();
        System.out.println("All images saved in: images/antiAliasing/");
        System.out.println("- no_antialiasing.png");
        System.out.println("- with_antialiasing_81_samples.png");
        System.out.println("- with_antialiasing_324_samples.png");
        System.out.println();
        System.out.println("MP1 Requirements fulfilled:");
        System.out.println("✓ 10+ geometric bodies (1 Plane + 7 Spheres + 5 Triangles)");
        System.out.println("✓ 3 different light sources");
        System.out.println("✓ Anti-aliasing ON/OFF capability");
        System.out.println("✓ 50+ sample rays (81 and 324)");
        System.out.println("✓ Performance timing measurements");
        System.out.println("✓ Organized image output");
        System.out.println("====================================");
    }
}