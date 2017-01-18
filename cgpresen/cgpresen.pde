import saito.objloader.*;

int windowState = 0;
File file;
OBJModel model;
boolean dragging = false;
boolean moving = false;
float moveDx, moveDy, moveDz;
float rotateDx, rotateDy;

void selectFile(File _file) {
  if (_file == null) {
    exit();
  } else {
    windowState = 1;
    file = _file;
    thread("loadOBJ");
  }
}

void loadOBJ() {
  model = new OBJModel(this, file.getAbsolutePath(), "relative", QUADS);
  model.scale(8);
  model.translateToCenter();
  windowState = 2;
}

void setup() {
  size(1920, 1080, P3D);
  frameRate(30);
  moveDx = width*13/24.0;
  moveDy = height*7.0/10.0;
  moveDz = 0;
  rotateDx = -PI/10;
  rotateDy = -PI/3;
  selectInput("Select .obj file", "selectFile");
}

void draw() {
  background(220);
  switch (windowState) {
    case 0 :
      ortho();
      drawMessage("Select a model");
    break;
    case 1 :
      ortho();
      drawMessage("LOADING...");
    break;
    case 2 :
      ortho();
      drawFPS();
      perspective();
      drawLight();
      drawModel();
    break;
  }
}

void drawMessage(String message) {
  fill(70);
  textAlign(CENTER, CENTER);
  textSize(30);
  text(message, width/2, height/2);
}

void drawFPS() {
  fill(100);
  textAlign(RIGHT, TOP);
  textSize(18);
  String [] frame = split(str(frameRate), '.');
  text(frame[0] + "." + frame[1].substring(0, 2) + " fps", width-10, 10);
}

void drawLight() {
  pushMatrix();

  translate(width/2, height/2, 0);
  rotateX(radians(-30));
  lights();

  popMatrix();
}

void drawModel() {
  pushMatrix();

  if (moving) {
    moveDx += mouseX-pmouseX;
    moveDy += mouseY-pmouseY;
  }
  translate(moveDx, moveDy, moveDz);
  if (dragging) {
    rotateDx -= radians( (mouseY-pmouseY)/6.0 );
    rotateDy += radians( (mouseX-pmouseX)/6.0 );
  }
  rotateX(rotateDx);
  rotateY(rotateDy);

  noStroke();
  model.draw();

  popMatrix();
}

void mousePressed() {
  switch (mouseButton) {
    case LEFT :
      moving = true;
    break;
    case RIGHT :
      dragging = true;
    break;
  }
}

void mouseReleased() {
  switch (mouseButton) {
    case LEFT :
      moving = false;
    break;
    case RIGHT :
      dragging = false;
    break;
  }
}

void mouseWheel(MouseEvent e){
  float amount = e.getAmount();
  moveDz -= amount*20;
}
