package phase2;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;

import edu.umd.cloud9.io.pair.PairOfInts;

public class ImageToIntermediateMusicTranslation extends Configured implements Tool {
  private static final Logger LOG = Logger.getLogger(ImageToIntermediateMusicTranslation.class);

  //Mapper: emits ...
  private static class PixelToToneMapper extends Mapper<LongWritable, Text, IntWritable, PairOfInts>{
    private static final IntWritable IMAGE_REGION = new IntWritable();
    private static final PairOfInts NOTE = new PairOfInts();


//    public void map(LongWritable key, Text value ){}
  }
  
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
