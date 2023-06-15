package main;

class Camera {
    public Vec3f position;
    public Vec3f direction;
    public Vec3f up;
    public float fov;
    public float tanFov;

    public Camera(Vec3f position, Vec3f direction, Vec3f up, float fov) {
        this.position = position;
        this.direction = direction.normalize();
        this.up = up.normalize();
        this.fov = fov;
        this.tanFov = (float) Math.tan(Math.toRadians(fov / 2));
    }

    public Ray getRay(float x, float y, int width, int height) {
        float aspectRatio = (float) width / height;
        float halfWidth = tanFov * aspectRatio;
        float halfHeight = tanFov;
    
        float dx = (2 * x / width - 1) * halfWidth;
        float dy = (1 - 2 * y / height) * halfHeight;
    
        Vec3f right = direction.crossProduct(up).normalize();
        Vec3f offset = right.scale(dx).add(up.scale(dy));
        Vec3f target = direction.add(offset).normalize();
    
        return new Ray(position, target);
    }
}