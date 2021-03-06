package Main;

import java.awt.Frame;
import java.awt.Toolkit;
import java.io.IOException;

import Interface_Utilisateur.Fenetre;
import Visualisation.Simulation_Frame;
import Visualisation.Simulation_Update_Thread;


//Centralise toutes les donnees interagissant dans la simulation
//L'acces aux donnees et l'initialisation des differents objets doivent etre fait depuis cette classe
public class Main {
	
	//Objets lies aux donnees necessaires a la representation 3D
	public static Donnees.Amas[][] amas;
	public static int duree_simulation = 1;
	public static double annee_fin;
	public static double delta_t;
	public static int nombre_frame;
	public static int expansion;
	
	//Remettre calcul_termine a "false" en cas de reinitialisation de la simulation
	public static boolean calcul_termine = false;
	
	//AWT
	public static Interface_Utilisateur.Fenetre request_frame;
	public static Visualisation.Simulation simulation;
	public static Visualisation.Simulation_Frame simulation_frame;
	private static Simulation_Update_Thread updater;
	
	/**
	 * main principal
	 * @param args
	 */
	public static void main(String args[]){
		System.out.println("Lancement du programme :");
		//Premiere fenetre permettant de rentrer les donnees necessaires au calcul
		//Met "duree_simulation", "annee_fin" et "delta_t" a jour via AWT events
		request_frame = new Fenetre();
	}
	
	/**
	 * Met "amas" a jour grace aux donnees fournies par "request_frame"
	 * Cette fonction doit etre appelee par un event de cette derniere
	 * @param dt temps en annees separant deux frames dans le calcul de la simulation
	 * @param nombre_frame nombre total de frame de la simulation
	 * @throws IOException
	 */
	public static void lancer_calcul() throws IOException{
		if(delta_t <= 0 || duree_simulation <= 0){
			System.out.println("Parametres pour le lancement du calcul de la simulation incorrects");
			System.out.println("Voir request_frame");
			System.exit(0);
		}
		amas = Donnees.AnalyseDonnees.produitFinal(delta_t,nombre_frame,expansion);
	}
	
	/**
	 * Instancie la fenetre de rendu de la simulation
	 * Cette fonction ne doit pas etre appelee avant la fin du calcul de donnees par Donnees.AnalyseDonnees
	 * Cette fonction doit etre appelee par un event de "request_frame"
	 * @throws InterruptedException 
	 */
	public static void lancer_simulation(){
		if(!calcul_termine){
			System.out.println("Le calcul des donnees n'a pas ete effectue");
			System.out.println("Voir request_frame");
			System.exit(0);
		}
		simulation = new Visualisation.Simulation();
		
		java.awt.Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int)screenSize.getWidth();
		int height = (int)screenSize.getHeight();
		Frame f = new Simulation_Frame(simulation, width, height, false);

		 updater = new Simulation_Update_Thread();
		 Thread thread = new Thread(updater);
		 thread.start();
	}
	
}
