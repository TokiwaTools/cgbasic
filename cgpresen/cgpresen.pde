import saito.objloader.*;

int windowState;
File file;
OBJModel model;
PImage logo;

boolean dragging;
boolean moving;
int recoding;
int playing;

CameraMatrix currentMatrix;
ArrayList <CameraMatrix> footprintMatrix;
int footprintFrameCount;

int popupCount;

void initialize() {
  windowState = 0;
  dragging = false;
  moving = false;
  recoding = -1;
  playing = -1;
  footprintFrameCount = 0;
  popupCount = 0;
  currentMatrix = new CameraMatrix(width*13.0/24.0, height*7.0/10.0, 0, -PI/10, -PI/3, 0);
  footprintMatrix = new ArrayList <CameraMatrix> ();
  selectInput("Select .obj file", "selectFile");
}

void setup() {
  size(displayWidth, displayHeight, OPENGL);
  initialize();
}

void draw() {
  if (playing == 2) {
    currentMatrix = footprintMatrix.get(footprintFrameCount);
    footprintFrameCount++;
    if (footprintFrameCount >= footprintMatrix.size()-1) {
      footprintFrameCount = 0;
    }
  }

  switch (windowState) {
    case -1 :
      ortho();
      background(220);
      drawMessage("JPY 3,000", 100);
      drawLogo();
    break;
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
