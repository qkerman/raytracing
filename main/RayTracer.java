package main;

class RayTracer {
    private Scene scene;
    private Camera camera;
    private int maxRecursionStep = 5;

    public RayTracer(Scene scene, Camera camera) {
        this.scene = scene;
        this.camera = camera;
    }

    public byte[] render(int width, int height) {
        byte[] buffer = new byte[width * height * 3];
        byte[] colorBytes = new byte[3];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Ray ray = camera.getRay(x, y, width, height);
                Color color = scene.findColor(ray.p, ray.v, 0, maxRecursionStep);
                color = calculateColor(color);

                colorBytes[0] = (byte) (255 * color.x);
                colorBytes[1] = (byte) (255 * color.y);
                colorBytes[2] = (byte) (255 * color.z);

                int index = (y * width + x) * 3;
                System.arraycopy(colorBytes, 0, buffer, index, 3);
            }
        }
        return buffer;
    }

    private Color calculateColor(Color color) {
        float colorX = color.x / (color.x + 1f);
        float colorY = color.y / (color.y + 1f);
        float colorZ = color.z / (color.z + 1f);
        return new Color(colorX, colorY, colorZ);
    }
}