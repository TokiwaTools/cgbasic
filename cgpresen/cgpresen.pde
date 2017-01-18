import saito.objloader.*;

OBJModel model;
boolean dragging = false;
boolean moving = false;
float moveDx, moveDy, moveDz;
float rotateDx, rotateDy;

void setup() {
  size(1920, 1080, P3D);
  moveDx = width*13/24.0;
  moveDy = height*7.0/10.0;
  moveDz = 0;
  rotateDx = -PI/10;
  rotateDy = -PI/3;
  model = new OBJModel(this, "boat.obj", "relative", QUADS);
  model.scale(8);
  model.translateToCenter();
  noStroke();
  frameRate(30);
}

void draw() {
  background(220);
  drawLight();
  drawModel();
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
    rotateDx -= (mouseY-pmouseY)/128.0;
    rotateDy += (mouseX-pmouseX)/128.0;
  }
  rotateX(rotateDx);
  rotateY(rotateDy);
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
