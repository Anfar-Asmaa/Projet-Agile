package com.TETOSOFT.tilegame;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.*;
import java.util.ArrayList;
import javax.swing.ImageIcon;

import com.TETOSOFT.graphics.*;
import com.TETOSOFT.tilegame.sprites.*;


/**
    The ResourceManager class loads and manages tile Images and
    "host" Sprites used in the game. Game Sprites are cloned from
    "host" Sprites.
*/
public class MapLoader 
{
    private ArrayList tiles;
    public int currentMap;
    private GraphicsConfiguration gc;

    // host sprites used for cloning
    private Sprite playerSprite;
    private Sprite musicSprite;
    private Sprite coinSprite;
    private Sprite goalSprite;
    private Sprite grubSprite;
    private Sprite flySprite;

    /**
        Creates a new ResourceManager with the specified
        GraphicsConfiguration.
    */
    public MapLoader(){}
    public MapLoader(GraphicsConfiguration gc)
    {
        this.gc = gc;
        loadTileImages();
        loadCreatureSprites();
        loadPowerUpSprites();
    }


    /**
        Gets an image from the images/ directory.
    */
    public Image loadImage(String name) 
    {
        String filename = "images/" + name;
        return new ImageIcon(filename).getImage();
    }


    public Image getMirrorImage(Image image) 
    {
        return getScaledImage(image, -1, 1);
    }


    public Image getFlippedImage(Image image) 
    {
        return getScaledImage(image, 1, -1);
    }


    private Image getScaledImage(Image image, float x, float y) 
    {

        // set up the transform
        AffineTransform transform = new AffineTransform();
        transform.scale(x, y);
        transform.translate(
            (x-1) * image.getWidth(null) / 2,
            (y-1) * image.getHeight(null) / 2);

        // create a transparent (not translucent) image
        Image newImage = gc.createCompatibleImage(
            image.getWidth(null),
            image.getHeight(null),
            Transparency.BITMASK);

        // draw the transformed image
        Graphics2D g = (Graphics2D)newImage.getGraphics();
        g.drawImage(image, transform, null);
        g.dispose();

        return newImage;
    }
    
