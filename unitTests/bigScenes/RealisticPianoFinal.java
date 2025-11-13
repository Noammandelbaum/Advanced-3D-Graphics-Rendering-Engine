package bigScenes;

import lighting.*;
import primitives.*;
import geometries.*;
import renderer.*;
import renderer.superSampling.SamplingConfig;
import renderer.superSampling.SamplingPattern;
import scene.Scene;

/**
 * Final realistic piano scene with proper keyboard housing
 */
public class RealisticPianoFinal {
    public static void main(String[] args) {
        Scene scene = new Scene("Realistic Piano Final");
        scene.setBackground(new Color(35, 35, 40));
        
        // Floor
        scene.geometries.add(new Plane(new Point(0, -3, 0), new Vector(0, 1, 0))
                .setEmission(new Color(70, 50, 35))
                .setMaterial(new Material().setKD(0.8).setKS(0.1).setShininess(15)));

        // Wall
        scene.geometries.add(new Plane(new Point(0, 0, -6), new Vector(0, 0, 1))
                .setEmission(new Color(50, 50, 55))
                .setMaterial(new Material().setKD(0.9).setKS(0.05)));

        // Create UPRIGHT piano with proper keyboard housing
        createRealisticUprightPiano(scene, 0, 0);
        
        // Create proper piano bench - closer to piano
        createProperPianoBench(scene, 0, 2.4);

        // Good lighting
        scene.setAmbientLight(new AmbientLight(new Color(20, 20, 25), new Double3(0.3)));
        scene.lights.add(new DirectionalLight(new Color(150, 140, 120), new Vector(0.3, -1, -0.4)));
        scene.lights.add(new PointLight(new Color(100, 90, 80), new Point(-4, 4, 5))
                .setKL(0.0008).setKQ(0.00008));

        // Camera positioned to show both piano and bench
        Camera camera = Camera.getBuilder()
                .setLocation(new Point(-6, 2, 8))
                .setDirection(new Vector(1, 0, -1), new Vector(0, 1, 0))
                .setVPDistance(10)
                .setVPSize(14, 14)
                .setImageWriter(new ImageWriter("bigScenes/RealisticPianoFinal", 800, 800))
                .setRayTracer(new SimpleRayTracer(scene))
                .build();

        System.out.println("Rendering piano without anti-aliasing...");
        long startTime = System.currentTimeMillis();

        camera.generateRenderedImage();
        camera.writeToImage();

        long noAATime = System.currentTimeMillis() - startTime;
        System.out.println("Time without AA: " + noAATime + " ms");

        // Now render with anti-aliasing
        System.out.println("Rendering piano with anti-aliasing (30 samples)...");
        startTime = System.currentTimeMillis();

        Camera cameraAA = Camera.getBuilder()
                .setLocation(new Point(-6, 2, 8))
                .setDirection(new Vector(1, 0, -1), new Vector(0, 1, 0))
                .setVPDistance(10)
                .setVPSize(14, 14)
                .setImageWriter(new ImageWriter("bigScenes/RealisticPianoFinal_WithAA", 800, 800))
                .setRayTracer(new SimpleRayTracer(scene))
                .setSamplingConfig(new SamplingConfig().enableAntiAliasing(30, 1.0, SamplingPattern.JITTERED))
                .build();

        cameraAA.generateRenderedImage();
        cameraAA.writeToImage();

        long withAATime = System.currentTimeMillis() - startTime;
        System.out.println("Time with AA: " + withAATime + " ms");
        System.out.println("Performance impact: " + String.format("%.1fx", (double)withAATime / noAATime) + " slower");
        System.out.println("Images saved: RealisticPianoFinal.png and RealisticPianoFinal_WithAA.png");
    }

    static void createRealisticUprightPiano(Scene scene, double centerX, double centerZ) {
        Material pianoMatte = new Material()
                .setKD(0.6)
                .setKS(0.4)
                .setShininess(60)
                .setKR(0.1);
        
        Material whiteMaterial = new Material()
                .setKD(0.8)
                .setKS(0.2)
                .setShininess(25);
        
        Material blackMaterial = new Material()
                .setKD(0.4)
                .setKS(0.6)
                .setShininess(100);

        Color pianoBody = new Color(60, 50, 70);
        Color pianoTop = new Color(70, 60, 80);
        
        double width = 5;
        double height = 4;
        double depth = 2;
        
        // Main piano body
        createTallPianoBody(scene, centerX, centerZ, width, height, depth, pianoBody, pianoTop, pianoMatte);
        
        // KEYBOARD HOUSING - the box that contains the keys
        createKeyboardHousing(scene, centerX, centerZ, width, pianoBody, pianoMatte);
        
        // Keys INSIDE the housing
        createHousedKeyboard(scene, centerX, centerZ, width, whiteMaterial, blackMaterial);
        
        // Piano legs
        createSturdyPianoLegs(scene, centerX, centerZ, pianoBody, pianoMatte);
    }

