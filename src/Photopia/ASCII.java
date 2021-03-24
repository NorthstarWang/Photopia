package Photopia;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;

public interface ASCII {
    String ascii = "$@B%8&WM#*oahkbdpqwmZO0QLCJUYXzcvunxrjft/\\|()1{}[]?-_+~<>i!lI;:,\\\"^`'.";
    String base = "@#&$%*o!;.";

    int IMAGE_TYPE = BufferedImage.TYPE_INT_RGB;

    static String txtToImageByBase64(BufferedImage bi, int set_row_pixel, float contrast, int brightness, String type) throws IOException {
        int ratio;
        //ratio determines the step for the render character. E.g. ratio = 5 means 5 pixels transform into 1 ASCII character.
        if(set_row_pixel==0){
            //If default setting
            ratio = 1;
        }else{
            //The ratio cannot be less than 1
            ratio = Math.max(set_row_pixel, 1);
        }
        int width = bi.getWidth();
        int height = bi.getHeight();
        int minx = bi.getMinX();
        int miny = bi.getMinY();
        BufferedImage bufferedImage = new BufferedImage(width, height, IMAGE_TYPE);
        // Get Image context
        Graphics g = createGraphics(bufferedImage, width, height, ratio);
        for (int i = miny; i < height; i += ratio) {
            for (int j = minx; j < width; j += ratio) {
                //Get pixel
                int pixel = bi.getRGB(j, i);
                // Get RGB value of the pixel
                int red = (pixel & 0xff0000) >> 16;
                int green = (pixel & 0xff00) >> 8;
                int blue = (pixel & 0xff);
                //Get grey value
                float grey = clamp(contrast*(0.3f * red + 0.59f * green + 0.11f * blue) + brightness);
                int index = Math.round(grey * (ascii.length() + 1) / 255);
                String c = index >= ascii.length() ? " " : String.valueOf(ascii.charAt(index));
                if(type.equals("color")){
                    //If color option is selected
                    Color color = new Color(red,green,blue);
                    g.setColor(color);
                }
                g.drawString(c, j, i);
            }
        }
        g.dispose();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", out);
        return Base64.getEncoder().encodeToString(out.toByteArray());
    }

    static String Base64toImg(String base64) throws IOException {
        byte[] decode = Base64.getDecoder().decode(base64);
        for (int i = 0;i<decode.length;++i){//Adjust error byte
            if (decode[i]<0){
                decode[i]+=256;
            }
        }
        String path = "src\\Photopia\\temp\\temp.jpg";//Store image in this path
        OutputStream out = new FileOutputStream(path);//Create image
        out.write(decode);
        out.flush();
        out.close();
        return path;
    }

    private static Graphics createGraphics(BufferedImage image, int width,
                                           int height, int size) {
        Graphics g = image.createGraphics();
        g.setColor(null);
        g.fillRect(0, 0, width, height);// Fill background
        g.setColor(Color.BLACK);
        g.setFont(new Font("System", Font.PLAIN, size));
        return g;
    }

    private static float clamp(float rgb){
        //If grey value must be within the range of 0-255
        if(rgb>255){
            return 255;
        }else if(rgb<0){
            return 0;
        }
        return rgb;
    }
}
