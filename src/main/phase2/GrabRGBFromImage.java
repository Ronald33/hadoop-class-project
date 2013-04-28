package phase2;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.imageio.ImageIO;

import org.apache.hadoop.io.BytesWritable;

public class GrabRGBFromImage {

  public static void main(String[] args) throws IOException {
//    BufferedImage img = null;
//    try {
//      img = ImageIO.read(new File("data/one.jpg"));
//    } catch (IOException e) {
//    }
    
    
    byte[] bytes = {0,0,0,4};
    BytesWritable imgPixelBytes = new BytesWritable(bytes);
    
    byte[] imgBytes = imgPixelBytes.getBytes();
    ByteArrayInputStream inStream = new ByteArrayInputStream(imgBytes);
    
    ObjectInputStream ois = new ObjectInputStream(inStream);
    
    int intRead = ois.readInt();
    System.out.println("int read was: " + intRead);
    
    ois.close();
    
    
//    Color c = new Color(img.getRGB(1,1));
//    // Return individual red, green, and blue colors as values b/t 0 and 255
//    int red = c.getRed();
//    int green = c.getGreen();
//    int blue = c.getBlue();
//    
//    System.out.println("red: "+red);
//    System.out.println("green: "+green);
//    System.out.println("blue: "+blue);
//    
//    // Resize image to 3X3 pixels
//    BufferedImage scaledImg = new BufferedImage(3,3,BufferedImage.TYPE_INT_RGB);
//    Graphics2D g = scaledImg.createGraphics();
//    g.drawImage(img,0,0,3,3,null);
//    g.dispose();
//    File outputfile = new File("data/oneScaled.jpg");
//    try {
//      ImageIO.write(scaledImg,  "jpg", outputfile);
//    } catch (IOException e) {
//    }

  
  
  }

}