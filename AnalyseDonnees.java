package Donnees;

public class AnalyseDonnees {
	
	public static Vect3 Calcul_Coordonnées (Amas Amas) {
		Vect3 VectPos = new Vect3(0, 0, 0);
		
		int glon = Amas.glon;
		int glat = Amas.glat;
		int dist = Amas.dist;
		
		VectPos.setX(dist*(Math.cos((double) glat))*(Math.sin((double) glon)));
		VectPos.setY(dist*(Math.cos((double) glat))*(Math.cos((double) glon)));
		VectPos.setZ(dist*(Math.sin((double) glat)));
		
		return VectPos;
	}
}
