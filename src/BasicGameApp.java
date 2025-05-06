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
import javax.swing.JFrame;
import javax.swing.JPanel;


//*******************************************************************************

public class BasicGameApp implements Runnable, KeyListener {

    //Variable Definition Section
    //Declare the variables used in the program
    //You can set their initial values too

    Image backgroundPic; // Declares "backgroundPic" as an Image
    Image StartScreen;

    //Sets the width and height of the program window
    final int WIDTH = 1000;
    final int HEIGHT = 700;

    // step 1 of creating an array
    public BlueBlock[] trail;
    public BadGuy[] BadGuys;
    int blockNum = 0;
    int score = 0;


    //Declare the variables needed for the graphics
    public JFrame frame;
    public Canvas canvas;
    public JPanel panel;

    public BufferStrategy bufferStrategy;

    public BlueBlock mainBlock;
    public Image pic;
    public Rectangle upRec, downRec, rightRec, leftRec;

    public boolean gameStart;
    public boolean gameEnd = false;


    // Main method definition
    // This is the code that runs first and automatically
    public static void main(String[] args) {
        BasicGameApp ex = new BasicGameApp();   //creates a new instance of the game
        new Thread(ex).start();                 //creates a threads & starts up the code in the run( ) method
    }

    public SoundFile LoseSound;


    // This section is the setup portion of the program
    // Initialize your variables and construct your program objects here.
    public BasicGameApp() { // BasicGameApp constructor

        setUpGraphics();
        canvas.addKeyListener(this);

        pic = Toolkit.getDefaultToolkit().getImage("BlueBlock.png");

        backgroundPic = Toolkit.getDefaultToolkit().getImage("YellowBackground.png"); // Retrieves the backgroundPic "StageBackdrop.jpg" declared as a variable above
        StartScreen = Toolkit.getDefaultToolkit().getImage("StartScreen.jpg"); // Retrieves the backgroundPic "StageBackdrop.jpg" declared as a variable above


        //variable and objects
        //create (construct) the objects needed for the game
        int randomMainXPOS = (int) (Math.random() * 999); // These random integers are used to define where on the x-axis the tomatoes and flowers will appear on the screen
        int randomMainYPOS = (int) (Math.random() * 699);

        mainBlock = new BlueBlock(randomMainXPOS, randomMainYPOS, 0, 0);
        trail = new BlueBlock[10000];
        BadGuys = new BadGuy[6];


        for (int i = 0; i < trail.length; i = i + 1) {
            trail[i] = new BlueBlock(1010, 710, 10, 20);
        }

        for (int i = 0; i < BadGuys.length; i = i + 1) {
            int randomBadXPOS = (int) (Math.random() * 500);
            int randomBadYPOS = (int) (Math.random() * 500);
            BadGuys[i] = new BadGuy(randomBadXPOS, randomBadYPOS);
        }
        for (int i = 0; i < BadGuys.length; i = i + 1) {
            BadGuys[i].pic = Toolkit.getDefaultToolkit().getImage("BadGuy.png");
        }
        mainBlock.pic = Toolkit.getDefaultToolkit().getImage("BlueBlock.png");

        LoseSound = new SoundFile("Junk Crash 03.wav");


    } // end BasicGameApp constructor


//*******************************************************************************
//User Method Section\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\

//
// put your code to do things here.

    // main thread
    // this is the code that plays the game after you set things up
    public void run() {
        //for the moment we will loop things forever.
        while (true) {
            if (gameStart == true) {
                checkKey();
                checkIntersections();

//            System.out.println("This is the score:" + blockNum);
            }

            if (gameEnd == false) {
                checkKey();
                checkIntersections();

                for (int i = 0; i < BadGuys.length; i++) {
                    BadGuys[i].BadMove(mainBlock.xpos, mainBlock.ypos);
                }

                for (int i = 0; i < BadGuys.length; i = i + 1) {
                    if (mainBlock.rec.intersects(BadGuys[i].rec)) {
                        mainBlock.isAlive = false;
                        gameEnd = true;
//                    System.out.println("LOSER");
                        LoseSound.play();
                    }
                }
            }
            render();
            pause(10);
        }
    }

    public void moveThings() {
        //call the move() code for each object
        mainBlock.move();
        //track the trail for each move
        trail[blockNum].trailMove(mainBlock.xpos, mainBlock.ypos);
        blockNum++;

    }

    public void checkKey() {
        if (mainBlock.up == true) {
            mainBlock.dy = -2;
            moveThings();  //move all the game objects
        } else if (mainBlock.down == true) {
            mainBlock.dy = 2;
            moveThings();  //move all the game objects
        } else if (mainBlock.left == true) {
            mainBlock.dx = -2;
            moveThings();  //move all the game objects
        } else if (mainBlock.right == true) {
            mainBlock.dx = 2;
            moveThings();  //move all the game objects

        } else {
            mainBlock.dx = 0;
            mainBlock.dy = 0;
        }

    }

