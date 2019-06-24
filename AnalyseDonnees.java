package Donnees;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class AnalyseDonnees {
	final static double G = 6.67408 * Math.pow(10.0, -11.0);
	final static int NB = 11508;
	final static double MPC = 3.0856775814672 * Math.pow(10.0, 22.0);
	static int H0 = 75000;
	static double VAR = H0 / MPC;
	public static double avancement = 0;
	
	private static Amas[][] res;
	
	/**
	 * Calcule la distance entre 2 amas
	 * 
	 * @param x1 coordonnee x de amas1
	 * @param y1 coordonnee y de amas1
	 * @param z1 coordonnee z de amas1
	 * @param x2 coordonnee x de amas2
	 * @param y2 coordonnee y de amas2
	 * @param z2 coordonnee z de amas2
	 * @return
	 */
	public static double calculDist(double x1, double y1, double z1, double x2, double y2, double z2) {
		double res, x, y, z;

		x = x2 - x1;
		y = y2 - y1;
		z = z2 - z1;

		res = Math.sqrt(x * x + y * y + z * z);

		return res;
	}

	/**
	 * Calcule le vecteur acceleration de l'amas
	 * 
	 * @param i indice de la frame
	 * @param j indice de l amas dans le tableau
	 * @param t tableau principal
	 * @return Vecteur acceleration de l amas a t[i][j]
	 */
	public static Vect3 calculAcc(int i, int j, Amas[][] t) {
		Vect3 res = new Vect3(0, 0, 0);
		Vect3 tmp = new Vect3(0, 0, 0);
		double x, y, z, a;
		double masse, distance, force, distance2;
		x = t[i][j].getPos().getX();
		y = t[i][j].getPos().getY();
		z = t[i][j].getPos().getZ();
		for (int k = 0; k < NB; k++) {
			if (k != j) {

				masse = t[i][k].getMvir();

				if (k < j) {// vecteur change (amas precedents < j)

					distance = calculDist(x, y, z, t[i][k].getPos().getX(), t[i][k].getPos().getY(),
							t[i][k].getPos().getZ());
					distance2 = distance * distance;// d
					force = masse / distance2;// mB/d2
					a = 1/ distance;
					// norme du vecteur
					tmp.setX((t[i][k].getPos().getX() - x) * a);
					tmp.setY((t[i][k].getPos().getY() - y) * a);
					tmp.setZ((t[i][k].getPos().getZ() - z) * a);

				} else {// vecteur pas encore change (amas suivants > j)
					distance = calculDist(x, y, z, t[i][k].getPos2().getX(), t[i][k].getPos2().getY(),
							t[i][k].getPos2().getZ());
					distance2 = distance * distance;// d
					force = masse / distance2;// mB/d²
					a = 1/ distance;
					// norme du vecteur
					tmp.setX((t[i][k].getPos2().getX() - x) * a);
					tmp.setY((t[i][k].getPos2().getY() - y) * a);
					tmp.setZ((t[i][k].getPos2().getZ() - z) * a);
				}
				tmp.mul(force);// vecteur*force
				res.addV(tmp);// addition avec les autres vecteurs acc
			}
		}
		res.mul(G);// facteur constante G

		return res;
	}


	/**
	 * Calcule le vecteur vitesse de l'amas
	 * 
	 * @param i indice de la frame
	 * @param j indice de l amas dans le tableau
	 * @param t tableau principal
	 * @return Vecteur vitesse de l amas a t[i][j]
	 */
	public static Vect3 calculVit(int i, int j, Amas[][] t, double dt) {
		double vx, vy, vz;

		Vect3 acc = calculAcc(i, j, t);

		vx = t[i][j].getVit().getX() + acc.getX() * dt;
		vy = t[i][j].getVit().getY() + acc.getY() * dt;
		vz = t[i][j].getVit().getZ() + acc.getZ() * dt;

		return new Vect3(vx, vy, vz);
	}

	/**
	 * Calcule le vecteur position a la frame+1 de l'amas
	 * 
	 * @param i indice de la frame
	 * @param j indice de l amas dans le tableau
	 * @param t tableau principal
	 * @param opt boolean d'expansion
	 * @return Vecteur position de l amas a t[i][j]
	 */
	public static Vect3 calculPos(int i, int j, Amas[][] t, double dt, int opt) {
		double rx, ry, rz;
		double exp_x, exp_y, exp_z;
		Vect3 pos = t[i][j].getPos();
		Vect3 vit = t[i][j].getVit();

		
		rx = pos.getX() + vit.getX() * dt;
		ry = pos.getY() + vit.getY() * dt;
		rz = pos.getZ() + vit.getZ() * dt;
		
		//Increment des positions lie aux positions precedentes en fonction de HO
		if(opt != 0){
			exp_x = pos.getX() * VAR * dt;
			exp_y = pos.getY() * VAR * dt;
			exp_z = pos.getZ() * VAR * dt;

			rx += exp_x;
			ry += exp_y;
			rz += exp_z;
		}
		
		return new Vect3(rx, ry, rz);
	}

	/**
	 * Produit un tableau avec les positions des amas a chaque frame
	 * tableau[nbFrames][nbAmas]
	 * 
	 * @param dt     deltaT temps en million d'annees
	 * @param frames nombre de frame de la simulation
	 * @return Tableau avec toutes les positions de chaque amas
	 * @throws IOException
	 */
	public static Amas[][] produitFinal(double dt,final int frames,final int opt) throws IOException {
		final double time = dt * 3.1536 * Math.pow(10, 13);// transformation du temps en seconde

		res = new Amas[frames][NB];// initialisation du tableau
		LectureDonnees.donnees(res, time);



		SwingWorker sw = new SwingWorker(){
			protected Object doInBackground() throws Exception {



					for (int f = 1; f < frames - 1; f++) {
						for (int j = 0; j < NB; j++) {
							// pos2 est la position a t+1 qui est calcule l'instant t
							res[f][j].setPos(Vect3.copie(res[f][j].getPos2()));// a chaque debut de boucle on remet la position2
							// a la position1
							res[f][j].setVit(calculVit(f, j, res, time));// calcule de la vitesse
							res[f][j].setPos2(calculPos(f, j, res, time, opt));// calcule de la position pour la prochaine frame dans
							// pos2
							res[f + 1][j] = Amas.copie(res[f][j]);// copie de l'amas a la frame+1
						}
						avancement = (f+1)*100/(frames-1);
						setProgress((int) avancement);
						System.out.println(avancement + " %");
					}

					for (int j = 0; j < NB; j++) {
						res[frames - 1][j].setPos(Vect3.copie(res[frames - 1][j].getPos2()));
					}

			//Swing framework 

				return null;
			}

			public void done(){ 
				Main.Main.calcul_termine = true;
				Main.Main.lancer_simulation();
				Main.Main.request_frame.closeDialog();
				Main.Main.request_frame.openSimulationNavigator();
			}         

		};

		sw.addPropertyChangeListener(new PropertyChangeListener(){
			public void propertyChange(PropertyChangeEvent event) {
				if("progress".equals(event.getPropertyName())){
					if(SwingUtilities.isEventDispatchThread())
						Main.Main.request_frame.dialog.updateComponents();
				}            
			}         
		});

		sw.execute();
		
		//End of Swing framework

		return res;
	}

}