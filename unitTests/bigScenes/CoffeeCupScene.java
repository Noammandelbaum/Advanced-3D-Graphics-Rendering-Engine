package bigScenes;

import lighting.DirectionalLight;
import primitives.*;
import geometries.*;
import renderer.Camera;
import renderer.ImageWriter;
import renderer.SimpleRayTracer;
import scene.Scene;

public class CoffeeCupScene {
    public static void main(String[] args) {
        Scene scene = new Scene("Coffee Cup with Realistic Handle");
        scene.setBackground(new Color(250, 245, 240));

        double cupHeight = 2.5;
        double cupRadiusTop = 1.2;
        double cupRadiusBottom = 0.9;
        double yBase = -1.2;
        int segments = 40;

        // גוף הכוס
        for (int i = 0; i < segments; i++) {
            double angle1 = Math.toRadians(i * 360.0 / segments);
            double angle2 = Math.toRadians((i + 1) * 360.0 / segments);

            double x1Top = cupRadiusTop * Math.cos(angle1);
            double z1Top = cupRadiusTop * Math.sin(angle1);
            double x2Top = cupRadiusTop * Math.cos(angle2);
            double z2Top = cupRadiusTop * Math.sin(angle2);

            double x1Bottom = cupRadiusBottom * Math.cos(angle1);
            double z1Bottom = cupRadiusBottom * Math.sin(angle1);
            double x2Bottom = cupRadiusBottom * Math.cos(angle2);
            double z2Bottom = cupRadiusBottom * Math.sin(angle2);

            scene.geometries.add(
                    new Triangle(
                            new Point(x1Bottom, yBase, z1Bottom),
                            new Point(x2Bottom, yBase, z2Bottom),
                            new Point(x1Top, yBase + cupHeight, z1Top)
                    ).setEmission(new Color(255, 255, 255)),
                    new Triangle(
                            new Point(x1Top, yBase + cupHeight, z1Top),
                            new Point(x2Bottom, yBase, z2Bottom),
                            new Point(x2Top, yBase + cupHeight, z2Top)
                    ).setEmission(new Color(255, 255, 255))
            );
        }

        // קפה
        for (int i = 0; i < segments; i++) {
            double angle1 = Math.toRadians(i * 360.0 / segments);
            double angle2 = Math.toRadians((i + 1) * 360.0 / segments);
            double r = cupRadiusTop * 0.95;
            double x1 = r * Math.cos(angle1);
            double z1 = r * Math.sin(angle1);
            double x2 = r * Math.cos(angle2);
            double z2 = r * Math.sin(angle2);

            scene.geometries.add(
                    new Triangle(
                            new Point(0, yBase + cupHeight - 0.05, 0),
                            new Point(x1, yBase + cupHeight - 0.05, z1),
                            new Point(x2, yBase + cupHeight - 0.05, z2)
                    ).setEmission(new Color(50, 30, 20))
            );
        }



        // שולחן
        scene.geometries.add(new Plane(new Point(0, yBase - 0.01, 0), new Vector(0, 1, 0))
                .setEmission(new Color(180, 140, 100)));

        // תאורה
        scene.lights.add(new DirectionalLight(new Color(255, 230, 200), new Vector(-1, -1, 1)));

        Vector vTo = new Vector(0, -0.3, 1);
        Vector vRight = vTo.crossProduct(new Vector(0, 1, 0)).normalize();
        Vector vUp = vRight.crossProduct(vTo).normalize();

        Camera camera = Camera.getBuilder()
                .setLocation(new Point(0, 3, -8))
                .setDirection(vTo, vUp)
                .setVPDistance(20)
                .setVPSize(15, 15)
                .setImageWriter(new ImageWriter("bigScenes/CoffeeCupFixedHandle", 600, 600))
                .setRayTracer(new SimpleRayTracer(scene))
                .build();

        camera.generateRenderedImage();
        camera.writeToImage();
    }
}
