//Basic Game Application
// Basic Object, Image, Movement
// Threaded

//*******************************************************************************
//Import Section
//Add Java libraries needed for the game
//import java.awt.Canvas;

//Graphics Libraries
import org.w3c.dom.css.RGBColor;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JPanel;


//*******************************************************************************

public class BasicGameApp implements Runnable, KeyListener {

    //Variable Definition Section
    //Declare the variables used in the program

    Image backgroundPic; // Declares "backgroundPic" as an Image
    Image StartScreen; // Declares "startScreen" as an Image

    //Sets the width and height of the program window
    final int WIDTH = 1000;
    final int HEIGHT = 700;

    // step 1 of creating an array
    public BlueBlock[] trail; // Array to store player movement trail
    public BadGuy[] BadGuys; // Array to store enemy objects
    Coin Coin1;
    int blockNum = 0; // Index for trail array
    int score = 0; // Score counter

    // Collision tracking booleans
    public boolean isIntersecting = false;
    public boolean currentlyIntersecting = false;


    //Declare the variables needed for the graphics
    public JFrame frame;
    public Canvas canvas;
    public JPanel panel;

    public BufferStrategy bufferStrategy;

    public BlueBlock mainBlock; // The main character block
    public Image pic; // Image for the main block

    public boolean gameStart; // Whether the game has started
    public boolean gameEnd = false; // Whether the game has ended
    public Polygon[] closedLoop = new Polygon[20]; // For completed areas
    int loopNum = 0; // Number of completed loops tracked
    int red = 10;
    int coinSpeed = 0;
    int coinCount = 0; // Set a count to track how many times coin and main block have intersected
    // public ArrayList<Polygon> closedLoop;


    // Main method definition
    // This is the code that runs first and automatically
    public static void main(String[] args) {
        BasicGameApp ex = new BasicGameApp();   //creates a new instance of the game
        new Thread(ex).start();  //creates a threads & starts up the code in the run( ) method
    }

    public SoundFile LoseSound; // Sound to play when the player loses
    public SoundFile CoinSound; // Sound to play when the player loses


    // This section is the setup portion of the program
    public BasicGameApp() { // BasicGameApp constructor

        setUpGraphics();
        canvas.addKeyListener(this);

        pic = Toolkit.getDefaultToolkit().getImage("BlueBlock.png"); // Load player image

        backgroundPic = Toolkit.getDefaultToolkit().getImage("YellowBackground.png"); // Retrieves the backgroundPic declared as a variable above
        StartScreen = Toolkit.getDefaultToolkit().getImage("StartScreen.jpg"); // Retrieves the backgroundPic declared as a variable above

        //variable and objects
        //create (construct) the objects needed for the game
        int randomMainXPOS = (int) (Math.random() * 999); // These random integers are used to define where on the x-axis the objects will appear on the screen
        int randomMainYPOS = (int) (Math.random() * 699);


        mainBlock = new BlueBlock(randomMainXPOS, randomMainYPOS, 0, 0); // Creates main block at a random position
        trail = new BlueBlock[10000]; // Large array to track player's trail
        BadGuys = new BadGuy[2]; // Create 2 enemies

        int newrandomCoinXPos = (int) (Math.random() * 950); // create a coin
        int newrandomCoinYpos = (int) (Math.random() * 650);
        Coin1 = new Coin(newrandomCoinXPos, newrandomCoinYpos);

// Fill in trail with BlueBlocks off screen initially
        for (int i = 0; i < trail.length; i = i + 1) {
            trail[i] = new BlueBlock(1010, 710, 10, 20);
        }

        // Define position of BadGuys at random position
        for (int i = 0; i < BadGuys.length; i = i + 1) {
            int randomBadXPOS = (int) (Math.random() * 999);
            int randomBadYPOS = (int) (Math.random() * 700);
            BadGuys[i] = new BadGuy(randomBadXPOS, randomBadYPOS);
        }

        // Render image for each BadGuy
        for (int i = 0; i < BadGuys.length; i = i + 1) {
            BadGuys[i].pic = Toolkit.getDefaultToolkit().getImage("BadGuy.png");
        }

        // Render image for main block
        mainBlock.pic = Toolkit.getDefaultToolkit().getImage("BlueBlock.png");

        LoseSound = new SoundFile("Junk Crash 03.wav"); // Load losing sound effect
        CoinSound = new SoundFile("Bell 01.wav"); // Load losing sound effect


    } // end BasicGameApp constructor


//*******************************************************************************
//User Method Section\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

