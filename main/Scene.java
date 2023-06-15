package main;

import java.util.ArrayList;
import java.util.List;

class Scene {
    private List<Shape> objects = new ArrayList<>();
    private List<Light> lights = new ArrayList<>();

    public Scene() {
        objects.add(new Plane(new Vec3f(0, -1, 0), new Vec3f(1f, 1f, 1f), "res/aiGenerated2.jpg"));


        Sphere sphereColor = new Sphere(new Vec3f(0, -0.8f, 1f), 0.5f, new Color(1, 1, 1), 3f, 0f, 0f);
        Sphere sphereTexture = new Sphere(new Vec3f(0, 0.7f, 1f), 1f, 0.2f, 0f, 1f, "res/aiGenerated.jpg");
        objects.add(sphereTexture);
        objects.add(sphereColor);


        Light light1 = new Light(new Vec3f(5, -4, 0), new Color(1, 1, 0.2f), 1f, 1f, 1f, 1f, 1f);
        lights.add(light1);

        Light light2 = new Light(new Vec3f(-5, -4, 0), new Color(0f, 0.1f, 1), 1f, 1f, 1f, 1f, 1f);
        lights.add(light2);


        
    }

    public List<Shape> getObjects() {
        return objects;
    }

    public List<Light> getLights() {
        return lights;
    }

    public boolean colorShadow(Vec3f M, Light light) {
        Vec3f L = light.position.sub(M).normalize();
        Ray shadowRay = new Ray(M.add(L.scale(0.001f)), L);
    
        for (Shape obj : objects) {
            double lambda = obj.getIntersection(shadowRay.p, shadowRay.v);
            if (lambda > 0) {
                return true;
            }
        }
        return false;
    }

    public Color findColor(Vec3f P, Vec3f v, int recursionStep, int maxRecursionStep) {
        Shape closestObject = findClosestObject(P, v);
        if (closestObject == null) {
            return new Color(1f, 1f, 0.2f);
        }
    
        Vec3f intersection = P.add(v.scale((float) closestObject.getIntersection(P, v)));
        Vec3f normal = closestObject.getNormal(intersection);
        Color color = closestObject.getTextureColor(intersection);
    
        Color ambientColor = new Color(0f, 0f, 0f);
        Color diffuseColor = new Color(0, 0, 0);
        Color specularColor = new Color(0, 0, 0);
    
        for (Light light : lights) {
            boolean isInShadow = colorShadow(intersection, light);
            if (!isInShadow) {
                ambientColor = ambientColor.add(calculateAmbientColor(color, light));
                diffuseColor = diffuseColor.add(calculateDiffuseColor(color, light, intersection, normal));
                specularColor = specularColor.add(calculateSpecularColor(color, light, intersection, normal, v, closestObject.shininess));
            }
        }
    
        Color refractionColor = calculateRefractionColor(closestObject, intersection, v, recursionStep, maxRecursionStep);
        Color reflectionColor = calculateReflectionColor(closestObject, intersection, v, recursionStep, maxRecursionStep);
    
        Color finalColor = ambientColor.add(diffuseColor).add(specularColor).add(reflectionColor.scale(closestObject.reflectivity)).add(refractionColor.scale(closestObject.transparency));
        return finalColor;
    }

    private Shape findClosestObject(Vec3f P, Vec3f v) {
        double minLambda = Double.POSITIVE_INFINITY;
        Shape closestObject = null;
        for (Shape obj : objects) {
            double lambda = obj.getIntersection(P, v);
            if (lambda > 0 && lambda < minLambda) {
                minLambda = lambda;
                closestObject = obj;
            }
        }
        return closestObject;
    }
    
    private Color calculateAmbientColor(Color color, Light light) {
        return color.scale(light.color).scale(light.ambientCoefficient).scale(light.intensity);
    }
    
    private Color calculateDiffuseColor(Color color, Light light, Vec3f intersection, Vec3f normal) {
        Vec3f L = light.position.sub(intersection).normalize();
        float NdotL = normal.dotProduct(L);
        Color diffuseTerm = color.scale(light.color).scale(Math.max(NdotL, 0)).scale(light.diffuseCoefficient);
        float distanceToLight = light.position.sub(intersection).length();
        float attenuation = light.intensity / (distanceToLight * distanceToLight);
        return diffuseTerm.scale(light.intensity).scale(attenuation);
    }
    
    private Color calculateSpecularColor(Color color, Light light, Vec3f intersection, Vec3f normal, Vec3f v, float shininess) {
        Vec3f L = light.position.sub(intersection).normalize();
        Vec3f V = v.scale(-1).normalize();
        Vec3f H = L.add(V).normalize();
        float NdotH = normal.dotProduct(H);
        Color specularTerm = light.color.scale((float) Math.pow(Math.max(NdotH, 0), shininess)).scale(light.specularCoefficient);
        float distanceToLight = light.position.sub(intersection).length();
        float attenuation = light.intensity / (distanceToLight * distanceToLight);
        return specularTerm.scale(light.intensity).scale(attenuation);
    }
    
    private Color calculateRefractionColor(Shape closestObject, Vec3f intersection, Vec3f v, int recursionStep, int maxRecursionStep) {
        float refractiveIndex = 1f;
        float transparency = closestObject.transparency;
        Color refractionColor = new Color(0, 0, 0);
        if (transparency > 0 && recursionStep < maxRecursionStep) {
            float n1 = 1f;
            float n2 = refractiveIndex;
            float n = n1 / n2;
            Vec3f normal = closestObject.getNormal(intersection);
            if (normal.dotProduct(v) > 0) {
                n = n2 / n1;
                normal = normal.scale(-1);
            }
            float cosI = -normal.dotProduct(v);
            float cosT2 = 1 - n * n * (1 - cosI * cosI);
            if (cosT2 > 0) {
                Vec3f refractionDirection = v.scale(n).add(normal.scale(n * cosI - (float) Math.sqrt(cosT2))).normalize();
                refractionColor = findColor(intersection.sub(refractionDirection.scale(0.0001f)), refractionDirection, recursionStep + 1, maxRecursionStep);
            }
        }
        return refractionColor;
    }
    
    private Color calculateReflectionColor(Shape closestObject, Vec3f intersection, Vec3f v, int recursionStep, int maxRecursionStep) {
        float reflectivity = closestObject.reflectivity;
        Color reflectionColor = new Color(0, 0, 0);
        if (reflectivity > 0 && recursionStep < maxRecursionStep) {
            Vec3f normal = closestObject.getNormal(intersection);
            Vec3f reflectionDirection = v.sub(normal.scale(2 * v.dotProduct(normal))).normalize();
            reflectionColor = findColor(intersection.sub(reflectionDirection.scale(0.0001f)), reflectionDirection, recursionStep + 1, maxRecursionStep);
        }
        return reflectionColor;
    }
}