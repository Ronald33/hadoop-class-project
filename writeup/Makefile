TEX      := latex
PDFTEX   := pdflatex
BIBTEX   := bibtex
RM       := rm -f
OPTIONS  = -o
PDFTEXOPTIONS = --interaction batchmode

DVI     = paper.dvi
TARGET  = paper.pdf
BIBSRC  = $(shell ls *.bib)
BIBFILE = $(shell ls *.bbl)

all: $(TARGET)

paper.pdf: $(DVI)
	clear
	pdflatex paper.tex

clean: 
	$(RM) $(TARGET)  $(DVI) $(BIBFILE) *.aux *.log *blg *~ semantic.cache $(basename $(TARGET)).out 

