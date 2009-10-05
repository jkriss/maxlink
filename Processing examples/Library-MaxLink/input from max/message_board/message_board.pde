import maxlink.*;

// jkriss ~ 31 july 2004
// updated 21 april 2005 for Processing 0085

// This sketch demonstrates how to use setter functions
// to handle input from Max

MaxLink link = new MaxLink(this, "message_board");

String oldMessage = "";
String message = "";
int messageCount = 0;
PFont f;

void setup() {
  size(500, 120);
  background(20);
  f = loadFont("Meta-Bold.vlw.gz"); 
  
  // declare the variable name and the setter function name
  link.declareInlet("message", "setMessage");
}

void draw() {
  drawMessage();
}

// this method must be public
public void setMessage(String newMessage) {
  messageCount++;
  link.output(messageCount);
  oldMessage = message;
  message = newMessage;
}

void drawMessage() {
  background(20);
  textFont(f, 44); 
  text(message, 35, 50);
  
  textFont(f, 22);
  if (oldMessage != "") text("previous message: " + oldMessage, 37, 80);
}

