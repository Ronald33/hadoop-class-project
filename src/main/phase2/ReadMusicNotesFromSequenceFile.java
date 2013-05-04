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
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;

import edu.umd.cloud9.io.array.ArrayListOfIntsWritable;

/** This program reads midi notes stored as a bytes array in a sequence file
 * The notes are in the format tone number followed by velocity represented as ints.
 * 
 * 
 * There should only be one key-value pair in this sequence file, since it hods all the music
 * for one image region (ie is the result of one reducer's work)
 * 
 * Later on we can have one reducer do all the work for each image region, 
 *  so that one sequence file has all the parts (but lets start with this for now)
 */
public class ReadMusicNotesFromSequenceFile {

  private static final String BASE_PATH = "base";
  private static final String MIN_LENGTH = "note_length";
  private static final Logger LOG = Logger.getLogger(ReadMusicNotesFromSequenceFile.class);

  @SuppressWarnings("static-access")
  public static void main(String[] args) throws IOException, InstantiationException, IllegalAccessException {

  

    // The sequence file uses <K, V> of types <NullWritable, BytesWritable>

    // Input and output directories are specified in the command line
    Options options = new Options();
    options.addOption(OptionBuilder.withArgName("path").hasArg()
        .withDescription("base").create(BASE_PATH));
    options.addOption(OptionBuilder.withArgName("note_length").hasArg()
        .withDescription("note_length").create(MIN_LENGTH));
    
    CommandLine cmdline = null;
    CommandLineParser parser = new GnuParser();

    try {
      cmdline = parser.parse(options, args);
    } catch (ParseException exp) {
      System.err.println("Error parsing command line: " + exp.getMessage());
      System.exit(-1);
    }

    if (!cmdline.hasOption(BASE_PATH) || !cmdline.hasOption(MIN_LENGTH)) {
      System.out.println("args: " + Arrays.toString(args));
      HelpFormatter formatter = new HelpFormatter();
      formatter.setWidth(120);
      formatter.printHelp(WriteImagesToSequenceFile.class.getName(), options);
      ToolRunner.printGenericCommandUsage(System.out);
      System.exit(-1);
    }

    String basePath = cmdline.getOptionValue(BASE_PATH);
    int minLength = Integer.parseInt(cmdline.getOptionValue(MIN_LENGTH));
    String inputPath = basePath + "/music/part-r-00000";
    String outputPath = basePath + "/midi";
    

    LOG.info("Tool name: " + WriteImagesToSequenceFile.class.getSimpleName());
    LOG.info(" - basePath: " + basePath);
    LOG.info(" - inputPath: " + inputPath);
    LOG.info(" - output: " + outputPath);

    Configuration config = new Configuration();
    Path path = new Path(inputPath);
    
    @SuppressWarnings("deprecation")
    SequenceFile.Reader reader = new SequenceFile.Reader(FileSystem.get(config), path, config);
    IntWritable key = (IntWritable) reader.getKeyClass().newInstance();
    ArrayListOfIntsWritable notesArray = (ArrayListOfIntsWritable) reader.getValueClass().newInstance();
    
    // Make midi directory if its not there
    //  Hadoop does this for us, but we're writing with MidiFile this time
    FileSystem.get(config).mkdirs(new Path(outputPath));
    
    
    int partCounter = 1;
    
    //Read all the lines
    while(reader.next(key, notesArray)){
      int tone = -1;
      int velocity = -1;
      
      MidiFile mf = new MidiFile();

      // Read note information 2 ints at a time
      for(int i = 0; i < notesArray.size() - 1; i = i + 2){
        
        tone = notesArray.get(i);
        velocity = notesArray.get(i + 1);
        mf.noteOnOffNow(minLength, tone, velocity);
        
      }
      
      mf.writeToFile(outputPath + "/midiout" + partCounter + ".mid");
      partCounter++;
    }
    
    reader.close();
    
  }  

}


