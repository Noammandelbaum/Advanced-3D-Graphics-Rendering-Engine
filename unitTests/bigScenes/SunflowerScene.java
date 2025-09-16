package bigScenes;

import lighting.AmbientLight;
import lighting.DirectionalLight;
import lighting.PointLight;
import lighting.SpotLight;
import primitives.*;
import geometries.*;
import renderer.Camera;
import renderer.ImageWriter;
import renderer.SimpleRayTracer;
import scene.Scene;

import java.util.Random;

public class SunflowerScene {
    public static void main(String[] args) {
        Scene scene = new Scene("Realistic Sunflower");
        scene.setBackground(new Color(135, 206, 235)); // שמיים בהירים

        // Brown soil plane
        // עדכון צבע הקרקע לירוק
        scene.geometries.add(new Plane(new Point(0, -5, 0), new Vector(0, 1, 0))
                .setEmission(new Color(50, 180, 50)) // צבע ירוק טבעי
                .setMaterial(new Material().setKD(0.6)));


// Small grass elements randomly scattered
        for (int i = -30; i <= 30; i += 2) {
            for (int j = -30; j <= 10; j += 2) {
                scene.geometries.add(new Sphere(0.1, new Point(i, -4.9, j))
                        .setEmission(new Color(34,139,34)));
            }
        }

// יצירת עננים מתוכננים ולא רנדומליים
//        createCloud(scene, -15, 14, -30, 2.5); // ענן גדול יחסית
//        createCloud(scene, 5, 13, -28, 2.0);
//        createCloud(scene, 20, 12, -35, 1.8);
//        createCloud(scene, -5, 15, -25, 2.3);
//        createCloud(scene, 12, 11, -20, 2.1);



        // יצירת בתים - מתחילים רחוק יותר כך שהשורה הראשונה לא מופיעה
        for (int z = -15; z >= -40; z -= 10) { // התחל מהשורה השנייה, לא הראשונה
            for (int x = -30; x <= 30; x += 10) {
                createHouse(scene, x, z);
            }
        }



        // Loop to create a field of sunflowers
        // Field of sunflowers
//        for (int x = -30; x <= 30; x += 3) {
//            for (int z = -25; z <= 0; z += 3) {
//                // Stem
//                for (double y = -5; y <= -0.7; y += 0.3) {
//                    scene.geometries.add(new Sphere(0.15, new Point(x, y, z))
//                            .setEmission(new Color(34,139,34)));
//                }
//
//                // Leaves
//                scene.geometries.add(new Triangle(new Point(x, -3, z), new Point(x-0.7, -3.3, z+0.3), new Point(x-0.4, -4, z))
//                        .setEmission(new Color(34,139,34)));
//                scene.geometries.add(new Triangle(new Point(x, -3.5, z), new Point(x+0.7, -4, z+0.3), new Point(x+0.4, -4.5, z))
//                        .setEmission(new Color(34,139,34)));
//
//                // Sunflower center
//                scene.geometries.add(new Sphere(0.5, new Point(x, 0, z))
//                        .setEmission(new Color(80, 50, 20)));
//
//                // Seeds
//                for (double angle = 0; angle < 360; angle += 30) {
//                    double rad = Math.toRadians(angle);
//                    scene.geometries.add(new Sphere(0.07, new Point(x + Math.cos(rad)*0.4, Math.sin(rad)*0.4, z + 0.4))
//                            .setEmission(new Color(110,70,30)));
//                }
//
//                // Petals (yellow)
//                for (double angle = 0; angle < 360; angle += 30) {
//                    double rad = Math.toRadians(angle);
//                    Point base1 = new Point(x + Math.cos(rad)*0.6, Math.sin(rad)*0.6, z);
//                    Point base2 = new Point(x + Math.cos(rad+0.2)*0.6, Math.sin(rad+0.2)*0.6, z);
//                    Point tip = new Point(x + Math.cos(rad+0.1)*1.2, Math.sin(rad+0.1)*1.2, z + 0.3);
//                    scene.geometries.add(new Triangle(base1, base2, tip)
//                            .setEmission(new Color(255,215,0))
//                            .setMaterial(new Material().setKD(0.5).setKS(0.2)));
//                }
//            }
//        }


        // תאורה דרמטית (מדמה שמש)

// ספוטלייט (האור הראשי, לא משתנה)
        scene.lights.add(new SpotLight(new Color(255, 250, 224), new Point(50, 50, 30), new Vector(-1, -1, -1))
                .setKL(0.001)
                .setKQ(0.0001));
//
//// נקודת אור רכה להוספת נוכחות אור כללית
//        scene.lights.add(new PointLight(new Color(255, 240, 220), new Point(-30, 20, 15))
//                .setKL(0.002)
//                .setKQ(0.0002));
//
//// כיוון נוסף של אור (מאוד חלש כדי רק לתת קצת יותר משחק בצללים)
//        scene.lights.add(new DirectionalLight(new Color(200, 200, 200), new Vector(1, -1, -0.5)));

        // מצלמה ממוקמת בצורה שתדגיש את הפרח
        Camera camera = Camera.getBuilder()
                .setLocation(new Point(0, 0, 5)) // בתוך השדה, בגובה נמוך יותר
                .setDirection(new Vector(0, 0, -1), new Vector(0, 1, 0)) // מבט ישר קדימה
                .setVPDistance(10)
                .setVPSize(30, 30)
                .setImageWriter(new ImageWriter("bigScenes/DeepSunflowerField", 1000, 1000))
                .setRayTracer(new SimpleRayTracer(scene))
                .build();

        camera.generateRenderedImage();
        camera.writeToImage();
    }

