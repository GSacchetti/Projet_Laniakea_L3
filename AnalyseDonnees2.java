package Donnees;

import java.io.IOException;

public class AnalyseDonnees {
	final static double G = 6.67408 * Math.pow(10.0, -11.0);
	final static double MPC = 3.086 * Math.pow(10.0, 22.0);
	final static double TMsun = 1.9884 * Math.pow(10.0, 42.0);
	final static int NB = 11508;

	public static Vect3 calculCoordonnees(Amas amas) {
		Vect3 vectPos = new Vect3(0, 0, 0);

		double glon = amas.getGlon();
		double glat = amas.getGlat();
		double dist = amas.getDist();

		vectPos.setX(dist * (Math.cos(Math.toRadians(glat))) * (Math.sin(Math.toRadians(glon))));
		vectPos.setY(dist * (Math.cos(Math.toRadians(glat))) * (Math.cos(Math.toRadians(glon))));
		vectPos.setZ(dist * (Math.sin(Math.toRadians(glat))));
		
		// vitesse propre initiale
		//amas.setVit(new Vect3(vectPos.getX()*(amas.getVgsr()-76*(dist/PC)),vectPos.getY()*(amas.getVgsr()-76*(dist/PC)),vectPos.getZ()*(amas.getVgsr()-76*(dist/PC))));
		//--------
		
		return vectPos;
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

	public static Vect3 calculAcc(int i, int j, Amas[][] t) {
		Vect3 res = new Vect3(0, 0, 0);
		Vect3 tmp = new Vect3(0, 0, 0);
		double x, y, z;
		double masse, dist, force;
		x = t[i][j].getPos().getX();
		y = t[i][j].getPos().getY();
		z = t[i][j].getPos().getZ();
		for (int k = 0; k < NB; k++) {
			if (k != j) {
				masse = t[i][k].getMvir();
				
				if (k < j) {// vecteur change (amas precedents)
					dist = calculDist(x, y, z, t[i][k].getPos().getX(), t[i][k].getPos().getY(), t[i][k].getPos().getZ());
					dist *= dist;// dÂ²
					force = masse / dist;// mB/dÂ²
					
					tmp.setX(t[i][k].getPos().getX() - x);
					tmp.setY(t[i][k].getPos().getY() - y);
					tmp.setZ(t[i][k].getPos().getZ() - z);

				} else {// vecteur pas encore change (amas suivants)
					dist = calculDist(x, y, z, t[i][k].getPos2().getX(), t[i][k].getPos2().getY(), t[i][k].getPos2().getZ());
					dist *= dist;// dÂ²	
					force = masse / dist;// mB/dÂ²
					
					tmp.setX(t[i][k].getPos2().getX() - x);
					tmp.setY(t[i][k].getPos2().getY() - y);
					tmp.setZ(t[i][k].getPos2().getZ() - z);

				}
				tmp.mul(force);// vecteur*force
				res.addV(tmp);// addition avec les autres vecteurs acc
			}
		}
		res.mul(G);// facteur constante G

		return res;
	}

	public static void calculVit(int i, int j, Amas[][] t, int dt) {

		double x, y, z;
		Vect3 acc = calculAcc(i, j, t);
		x = t[i][j].getVit().getX() + acc.getX() * dt;
		y = t[i][j].getVit().getY() + acc.getY() * dt;
		z = t[i][j].getVit().getZ() + acc.getZ() * dt;

		// maj vitesse
		t[i][j].getVit().setX(x);
		t[i][j].getVit().setY(y);
		t[i][j].getVit().setZ(z);

	}

	public static void calculPos(int i, int j, Amas[][] t, int dt) {
		double x, y, z;

		Vect3 pos = t[i][j].getPos();
		Vect3 vit = t[i][j].getVit();

		x = pos.getX() + vit.getX() * dt;
		y = pos.getY() + vit.getY() * dt;
		z = pos.getZ() + vit.getZ() * dt;
		
		t[i][j].getPos2().setX(x);
		t[i][j].getPos2().setY(y);
		t[i][j].getPos2().setZ(z);

	}

	public static Amas[][] produitFinal(int dt, int frames) throws IOException {
		Amas[][] res = new Amas[frames][NB];
		LectureDonnees.donnees(res);
		for (int f = 1; f < frames - 1; f++) {
			for (int j = 0; j < NB; j++) {
				res[f][j].setPos(Vect3.copie(res[f][j].getPos2()));
				calculVit(f, j, res, dt);
				calculPos(f, j, res, dt);
				res[f + 1][j] = Amas.copie(res[f][j]);
			}
		}
		for (int j = 0; j < NB; j++) {
			res[frames - 1][j].setPos(Vect3.copie(res[frames - 1][j].getPos2()));
		}
		return res;
	}

}
