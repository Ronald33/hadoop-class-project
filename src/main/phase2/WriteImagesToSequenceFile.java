package phase2;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.util.ToolRunner;

import edu.umd.cloud9.io.array.ArrayListOfIntsWritable;

public class WriteImagesToSequenceFile {

  private static final String BASE_PATH = "base";

  private WriteImagesToSequenceFile() {}

  @SuppressWarnings({ "static-access", "deprecation" })
  public static void main(String[] args) throws Exception {

    // This program reads each 3X3 image from a directory 
    // and writes its RGB data to a sequence file 
    // where each line encodes one image.

    // The sequence file uses <K, V> of types <NullWritable, BytesWritable>

    // Input and output directories are specified in the command line
    Options options = new Options();
    options.addOption(OptionBuilder.withArgName("path").hasArg()
        .withDescription("base").create(BASE_PATH));

    CommandLine cmdline = null;
    CommandLineParser parser = new GnuParser();

    try {
      cmdline = parser.parse(options, args);
    } catch (ParseException exp) {
      System.err.println("Error parsing command line: " + exp.getMessage());
      System.exit(-1);
    }

    if (!cmdline.hasOption(BASE_PATH)) {
      System.out.println("args: " + Arrays.toString(args));
      HelpFormatter formatter = new HelpFormatter();
      formatter.setWidth(120);
      formatter.printHelp(WriteImagesToSequenceFile.class.getName(), options);
      ToolRunner.printGenericCommandUsage(System.out);
      System.exit(-1);
    }

    String basePath = cmdline.getOptionValue(BASE_PATH);
    String inputPath = basePath + "/thumbs";
    String outputPath = basePath + "/sequence";
        
    Configuration conf = new Configuration();
    FileSystem fs = FileSystem.get(conf);

    FileStatus[] fss = fs.globStatus(new Path(inputPath + "/*.jpg"));
    System.out.println("fss length: " + fss.length);
    
    Path outPath = new Path(outputPath);
    BufferedImage img = null;
    SequenceFile.Writer writer = null;

    try {
      System.out.println("inside try");
      writer = SequenceFile.createWriter(fs, conf, outPath, NullWritable.class, ArrayListOfIntsWritable.class);

      // Read each image and write its data to the sequence file
      for (FileStatus status : fss) {
        System.out.println("Inside FileStatus iterator");
        ArrayListOfIntsWritable pixelData = new ArrayListOfIntsWritable();

        // FIXME : substring removes "file:" from the path string
        img = ImageIO.read(new File(status.getPath().toString().substring(5)));


        // Get the RBG integer for each pixel in 3X3
        for(int j = 0; j < 3; j++) {
          for(int i = 0; i < 3; i++) {
            //System.out.println("Reading pixel: i = " + i + ", j = " + j);
            pixelData.add(img.getRGB(i,j));
          }
        }

        // Write to the sequence file
        writer.append(NullWritable.get(), pixelData);
      } 
    } finally {
      IOUtils.closeStream(writer);
    }
    //    return 0;
  } 

}


