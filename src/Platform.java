import org.lwjgl.opengl.GL11;

public class Platform 
{
	public double x, y, width, height;
	private String color;
	
	Platform(double x, double y, double width, double height, String color)
	{
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.color = color;
		
		initiate();
	}
	
	private void initiate()
	{	
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, width, height, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
	
	public void run()
	{
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		
		 // set the color of the quad (R,G,B,A)
		switch(color)
		{
			case "BLACK":
		    	GL11.glColor3d(0, 0, 0);
				break;
			case "WHITE":
	    		GL11.glColor3d(255, 255, 255);
				break;
			default:
			    GL11.glColor3f(.5f, .5f, 1.0f);
			    break;
		}
 
	    // draw quad
	    GL11.glBegin(GL11.GL_QUADS);
	    GL11.glVertex2d(x - width / 2, y - height / 2);
	    GL11.glVertex2d(x - width / 2, y + height / 2);	  
	    GL11.glVertex2d(x + width / 2, y + height / 2);	 
	    GL11.glVertex2d(x + width / 2, y - height / 2);
	    GL11.glEnd();
	}
}
