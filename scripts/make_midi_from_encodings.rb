# Script to run full pipeline
# 
# Requirements:
# 	ImageMagick - library
# 	RMagick - RubyGem
# 	OptParse - RubyGem
# 	
# 	Regular Hadoop Requirements
# 
# Notes:
# 
# 	Run 'ruby -Iscripts make_midi_from_source.rb -i <base_path>'
# 	
#

require 'optparse'
require 'make_thumbnails'

opts = {}


opt_parser = OptionParser.new do |opt|
  opt.banner = "Usage: opt_parser COMMAND [OPTIONS]"
  opt.separator  ""

  opt.on("-i","--base-directory BASEPATH", "Base directory: Contains /source") do |i|
    opts[:i] = i
  end

  # Probably don't need this
  # opt.on("-o","--output-directory OUTPATH", "Directory to put output") do |o|
  #   opts[:o] = o
  # end

  # Unused for now:
  # 
  # opt.on("-n","--chord-size SIZE", Numeric, "Notes per region") do |n|
  #   opts[:n] = n
  # end
  # 
  # opt.on("-r","--number-regions SIZE", Numeric, "Number of image/musical regions") do |r|
  #   opts[:r] = r
  # end
  # 
  # opt.on("-t", "--test", "Run in test mode.") do
  # 	opts[:test] = true
  # end

  opt.on("-h","--help","help") do
    puts opt_parser
  end

end

opt_parser.parse!

#Sanity Check
puts "Options: "
opts.each do |k,v|
	puts "#{k}: #{v}"
end

base_path     = opts[:i]
# source_path   = base_path + "/source"
# thumbs_path   = base_path + "/thumbs"
# sequence_path = base_path + "/sequence"
# music_path    = base_path + "/music"
# midi_path     = base_path + "/midi"

classpath = ".:build:lib/*"


puts "\nConverting source to encodings..."

ThumbMaker.convert_thumbs(base_path)

# Convert source images to encodings

	# Relevant parameters :
	# 	opts[:i] - location of source images
	# 	opts[:r] - number of regions (we've been defaulting with 9)
	# 			* does this have to be a square? maybe not
	# 	opts[:n] - pixels per region (ie notes per time unit)
	#
	# Current:
	# img.thumbnail(3, 3)
	#
	# rows * columns = r
	# total_pixels = n (pixels per region) * r (regions)
	# total_pixels / COMPLETE THIS ......
	#
	# img.thumbnail(rows, columns)

puts "\nWriting encodings to SequenceFile..."

# 'ant compile' has to have run before this
`ant`
`java -classpath #{classpath} phase2.WriteImagesToSequenceFile -base #{base_path}`

# Write encodings to SequenceFile
	# input_path = opts[:i] + "-thumbs"
	# output_path = opts[:i] + "-sequence"
	#
	# Use Ruby to call shell or java:
	# 	'java WriteImagesToSequenceFile -input input_path -output output_path'
	# 	'etc/hadoop-local phase2. .etc... '

puts "\nStarting MapReduce job..."

# 'ant' default targer has to have run before this
`etc/hadoop-local.sh phase2.Image2MusicMR -base #{base_path}`

# Run MapReduce job

	# input_path = opts[:i] + "-sequence"
	# output_path = opts[:i] + "-music"
	# Use Ruby to call shell:
	#  'etc/hadoop-local phase2.Image2MusicMR -input input_path -output output_path'
	#

puts "\nWriting Music SequenceFile to MIDI..."

`java -classpath #{classpath} phase2.ReadMusicNotesFromSequenceFile -base #{base_path}`
# Read SequenceFile output of MR job and produce midi files

	# input_path = opts[:i] + "-music"
	# output_path = opts[:o] + "-midi" ??? maybe no need for this option, just use [:i] + "-midi"
	# Use Ruby to call shell:
	#   'java ReadMusicNotesFromSequenceFile -input input_path -output output_path'

puts "\nYay?"
