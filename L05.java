import java.awt.*;
import java.awt.event.*;
import javax.swing.JFrame;
import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

public class L05 extends JFrame {
  
  private static final int WIDTH = 1024;
  private static final int HEIGHT = 768;
  private static final Dimension dimension = new Dimension(WIDTH, HEIGHT);
  private GLCanvas canvas;
  private GLEventListener glEventListener;
  private final FPSAnimator animator; 

  public static void main(String[] args) {
    L05 b1 = new L05("L05");
    b1.getContentPane().setPreferredSize(dimension);
    b1.pack();
    b1.setVisible(true);
    b1.canvas.requestFocusInWindow();
  }

  public L05(String textForTitleBar) {
    super(textForTitleBar);
    setUpCanvas();
    getContentPane().add(canvas, BorderLayout.CENTER);
    addWindowListener(new windowHandler());
    animator = new FPSAnimator(canvas, 60);
    animator.start();
  }

  private void setUpCanvas() {
    GLCapabilities glcapabilities = new GLCapabilities(GLProfile.get(GLProfile.GL3));
    canvas = new GLCanvas(glcapabilities);
    Camera camera = new Camera(Camera.DEFAULT_POSITION,
        Camera.DEFAULT_TARGET, Camera.DEFAULT_UP);
    glEventListener = new L05_GLEventListener(camera);
    canvas.addGLEventListener(glEventListener);
    canvas.addMouseMotionListener(new MyMouseInput(camera));
    canvas.addKeyListener(new MyKeyboardInput(camera, (L05_GLEventListener)glEventListener));
  }

  private class windowHandler extends WindowAdapter {
    public void windowClosing(WindowEvent e) {
      animator.stop();
      remove(canvas);
      dispose();
      System.exit(0);
    }
  }
}

class MyKeyboardInput extends KeyAdapter  {
  private Camera camera;
  private L05_GLEventListener glevent;

  public MyKeyboardInput(Camera camera, L05_GLEventListener glevent) {
    this.camera = camera;
    this.glevent = glevent;
  }

  public void keyPressed(KeyEvent e) {
    Camera.Movement m = Camera.Movement.NO_MOVEMENT;
    switch (e.getKeyCode()) {
      case KeyEvent.VK_LEFT:  m = Camera.Movement.LEFT;  break;
      case KeyEvent.VK_RIGHT: m = Camera.Movement.RIGHT; break;
      case KeyEvent.VK_UP:    m = Camera.Movement.UP;    break;
      case KeyEvent.VK_DOWN:  m = Camera.Movement.DOWN;  break;
      case KeyEvent.VK_A:  m = Camera.Movement.FORWARD;  break;
      case KeyEvent.VK_Z:  m = Camera.Movement.BACK;  break;
      case KeyEvent.VK_1: glevent.slideBase(-0.1f); break;
      case KeyEvent.VK_2: glevent.slideBase(0.1f); break;
      case KeyEvent.VK_3: glevent.rotateLowerArmY(-5f); break;
      case KeyEvent.VK_4: glevent.rotateLowerArmY(5f); break;
      case KeyEvent.VK_5: glevent.rotateLowerArmX(-5f); break;
      case KeyEvent.VK_6: glevent.rotateLowerArmX(5f); break;
      case KeyEvent.VK_7: glevent.rotateUpperArmX(-5f); break;
      case KeyEvent.VK_8: glevent.rotateUpperArmX(5f); break;
      case KeyEvent.VK_9: glevent.rotateHeadX(-5f); break;
      case KeyEvent.VK_0: glevent.rotateHeadX(5f); break;
      case KeyEvent.VK_L: glevent.toggleLamp(); break;
    }
    camera.keyboardInput(m);
  }
}

class MyMouseInput extends MouseMotionAdapter {
  private Point lastpoint;
  private Camera camera;
  
  public MyMouseInput(Camera camera) {
    this.camera = camera;
  }
  
    /**
   * mouse is used to control camera position
   *
   * @param e  instance of MouseEvent
   */    
  public void mouseDragged(MouseEvent e) {
    Point ms = e.getPoint();
    float sensitivity = 0.001f;
    float dx=(float) (ms.x-lastpoint.x)*sensitivity;
    float dy=(float) (ms.y-lastpoint.y)*sensitivity;
    //System.out.println("dy,dy: "+dx+","+dy);
    if (e.getModifiersEx()==MouseEvent.BUTTON1_DOWN_MASK)
      camera.updateYawPitch(dx, -dy);
    lastpoint = ms;
  }

  /**
   * mouse is used to control camera position
   *
   * @param e  instance of MouseEvent
   */  
  public void mouseMoved(MouseEvent e) {   
    lastpoint = e.getPoint(); 
  }

}