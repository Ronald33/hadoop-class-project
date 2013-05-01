package phase2;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
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
      img = ImageIO.read(new File("images/collection1-thumbs/thumb-sample-01.jpg"));
    } catch (IOException e) {
    
    }

    // Get the RGB integer for each pixel in 3X3
    for(int j = 0; j < 3; j++) {
      for(int i = 0; i < 3; i++) {
        System.out.println("Reading pixel: i = " + i + ", j = " + j);
        out.writeInt(img.getRGB(i, j));
      }
    }
    out.close();

    // Make a BytesWritable and with the output stream data
    BytesWritable BW = new BytesWritable(bOut.toByteArray());
    bOut.close();


    byte[] imgBytes = BW.getBytes();
    ByteArrayInputStream inStream = new ByteArrayInputStream(imgBytes);

    ObjectInputStream ois = new ObjectInputStream(inStream);

    int pixel = -1;
    Color c = null;
    while(ois.available() > 0){
      pixel = ois.readInt();
      c = new Color(pixel);
     
      System.out.print("R: " + c.getRed() + "\t");
      System.out.print("G: " +  c.getGreen() + "\t");
      System.out.print("B: "+  c.getBlue());
      System.out.println();
    }

    ois.close();

  }

}