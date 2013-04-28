package phase2;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.*;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import javax.imageio.ImageIO;

import org.apache.hadoop.io.BytesWritable;

public class GrabRGBFromImage {

  public static void main(String[] args) throws IOException {
    BufferedImage img = null;
    ByteArrayOutputStream bOut = new ByteArrayOutputStream();
    ObjectOutput out = new ObjectOutputStream(bOut);
    
    // Read an image from file
    try {
      img = ImageIO.read(new File("data/one.jpg"));
    } catch (IOException e) {
    }
    
    // Get the RGB integer for each pixel in 3X3
    for(int i=1; i<4; i++) {
      for(int j=1; j<4; j++) {
        out.writeInt(img.getRGB(i, j));
      }
    }
    out.close();
    
    // Make a BytesWritable and with the output stream data
    BytesWritable BW = new BytesWritable(bOut.toByteArray());
    bOut.close();
    
    
    Color c = new Color(img.getRGB(1,1));
    // Return individual red, green, and blue colors as values b/t 0 and 255
    int red = c.getRed();
    int green = c.getGreen();
    int blue = c.getBlue();
    
    System.out.println("red: "+red);
    System.out.println("green: "+green);
    System.out.println("blue: "+blue);
    
    // Resize image
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