package Donnees;

public class Amas {
	private int nest, vgsr;
	private String abell, gName;
	private double glon, glat, dist, mvir;
	private Vect3 vit, pos, pos2;

	public Amas() {
		this.nest = 0;
		this.vgsr = 0;
		this.mvir = 0;
		this.abell = "";
		this.gName = "";
		this.glon = 0;
		this.glat = 0;
		this.dist = 0;
		
		this.vit = new Vect3(0, 0, 0);
		this.pos = new Vect3(0, 0, 0);
		this.pos2 = new Vect3(0, 0, 0);
	}
	
	public static Amas copie(Amas a) {
		Amas res = new Amas();
		res.nest = a.nest;
		res.vgsr = a.vgsr;
		res.mvir = a.mvir;
		res.abell = a.abell;
		res.gName = a.gName;
		res.glon = a.glon;
		res.glat = a.glat;
		res.dist = a.dist;
		
		res.vit = Vect3.copie(a.vit);
		res.pos = Vect3.copie(a.pos);
		res.pos2 = Vect3.copie(a.pos2);
		return res;
	}
	public Vect3 getPos2() {
		return pos2;
	}

	public void setPos2(Vect3 pos2) {
		this.pos2 = pos2;
	}

	

	public Vect3 getVit() {
		return vit;
	}

	public void setVit(Vect3 vit) {
		this.vit = vit;
	}

	public Vect3 getPos() {
		return pos;
	}

	public void setPos(Vect3 pos) {
		this.pos = pos;
	}

	public int getNest() {
		return nest;
	}

	public void setNest(int nest) {
		this.nest = nest;
	}

	public int getVgsr() {
		return vgsr;
	}

	public void setVgsr(int vgsr) {
		this.vgsr = vgsr;
	}

	public double getMvir() {
		return mvir;
	}

	public void setMvir(double mvir) {
		this.mvir = mvir;
	}

	public String getAbell() {
		return abell;
	}

	public void setAbell(String abell) {
		this.abell = abell;
	}

	public String getgName() {
		return gName;
	}

	public void setgName(String gName) {
		this.gName = gName;
	}

	public double getGlon() {
		return glon;
	}

	public void setGlon(double glon) {
		this.glon = glon;
	}

	public double getGlat() {
		return glat;
	}

	public void setGlat(double glat) {
		this.glat = glat;
	}

	public double getDist() {
		return dist;
	}

	public void setDist(double dist) {
		this.dist = dist;
	}
}
