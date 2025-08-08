import gmaths.*;
import java.util.ArrayList;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.glsl.*;
import com.jogamp.opengl.util.texture.*;

/* I declare that this code is my own work */
/* Author Jack Edwards jgedwards1@sheffield.ac.uk */
public class Laptop {

  private Camera camera;
  private Light[] lights;
  private Texture keyboardTex, screenTex;

  private ModelMultipleLights base, lid;

  // placement on teable
  private float tableTop = 1.3f;
  private float tableZ = -3.25f;

  // dimensions
  private float baseW = 1.0f, baseD = 0.6f, baseH = 0.05f;
  private float lidH = 0.05f; 

  private float lidAngle = 90f; 

  public Laptop(GL3 gl, Camera camera, Light[] lights) {
    this.camera = camera;
    this.lights = lights;

    keyboardTex = TextureLibrary.loadTexture(gl, "keyboard.jpg");
    screenTex = TextureLibrary.loadTexture(gl, "screen.jpg");

    base = makeBase(gl);
    lid = makeLid(gl);
  }

  private ModelMultipleLights makeBase(GL3 gl) {
    Vec3 basecolor = new Vec3(1f,1f,1f);
    Material material = new Material(basecolor, basecolor, new Vec3(0.3f,0.3f,0.3f), 32f);
    Mat4 model = Mat4Transform.scale(baseW, baseH, baseD);
    Mesh mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_m_1t.txt");
    return new ModelMultipleLights("laptop_base", mesh, model, shader, material, lights, camera, keyboardTex);
  }

  private ModelMultipleLights makeLid(GL3 gl) {
    Vec3 basecolor = new Vec3(1f,1f,1f);
    Material material = new Material(basecolor, basecolor, new Vec3(0.3f,0.3f,0.3f), 32f);
    Mat4 model = Mat4Transform.scale(baseW, lidH, baseD);
    Mesh mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_m_1t.txt");
    return new ModelMultipleLights("laptop_lid", mesh, model, shader, material, lights, camera, screenTex);
  }

  private Mat4 baseMatrix() {
    Mat4 m = new Mat4(1);
    m = Mat4.multiply(Mat4Transform.scale(baseW, baseH, baseD), m);
    m = Mat4.multiply(Mat4Transform.translate(0, tableTop + baseH/2f, tableZ), m);
    return m;
  }

  private Mat4 lidMatrix() {
    Mat4 m = new Mat4(1);
    m = Mat4.multiply(Mat4Transform.scale(baseW, lidH, baseD), m);
    m = Mat4.multiply(Mat4Transform.translate(0, lidH*0.5f, baseD*0.5f), m);
    // rotate lid so positive angles open it upward 
    m = Mat4.multiply(Mat4Transform.rotateAroundX(-lidAngle), m);
    m = Mat4.multiply(Mat4Transform.translate(0, tableTop + baseH, tableZ - baseD*0.5f), m);
    return m;
  }

  public void render(GL3 gl) {
    base.setModelMatrix(baseMatrix());
    base.render(gl);
    lid.setModelMatrix(lidMatrix());
    lid.render(gl);
  }

  public void dispose(GL3 gl) {
    base.dispose(gl);
    lid.dispose(gl);
    keyboardTex.destroy(gl);
    screenTex.destroy(gl);
  }

  public void rotateLid(float d) {
    lidAngle += d;
    if (lidAngle < 0f) lidAngle = 0f;
    if (lidAngle > 120f) lidAngle = 120f;
  }
}
