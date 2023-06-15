package main;

class Plane extends Shape {
    private Vec3f normal;
    private Vec3f point;
    public Color color;

    public Texture texture = null;

    public Plane(Vec3f normal, Vec3f point, Color color) {
        this.normal = normal;
        this.point = point;
        this.color = color;

        reflectivity = 0.0f;
        transparency = 0f;
        shininess = 10f;
    }

    public Plane(Vec3f normal, Vec3f point, String texture) {
        this.normal = normal;
        this.point = point;
        this.texture = new Texture(texture);

        reflectivity = 0.0f;
        transparency = 0f;
        shininess = 10f;
    }

    public double getIntersection(Vec3f P, Vec3f v) {
        double t = -(normal.dotProduct(P.sub(point))) / normal.dotProduct(v);
        if (t < 0) {
            return -1;
        }
        return t;
    }

    public Color getColor() {
        return color;
    }

    public Color getTextureColor(Vec3f i) {
        if (texture == null) {
            return getColor();
        }
        double u = (i.x - Math.floor(i.x));
        double v = (i.z - Math.floor(i.z));
        return texture.getColor((float)u, (float)v);
    }

    public Vec3f getNormal(Vec3f i) {
        return normal;
    }


}