    public TileMap loadFirstMap() {
    	try {
    		currentMap = 1;
            return loadMap(
                "maps/map1.txt");
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }


    public TileMap loadNextMap() 
    {
        TileMap map = null;
        while (map == null) 
        {
            currentMap++;
            try {
                map = loadMap(
                    "maps/map" + currentMap + ".txt");
            }
            catch (IOException ex) 
            {
                if (currentMap == 2) 
                {
                    // no maps to load!
                    return null;
                }
                  currentMap = 0;
                map = null;
            }
        }

        return map;
    }


    public TileMap reloadMap() 
    {
        try {
            return loadMap(
                "maps/map" + currentMap + ".txt");
        }
        catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }


    private TileMap loadMap(String filename)
        throws IOException
    {
        ArrayList lines = new ArrayList();
        int width = 0;
        int height = 0;

        // read every line in the text file into the list
        BufferedReader reader = new BufferedReader(
            new FileReader(filename));
        while (true) {
            String line = reader.readLine();
            // no more lines to read
            if (line == null) {
                reader.close();
                break;
            }

            // add every line except for comments
            if (!line.startsWith("#")) {
                lines.add(line);
                width = Math.max(width, line.length());
            }
        }

        // parse the lines to create a TileEngine
        height = lines.size();
        TileMap newMap = new TileMap(width, height);
        for (int y=0; y<height; y++) {
            String line = (String)lines.get(y);
            for (int x=0; x<line.length(); x++) {
                char ch = line.charAt(x);

                // check if the char represents tile A, B, C etc.
                int tile = ch - 'A';
                if (tile >= 0 && tile < tiles.size()) {
                    newMap.setTile(x, y, (Image)tiles.get(tile));
                }

                // check if the char represents a sprite
                else if (ch == 'o') {
                    addSprite(newMap, coinSprite, x, y);
                }
                else if (ch == '!') {
                    addSprite(newMap, musicSprite, x, y);
                }
                else if (ch == '*') {
                    addSprite(newMap, goalSprite, x, y);
                }
                else if (ch == '1') {
                    addSprite(newMap, grubSprite, x, y);
                }
                else if (ch == '2') {
                    addSprite(newMap, flySprite, x, y);
                }
            }
        }

        // add the player to the map
        Sprite player = (Sprite)playerSprite.clone();
        player.setX(TileMapDrawer.tilesToPixels(3));
        player.setY(lines.size());
        newMap.setPlayer(player);

        return newMap;
    }


    private void addSprite(TileMap map,
        Sprite hostSprite, int tileX, int tileY)
    {
        if (hostSprite != null) {
            // clone the sprite from the "host"
            Sprite sprite = (Sprite)hostSprite.clone();

            // center the sprite
            sprite.setX(
                TileMapDrawer.tilesToPixels(tileX) +
                (TileMapDrawer.tilesToPixels(1) -
                sprite.getWidth()) / 2);

            // bottom-justify the sprite
            sprite.setY(
                TileMapDrawer.tilesToPixels(tileY + 1) -
                sprite.getHeight());

            // add it to the map
            map.addSprite(sprite);
        }
    }


    // -----------------------------------------------------------
    // code for loading sprites and images
    // -----------------------------------------------------------


    public void loadTileImages()
    {
        // keep looking for tile A,B,C, etc. this makes it
        // easy to drop new tiles in the images/ directory
        tiles = new ArrayList();
        char ch = 'A';
        
        while (true) 
        {
            String name = ch + ".png";
            File file = new File("images/" + name);
            if (!file.exists()) 
                break;
            
            tiles.add(loadImage(name));
            ch++;
        }
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    public void loadCreatureSprites() 
    {

        Image[][] grubImages = new Image[4][];
        Image[][] playerImages = new Image[4][];
        Image[][] flyImages = new Image[4][];


        playerImages[0] = new Image[]{
            loadImage("PlayerWalk/Chara_BlueWalk00000.png"),
            loadImage("PlayerWalk/Chara_BlueWalk00003.png"),
            loadImage("PlayerWalk/Chara_BlueWalk00006.png"),
            loadImage("PlayerWalk/Chara_BlueWalk00009.png"),
            loadImage("PlayerWalk/Chara_BlueWalk00012.png"),
            loadImage("PlayerWalk/Chara_BlueWalk00015.png"),
        };

        grubImages[0] = new Image[] {
            loadImage("grub1.png"),
            loadImage("grub2.png"),
            loadImage("grub3.png"),
            loadImage("grub4.png"),
        };

        flyImages[0] = new Image[] {
                loadImage("fly1.png"),
                loadImage("fly2.png"),
                loadImage("fly3.png"),
                loadImage("fly4.png"),
                loadImage("fly5.png"),
        };

        //initializing all images array
        grubImages[1] = new Image[grubImages[0].length];
        grubImages[2] = new Image[grubImages[0].length];
        grubImages[3] = new Image[grubImages[0].length];

        flyImages[1] = new Image[flyImages[0].length];
        flyImages[2] = new Image[flyImages[0].length];
        flyImages[3] = new Image[flyImages[0].length];

        playerImages[1] = new Image[playerImages[0].length];
        playerImages[2] = new Image[playerImages[0].length];
        playerImages[3] = new Image[playerImages[0].length];


        //populating all images arrays
        for (int i=0; i<grubImages[0].length; i++) {
            // right-facing images
            grubImages[1][i] = getMirrorImage(grubImages[0][i]);
            // left-facing "dead" images
            grubImages[2][i] = getFlippedImage(grubImages[0][i]);
            // right-facing "dead" images
            grubImages[3][i] = getFlippedImage(grubImages[1][i]);
        }

        for (int i=0; i<flyImages[0].length; i++) {
            // right-facing images
            flyImages[1][i] = getMirrorImage(flyImages[0][i]);
            // left-facing "dead" images
            flyImages[2][i] = getFlippedImage(flyImages[0][i]);
            // right-facing "dead" images
            flyImages[3][i] = getFlippedImage(flyImages[1][i]);
        }

        for (int i=0; i<playerImages[0].length; i++)
        {
            //fixing left-right inversion for player
            playerImages[1][i] = playerImages[0][i];
            // right-facing images
            playerImages[0][i] = getMirrorImage(playerImages[1][i]);
            // left-facing "dead" images
            playerImages[2][i] = getFlippedImage(playerImages[0][i]);
            // right-facing "dead" images
            playerImages[3][i] = getFlippedImage(playerImages[1][i]);
        }

        // create creature animations
        Animation[] playerAnim = new Animation[4];
        Animation[] flyAnim = new Animation[4];
        Animation[] grubAnim = new Animation[4];

        //5
        Animation[] playerWalkAnim = new Animation[4];


        for (int i=0; i<4; i++) 
        {
            flyAnim[i] = createFlyAnim (flyImages[i]);
            grubAnim[i] = createGrubAnim (grubImages[i][0], grubImages[i][1], grubImages[i][2], grubImages[i][3]);

            playerWalkAnim[i] = createPlayerWalkAnim(playerImages[i]);
        }

        // create creature sprites
        flySprite = new Fly (flyAnim[0], flyAnim[1],flyAnim[2], flyAnim[3]);
        grubSprite = new Grub (grubAnim[0], grubAnim[1],grubAnim[2], grubAnim[3]);
        playerSprite = new Player (playerWalkAnim[0], playerWalkAnim[1],playerWalkAnim[2], playerWalkAnim[3]);

     }


    private Animation createPlayerAnim(Image player1)
    {
        Animation anim = new Animation();
        anim.addFrame(player1, 250);

        return anim;
    }

    //7
    private Animation createPlayerWalkAnim( Image[] playerImages) {
        Animation anim = new Animation();
        for(int j = 0; j <6; j++)
            anim.addFrame(playerImages[j], 150);
        return anim;
    }

    private Animation createFlyAnim(Image[] flyImages)
    {
        Animation anim = new Animation();
        for(int j = 0; j <5; j++)
            anim.addFrame(flyImages[j], 250);

        return anim;
    }


    private Animation createGrubAnim(Image img1, Image img2, Image img3, Image img4)
    {
        Animation anim = new Animation();
        anim.addFrame(img1, 250);
        anim.addFrame(img2, 250);
        anim.addFrame(img3, 250);
        anim.addFrame(img4, 250);
        return anim;
    }


    private void loadPowerUpSprites() 
    {
        // create "goal" sprite
        Animation anim = new Animation();
        anim.addFrame(loadImage("heart.png"), 150);
        goalSprite = new PowerUp.Goal(anim);

        // create "star" sprite
        anim = new Animation();
        anim.addFrame(loadImage("coin1.png"),250);
        anim.addFrame(loadImage("coin2.png"),250);
        anim.addFrame(loadImage("coin3.png"),250);
        anim.addFrame(loadImage("coin4.png"),250);
        anim.addFrame(loadImage("coin5.png"),250);
        coinSprite = new PowerUp.Star(anim);

        // create "music" sprite
        anim = new Animation();
        anim.addFrame(loadImage("music1.png"), 150);
        anim.addFrame(loadImage("music2.png"), 150);
        anim.addFrame(loadImage("music3.png"), 150);
        anim.addFrame(loadImage("music2.png"), 150);
        musicSprite = new PowerUp.Music(anim);
        musicSprite=new PowerUp.Music(anim);
    }

}
