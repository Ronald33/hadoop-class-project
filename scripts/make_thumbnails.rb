require 'rmagick'

class ThumbMaker

	def self.convert_thumbs(dir_path)
		if(Dir.exists? "#{dir_path}/source")
		# Make directory name string for thumbs
		thumb_directory = "#{dir_path}/thumbs"

		# Actually make the directory
		Dir.mkdir(thumb_directory) unless Dir.exists?(thumb_directory)

		puts "Making thumbnail for..."
		Dir.glob("#{dir_path}/source/*.jpg") do |fname|
						
			source_filename = fname.split("/").last
			thumb_fname = "thumb-#{source_filename}"
			thumb_dest = thumb_directory + "/#{thumb_fname}"

			print fname
			puts " => #{thumb_dest}"

			# This is the magic
			img = Magick::Image.read(fname)[0]
			thumb = img.thumbnail(3,3)
			thumb.write(thumb_dest)
		end

		else
			puts "Usage: 'ruby scripts/make_thumbnails.rb <path to set directory>'"
			puts "Ensure source images are in: #{dir_path}/source"
		end
	end

end


if __FILE__ == $0

	dir_path = ARGV.shift
	ThumbMaker.convert_thumbs(dir_path)
end