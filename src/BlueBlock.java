import java.awt.*;

public class BlueBlock {

    //VARIABLE DECLARATION SECTION

    public Image pic; // An image that represents visual of the block
    public int xpos; // the x position of the block
    public int ypos; // the y position of the block
    public int width; // The width of the block
    public int height; // The height of the block
    public boolean isAlive; // a boolean to denote if the block is alive or dead.
    public int dx = 0; // the speed of the block in the x direction
    public int dy = 0; // the speed of the block in the y direction
    public Rectangle rec; // A rectangle used for collision detection
    public Color color; // The color of the block



    // METHOD DEFINITION SECTION
    public boolean up; // True if the block is moving up
    public boolean down; // True if the block is moving down
    public boolean left; // True if the block is moving left
    public boolean right; // True if the block is moving right
    public int xposOld,yposOld; // Previous position values, used for trail or undoing moves


    // Constructor, called when a BlueBlock object is created
    public BlueBlock(int pXpos, int pYpos, int dxParameter, int dyParameter) {

        xpos = pXpos; // Set initial x position to a parameter
        ypos = pYpos; // Set initial y position to a parameter
        width = 5; // Set fixed width of block
        height = 5; // Set fixed height of block
        dx = dxParameter; // Set the horizontal speed to the parameter
        dy = dyParameter; // Set vertical speed to the parameter
        isAlive = true; // Set the block as intiially alive
        rec = new Rectangle(xpos, ypos, width, height); // Create a rectangle based on current position and size


    } // Method: move
// Moves the block based on dx and dy and wraps around screen edges
    public void move() {
        xposOld=xpos; // Save current x-position as previous
        yposOld=ypos; // Save current y-position as previous
        xpos = xpos + dx; // Update x-position based on dx
        ypos = ypos + dy; // Update y-position based on dy



        // If the block moves past the right edge, wrap to the left
        if (xpos > 1000 + width) {
            xpos = 0 - width;
        }

        // If the block moves past the left edge, wrap to the right
        if (xpos < 0 - width) {
            xpos = 1000 + width;
        }

        // If the block moves above the top edge, wrap to the bottom
        if (ypos < 0 - height){
            ypos = 700 + height;
        }

        // If the block moves below the bottom edge, wrap to the top
        if (ypos > 700 + height) {
            ypos = 0 - height;
        }

        // Update the rectangle for collision detection with the new position
        rec = new Rectangle(xpos, ypos, width, height);
    }

    // Method: trailMove
    // Instantly moves the block to a specific position
    public void trailMove(int x, int y) {
        xpos = x; // Set x position to a given value
        ypos = y; // Set y position to a given value
        rec = new Rectangle(xpos, ypos, width, height); // Update rectangle with new position
    }
}