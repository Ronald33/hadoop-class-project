package phase2;

import java.awt.Color;
import java.util.Random;


public class UsingColorToConvertRgbIntoMusicNotes {
  
  public static int LOWEST_NOTE = 12;

  /**
   * @param args
   */
  public static void main(String[] args) {

    float[] blackHSBValues = getHSBValues(Color.BLACK);
    float[] whiteHSBValues = getHSBValues(Color.WHITE);
    
    printHSBValues("Black", blackHSBValues);
    printHSBValues("White", whiteHSBValues);
    
    float[] orangeHSBValues = getHSBValues(Color.ORANGE);
    printHSBValues("Orange", orangeHSBValues);
    
    float[] redHSBValues = getHSBValues(Color.RED);
    printHSBValues("Red", redHSBValues);
    
    Random r = new Random();
    for(int i = 0; i < 1000; i = i + 25){
      float val = (float) (i / 1000.0);
      
      //float rand = r.nextFloat();
      int tone = hueFloatToToneInt(val);
      
      
    
      System.out.println("tone: " + tone);
      
      System.out.println();
//      System.out.println("Float: " + rand + " ==> tone: " + hueFloatToToneInt(rand));
    }
    
  }
  
  public static float[] getHSBValues(Color color){
    int redValue = color.getRed();
    int blueValue = color.getBlue();
    int greenValue = color.getGreen();
  
    return Color.RGBtoHSB(redValue, greenValue, blueValue, null); 
  }
  
  public static void printHSBValues(String colorName, float[] hsvals){
    System.out.println(colorName + " is:");
    
    System.out.println("\tHue: " + hsvals[0]);
    System.out.println("\tSat: " + hsvals[1]);
    System.out.println("\tBrightness:" + hsvals[2]);
  }
  
  /**
   * RGBtoHSB returns hue value as a float (I think between 0 and 1)
   * This float * 360 is the hue angle in the HSB model.
   * Take the floor or ceil of this angle to make and int
   * Then mod that number by 30 (there are 12 tones and 30 divides 360 12 times)
   * 
   * 
   * returned tone should be between 0 and 11 inclusive. 
   * @return
   */
  public static int hueFloatToToneInt(float hueFloat){
    
    System.out.println("hueFLoat: " + hueFloat);

    int hueAngle = (int) Math.floor(hueFloat * 360);
    System.out.println("hueAngle: " + hueAngle);
    
    int tone = hueAngle / 30;
    
    
    return tone + LOWEST_NOTE;
  }

}
