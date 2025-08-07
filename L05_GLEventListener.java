import gmaths.*;

import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.*;
import com.jogamp.opengl.util.glsl.*;
import com.jogamp.opengl.util.texture.*;
import com.jogamp.opengl.util.texture.awt.*;
import com.jogamp.opengl.util.texture.spi.JPEGImage;
  
public class L05_GLEventListener implements GLEventListener {
  
  private static final boolean DISPLAY_SHADERS = false;
  private Camera camera;
    
  /* The constructor is not used to initialise anything */
  public L05_GLEventListener(Camera camera) {
    this.camera = camera;
    this.camera.setPosition(new Vec3(4f,6f,15f));
    this.camera.setTarget(new Vec3(0f,5f,0f));
  }
  
  // ***************************************************
  /*
   * METHODS DEFINED BY GLEventListener
   */

  /* Initialisation */
  public void init(GLAutoDrawable drawable) {   
    GL3 gl = drawable.getGL().getGL3();
    System.err.println("Chosen GLCapabilities: " + drawable.getChosenGLCapabilities());
    gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); 
    gl.glClearDepth(1.0f);
    gl.glEnable(GL.GL_DEPTH_TEST);
    gl.glDepthFunc(GL.GL_LESS);
    gl.glFrontFace(GL.GL_CCW);    // default is 'CCW'
    gl.glEnable(GL.GL_CULL_FACE); // default is 'not enabled' so needs to be enabled
    gl.glCullFace(GL.GL_BACK);   // default is 'back', assuming CCW
    initialise(gl);
    startTime = getSeconds();
  }
  
  /* Called to indicate the drawing surface has been moved and/or resized  */
  public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    GL3 gl = drawable.getGL().getGL3();
    gl.glViewport(x, y, width, height);
    float aspect = (float)width/(float)height;
    camera.setPerspectiveMatrix(Mat4Transform.perspective(45, aspect));
  }

  /* Draw */
  public void display(GLAutoDrawable drawable) {
    GL3 gl = drawable.getGL().getGL3();
    render(gl);
  }

  /* Clean up memory */
  public void dispose(GLAutoDrawable drawable) {
    GL3 gl = drawable.getGL().getGL3();
    room.dispose(gl);
    lights[0].dispose(gl);
    lights[1].dispose(gl);
    textures.destroy(gl);
  }

  // ***************************************************
  /* THE SCENE
   * Now define all the methods to handle the scene.
   * This will be added to in later examples.
   */

  // textures
  private TextureLibrary textures;

  private Room room;
  private Light[] lights = new Light[2];

  private void loadTextures(GL3 gl) {
    textures = new TextureLibrary();
    textures.add(gl, "container_diffuse", "assets/textures/container2.jpg");
    textures.add(gl, "chequerboard", "assets/textures/chequerboard.jpg");
    textures.add(gl, "cloud", "assets/textures/cloud.jpg");
  }

  public void initialise(GL3 gl) {
    loadTextures(gl);

    lights[0] = new Light(gl);
    lights[0].setCamera(camera);
    lights[1] = new Light(gl);
    lights[1].setCamera(camera);
    room = new Room(gl, camera, lights, textures.get("chequerboard"), textures.get("container_diffuse"), textures.get("cloud"));
  }
  
  public void render(GL3 gl) {
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);

    lights[0].setPosition(getLight0Position());  // changing light position each frame
    lights[0].render(gl);

    lights[1].setPosition(getLight1Position());  // changing light position each frame
    lights[1].render(gl);
    
    room.render(gl);
  }
  
  // The light's position is continually being changed, so needs to be calculated for each frame.
  private Vec3 getLight0Position() {
    double elapsedTime = getSeconds()-startTime;
    float x = 8.0f*(float)(Math.sin(Math.toRadians(elapsedTime*50)));
    float y = 3.4f;
    float z = 5.0f*(float)(Math.cos(Math.toRadians(elapsedTime*50)));
    return new Vec3(x,y,z);
  }

  private Vec3 getLight1Position() {
    double elapsedTime = getSeconds()-startTime;
    float x = 8.0f*(float)(Math.sin(Math.toRadians(elapsedTime*80)));
    float y = 7.4f;
    float z = 3.0f*(float)(Math.cos(Math.toRadians(elapsedTime*80)));
    return new Vec3(x,y,z);
  }

    // ***************************************************
  /* TIME
   */
  
  private double startTime;
  
  private double getSeconds() {
    return System.currentTimeMillis()/1000.0;
  }

}