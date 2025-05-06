import java.awt.*;

public class BadGuy {

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


    public BadGuy(int pXpos, int pYpos) {

        xpos = pXpos;
        ypos = pYpos;
        width = 50;
        height = 50;
        dx = 1;
        dy = 1;
        isAlive = true;
        hits = 0;
        rec = new Rectangle(xpos, ypos, width, height);


    } // constructor

    public void BadMove(int MBXpos, int MBYpos) {
//        System.out.println(MBXpos);
//        System.out.println(xpos);
        if (xpos < 0) {
            dx = -dx;
        } // right/left bounce

        else if (xpos >= 950) {
            dx = -dx;
        } // right/left bounce

        else if (xpos < MBXpos) {
            dx = Math.abs(dx);
        } // right/left bounce

        else if (xpos >= MBXpos) {
            dx = -1 * Math.abs(dx);
        } // right/left bounce

        if (ypos < 0) {
            dy = -dy;
        }

        else if (ypos >= 650) {
            dy = -dy;
        }

        else if (ypos < MBYpos) {
            dy = Math.abs(dy);
        }

        else if (ypos >= MBYpos) {
            dy = -1 * Math.abs(dy);
        }
        xpos = xpos + dx;
        ypos = ypos + dy;

        rec = new Rectangle(xpos, ypos, width, height);
    }
}