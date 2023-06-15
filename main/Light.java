package main;

class Light {
    public Vec3f position;
    public Color color;
    public float intensity;
    public float ambientCoefficient;
    public float diffuseCoefficient;
    public float specularCoefficient;


    public Light(Vec3f position, Color color, float intensity, float ambientCoefficient, float diffuseCoefficient, float specularCoefficient, float shininess) {
        this.position = position;
        this.color = color;
        this.intensity = intensity;
        this.ambientCoefficient = ambientCoefficient;
        this.diffuseCoefficient = diffuseCoefficient;
        this.specularCoefficient = specularCoefficient;
    }
}