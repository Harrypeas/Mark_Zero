package JOGL;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.LineBorder;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL2ES1;
import com.jogamp.opengl.GL2ES3;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLException;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.fixedfunc.GLLightingFunc;
import com.jogamp.opengl.fixedfunc.GLMatrixFunc;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureCoords;
import com.jogamp.opengl.util.texture.TextureIO;

/**
 * NeHe Lesson 8: Blending
 *
 * 'b': toggle blending on/off
 * 'l': toggle light on/off
 * 'f': switch to the next texture filters (nearest, linear, mipmap)
 * Page-up/Page-down: zoom in/out decrease/increase z
 * up-arrow/down-arrow: decrease/increase x rotational speed
 * left-arrow/right-arrow: decrease/increase y rotational speed
 * F1: toggle full-screen and windowed mode
 * ESC: quit
 */
public class NeheJOGL08Blending extends JPanel implements GLEventListener, KeyListener {
   /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final int REFRESH_FPS = 60;    // Display refresh frames per second
   private GLU glu;             // For the GL Utility
   public final com.jogamp.opengl.util.FPSAnimator animator;  // Used to drive display() 
   GLCanvas canvas;
   
   private static float angleX = 0.0f; // rotational angle for x-axis in degree
   private static float angleY = 0.0f; // rotational angle for y-axis in degree
   private static float z = -5.0f; // z-location
   private static float rotateSpeedX = 0.0f; // rotational speed for x-axis
   private static float rotateSpeedY = 0.0f; // rotational speed for y-axis

   private static float zIncrement = 0.02f; // for zoom in/out
   private static float rotateSpeedXIncrement = 0.01f; // adjusting x rotational speed
   private static float rotateSpeedYIncrement = 0.01f; // adjusting y rotational speed

   // Textures with three different filters - Nearest, Linear & MIPMAP_NEAREST
   private Texture[] textures = new Texture[3];
   private static int currTextureFilter = 0; // currently used filter
   private String textureFileName = "images/iron_man.png";
   private String texture2Name = "images/iron_man.png";

   // Texture image flips vertically. Shall use TextureCoords class to retrieve the
   // top, bottom, left and right coordinates.
   private float textureTop;
   private float textureBottom;
   private float textureLeft;
   private float textureRight;
   
   private float textureTop2;
   private float textureBottom2;
   private float textureLeft2;
   private float textureRight2;
   
   private float pointmove = 0;

   // Lighting
   private static boolean isLightOn;

   // Blending
   private static boolean blendingEnabled; // blending on/off

