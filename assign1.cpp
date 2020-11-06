/*
  CSCI 480 Computer Graphics
  Assignment 1: Height Fields
  Timothy Wang
*/

#include <iostream>
#include <stdlib.h>
#include <OpenGL/gl.h>
#include <OpenGL/glu.h>
#include <GLUT/glut.h>
#include <pic.h>

int g_iMenuId;

int g_vMousePos[2] = {0, 0};
int g_iLeftMouseButton = 0;    /* 1 if pressed, 0 if not */
int g_iMiddleMouseButton = 0;
int g_iRightMouseButton = 0;

typedef enum { ROTATE, TRANSLATE, SCALE } CONTROLSTATE;

CONTROLSTATE g_ControlState = ROTATE;

/* state of the world */
float g_vLandRotate[3] = {0.0, 0.0, 0.0};
float g_vLandTranslate[3] = {0.0, 0.0, 0.0};
float g_vLandScale[3] = {1.0, 1.0, 1.0};

/* see <your pic directory>/pic.h for type Pic */
Pic * g_pHeightData;

GLenum myMode = GL_FILL;

/* Write a screenshot to the specified filename */
void saveScreenshot (char *filename)
{
  int i, j;
  Pic *in = NULL;

  if (filename == NULL)
    return;

  /* Allocate a picture buffer */
  in = pic_alloc(640, 480, 3, NULL);

  printf("File to save to: %s\n", filename);

  for (i=479; i>=0; i--) {
    glReadPixels(0, 479-i, 640, 1, GL_RGB, GL_UNSIGNED_BYTE,
                 &in->pix[i*in->nx*in->bpp]);
  }

  if (jpeg_write(filename, in))
    printf("File saved Successfully\n");
  else
    printf("Error in Saving\n");

  pic_free(in);
}

void myinit()
{
  /* setup gl view here */
  glClearColor(0.0, 0.0, 0.0, 0.0); //Background color
  // enable depth buffering
  glEnable(GL_DEPTH_TEST); 
  // interpolate colors during rasterization
  glShadeModel(GL_SMOOTH);  
}

void display()
{
  /* draw 1x1 cube about origin */
  /* replace this code with your height field implementation */
  /* you may also want to precede it with your 
rotation/translation/scaling */

  glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
  glMatrixMode(GL_MODELVIEW);
  glLoadIdentity(); // reset transformation

  //Set up camera
  gluLookAt(0,0,5,0,-1,0,0,1,0);

  //Take input from mouse data to translate height field
  glTranslatef(g_vLandTranslate[0], g_vLandTranslate[1], g_vLandTranslate[2]);

  //Take input from mouse data to rotate height field
  glRotatef(g_vLandRotate[0], 1,0,0);
  glRotatef(g_vLandRotate[1], 0,1,0);
  glRotatef(g_vLandRotate[2], 0,0,1);

  //Take input from mouse data to scale height field
  glScalef(g_vLandScale[0], g_vLandScale[1], g_vLandScale[2]);

  glPolygonMode(GL_FRONT_AND_BACK, myMode); //myMode --> either GL_POINT, GL_Line, or GL_FILL

  //Go through all of the points in the image, rendering using Triangle Strips
  for (int i = 0; i < (g_pHeightData->nx - 1); i++){

    //Start new triangle strip
    glBegin(GL_TRIANGLE_STRIP);

    //Render points down the triangle strip
    for (int j = 0; j < g_pHeightData->ny; j++){

      //Get grayscale value from two points:
      float gValue1 = (float) PIC_PIXEL(g_pHeightData, i, j, 0);
      float gValue2 = (float) PIC_PIXEL(g_pHeightData, i+1, j, 0);
      
      //Scale i to a -1 to 1 range
      float new_i = ( (float)i - (g_pHeightData->nx) / 2.0f) / ((g_pHeightData->nx) / 2.0f);

      float new_iplus = ( (float)i + 1 - (g_pHeightData->nx) / 2.0f) / ((g_pHeightData->nx) / 2.0f);

      //Scale j to a -1 to 1 range

      float new_j = ((float)j - (g_pHeightData->ny) / 2.0f) / ((g_pHeightData->ny) / 2.0f);

      //Render pixel:

      //Get color value by scaling gValue1 and gValue2. New value will be between 0 and 1.
      float gValue1_color = gValue1/256;
      float gValue2_color = gValue2/256;

      //Determine color of pixel.
      glColor3f(gValue1_color, gValue1_color, 1.0f);

      //Scale down z-value of the point.
      float zValue1 = gValue1/255.0f/2.5f; // -> Scale it down
      glVertex3f(new_i, new_j, zValue1); //Render point

      //Repeat for the second pixel
      glColor3f(gValue2_color, gValue2_color, 1.0f);

      float zValue2 = gValue2/255.0f/2.5f; // -> Scale it down
      glVertex3f(new_iplus, new_j, zValue2);
    }
    //End Triangle strip. Move on to the second triangle strip.
    glEnd();

  }  

  glutSwapBuffers(); // double buffer flush
}

//When the window containing the height field first opens and whenever the window resizes.
void reshape(int x, int y) {

  glViewport(0, 0, x, y);
  glMatrixMode(GL_PROJECTION);
  glLoadIdentity();
  //Sets up a perspective projection matrix
  gluPerspective(60.0, 1.0 * x / y, 0.01, 10.0);
  glMatrixMode(GL_MODELVIEW); 
}

