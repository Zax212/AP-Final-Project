import org.lwjgl.opengl.GL11;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

public abstract class Character 
{
	public int damageCounter = 0;
	public double x, y;
	public boolean platformDetected, topCollision, shieldOn;
	
	//Anything that will not change after class is instantiated
	private int cUP, cDOWN, cLEFT, cRIGHT, cNORMAL, cSPECIAL;
	private int iD;
	private double normalAttackHitboxX = 4;
	private double normalAttackHitboxY = 12;
	private double xMaxVelocity = .8;
	private double yVelocityInitial = -1;
	private double xAcceleration = .01;
	private double yAcceleration = -.005; //Gravity
	private boolean displayCharSkin;
	private boolean debugMode = false;
	
	//Anything that will change after class is instantiated
	protected int directionFacing, directionJumping, moveCount = 0;
	protected double xVelocity = 0 , yVelocity = 0;
	protected boolean isJumping = false, shieldOnCooldown = false,  throwingProjectile = false;
	protected Texture characterSkin, attackLeftNormal, attackRightNormal, attackLeftSide, attackRightSide, attackUp, attackDown, shielded, projectile;
	
	Character(int x, int y, int cUP, int cDOWN, int cLEFT, int cRIGHT, int cNORMAL, int cSPECIAL, int iD)
	{
		this.iD = iD;
		this.x = x;
		this.y = y;
		this.cUP = cUP;
		this.cDOWN = cDOWN;
		this.cLEFT = cLEFT;
		this.cRIGHT = cRIGHT;
		this.cNORMAL = cNORMAL;
		this.cSPECIAL = cSPECIAL;

		initiate();
	}
	
	private double getXAcceleration()
	{
		return xAcceleration;
	}
	
	private void upNormalAttack()
	{	
		draw(attackUp.getTextureID());
		
	    checkAttackHit(0, ((normalAttackHitboxY - Main.characterHeight) / 2), normalAttackHitboxX * 2, normalAttackHitboxY * 5, 0.5);
	}
	
	private void downNormalAttack()
	{
		draw(attackDown.getTextureID());
		
		checkAttackHit(0, ((normalAttackHitboxY * 0.1 + Main.characterHeight) / 2), normalAttackHitboxX * 15, normalAttackHitboxY * 0.25, 0.1);
	}
	
	private void sideNormalAttack()
	{
		if (directionFacing > 0)
			draw(attackRightSide.getTextureID());
		else
			draw(attackLeftSide.getTextureID());
		
	    checkAttackHit(((normalAttackHitboxX + Main.characterWidth) / 2) * directionFacing, 0, normalAttackHitboxX * 4, normalAttackHitboxY, 0.25);
	}
	
	private void normalAttack()
	{			
		if (directionFacing > 0)
			draw(attackRightNormal.getTextureID());
		else
			draw(attackLeftNormal.getTextureID());
		
	    checkAttackHit(((normalAttackHitboxX + Main.characterWidth) / 2) * directionFacing, 0, normalAttackHitboxX, normalAttackHitboxY, 1);
	}
	
	private void shield()
	{
		reset();
		
		draw(shielded.getTextureID());
	}
	
	//Special attacks to be defined within individual subclasses for each character.
	protected abstract void upSpecialAttack();
	protected abstract void downSpecialAttack();
	protected abstract void sideSpecialAttack();
	protected abstract void specialAttack();
	protected abstract double getPower();
	protected abstract double getDurability();
	protected abstract int getMoveRefractoryPeriod();
	
	private void checkAttackHit(double offsetX, double offsetY, double hitboxX, double hitboxY, double damageMultiplier)
	{		
		if (debugMode)
		{
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
			
	    	GL11.glColor3d(0, 0, 255);
			    	
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2d(x + offsetX - hitboxX / 2, y + offsetY - hitboxY / 2);
			GL11.glVertex2d(x + offsetX + hitboxX / 2, y + offsetY - hitboxY / 2);
			GL11.glVertex2d(x + offsetX + hitboxX / 2, y + offsetY + hitboxY / 2);
			GL11.glVertex2d(x + offsetX - hitboxX / 2, y + offsetY + hitboxY / 2);
		    GL11.glEnd();
		}
		
		for (Character a: Main.players)
	    {
	    	if (iD != a.iD)
	    	{
	    		if (Collision.rectangleRectangle(x + offsetX, y + offsetY, hitboxX, hitboxY, a.x, a.y, Main.characterWidth, Main.characterHeight)) //Works if the hitboxes at manually made larger, don't konw why atm
	    			a.hit(getPower() * damageMultiplier, directionFacing, true);
	    	}
	   }
	}
	
