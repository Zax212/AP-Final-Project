public class Collision 
{
	public static double distance( double x1, double y1, double x2, double y2 )
	{
	    return Math.sqrt( Math.pow( x2 - x1, 2 ) + Math.pow( y2 - y1, 2 ) );
	}
	
	public static boolean sideCollision(double characterX, double characterY, double characterWidth, double characterHeight, double x2, double y2, double width2, double height2)
	{
		/*double diagonalCenterCornerPlatform = distance(x2, y2, x2 + width2 / 2, y2 + height2 / 2);
		double horizontalCenterCornerPlatform = width2 / 2;
		double angleCenterCornerPlatform = Math.toDegrees(Math.acos(horizontalCenterCornerPlatform / diagonalCenterCornerPlatform)) - 5;
		
		double diagonalCenterCornerBothTop = distance(x2, y2, characterX, characterY + characterHeight / 2);
		double horizontalCenterCornerBothTop = Math.abs(x2 - characterX);
		double angleCenterCornerBothTop = Math.toDegrees(Math.acos(horizontalCenterCornerBothTop / diagonalCenterCornerBothTop));
		
		double diagonalCenterCornerBothBottom = distance(x2, y2, characterX, characterY - characterHeight / 2);
		double horizontalCenterCornerBothBottom = Math.abs(x2 - characterX);
		double angleCenterCornerBothBottom = Math.toDegrees(Math.acos(horizontalCenterCornerBothBottom / diagonalCenterCornerBothBottom));
		
		System.out.println(angleCenterCornerPlatform + " " + angleCenterCornerBothBottom + " " + angleCenterCornerBothTop);
		
		if (angleCenterCornerBothTop < angleCenterCornerPlatform || angleCenterCornerBothBottom < angleCenterCornerPlatform)
			return true;
		else
			return false;*/
		
		if (characterY - characterHeight / 2 > y2 + height2 || characterY + characterHeight / 2 < y2 - height2)
			return true;
		else
			return false;
	}
	
	public static boolean rectangleRectangle(double x1, double y1, double width1, double height1, double x2, double y2, double width2, double height2) 
	{
		if (x1 + width1 /2 >= x2 - width2 / 2 &&
			y1 + height1 / 2 >= y2 - height2 / 2 &&
			x1 - width1 /2 <= x2 + width2 / 2 &&
			y1 - height1 / 2 <= y2 + height2 / 2)
			return true;
		else			
			return false;
	}
	
	public static boolean rectangleCircle()
	{
		return true;
	}
	
	public static boolean circleCircle(double x1,double y1,double r1,double x2,double y2, double r2)
	{
		if( distance( x1, y1, x2, y2 ) < ( r1 + r2 ))
			return true;
		else
			return false;
	}
}