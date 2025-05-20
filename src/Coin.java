import java.awt.*;

public class Coin {

    //VARIABLE DECLARATION SECTION

    public Image pic; // An image that represents visual of the coin
    public int xpos; // the x position of the coin
    public int ypos; // the y position of the coin
    public int width; // The width of the coin
    public int height; // The height of the coin
    public boolean isAlive; // a boolean to denote if the coin is alive or dead.
    public Rectangle rec; // A rectangle used for collision detection
    public Color color; // The color of the block


    // Constructor, called when a BlueBlock object is created
    public Coin(int pXpos, int pYpos) {

        xpos = pXpos; // Set initial x position to a parameter
        ypos = pYpos; // Set initial y position to a parameter
        width = 50; // Set fixed width of block
        height = 50; // Set fixed height of block
        isAlive = true; // Set the coin as intiially alive
        rec = new Rectangle(xpos, ypos, width, height); // Create a rectangle based on current position and size


    }
}