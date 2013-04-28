package phase2;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
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
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;

public class WriteImagesToSequenceFile {
  
  private static final String INPUT = "input";
  private static final Logger LOG = Logger.getLogger(WriteImagesToSequenceFile.class);

  @SuppressWarnings("static-access")
  public static void main(String[] args) throws IOException {
  
    // This program reads each 3X3 image from a directory 
    // and writes its RGB data to a sequence file 
    // where each line encodes one image.
    
    // The sequence file uses <K, V> of types <NullWritable, BytesWritable>
    
    // The image directory is input from the command line!
    Options options = new Options();
    options.addOption(OptionBuilder.withArgName("path").hasArg()
        .withDescription("input path").create(INPUT));
    
    CommandLine cmdline = null;
    CommandLineParser parser = new GnuParser();

    try {
      cmdline = parser.parse(options, args);
    } catch (ParseException exp) {
      System.err.println("Error parsing command line: " + exp.getMessage());
      System.exit(-1);
    }

    if (!cmdline.hasOption(INPUT)) {
      System.out.println("args: " + Arrays.toString(args));
      HelpFormatter formatter = new HelpFormatter();
      formatter.setWidth(120);
      formatter.printHelp(WriteImagesToSequenceFile.class.getName(), options);
      ToolRunner.printGenericCommandUsage(System.out);
      System.exit(-1);
    }
    
    String inputPath = cmdline.getOptionValue(INPUT);
    
    LOG.info("Tool name: " + WriteImagesToSequenceFile.class.getSimpleName());
    LOG.info(" - input: " + inputPath);
    
    Configuration conf = new Configuration();
      FileSystem fs = FileSystem.get(conf);
      FileStatus[] fss = fs.globStatus(new Path(inputPath + "*.jpg"));
      BufferedImage img = null;
      
      // Read each image and write its data to the sequence file
      for (FileStatus status : fss) {
        Path path = status.getPath();
        SequenceFile.Writer writer = null;
        try { 
          writer = SequenceFile.createWriter(fs, conf, path, NullWritable.class, BytesWritable.class);
          ByteArrayOutputStream bOut = new ByteArrayOutputStream();
          ObjectOutput out = new ObjectOutputStream(bOut);
          
          // I'm not entirely sure status.toString() will work...
          img = ImageIO.read(new File(status.toString()));
          
          // Get the RBG integer for each pixel in 3X3
          for(int i = 0; i < 3; i++) {
            for(int j = 0; j < 3; j++) {
              System.out.println("Reading pixel: i = " + i + ", j = " + j);
              out.writeInt(img.getRGB(i, j));
            }
          }
          out.close();
          
          // Make a BytesWritable with the output stream data
          BytesWritable BW = new BytesWritable(bOut.toByteArray());
          bOut.close();
  
          // Write to the sequence file
          writer.append(NullWritable.get(), BW);
        } finally {
          IOUtils.closeStream(writer);
      }
    }  
    
  }
}

