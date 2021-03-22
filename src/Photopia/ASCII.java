package Photopia;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;

public interface ASCII {
    String ascii = "$@B%8&WM#*oahkbdpqwmZO0QLCJUYXzcvunxrjft/\\|()1{}[]?-_+~<>i!lI;:,\\\"^`'.";
    String base = "@#&$%*o!;.";
    /**
     * 图片类型
     */
    int IMAGE_TYPE = BufferedImage.TYPE_INT_RGB;

    static String txtToImageByBase64(BufferedImage bi) throws IOException {
        int width = bi.getWidth();
        int height = bi.getHeight();
        int minx = bi.getMinX();
        int miny = bi.getMinY();
        int speed = 7;
        BufferedImage bufferedImage = new BufferedImage(width, height, IMAGE_TYPE);
        // Get Image context
        Graphics g = createGraphics(bufferedImage, width, height, speed);
        for (int i = miny; i < height; i += speed) {
            for (int j = minx; j < width; j += speed) {
                int pixel = bi.getRGB(j, i); // Change the pixel to RGB ASCii
                int red = (pixel & 0xff0000) >> 16;
                int green = (pixel & 0xff00) >> 8;
                int blue = (pixel & 0xff);
                float gray = 0.299f * red + 0.578f * green + 0.114f * blue;
                int index = Math.round(gray * (ascii.length() + 1) / 255);
                String c = index >= ascii.length() ? " " : String.valueOf(ascii.charAt(index));
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
}
