require 'rmagick'

FNAME = "images/road_thumb.jpg"

TILE_COLS = 3
TILE_ROWS = 3

TILE_WIDTH = 200
TILE_HEIGHT = 200

thumb = Magick::Image.read(FNAME)[0]

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

new_img.write("#{FNAME}-large.jpg")