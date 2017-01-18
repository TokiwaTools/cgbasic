void drawMessage(String message) {
  fill(70);
  textAlign(CENTER, CENTER);
  textSize(30);
  text(message, width/2, height/2);
}

void drawPopup(String message) {
  drawPopup(message, 90);
}

void drawPopup(String message, float ver) {
  popupCount += 255*(1 - ver*0.01);
  if (popupCount >= 255) {
    return;
  }
  fill(20, 20, 20, 255-popupCount);
  textAlign(LEFT, TOP);
  textSize(50);
  text(message, 30, 30);
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
    currentMatrix.addPosition( mouseX-pmouseX, mouseY-pmouseY, 0 );
  }
  translate( currentMatrix.getDx(), currentMatrix.getDy(), currentMatrix.getDz() );
  if (dragging) {
    currentMatrix.addArgument( -radians((mouseY-pmouseY)/6.0), radians((mouseX-pmouseX)/6.0), 0 );
  }
  rotateX( currentMatrix.getArgX() );
  rotateY( currentMatrix.getArgY() );

  noStroke();
  model.draw();

  popMatrix();
}

void drawPopupMessages() {
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
