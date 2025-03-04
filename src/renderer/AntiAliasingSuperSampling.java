//package renderer;
//
//import primitives.Color;
//import primitives.Point;
//import primitives.Ray;
//import primitives.Vector;
//import java.util.List;
//
///**
// * Implementation of anti-aliasing super sampling enhancement.
// */
//public class AntiAliasingSuperSampling extends SuperSampling {
//    private double pixelWidth;
//    private double pixelHeight;
//    private Vector vRight;
//    private Vector vUp;
//    private Point pixelCenter;
//    private Point cameraPosition;
//
//    public AntiAliasingSuperSampling(int numSamples, Point cameraPosition, Point pixelCenter, double pixelWidth, double pixelHeight, Vector vRight, Vector vUp) {
//        super(numSamples);
//        this.cameraPosition = cameraPosition;
//        this.pixelCenter = pixelCenter;
//        this.pixelWidth = pixelWidth;
//        this.pixelHeight = pixelHeight;
//        this.vRight = vRight;
//        this.vUp = vUp;
//    }
//
//    @Override
//    protected List<Ray> generateSampleRays() {
//        // שימוש ב-RayGenerator כדי לייצר קרניים עבור אנטי-אליאסינג
//        return RayGenerator.generateAntiAliasingRays(cameraPosition, pixelCenter, vRight, vUp, pixelWidth, pixelHeight, numSamples);
//    }
//}
