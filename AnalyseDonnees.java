package Donnees;

public class AnalyseDonnees {
	
	public static Vect3 calculCoordonnées (Amas amas) {
		Vect3 VectPos = new Vect3(0, 0, 0);

		double glon = amas.getGlon();
		double glat = amas.getGlat();
		double dist = amas.getDist();

		VectPos.setX(dist * (Math.cos(glat)) * (Math.sin(glon)));
		VectPos.setY(dist * (Math.cos(glat)) * (Math.cos(glon)));
		VectPos.setZ(dist * (Math.sin(glat)));

		return VectPos;
	}
	public static double calculDist(double x1, double y1, double z1, double x2, double y2, double z2) {
		double res, x, y, z;
		x = x2 - x1;
		y = y2 - y1;
		z = z2 - z1;
		res = Math.sqrt(x * x + y * y);
		res = Math.sqrt(res * res + z * z);
		return res;
	}

	public static Vect3 calculAcc(int n,Galaxie[] t) {
		Vect3 res = new Vect3(0,0,0);
		Vect3 tmp = new Vect3(0,0,0);
		double x,y,z;
		double masse,dist,force;
		x = t[n].getPos().getX();
		y = t[n].getPos().getY();
		z = t[n].getPos().getZ();
		for(int i = 0; i < t.length;i++) {
			if(i != n) {
				masse = t[i].getMvir();
				dist = calculDist(x,y,z,t[i].getPos().getX(),t[i].getPos().getY(),t[i].getPos().getZ());
				dist *= dist;//	d²
				force = masse/dist;//	mB/d²
				//vecteur
				tmp.setX(t[i].getPos().getX()-x);
				tmp.setY(t[i].getPos().getY()-y);
				tmp.setZ(t[i].getPos().getZ()-z);
				//------------------
				tmp.mul(force);//	vecteur*force
				res.addV(tmp);//	addition avec les autres vecteurs acc
			}
		}
		res.mul(G);// facteur constante G
		return res;
	}
}