    static void createTallPianoBody(Scene scene, double centerX, double centerZ, double width, double height, double depth, Color bodyColor, Color topColor, Material material) {
        double baseY = -3;
        
        // FRONT PANEL
        scene.geometries.add(new Triangle(
            new Point(centerX - width/2, baseY, centerZ + depth/2),
            new Point(centerX + width/2, baseY, centerZ + depth/2),
            new Point(centerX - width/2, baseY + height, centerZ + depth/2))
            .setEmission(bodyColor).setMaterial(material));
        scene.geometries.add(new Triangle(
            new Point(centerX + width/2, baseY, centerZ + depth/2),
            new Point(centerX + width/2, baseY + height, centerZ + depth/2),
            new Point(centerX - width/2, baseY + height, centerZ + depth/2))
            .setEmission(bodyColor).setMaterial(material));

        // BACK PANEL
        scene.geometries.add(new Triangle(
            new Point(centerX - width/2, baseY, centerZ - depth/2),
            new Point(centerX + width/2, baseY, centerZ - depth/2),
            new Point(centerX - width/2, baseY + height, centerZ - depth/2))
            .setEmission(bodyColor).setMaterial(material));
        scene.geometries.add(new Triangle(
            new Point(centerX + width/2, baseY, centerZ - depth/2),
            new Point(centerX + width/2, baseY + height, centerZ - depth/2),
            new Point(centerX - width/2, baseY + height, centerZ - depth/2))
            .setEmission(bodyColor).setMaterial(material));

        // LEFT SIDE
        scene.geometries.add(new Triangle(
            new Point(centerX - width/2, baseY, centerZ - depth/2),
            new Point(centerX - width/2, baseY, centerZ + depth/2),
            new Point(centerX - width/2, baseY + height, centerZ - depth/2))
            .setEmission(bodyColor).setMaterial(material));
        scene.geometries.add(new Triangle(
            new Point(centerX - width/2, baseY, centerZ + depth/2),
            new Point(centerX - width/2, baseY + height, centerZ + depth/2),
            new Point(centerX - width/2, baseY + height, centerZ - depth/2))
            .setEmission(bodyColor).setMaterial(material));

        // RIGHT SIDE
        scene.geometries.add(new Triangle(
            new Point(centerX + width/2, baseY, centerZ - depth/2),
            new Point(centerX + width/2, baseY, centerZ + depth/2),
            new Point(centerX + width/2, baseY + height, centerZ - depth/2))
            .setEmission(bodyColor).setMaterial(material));
        scene.geometries.add(new Triangle(
            new Point(centerX + width/2, baseY, centerZ + depth/2),
            new Point(centerX + width/2, baseY + height, centerZ + depth/2),
            new Point(centerX + width/2, baseY + height, centerZ - depth/2))
            .setEmission(bodyColor).setMaterial(material));

        // TOP
        scene.geometries.add(new Triangle(
            new Point(centerX - width/2, baseY + height, centerZ - depth/2),
            new Point(centerX + width/2, baseY + height, centerZ - depth/2),
            new Point(centerX - width/2, baseY + height, centerZ + depth/2))
            .setEmission(topColor).setMaterial(material));
        scene.geometries.add(new Triangle(
            new Point(centerX + width/2, baseY + height, centerZ - depth/2),
            new Point(centerX + width/2, baseY + height, centerZ + depth/2),
            new Point(centerX - width/2, baseY + height, centerZ + depth/2))
            .setEmission(topColor).setMaterial(material));
    }

