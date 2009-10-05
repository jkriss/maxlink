import maxlink.*;

// set the background color from Max
// jkriss ~ 28 july 2004
// updated 21 april 2005 for Processing 0085

// Open "color picker.pat" in the MaxLink examples folder 
// to control the color.

MaxLink link = new MaxLink(this,"color_sketch");

// these must be public
public float r,g,b;

void setup() {
  size(250,250);
  link.declareInlet("r");
  link.declareInlet("g");
  link.declareInlet("b");
}

void draw() {
  background(r,g,b);
}
