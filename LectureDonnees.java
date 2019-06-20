package Donnees;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;


public class LectureDonnees {
	final static double TMsun = 1.9884 * Math.pow(10.0, 42.0);
	final static double MPC = 3.0856775814672 * Math.pow(10.0, 22.0);
	final static int H0 = 75;
	static double VAR = H0/MPC;
	/**
	 * Initialise le tableau principal[nbFrames][nbAmas] apres lecture du fichier table2.dat 
	 * Distance en metre et poids en kilogramme
	 * @param tab Tableau avec toutes les positions
	 * @param dt Temps en seconde
	 * @throws IOException
	 */
	public static void donnees(Amas[][] tab, double dt) throws IOException {
		String ligne = "";

		int c = 0;
		int compteur = 0;

		BufferedReader ficTexte;
		try {
			ficTexte = new BufferedReader(new FileReader(new File("table3.dat")));

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
							if (Double.parseDouble(mot) == 0.0) {
								galaxy.setDist(0.001 * MPC);
							} else {
								galaxy.setDist(Double.parseDouble(mot) * MPC);
							}
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
						case 126:
							for (int j = compteur; j < 131; j++) {
								if (!Character.isWhitespace(charArray[j])) {
									mot += charArray[j];
								}
								compteur++;
							}
							galaxy.setVls(Integer.parseInt(mot));
							break;
						case 149:
							for (int j = compteur; j < 158; j++) {
								if (!Character.isWhitespace(charArray[j])) {
									mot += charArray[j];
								}
								compteur++;
							}
							galaxy.setMvir(Double.parseDouble(mot) * TMsun);
							break;
						case 159:
							for (int j = compteur; j < 169; j++) {
								if (!Character.isWhitespace(charArray[j])) {
									mot += charArray[j];
								}
								compteur++;
							}
							if (galaxy.getMvir() == 0.0) {
								galaxy.setMvir(Double.parseDouble(mot) * TMsun);
							}
							break;
						default:
							compteur++;
							break;
						}
					}
					galaxy.setPos(calculCoordonnees(galaxy));
					galaxy.setVit(vitesseInit(galaxy));
					tab[0][c] = galaxy;
					c++;
				}
			} while (ligne != null);
			for(int i = 0; i < tab[0].length; i++) { // boucle qui initialise la position a t+1 dechaque amas
				tab[0][i].setPos2(AnalyseDonnees.calculPos(0, i, tab, dt));
				tab[1][i] = Amas.copie(tab[0][i]);
			}
			System.out.println("Lecture finie.");

			ficTexte.close();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Calcule le vecteur position d un amas à partir de sa latitude, sa longitude et de sa distance
	 * @param amas
	 * @return Vecteur position initial
	 */
	public static Vect3 calculCoordonnees(Amas amas) {
		Vect3 vectPos = new Vect3(0, 0, 0);
		
		double glon = amas.getGlon();
		double glat = amas.getGlat();
		double distance = amas.getDist();

		vectPos.setX(distance * (Math.cos(Math.toRadians(glat))) * (Math.sin(Math.toRadians(glon))));
		vectPos.setY(distance * (Math.cos(Math.toRadians(glat))) * (Math.cos(Math.toRadians(glon))));
		vectPos.setZ(distance * (Math.sin(Math.toRadians(glat))));
		
		return vectPos;
	}
	
	/**
	 * Calcule le vecteur vitesse propre d un amas pour initialiser sa vitesse a t = 0
	 * @param amas
	 * @return
	 */
	public static Vect3 vitesseInit(Amas amas) {
		double vx, vy, vz;
		
		vx = (amas.getPos().getX()/amas.getDist())*(amas.getVls()-Math.abs((VAR * amas.getPos().getX())))*1000;
		vy = (amas.getPos().getY()/amas.getDist())*(amas.getVls()-Math.abs((VAR * amas.getPos().getY())))*1000;
		vz = (amas.getPos().getZ()/amas.getDist())*(amas.getVls()-Math.abs((VAR * amas.getPos().getZ())))*1000;
		
		return new Vect3(vx,vy,vz);
	}

	public static void main(String[] args) throws IOException {
		int f = 15;
		Amas[][] tab = AnalyseDonnees.produitFinal(40, f, 1);// 1.26144*Math.pow(10, 15) == 40
																					// millions d annees
		for (int i = 0; i < f; i++) {
			for (int j = 0; j < 1; j++) {
				System.out.println(tab[i][j].getNest() + ", frame : " + i + ", amas : " + j + " -> "
						+ ((tab[i][j].getPos().getX()) / (1 * Math.pow(10, 19))) + " "
						+ ((tab[i][j].getPos().getY()) / (1 * Math.pow(10, 19))) + " "
						+ ((tab[i][j].getPos().getZ()) / (1 * Math.pow(10, 19))));
				System.out.println("			vitesse : " + tab[i][j].getVit().getX() + " "
						+ tab[i][j].getVit().getY() + " " + tab[i][j].getVit().getZ());
				System.out.println("--------------------");
			}
		}
		

	}

}
