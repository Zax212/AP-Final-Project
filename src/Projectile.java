import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

public class Projectile {

	public double projectileX, projectileY;
	public boolean platformDetected;
	public Texture image;
	
	protected double projectileXVelocity;
	protected double projectileYVelocity = 0;
	protected boolean removeProjectile = false;
	
	private int directionTraveling;
	private double projectileXAcceleration = .01;
	private double projectileYAcceleration;
	private double power;
	private boolean knockBack;
	
	//constructor creates a projectile object from character info
	public Projectile(double characterXVelocity, double characterXPosition, double characterYPosition, double xVelocityInitial, int characterDirectionFacing, double power, Texture projectileLook, double yAcceleration, boolean knockBack)
	{
		projectileX = characterXPosition + Main.characterWidth * characterDirectionFacing; 
		projectileY = characterYPosition; //initial position of projectile is position of character
		projectileXVelocity = characterXVelocity * characterDirectionFacing; //initial projectileX velocitprojectileY is same as character's projectileX velocitprojectileY; directionFacing should be accounted for when passing this value to the constructor
		projectileXVelocity = xVelocityInitial;
		directionTraveling = characterDirectionFacing;
		this.power = power;
		image = projectileLook;
		projectileYAcceleration = yAcceleration;
		this.knockBack = knockBack;
	}
	
	//The following is copied and modified from the Character class (run method)
	protected void run()
	{		
		if (platformDetected)
			removeProjectile = true;
		
		projectileX += projectileXVelocity * directionTraveling;
		projectileY += projectileYVelocity;

		for (Character a: Main.players)
	    {
    		if (Collision.rectangleRectangle(projectileX, projectileY, image.getImageWidth(), image.getImageHeight(), a.x, a.y, Main.characterWidth, Main.characterHeight))
    			a.hit(power, directionTraveling, knockBack);
	    }
		
		drawProjectile();
		
		projectileXVelocity -= projectileXAcceleration / 2.0 * Math.abs(projectileXVelocity);
    		
    	projectileYVelocity += projectileYAcceleration;
	}
	
	private void drawProjectile()
	{
		Color.white.bind();
	    GL11.glBindTexture(GL11.GL_TEXTURE_2D, image.getTextureID());
		 
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(1, 0);
		GL11.glVertex2d(projectileX + image.getImageWidth(), projectileY - image.getImageHeight());
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex2d(projectileX + image.getImageWidth(), projectileY + image.getImageHeight());
		GL11.glTexCoord2f(0, 1);
		GL11.glVertex2d(projectileX - image.getImageWidth(), projectileY + image.getImageHeight());
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex2d(projectileX - image.getImageWidth(), projectileY - image.getImageHeight());
						
		GL11.glEnd(); 
	}
	
}
