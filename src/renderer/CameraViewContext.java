//package renderer;
//
//import primitives.Point;
//import primitives.Vector;
//
//public class CameraViewContext implements Cloneable{
//    private Point cameraPosition;
//    private Point viewPlaneCenter;
//    private Vector upVector;
//    private Vector toVector;
//    private Vector rightVector;
//    private double viewPlaneWidth;
//    private double viewPlaneHeight;
//    private double distanceToViewPlane;
//
//    public CameraViewContext() {
//        this.cameraPosition = null;
//        this.upVector = null;
//        this.toVector = null;
//        this.viewPlaneWidth = 0.0;
//        this.viewPlaneHeight = 0.0;
//        this.distanceToViewPlane = 0.0;
//        this.viewPlaneCenter = null;
//    }
//
//    /**
//     * Constructs a CameraViewContext with the given camera position, up vector, forward vector, width, height, and distance.
//     * The right vector and view plane center are calculated automatically.
//     *
//     * @param p0       the position of the camera
//     * @param vUp      the up vector of the view plane
//     * @param vTo      the forward vector from the camera to the view plane
//     * @param width    the width of the view plane
//     * @param height   the height of the view plane
//     * @param distance the distance from the camera to the view plane
//     */
//    public CameraViewContext(Point p0, Vector vUp, Vector vTo, double width, double height, double distance) {
//        this.cameraPosition = p0;
//        this.upVector = vUp;
//        this.toVector = vTo;
//        this.viewPlaneWidth = width;
//        this.viewPlaneHeight = height;
//        this.distanceToViewPlane = distance;
//        this.rightVector = vTo.crossProduct(vUp).normalize();
//        this.viewPlaneCenter = p0.add(vTo.scale(distance));
//    }
//
//    // Getters for each field
//    public Point getCameraPosition() {
//        return cameraPosition;
//    }
//
//    public Point getViewPlaneCenter() {
//        return viewPlaneCenter;
//    }
//
//    public Vector getUpVector() {
//        return upVector;
//    }
//
//    public Vector getToVector() {
//        return toVector;
//    }
//
//    public Vector getRightVector() {
//        return rightVector;
//    }
//
//    public double getViewPlaneWidth() {
//        return viewPlaneWidth;
//    }
//
//    public double getViewPlaneHeight() {
//        return viewPlaneHeight;
//    }
//
//    public double getDistanceToViewPlane() {
//        return distanceToViewPlane;
//    }
//
//    // Setters for each field
//    public void setCameraPosition(Point cameraPosition) {
//        this.cameraPosition = cameraPosition;
//        updateViewPlaneCenter();
//    }
//
//    public void setUpVector(Vector upVector) {
//        this.upVector = upVector;
//        updateRightVector();
//    }
//
//    public void setToVector(Vector toVector) {
//        this.toVector = toVector;
//        updateRightVector();
//        updateViewPlaneCenter();
//    }
//
//    public void setViewPlaneWidth(double viewPlaneWidth) {
//        this.viewPlaneWidth = viewPlaneWidth;
//    }
//
//    public void setViewPlaneHeight(double viewPlaneHeight) {
//        this.viewPlaneHeight = viewPlaneHeight;
//    }
//
//    public void setDistanceToViewPlane(double distanceToViewPlane) {
//        this.distanceToViewPlane = distanceToViewPlane;
//        updateViewPlaneCenter();
//    }
//
//    // Helper methods to update dependent fields
//    private void updateRightVector() {
//        if (toVector != null && upVector != null) {
//            this.rightVector = toVector.crossProduct(upVector).normalize();
//        }
//    }
//
//    private void updateViewPlaneCenter() {
//        if (cameraPosition != null && toVector != null && distanceToViewPlane != 0.0) {
//            this.viewPlaneCenter = cameraPosition.add(toVector.scale(distanceToViewPlane));
//        }
//    }
//
//    @Override
//    public CameraViewContext clone() {
//        try {
//            CameraViewContext cloned = (CameraViewContext) super.clone();
//            cloned.cameraPosition = new Point(this.cameraPosition);
//            cloned.viewPlaneCenter = new Point(this.viewPlaneCenter);
//            cloned.upVector = new Vector(this.upVector);
//            cloned.toVector = new Vector(this.toVector);
//            cloned.rightVector = new Vector(this.rightVector);
//            return cloned;
//        } catch (CloneNotSupportedException e) {
//            throw new AssertionError(); // Should never happen because CameraViewContext implements Cloneable
//        }
//    }
//}
//
