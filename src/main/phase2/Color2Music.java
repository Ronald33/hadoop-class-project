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
    
    int tone = getMidiToneFromHSValues(hue, saturation, brightness);
    
//    
//    int velocity = saturationFloatToVelocityInt(saturation);
//    if(velocity > MIN_VELOCITY_THRESHOLD){
//      tone = getMidiToneFromHSValues(hue, saturation, brightness);
//    }

    return new MidiNote(tone, 127);
  }
  
  private static int getMidiToneFromHSValues(float h, float s, float b){
    int note = hueFloatToNoteInt(h);
    int octave = brightessAndSaturationToOctaveInt(s, b);
    int tone = note + (12 * octave);
    return tone;
  }
  
  private static int brightessAndSaturationToOctaveInt(float s, float b){
    
    
    System.out.print("b: " + b + "\ts: " + s );
    
    double bWeight = 0.5;
    double sWeight = 1 - bWeight;

    double maxSContribution = sWeight * OCTAVE_RANGE;
    double maxBContribution = bWeight * OCTAVE_RANGE;
    
//    System.out.println("- maxsat: " + maxSContribution);
//    System.out.println("- maxbri: " + maxBContribution);
    
    
    
    // faux "absolute value"
    // s below 0.5 maps to (1 - s) (ie put it in the 0.5 -> 1.0 range
    double satAbs = s < 0.5 ? (1 - s) : s;
    double sValue = Math.floor(satAbs / (1.0 / maxSContribution));

 // Number between 0 and (maxBContribution - 1)
    double bValue = Math.floor(b / (1.0 / maxBContribution));
    
//    System.out.println(" --- sValue: " + sValue);
//    System.out.println(" --- bValue: " + bValue);
    
    int octave = (int) Math.floor(bValue + sValue);
    
    
    
//    if(b < 0.2) octave = 0;
    
    
    System.out.println(" -- \tOctave: " + octave );
//    System.out.println();
    return octave;
    
//    int brightnessComponent = (int) Math.floor(b / (1.0 / OCTAVE_RANGE));
//    int satOn = 0;
//    
//    if(s < 0.33 || s > 0.67 ){
//      satOn = 1;
//    }
//    
////    int saturationComponent = (int) Math.floor((1.0 - s) / (1.0 / OCTAVE_RANGE));
//    
//    int retVal = (int) ((5/(float)(OCTAVE_RANGE)) * brightnessComponent + (satOn/(float)(OCTAVE_RANGE)));  
//    
//    return retVal;
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
  
  public static void main(String[] args){
    
    for(int b = 0; b < 1000; b = b + 100){
      for(int s = 0; s < 1000; s = s + 100){
        
        float sat = s / (float) 1000.0; 
        float bri = b / (float) 1000.0; 
        
        int octave = brightessAndSaturationToOctaveInt(sat, bri);
        
//        System.out.println("b: " + b + ",  s : " + s +  "  Octave: " + octave);
        
      }
    }

//    
//    System.out.println("Out of Photoshop: ");
//    MidiNote a = Color2Music.convert(new Color(237, 222, 5));
//    System.out.println("a : " + a.getTone());
//    System.out.println("a vel : " + a.getVelocity());
//    System.out.println(getNoteFromToneNumber(103));
////  
//    MidiNote b = Color2Music.convert(new Color(0, 255, 83));
//    System.out.println("b : " + b.getTone());
//    System.out.println("vel : " + b.getVelocity());
//    
//    
//    MidiNote c = Color2Music.convert(new Color(255, 0, 19));
//    System.out.println("c : " + c.getTone());
//    System.out.println("vel : " + c.getVelocity());
//    
//    MidiNote d = Color2Music.convert(new Color(0, 0, 0));
//    System.out.println("d : " + d.getTone());
//    System.out.println("vel : " + d.getVelocity());
//    
//    MidiNote e = Color2Music.convert(new Color(255, 255, 255));
//    System.out.println("e : " + e.getTone());
//    System.out.println("vel : " + e.getVelocity());
//    
//    
//    
//    System.out.println("Thumnail values : ");
//    MidiNote a2 = Color2Music.convert(new Color(0, 0, 253));
//    System.out.println("a : " + a2.getTone());
//    System.out.println("a vel : " + a2.getVelocity());
//  
//    MidiNote b2 = Color2Music.convert(new Color(0, 255, 1));
//    System.out.println("b : " + b2.getTone());
//    System.out.println("vel : " + b2.getVelocity());
//    
//    
//    MidiNote c2 = Color2Music.convert(new Color(253, 0, 1));
//    System.out.println("c : " + c2.getTone());
//    System.out.println("vel : " + c2.getVelocity());
//    
//    MidiNote d2 = Color2Music.convert(new Color(0, 0, 0));
//    System.out.println("d : " + d2.getTone());
//    System.out.println("vel : " + d2.getVelocity());
//    
//    MidiNote e2 = Color2Music.convert(new Color(255, 255, 255));
//    System.out.println("e2 : " + e2.getTone());
//    System.out.println("vel : " + e2.getVelocity());
  }

}