    static void createKeyboardHousing(Scene scene, double centerX, double centerZ, double pianoWidth, Color housingColor, Material material) {
        // This creates the BOX that surrounds the keys like in a real piano
        double housingWidth = pianoWidth;          // FULL piano width - extends beyond keys
        double housingDepth = 1.4;                 // Deep enough for keys  
        double housingHeight = 0.4;                // Height of housing walls
        double housingBaseY = -1.0;                // RAISED: keyboard at 1/3 height of piano
        
        double housingZ = centerZ + 1.0 + housingDepth/2;  // Position in front of piano
        
        // HOUSING BOTTOM - the keys will sit ON this
        scene.geometries.add(new Triangle(
            new Point(centerX - housingWidth/2, housingBaseY, housingZ - housingDepth/2),
            new Point(centerX + housingWidth/2, housingBaseY, housingZ - housingDepth/2),
            new Point(centerX - housingWidth/2, housingBaseY, housingZ + housingDepth/2))
            .setEmission(new Color(40, 35, 45)).setMaterial(material));
        scene.geometries.add(new Triangle(
            new Point(centerX + housingWidth/2, housingBaseY, housingZ - housingDepth/2),
            new Point(centerX + housingWidth/2, housingBaseY, housingZ + housingDepth/2),
            new Point(centerX - housingWidth/2, housingBaseY, housingZ + housingDepth/2))
            .setEmission(new Color(40, 35, 45)).setMaterial(material));
        
        // HOUSING BACK WALL - connects to piano
        scene.geometries.add(new Triangle(
            new Point(centerX - housingWidth/2, housingBaseY, housingZ - housingDepth/2),
            new Point(centerX + housingWidth/2, housingBaseY, housingZ - housingDepth/2),
            new Point(centerX - housingWidth/2, housingBaseY + housingHeight, housingZ - housingDepth/2))
            .setEmission(housingColor).setMaterial(material));
        scene.geometries.add(new Triangle(
            new Point(centerX + housingWidth/2, housingBaseY, housingZ - housingDepth/2),
            new Point(centerX + housingWidth/2, housingBaseY + housingHeight, housingZ - housingDepth/2),
            new Point(centerX - housingWidth/2, housingBaseY + housingHeight, housingZ - housingDepth/2))
            .setEmission(housingColor).setMaterial(material));
        
        // HOUSING FRONT WALL - lower so we can see keys
        double frontWallHeight = housingHeight * 0.3;
        scene.geometries.add(new Triangle(
            new Point(centerX - housingWidth/2, housingBaseY, housingZ + housingDepth/2),
            new Point(centerX + housingWidth/2, housingBaseY, housingZ + housingDepth/2),
            new Point(centerX - housingWidth/2, housingBaseY + frontWallHeight, housingZ + housingDepth/2))
            .setEmission(housingColor).setMaterial(material));
        scene.geometries.add(new Triangle(
            new Point(centerX + housingWidth/2, housingBaseY, housingZ + housingDepth/2),
            new Point(centerX + housingWidth/2, housingBaseY + frontWallHeight, housingZ + housingDepth/2),
            new Point(centerX - housingWidth/2, housingBaseY + frontWallHeight, housingZ + housingDepth/2))
            .setEmission(housingColor).setMaterial(material));
        
        // HOUSING LEFT SIDE
        scene.geometries.add(new Triangle(
            new Point(centerX - housingWidth/2, housingBaseY, housingZ - housingDepth/2),
            new Point(centerX - housingWidth/2, housingBaseY, housingZ + housingDepth/2),
            new Point(centerX - housingWidth/2, housingBaseY + housingHeight, housingZ - housingDepth/2))
            .setEmission(housingColor).setMaterial(material));
        scene.geometries.add(new Triangle(
            new Point(centerX - housingWidth/2, housingBaseY, housingZ + housingDepth/2),
            new Point(centerX - housingWidth/2, housingBaseY + frontWallHeight, housingZ + housingDepth/2),
            new Point(centerX - housingWidth/2, housingBaseY + housingHeight, housingZ - housingDepth/2))
            .setEmission(housingColor).setMaterial(material));
        
        // HOUSING RIGHT SIDE
        scene.geometries.add(new Triangle(
            new Point(centerX + housingWidth/2, housingBaseY, housingZ - housingDepth/2),
            new Point(centerX + housingWidth/2, housingBaseY, housingZ + housingDepth/2),
            new Point(centerX + housingWidth/2, housingBaseY + housingHeight, housingZ - housingDepth/2))
            .setEmission(housingColor).setMaterial(material));
        scene.geometries.add(new Triangle(
            new Point(centerX + housingWidth/2, housingBaseY, housingZ + housingDepth/2),
            new Point(centerX + housingWidth/2, housingBaseY + frontWallHeight, housingZ + housingDepth/2),
            new Point(centerX + housingWidth/2, housingBaseY + housingHeight, housingZ - housingDepth/2))
            .setEmission(housingColor).setMaterial(material));
    }

