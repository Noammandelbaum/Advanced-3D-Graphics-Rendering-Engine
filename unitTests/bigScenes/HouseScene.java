package bigScenes;

import lighting.DirectionalLight;
import lighting.SpotLight;
import primitives.*;
import geometries.*;
import renderer.Camera;
import renderer.ImageWriter;
import renderer.SimpleRayTracer;
import scene.Scene;

public class HouseScene {
    public static void main(String[] args) {
        Scene scene = new Scene("Enhanced House Scene");
        scene.setBackground(new Color(135, 206, 235)); // Sky Blue

        // 🌿 Grass (Plane)
        scene.geometries.add(new Plane(new Point(0, -5, 0), new Vector(0, 1, 0))
                .setEmission(new Color(34, 139, 34))
                .setMaterial(new Material().setKD(0.6).setKS(0.2)));

        // 🏠 Base (Floor)
        scene.geometries.add(
                new Triangle(new Point(-4, -5, -4), new Point(4, -5, -4), new Point(-4, -5, 4))
                        .setEmission(new Color(139, 69, 19)),
                new Triangle(new Point(4, -5, -4), new Point(4, -5, 4), new Point(-4, -5, 4))
                        .setEmission(new Color(139, 69, 19))
        );

        // 🧱 Walls with Reflection
        Material wallMaterial = new Material().setKD(0.5).setKS(0.3).setShininess(30);
        Color wallColor = new Color(210, 180, 140);

        // Front wall with door and windows
        scene.geometries.add(
                new Triangle(new Point(-4, -5, -4), new Point(4, -5, -4), new Point(-4, 2, -4))
                        .setEmission(wallColor).setMaterial(wallMaterial),
                new Triangle(new Point(4, -5, -4), new Point(4, 2, -4), new Point(-4, 2, -4))
                        .setEmission(wallColor).setMaterial(wallMaterial)
        );

        // 🚪 Door
        scene.geometries.add(
                new Triangle(new Point(-0.8, -5, -4.01), new Point(0.8, -5, -4.01), new Point(-0.8, 0, -4.01))
                        .setEmission(new Color(101, 67, 33)),
                new Triangle(new Point(0.8, -5, -4.01), new Point(0.8, 0, -4.01), new Point(-0.8, 0, -4.01))
                        .setEmission(new Color(101, 67, 33))
        );

        // 🪟 Windows
//        Material windowMaterial = new Material().setKT(0.7).setKR(0.1);
//        scene.geometries.add(
//                new Triangle(new Point(-3, -1, -4.01), new Point(-2, -1, -4.01), new Point(-3, 1, -4.01))
//                        .setEmission(new Color(173, 216, 230)).setMaterial(windowMaterial),
//                new Triangle(new Point(-2, -1, -4.01), new Point(-2, 1, -4.01), new Point(-3, 1, -4.01))
//                        .setEmission(new Color(173, 216, 230)).setMaterial(windowMaterial),
//                new Triangle(new Point(2, -1, -4.01), new Point(3, -1, -4.01), new Point(2, 1, -4.01))
//                        .setEmission(new Color(173, 216, 230)).setMaterial(windowMaterial),
//                new Triangle(new Point(3, -1, -4.01), new Point(3, 1, -4.01), new Point(2, 1, -4.01))
//                        .setEmission(new Color(173, 216, 230)).setMaterial(windowMaterial)
//        );

        // 🏠 Side and back walls
        scene.geometries.add(
                new Triangle(new Point(-4, -5, 4), new Point(-4, 2, 4), new Point(-4, -5, -4))
                        .setEmission(wallColor).setMaterial(wallMaterial),
                new Triangle(new Point(-4, 2, -4), new Point(-4, 2, 4), new Point(-4, -5, -4))
                        .setEmission(wallColor).setMaterial(wallMaterial),

                new Triangle(new Point(4, -5, -4), new Point(4, 2, -4), new Point(4, -5, 4))
                        .setEmission(wallColor).setMaterial(wallMaterial),
                new Triangle(new Point(4, 2, 4), new Point(4, 2, -4), new Point(4, -5, 4))
                        .setEmission(wallColor).setMaterial(wallMaterial),

                new Triangle(new Point(-4, -5, 4), new Point(4, -5, 4), new Point(-4, 2, 4))
                        .setEmission(wallColor).setMaterial(wallMaterial),
                new Triangle(new Point(4, -5, 4), new Point(4, 2, 4), new Point(-4, 2, 4))
                        .setEmission(wallColor).setMaterial(wallMaterial)
        );

        // 🏠 Roof (reflective)
        Material roofMaterial = new Material().setKD(0.3).setKS(0.4).setShininess(100);
        scene.geometries.add(
                new Triangle(new Point(-4, 2, -4), new Point(4, 2, -4), new Point(0, 5, 0))
                        .setEmission(new Color(178, 34, 34)).setMaterial(roofMaterial),
                new Triangle(new Point(-4, 2, 4), new Point(4, 2, 4), new Point(0, 5, 0))
                        .setEmission(new Color(178, 34, 34)).setMaterial(roofMaterial)
        );

        createCloud(scene, -20, 18, -50, 3.5);
        createCloud(scene, 10, 17, -55, 3.0);
        createCloud(scene, 25, 16, -45, 3.2);


        createCloud(scene, -5, 12, -8, 2.5);
        createCloud(scene, 3, 14, -10, 2.0);
        createCloud(scene, 8, 13, -12, 1.8);


//        for (double x = -6; x <= -3; x += 1.5) {
//            createSunflower(scene, x, -5);
//        }
        for (double x = 3; x <= 6; x += 1.5) {
            createSunflower(scene, x, -5);
        }


//// חמניות מאחורי הבית
//        for (double x = -10; x <= 10; x += 2) {
//            createSunflower(scene, x, -15);
//        }




        // 💡 Interior and exterior lighting
//        scene.lights.add(new SpotLight(new Color(800, 500, 500), new Point(0, 3, -10), new Vector(0, -1, 2))
//                .setKL(0.0001).setKQ(0.000005));

        // תאורת שמש (מגיעה מלמעלה כמו השמש)
        scene.lights.add(new DirectionalLight(new Color(255, 250, 224), new Vector(-1, -1, -0.5)));


        // 📸 Camera
        Camera camera = Camera.getBuilder()
                .setLocation(new Point(10, 0, -20))
                .setDirection(new Vector(-8, 0, 20), new Vector(0, 1, 0))
                .setVPDistance(15)
                .setVPSize(15, 15)
                .setImageWriter(new ImageWriter("bigScenes/EnhancedHouseScene", 600, 600))
                .setRayTracer(new SimpleRayTracer(scene))
                .build();

        camera.generateRenderedImage();
        camera.writeToImage();
    }

