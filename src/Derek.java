import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Derek extends Character
{
	private int moveRefractoryPeriod = 200;
	private double power = 2; //How far others go when hit, higher the better
	private double durability = 2; //How far person goes when hit, higher the better
	
	Derek(int x, int y, int cUP, int cDOWN, int cLEFT, int cRIGHT, int cNORMAL, int cSPECIAL, int iD) // Later, will need to add String name so we can differentiate between the characters (Once we have pictures)
	{
		super(x, y, cUP, cDOWN, cLEFT, cRIGHT, cNORMAL, cSPECIAL, iD);
	}
	
	protected void initiate()
	{
		super.initiate(); 

		//This is where we put the class specific pictures
		try {
			characterSkin = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("img/BatDerek.png"));
			attackLeftNormal = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("img/BatDerek_FLEFT.png"));
			attackRightNormal = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("img/BatDerek_FRIGHT.png"));
			attackLeftSide = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("img/BatDerek_LEFT.png"));
			attackRightSide = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("img/BatDerek_RIGHT.png"));
			attackUp = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("img/BatDerek_UP.png"));
			attackDown = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("img/BatDerek_DOWN.png"));
			shielded = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("img/BatDerek_SHIELD.png"));
			projectile = TextureLoader.getTexture("PNG", ResourceLoader.getResourceAsStream("img/Baseball.png"));
        } 
		catch(IOException e) {
			e.printStackTrace();
		}
	}

	//attack definitions for tester are outputs--will contain specific attacks in other subclasseswwwwww
	public void specialAttack() {
		//System.out.println("special attack");
		if (moveCount > moveRefractoryPeriod)
		{
			moveCount = 0;
			throwingProjectile = true;				        
			Main.projectiles.add(new Projectile(xVelocity, x, y, 5, directionFacing, 1, projectile, 0, false));
		}
	}

	protected void upSpecialAttack() {
		System.out.println("up special attack");
	}

	protected void downSpecialAttack() {
		System.out.println("down special attack");				          
	}

	protected void sideSpecialAttack() {
		System.out.println("side special attack");
	}
	
	protected double getPower()
	{
		return power;
	}
	
	protected double getDurability()
	{
		return durability;
	}

	protected int getMoveRefractoryPeriod() {
		return moveRefractoryPeriod;
	}
}
