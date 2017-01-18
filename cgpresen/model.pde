void selectFile(File _file) {
  if (_file == null) {
    exit();
  } else {
    file = _file;

    String filename = _file.getAbsolutePath();
    String ext = "obj";
    int point = filename.lastIndexOf(".");
    if (point != -1) {
      ext = filename.substring(point+1);
    }
    if (ext.equals("obj")) {
      windowState = 1;
      thread("loadOBJ");
    } else if (ext.equals("png")) {
      windowState = -1;
      logo = loadImage(filename);
    }
  }
}

void loadOBJ() {
  model = new OBJModel(this);
  model.disableDebug();
  model.setDrawMode(QUADS);
  model.setTexturePathMode("relative");
  model.load( file.getAbsolutePath() );
  model.scale(12);
  model.translateToCenter();
  windowState = 2;
}