    static void createHousedKeyboard(Scene scene, double centerX, double centerZ, double pianoWidth, Material whiteMaterial, Material blackMaterial) {
        double keyWidth = 0.18;
        double whiteKeyLength = 1.1;
        double blackKeyLength = 0.7;
        double keyboardHeight = -0.95;  // RAISED: Keys sit ON the raised housing bottom
        
        double keyboardZ = centerZ + 1.0 + whiteKeyLength/2;
        
        // White keys - sitting IN the housing
        for (int i = -12; i <= 12; i++) {
            double keyX = centerX + i * keyWidth;
            
            scene.geometries.add(new Triangle(
                new Point(keyX, keyboardHeight, keyboardZ - whiteKeyLength/2),
                new Point(keyX + keyWidth * 0.95, keyboardHeight, keyboardZ - whiteKeyLength/2),
                new Point(keyX, keyboardHeight, keyboardZ + whiteKeyLength/2))
                .setEmission(new Color(240, 240, 230)).setMaterial(whiteMaterial));
            scene.geometries.add(new Triangle(
                new Point(keyX + keyWidth * 0.95, keyboardHeight, keyboardZ - whiteKeyLength/2),
                new Point(keyX + keyWidth * 0.95, keyboardHeight, keyboardZ + whiteKeyLength/2),
                new Point(keyX, keyboardHeight, keyboardZ + whiteKeyLength/2))
                .setEmission(new Color(240, 240, 230)).setMaterial(whiteMaterial));
        }
        
        // Black keys - also in the housing, slightly higher
        int[] blackKeyPattern = {-11, -10, -8, -7, -5, -4, -3, -1, 0, 2, 3, 5, 6, 8, 9, 11};
        for (int offset : blackKeyPattern) {
            if (offset >= -11 && offset <= 11) {
                double keyX = centerX + offset * keyWidth + keyWidth/2;
                
                scene.geometries.add(new Triangle(
                    new Point(keyX, keyboardHeight + 0.05, keyboardZ - blackKeyLength/2),
                    new Point(keyX + keyWidth * 0.6, keyboardHeight + 0.05, keyboardZ - blackKeyLength/2),
                    new Point(keyX, keyboardHeight + 0.05, keyboardZ + blackKeyLength/2))
                    .setEmission(new Color(25, 25, 25)).setMaterial(blackMaterial));
                scene.geometries.add(new Triangle(
                    new Point(keyX + keyWidth * 0.6, keyboardHeight + 0.05, keyboardZ - blackKeyLength/2),
                    new Point(keyX + keyWidth * 0.6, keyboardHeight + 0.05, keyboardZ + blackKeyLength/2),
                    new Point(keyX, keyboardHeight + 0.05, keyboardZ + blackKeyLength/2))
                    .setEmission(new Color(25, 25, 25)).setMaterial(blackMaterial));
            }
        }
    }

    static void createSturdyPianoLegs(Scene scene, double centerX, double centerZ, Color legColor, Material legMaterial) {
        double[][] legPositions = {
            {centerX - 2, centerZ + 0.8},
            {centerX + 2, centerZ + 0.8}
        };
        
        for (double[] pos : legPositions) {
            for (double y = -3; y <= -1.5; y += 0.06) {
                scene.geometries.add(new Sphere(0.08, new Point(pos[0], y, pos[1]))
                        .setEmission(legColor).setMaterial(legMaterial));
            }
        }
    }

