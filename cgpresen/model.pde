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
  model = new OBJModel(this);
  model.disableDebug();
  model.setDrawMode(QUADS);
  model.setTexturePathMode("relative");
  model.load( file.getAbsolutePath() );
  model.scale(8);
  model.translateToCenter();
  windowState = 2;
}
