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
  currentMatrix.addPosition(0, 0, -amount*20);
}

void keyReleased() {
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

void turnPlaying() {
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

void turnRecoding() {
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

boolean pausePlaying() {
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

boolean pauseRecoding() {
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