    static void createProperPianoBench(Scene scene, double centerX, double centerZ) {
        Material benchMaterial = new Material()
                .setKD(0.7)
                .setKS(0.3)
                .setShininess(50);
        
        // Simple bench colors without backrest
        Color benchTop = new Color(180, 120, 80);
        Color benchSides = new Color(160, 100, 60);
        Color benchLegs = new Color(140, 90, 50);
        
        double benchWidth = 3.0;
        double benchDepth = 1.2;
        double benchHeight = 0.25;
        double benchY = -1.5;   // RAISED: bench height adjusted for raised keyboard
        
        // BENCH TOP
        scene.geometries.add(new Triangle(
            new Point(centerX - benchWidth/2, benchY, centerZ), 
            new Point(centerX + benchWidth/2, benchY, centerZ), 
            new Point(centerX - benchWidth/2, benchY, centerZ + benchDepth))
            .setEmission(benchTop).setMaterial(benchMaterial));
        scene.geometries.add(new Triangle(
            new Point(centerX + benchWidth/2, benchY, centerZ), 
            new Point(centerX + benchWidth/2, benchY, centerZ + benchDepth), 
            new Point(centerX - benchWidth/2, benchY, centerZ + benchDepth))
            .setEmission(benchTop).setMaterial(benchMaterial));
        
        // BENCH SIDES
        scene.geometries.add(new Triangle(
            new Point(centerX - benchWidth/2, benchY, centerZ), 
            new Point(centerX - benchWidth/2, benchY, centerZ + benchDepth), 
            new Point(centerX - benchWidth/2, benchY - benchHeight, centerZ))
            .setEmission(benchSides).setMaterial(benchMaterial));
        scene.geometries.add(new Triangle(
            new Point(centerX - benchWidth/2, benchY, centerZ + benchDepth), 
            new Point(centerX - benchWidth/2, benchY - benchHeight, centerZ + benchDepth), 
            new Point(centerX - benchWidth/2, benchY - benchHeight, centerZ))
            .setEmission(benchSides).setMaterial(benchMaterial));
        
        scene.geometries.add(new Triangle(
            new Point(centerX + benchWidth/2, benchY, centerZ), 
            new Point(centerX + benchWidth/2, benchY, centerZ + benchDepth), 
            new Point(centerX + benchWidth/2, benchY - benchHeight, centerZ))
            .setEmission(benchSides).setMaterial(benchMaterial));
        scene.geometries.add(new Triangle(
            new Point(centerX + benchWidth/2, benchY, centerZ + benchDepth), 
            new Point(centerX + benchWidth/2, benchY - benchHeight, centerZ + benchDepth), 
            new Point(centerX + benchWidth/2, benchY - benchHeight, centerZ))
            .setEmission(benchSides).setMaterial(benchMaterial));
        
        // BENCH FRONT
        scene.geometries.add(new Triangle(
            new Point(centerX - benchWidth/2, benchY, centerZ + benchDepth), 
            new Point(centerX + benchWidth/2, benchY, centerZ + benchDepth), 
            new Point(centerX - benchWidth/2, benchY - benchHeight, centerZ + benchDepth))
            .setEmission(benchSides).setMaterial(benchMaterial));
        scene.geometries.add(new Triangle(
            new Point(centerX + benchWidth/2, benchY, centerZ + benchDepth), 
            new Point(centerX + benchWidth/2, benchY - benchHeight, centerZ + benchDepth), 
            new Point(centerX - benchWidth/2, benchY - benchHeight, centerZ + benchDepth))
            .setEmission(benchSides).setMaterial(benchMaterial));
        
        // BENCH BACK
        scene.geometries.add(new Triangle(
            new Point(centerX - benchWidth/2, benchY, centerZ), 
            new Point(centerX + benchWidth/2, benchY, centerZ), 
            new Point(centerX - benchWidth/2, benchY - benchHeight, centerZ))
            .setEmission(benchSides).setMaterial(benchMaterial));
        scene.geometries.add(new Triangle(
            new Point(centerX + benchWidth/2, benchY, centerZ), 
            new Point(centerX + benchWidth/2, benchY - benchHeight, centerZ), 
            new Point(centerX - benchWidth/2, benchY - benchHeight, centerZ))
            .setEmission(benchSides).setMaterial(benchMaterial));
        
        // BENCH LEGS
        double[][] benchLegPos = {
            {centerX - benchWidth/2 + 0.5, centerZ + 0.3}, 
            {centerX + benchWidth/2 - 0.5, centerZ + 0.3}, 
            {centerX - benchWidth/2 + 0.5, centerZ + benchDepth - 0.3}, 
            {centerX + benchWidth/2 - 0.5, centerZ + benchDepth - 0.3}
        };
        
        for (double[] pos : benchLegPos) {
            for (double y = -3; y <= benchY - benchHeight; y += 0.02) {
                scene.geometries.add(new Sphere(0.1, new Point(pos[0], y, pos[1]))
                        .setEmission(benchLegs).setMaterial(benchMaterial));
            }
        }
    }
}