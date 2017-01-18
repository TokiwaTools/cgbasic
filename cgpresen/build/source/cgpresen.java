import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import saito.objloader.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class cgpresen extends PApplet {



OBJModel model;
boolean dragging = false;
boolean moving = false;
float moveDx, moveDy, moveDz;
float rotateDx, rotateDy;

public void setup() {
  size(1920, 1080, P3D);
  moveDx = width*13/24.0f;
  moveDy = height*7.0f/10.0f;
  moveDz = 0;
  rotateDx = -PI/10;
  rotateDy = -PI/3;
  model = new OBJModel(this, "boat.obj", "relative", QUADS);
  model.scale(8);
  model.translateToCenter();
  noStroke();
  frameRate(30);
}

public void draw() {
  background(220);
  drawLight();
  drawModel();
}

public void drawLight() {
  pushMatrix();

  translate(width/2, height/2, 0);
  rotateX(radians(-30));
  lights();

  popMatrix();
}

public void drawModel() {
  pushMatrix();

  if (moving) {
    moveDx += mouseX-pmouseX;
    moveDy += mouseY-pmouseY;
  }
  translate(moveDx, moveDy, moveDz);
  if (dragging) {
    rotateDx -= (mouseY-pmouseY)/128.0f;
    rotateDy += (mouseX-pmouseX)/128.0f;
  }
  rotateX(rotateDx);
  rotateY(rotateDy);
  model.draw();

  popMatrix();
}

public void mousePressed() {
  switch (mouseButton) {
    case LEFT :
      moving = true;
    break;
    case RIGHT :
      dragging = true;
    break;
  }
}

public void mouseReleased() {
  switch (mouseButton) {
    case LEFT :
      moving = false;
    break;
    case RIGHT :
      dragging = false;
    break;
  }
}

public void mouseWheel(MouseEvent e){
  float amount = e.getAmount();
  moveDz -= amount*20;
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "cgpresen" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
