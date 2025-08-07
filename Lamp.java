import gmaths.*;
import java.util.ArrayList;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.glsl.*;
import com.jogamp.opengl.util.texture.*;

/**
 * Hierarchical angle-poise lamp with a decorative pair of horns and a
 * spotlight located at the nose. Simple cubes are used for all geometry.
 */
public class Lamp {

  private ArrayList<ModelMultipleLights> parts;
  private Camera camera;
  private Light[] lights;
  private Light bulbLight;
  private Texture lampTex;
  private Texture bulbTex;

  // transformation state
  private float baseSlide = 0f;
  private float lowerX = 0f;
  private float lowerY = 0f;
  private float upperX = 0f;
  private float headX = 0f;
  private boolean lightOn = true;

  // dimensions
  private float baseW = 0.5f, baseH = 0.2f, baseD = 0.5f;
  private float armW = 0.1f, armL = 1.0f;
  private float headW = 0.3f, headH = 0.2f, headD = 0.4f;
  private float hornW = 0.1f, hornH = 0.2f, hornD = 0.1f;
  private float bulbSize = 0.2f;

  // placement on table
  private float tableTop = 1.3f;
  private float baseZ = -3.25f;

  private ModelMultipleLights base, lowerArm, upperArm, head, leftHorn, rightHorn, bulbModel;

  public Lamp(GL3 gl, Camera camera, Light[] lights, Light bulbLight) {
    this.camera = camera;
    this.lights = lights;
    this.bulbLight = bulbLight;

    lampTex = TextureLibrary.loadTexture(gl, "assets/textures/container2.jpg");
    lampTex.bind(gl);
    lampTex.setTexParameteri(gl, GL3.GL_TEXTURE_WRAP_S, GL3.GL_REPEAT);
    lampTex.setTexParameteri(gl, GL3.GL_TEXTURE_WRAP_T, GL3.GL_REPEAT);

    bulbTex = TextureLibrary.loadTexture(gl, "assets/textures/cloud.jpg");

    parts = new ArrayList<>();
    base = makeBase(gl);
    lowerArm = makeArm(gl, "lower_arm");
    upperArm = makeArm(gl, "upper_arm");
    head = makeHead(gl);
    leftHorn = makeHorn(gl, true);
    rightHorn = makeHorn(gl, false);
    bulbModel = makeBulb(gl);
    parts.add(base);
    parts.add(lowerArm);
    parts.add(upperArm);
    parts.add(head);
    parts.add(leftHorn);
    parts.add(rightHorn);
  }

  private ModelMultipleLights makeBase(GL3 gl) {
    Vec3 basecolor = new Vec3(1f,1f,1f);
    Material material = new Material(basecolor, basecolor, new Vec3(0.3f,0.3f,0.3f), 32f);
    Mat4 model = Mat4Transform.scale(baseW, baseH, baseD);
    Mesh mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_m_1t.txt");
    return new ModelMultipleLights("lamp_base", mesh, model, shader, material, lights, camera, lampTex);
  }

  private ModelMultipleLights makeArm(GL3 gl, String name) {
    Vec3 basecolor = new Vec3(1f,1f,1f);
    Material material = new Material(basecolor, basecolor, new Vec3(0.3f,0.3f,0.3f), 32f);
    Mat4 model = Mat4Transform.scale(armW, armL, armW);
    Mesh mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_m_1t.txt");
    return new ModelMultipleLights(name, mesh, model, shader, material, lights, camera, lampTex);
  }

  private ModelMultipleLights makeHead(GL3 gl) {
    Vec3 basecolor = new Vec3(1f,1f,1f);
    Material material = new Material(basecolor, basecolor, new Vec3(0.3f,0.3f,0.3f), 32f);
    Mat4 model = Mat4Transform.scale(headW, headH, headD);
    Mesh mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_m_1t.txt");
    return new ModelMultipleLights("lamp_head", mesh, model, shader, material, lights, camera, lampTex);
  }

