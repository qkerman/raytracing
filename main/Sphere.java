package main;

class Sphere extends Shape {
    private Vec3f position;
    private double radius;
    private Texture texture;
    private Color color;

    public Sphere(Vec3f center, double radius, Color color, float reflectivity, float transparency, float shininess) {
        this.position = center;
        this.radius = radius;
        this.reflectivity = reflectivity;
        this.transparency = transparency;
        this.shininess = shininess;
        this.color = color;
    }

    public Sphere(Vec3f center, double radius, float reflectivity, float transparency, float shininess, String textureFilename) {
        this(center, radius, null, reflectivity, transparency, shininess);
        this.texture = new Texture(textureFilename);
    }

    public double getIntersection(Vec3f P, Vec3f v) {
        Vec3f L = P.sub(position);
        double a = v.dotProduct(v);
        double b = 2 * v.dotProduct(L);
        double c = L.dotProduct(L) - radius * radius;
        double delta = b * b - 4 * a * c;
        if (delta < 0) {
            return -1;
        } else if (delta == 0) {
            return -b / (2 * a);
        } else {
            double t1 = (-b + Math.sqrt(delta)) / (2 * a);
            double t2 = (-b - Math.sqrt(delta)) / (2 * a);
            return Math.min(t1, t2);
        }
    }
    
    public Color getColor() {
        return color;
    }

    public Color getTextureColor(Vec3f i) {
        if (texture == null) {
            return getColor();
        }
        Vec3f d = i.sub(position).normalize();
        double u = 0.5 + Math.atan2(d.z, d.x) / (2 * Math.PI);
        double v = 0.5 - Math.asin(d.y) / Math.PI;
        return texture.getColor((float)u, (float)v);
    }

    public Vec3f getNormal(Vec3f i) {
        return i.sub(position).normalize();
    }
}