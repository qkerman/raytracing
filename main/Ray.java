package main;

class Ray {
    public Vec3f p;
    public Vec3f v;

    public Ray(Vec3f p, Vec3f v) {
        this.p = p;
        this.v = v.normalize();
    }
}