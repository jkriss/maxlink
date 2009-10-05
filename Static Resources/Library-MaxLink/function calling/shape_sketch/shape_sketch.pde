import maxlink.*;

// jkriss ~ 6 aug 2004
// updated 21 april 2005 for Processing 0085

// This sketch demonstrates how to call
// Processing functions from Max

MaxLink link = new MaxLink(this, "shape_sketch");

void setup() {
  size(300, 300);
  background(250);
  noStroke();
  smooth();
  
  // declare the variable name and the setter function name
  link.declareMaxFunction("drawCircle");
  link.declareMaxFunction("drawSquare");
  link.declareMaxFunction("clear");
}

void draw() {
  // must define draw function to
  // allow accurate future drawing
}

// these methods must be public
public void drawCircle() {
  fill(0, 0, 240, 200);
  float r = random(50) + 5;
  ellipse(random(width), random(height), r, r);
}

public void drawSquare() {
  fill(50, 240, 50, 200);
  float w = random(50) + 5;
  rect(random(width), random(height), w, w);
}

public void clear() {
  background(250);
}