	public void hit(double power, int directionFacing, boolean knockBack) //Replace directionFacing with x, y coords and calculate angle of attack later
	{	
		if (knockBack)
			xVelocity += (damageCounter * 0.001 + power * 0.05) / getDurability() * directionFacing;
		
		damageCounter += power;
	}
	
	private void reset()
	{
		xVelocity = 0;
	}
	
	private void move()
	{
		if (Keyboard.isKeyDown(cUP))
		{
			if (yVelocity == 0)
			{
				yVelocity = yVelocityInitial;
				y += yVelocity;
			}
		}
		if (Keyboard.isKeyDown(cLEFT))
    	{
    		xVelocity -= xAcceleration;
			if (Math.abs(xVelocity) > xMaxVelocity)
				xVelocity = xMaxVelocity * -1.0;
    	}
		if (Keyboard.isKeyDown(cRIGHT))
		{
			xVelocity += xAcceleration;
			if (Math.abs(xVelocity) > xMaxVelocity)
				xVelocity = xMaxVelocity;
		}
		if (Keyboard.isKeyDown(cDOWN) && !(Keyboard.isKeyDown(cNORMAL) || Keyboard.isKeyDown(cSPECIAL)))
		{
			if (!shieldOnCooldown)
				shield();
		}
		else
			shieldOn = false;
	}
	
	protected void attack()
	{
		if (Keyboard.isKeyDown(cUP) && Keyboard.isKeyDown(cNORMAL))
			{upNormalAttack();}
		else if (Keyboard.isKeyDown(cDOWN) && Keyboard.isKeyDown(cNORMAL))
			{downNormalAttack();}
		else if ((Keyboard.isKeyDown(cLEFT) || Keyboard.isKeyDown(cRIGHT)) && Keyboard.isKeyDown(cNORMAL))
			{sideNormalAttack();}
		else if (Keyboard.isKeyDown(cNORMAL))
			{normalAttack();}
		//else if (Keyboard.isKeyDown(cUP) && Keyboard.isKeyDown(cSPECIAL))
			//{upSpecialAttack();}
		//else if (Keyboard.isKeyDown(cDOWN) && Keyboard.isKeyDown(cSPECIAL))
			//{downSpecialAttack();}
		//else if ((Keyboard.isKeyDown(cLEFT) || Keyboard.isKeyDown(cRIGHT)) && Keyboard.isKeyDown(cSPECIAL))
			//{sideSpecialAttack();}
		else if (Keyboard.isKeyDown(cSPECIAL))
			{specialAttack();}
	}
	
	protected void draw(int textureID)
	{
		displayCharSkin = false;
		
		Color.white.bind();
	    GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		 
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(1, 0);
		GL11.glVertex2d(x + Main.characterWidth * 1.35, y - Main.characterHeight * 0.775);
		GL11.glTexCoord2f(1, 1);
		GL11.glVertex2d(x + Main.characterWidth * 1.35, y + Main.characterHeight * 1.225);
		GL11.glTexCoord2f(0, 1);
		GL11.glVertex2d(x - Main.characterWidth * 0.65, y + Main.characterHeight * 1.225);
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex2d(x - Main.characterWidth * 0.65, y - Main.characterHeight * 0.775);
		
		GL11.glEnd(); 
	}
	
	protected void initiate()
	{		
		directionFacing = 1;
	}
	
	protected void run()
	{					
		if (platformDetected)
		{			
			if (!topCollision)
				xVelocity *= -1;
			
			if (yVelocity >= 0)
			{
				yVelocity = 0;
			}
			else
			{
				yVelocity *= -1;
			}
		}
		
		if (x < 0 || x > 1000 || Collision.rectangleRectangle(x, y, Main.characterWidth, Main.characterHeight, 150, 750, 1, 145) || Collision.rectangleRectangle(x, y, Main.characterWidth, Main.characterHeight, 850, 750, 1, 145))
		{
			xVelocity *= -1;
		}
		
		
		x += xVelocity;
				
		if (yVelocity != yVelocityInitial)
			y += yVelocity;
		
	    move();
	    
	    displayCharSkin = true;
		
		attack();
		
		if (displayCharSkin)
			draw(characterSkin.getTextureID());
		
		if(xVelocity != 0)
			directionFacing = (int) (xVelocity / Math.abs(xVelocity));
		
		if (xVelocity > 0 )
	    	xVelocity -= getXAcceleration() / 2.0 * Math.abs(xVelocity);
    	if (xVelocity < 0)
	    	xVelocity += getXAcceleration() / 2.0 * Math.abs(xVelocity);
    		
    	yVelocity -= yAcceleration;	
    	
    	moveCount++;
	}
}
