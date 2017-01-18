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

  void addPosition(float _dx, float _dy, float _dz) {
    dx += _dx;
    dy += _dy;
    dz += _dz;
  }

  void addArgument(float _argX, float _argY, float _argZ) {
    argX += _argX;
    argY += _argY;
    argZ += _argZ;
  }

  float getDx() {
    return dx;
  }

  float getDy() {
    return dy;
  }

  float getDz() {
    return dz;
  }

  float getArgX() {
    return argX;
  }

  float getArgY() {
    return argY;
  }

  float getArgZ() {
    return argZ;
  }

  @Override
  CameraMatrix clone() {
    try {
      return (CameraMatrix)super.clone();
    } catch(Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
