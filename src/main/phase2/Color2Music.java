package phase2;

import java.awt.Color;



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

  public static MidiNote convert(Color c){
    
    float[] hsvals = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
    float hue = hsvals[0];
    float saturation = hsvals[1];
    float brightness = hsvals[2];
    
    int tone = -1;
    int velocity = saturationFloatToVelocityInt(saturation);
    if(velocity > MIN_VELOCITY_THRESHOLD){
      tone = getMidiToneFromHSValues(hue, brightness);
    }

    return new MidiNote(tone, velocity);
  }
  
  
  /**
   * Returns the tone given the hue and saturation input values.
   * 
   * @param hue
   * @param brightness
   * @return
   */
  private static int getMidiToneFromHSValues(float hue, float brightness){
    int note = hueFloatToNoteInt(hue);
    int octave = brightnessFloatToOctaveInt(brightness);
    int tone = note + (12 * octave);
    return tone;
  }
  
  
  /**
   * Returns the velocity (MIDI) given the saturation value
   * @param saturation
   * @return
   */
  private static int saturationFloatToVelocityInt(float saturation){
    int velocity = -1;
    if(saturation > SATURATION_THRESHOLD_4_MAX_VELOCITY) velocity = 127;
    else {
      int positionInDiscreteVelocityRange = (int) Math.floor(saturation / (1.0 / (VELOCITY_MAX - VELOCITY_MIN)));
      velocity = positionInDiscreteVelocityRange + VELOCITY_MIN;
    }
    
    return velocity;
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

  /**
   * Given brightness value
   *   returns 0 - 7 indicating which octave
   *   There are 8 octaves in the range we're using
   * @return
   */
  private static int brightnessFloatToOctaveInt(float brightness){
    return (int) Math.floor(brightness / (1.0 / OCTAVE_RANGE));
  }

}