    // main thread
    public void run() { // Game loop that runs continuously
        while (true) {

            if (gameStart == true && gameEnd == false) { // If game is active
                checkKey(); // Handle movement
                checkIntersections(); // Check if player intersects trail

                // End game before closedLoop fills up
                if (loopNum == 20) {
                    gameEnd = true;
                }

                // Move each BadGuy toward main block
                for (int i = 0; i < BadGuys.length; i++) {
                    BadGuys[i].BadMove(mainBlock.xpos, mainBlock.ypos);
                }

                 // Check for collision between player and enemies
                for (int i = 0; i < BadGuys.length; i = i + 1) {
                    if (mainBlock.rec.intersects(BadGuys[i].rec)) {
                        mainBlock.isAlive = false; // Player dies
                        gameEnd = true; // Game ends
                        LoseSound.play(); // Play losing sound
                    }
                }
                // Check for collision between player and coin
                if (mainBlock.rec.intersects(Coin1.rec)) {
                    int newrandomCoinXPos = (int) (Math.random() * 950); //move the coin
                    int newrandomCoinYpos = (int) (Math.random() * 650);
                    Coin1 = new Coin(newrandomCoinXPos, newrandomCoinYpos);
                    Coin1.pic = Toolkit.getDefaultToolkit().getImage("coin.png");
                    coinSpeed = coinSpeed + 1; // boost the speed of the player
                    coinCount++;
                    if (coinCount >=3) {
                        coinSpeed = 0;
                    }
                    CoinSound.play();

                }

            }
            render(); // Draw everything on the screen
            pause(10);
        }
    }

 // Moving the player and updating the trail
    public void moveThings() {
        mainBlock.move(); // Move player
        trail[blockNum].trailMove(mainBlock.xpos, mainBlock.ypos); // Copy position to trail
        blockNum++; // Move to next trail index


    }
 // Handle movement keys
    public void checkKey() {
        if (mainBlock.up == true) {
            mainBlock.dy = -1-coinSpeed;
            moveThings();  //move all the game objects
        } else if (mainBlock.down == true) {
            mainBlock.dy = 1+coinSpeed;
            moveThings();  //move all the game objects
        } else if (mainBlock.left == true) {
            mainBlock.dx = -1-coinSpeed;
            moveThings();  //move all the game objects
        } else if (mainBlock.right == true) {
            mainBlock.dx = 1+coinSpeed;
            moveThings();  //move all the game objects

        } else { // No movement
            mainBlock.dx = 0;
            mainBlock.dy = 0;
        }

    }

    //Paints things on the screen using bufferStrategy
    private void render() {
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        g.clearRect(0, 0, WIDTH, HEIGHT);

        // Start screen
        if (gameStart == false) {
            g.drawImage(StartScreen, 0, 0, WIDTH, HEIGHT, null);
            g.setColor(new Color(164, 176, 245));
            g.setFont(new Font("Arial", Font.BOLD, 22));
            g.drawString("Welcome to Tail Chaser!", 370, 315);
            g.setFont(new Font("Arial", Font.BOLD, 19));
            g.drawString("Use the arrow keys to move.", 370, 360);
            g.drawString("Watch out for bad guys!", 370, 385);
            g.drawString("Press enter to start", 370, 410);

        }

        // End screen
        else if (gameEnd == true) {
            if (score == 0) {
                score = blockNum + (100 * loopNum); // Calculate final score
            }
            g.drawImage(StartScreen, 0, 0, WIDTH, HEIGHT, null);
            g.setColor(new Color(164, 176, 245));
            g.setFont(new Font("Arial", Font.BOLD, 25));
            g.drawString("Game Over!", 384, 320);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Final Score: " + score, 384, 340);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Press the space bar", 384, 360);
            g.drawString("to try again", 384, 380);
            // draw The Poly in Red of the closed loop.
            g.setColor(new Color(164, 176, 245, 100));


        }
        // Active game screen
        else {
            score = 0; // Reset score
            g.drawImage(backgroundPic, 0, 0, WIDTH, HEIGHT, null); // Draw background
            g.drawImage(mainBlock.pic, mainBlock.xpos, mainBlock.ypos, mainBlock.width, mainBlock.height, null); // Draw player


                // Draw closed loops
                for (int i = 0; i < loopNum; i++) {
                    g.setColor(new Color(164, 176, 245));
                    g.fillPolygon(closedLoop[i]);
                }

                // Draw trail
                for (int i = 0; i < trail.length; i = i + 1) {
                    g.setColor(new Color(164, 176, 245));
                    g.fillRect(trail[i].xpos, trail[i].ypos, trail[i].width, trail[i].height);

                }

                // Draw enemies
                for (int i = 0; i < BadGuys.length; i = i + 1) {
                    g.drawImage(BadGuys[i].pic, BadGuys[i].xpos, BadGuys[i].ypos, BadGuys[i].width, BadGuys[i].height, null);
                }

                // Draw coin
                g.drawImage(Coin1.pic, Coin1.xpos, Coin1.ypos, Coin1.width, Coin1.height, null);

                // Draw score
                g.setColor(new Color(186, 43, 43));
                g.setFont(new Font("Arial", Font.BOLD, 20));
                g.drawString("Score: " + (blockNum + (100 * loopNum)), 30, 30);


            }

