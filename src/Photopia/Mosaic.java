package Photopia;

import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;
import java.util.Collection;
import java.util.Vector;

public interface Mosaic {
    static String mosaicGenerate(String dirPath, int scale, BufferedImage img) throws IOException {
        int pixel, red,green, blue, pointer;
        double min;
        Color color;
        File[] files = new File(dirPath).listFiles();
        Vector<java.awt.Image> images = new Vector<java.awt.Image>();

        //Add all file images to vector images
        for (File file:
                files) {
            //Check if the file is image
            if (Controller.checkFileExtension(file.getName())){
                images.add(ImageIO.read(file));
            }
        }

        //Calculate all image file RGB value and store in a vector
        Vector<Color> rgb = calc_files_avg_color(dirPath);

        //Defines the dimensions for each tile
        int width = img.getWidth()/scale;
        int height = img.getHeight()/scale;

        //Create an image that contains the original image with the size of the tile so that each pixel of this image can be the average color of the tile area.
        BufferedImage small_image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        //Create the image that has the original size.
        BufferedImage big_image = new BufferedImage(img.getWidth(),img.getHeight(),BufferedImage.TYPE_INT_RGB);

        //Start drawing
        Graphics g = small_image.createGraphics();
        Graphics g_b = big_image.createGraphics();

        //Draw image on small image
        g.drawImage(img,0,0,width,height,null);
        g.dispose();

        //For each pixel of the small image, it can become a tile in the large image.
        for (int x=0;x<width;++x){
            for (int y = 0; y < height; y++) {
                pixel = small_image.getRGB(x,y);
                // Get RGB value of the pixel
                red = (pixel & 0xff0000) >> 16;
                green = (pixel & 0xff00) >> 8;
                blue = (pixel & 0xff);

                pointer = 0;
                min = 255;//Largest difference possible
                color = new Color(red,green,blue);
                double difference;

                //Get the closest color from the vector rgb
                for (int i = 0; i < rgb.size(); i++) {
                    difference = getColorSimilarity(color, rgb.get(i));
                    if(getColorSimilarity(color, rgb.get(i))<min){
                        min = difference;
                        pointer = i;
                    }
                }

                //Generate the tile with the cloest image
                g_b.drawImage(images.get(pointer),x*scale,y*scale,scale,scale,null);
            }
        }
        g_b.dispose();
        //Export image as Base64
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(big_image, "jpg", out);
        //Return the path of the image
        return ASCII.Base64toImg(Base64.getEncoder().encodeToString(out.toByteArray()));
    }

    private static Vector<Color> calc_files_avg_color(String dir) throws IOException {
        //This method reads all the image file in the directory and calculate the average color for each image and return a vector that stores all the average.
        //Declare variables
        int pixel, red,green, blue;
        Color color;
        BufferedImage f;

        //Calculate all image file RGB value and store in a vector
        Vector<Color> rgb = new Vector<Color>(0);
        File[] files = new File(dir).listFiles();
        for (File file:
                files) {
            //Check if the file is image
            if (Controller.checkFileExtension(file.getName())){
                //Read image
                f = ImageIO.read(file);
                //Initialise the Buckets that accumulate the RGB values of all pixels, so as to achieve the average color of the image
                int redBucket = 0, greenBucket=0,blueBucket=0, pixel_num=0;
                for (int i = 0; i < f.getWidth(); i++) {
                    for (int j = 0; j < f.getHeight(); j++) {
                        //Accumulate the rgb
                        pixel = f.getRGB(i,j);
                        redBucket += (pixel & 0xff0000) >> 16;
                        greenBucket += (pixel & 0xff00) >> 8;
                        blueBucket += (pixel & 0xff);
                        pixel_num++;
                    }
                }
                color = new Color(redBucket/pixel_num,greenBucket/pixel_num,blueBucket/pixel_num);
                rgb.add(color);
            }
        }
        return rgb;
    }

    private static double getColorSimilarity(Color color, Color color_new){
        double dr, dg, db;
        dr = color.getRed() - color_new.getRed();
        dg = color.getGreen() - color_new.getGreen();
        db = color.getBlue() - color_new.getBlue();
        return Math.pow(Math.pow(dr,2)+Math.pow(dg,2)+Math.pow(db,2),0.5);
    }
}
