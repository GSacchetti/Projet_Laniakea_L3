package Visualisation;

import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;

/**
 * 
 * @author Guillaume Fourniols
 *	La classe fournit a simulation un vecteur simple a orienter pour representer les transitions des differents objets.
 *	Ce vecteur est donc un objet 3D.
 */
public class Simulation_Vector extends Shape3D{

		//L'objet ne contient qu'une surface place compactee sur deux dimensions (4 vertices)
		//integralement represente sur l'axe X
	   private static final float[] verts = {
			 1.0f,  0.0f,  0.0f,
			 1.0f,  0.0f,  0.0f,
			 0.0f,  0.0f,  0.0f,
			 0.0f,  0.0f,  0.0f,
	   };
	   
	   //La couleur sera modifiee pour representer correctement la vitesse de l'objet
	   private static final float[] colors = {
			0.0f, 1.0f, 1.0f,
			0.0f, 1.0f, 1.0f,
			0.0f, 1.0f, 1.0f,
			0.0f, 1.0f, 1.0f,
	   };
	   
	   double scale;
	   
	    public Simulation_Vector(double scale) {
		QuadArray vector = new QuadArray(4, QuadArray.COORDINATES |
			QuadArray.COLOR_3);

		float scaledVerts[] = new float[verts.length];
		for (int i = 0; i < verts.length; i++)
		    scaledVerts[i] = verts[i] * (float)scale;

		vector.setCoordinates(0, scaledVerts);
		vector.setColors(0, colors);

		this.setGeometry(vector);

		this.scale = scale;
	    }
	    
	    public Simulation_Vector(float x, float y, float z, float x2, float y2, float z2) {
	    	float[] vertices = {
	    			x,y,z,
	    			x,y,z,
	    			x2,y2,z2,
	    			x2,y2,z2
	    	};
			QuadArray vector = new QuadArray(4, QuadArray.COORDINATES |
				QuadArray.COLOR_3);

			float scaledVerts[] = new float[vertices.length];
			for (int i = 0; i < vertices.length; i++)
			    scaledVerts[i] = vertices[i] * (float)scale;

			vector.setCoordinates(0, scaledVerts);
			vector.setColors(0, colors);

			this.setGeometry(vector);

			this.scale = 1;
		    }
}