            g.dispose();
            bufferStrategy.show();
        }

        //Pauses or sleeps the computer for the amount specified in milliseconds
        public void pause(int time) {
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                e.printStackTrace();  // Or just leave the block empty if you prefer
            }
        }


    //Graphics setup method
    private void setUpGraphics() {
        frame = new JFrame("Application Template");   //Create the program window or frame.  Names it.

        panel = (JPanel) frame.getContentPane();  //sets up a JPanel which is what goes in the frame
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));  //sizes the JPanel
        panel.setLayout(null);   //set the layout

        // creates a canvas which is a blank rectangular area of the screen onto which the application can draw
        // and trap input events (Mouse and Keyboard events)
        canvas = new Canvas();
        canvas.setBounds(0, 0, WIDTH, HEIGHT);
        canvas.setIgnoreRepaint(true);

        panel.add(canvas);  // adds the canvas to the panel.

        // frame operations
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //makes the frame close and exit nicely
        frame.pack();  //adjusts the frame and its contents so the sizes are at their default or larger
        frame.setResizable(false);   //makes it so the frame cannot be resized
        frame.setVisible(true);      //IMPORTANT!!!  if the frame is not set to visible it will not appear on the screen!

        // sets up things so the screen displays images nicely.
        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();
        canvas.requestFocus();
//        System.out.println("DONE graphic setup");


    }

    public void keyTyped(KeyEvent e) {

    }

    public void checkIntersections() {
        currentlyIntersecting = false; // Reset at the start of the check

        for (int i = 0; i < blockNum - 75; i++) { // Ensure a minimum distance to avoid false positives

            if (mainBlock.rec.intersects(trail[i].rec)) { // Check if the main block intersects with this trail segment
                currentlyIntersecting = true; // Update boolean to show current intersection

                if (isIntersecting == false) { // If this is the first time intersecting
                    isIntersecting = true; // Record that we are now intersecting

                    // Create a polygon from the trail points
                    closedLoop[loopNum] = new Polygon();
                    red = red + 10;

                    for (int j = i; j < blockNum; j++) { // Add points to the polygon from this point in the trail forward
                        closedLoop[loopNum].addPoint(trail[j].xpos, trail[j].ypos);
                    }
                    loopNum++; // Move to the next loop index for future usage

                    break; // Exit after the first intersection is handled
                }

                break; // Don't keep checking more once we know we're intersecting
            }
        }

        if ((currentlyIntersecting == false) && (isIntersecting == true)) {
            // If we are no longer intersecting but were before, reset the boolean
            isIntersecting = false;
        }
    }


    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode(); // Get the key code for the pressed key

        if (keyCode == 38) { // Up arrow key
            mainBlock.up = true; // Move mainBlock up
        }

        if (keyCode == 40) { // Down arrow key
            mainBlock.down = true; // Move mainBlock down
        }

        if (keyCode == 37) { // left arrow key
            mainBlock.left = true; // Move mainBlock left
        }

        if (keyCode == 39) { // right arrow key
            mainBlock.right = true; // Move mainBlock right
        }
//
    }

    public void keyReleased(KeyEvent e){
        int keyCode = e.getKeyCode(); // Get the key code for the released key
        if (keyCode == 38) { // If up arrow is released
            mainBlock.up = false; // Stop upwards movement
        }

        if (keyCode == 40) { // If down arrow is released
            mainBlock.down = false; // Stop downward movement
        }
        if (keyCode == 37) { // If left arrow is released
            mainBlock.left = false; // Stop leftward movement
        }

        if (keyCode == 39) { // If right arrow is released
            mainBlock.right = false; // Stop rightward movement
        }

        if (keyCode == 10 && gameStart == false) { // If enter key has been pressed and the game is yet to start
            gameStart = true; // Start the game
        }

        if (keyCode == 32 && gameEnd == true){ // If space key is pressed after the game ends

            // Reset and re-randomize all bad guys
            for (int i = 0; i < BadGuys.length; i = i + 1) {
                int randomBadXPOS = (int) (Math.random() * 950);
                int randomBadYPOS = (int) (Math.random() * 650);
                BadGuys[i] = new BadGuy(randomBadXPOS, randomBadYPOS);

            }

            // Reload bad guys
            for (int i = 0; i < BadGuys.length; i = i + 1) {
                BadGuys[i].pic = Toolkit.getDefaultToolkit().getImage("BadGuy.png");
            }

            // Reset trail positions
            for (int i = 0; i < trail.length; i = i + 1) {
                trail[i] = new BlueBlock(1010, 710, 10, 20);
            }

            // Clear closed loops
            for (int i = 0; i < closedLoop.length; i++) {
                closedLoop[i] = null;
            }

            loopNum = 0; // Reset loop counter
            gameStart = true; // Restart the game
            gameEnd = false; // Mark the game as ongoing
            blockNum = 0; // Reset trail length
            coinSpeed = 0; // Reset speed boost from coin

        }
    }
}