    static void createCloud(Scene scene, double centerX, double centerY, double centerZ, double scale) {
        Material cloudMaterial = new Material().setKT(0.6).setKR(0.3); // שקיפות ורפלקציה קלה

        scene.geometries.add(new Sphere(scale * 2.0, new Point(centerX, centerY, centerZ))
                .setEmission(new Color(255, 255, 255)).setMaterial(cloudMaterial));
        scene.geometries.add(new Sphere(scale * 1.7, new Point(centerX + scale * 1.2, centerY - scale * 0.4, centerZ - scale * 0.5))
                .setEmission(new Color(255, 255, 255)).setMaterial(cloudMaterial));
        scene.geometries.add(new Sphere(scale * 1.5, new Point(centerX - scale * 1.3, centerY - scale * 0.5, centerZ + scale * 0.8))
                .setEmission(new Color(255, 255, 255)).setMaterial(cloudMaterial));
    }

    static void createSunflower(Scene scene, double x, double z) {
        // גבעול
        for (double y = -5; y <= -0.7; y += 0.3) {
            scene.geometries.add(new Sphere(0.15, new Point(x, y, z))
                    .setEmission(new Color(34, 139, 34)));
        }

        // עלים
        scene.geometries.add(new Triangle(new Point(x, -3, z), new Point(x - 0.7, -3.3, z + 0.3), new Point(x - 0.4, -4, z))
                .setEmission(new Color(34, 139, 34)));
        scene.geometries.add(new Triangle(new Point(x, -3.5, z), new Point(x + 0.7, -4, z + 0.3), new Point(x + 0.4, -4.5, z))
                .setEmission(new Color(34, 139, 34)));

        // מרכז חמנייה
        scene.geometries.add(new Sphere(0.5, new Point(x, 0, z))
                .setEmission(new Color(80, 50, 20)));

        // זרעים
        for (double angle = 0; angle < 360; angle += 30) {
            double rad = Math.toRadians(angle);
            scene.geometries.add(new Sphere(0.07, new Point(x + Math.cos(rad) * 0.4, Math.sin(rad) * 0.4, z + 0.4))
                    .setEmission(new Color(110, 70, 30)));
        }

        // עלי כותרת צהובים
        for (double angle = 0; angle < 360; angle += 30) {
            double rad = Math.toRadians(angle);
            Point base1 = new Point(x + Math.cos(rad) * 0.6, Math.sin(rad) * 0.6, z);
            Point base2 = new Point(x + Math.cos(rad + 0.2) * 0.6, Math.sin(rad + 0.2) * 0.6, z);
            Point tip = new Point(x + Math.cos(rad + 0.1) * 1.2, Math.sin(rad + 0.1) * 1.2, z + 0.3);
            scene.geometries.add(new Triangle(base1, base2, tip)
                    .setEmission(new Color(255, 215, 0))
                    .setMaterial(new Material().setKD(0.5).setKS(0.2)));
        }
    }


}
