package phase2;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GrabRGBFromImage {

  public static void main(String[] args) {
    BufferedImage img = null;
    try {
      img = ImageIO.read(new File("data/one.jpg"));
    } catch (IOException e) {
    }
    
    
    Color c = new Color(img.getRGB(1,1));
    // Return individual red, green, and blue colors as values b/t 0 and 255
    int red = c.getRed();
    int green = c.getGreen();
    int blue = c.getBlue();
    
    System.out.println("red: "+red);
    System.out.println("green: "+green);
    System.out.println("blue: "+blue);
    
    // Resize image to 3X3 pixels
    BufferedImage scaledImg = new BufferedImage(3,3,BufferedImage.TYPE_INT_RGB);
    Graphics2D g = scaledImg.createGraphics();
    g.drawImage(img,0,0,3,3,null);
    g.dispose();
    File outputfile = new File("data/oneScaled.jpg");
    try {
      ImageIO.write(scaledImg,  "jpg", outputfile);
    } catch (IOException e) {
    }
  }

}