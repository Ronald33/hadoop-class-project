require 'rmagick'

img = Magick::Image::read("images/road.jpg")[0]

thumb = img.thumbnail(3, 3)

thumb.write("images/road_thumb.jpg")

# Dir.glob("*.jpg") do |fname|
#    img = Magick::Image.read(fname)[0]
#    img.thumbnail(3,3).write("#{fname}-thumb.jpg")
# end   