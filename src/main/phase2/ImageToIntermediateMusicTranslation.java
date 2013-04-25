package phase2;

import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;

import edu.umd.cloud9.io.pair.PairOfInts;

public class ImageToIntermediateMusicTranslation extends Configured implements Tool {
  private static final Logger LOG = Logger.getLogger(ImageToIntermediateMusicTranslation.class);

  
  // Mapper: emits ...
  private static class PixelToToneMapper extends Mapper<IntWritable, IntWritable, IntWritable, IntWritable>{
    private static final IntWritable IMAGE_REGION = new IntWritable();
    private static final PairOfInts NOTE = new PairOfInts();


    public void map(IntWritable key, IntWritable value, Context context)
      throws IOException, InterruptedException {
      
      // Read in intermediate image data representation
      
    }
  }
  
  
  // Reducer: sums up ...
  private static class PixelToToneReducer extends Reducer<IntWritable, IntWritable, IntWritable, PairOfInts> {
    
    private static final IntWritable SUM = new IntWritable();
    
    public void reduce(IntWritable key, Iterable<IntWritable> values, Context context) 
      throws IOException, InterruptedException {
      
      // Create MIDI output
      
    }
  }
  
  public ImageToIntermediateMusicTranslation() {
  }
  
  private static final String INPUT = "input";
  private static final String OUTPUT = "output";
  
  /**
   * Runs this tool.
   */
  @Override
  public int run(String[] args) throws Exception {
    
    Options options = new Options();
    
    options.addOption(OptionBuilder.withArgName("path").hasArg()
        .withDescription("input path").create(INPUT));
    options.addOption(OptionBuilder.withArgName("path").hasArg()
        .withDescription("output path").create(OUTPUT));

    CommandLine cmdline;
    CommandLineParser parser = new GnuParser();

    try {
      cmdline = parser.parse(options, args);
    } catch (ParseException exp) {
      System.err.println("Error parsing command line: " + exp.getMessage());
      return -1;
    }

    if (!cmdline.hasOption(INPUT) || !cmdline.hasOption(OUTPUT)) {
      System.out.println("args: " + Arrays.toString(args));
      HelpFormatter formatter = new HelpFormatter();
      formatter.setWidth(120);
      formatter.printHelp(this.getClass().getName(), options);
      ToolRunner.printGenericCommandUsage(System.out);
      return -1;
    }

    String inputPath = cmdline.getOptionValue(INPUT);
    String outputPath = cmdline.getOptionValue(OUTPUT);
    
    LOG.info("Tool: " + ImageToIntermediateMusicTranslation.class.getSimpleName());
    LOG.info(" - input path: " + inputPath);
    LOG.info(" - output path: " + outputPath);
    
    Configuration conf = getConf();
    Job job = Job.getInstance(conf);
    job.setJobName(ImageToIntermediateMusicTranslation.class.getName() + ":" + inputPath);
    job.setJarByClass(ImageToIntermediateMusicTranslation.class);
    
    FileInputFormat.addInputPath(job, new Path(inputPath));
    FileOutputFormat.setOutputPath(job, new Path(outputPath));
    
    job.setInputFormatClass(SequenceFileInputFormat.class);
    job.setOutputFormatClass(TextOutputFormat.class);
    
    job.setMapOutputKeyClass(IntWritable.class);
    job.setMapOutputValueClass(IntWritable.class);
    
    job.setOutputKeyClass(IntWritable.class);
    job.setOutputValueClass(IntWritable.class);

    job.setMapperClass(PixelToToneMapper.class);
    job.setReducerClass(PixelToToneReducer.class);

    // Delete the output directory if it exists already.
    FileSystem.get(conf).delete(new Path(outputPath), true);

    job.waitForCompletion(true);
    
    return 0;
  }

  /**
   * Dispatches command-line arguments to the tool via the {@code ToolRunner}.
   */
  public static void main(String[] args) throws Exception{
    ToolRunner.run(new ImageToIntermediateMusicTranslation(), args);
  }

}
