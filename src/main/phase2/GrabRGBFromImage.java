package phase2;

import java.awt.Color;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class GrabRGBFromImage {

  public static void main(String[] args) {
    BufferedImage img = null;
    try {
      img = ImageIO.read(new File("magenta.jpg"));
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
  }

}