package main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;


public class Main
{

    private static void writeShort(FileOutputStream fout,int n) throws IOException
    {
        fout.write(n&255);
        fout.write((n>>8)&255);
    }

   
    private static void saveTGA(String filename, byte buffer[], int width, int height) throws IOException, UnsupportedEncodingException {

        FileOutputStream fout = new FileOutputStream(new File(filename));

        fout.write(0); // Comment size, no comment
        fout.write(0); // Colormap type: No colormap
        fout.write(2); // Image type
        writeShort(fout,0); // Origin
        writeShort(fout,0); // Length
        fout.write(0); // Depth
        writeShort(fout,0); // X origin
        writeShort(fout,0); // Y origin
        writeShort(fout,width); // Width of the image
        writeShort(fout,height); // Height of the image
        fout.write(24); // Pixel size in bits (24bpp)
        fout.write(0); // Descriptor

        /* Write the buffer */
        fout.write(buffer);

        fout.close();
    }


    public static void main(String[] args) {
        int w=3000;
        int h=3000;
                
        Scene scene = new Scene();

        Camera camera = new Camera(new Vec3f(0, -2,-3), new Vec3f(0,0.5f,1), new Vec3f(0,1,0), 90);


        RayTracer rayTracer = new RayTracer(scene, camera);
        byte[] buffer = rayTracer.render(w,h);

        try {
            saveTGA("imagetest.tga",buffer,w,h);
        }
        catch(Exception e)
        {
            System.err.println("TGA file not created :"+e);
        }
    }  
}