   // Constructor
   public NeheJOGL08Blending() {
      GLCapabilities capabilities =new GLCapabilities(GLProfile.getDefault());
      capabilities.setBackgroundOpaque(false);
      capabilities.setAlphaBits(8);
	  capabilities.setDoubleBuffered(true);
	  capabilities.setHardwareAccelerated(true);
	  setBorder(new LineBorder(Color.BLACK));
      canvas = new GLCanvas(capabilities);
      this.setLayout(new BorderLayout());
      this.add(canvas, BorderLayout.CENTER);
      canvas.setBackground(new Color(50, 150, 100, 150));
      
      setBackground(new Color(50, 150, 100, 150));
      setTransferHandler(new CanvasDataHandler());
      canvas.addGLEventListener(this);
      canvas.addKeyListener(this);
      canvas.setFocusable(true);  // To receive key event
      canvas.requestFocus();
      
      canvas.addMouseListener(new MouseListener() {
		
		@Override
		public void mouseReleased(java.awt.event.MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mousePressed(java.awt.event.MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mouseExited(java.awt.event.MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mouseEntered(java.awt.event.MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void mouseClicked(java.awt.event.MouseEvent e) {
			// TODO Auto-generated method stub
			repaint();
		}
	});
   
      // Run the animation loop using the fixed-rate Frame-per-second animator,
      // which calls back display() at this fixed-rate (FPS).
      animator = new com.jogamp.opengl.util.FPSAnimator(canvas, REFRESH_FPS, true);
   }

   // Main program
   public static void main(String[] args) {
      final int WINDOW_WIDTH = 600;
      final int WINDOW_HEIGHT = 600;
      final String WINDOW_TITLE = "Nehe #8: Blending";

      JFrame frame = new JFrame();
      final NeheJOGL08Blending joglMain = new NeheJOGL08Blending();
      frame.setContentPane(joglMain);
      frame.addWindowListener(new WindowAdapter() {
         @Override 
         public void windowClosing(WindowEvent e) {
            // Use a dedicate thread to run the stop() to ensure that the
            // animator stops before program exits.
            new Thread() {
               @Override 
               public void run() {
                  joglMain.animator.stop(); // stop the animator loop
                  System.exit(0);
               }
            }.start();
         }
      });
      frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
      frame.setTitle(WINDOW_TITLE);
      frame.setVisible(true);
//      joglMain.animator.start(); // start the animation loop
   }

   // ------ Implement methods declared in GLEventListener ------

   /**
    * Called back immediately after the OpenGL context is initialized. Can be used 
    * to perform one-time initialization. Run only once.
    */
   @Override
   public void init(GLAutoDrawable drawable) {
	   System.out.println("init");
      GL2 gl = drawable.getGL().getGL2(); // Get the OpenGL graphics context
      glu = new GLU(); // GL Utilities
      gl.glClearColor(0.25f, 0.25f, 0.25f, 0.5f);  // Set background (clear) color
      gl.glClearDepth(1.0f); // Set clear depth value to farthest
//      gl.glEnable(GL2.GL_DEPTH_TEST); // Enables depth testing
      gl.glDepthFunc(GL2.GL_LEQUAL); // The type of depth test to do
      // Do the best perspective correction
      gl.glHint(GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST);
      // Enable smooth shading, which blends colors nicely, and smoothes out lighting.
      gl.glShadeModel(GLLightingFunc.GL_SMOOTH);

      // Load textures from image
      try {
         // Use URL so that can read from JAR and disk file.
         File image = new File(textureFileName);
         File image2 = new File(texture2Name);

         // Create a OpenGL Texture object from (URL, mipmap, file suffix)
         textures[0] = TextureIO.newTexture(image, false);
         // Nearest filter is least compute-intensive
         // Use nearer filter if image is larger than the original texture
         gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);
         // Use nearer filter if image is smaller than the original texture
         gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);

         textures[1] = TextureIO.newTexture(image2, false);
         // Linear filter is more compute-intensive
         // Use linear filter if image is larger than the original texture
         gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
         // Use linear filter if image is smaller than the original texture
         gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_LINEAR);

         textures[2] = TextureIO.newTexture(image, true); // mipmap is true
         // Use mipmap filter is the image is smaller than the texture
         gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_LINEAR);
         gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER,
        		 GL2.GL_LINEAR_MIPMAP_NEAREST);

         // Get the top and bottom coordinates of the textures. Image flips vertically.
         TextureCoords textureCoords;
         textureCoords = textures[0].getImageTexCoords();
         textureTop = textureCoords.top();
         textureBottom = textureCoords.bottom();
         textureLeft = textureCoords.left();
         textureRight = textureCoords.right();
         
         TextureCoords textureCoords2;
         textureCoords2 = textures[1].getImageTexCoords();
         textureTop2 = textureCoords2.top();
         textureBottom2 = textureCoords2.bottom();
         textureLeft2 = textureCoords2.left();
         textureRight2 = textureCoords2.right();
         
      } catch (GLException e) {
         e.printStackTrace();
      } catch (IOException e) {
         e.printStackTrace();
      }

      // Set up the lighting for Light-1
      // Ambient light does not come from a particular direction. Need some ambient
      // light to light up the scene. Ambient's value in RGBA
      float[] lightAmbientValue = {0.5f, 0.5f, 0.5f, 1.0f};
      // Diffuse light comes from a particular location. Diffuse's value in RGBA
      float[] lightDiffuseValue = {1.0f, 1.0f, 1.0f, 1.0f};
      // Diffuse light location xyz (in front of the screen).
      float lightDiffusePosition[] = {0.0f, 0.0f, 2.0f, 1.0f};

