import gmaths.*;
import java.util.ArrayList;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.glsl.*;
import com.jogamp.opengl.util.texture.*;

/**
 * Simple wooden table composed of a cuboid top and four cuboid legs.
 */
public class Table {

  private ArrayList<ModelMultipleLights> parts;
  private Camera camera;
  private Light[] lights;
  private Texture woodTex;

  private float roomSize;
  private float topWidth = 4f;
  private float topDepth = 1.5f;
  private float topThickness = 0.1f;
  private float legThickness = 0.2f;
  private float legHeight = 1.2f;

  public Table(GL3 gl, Camera camera, Light[] lights, float roomSize) {
    this.camera = camera;
    this.lights = lights;
    this.roomSize = roomSize;

    woodTex = TextureLibrary.loadTexture(gl, "assets/textures/wood.jpg");
    woodTex.bind(gl);
    woodTex.setTexParameteri(gl, GL3.GL_TEXTURE_WRAP_S, GL3.GL_REPEAT);
    woodTex.setTexParameteri(gl, GL3.GL_TEXTURE_WRAP_T, GL3.GL_REPEAT);

    parts = new ArrayList<>();
    parts.add(makeTop(gl));

    float legX = topWidth * 0.5f - legThickness * 0.5f;
    float legZ = topDepth * 0.5f - legThickness * 0.5f;
    parts.add(makeLeg(gl, -legX, -legZ));
    parts.add(makeLeg(gl, legX, -legZ));
    parts.add(makeLeg(gl, -legX, legZ));
    parts.add(makeLeg(gl, legX, legZ));
  }

  private ModelMultipleLights makeTop(GL3 gl) {
    String name = "table_top";
    Vec3 base = new Vec3(0.5f, 0.5f, 0.5f);
    Material material = new Material(base, base, new Vec3(0.3f, 0.3f, 0.3f), 32f);
    float zPos = -roomSize * 0.5f + topDepth * 0.5f;
    float yPos = legHeight + topThickness * 0.5f;
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(topWidth, topThickness, topDepth), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(0, yPos, zPos), modelMatrix);
    Mesh mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_m_1t.txt");
    return new ModelMultipleLights(name, mesh, modelMatrix, shader, material, lights, camera, woodTex);
  }

  private ModelMultipleLights makeLeg(GL3 gl, float xOffset, float zOffset) {
    String name = "table_leg";
    Vec3 base = new Vec3(0.5f, 0.5f, 0.5f);
    Material material = new Material(base, base, new Vec3(0.3f, 0.3f, 0.3f), 32f);
    float zBase = -roomSize * 0.5f + topDepth * 0.5f;
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(legThickness, legHeight, legThickness), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(xOffset, legHeight * 0.5f, zBase + zOffset), modelMatrix);
    Mesh mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_m_1t.txt");
    return new ModelMultipleLights(name, mesh, modelMatrix, shader, material, lights, camera, woodTex);
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
    woodTex.destroy(gl);
  }
}

