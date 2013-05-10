package phase2;

public class Color2Music {

  public static final int LOWEST_NOTE = 36;
  public static final int OCTAVE_RANGE = 6;
  public static final int VELOCITY_MIN = 60;
  public static final int VELOCITY_MAX = 127;
  /** HSB Value for brightness.  Above this threshold gets max velocity: 127 */
  public static final double SATURATION_THRESHOLD_4_MAX_VELOCITY = 0.9;
  /** Midi Velocity value. Below this threshold and tone gets -1 "rest" */
  public static final double MIN_VELOCITY_THRESHOLD = 40;
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

  private static float[] hsvConversionArray = new float[3];
  
  /**
   * Convert an RGB pixel to an HSV int array
   * @param pixel
   */
  private static void RGBtoHSV(int pixel) {
    float r = (pixel & 0xFF)/255.0F;
    float g = (pixel >> 8)/255.0F;
    float b = ((pixel >> 16) & 0xFF)/255.0F;
    
    float min = Math.min(r,Math.min(g,b));
    float max = Math.max(r,Math.max(g,b));
    
    if (Math.min(r,Math.min(g,b))==Math.max(r,Math.max(g,b))) {
     hsvConversionArray[0] = 0;
     hsvConversionArray[1] = 0;
     hsvConversionArray[2] = Math.min(r,Math.min(g,b));
     return;
    }

    // Colors other than black-gray-white:
    float d = (r==min) ? g-b : ((b==min) ? r-g : b-r);
    float h = (r==min) ? 3 : ((b==min) ? 1 : 5);
    hsvConversionArray[0] = 60*(h - d/(max - min));
    hsvConversionArray[1] = (max - min)/max;
    hsvConversionArray[2] = max;
    return;
  }
  
  public static MidiNote convert(int pixel){
    RGBtoHSV(pixel);
    
    float hue = hsvConversionArray[0];
    float saturation = hsvConversionArray[1];
    float brightness = hsvConversionArray[2];
    
    int tone = getMidiToneFromHSValues(hue, saturation, brightness);
    return new MidiNote(tone, 127);
  }
  
  private static int getMidiToneFromHSValues(float h, float s, float b){
    int note = hueFloatToNoteInt(h);
    int octave = brightessAndSaturationToOctaveInt(s, b);
    int tone = note + (12 * octave);
    return tone;
  }
  
  private static int brightessAndSaturationToOctaveInt(float s, float b){
    
    double bWeight = 0.5;
    double sWeight = 1 - bWeight;

    double maxSContribution = sWeight * OCTAVE_RANGE;
    double maxBContribution = bWeight * OCTAVE_RANGE;
    
    double satAbs = s < 0.5 ? (1 - s) : s;
    double sValue = Math.floor(satAbs / (1.0 / maxSContribution));
    double bValue = Math.floor(b / (1.0 / maxBContribution));
    
    int octave = (int) Math.floor(bValue + sValue);
    
    return octave;
   
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
  private static int hueFloatToNoteInt(float hueFloat){
    int hueAngle = (int) Math.floor(hueFloat * 360);
    int region = hueAngle / 30;
    int tone = regionToMidiNote[region] + LOWEST_NOTE;

    return tone;
  }

}
