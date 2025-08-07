import gmaths.*;

import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.*;
import com.jogamp.opengl.util.glsl.*;
import com.jogamp.opengl.util.texture.*;

public class Room {

  private ModelMultipleLights[] wall;
  private Camera camera;
  private Light[] lights;
  private Texture t0, t1;
  private float size = 16f;

  public Room(GL3 gl, Camera c, Light[] l, Texture t0, Texture t1) {
    camera = c;
    lights = l;
    this.t0 = t0;
    this.t1 = t1;
    wall = new ModelMultipleLights[4];
    wall[0] = makeWall0(gl); // floor
    wall[1] = makeWall1(gl); // back wall
    wall[2] = makeWall2(gl); // right wall
    wall[3] = makeWall3(gl); // left wall
  }

  private ModelMultipleLights makeWall0(GL3 gl) {
    String name = "floor";
    Vec3 basecolor = new Vec3(0.5f, 0.5f, 0.5f);
    Material material = new Material(basecolor, basecolor, new Vec3(0.3f, 0.3f, 0.3f), 4.0f);
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size, 1f, size), modelMatrix);
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_m_1t.txt");
    return new ModelMultipleLights(name, mesh, modelMatrix, shader, material, lights, camera, t0);
  }

  private ModelMultipleLights makeWall1(GL3 gl) {
    String name = "wall";
    Vec3 basecolor = new Vec3(0.5f, 0.5f, 0.5f);
    Material material = new Material(basecolor, basecolor, new Vec3(0.3f, 0.3f, 0.3f), 4.0f);
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size, 1f, size), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundX(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(0, size * 0.5f, -size * 0.5f), modelMatrix);
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_m_1t.txt");
    return new ModelMultipleLights(name, mesh, modelMatrix, shader, material, lights, camera, t1);
  }

  private ModelMultipleLights makeWall2(GL3 gl) {
    String name = "wall";
    Material material = new Material(new Vec3(0.1f, 0.5f, 0.91f), new Vec3(0.1f, 0.5f, 0.91f), new Vec3(0.3f, 0.3f, 0.3f), 4.0f);
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size, 1f, size), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundZ(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(size * 0.5f, size * 0.5f, 0), modelMatrix);
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_m_0t.txt");
    return new ModelMultipleLights(name, mesh, modelMatrix, shader, material, lights, camera);
  }

    private ModelMultipleLights makeWall3(GL3 gl) {
      String name = "wall";
      Vec3 basecolor = new Vec3(0.5f, 0.5f, 0.5f); // grey
      Material material = new Material(basecolor, basecolor, new Vec3(0.3f, 0.3f, 0.3f), 4.0f);
      Mat4 modelMatrix = new Mat4(1);
      modelMatrix = Mat4.multiply(Mat4Transform.scale(size, 1f, size), modelMatrix);
      modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(90), modelMatrix); // CHANGED to 90
      modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundZ(-90), modelMatrix);
      modelMatrix = Mat4.multiply(Mat4Transform.translate(size * -0.5f, size * 0.5f, 0), modelMatrix); // CHANGED sign
      Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
      Shader shader = new Shader(gl, "assets/shaders/vs_standard.txt", "assets/shaders/fs_standard_m_1t.txt");
      return new ModelMultipleLights(name, mesh, modelMatrix, shader, material, lights, camera, t1);
    }


  public void render(GL3 gl) {
    for (int i = 0; i < wall.length; i++) {
      wall[i].render(gl);
    }
  }

  public void dispose(GL3 gl) {
    for (int i = 0; i < wall.length; i++) {
      wall[i].dispose(gl);
    }
  }
}
