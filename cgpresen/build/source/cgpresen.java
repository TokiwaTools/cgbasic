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



int windowState;
File file;
OBJModel model;

boolean dragging;
boolean moving;
int recoding;
int playing;

CameraMatrix currentMatrix;
ArrayList <CameraMatrix> footprintMatrix;
int footprintFrameCount;

int popupCount;

public void initialize() {
  windowState = 0;
  dragging = false;
  moving = false;
  recoding = -1;
  playing = -1;
  footprintFrameCount = 0;
  popupCount = 0;
  currentMatrix = new CameraMatrix(width*13.0f/24.0f, height*7.0f/10.0f, 0, -PI/10, -PI/3, 0);
  footprintMatrix = new ArrayList <CameraMatrix> ();
  selectInput("Select .obj file", "selectFile");
}

public void setup() {
  size(1280, 720, OPENGL);
  initialize();
}

public void draw() {
  if (playing == 2) {
    currentMatrix = footprintMatrix.get(footprintFrameCount);
    footprintFrameCount++;
    if (footprintFrameCount >= footprintMatrix.size()-1) {
      footprintFrameCount = 0;
    }
  }

  switch (windowState) {
    case 0 :
      ortho();
      background(220);
      drawMessage("Select a model");
    break;
    case 1 :
      ortho();
      background(220);
      drawMessage("LOADING...");
    break;
    case 2 :
      perspective();
      background(220);
      drawLight();
      drawModel();
      ortho();
      drawFPS();
      drawPopupMessages();
      if (recoding == 2) {
        footprintMatrix.add( currentMatrix.clone() );
      }
    break;
  }
}
class CameraMatrix implements Cloneable {
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

  public @Override
  CameraMatrix clone() {
    try {
      return (CameraMatrix)super.clone();
    } catch(Exception e) {
      e.printStackTrace();
    }
    return null;
  }
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

public void keyReleased() {
  switch (key) {
    case 'r' :
      if (playing != 2) {
        turnRecoding();
      }
    break;
    case 'p' :
      if (recoding != 2) {
        turnPlaying();
      }
    break;
    case ' ' :
      if ( !pausePlaying() ) {
        pauseRecoding();
      }
    break;
  }
  if (keyCode == BACKSPACE) {
    println("Init");
    initialize();
  }
}

public void turnPlaying() {
  if (playing <= 0 && footprintMatrix.size() != 0) {
    playing = 2;
    popupCount = 0;
    println("[Footprint] Play");
  } else {
    playing = 0;
    popupCount = 0;
    footprintFrameCount = 0;
    println("[Footprint] Stop");
  }
}

public void turnRecoding() {
  if (recoding <= 0) {
    recoding = 2;
    popupCount = 0;
    footprintMatrix.clear();
    footprintFrameCount = 0;
    println("[Rec] Recoding");
  } else  {
    recoding = 0;
    popupCount = 0;
    println("[Rec] Stop");
  }
}

public boolean pausePlaying() {
  switch (playing) {
      case 1 :
        playing = 2;
        popupCount = 0;
        println("[Footprint] Play");
        return true;
      case 2 :
        playing = 1;
        popupCount = 0;
        println("[Footprint] Pause");
        return true;
  }
  return false;
}

public boolean pauseRecoding() {
  switch (recoding) {
    case 1 :
      recoding = 2;
      popupCount = 0;
      println("[Rec] Recoding");
      return true;
    case 2 :
      recoding = 1;
      popupCount = 0;
      println("[Rec] Pause");
      return true;
  }
  return false;
}
public void drawMessage(String message) {
  fill(70);
  textAlign(CENTER, CENTER);
  textSize(30);
  text(message, width/2, height/2);
}

public void drawPopup(String message) {
  drawPopup(message, 90);
}

public void drawPopup(String message, float ver) {
  popupCount += 255*(1 - ver*0.01f);
  if (popupCount >= 255) {
    return;
  }
  fill(20, 20, 20, 255-popupCount);
  textAlign(LEFT, TOP);
  textSize(50);
  text(message, 30, 30);
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

public void drawPopupMessages() {
  switch (playing) {
    case 0 :
      drawPopup("Footprint: Stop");
      return;
    case 1 :
      drawPopup("Footprint: Pause");
      return;
    case 2 :
      drawPopup("Footprint: Play");
      return;
  }

  switch (recoding) {
    case 0 :
      drawPopup("Rec: Stop");
      return;
    case 1 :
      drawPopup("Rec: Pause");
      return;
    case 2 :
      drawPopup("Rec: Recoding");
      return;
  }
}
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
  model = new OBJModel(this);
  model.disableDebug();
  model.setDrawMode(QUADS);
  model.setTexturePathMode("relative");
  model.load( file.getAbsolutePath() );
  model.scale(8);
  model.translateToCenter();
  windowState = 2;
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
