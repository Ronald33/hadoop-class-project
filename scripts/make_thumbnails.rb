require 'rmagick'

directory = ARGV.shift

if(Dir.exists? directory)
	# Make directory name string for thumbs
	thumb_directory = "#{directory}-thumbs"

	# Actually make the directory
	Dir.mkdir(thumb_directory) unless Dir.exists?(thumb_directory)

	Dir.glob("#{directory}/*.jpg") do |fname|
		puts fname
		filename_parts = fname.split("/")
		path = filename_parts[0, filename_parts.length - 1]
		path[path.length - 1] = path.last + "-thumbs"

		thumb_fname = "thumb-#{filename_parts.last}"


		img = Magick::Image.read(fname)[0]
		thumb = img.thumbnail(3,3)
		thumb.write(path.join("/") + "/#{thumb_fname}")
	end

else
	puts "Usage: 'ruby scripts/make_thumbnails.rb <directory>'"
end