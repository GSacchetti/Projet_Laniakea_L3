package Donnees;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LectureDonnees {
	final static double G = 6.67408 * Math.pow(10.0, -11.0);

	public static Amas[] donnees() throws IOException {
		Amas[] tab = new Amas[11508];
		String ligne = "";
		String fichier = "";
		int c = 0;
		int compteur = 0;
		// BufferedReader clavier = new BufferedReader(new
		// InputStreamReader(System.in));

		// System.out.println("Quel est le nom de votre fichier ?");
		fichier = "table2.dat";
		BufferedReader ficTexte;
		try {
			ficTexte = new BufferedReader(new FileReader(new File("table2.dat")));
			if (ficTexte == null) {
				throw new FileNotFoundException("Fichier non trouvé: " + fichier);
			}
			do {
				ligne = ficTexte.readLine();
				if (ligne != null) {
					compteur = 0;
					char[] charArray = ligne.toCharArray();
					Amas galaxy = new Amas();
					while (compteur < 197) {
						String mot = "";
						switch (compteur) {
						case 0:
							for (int j = compteur; j < 6; j++) {
								if (!Character.isWhitespace(charArray[j])) {
									mot += charArray[j];
								}
								compteur++;
							}
							galaxy.setNest(Integer.parseInt(mot));
							break;
						case 22:
							for (int j = compteur; j < 27; j++) {
								if (!Character.isWhitespace(charArray[j])) {
									mot += charArray[j];
								}
								compteur++;
							}
							galaxy.setDist(Double.parseDouble(mot));
							break;
						case 28:
							for (int j = compteur; j < 33; j++) {
								if (!Character.isWhitespace(charArray[j])) {
									mot += charArray[j];
								}
								compteur++;
							}
							galaxy.setAbell(mot);
							break;
						case 34:
							for (int j = compteur; j < 43; j++) {
								if (!Character.isWhitespace(charArray[j])) {
									mot += charArray[j];
								}
								compteur++;
							}
							galaxy.setgName(mot);
							break;
						case 56:
							for (int j = compteur; j < 62; j++) {
								if (!Character.isWhitespace(charArray[j])) {
									mot += charArray[j];
								}
								compteur++;
							}
							galaxy.setGlon(Double.parseDouble(mot));
							break;
						case 63:
							for (int j = compteur; j < 69; j++) {
								if (!Character.isWhitespace(charArray[j])) {
									mot += charArray[j];
								}
								compteur++;
							}
							galaxy.setGlat(Double.parseDouble(mot));
							break;
						case 120:
							for (int j = compteur; j < 125; j++) {
								if (!Character.isWhitespace(charArray[j])) {
									mot += charArray[j];
								}
								compteur++;
							}
							galaxy.setVgsr(Integer.parseInt(mot));
							break;
						case 149:
							for (int j = compteur; j < 158; j++) {
								if (!Character.isWhitespace(charArray[j])) {
									mot += charArray[j];
								}
								compteur++;
							}
							galaxy.setMvir(Double.parseDouble(mot));
							break;
						default:
							compteur++;
							break;
						}
					}
					tab[c] = galaxy;
					c++;
				}
			} while (ligne != null);
			// Test
			for (int i = 0; i < 11508; i++) {
				System.out.println(tab[i].getDist());
			}
			System.out.println(c);
			// --------

			ficTexte.close();
			return tab;
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return tab;
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

	public static Vect3 calculAcc(int n, Amas[] t) {
		Vect3 res = new Vect3(0, 0, 0);
		Vect3 tmp = new Vect3(0, 0, 0);
		double x, y, z;
		double masse, dist, force;
		x = t[n].getPos().getX();
		y = t[n].getPos().getY();
		z = t[n].getPos().getZ();
		for (int i = 0; i < t.length; i++) {
			if (i != n) {
				masse = t[i].getMvir();
				dist = calculDist(x, y, z, t[i].getPos().getX(), t[i].getPos().getY(), t[i].getPos().getZ());
				dist *= dist;// d²
				force = masse / dist;// mB/d²
				// vecteur
				tmp.setX(t[i].getPos().getX() - x);
				tmp.setY(t[i].getPos().getY() - y);
				tmp.setZ(t[i].getPos().getZ() - z);
				// ------------------
				tmp.mul(force);// vecteur*force
				res.addV(tmp);// addition avec les autres vecteurs acc
			}
		}
		res.mul(G);// facteur constante G
		return res;
	}

	public static Vect3 calculCoordonnées(Amas amas) {
		Vect3 VectPos = new Vect3(0, 0, 0);

		double glon = amas.getGlon();
		double glat = amas.getGlat();
		double dist = amas.getDist();

		VectPos.setX(dist * (Math.cos(glat)) * (Math.sin(glon)));
		VectPos.setY(dist * (Math.cos(glat)) * (Math.cos(glon)));
		VectPos.setZ(dist * (Math.sin(glat)));

		return VectPos;
	}

	public static void main(String[] args) throws IOException {
		donnees();
	}

}
