package main;

class Color extends Vec3f {

        
    private float alpha = 0.0f;

    public Color(float r, float g, float b, float alpha) {
        super(r, g, b);
        this.alpha = alpha;
    }

    public Color(float r, float g, float b) {
        super(r, g, b);
    }

    public float getAlpha() {
        return alpha;
    }


    public void setAlpha(float alpha) {
        this.alpha = alpha;
    }

    public Color scale(Color c){
        float r = x * c.x;
        float g = y * c.y;
        float b = z * c.z;
        float a = alpha * c.alpha;

        return new Color(r, g, b, a);
    }

    public Color scale(float coeff){
        float r = x * coeff;
        float g = y * coeff;
        float b = z * coeff;
        float a = alpha * coeff;

        return new Color(r, g, b, a);
    }


    public Color add(Color c) {
        float r = x + c.x;
        float g = y + c.y;
        float b = z + c.z;
        float a = alpha + c.alpha;

        return new Color(r, g, b, a);
    }


}