      gl.glLightfv(GLLightingFunc.GL_LIGHT1, GLLightingFunc.GL_AMBIENT, lightAmbientValue, 0);
      gl.glLightfv(GLLightingFunc.GL_LIGHT1, GLLightingFunc.GL_DIFFUSE, lightDiffuseValue, 0);
      gl.glLightfv(GLLightingFunc.GL_LIGHT1, GLLightingFunc.GL_POSITION, lightDiffusePosition, 0);
      gl.glEnable(GLLightingFunc.GL_LIGHT1); // Enable Light-1
      gl.glDisable(GLLightingFunc.GL_LIGHTING); // But disable lighting
      isLightOn = false;

      // Blending control
      // Full Brightness with specific alpha (1 for opaque, 0 for transparent)
      gl.glColor4f(1.0f, 1.0f, 1.0f, 0.5f);
      // Used blending function based On source alpha value
      gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE);
      gl.glEnable(GL2.GL_BLEND);
      gl.glDisable(GL2.GL_DEPTH_TEST);
      blendingEnabled = true;

      // Changing the color4f's alpha value has no effect.
      // Vertex colors have no effect if lighting is enabled, instead material
      // colors could be used, as follows:
      //gl.glEnable(GL_COLOR_MATERIAL);
      //gl.glColorMaterial(GL_FRONT_AND_BACK, GL_AMBIENT_AND_DIFFUSE);
   }

   /**
    * Call-back handler for window re-size event. Also called when the drawable is 
    * first set to visible.
    */
   @Override
   public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
      GL2 gl = drawable.getGL().getGL2(); // Get the OpenGL graphics context

      if (height == 0) {
         height = 1; // prevent divide by zero
      }
      float aspect = (float)width / height;

      // Set the viewport (display area) to cover the entire window
      gl.glViewport(0, 0, width, height);

      // Setup perspective projection, with aspect ratio matches viewport
      gl.glMatrixMode(GLMatrixFunc.GL_PROJECTION); // Choose projection matrix
      gl.glLoadIdentity(); // Reset projection matrix
      glu.gluPerspective(30.0, aspect, 0.1, 200.0); // fovy, aspect, zNear, zFar
