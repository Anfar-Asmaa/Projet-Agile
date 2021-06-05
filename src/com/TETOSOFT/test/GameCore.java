package com.TETOSOFT.test;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.print.DocFlavor.URL;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.TETOSOFT.graphics.ScreenManager;
import com.TETOSOFT.input.InputManager;
import com.TETOSOFT.tilegame.TileMapDrawer;

/**
    Simple abstract class used for testing. Subclasses should
    implement the draw() method.
*/
public abstract class GameCore {

    protected static final int FONT_SIZE = 18;

    private static final DisplayMode POSSIBLE_MODES[] = {
        new DisplayMode(800, 600, 32, 0),
        /*new DisplayMode(800, 600, 16, 0),
        new DisplayMode(800, 600, 24, 0),
        new DisplayMode(640, 480, 16, 0),
        new DisplayMode(640, 480, 32, 0),
        new DisplayMode(640, 480, 24, 0),
        new DisplayMode(1024, 768, 16, 0),
        new DisplayMode(1024, 768, 32, 0),
        new DisplayMode(1024, 768, 24, 0),*/
    };

    protected boolean isRunning;
    public static boolean menuRunning;
    public static boolean restartMenu = false;
    protected ScreenManager screen;
    protected JFrame frame;
    private TileMapDrawer drawer;


    /**
        Signals the game loop that it's time to quit
    */
    public void stop() {
        isRunning = false;
    }


    /**
        Calls init() and gameLoop()
    */
    public void run() {
        try {
            init();
            menuLoop();
        }
        finally {
            screen.restoreScreen();
            lazilyExit();
        }
    }


    /**
        Exits the VM from a daemon thread. The daemon thread waits
        2 seconds then calls System.exit(0). Since the VM should
        exit when only daemon threads are running, this makes sure
        System.exit(0) is only called if neccesary. It's neccesary
        if the Java Sound system is running.
    */
    public static void lazilyExit() {
        Thread thread = new Thread() {
            public void run() {
                // first, wait for the VM exit on its own.
                try {
                    Thread.sleep(2000);
                }
                catch (InterruptedException ex) { }
                // system is still running, so force an exit
                System.exit(0);
            }
        };
        thread.setDaemon(true);
        thread.start();
    }


    /**
        Sets full screen mode and initiates and objects.
    */
    public void init() 
    {
        screen = new ScreenManager();
        DisplayMode displayMode =
        screen.findFirstCompatibleMode(POSSIBLE_MODES);
        frame = screen.setFullScreen(displayMode);

        Window window = screen.getFullScreenWindow();
        window.setFont(new Font("Dialog", Font.PLAIN, FONT_SIZE));
		
        
        window.setBackground(Color.BLACK);
        window.setForeground(Color.WHITE);
        
        menuRunning = true;
        //isRunning = true;
    }


    public Image loadImage(String fileName) {
        return new ImageIcon(fileName).getImage();
    }
    
    /**
	    Runs through the menu loop until clicked on play or quit.
	*/
    public void menuLoop() {

    	long startTime = System.currentTimeMillis();
        long currTime = startTime;

        while (menuRunning) {
            long elapsedTime =
                System.currentTimeMillis() - currTime;
            currTime += elapsedTime;

            // update
            update(elapsedTime);

            // draw the screen
            Graphics2D g = screen.getGraphics();
            draw(g);
            g.dispose();
            screen.update();
            
            
            // don't take a nap! run as fast as possible
            /*try {
                Thread.sleep(20);
            }
            catch (InterruptedException ex) { }*/
        }
        
        startGame();

        
    }
    
    public void startButtonLoop() {
    	long startTime = System.currentTimeMillis();
        long currTime = startTime;

        long elapsedTime =
            System.currentTimeMillis() - currTime;
        currTime += elapsedTime;

        // update
        update(elapsedTime);

        // draw the screen
        Graphics2D g = screen.getGraphics();
        draw(g);
        g.dispose();
        screen.update();
        
        try {
			TimeUnit.SECONDS.sleep((long) 0.7);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
      
    
    }
    

    /**
        Runs through the game loop until stop() is called.
    */
    
    public void gameLoop() {
        long startTime = System.currentTimeMillis();
        long currTime = startTime;
        while (isRunning) {
            long elapsedTime =
                System.currentTimeMillis() - currTime;
            currTime += elapsedTime;

            // update
            update(elapsedTime);

            // draw the screen
            Graphics2D g = screen.getGraphics();
            draw(g);
            g.dispose();
            screen.update();

            // don't take a nap! run as fast as possible
            /*try {
                Thread.sleep(20);
            }
            catch (InterruptedException ex) { }*/
        }
        restartMenu();
    }

    /**
        Updates the state of the game/animation based on the
        amount of elapsed time that has passed.
    */
    public void update(long elapsedTime) {
        // do nothing
    }


    /**
        Draws to the screen. Subclasses must override this
        method.
    */
    public abstract void draw(Graphics2D g);
    public abstract void startGame();
    public abstract void restartMenu();
}
