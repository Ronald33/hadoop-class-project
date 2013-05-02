package phase2;

import java.awt.Color;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
import org.apache.hadoop.io.BytesWritable;
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
  private static class PixelToToneMapper extends Mapper<NullWritable, BytesWritable, IntWritable, PairOfInts>{
    private static final IntWritable IMAGE_REGION = new IntWritable();
    private static final PairOfInts NOTE_VELOCITY = new PairOfInts();


    public void map(NullWritable key, BytesWritable bytes, Context context)
        throws IOException, InterruptedException {

      // Read in intermediate image data representation
      ByteArrayInputStream bytesStream = new ByteArrayInputStream(bytes.getBytes());
      ObjectInputStream ois = new ObjectInputStream(bytesStream);
      int pixel = -1;
      int regionCounter = 1;

      System.out.println("Starting loop over ObjectInputStream...");

      for(int i = 1; i <= 9; i++){
        LOG.info("Region counter #: " + regionCounter);
        if(regionCounter > 9) LOG.info("ERROR: Region Counter > 9:" + regionCounter);

        pixel = ois.readInt();

        MidiNote midiNote = Color2Music.convert(new Color(pixel));

        IMAGE_REGION.set(regionCounter);
        NOTE_VELOCITY.set(midiNote.getTone(), midiNote.getVelocity());
        context.write(IMAGE_REGION, NOTE_VELOCITY);

        regionCounter++;

      }

      System.out.println("Try to close ObjectInputStream....");
      ois.close();
      System.out.println("Closed ObjectInputStream....");
    }

    //      
    //      while(ois.available() > 0){
    //        LOG.info("Region counter #: " + regionCounter);
    //        if(regionCounter > 9) LOG.info("ERROR: Region Counter > 9:" + regionCounter);
    //
    //        pixel = ois.readInt();
    //
    //        MidiNote midiNote = Color2Music.convert(new Color(pixel));
    //
    //        IMAGE_REGION.set(regionCounter);
    //        NOTE_VELOCITY.set(midiNote.getTone(), midiNote.getVelocity());
    //        context.write(IMAGE_REGION, NOTE_VELOCITY);
    //
    //        regionCounter++;
    //      }
    //      
    //      System.out.println("Try to close ObjectInputStream....");
    //      ois.close();
    //      System.out.println("Closed ObjectInputStream....");
    //    }
  }


  // Reducer: sums up ...
  private static class PixelToToneReducer extends Reducer<IntWritable, PairOfInts, IntWritable, BytesWritable> {

    //    private static final int MIN_NOTE_LENGTH = 2;

    public void reduce(IntWritable key, Iterable<PairOfInts> values, Context context) 
        throws IOException, InterruptedException {

      ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
      ObjectOutputStream out = new ObjectOutputStream(byteOut);

      int tone = -1;
      int velocity = -1;

      for(PairOfInts pair : values){

        tone = pair.getLeftElement();
        velocity = pair.getRightElement();

        out.writeInt(tone);
        out.writeInt(velocity);


      }

      out.close();

      BytesWritable MIDI_EVENTS = new BytesWritable(byteOut.toByteArray());

      context.write(key, MIDI_EVENTS);

    }
  }

  public Image2MusicMR() {
  }

  private static final String INPUT = "input";
  private static final String OUTPUT = "output";

  /**
   * Runs this tool.
   */
  @SuppressWarnings("static-access")
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

    LOG.info("Tool: " + Image2MusicMR.class.getSimpleName());
    LOG.info(" - input path: " + inputPath);
    LOG.info(" - output path: " + outputPath);

    Configuration conf = getConf();
    Job job = Job.getInstance(conf);
    job.setJobName(Image2MusicMR.class.getName() + ":" + inputPath);
    job.setJarByClass(Image2MusicMR.class);

    FileInputFormat.addInputPath(job, new Path(inputPath));
    FileOutputFormat.setOutputPath(job, new Path(outputPath));

    job.setInputFormatClass(SequenceFileInputFormat.class);
    job.setOutputFormatClass(SequenceFileOutputFormat.class);

    job.setMapOutputKeyClass(IntWritable.class);
    job.setMapOutputValueClass(PairOfInts.class);

    // TODO : Not sure about the output class of this job.
    job.setOutputKeyClass(IntWritable.class);
    job.setOutputValueClass(BytesWritable.class);

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
    ToolRunner.run(new Image2MusicMR(), args);
  }

}
