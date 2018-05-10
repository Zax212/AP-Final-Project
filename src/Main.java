import java.awt.Font;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;
 
public class Main {
 
	public static int characterWidth = 32, characterHeight = 70;
    public static ArrayList<Character> players = new ArrayList<Character>();
	public static ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	
	private double startTime = getTime();
    private ArrayList<Platform> platforms = new ArrayList<Platform>(); //(x, y, width, height)
    private Texture background;
    private Texture menuScreen;
    private Texture playButton;
    private Texture playButtonHighlighted;
    private TrueTypeFont fontBaren, fontTimes;
    private boolean antiAlias = true;
    private boolean menu = true;
    
    public void start()
    {    	
    	initiate();
    	    	
		while (!Display.isCloseRequested()) 
	    {
			if (getTime() - startTime > 1500) //How fast the game runs, refreshes every "" microseconds
		    {
				// render 
			    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); // Clear the screen and depth buffer
		    
				if (!menuScreen())
					gameLoop();
				
				startTime = getTime();
				
				Display.update();
		    }
		}
		
		Display.destroy();
    }
    
    public static void main(String[] argv)
    {
        Main displayImage = new Main();
        displayImage.start(); 	
    }
    
    private void initiate()
    {
	    try {
		    Display.setDisplayMode(new DisplayMode(1000, 750));
		    Display.create();
		} catch (LWJGLException e) {
		    e.printStackTrace();
		    System.exit(0);
		}
	    
		try {
			background = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("img/background.png"));
		    menuScreen = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("img/TheDarkCowRises.png"));
		    playButton = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("img/playOff.png"));
		    playButtonHighlighted = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("img/playOn.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		Font awtFont = new Font("Times New Roman", Font.BOLD, 24);
		fontTimes = new TrueTypeFont(awtFont, antiAlias);
 
		// load font from file
		try {
			InputStream inputStream	= ResourceLoader.getResourceAsStream("font/Baron_Neue.otf");
 
			Font awtFont2 = Font.createFont(Font.TRUETYPE_FONT, inputStream);
			awtFont2 = awtFont2.deriveFont(24f); // set font size
			fontBaren = new TrueTypeFont(awtFont2, antiAlias);
 
		} catch (Exception e) {
			e.printStackTrace();
		}
		  	        
	    Display.setTitle("ZEB");
	    	    
	    map1();
	    //map2();
	    	        	    
	    players.add(new Sundeep(800, 650 - characterHeight, Keyboard.KEY_UP, Keyboard.KEY_DOWN, Keyboard.KEY_LEFT, Keyboard.KEY_RIGHT, Keyboard.KEY_NUMPAD1, Keyboard.KEY_NUMPAD2, 0));
	    players.add(new Derek(200, 650 - characterHeight, Keyboard.KEY_W, Keyboard.KEY_S, Keyboard.KEY_A, Keyboard.KEY_D, Keyboard.KEY_F, Keyboard.KEY_G, 1));
	    
	    initiateGL();
    }
    
	private double getTime()
	{
		return System.nanoTime() / 1000.0; //Microseconds
	}
	
	private void initiateGL()
	{
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glShadeModel(GL11.GL_SMOOTH);        
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDisable(GL11.GL_LIGHTING); 
		
		GL11.glEnable(GL11.GL_TEXTURE_2D);              
		 
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);         
		 
		// enable alpha blending
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		 
		GL11.glViewport(0,0, Display.getWidth(), Display.getHeight());
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		
	    GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
	
	private void map1()
	{
		platforms.add(new Platform(500, 700, 700, 100, "WHITE")); //Floor
	    platforms.add(new Platform(500, 600, 100, 5, "DEFAULT")); //Raised, floating middle platform
	    for (int i = 0; i < 2; i++)
	        platforms.add(new Platform(300 - i * 300, 500 - i * 100, 100, 5, "DEFAULT"));
	    for (int i = 0; i < 2; i++)
	        platforms.add(new Platform(700 + i * 300, 500 - i * 100, 100, 5, "DEFAULT"));
	}
	
	private void map2()
	{
		platforms.add(new Platform(200, 700, 400, 100, "WHITE")); //Floor1
		platforms.add(new Platform(800, 700, 400, 100, "WHITE")); //Floor2
	    platforms.add(new Platform(500, 550, 100, 25, "DEFAULT")); //Raised, floating middle platform
	}
	
	private boolean menuScreen()
	{
		if (menu)
		{
			Color.white.bind();
		    GL11.glBindTexture(GL11.GL_TEXTURE_2D, menuScreen.getTextureID());
			 
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0, 0);
				GL11.glVertex2d(0, 0);
			GL11.glTexCoord2f(1, 0);
				GL11.glVertex2d(1025, 0);
			GL11.glTexCoord2f(1, 1);
				GL11.glVertex2d(1025, 1025);
			GL11.glTexCoord2f(0, 1);
				GL11.glVertex2d(0, 1025);

			GL11.glEnd(); 
			
			Color.white.bind();
			
			if (Collision.rectangleRectangle(Mouse.getX(), Mouse.getY(), 1, 1, 850, 75, playButton.getImageWidth(), playButton.getImageHeight()))
			{
			    GL11.glBindTexture(GL11.GL_TEXTURE_2D, playButton.getTextureID());
			    
			    if (Mouse.isButtonDown(0))
			    	menu = false;
			}
			else
			{
			    GL11.glBindTexture(GL11.GL_TEXTURE_2D, playButtonHighlighted.getTextureID());
			}
			 
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glTexCoord2f(0, 0);
				GL11.glVertex2d(850 - playButton.getImageWidth() / 2, 675 - playButton.getImageHeight() / 2);
			GL11.glTexCoord2f(1, 0);
				GL11.glVertex2d(850 + playButton.getImageWidth() / 2, 675 - playButton.getImageHeight() / 2);
			GL11.glTexCoord2f(1, 1);
				GL11.glVertex2d(850 + playButton.getImageWidth() / 2, 675 + playButton.getImageHeight() / 2);
			GL11.glTexCoord2f(0, 1);
				GL11.glVertex2d(850 - playButton.getImageWidth() / 2, 675 + playButton.getImageHeight() / 2);
				
			GL11.glEnd();

			return true;
		}
		else
			return false;
	}
	
	private void gameLoop()
	{
	    Color.white.bind();
	    GL11.glBindTexture(GL11.GL_TEXTURE_2D, background.getTextureID());
		 
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(0, 0);
			GL11.glVertex2d(0, 0);
		GL11.glTexCoord2f(1, 0);
			GL11.glVertex2d(1025, 0);
		GL11.glTexCoord2f(1, 1);
			GL11.glVertex2d(1025, 1025);
		GL11.glTexCoord2f(0, 1);
			GL11.glVertex2d(0, 1025);

		GL11.glEnd(); 
	    
	    for (Platform a: platforms)
	    {
	    	a.run();
	    	for (Character b: players)
	    	{
	    		if (Collision.rectangleRectangle(a.x, a.y, a.width, a.height, b.x, b.y, characterWidth, characterHeight))
	    		{
	    			b.platformDetected = true;
	    			
	    			if ((b.y + characterHeight / 2 - 1) < (a.y - a.height / 2))
	    				b.topCollision = true;
	    			else
	    				b.topCollision = false;
	    		}
	    	}
	    	for (Projectile b: projectiles)
	    	{
	    		if (Collision.rectangleRectangle(a.x, a.y, a.width, a.height, b.projectileX, b.projectileY, b.image.getImageWidth(), b.image.getImageHeight()))
	    		{
	    			b.platformDetected = true;
	    		}
	    	}
	    }
	    
	    for (Character a: players)
	    {
	    	a.run();
	    	a.platformDetected = false;
	    	
	    	Color.white.bind();
	    	
	    	Color color;
	    	if (a.damageCounter >= 1000)
	    		color = Color.black;
	    	else if (a.damageCounter < 1000 && a.damageCounter >= 500)
	    		color = Color.red;
	    	else if (a.damageCounter < 500 && a.damageCounter >= 350)
	    		color = Color.orange;
	    	else if (a.damageCounter < 350 && a.damageCounter >= 100)
	    		color = Color.yellow;
	    	else
	    		color = Color.green;
	    	

			fontTimes.drawString(0,0, "Player1: WASD to move, F & G to attack", Color.red);
	    	fontTimes.drawString(425,0, "Player2: Arrow Keys to move, 1 & 2(NumPad) to attack", Color.green);
	    	
	    	fontBaren.drawString((float) (a.x - characterWidth / 2), (float) (a.y - characterHeight), (a.damageCounter + "%"), color);	
	    }
	    
	    for (int i = 0; i < projectiles.size(); i++)
	    {
	    	if (projectiles.get(i).removeProjectile || projectiles.get(i).projectileX < 0 || projectiles.get(i).projectileX > Display.getWidth() || projectiles.get(i).projectileY < 0 || projectiles.get(i).projectileY > Display.getHeight())
	    	{
	    		projectiles.remove(i);
	    		continue;
	    	}
	    	projectiles.get(i).run();
	    	projectiles.get(i).platformDetected = false;
	    }		
    }
}
