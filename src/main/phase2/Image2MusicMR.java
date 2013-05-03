package phase2;

import java.awt.Color;
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
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;

import edu.umd.cloud9.io.array.ArrayListOfIntsWritable;
import edu.umd.cloud9.io.pair.PairOfInts;

public class Image2MusicMR extends Configured implements Tool {
  private static final Logger LOG = Logger.getLogger(Image2MusicMR.class);


  // Mapper: emits ...
  /**
   * Input Key: Arbitrary Number
   * Input Value: BytesWritable (representing an image) byte array dumped from a bufferedimage
   *
   * Output Key: Region Number (IntWritable)
   * OutputValue: tone x velocity pair (PairOfIntWriable)
   *
   */
  private static class PixelToToneMapper extends Mapper<NullWritable, ArrayListOfIntsWritable, IntWritable, PairOfInts>{
    private static final IntWritable IMAGE_REGION = new IntWritable();
    private static final PairOfInts NOTE_VELOCITY = new PairOfInts();


    public void map(NullWritable key, ArrayListOfIntsWritable pixels, Context context)
        throws IOException, InterruptedException {

      // Read in intermediate image data representation
      int pixel = -1;
      int regionCounter = 1;
      MidiNote midiNote = null;

      System.out.println("Starting loop over ObjectInputStream...");
      
      for(int i = 0; i < pixels.size(); i++){
        LOG.info("Region counter #: " + regionCounter);
        if(regionCounter > 9) LOG.info("ERROR: Region Counter > 9:" + regionCounter);

        pixel = pixels.get(i);
        midiNote = Color2Music.convert(new Color(pixel));

        IMAGE_REGION.set(regionCounter);
        NOTE_VELOCITY.set(midiNote.getTone(), midiNote.getVelocity());
        context.write(IMAGE_REGION, NOTE_VELOCITY);

        regionCounter++;        
      }

    }

  }


  // Reducer: sums up ...
  private static class PixelToToneReducer extends Reducer<IntWritable, PairOfInts, IntWritable, ArrayListOfIntsWritable> {

    //    private static final int MIN_NOTE_LENGTH = 2;

    public void reduce(IntWritable key, Iterable<PairOfInts> values, Context context) 
        throws IOException, InterruptedException {

      int tone = -1;
      int velocity = -1;
      ArrayListOfIntsWritable musicInfo = new ArrayListOfIntsWritable();
      
      for(PairOfInts pair : values){

        tone = pair.getLeftElement();
        velocity = pair.getRightElement();

        musicInfo.add(tone);
        musicInfo.add(velocity);

      }

      context.write(key, musicInfo);

    }
  }

  public Image2MusicMR() {
  }

  private static final String BASE_PATH = "base";
//  private static final String OUTPUT = "output";

  /**
   * Runs this tool.
   */
  @SuppressWarnings("static-access")
  @Override
  public int run(String[] args) throws Exception {

    Options options = new Options();

    options.addOption(OptionBuilder.withArgName("path").hasArg()
        .withDescription("base").create(BASE_PATH));
//    options.addOption(OptionBuilder.withArgName("path").hasArg()
//        .withDescription("output path").create(OUTPUT));

    CommandLine cmdline;
    CommandLineParser parser = new GnuParser();

    try {
      cmdline = parser.parse(options, args);
    } catch (ParseException exp) {
      System.err.println("Error parsing command line: " + exp.getMessage());
      return -1;
    }

    if (!cmdline.hasOption(BASE_PATH)) {
      System.out.println("args: " + Arrays.toString(args));
      HelpFormatter formatter = new HelpFormatter();
      formatter.setWidth(120);
      formatter.printHelp(this.getClass().getName(), options);
      ToolRunner.printGenericCommandUsage(System.out);
      return -1;
    }

    String basePath = cmdline.getOptionValue(BASE_PATH);
//    String outputPath = cmdline.getOptionValue(OUTPUT);

    LOG.info("Tool: " + Image2MusicMR.class.getSimpleName());
    LOG.info(" - basePath: " + basePath);
//    LOG.info(" - output path: " + outputPath);

    Configuration conf = getConf();
    Job job = Job.getInstance(conf);
    job.setJobName(Image2MusicMR.class.getName() + ":" + basePath);
    job.setJarByClass(Image2MusicMR.class);

    FileInputFormat.addInputPath(job, new Path(basePath + "/sequence"));
    FileOutputFormat.setOutputPath(job, new Path(basePath + "/music"));

    job.setInputFormatClass(SequenceFileInputFormat.class);
    job.setOutputFormatClass(SequenceFileOutputFormat.class);

    job.setMapOutputKeyClass(IntWritable.class);
    job.setMapOutputValueClass(PairOfInts.class);

    // TODO : Not sure about the output class of this job.
    job.setOutputKeyClass(IntWritable.class);
    job.setOutputValueClass(ArrayListOfIntsWritable.class);

    job.setMapperClass(PixelToToneMapper.class);
    job.setReducerClass(PixelToToneReducer.class);

    // Delete the output directory if it exists already.
    FileSystem.get(conf).delete(new Path(basePath + "/music"), true);

    job.waitForCompletion(true);

    return 0;
  }


  /**
   * Dispatches command-line arguments to the tool via the {@code ToolRunner}.
   */
  public static void main(String[] args) throws Exception{
    ToolRunner.run(new Image2MusicMR(), args);
  }

}
