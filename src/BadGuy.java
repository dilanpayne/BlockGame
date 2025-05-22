import java.awt.*;

public class BadGuy {

    //VARIABLE DECLARATION SECTION
    //Here's where you state which variables you are going to use for the BadGuy object.

    public Image pic; // Image for the BadGuy
    public int xpos; //the x position
    public int ypos; //the y position
    public int width; // Width of the BadGuy (set in constructor)
    public int height; // Height of the BadGuy (set in constructor)
    public boolean isAlive; //a boolean to denote if the hero is alive or dead.
    public int dx = 0; //the speed of the BadGuy in the x direction
    public int dy = 0; //the speed of the BadGuy in the y direction
    public Rectangle rec; // Rectangle used for collision detection


    public BadGuy(int pXpos, int pYpos) {

        xpos = pXpos; // set x position to a parameter
        ypos = pYpos; // set y position to a parameter
        width = 50; // set width to 50 pixels
        height = 50; // set height to 50 pixels
        dx = 1; // initial horizontal speed
        dy = 1; // initial vertical speed
        isAlive = true; // Starts off alive
        rec = new Rectangle(xpos, ypos, width, height); // Create rectangle for collision detection


    } // constructor
// Method to update the BadGuy's position based on a target
    public void badMove(int mBXpos, int mBYpos) {

        if (xpos < mBXpos) {
            dx = Math.abs(dx);
        } // move right if target is to right

        else if (xpos >= mBXpos) {
            dx = -1 * Math.abs(dx);
        } // move left if target is to left

        if (xpos < 0) {
            dx = -dx;
        } // right/left bounce

        else if (xpos >= 950) {
            dx = -dx;
        } // right/left bounce

        if (ypos < mBYpos) {
            dy = Math.abs(dy);
        } // Move down if target is below

        else if (ypos >= mBYpos) {
            dy = -1 * Math.abs(dy);
        } // Move up if target is above

        if (ypos < 0) {
            dy = -dy;
        } // top edge of screen bounce

        else if (ypos >= 650) {
            dy = -dy;
        } // bottom edge of screen bounce


        // Update position based on speed
        xpos = xpos + dx;
        ypos = ypos + dy;

// Update the rectangle for collision detection with new position
        rec = new Rectangle(xpos, ypos, width, height);
    }
}