void menufunc(int value)
{
  switch (value)
  {
    case 0:
      exit(0);
      break;
  }
}

void doIdle()
{
  /* do some stuff... */


  /* make the screen update */
  glutPostRedisplay();
}

/* converts mouse drags into information about 
rotation/translation/scaling */
void mousedrag(int x, int y)
{
  int vMouseDelta[2] = {x-g_vMousePos[0], y-g_vMousePos[1]};
  
  switch (g_ControlState)
  {
    case TRANSLATE:  
      if (g_iLeftMouseButton)
      {
        g_vLandTranslate[0] += vMouseDelta[0]*0.01;
        g_vLandTranslate[1] -= vMouseDelta[1]*0.01;
      }
      if (g_iMiddleMouseButton)
      {
        g_vLandTranslate[2] += vMouseDelta[1]*0.01;
      }
      break;
    case ROTATE:
      if (g_iLeftMouseButton)
      {
        g_vLandRotate[0] += vMouseDelta[1];
        g_vLandRotate[1] += vMouseDelta[0];
      }
      if (g_iMiddleMouseButton)
      {
        g_vLandRotate[2] += vMouseDelta[1];
      }
      break;
    case SCALE:
      if (g_iLeftMouseButton)
      {
        g_vLandScale[0] *= 1.0+vMouseDelta[0]*0.01;
        g_vLandScale[1] *= 1.0-vMouseDelta[1]*0.01;
      }
      if (g_iMiddleMouseButton)
      {
        g_vLandScale[2] *= 1.0-vMouseDelta[1]*0.01;
      }
      break;
  }
  g_vMousePos[0] = x;
  g_vMousePos[1] = y;
}

void mouseidle(int x, int y)
{
  g_vMousePos[0] = x;
  g_vMousePos[1] = y;
}

void mousebutton(int button, int state, int x, int y)
{

  switch (button)
  {
    case GLUT_LEFT_BUTTON:
      g_iLeftMouseButton = (state==GLUT_DOWN);
      break;
    case GLUT_MIDDLE_BUTTON:
      g_iMiddleMouseButton = (state==GLUT_DOWN);
      break;
    case GLUT_RIGHT_BUTTON:
      g_iRightMouseButton = (state==GLUT_DOWN);
      break;
  }
 
  switch(glutGetModifiers())
  {
    case GLUT_ACTIVE_CTRL:
      g_ControlState = TRANSLATE;
      break;
    case GLUT_ACTIVE_SHIFT:
      g_ControlState = SCALE;
      break;
    default:
      g_ControlState = ROTATE;
      break;
  }

  g_vMousePos[0] = x;
  g_vMousePos[1] = y;
}

//You can use z to change g_ControlState (for Mac keyboards, where the CTRL button doesn't work).
void replaceCTRL(unsigned char key, int x, int y) {

  //Use 'z' instead of control to translate height field. (For Mac keyboards)
  if (key == 'z'){
    g_ControlState = TRANSLATE;
  }

  //Pressing 'p' will switch the rendering to points. 'l' to lines (wireframe). 'f' to fill (solid triangles).
  if (key == 'p'){
    myMode = GL_POINT;
  }
  if (key == 'l'){
    myMode = GL_LINE;
  }
  if (key == 'f'){
    myMode = GL_FILL;
  }

}


int main (int argc, char ** argv)
{
  if (argc<2)
  {  
    printf ("usage: %s heightfield.jpg\n", argv[0]);
    exit(1);
  }

  g_pHeightData = jpeg_read(argv[1], NULL);
  if (!g_pHeightData)
  {
    printf ("error reading %s.\n", argv[1]);
    exit(1);
  }

  glutInit(&argc,argv);
  
  /*
    create a window here..should be double buffered and use depth testing
  
    the code past here will segfault if you don't have a window set up....
    replace the exit once you add those calls.
  */

  //Based on ColorCube code
    
  // request double buffer
  glutInitDisplayMode(GLUT_DOUBLE | 
    GLUT_DEPTH | GLUT_RGBA);
    
  // set window size
  glutInitWindowSize(640, 480);
    
  // set window position
  glutInitWindowPosition(0, 0);
    
  // creates a window
  glutCreateWindow("Assignment 1");

  /* tells glut to use a particular display function to redraw */
  glutDisplayFunc(display);
  
  /* allow the user to quit using the right mouse button menu */
  g_iMenuId = glutCreateMenu(menufunc);
  glutSetMenu(g_iMenuId);
  glutAddMenuEntry("Quit",0);
  glutAttachMenu(GLUT_RIGHT_BUTTON);
  
  /* replace with any animate code */
  glutIdleFunc(doIdle);

  glutReshapeFunc(reshape);

  /* callback for mouse drags */
  glutMotionFunc(mousedrag);
  /* callback for idle mouse movement */
  glutPassiveMotionFunc(mouseidle);
  /* callback for mouse button changes */
  glutMouseFunc(mousebutton);

  //Callback for keyboard
  glutKeyboardFunc(replaceCTRL);

  /* do initialization */
  myinit();

  //Enter main loop
  glutMainLoop();
  return(0);
}
