import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.*;
import javax.imageio.ImageIO;

public class SepiaToneFilter {

    private static BufferedImage image;

    public static BufferedImage readImage (String path) {
        try {
            File input_file = new File(path);
            image = ImageIO.read(input_file);
        } catch (Exception e) {e.printStackTrace();}

        return image;
    }

    public static BufferedImage test(BufferedImage img, int width, int height) {

        BufferedImage sepia = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        // Good value (sepiaDepth) is 20 but 0 produces black-and-white image
        int sepiaDepth = 20;
        int sepiaIntensity = 80;

        WritableRaster raster = sepia.getRaster();

        // 3 integers are needed (for R,G,B color values) per pixel.
        int[] pixels = new int[width * height * 3];
        img.getRaster().getPixels(0, 0, width, height, pixels);

        //  Process 3 ints at a time for each pixel.  Each pixel has 3 RGB
        //    colors in array
        for (int i = 0; i < pixels.length; i += 3) {
            int r = pixels[i];
            int g = pixels[i + 1];
            int b = pixels[i + 2];

            int gry = (r + g + b) / 3;
            r = g = b = gry;
            r = r + (sepiaDepth * 2);
            g = g + sepiaDepth;

            if (r > 255) {
                r = 255;
            }
            if (g > 255) {
                g = 255;
            }
            if (b > 255) {
                b = 255;
            }

            // Darken blue color to increase sepia effect
            b -= sepiaIntensity;

            // Normalizes if out of bounds
            if (b < 0) {
                b = 0;
            }
            if (b > 255) {
                b = 255;
            }

            pixels[i] = r;
            pixels[i + 1] = g;
            pixels[i + 2] = b;
        }
        raster.setPixels(0, 0, width, height, pixels);

        return sepia;
    }

    static public void main(String args[]) throws Exception {

        String input_image_path = "C:\\He\\Thesis\\Sepia-Tone-Filter\\rsc\\img.JPG";
        String output_image_path = "C:\\He\\Thesis\\Sepia-Tone-Filter\\rsc\\op\\";
        image = readImage(input_image_path);

        int width = image.getWidth();
        int height = image.getHeight();


        long iteration = 20; // do 1000 for final
        for (long i = 0; i < iteration; i++) {
            try {
                image = test(image, width, height);
                File output_img = new File(output_image_path + i + "_output.jpg");
                ImageIO.write(image, "jpg", output_img);
            } catch (Exception e) {
            }
        }
    }
}
