package renderer;

import primitives.Point;
import primitives.Ray;
import primitives.Vector;

import java.util.ArrayList;
import java.util.List;

import static primitives.Util.alignZero;
import static primitives.Util.isZero;

/**
 * A generator class for producing rays for various sampling techniques.
 */
public class RayGenerator {

    //    private final CameraViewContext cameraViewContext;
    private Camera camera;

    public RayGenerator(Camera camera) {
        this.camera = camera;
    }

    /**
     * Generates a central ray from a given origin through a specified target.
     *
     * @param origin the starting point of the ray
     * @param target the target point through which the ray passes
     * @return the central ray
     */
    public static Ray generateCentralRay(Point origin, Point target) {
        return new Ray(origin, target.subtract(origin));
    }

    /**
     * Constructs a ray from the camera through a pixel (i, j).
     *
     * @param nX number of pixels in the x direction
     * @param nY number of pixels in the y direction
     * @param j  the pixel column index
     * @param i  the pixel row index
     * @return the constructed ray
     */
    public Ray generateCentralRayOfPixel(int nX, int nY, int j, int i) {
        return generateCentralRay(camera.getP0(), getCenterOfPixel(nX, nY, j, i));
    }


    public Point getCenterOfPixel(int nX, int nY, int j, int i) {
        double yI = alignZero(-(i - (nY - 1) / 2d) * camera.getHeight() / nY); // Vertical shift
        double xJ = alignZero((j - (nX - 1) / 2d) * camera.getWidth() / nX); // Horizontal shift

        Point pIJ = camera.getVPCenter();
        ;
        if (!isZero(xJ)) pIJ = pIJ.add(camera.getVRight().scale(xJ));
        if (!isZero(yI)) pIJ = pIJ.add(camera.getVUp().scale(yI));

        return pIJ;
    }

    /**
     * Generates multiple sample rays within a specified area for anti-aliasing.
     *
     * @param numSamples the number of rays to generate
     * @return a list of sample rays
     */
    public List<Ray> generateAntiAliasingRays(int nX, int nY, int j, int i, double pixelRadius, int numSamples) {
        Point centerOfPixel = getCenterOfPixel(nX, nY, j, i);
        Vector vRight = camera.getVRight();
        Vector vUp = camera.getVUp();

//        return generateAntiAliasingConeRandomRays(numSamples, pixelRadius, vRight, vUp, centerOfPixel);
//        return generateAntiAliasingGridRays(numSamples, pixelRadius, vRight, vUp, centerOfPixel);
        return generateAntiAliasingRingRays(numSamples, pixelRadius, vRight, vUp, centerOfPixel);
        // return generateAntiAliasingSpiralRays(numSamples, pixelRadius, vRight, vUp, centerOfPixel);
    }


    public List<Ray> generateAntiAliasingConeRandomRays(int numSamples, double pixelRadius, Vector vRight, Vector vUp, Point centerOfPixel) {
        List<Ray> rays = new ArrayList<>();

        for (int k = 0; k < numSamples; k++) {
            double xOffset = (Math.random() - 0.5) * pixelRadius * 2;
            double yOffset = (Math.random() - 0.5) * pixelRadius * 2;

            Point samplePoint = centerOfPixel.add(vRight.scale(xOffset)).add(vUp.scale(yOffset));

            rays.add(generateCentralRay(camera.getP0(), samplePoint));
        }
        return rays;
    }

    public List<Ray> generateAntiAliasingGridRays(int numSamples, double pixelRadius, Vector vRight, Vector vUp, Point centerOfPixel) {
        List<Ray> rays = new ArrayList<>();

        int gridSize = (int) Math.ceil(Math.sqrt(numSamples)); // גודל הרשת (מספר תאים בכל שורה ועמודה)
        double step = (pixelRadius * 2) / gridSize; // גודל הצעד בין תאים בתוך הפיקסל

        for (int x = 0; x < gridSize; x++) {
            for (int y = 0; y < gridSize; y++) {
                // חישוב ההיסטים של כל נקודה בגריד
                double xOffset = (x + 0.5) * step - pixelRadius; // מרכז התא האופקי
                double yOffset = (y + 0.5) * step - pixelRadius; // מרכז התא האנכי

                // יצירת נקודת דגימה בתוך הפיקסל
                Point samplePoint = centerOfPixel.add(vRight.scale(xOffset)).add(vUp.scale(yOffset));
                Vector direction = samplePoint.subtract(camera.getP0());

                // הוספת קרן אם הווקטור תקין
                if (!isZero(direction.length())) {
                    rays.add(generateCentralRay(camera.getP0(), samplePoint));
                }
            }
        }
        return rays;
    }


    public List<Ray> generateAntiAliasingRingRays(int numSamples, double pixelRadius, Vector vRight, Vector vUp, Point centerOfPixel) {
        List<Ray> rays = new ArrayList<>();
        rays.add(generateCentralRay(camera.getP0(), centerOfPixel)); // קרן למרכז הפיקסל

        int numRings = Math.max(1, (int) Math.sqrt(numSamples)); // מספר טבעות
        double ringStep = pixelRadius / numRings; // מרחק בין הטבעות

        for (int ring = 1; ring <= numRings; ring++) {
            double currentRadius = ring * ringStep; // רדיוס הטבעת
            int pointsInRing = Math.max(6, ring * 6); // מספר נקודות בטבעת

            for (int k = 0; k < pointsInRing; k++) {
                // חישוב זוויות ואופסטים
                double angle = 2 * Math.PI * k / pointsInRing;
                double xOffset = currentRadius * Math.cos(angle);
                double yOffset = currentRadius * Math.sin(angle);

                // דילוג על נקודות אפס
                if (isZero(xOffset) || isZero(yOffset)) {
                    continue;
                }

                Point samplePoint = centerOfPixel.add(vRight.scale(xOffset)).add(vUp.scale(yOffset));
                rays.add(generateCentralRay(camera.getP0(), samplePoint));
            }
        }
        return rays;
    }


    private List<Ray> generateAntiAliasingSpiralRays(int numSamples, double pixelRadius, Vector vRight, Vector vUp, Point centerOfPixel) {
        List<Ray> rays = new ArrayList<>();

        double goldenAngle = Math.PI * (3 - Math.sqrt(5)); // יחס הזהב

        for (int k = 0; k < numSamples; k++) {
            double radius = pixelRadius * Math.sqrt((double) k / numSamples); // רדיוס עולה
            double angle = k * goldenAngle;

            double xOffset = radius * Math.cos(angle);
            double yOffset = radius * Math.sin(angle);

            Point samplePoint = centerOfPixel.add(vRight.scale(xOffset)).add(vUp.scale(yOffset));
            rays.add(generateCentralRay(camera.getP0(), samplePoint));
        }
        return rays;
    }
}