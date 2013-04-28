package phase2;

public class MidiNote {
  public int tone;
  public int velocity;
  
  public MidiNote(int t, int v){
    this.tone = t;
    this.velocity = v;
  }

  public int getTone() {
    return tone;
  }

  public int getVelocity() {
    return velocity;
  }
  
  
}
