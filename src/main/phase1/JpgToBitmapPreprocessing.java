package phase1;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;

public class JpgToBitmapPreprocessing extends Configured implements Tool {
  private static final Logger LOG = Logger.getLogger(JpgToBitmapPreprocessing.class);

  
  public JpgToBitmapPreprocessing() {}
  
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
    // TODO Auto-generated method stub
    ToolRunner.run(new JpgToBitmapPreprocessing(), args);
  }

}
