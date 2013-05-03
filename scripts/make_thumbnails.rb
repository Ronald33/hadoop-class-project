require 'rmagick'

dir_path = ARGV.shift

if(Dir.exists? "#{dir_path}/source")
	# Make directory name string for thumbs
	thumb_directory = "#{dir_path}/thumbs"

	# Actually make the directory
	Dir.mkdir(thumb_directory) unless Dir.exists?(thumb_directory)

	Dir.glob("#{dir_path}/source/*.jpg") do |fname|
		puts fname
		source_filename = fname.split("/").last

		thumb_fname = "thumb-#{source_filename}"

		img = Magick::Image.read(fname)[0]
		thumb = img.thumbnail(3,3)
		thumb.write(thumb_directory + "/#{thumb_fname}")
	end

else
	puts "Usage: 'ruby scripts/make_thumbnails.rb <path to set directory>'"
	puts "Ensure source images are in: #{dir_path}/source"
end