import gmaths.*;

import java.util.ArrayList;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.glsl.*;
import com.jogamp.opengl.util.texture.*;

public class Room {

  private ArrayList<ModelMultipleLights> parts;
  private Camera camera;
  private Light[] lights;
  private Texture floorTex, wallTex, windowTex;
  private float size = 16f;

  public Room(GL3 gl, Camera c, Light[] l, Texture floorTex, Texture wallTex, Texture windowTex) {
    camera = c;
    lights = l;
    this.floorTex = floorTex;
    this.wallTex = wallTex;
    this.windowTex = windowTex;

    wallTex.bind(gl);
    wallTex.setTexParameteri(gl, GL3.GL_TEXTURE_WRAP_S, GL3.GL_REPEAT);
    wallTex.setTexParameteri(gl, GL3.GL_TEXTURE_WRAP_T, GL3.GL_REPEAT);
    windowTex.bind(gl);
    windowTex.setTexParameteri(gl, GL3.GL_TEXTURE_WRAP_S, GL3.GL_REPEAT);
    windowTex.setTexParameteri(gl, GL3.GL_TEXTURE_WRAP_T, GL3.GL_REPEAT);

    parts = new ArrayList<>();
    parts.add(makeFloor(gl));
    parts.add(makeBackWall(gl));
    parts.add(makeRightWall(gl));
    parts.add(makeLeftWall(gl));
    parts.add(makeWindow(gl));
    parts.add(makeNoticeBoard(gl));
    parts.add(makePoster(gl, -2f));
    parts.add(makePoster(gl, 0f));
    parts.add(makePoster(gl, 2f));
  }

  private ModelMultipleLights makeFloor(GL3 gl) {
    String name = "floor";
    Vec3 basecolor = new Vec3(0.5f, 0.5f, 0.5f);
    Material material = new Material(basecolor, basecolor, new Vec3(0.3f, 0.3f, 0.3f), 4.0f);
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size, 1f, size), modelMatrix);
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_m_1t.txt");
    return new ModelMultipleLights(name, mesh, modelMatrix, shader, material, lights, camera, floorTex);
  }

  private ModelMultipleLights makeBackWall(GL3 gl) {
    String name = "back_wall";
    Vec3 basecolor = new Vec3(0.5f, 0.5f, 0.5f);
    Material material = new Material(basecolor, basecolor, new Vec3(0.3f, 0.3f, 0.3f), 4.0f);
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size, 1f, size), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundX(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(0, size * 0.5f, -size * 0.5f), modelMatrix);
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_m_0t.txt");
    return new ModelMultipleLights(name, mesh, modelMatrix, shader, material, lights, camera);
  }

  private ModelMultipleLights makeRightWall(GL3 gl) {
    String name = "right_wall";
    Vec3 basecolor = new Vec3(0.5f, 0.5f, 0.5f);
    Material material = new Material(basecolor, basecolor, new Vec3(0.3f, 0.3f, 0.3f), 4.0f);
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size, 1f, size), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundZ(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(size * 0.5f, size * 0.5f, 0), modelMatrix);

    float[] vertices = {
      -0.5f, 0.0f, -0.5f,  0.0f, 1.0f, 0.0f,  0.0f, 4.0f,
      -0.5f, 0.0f,  0.5f,  0.0f, 1.0f, 0.0f,  0.0f, 0.0f,
       0.5f, 0.0f,  0.5f,  0.0f, 1.0f, 0.0f,  4.0f, 0.0f,
       0.5f, 0.0f, -0.5f,  0.0f, 1.0f, 0.0f,  4.0f, 4.0f
    };
    Mesh mesh = new Mesh(gl, vertices, TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_m_1t.txt");
    return new ModelMultipleLights(name, mesh, modelMatrix, shader, material, lights, camera, wallTex);
  }

  private ModelMultipleLights makeLeftWall(GL3 gl) {
    String name = "left_wall";
    Vec3 basecolor = new Vec3(0.5f, 0.5f, 0.5f);
    Material material = new Material(basecolor, basecolor, new Vec3(0.3f, 0.3f, 0.3f), 4.0f);
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size, 1f, size), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundZ(-90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(-size * 0.5f, size * 0.5f, 0), modelMatrix);
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_m_0t.txt");
    return new ModelMultipleLights(name, mesh, modelMatrix, shader, material, lights, camera);
  }

  private ModelMultipleLights makeWindow(GL3 gl) {
    String name = "window";
    Vec3 basecolor = new Vec3(1.0f, 1.0f, 1.0f);
    Material material = new Material(basecolor, basecolor, new Vec3(0.3f, 0.3f, 0.3f), 4.0f);
    float w = 6f;
    float h = 6f;
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(w, 1f, h), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundZ(-90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(-size * 0.5f + 0.01f, size * 0.5f, 0), modelMatrix);
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_m_1t.txt");
    return new ModelMultipleLights(name, mesh, modelMatrix, shader, material, lights, camera, windowTex);
  }

  private ModelMultipleLights makeNoticeBoard(GL3 gl) {
    String name = "board";
    Vec3 basecolor = new Vec3(0.55f, 0.35f, 0.15f);
    Material material = new Material(basecolor, basecolor, new Vec3(0.3f, 0.3f, 0.3f), 4.0f);
    float w = 6f;
    float h = 3f;
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(w, 1f, h), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundX(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(0, size * 0.6f, -size * 0.5f + 0.01f), modelMatrix);
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_m_0t.txt");
    return new ModelMultipleLights(name, mesh, modelMatrix, shader, material, lights, camera);
  }

  private ModelMultipleLights makePoster(GL3 gl, float xOffset) {
    String name = "poster";
    Vec3 basecolor = new Vec3(1.0f, 1.0f, 1.0f);
    Material material = new Material(basecolor, basecolor, new Vec3(0.3f, 0.3f, 0.3f), 4.0f);
    float w = 1f;
    float h = 1.5f;
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(w, 1f, h), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundX(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(xOffset, size * 0.6f, -size * 0.5f + 0.02f), modelMatrix);
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_m_1t.txt");
    return new ModelMultipleLights(name, mesh, modelMatrix, shader, material, lights, camera, wallTex);
  }

  public void render(GL3 gl) {
    for (ModelMultipleLights m : parts) {
      m.render(gl);
    }
  }

  public void dispose(GL3 gl) {
    for (ModelMultipleLights m : parts) {
      m.dispose(gl);
    }
  }
}
