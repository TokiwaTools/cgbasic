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



int windowState = 0;
File file;
OBJModel model;
boolean dragging = false;
boolean moving = false;
CameraMatrix currentMatrix;

public void selectFile(File _file) {
  if (_file == null) {
    exit();
  } else {
    windowState = 1;
    file = _file;
    thread("loadOBJ");
  }
}

public void loadOBJ() {
  model = new OBJModel(this, file.getAbsolutePath(), "relative", QUADS);
  model.scale(8);
  model.translateToCenter();
  windowState = 2;
}

public void setup() {
  size(1280, 1080, OPENGL);
  currentMatrix = new CameraMatrix(width*13.0f/24.0f, height*7.0f/10.0f, 0, -PI/10, -PI/3, 0);
  selectInput("Select .obj file", "selectFile");
}

public void draw() {
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

public void drawMessage(String message) {
  fill(70);
  textAlign(CENTER, CENTER);
  textSize(30);
  text(message, width/2, height/2);
}

public void drawFPS() {
  fill(100);
  textAlign(RIGHT, TOP);
  textSize(18);
  String [] frame = split(str(frameRate), '.');
  text(frame[0] + "." + frame[1].substring(0, 2) + " fps", width-10, 10);
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
    currentMatrix.addPosition( mouseX-pmouseX, mouseY-pmouseY, 0 );
  }
  translate( currentMatrix.getDx(), currentMatrix.getDy(), currentMatrix.getDz() );
  if (dragging) {
    currentMatrix.addArgument( -radians((mouseY-pmouseY)/6.0f), radians((mouseX-pmouseX)/6.0f), 0 );
  }
  rotateX( currentMatrix.getArgX() );
  rotateY( currentMatrix.getArgY() );

  noStroke();
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
  currentMatrix.addPosition(0, 0, -amount*20);
}
class CameraMatrix {
  float dx, dy, dz;
  float argX, argY, argZ;

  CameraMatrix(float _dx, float _dy, float _dz, float _argX, float _argY, float _argZ) {
    dx = _dx;
    dy = _dy;
    dz = _dz;
    argX = _argX;
    argY = _argY;
    argZ = _argZ;
  }

  public void addPosition(float _dx, float _dy, float _dz) {
    dx += _dx;
    dy += _dy;
    dz += _dz;
  }

  public void addArgument(float _argX, float _argY, float _argZ) {
    argX += _argX;
    argY += _argY;
    argZ += _argZ;
  }

  public float getDx() {
    return dx;
  }

  public float getDy() {
    return dy;
  }

  public float getDz() {
    return dz;
  }

  public float getArgX() {
    return argX;
  }

  public float getArgY() {
    return argY;
  }

  public float getArgZ() {
    return argZ;
  }
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "--full-screen", "--bgcolor=#666666", "--stop-color=#cccccc", "cgpresen" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
