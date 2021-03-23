package Photopia;

import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;
import java.util.Collection;

public interface Mosaic {
    static String mosaicGenerate(String dirPath, int scale, BufferedImage img) throws IOException {
        int pixel, red,green, blue,index;
        Color color;

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
                color = new Color(red,green,blue);

                //Generate the tile with the pixel color
                g_b.setColor(color);
                g_b.fillRect(x*scale,y*scale,scale,scale);
            }
        }
        g_b.dispose();
        //Export image as Base64
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(big_image, "jpg", out);
        //Return the path of the image
        return ASCII.Base64toImg(Base64.getEncoder().encodeToString(out.toByteArray()));
    }

}