  private ModelMultipleLights makeHorn(GL3 gl, boolean left) {
    Vec3 basecolor = new Vec3(1f,1f,1f);
    Material material = new Material(basecolor, basecolor, new Vec3(0.3f,0.3f,0.3f), 32f);
    Mat4 model = Mat4Transform.scale(hornW, hornH, hornD);
    Mesh mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_m_1t.txt");
    String name = left ? "horn_left" : "horn_right";
    return new ModelMultipleLights(name, mesh, model, shader, material, lights, camera, lampTex);
  }

  private ModelMultipleLights makeBulb(GL3 gl) {
    Vec3 basecolor = new Vec3(1f,1f,1f);
    Material material = new Material(basecolor, basecolor, new Vec3(0.3f,0.3f,0.3f), 32f);
    Mat4 model = Mat4Transform.scale(bulbSize, bulbSize, bulbSize);
    Mesh mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_m_1t.txt");
    return new ModelMultipleLights("lamp_bulb", mesh, model, shader, material, lights, camera, bulbTex);
  }

  private Mat4 baseMatrix() {
    Mat4 m = new Mat4(1);
    m = Mat4.multiply(Mat4Transform.scale(baseW, baseH, baseD), m);
    m = Mat4.multiply(Mat4Transform.translate(0, tableTop + baseH/2f, baseZ + baseSlide), m);
    return m;
  }

  private Mat4 lowerArmMatrix() {
    Mat4 m = new Mat4(1);
    m = Mat4.multiply(Mat4Transform.scale(armW, armL, armW), m);
    m = Mat4.multiply(Mat4Transform.translate(0, armL*0.5f, 0), m);
    m = Mat4.multiply(Mat4Transform.rotateAroundX(lowerX), m);
    m = Mat4.multiply(Mat4Transform.rotateAroundY(lowerY), m);
    m = Mat4.multiply(Mat4Transform.translate(0, tableTop + baseH, baseZ + baseSlide), m);
    return m;
  }

  private Mat4 upperArmMatrix() {
    Mat4 m = new Mat4(1);
    m = Mat4.multiply(Mat4Transform.scale(armW, armL, armW), m);
    m = Mat4.multiply(Mat4Transform.translate(0, armL*0.5f, 0), m);
    m = Mat4.multiply(Mat4Transform.rotateAroundX(upperX), m);
    m = Mat4.multiply(Mat4Transform.translate(0, armL, 0), m);
    m = Mat4.multiply(Mat4Transform.rotateAroundX(lowerX), m);
    m = Mat4.multiply(Mat4Transform.rotateAroundY(lowerY), m);
    m = Mat4.multiply(Mat4Transform.translate(0, tableTop + baseH, baseZ + baseSlide), m);
    return m;
  }

  private Mat4 headMatrix() {
    Mat4 m = new Mat4(1);
    m = Mat4.multiply(Mat4Transform.scale(headW, headH, headD), m);
    m = Mat4.multiply(Mat4Transform.translate(0, headH*0.5f, -headD*0.5f), m);
    m = Mat4.multiply(Mat4Transform.rotateAroundX(headX), m);
    m = Mat4.multiply(Mat4Transform.translate(0, armL, 0), m);
    m = Mat4.multiply(Mat4Transform.rotateAroundX(upperX), m);
    m = Mat4.multiply(Mat4Transform.translate(0, armL, 0), m);
    m = Mat4.multiply(Mat4Transform.rotateAroundX(lowerX), m);
    m = Mat4.multiply(Mat4Transform.rotateAroundY(lowerY), m);
    m = Mat4.multiply(Mat4Transform.translate(0, tableTop + baseH, baseZ + baseSlide), m);
    return m;
  }

