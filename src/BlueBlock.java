import java.awt.*;

public class BlueBlock {

    //VARIABLE DECLARATION SECTION
    //Here's where you state which variables you are going to use.

    public Image pic;
    public int xpos;                //the x position
    public int ypos;                //the y position
    public int width;
    public int height;
    public boolean isAlive;            //a boolean to denote if the hero is alive or dead.
    public int dx = 0;                  //the speed of the hero in the x direction
    public int dy = 0;                 //the speed of the hero in the y direction
    public Rectangle rec;
    public int hits;
    public Color color;



    // METHOD DEFINITION SECTION
    public boolean up;
    public boolean down;
    public boolean left;
    public boolean right;
    public int xposOld,yposOld;

    //This is a constructor that takes 3 parameters.  This allows us to specify the object's name and position when we build it.
    // if you put in a String, an int and an int the program will use this constructor instead of the one above.

    public BlueBlock(int pXpos, int pYpos, int dxParameter, int dyParameter) {

        xpos = pXpos;
        ypos = pYpos;
        width = 30;
        height = 30;
        dx = dxParameter;
        dy = dyParameter;
        isAlive = true;
        hits = 0;
        rec = new Rectangle(xpos, ypos, width, height);
        color = Color.red;


    } // constructor

    public void move() {
        xposOld=xpos;
        yposOld=ypos;
        xpos = xpos + dx;
        ypos = ypos + dy;



        if (xpos > 1000 + width) {
            xpos = 0 - width;
        } // right/left bounce

        if (xpos < 0 - width) {
            xpos = 1000 + width;
        } // right/left bounce

        if (ypos < 0 - height){
            ypos = 700 + height;
        }

        if (ypos > 700 + height) {
            ypos = 0 - height;
        }

        rec = new Rectangle(xpos, ypos, width, height);
    }

    public void trailMove(int x, int y) {
        xpos = x;
        ypos = y;
        rec = new Rectangle(xpos, ypos, width, height);
    }
}//end of the Mouse object class  definition
