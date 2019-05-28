package Visualisation;

import javax.media.j3d.QuadArray;
import javax.media.j3d.Shape3D;

public class Simulation_Cube extends Shape3D{

	   private static final float[] verts = {
		    // front face (upper half)
			 1.0f,  -1.0f,  1.0f,
			 1.0f,  1.0f,  1.0f,
			-1.0f,  1.0f,  1.0f,
			-1.0f,  -1.0f,  1.0f,
		    // back face
			-1.0f, -1.0f, -1.0f,
			-1.0f,  1.0f, -1.0f,
			 1.0f,  1.0f, -1.0f,
			 1.0f, -1.0f, -1.0f,
		    // right face
			 1.0f, -1.0f, -1.0f,
			 1.0f,  1.0f, -1.0f,
			 1.0f,  1.0f,  1.0f,
			 1.0f, -1.0f,  1.0f,
		    // left face
			-1.0f, -1.0f,  1.0f,
			-1.0f,  1.0f,  1.0f,
			-1.0f,  1.0f, -1.0f,
			-1.0f, -1.0f, -1.0f,
		    // top face
			 1.0f,  1.0f,  1.0f,
			 1.0f,  1.0f, -1.0f,
			-1.0f,  1.0f, -1.0f,
			-1.0f,  1.0f,  1.0f,
		    // bottom face
			-1.0f, -1.0f,  1.0f,
			-1.0f, -1.0f, -1.0f,
			 1.0f, -1.0f, -1.0f,
			 1.0f, -1.0f,  1.0f,
			 // bottom reg (simulation scale)
			 -1.0f,  -1.05f,  1.0f,
			 -0.8f,  -1.05f,  1.0f,
			 -1.0f,  -1.05f,  1.0f,
			 -0.8f,  -1.05f,  1.0f,
			 // bottom reg (simulation scale 2)
			 -1.0f,  -1.04f,  1.0f,
			 -1.0f,  -1.06f,  1.0f,
			 -1.0f,  -1.04f,  1.0f,
			 -1.0f,  -1.06f,  1.0f,
			 // bottom reg (simulation scale 3)
			 -0.8f,  -1.04f,  1.0f,
			 -0.8f,  -1.06f,  1.0f,
			 -0.8f,  -1.04f,  1.0f,
			 -0.8f,  -1.06f,  1.0f,

		    };

		    private static final float[] colors = {
		    // front face (red)
			1.0f, 1.0f, 1.0f,
			1.0f, 1.0f, 1.0f,
			1.0f, 1.0f, 1.0f,
			1.0f, 1.0f, 1.0f,
		    // back face (green)
			1.0f, 1.0f, 1.0f,
			1.0f, 1.0f, 1.0f,
			1.0f, 1.0f, 1.0f,
			1.0f, 1.0f, 1.0f,
		    // right face (blue)
			1.0f, 1.0f, 1.0f,
			1.0f, 1.0f, 1.0f,
			1.0f, 1.0f, 1.0f,
			1.0f, 1.0f, 1.0f,
		    // left face (yellow)
			1.0f, 1.0f, 1.0f,
			1.0f, 1.0f, 1.0f,
			1.0f, 1.0f, 1.0f,
			1.0f, 1.0f, 1.0f,
		    // top face (magenta)
			1.0f, 1.0f, 1.0f,
			1.0f, 1.0f, 1.0f,
			1.0f, 1.0f, 1.0f,
			1.0f, 1.0f, 1.0f,
		    // bottom face (cyan)
			1.0f, 1.0f, 1.0f,
			1.0f, 1.0f, 1.0f,
			1.0f, 1.0f, 1.0f,
			1.0f, 1.0f, 1.0f,
		    // simulation scale
			1.0f, 1.0f, 1.0f,
			1.0f, 1.0f, 1.0f,
			1.0f, 1.0f, 1.0f,
			1.0f, 1.0f, 1.0f,
		    // simulation scale 2
			1.0f, 1.0f, 1.0f,
			1.0f, 1.0f, 1.0f,
			1.0f, 1.0f, 1.0f,
			1.0f, 1.0f, 1.0f,
		    // simulation scale 3
			1.0f, 1.0f, 1.0f,
			1.0f, 1.0f, 1.0f,
			1.0f, 1.0f, 1.0f,
			1.0f, 1.0f, 1.0f,
		    };

		    double scale;

		    /**
		     * Constructs a color cube with unit scale.  The corners of the
		     * color cube are [-1,-1,-1] and [1,1,1].
		     */
		    public Simulation_Cube() {
			QuadArray cube = new QuadArray(36, QuadArray.COORDINATES |
				QuadArray.COLOR_3);

			cube.setCoordinates(0, verts);
			cube.setColors(0, colors);

			this.setGeometry(cube);

			scale = 1.0;
		    }


		    /**
		     * Constructs a color cube with the specified scale.  The corners of the
		     * color cube are [-scale,-scale,-scale] and [scale,scale,scale].
		     * @param scale the scale of the cube
		     */
		    public Simulation_Cube(double scale) {
			QuadArray cube = new QuadArray(36, QuadArray.COORDINATES |
				QuadArray.COLOR_3);

			float scaledVerts[] = new float[verts.length];
			for (int i = 0; i < verts.length; i++)
			    scaledVerts[i] = verts[i] * (float)scale;

			cube.setCoordinates(0, scaledVerts);
			cube.setColors(0, colors);

			this.setGeometry(cube);

			this.scale = scale;
		    }



		    /**
		     * Returns the scale of the Simulation_Cube
		     *
		     * @since Java 3D 1.2.1
		     */
		    public double getScale() {
			return scale;
		    }
}
