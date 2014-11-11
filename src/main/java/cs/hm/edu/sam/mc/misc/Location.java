package cs.hm.edu.sam.mc.misc;

/**
 * Location-class.
 * 
 * @author Christoph Friegel
 * @version 0.1
 */

public class Location {

	private double x; //longitude
	private double y; //latitude
	
	public Location( double newX, double newY ) {
		this.x = newX;
		this.y = newY;
	}
	
	//Get longitude
	public double getX() {
		return x;
	}
	
	//Get latitude
	public double getY() {
		return y;
	}
	
	@Override
	public String toString() {
		return new StringBuffer(" x : ").append(this.x)
				.append(" y : ").append(this.y).toString();
	}
	
}
