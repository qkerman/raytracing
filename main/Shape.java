package main;

abstract class Shape {
    public float shininess;
    public float reflectivity;
    public float transparency;

    abstract double getIntersection(Vec3f P, Vec3f v);
    abstract Color getColor();
    abstract Vec3f getNormal(Vec3f i);

    public Color getTextureColor(Vec3f i) {
        return getColor();
    }
}