    //Paints things on the screen using bufferStrategy
    private void render() {
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        g.clearRect(0, 0, WIDTH, HEIGHT);

        if (gameStart == false) {
            g.drawImage(StartScreen, 0, 0, WIDTH, HEIGHT, null);
            g.setColor(new Color(164, 176, 245));
            g.setFont(new Font("Arial", Font.BOLD, 18));
            g.drawString("Welcome to Tail Chaser!", 384, 315);
            g.setFont(new Font("Arial", Font.BOLD, 25));
            g.drawString("Press enter to start", 385, 360);

        }

        else if (gameEnd == true) {
            if (score == 0) {
                score = blockNum;
            }
            g.drawImage(StartScreen, 0, 0, WIDTH, HEIGHT, null);
            g.setColor(new Color (164, 176, 245));
            g.setFont(new Font("Arial", Font.BOLD, 25));
            g.drawString("You Lost!", 384, 320);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Final Score: " + score, 384, 340);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Press the space bar", 384, 360);
            g.drawString("to try again", 384, 380);



        }
        else {
            //draw the images
            score = 0;
            g.drawImage(backgroundPic, 0, 0, WIDTH, HEIGHT, null);
            g.drawImage(mainBlock.pic, mainBlock.xpos, mainBlock.ypos, mainBlock.width, mainBlock.height, null);


            for (int i = 0; i < trail.length; i = i + 1) {
                //g.drawImage(pic, trail[i].xpos, trail[i].ypos, trail[i].width, trail[i].height, null);
//            g.drawRect(trail[i].rec.x, trail[i].rec.y, trail[i].rec.width, trail[i].rec.height);
                g.setColor(new Color(164, 176, 245));
                g.fillRect(trail[i].xpos, trail[i].ypos, trail[i].width, trail[i].height);

            }

            for (int i = 0; i < BadGuys.length; i = i + 1) {
                // BadGuys[i].pic = Toolkit.getDefaultToolkit().getImage("BadGuy.png");
                g.drawImage(BadGuys[i].pic, BadGuys[i].xpos, BadGuys[i].ypos, BadGuys[i].width, BadGuys[i].height, null);
//            g.drawRect(BadGuys[i].rec.x, BadGuys[i].rec.y, BadGuys[i].rec.width, BadGuys[i].rec.height);
            }

            g.setColor(new Color(186, 43, 43));
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Score: " + blockNum, 30, 30);

        }

        g.dispose();
        bufferStrategy.show();
    }

    //Pauses or sleeps the computer for the amount specified in milliseconds
    public void pause(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
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
        for (int i = 0; i < trail.length; i = i + 1) {
            if (mainBlock.rec.intersects(trail[i].rec) && blockNum - i >= 75) {
//                System.out.println("Intersect");
                double rise = trail[blockNum - 1].ypos - trail[blockNum - 2].ypos;
                double run = trail[blockNum - 1].xpos - trail[blockNum - 2].xpos;
                double slope = rise / run;
                System.out.println("slope: " + slope);
                double perslope = 1 / (-1 * slope);
                System.out.println("perslope: " + perslope);



            
            }
//                if (mainBlock.xpos > mainBlock.xposOld) { // Moving right
//                    if (mainBlock.ypos > mainBlock.yposOld) { // Moving down
//                        System.out.println("Moving down-right, move left down up right");
//                    } else if (mainBlock.ypos < mainBlock.yposOld) { // Moving up
//                        System.out.println("Moving up-right, move left up down right down");
//                    } else {
//                        System.out.println("Moving right, move up and down");
//                    }
//
//                } else if (mainBlock.xpos < mainBlock.xposOld) { // Moving left
//                    if (mainBlock.ypos > mainBlock.yposOld) { // Moving down
//                        System.out.println("Moving down-left, move down right up left");
//                    } else if (mainBlock.ypos < mainBlock.yposOld) { // Moving up
//                        System.out.println("Moving up-left, move down left up right");
//                    } else {
//                        System.out.println("Moving left, move right");
//                    }
//
//                } else if (mainBlock.xpos == mainBlock.xposOld) {
//                    if (mainBlock.ypos > mainBlock.yposOld) {
//                        System.out.println("Moving down, move up");
//                    } else if (mainBlock.ypos < mainBlock.yposOld) {
//                        System.out.println("Moving up, move down");
//                    }
//                }
//            }
//        }
        }
    }

    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == 38) {
            mainBlock.up = true;
        }

        if (keyCode == 40) {
            mainBlock.down = true;
        }

        if (keyCode == 37) { // left
            mainBlock.left = true;
        }

        if (keyCode == 39) { // right
            mainBlock.right = true;
        }
//
    }

    public void keyReleased(KeyEvent e){
            int keyCode = e.getKeyCode();
            if (keyCode == 38) { // up
                mainBlock.up = false;
            }

            if (keyCode == 40) { // down
                mainBlock.down = false;
            }
            if (keyCode == 37) { // left
                mainBlock.left = false;
            }

            if (keyCode == 39) { // right
                mainBlock.right = false;
            }

            if (keyCode == 10 && gameStart == false){
                gameStart = true;
            }

        if (keyCode == 32 && gameEnd == true){

            for (int i = 0; i < BadGuys.length; i = i + 1) {
                int randomBadXPOS = (int) (Math.random() * 999);
                int randomBadYPOS = (int) (Math.random() * 999);
                BadGuys[i] = new BadGuy(randomBadXPOS, randomBadYPOS);

            }
            for (int i = 0; i < BadGuys.length; i = i + 1) {
                BadGuys[i].pic = Toolkit.getDefaultToolkit().getImage("BadGuy.png");
            }

            for (int i = 0; i < trail.length; i = i + 1) {
                trail[i] = new BlueBlock(1010, 710, 10, 20);
            }


            gameStart = true;
            gameEnd = false;
            blockNum = 0;

        }


    }

}
