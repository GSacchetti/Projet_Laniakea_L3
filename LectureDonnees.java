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
			ficTexte = new BufferedReader(new FileReader(new File("table2.dat")));

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
						case 70:
							for (int j = compteur; j < 78; j++) {
								if (!Character.isWhitespace(charArray[j])) {
									mot += charArray[j];
								}
								compteur++;
							}
							galaxy.setGlon(Double.parseDouble(mot));
							break;
						case 79:
							for (int j = compteur; j < 87; j++) {
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
							galaxy.setVgsr(Integer.parseInt(mot));
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
					galaxy.setPos(AnalyseDonnees.calculCoordonnees(galaxy));
					galaxy.setVit(vitesseInit(galaxy)); // vitesse initale ajout
					tab[0][c] = galaxy;
					c++;
				}
			} while (ligne != null);
			for(int i = 0; i < tab[0].length; i++) { // boucle qui initialise la position a t+1 dechaque amas
				tab[0][i].setPos2(AnalyseDonnees.calculPos(0, i, tab, dt));
				tab[1][i] = Amas.copie(tab[0][i]);
			}
			System.out.println("Lecture finie.");
			// Test

			// for (int i = 0; i < 11508; i++) { System.out.println(tab[0][i].getDist()); }
			// System.out.println(c);

			// --------

			ficTexte.close();
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public static void main(String[] args) throws IOException {
		int f = 15;
		Amas[][] tab = AnalyseDonnees.produitFinal(40, f, 0);// 1.26144*Math.pow(10, 15) == 40
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
