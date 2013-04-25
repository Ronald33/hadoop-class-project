package phase2;

import java.awt.Color;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


public class UsingColorToConvertRgbIntoMusicNotes {

  public static final int LOWEST_NOTE = 12;
  public static final int OCTAVE_RANGE = 9;
  public static final int C = 0;
  public static final int C_SHARP = 1;
  public static final int D = 2;
  public static final int D_SHARP = 3;
  public static final int E = 4;
  public static final int F = 5;
  public static final int F_SHARP = 6;
  public static final int G = 7;
  public static final int G_SHARP = 8;
  public static final int A = 9;
  public static final int A_SHARP = 10;
  public static final int B = 11;

  /**
   * The array maps a particular region (a 30 degree slice of a color wheel)
   *    to the musical note it should represent in the 
   *    virtuosoism model.
   */
  public static final int[] regionToMidiNote = {C, G, D, A, E, B, 
                                                F_SHARP, C_SHARP, G_SHARP, D_SHARP, A_SHARP, F};

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
      int tone = hueFloatToNoteInt(val);

      System.out.println("tone: " + getNoteFromToneNumber(tone));
      System.out.println();
    }

  }
  
  public static int getMidiToneFromHSValues(float hue, float sat){
    int note = hueFloatToNoteInt(hue);
    
    return 0;
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
   * Give a midi note number (0 - 120) returns the String name of the note
   * @param toneNumber
   * @return
   */
  public static String getNoteFromToneNumber(int toneNumber){
    int baseTone = toneNumber % 12;
    if(baseTone < 0 || baseTone > 11) {
      return "Invalid Tone Number";
    }
    
    switch(baseTone) {
      case 0: return "C";
      case 1: return "C#";
      case 2: return "D";
      case 3: return "D#";
      case 4: return "E";
      case 5: return "F";
      case 6: return "F#";
      case 7: return "G";
      case 8: return "G#";
      case 9: return "A";
      case 10: return "A#";
      //Case 11
      default: return "B";
    }
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
  public static int hueFloatToNoteInt(float hueFloat){

    System.out.println("hueFLoat: " + hueFloat);

    int hueAngle = (int) Math.floor(hueFloat * 360);
    System.out.println("hueAngle: " + hueAngle);

    int region = hueAngle / 30;
    int tone = regionToMidiNote[region] + LOWEST_NOTE;
    
    
    return tone;

  }

  /**
   * Given saturation value
   *   returns 0 - 8 indicating which octave
   *   There are 9 octaves in the audible range
   * @return
   */
  public static int saturationFloatToOctaveInt(float sat){
    return (int) Math.floor(sat / (1.0 / OCTAVE_RANGE));
  }

}
