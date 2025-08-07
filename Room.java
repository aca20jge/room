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
  private Texture noticeboardTex, poster1Tex, poster2Tex, poster3Tex, poster3Specular;
  // Adjusted room size to better align with noticeboard dimensions
  private float size = 8f;

  /**
   * Legacy constructor that assumes the wall texture is also used for the window.
   * This allows older code which passed only floor and wall textures to continue
   * compiling after the introduction of a dedicated window texture.
   *
   * @deprecated Prefer {@link #Room(GL3, Camera, Light[], Texture, Texture, Texture)}
   *             to supply a dedicated window texture.
   */
  @Deprecated
  public Room(GL3 gl, Camera c, Light[] l, Texture floorTex, Texture wallTex) {
    this(gl, c, l, floorTex, wallTex, wallTex);
  }

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

    noticeboardTex = TextureLibrary.loadTexture(gl, "assets/textures/noticeboard.jpg");
    poster1Tex = TextureLibrary.loadTexture(gl, "assets/textures/wattBook.jpg");
    poster2Tex = TextureLibrary.loadTexture(gl, "assets/textures/poster2.jpg");
    poster3Tex = TextureLibrary.loadTexture(gl, "assets/textures/poster3.jpg");
    poster3Specular = TextureLibrary.loadTexture(gl, "assets/textures/poster3_specular.jpg");

    parts = new ArrayList<>();
    parts.add(makeFloor(gl));
    parts.add(makeBackWall(gl));
    parts.add(makeRightWall(gl));
    parts.addAll(makeLeftWall(gl));
    parts.add(makeCloud(gl));
    parts.add(makeNoticeBoard(gl));
    parts.add(makePoster(gl, -2f, poster1Tex, null));
    parts.add(makePoster(gl, 0f, poster2Tex, null));
    parts.add(makePoster(gl, 2f, poster3Tex, poster3Specular));
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

  private ArrayList<ModelMultipleLights> makeLeftWall(GL3 gl) {
    ArrayList<ModelMultipleLights> wall = new ArrayList<>();
    Vec3 basecolor = new Vec3(0.5f, 0.5f, 0.5f);
    Material material = new Material(basecolor, basecolor, new Vec3(0.3f, 0.3f, 0.3f), 4.0f);

    float windowW = 6f;
    float windowH = 6f;
    float sideW = (size - windowW) * 0.5f;
    float borderH = (size - windowH) * 0.5f;

    // top and bottom segments
    wall.add(makeWallPanel(gl, size, borderH, size - borderH * 0.5f, 0));
    wall.add(makeWallPanel(gl, size, borderH, borderH * 0.5f, 0));

    // left and right segments
    wall.add(makeWallPanel(gl, sideW, windowH, borderH + windowH * 0.5f, -windowW * 0.5f - sideW * 0.5f));
    wall.add(makeWallPanel(gl, sideW, windowH, borderH + windowH * 0.5f, windowW * 0.5f + sideW * 0.5f));

    // frame around the cutout
    float frame = 0.2f;
    float windowTop = borderH + windowH;
    float windowBottom = borderH;
    float windowCentreY = borderH + windowH * 0.5f;
    wall.add(makeFramePanel(gl, windowW + 2 * frame, frame, windowTop + frame * 0.5f, 0));
    wall.add(makeFramePanel(gl, windowW + 2 * frame, frame, windowBottom - frame * 0.5f, 0));
    wall.add(makeFramePanel(gl, frame, windowH + 2 * frame, windowCentreY, -windowW * 0.5f - frame * 0.5f));
    wall.add(makeFramePanel(gl, frame, windowH + 2 * frame, windowCentreY, windowW * 0.5f + frame * 0.5f));

    return wall;
  }

  private ModelMultipleLights makeCloud(GL3 gl) {
    String name = "cloud";
    Vec3 basecolor = new Vec3(1.0f, 1.0f, 1.0f);
    Material material = new Material(basecolor, basecolor, new Vec3(0.3f, 0.3f, 0.3f), 4.0f);
    float windowW = 6f;
    float windowH = 6f;
    float w = windowW + 0.5f;
    float h = windowH + 0.5f;
    float windowCentreY = (size - windowH) * 0.5f + windowH * 0.5f;
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(w, 1f, h), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundZ(-90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(-size * 0.5f - 0.5f, windowCentreY, 0), modelMatrix);
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_m_1t.txt");
    return new ModelMultipleLights(name, mesh, modelMatrix, shader, material, lights, camera, windowTex);
  }

  private ModelMultipleLights makeWallPanel(GL3 gl, float w, float h, float y, float z) {
    String name = "left_wall";
    Vec3 basecolor = new Vec3(0.5f, 0.5f, 0.5f);
    Material material = new Material(basecolor, basecolor, new Vec3(0.3f, 0.3f, 0.3f), 4.0f);
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(w, 1f, h), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundZ(-90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(-size * 0.5f, y, z), modelMatrix);
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_m_0t.txt");
    return new ModelMultipleLights(name, mesh, modelMatrix, shader, material, lights, camera);
  }

  private ModelMultipleLights makeFramePanel(GL3 gl, float w, float h, float y, float z) {
    String name = "frame";
    Vec3 basecolor = new Vec3(0.0f, 0.0f, 0.0f);
    Material material = new Material(basecolor, basecolor, new Vec3(0.3f, 0.3f, 0.3f), 4.0f);
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(w, 1f, h), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundZ(-90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(-size * 0.5f + 0.01f, y, z), modelMatrix);
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_m_0t.txt");
    return new ModelMultipleLights(name, mesh, modelMatrix, shader, material, lights, camera);
  }

  private ModelMultipleLights makeNoticeBoard(GL3 gl) {
    String name = "board";
    Vec3 basecolor = new Vec3(1.0f, 1.0f, 1.0f);
    Material material = new Material(basecolor, basecolor, new Vec3(0.3f, 0.3f, 0.3f), 4.0f);
    float w = 6f;
    float h = 3f;
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(w, 1f, h), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundX(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(0, size * 0.6f, -size * 0.5f + 0.05f), modelMatrix);
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_m_1t.txt");
    return new ModelMultipleLights(name, mesh, modelMatrix, shader, material, lights, camera, noticeboardTex);
  }

  private ModelMultipleLights makePoster(GL3 gl, float xOffset, Texture diffuse, Texture specular) {
    String name = "poster";
    Vec3 basecolor = new Vec3(1.0f, 1.0f, 1.0f);
    Material material = new Material(basecolor, basecolor, new Vec3(0.3f, 0.3f, 0.3f), 4.0f);
    float w = 1f;
    float h = 1.5f;
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(w, 1f, h), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundX(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(xOffset, size * 0.6f, -size * 0.5f + 0.06f), modelMatrix);
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Shader shader;
    ModelMultipleLights poster;
    if (specular != null) {
      shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_m_2t.txt");
      poster = new ModelMultipleLights(name, mesh, modelMatrix, shader, material, lights, camera, diffuse, specular);
    } else {
      shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_m_1t.txt");
      poster = new ModelMultipleLights(name, mesh, modelMatrix, shader, material, lights, camera, diffuse);
    }
    return poster;
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
    noticeboardTex.destroy(gl);
    poster1Tex.destroy(gl);
    poster2Tex.destroy(gl);
    poster3Tex.destroy(gl);
    poster3Specular.destroy(gl);
  }
}
