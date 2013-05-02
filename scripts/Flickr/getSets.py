#!/usr/bin/python
"""Usage: python wallpaper.py [OPTIONS] TAGS
TAGS is a space delimited list of tags

OPTIONS:
  -w screenwidth or --width screenwidth
  -h screenheight or --height screenheight
  -f filename or --file filename

Requires:
 - Python Imaging Library [http://www.pythonware.com/products/pil/]
"""

__author__ = "James Clarke <james@jamesclarke.info>"
__version__ = "$Rev$"
__date__ = "$Date$"
__copyright__ = "Copyright 2004-5 James Clarke"

import sys
import os
import urllib
import Image
import flickr

#this is slow as so many API calls
def get_urls_for_tags(tags, number):
    # TODO: might need to change this to loop through each tag 
    photos = flickr.photos_search(tags=tags, per_page=number)
    urls = []
    for photo in photos:
        urls.append(photo.getURL(size='Square', urlType='source'))
    return urls

#quicker to just 'hack' the url
#I don't think this works anymore, a change in url format
def quick_get_urls_for_tags(tags, number):
    photos = flickr.photos_search(tags=tags, per_page=number)
    urls = []
    for photo in photos:
        urls.append('http://photos%s.flickr.com/%s_%s_s.jpg' %\
                    (photo.server, photo.id, photo.secret))
    return urls

def create_photos(urls, n, tag):
    
    # load each photo and save it as 1.jpg, 2.jpg, etc.
    for i in range(n):
	photo = load_photo(urls.pop())
	#if not os.path.exists(tag):
	#    os.makedirs(tag)
	photo.save(tag+'-'+str(i),"JPEG")
        

def load_photo(url):
    file, mime = urllib.urlretrieve(url)
    photo = Image.open(file)
    return photo

def main(*argv):
    from getopt import getopt, GetoptError

    try:
        (opts, args) = getopt(argv[1:], 'w:h:f', ['width', 'height', 'file'])
    except GetoptError, e:
        print e
        print __doc__
        return 1

    
    for o, a in opts:
        if o in ('-w', '--width'):
            width = a
        elif o in ('-h', '--height'):
            height = a
        elif o in ('-f' '--file'):
            file = a
        else:
            print "Unknown argument: %s" % o
            print __doc__
            return 1
        
    if len(args) == 0:
        print "You must specify at least one tag"
        print __doc__
        return 1
    
    tags = [item for item in args]
    
    #screen = (width, height)
    #n = photos_required(screen)

    # TODO: Make calls below for each tag in tags; name them "tag1.jpg", etc.

    n = 200
    for tag in tags:
        urls = get_urls_for_tags(tags, n)
        create_photos(urls, n, tag)

if __name__ == '__main__':
    sys.exit(main(*sys.argv))