//      glu.gluLookAt(0, 0, 6, 0, 0, -10, 0, 1, 0);

      // Enable the model-view transform
      gl.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
      gl.glLoadIdentity(); // reset
   }

   /**
    * Called back by the animator to perform rendering.
    */
   @Override
   public void display(GLAutoDrawable drawable) {
      GL2 gl = drawable.getGL().getGL2(); // Get the OpenGL graphics context
      gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT); // Clear color and depth buffers
      
      // ------ Render a Cube with texture ------
      gl.glLoadIdentity();   // reset model-view matrix
      gl.glTranslatef(0.0f, 0.0f, -5); // translate into the screen
      gl.glRotatef(angleX, 0.0f, 0.0f, 1.0f); // rotate about the x-axis

      gl.glRotatef(angleY, 0.0f, 1.0f, 0.0f); // rotate about the y-axis

      // Enables this texture's target (e.g., GL_TEXTURE_2D) in the current GL
      // context's state.
      textures[currTextureFilter].enable(gl);
      // Bind the texture with the currently chosen filter to the current OpenGL
      // graphics context.
      textures[currTextureFilter].bind(gl);

      // Lighting
      if (isLightOn) {
         gl.glEnable(GLLightingFunc.GL_LIGHTING);
      } else {
         gl.glDisable(GLLightingFunc.GL_LIGHTING);
      }

      // Blending control
      if (blendingEnabled) {
         gl.glEnable(GL2.GL_BLEND); // Turn blending on
         gl.glDisable(GL2.GL_DEPTH_TEST); // Turn depth testing off
      } else {
         gl.glDisable(GL2.GL_BLEND); // Turn blending off
         gl.glEnable(GL2.GL_DEPTH_TEST); // Turn depth testing on
      }

      gl.glBegin(GL2ES3.GL_QUADS); // of the color cube

      gl.glColor3f(0.3f, 0.6f, 1.0f);
      // Front Face
      gl.glNormal3f(0.0f, 0.0f, 1.0f);
      gl.glTexCoord2f(textureLeft, textureBottom);
      gl.glVertex3f(-1.0f, -1.0f, 1.0f); // bottom-left of the texture and quad
      gl.glTexCoord2f(textureRight, textureBottom);
      gl.glVertex3f(1.0f, -1.0f, 1.0f); // bottom-right of the texture and quad
      gl.glTexCoord2f(textureRight, textureTop);
      gl.glVertex3f(1.0f, 1.0f, 1.0f); // top-right of the texture and quad
      gl.glTexCoord2f(textureLeft, textureTop);
      gl.glVertex3f(-1.0f, 1.0f, 1.0f); // top-left of the texture and quad

      // Back Face
      gl.glNormal3f(0.0f, 0.0f, -1.0f);
      gl.glTexCoord2f(textureRight, textureBottom);
      gl.glVertex3f(-1.0f, -1.0f, -1.0f);
      gl.glTexCoord2f(textureRight, textureTop);
      gl.glVertex3f(-1.0f, 1.0f, -1.0f);
      gl.glTexCoord2f(textureLeft, textureTop);
      gl.glVertex3f(1.0f, 1.0f, -1.0f);
      gl.glTexCoord2f(textureLeft, textureBottom);
      gl.glVertex3f(1.0f, -1.0f, -1.0f);
      
      // Top Face
      gl.glNormal3f(0.0f, 1.0f, 0.0f);
      gl.glTexCoord2f(textureLeft, textureTop);
      gl.glVertex3f(-1.0f, 1.0f, -1.0f);
      gl.glTexCoord2f(textureLeft, textureBottom);
      gl.glVertex3f(-1.0f, 1.0f, 1.0f);
      gl.glTexCoord2f(textureRight, textureBottom);
      gl.glVertex3f(1.0f, 1.0f, 1.0f);
      gl.glTexCoord2f(textureRight, textureTop);
      gl.glVertex3f(1.0f, 1.0f, -1.0f);
      
      // Bottom Face
      gl.glNormal3f(0.0f, -1.0f, 0.0f);
      gl.glTexCoord2f(textureRight, textureTop);
      gl.glVertex3f(-1.0f, -1.0f, -1.0f);
      gl.glTexCoord2f(textureLeft, textureTop);
      gl.glVertex3f(1.0f, -1.0f, -1.0f);
      gl.glTexCoord2f(textureLeft, textureBottom);
      gl.glVertex3f(1.0f, -1.0f, 1.0f);
      gl.glTexCoord2f(textureRight, textureBottom);
      gl.glVertex3f(-1.0f, -1.0f, 1.0f);
      
      // Right face
      gl.glNormal3f(1.0f, 0.0f, 0.0f);
      gl.glTexCoord2f(textureRight, textureBottom);
      gl.glVertex3f(1.0f, -1.0f, -1.0f);
      gl.glTexCoord2f(textureRight, textureTop);
      gl.glVertex3f(1.0f, 1.0f, -1.0f);
      gl.glTexCoord2f(textureLeft, textureTop);
      gl.glVertex3f(1.0f, 1.0f, 1.0f);
      gl.glTexCoord2f(textureLeft, textureBottom);
      gl.glVertex3f(1.0f, -1.0f, 1.0f);
      
      // Left Face
      gl.glNormal3f(-1.0f, 0.0f, 0.0f);
      gl.glTexCoord2f(textureLeft, textureBottom);
      gl.glVertex3f(-1.0f, -1.0f, -1.0f);
      gl.glTexCoord2f(textureRight, textureBottom);
      gl.glVertex3f(-1.0f, -1.0f, 1.0f);
      gl.glTexCoord2f(textureRight, textureTop);
      gl.glVertex3f(-1.0f, 1.0f, 1.0f);
      gl.glTexCoord2f(textureLeft, textureTop);
      gl.glVertex3f(-1.0f, 1.0f, -1.0f);

      gl.glEnd();
      textures[1].enable(gl);
      textures[1].bind(gl);
      gl.glTranslatef(0.0f, 0.0f, 4f);

      gl.glTranslatef(pointmove, 0.0f, 0.0f);
      pointmove += 0.02;
      if (pointmove >= 2) {
		pointmove = -2f;
	}
      gl.glBegin(GL2.GL_POLYGON);
    // Front Face
    gl.glNormal3f(0.0f, 0.0f, 1.0f);
    gl.glTexCoord2f(textureLeft2, textureBottom2);
    gl.glVertex3f(-0.10f, -0.1f, 5.0f); // bottom-left of the texture and quad
    gl.glTexCoord2f(textureRight2, textureBottom2);
    gl.glVertex3f(0.1f, -0.1f, 5.0f); // bottom-right of the texture and quad
    gl.glTexCoord2f(textureRight2, textureTop2);
    gl.glVertex3f(0.1f, 0.1f, 5.0f); // top-right of the texture and quad
    gl.glTexCoord2f(textureLeft2, textureTop2);
    gl.glVertex3f(-0.1f, 0.1f, 5.0f); // top-left of the texture and quad
    gl.glEnd();
