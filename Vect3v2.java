package Donnees;

public class Vect3 {
	private double x,y,z;
	public Vect3 (double x,double y,double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public Vect3 addV(Vect3 d) {
		return new Vect3(this.x+d.x,this.y+d.y,this.z+d.z);
	}
	public Vect3 mul(double d) {
		return new Vect3(this.x*d,this.y*d,this.z*d);
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double getZ() {
		return z;
	}
	public void setZ(double z) {
		this.z = z;
	}
}