    // Function to create a cloud from multiple spheres
    static void createCloud(Scene scene, double centerX, double centerY, double centerZ, double scale) {
        Material cloudMaterial = new Material().setKT(0.6).setKR(0.3); // שקיפות ורפלקציה עדינה

        scene.geometries.add(new Sphere(scale * 2.0, new Point(centerX, centerY, centerZ))
                .setEmission(new Color(255,255,255)).setMaterial(cloudMaterial));
        scene.geometries.add(new Sphere(scale * 1.7, new Point(centerX + scale * 1.2, centerY - scale * 0.4, centerZ - scale * 0.5))
                .setEmission(new Color(255,255,255)).setMaterial(cloudMaterial));
        scene.geometries.add(new Sphere(scale * 1.5, new Point(centerX - scale * 1.3, centerY - scale * 0.5, centerZ + scale * 0.8))
                .setEmission(new Color(255,255,255)).setMaterial(cloudMaterial));
    }


    // Function to create a 3D house
    static void createHouse(Scene scene, double x, double z) {
        // חומרים וצבעים
        Material wallMaterial = new Material().setKD(0.6).setKS(0.3).setShininess(30);
        Color wallColor = new Color(210, 180, 140);
        Color roofColor = new Color(178, 34, 34);

        // קירות הבית
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

        // ✅ בדיקה יסודית של נקודת גג ממורכזת
        Point roofTop = new Point(x + 2, 0, z - 1.5);  // ממורכז מעל הבית

        // ✅ חיבור 4 הצדדים של הגג לנקודה אחת מרכזית
        scene.geometries.add(
                new Triangle(new Point(x, -2, z), new Point(x + 4, -2, z), roofTop) // צד קדמי
                        .setEmission(roofColor).setMaterial(new Material().setKD(0.3).setKS(0.4).setShininess(100)),
                new Triangle(new Point(x, -2, z - 3), new Point(x + 4, -2, z - 3), roofTop) // צד אחורי
                        .setEmission(roofColor).setMaterial(new Material().setKD(0.3).setKS(0.4).setShininess(100)),
                new Triangle(new Point(x, -2, z), new Point(x, -2, z - 3), roofTop) // צד שמאלי
                        .setEmission(roofColor).setMaterial(new Material().setKD(0.3).setKS(0.4).setShininess(100)),
                new Triangle(new Point(x + 4, -2, z), new Point(x + 4, -2, z - 3), roofTop) // צד ימני
                        .setEmission(roofColor).setMaterial(new Material().setKD(0.3).setKS(0.4).setShininess(100))
        );
    }




}
