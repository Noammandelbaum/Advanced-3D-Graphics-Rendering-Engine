package bigScenes;

import lighting.*;
import primitives.*;
import geometries.*;
import renderer.*;
import scene.Scene;

public class SimpleHouseScene {
    public static void main(String[] args) {
        Scene scene = new Scene("Simple House Test");
        scene.setBackground(new Color(135, 206, 235)); // Bright sky

        // Green ground
        scene.geometries.add(new Plane(new Point(0, -5, 0), new Vector(0, 1, 0))
                .setEmission(new Color(50, 180, 50))
                .setMaterial(new Material().setKD(0.6)));

        // ✅ Create a single house
        createHouse(scene, 0, -10);

        // ✅ Add a single cloud
        createCloud(scene, -3, 8, -15, 2.5);

        // ✅ Add a single sunflower
        createSunflower(scene, 5, -9);

        // ✅ 3 balanced light sources
        scene.setAmbientLight(new AmbientLight(new Color(255, 250, 224), new Double3(0.1))); // Soft ambient light
        scene.lights.add(new SpotLight(new Color(255, 250, 224), new Point(10, 10, 5), new Vector(-1, -1, -1))
                .setKL(0.0005).setKQ(0.0001)); // Sunlight-like spot
        scene.lights.add(new PointLight(new Color(255, 240, 220), new Point(-5, 5, -5))
                .setKL(0.002).setKQ(0.0005)); // Additional side light

        // ✅ Define a focused camera
        Camera camera = Camera.getBuilder()
                .setLocation(new Point(0, 2, 5))
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0))
                .setVPDistance(10)
                .setVPSize(15, 15)
                .setImageWriter(new ImageWriter("bigScenes/SimpleHouseScene", 600, 600))
                .setRayTracer(new SimpleRayTracer(scene))
                .build();

        camera.generateRenderedImage();
        camera.writeToImage();
    }

    static void createCloud(Scene scene, double centerX, double centerY, double centerZ, double scale) {
        Material cloudMaterial = new Material().setKT(0.6).setKR(0.3);

        scene.geometries.add(new Sphere(scale * 2.0, new Point(centerX, centerY, centerZ))
                .setEmission(new Color(255, 255, 255))
                .setMaterial(cloudMaterial));
        scene.geometries.add(new Sphere(scale * 1.7, new Point(centerX + scale * 1.2, centerY - scale * 0.4, centerZ - scale * 0.5))
                .setEmission(new Color(255, 255, 255))
                .setMaterial(cloudMaterial));
    }

    static void createSunflower(Scene scene, double x, double z) {
        // Stem
        for (double y = -5; y <= -1; y += 0.2) {
            scene.geometries.add(new Sphere(0.1, new Point(x, y, z)).setEmission(new Color(34, 139, 34)));
        }

        // Sunflower head
        scene.geometries.add(new Sphere(0.3, new Point(x, 0, z)).setEmission(new Color(80, 50, 20)));

        // Petals
        for (double angle = 0; angle < 360; angle += 30) {
            double rad = Math.toRadians(angle);
            Point base1 = new Point(x + Math.cos(rad) * 0.4, Math.sin(rad) * 0.4, z);
            Point tip = new Point(x + Math.cos(rad) * 0.7, Math.sin(rad) * 0.7, z + 0.2);
            scene.geometries.add(new Triangle(base1, new Point(x, 0, z), tip)
                    .setEmission(new Color(255, 215, 0))
                    .setMaterial(new Material().setKD(0.5).setKS(0.2)));
        }
    }

    static void createHouse(Scene scene, double x, double z) {
        // Materials and colors
        Material wallMaterial = new Material().setKD(0.6).setKS(0.3).setShininess(30);
        Material roofMaterial = new Material().setKD(0.3).setKS(0.4).setShininess(100);
        Color wallColor = new Color(210, 180, 140);
        Color roofColor = new Color(178, 34, 34);
        Color doorColor = new Color(101, 67, 33);

        // House walls
        scene.geometries.add(
                new Triangle(new Point(x, -5, z), new Point(x + 4, -5, z), new Point(x, -2, z))
                        .setEmission(wallColor).setMaterial(wallMaterial),
                new Triangle(new Point(x + 4, -5, z), new Point(x + 4, -2, z), new Point(x, -2, z))
                        .setEmission(wallColor).setMaterial(wallMaterial),
                new Triangle(new Point(x, -5, z), new Point(x, -5, z - 3), new Point(x, -2, z))
                        .setEmission(wallColor).setMaterial(wallMaterial),
                new Triangle(new Point(x, -5, z - 3), new Point(x, -2, z - 3), new Point(x, -2, z))
                        .setEmission(wallColor).setMaterial(wallMaterial)
        );

        // ✅ Add a door
        scene.geometries.add(
                new Triangle(new Point(x + 1.2, -5, z - 3.01), new Point(x + 2.8, -5, z - 3.01), new Point(x + 1.2, -3, z - 3.01))
                        .setEmission(doorColor),
                new Triangle(new Point(x + 2.8, -5, z - 3.01), new Point(x + 2.8, -3, z - 3.01), new Point(x + 1.2, -3, z - 3.01))
                        .setEmission(doorColor)
        );

        // ✅ Roof with left and right sides
        Point roofTop = new Point(x + 2, 0, z - 1.5); // Roof center

        scene.geometries.add(
                new Triangle(new Point(x, -2, z), new Point(x + 4, -2, z), roofTop) // Front side
                        .setEmission(roofColor).setMaterial(roofMaterial),
                new Triangle(new Point(x, -2, z - 3), new Point(x + 4, -2, z - 3), roofTop) // Back side
                        .setEmission(roofColor).setMaterial(roofMaterial),
                new Triangle(new Point(x, -2, z), new Point(x, -2, z - 3), roofTop) // Left side
                        .setEmission(roofColor).setMaterial(roofMaterial),
                new Triangle(new Point(x + 4, -2, z), new Point(x + 4, -2, z - 3), roofTop) // Right side
                        .setEmission(roofColor).setMaterial(roofMaterial)
        );
    }
}
