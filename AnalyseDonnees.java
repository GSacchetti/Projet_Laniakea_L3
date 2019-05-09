package Donnees;

public class AnalyseDonnees {
	
	public static Vect3 Calcul_Coordonnées (Amas amas) {
		Vect3 VectPos = new Vect3(0, 0, 0);
		
		int glon = amas.glon;
		int glat = amas.glat;
		int dist = amas.dist;
		
		VectPos.setX(dist*(Math.cos((double) glat))*(Math.sin((double) glon)));
		VectPos.setY(dist*(Math.cos((double) glat))*(Math.cos((double) glon)));
		VectPos.setZ(dist*(Math.sin((double) glat)));
		
		return VectPos;
	}
}