  private Mat4 hornMatrix(boolean left) {
    float xOffset = (left ? -1f : 1f) * (headW*0.5f);
    Mat4 m = new Mat4(1);
    m = Mat4.multiply(Mat4Transform.scale(hornW, hornH, hornD), m);
    m = Mat4.multiply(Mat4Transform.translate(xOffset, headH, -headD*0.2f), m);
    m = Mat4.multiply(Mat4Transform.rotateAroundX(headX), m);
    m = Mat4.multiply(Mat4Transform.translate(0, armL, 0), m);
    m = Mat4.multiply(Mat4Transform.rotateAroundX(upperX), m);
    m = Mat4.multiply(Mat4Transform.translate(0, armL, 0), m);
    m = Mat4.multiply(Mat4Transform.rotateAroundX(lowerX), m);
    m = Mat4.multiply(Mat4Transform.rotateAroundY(lowerY), m);
    m = Mat4.multiply(Mat4Transform.translate(0, tableTop + baseH, baseZ + baseSlide), m);
    return m;
  }

  private Mat4 bulbMatrix() {
    Mat4 m = new Mat4(1);
    m = Mat4.multiply(Mat4Transform.scale(bulbSize, bulbSize, bulbSize), m);
    m = Mat4.multiply(Mat4Transform.translate(0, 0, -headD*0.5f - bulbSize*0.5f), m);
    m = Mat4.multiply(Mat4Transform.rotateAroundX(headX), m);
    m = Mat4.multiply(Mat4Transform.translate(0, armL, 0), m);
    m = Mat4.multiply(Mat4Transform.rotateAroundX(upperX), m);
    m = Mat4.multiply(Mat4Transform.translate(0, armL, 0), m);
    m = Mat4.multiply(Mat4Transform.rotateAroundX(lowerX), m);
    m = Mat4.multiply(Mat4Transform.rotateAroundY(lowerY), m);
    m = Mat4.multiply(Mat4Transform.translate(0, tableTop + baseH, baseZ + baseSlide), m);
    return m;
  }

  private void updateLight() {
    Mat4 m = bulbMatrix();
    Vec3 pos = new Vec3(m.get(0,3), m.get(1,3), m.get(2,3));
    bulbLight.setPosition(pos);
    float totalX = lowerX + upperX + headX;
    float dirX = -(float)Math.sin(Math.toRadians(lowerY));
    float dirY = (float)Math.sin(Math.toRadians(totalX)) * (float)Math.cos(Math.toRadians(lowerY));
    float dirZ = -(float)Math.cos(Math.toRadians(totalX)) * (float)Math.cos(Math.toRadians(lowerY));
    bulbLight.setDirection(new Vec3(dirX, dirY, dirZ));
    bulbLight.setCutOff((float)Math.cos(Math.toRadians(15f)));
    bulbLight.setOuterCutOff((float)Math.cos(Math.toRadians(25f)));
    bulbLight.setOn(lightOn);
  }

  public void render(GL3 gl) {
    updateLight();

    base.setModelMatrix(baseMatrix());
    base.render(gl);

    lowerArm.setModelMatrix(lowerArmMatrix());
    lowerArm.render(gl);

    upperArm.setModelMatrix(upperArmMatrix());
    upperArm.render(gl);

    head.setModelMatrix(headMatrix());
    head.render(gl);

    leftHorn.setModelMatrix(hornMatrix(true));
    leftHorn.render(gl);
    rightHorn.setModelMatrix(hornMatrix(false));
    rightHorn.render(gl);

    if (lightOn) {
      bulbModel.setModelMatrix(bulbMatrix());
      bulbModel.render(gl);
    }
  }

  public void dispose(GL3 gl) {
    for (ModelMultipleLights m : parts) {
      m.dispose(gl);
    }
    bulbModel.dispose(gl);
    lampTex.destroy(gl);
    bulbTex.destroy(gl);
  }

  // control methods
  public void slideBase(float d) { baseSlide += d; }
  public void rotateLowerArmX(float d) { lowerX += d; }
  public void rotateLowerArmY(float d) { lowerY += d; }
  public void rotateUpperArmX(float d) { upperX += d; }
  public void rotateHeadX(float d) { headX += d; }
  public void toggleLight() { lightOn = !lightOn; }
}

