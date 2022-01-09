/**
 * @author Md Alvee Noor
 * 2022
 */

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.imageio.ImageIO;

public class SepiaToneFilter {

    // Source image data
    private static BufferedImage image;

    // Path for resultant images
    private static String output_images_path;

    // Source image dimensions
    static int width;
    static int height;

    // RGB values for the source image
    static float[] red_ij;
    static float[] green_ij;
    static float[] blue_ij;

    /**
     * Reads the image.
     *
     * @param image_path Source path of the image to read.
     */
    private static void readImage(String image_path) {
        try {
            // Load and read image
            File source_file = new File(image_path);
            image = ImageIO.read(source_file);

            // Check output directory
            checkOutputDirectory(source_file.getParent());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if output directory is there, creates one if not found.
     *
     * @param directory_path Source path of the image.
     */
    private static void checkOutputDirectory(String directory_path) {
        System.out.println("Source image path: " + directory_path);

        String separator = System.getProperty("file.separator");

        // Checks for the target directory for resultant images
        if (!Files.isDirectory(Paths.get(directory_path + separator + "ResultantImages"))) {
            System.out.println("Output directory is not found, creating one.");
            new File(directory_path + separator + "ResultantImages").mkdirs();
        } else System.out.println("Output directory is already there, skipping creation.");

        output_images_path = directory_path + separator + "ResultantImages" + separator;
    }

    /**
     * Reads the provided image's RGB values.
     *
     * @param img    Image to read.
     * @param width  Width of the image.
     * @param height Height of the image.
     */
    private static void readImageRGB(BufferedImage img, int width, int height) {
        for (int i = 0, k = 0; i < height; i++) {
            for (int j = 0; j < width; j++, k++) {
                Color c = new Color(img.getRGB(j, i));
                red_ij[k] = c.getRed();
                green_ij[k] = c.getGreen();
                blue_ij[k] = c.getBlue();
            }
        }
    }

    /**
     * Sets the gray-parameters for RGB values of the image & measures the time in ns.
     *
     * @return The time elapsed, in nano-seconds, for the operation
     */
    private static long setGray() {

        // Start counting time
        long startTime = System.nanoTime();

        for (int i = 0; i < height * width; i++) {
            red_ij[i] = (red_ij[i] * 0.299f);
            green_ij[i] = (green_ij[i] * 0.587f);
            blue_ij[i] = (blue_ij[i] * 0.114f);
        }

        return System.nanoTime() - startTime;
    }

    /**
     * Sets the Sepia Tone values and applies to the source image.
     *
     * @param img    Source image to apply Sepia Tone.
     * @param width  Width of the image.
     * @param height Height of the image.
     * @return Resultant image.
     */
    private static BufferedImage applySepiaFiler(BufferedImage img, int width, int height) {

        // Suitable value for sepiaToneDepth is 20 and 0 produces black-and-white image
        // Suitable value for sepiaToneIntensity is 80
        int sepiaToneDepth = 20;
        int sepiaToneIntensity = 80;

        //  Iterate over the image pixels for setting the Sepia-tone Filter
        for (int i = 0, k = 0; i < height; i++) {
            for (int j = 0; j < width; j++, k++) {
                int r;
                int g;
                int b;

                r = g = b = (int) ((red_ij[k] + green_ij[k] + blue_ij[k]));

                r = r + (sepiaToneDepth * 2);
                g = g + sepiaToneDepth;

                // Normalizes if out of bounds
                if (r > 255) {
                    r = 255;
                }
                if (g > 255) {
                    g = 255;
                }
                if (b > 255) {
                    b = 255;
                }

                // Darken blue color to increase Sepia effect
                b -= sepiaToneIntensity;

                // Normalizes if out of bounds
                if (b < 0) {
                    b = 0;
                }
                if (b > 255) {
                    b = 255;
                }

                try {
                    // Applies the Sepia-tone Filter to the pixels
                    Color newColor = new Color(r, g, b);
                    img.setRGB(j, i, newColor.getRGB());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return img;
    }

    public static void main(String[] args) throws Exception {

        // Exit if appropriate arguments are not given
        if (args.length != 2) {
            System.out.println("Usage: java SepiaToneFilter <JPG_IMAGE_PATH> <ITERATIONS> \nExiting.");
            System.exit(0);
        }

        // Get source image path from the provided argument
        String src_image = args[0];

        // Get iterations from the provided argument
        long iterations = Integer.valueOf(args[1]);

        // Stores the time elapsed for the target method
        long elapsed_time = 0;

        // Reads the image from source path
        readImage(src_image);

        width = image.getWidth();
        height = image.getHeight();

        red_ij = new float[height * width];
        green_ij = new float[height * width];
        blue_ij = new float[height * width];

        System.out.println("Applying Sepia Tone Filter to " + iterations + " images.");
        for (long i = 0; i < iterations; i++) {
            try {
                // Sequence of the process: read image RGB > set Gray-scale values > apply Sepia-tone Filter
                readImageRGB(image, width, height);
                elapsed_time += setGray();
                image = applySepiaFiler(image, width, height);
                File output = new File(output_images_path + i + "_output.jpg");
                ImageIO.write(image, "jpg", output);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("Elapsed time (average) for target method: " + (elapsed_time / iterations) + " ns");
        System.out.println("Resultant images' path: " + output_images_path);
    }
}
