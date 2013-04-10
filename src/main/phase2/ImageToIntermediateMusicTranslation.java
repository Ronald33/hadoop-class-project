package phase2;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;

public class ImageToIntermediateMusicTranslation extends Configured implements Tool {
  private static final Logger LOG = Logger.getLogger(ImageToIntermediateMusicTranslation.class);

  
  public ImageToIntermediateMusicTranslation() {}
  
  /**
   * Runs this tool.
   */
  @Override
  public int run(String[] arg0) throws Exception {
    // TODO Auto-generated method stub
    return 0;
  }

  /**
   * Dispatches command-line arguments to the tool via the {@code ToolRunner}.
   */
  public static void main(String[] args) throws Exception{
    ToolRunner.run(new ImageToIntermediateMusicTranslation(), args);
  }

}
