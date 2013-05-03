require 'rmagick'

dir_path = ARGV.shift
if(Dir.exists? "#{dir_path}/thumbs")
	blowups_directory = "#{dir_path}/thumbs-enlarge"

	# Actually make the directory
	Dir.mkdir(blowups_directory) unless Dir.exists?(blowups_directory)

	TILE_COLS = 3
	TILE_ROWS = 3

	TILE_WIDTH = 200
	TILE_HEIGHT = 200

	Dir.glob("#{dir_path}/thumbs/*.jpg") do |fname|
		puts fname
		source_filename = fname.split("/").last
		upscale_fname = "upscale-#{source_filename}"

		thumb = Magick::Image.read(fname)[0]

		pixels = thumb.get_pixels(0, 0, 3, 3)
		# create a new empty image to composite the tiles upon:
		new_img = Magick::Image.new(600, 600)

		# tiles will be an array of Image objects
		tiles = (TILE_COLS * TILE_ROWS).times.inject([]) do |arr, idx|
			# Get the pixel info
			pixel = pixels[idx]

			# Make a tile of that color
			tile = Magick::Image.new(TILE_WIDTH, TILE_HEIGHT) {self.background_color = pixel.to_color}

			# Add the tile to the array of tiles
			arr << tile
		end

		# Reconstitute tiles into one image
		tiles.each_with_index do |tile, idx|
		  new_img.composite!( tile, idx%TILE_COLS * TILE_WIDTH,
		                      idx/TILE_COLS * TILE_HEIGHT,
		                      Magick::OverCompositeOp)
		end

		new_img.write(blowups_directory + "/#{upscale_fname}")

	end # end-glob

else
	puts "Usage: 'ruby scripts/blow_up_thumbs.rb <path to dir with thumbnails>'"
end