//      gl.glDisable(GL2.GL_TEXTURE_2D);
//      gl.glTranslatef(1, 0, 0);
//      gl.glBegin(GL2.GL_QUADS); // of the color cube
//
//      // Front Face
//      gl.glNormal3f(0.0f, 0.0f, 1.0f);
//      gl.glTexCoord2f(textureLeft, textureBottom);
//      gl.glVertex3f(-1.0f, -1.0f, 1.0f); // bottom-left of the texture and quad
//      gl.glTexCoord2f(textureRight, textureBottom);
//      gl.glVertex3f(1.0f, -1.0f, 1.0f); // bottom-right of the texture and quad
//      gl.glTexCoord2f(textureRight, textureTop);
//      gl.glVertex3f(1.0f, 1.0f, 1.0f); // top-right of the texture and quad
//      gl.glTexCoord2f(textureLeft, textureTop);
//      gl.glVertex3f(-1.0f, 1.0f, 1.0f); // top-left of the texture and quad
//
//      // Back Face
//      gl.glNormal3f(0.0f, 0.0f, -1.0f);
//      gl.glTexCoord2f(textureRight, textureBottom);
//      gl.glVertex3f(-1.0f, -1.0f, -1.0f);
//      gl.glTexCoord2f(textureRight, textureTop);
//      gl.glVertex3f(-1.0f, 1.0f, -1.0f);
//      gl.glTexCoord2f(textureLeft, textureTop);
//      gl.glVertex3f(1.0f, 1.0f, -1.0f);
//      gl.glTexCoord2f(textureLeft, textureBottom);
//      gl.glVertex3f(1.0f, -1.0f, -1.0f);
//      
//      // Top Face
//      gl.glNormal3f(0.0f, 1.0f, 0.0f);
//      gl.glTexCoord2f(textureLeft, textureTop);
//      gl.glVertex3f(-1.0f, 1.0f, -1.0f);
//      gl.glTexCoord2f(textureLeft, textureBottom);
//      gl.glVertex3f(-1.0f, 1.0f, 1.0f);
//      gl.glTexCoord2f(textureRight, textureBottom);
//      gl.glVertex3f(1.0f, 1.0f, 1.0f);
//      gl.glTexCoord2f(textureRight, textureTop);
//      gl.glVertex3f(1.0f, 1.0f, -1.0f);
//      
//      // Bottom Face
//      gl.glNormal3f(0.0f, -1.0f, 0.0f);
//      gl.glTexCoord2f(textureRight, textureTop);
//      gl.glVertex3f(-1.0f, -1.0f, -1.0f);
//      gl.glTexCoord2f(textureLeft, textureTop);
//      gl.glVertex3f(1.0f, -1.0f, -1.0f);
//      gl.glTexCoord2f(textureLeft, textureBottom);
//      gl.glVertex3f(1.0f, -1.0f, 1.0f);
//      gl.glTexCoord2f(textureRight, textureBottom);
//      gl.glVertex3f(-1.0f, -1.0f, 1.0f);
//      
//      // Right face
//      gl.glNormal3f(1.0f, 0.0f, 0.0f);
//      gl.glTexCoord2f(textureRight, textureBottom);
//      gl.glVertex3f(1.0f, -1.0f, -1.0f);
//      gl.glTexCoord2f(textureRight, textureTop);
//      gl.glVertex3f(1.0f, 1.0f, -1.0f);
//      gl.glTexCoord2f(textureLeft, textureTop);
//      gl.glVertex3f(1.0f, 1.0f, 1.0f);
//      gl.glTexCoord2f(textureLeft, textureBottom);
//      gl.glVertex3f(1.0f, -1.0f, 1.0f);
//      
//      // Left Face
//      gl.glNormal3f(-1.0f, 0.0f, 0.0f);
//      gl.glTexCoord2f(textureLeft, textureBottom);
//      gl.glVertex3f(-1.0f, -1.0f, -1.0f);
//      gl.glTexCoord2f(textureRight, textureBottom);
//      gl.glVertex3f(-1.0f, -1.0f, 1.0f);
//      gl.glTexCoord2f(textureRight, textureTop);
//      gl.glVertex3f(-1.0f, 1.0f, 1.0f);
//      gl.glTexCoord2f(textureLeft, textureTop);
//      gl.glVertex3f(-1.0f, 1.0f, -1.0f);
//
//      gl.glEnd();
      // Update the rotational position after each refresh.
      angleX += rotateSpeedX;
      angleY += rotateSpeedY;
   }

   /**
    * Called back when the display mode (eg. resolution) has been changed.
    * (not implemented by JOGL)
    */
   public void displayChanged(GLAutoDrawable drawable, boolean modeChanged,
         boolean deviceChanged) {}

   // ------ Implement methods declared in KeyListener ------

   @Override
   public void keyPressed(KeyEvent e) {
      int keyCode = e.getKeyCode();
      switch (keyCode) {
         case KeyEvent.VK_B: // toggle blending on/off
            blendingEnabled = !blendingEnabled;
            break;
         case KeyEvent.VK_L: // toggle light on/off
            isLightOn = !isLightOn;
            break;
         case KeyEvent.VK_F: // switch to the next filter (NEAREST, LINEAR, MIPMAP)
            currTextureFilter = (currTextureFilter + 1) % textures.length;
            break;
         case KeyEvent.VK_PAGE_UP: // zoom-out
            z -= zIncrement;
            break;
         case KeyEvent.VK_PAGE_DOWN: // zoom-in
            z += zIncrement;
            break;
         case KeyEvent.VK_UP: // decrease rotational speed in x
            rotateSpeedX -= rotateSpeedXIncrement;
            break;
         case KeyEvent.VK_DOWN: // increase rotational speed in x
            rotateSpeedX += rotateSpeedXIncrement;
            break;
         case KeyEvent.VK_LEFT: // decrease rotational speed in y
            rotateSpeedY -= rotateSpeedYIncrement;
            break;
         case KeyEvent.VK_RIGHT: // increase rotational speed in y
            rotateSpeedY += rotateSpeedYIncrement;
            break;
      }
   }

   @Override
   public void keyReleased(KeyEvent e) {}

   @Override
   public void keyTyped(KeyEvent e) {}

@Override
public void dispose(GLAutoDrawable arg0) {
	// TODO Auto-generated method stub
	
}
}
