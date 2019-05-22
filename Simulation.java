package Visualisation_2;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

import javax.media.j3d.Alpha;
import javax.media.j3d.Appearance;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Light;
import javax.media.j3d.Material;
import javax.media.j3d.PointLight;
import javax.media.j3d.PolygonAttributes;
import javax.media.j3d.PositionPathInterpolator;
import javax.media.j3d.ScaleInterpolator;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.ViewPlatform;
import javax.vecmath.AxisAngle4f;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Point3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.geometry.Sphere;
import com.sun.j3d.utils.geometry.Text2D;
import com.sun.j3d.utils.universe.PlatformGeometry;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.universe.ViewingPlatform;

public class Simulation extends Applet{
	

	private static final long serialVersionUID = 1L;
	public static final int FRAMERATE = 10;
	private SimpleUniverse universe = null;
	private Canvas3D canvas = null;
	private TransformGroup viewtrans = null;
	
	private final static double RESCALING = 1.0*Math.pow(10, 23);
	
	public Simulation() {
		 super.init();
		 setLayout(new BorderLayout());
		 //-------------------3D RELATED------------------------------
		 GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();

		 canvas = new Canvas3D(config);

		  
		 universe = new SimpleUniverse(canvas);

		 BranchGroup scene = createSceneGraph();
		 universe.getViewingPlatform().setNominalViewingTransform();
		 universe.getViewer().getView().setBackClipDistance(1000.0);
		 universe.addBranchGraph(scene);
		 //end of 3D setup	  

		  add(canvas);		  
		 }
	 
	
	//Initialisation de la scene 3D
	 private BranchGroup createSceneGraph() {
		  BranchGroup objRoot = new BranchGroup();
		  BoundingSphere bounds = new BoundingSphere(new Point3d(), 100.0);

		  viewtrans = universe.getViewingPlatform().getViewPlatformTransform();
		  
		  
		  
		  
		  //---------------------------DEPLACEMENT DANS LA SCENE--------------------
		  Simulation_Navigator_Behaviour sim_nav = new Simulation_Navigator_Behaviour(viewtrans, new Vector3d(0.0f,50.0f,0.0f));
		  sim_nav.setSchedulingBounds(bounds);
		  PlatformGeometry pg = new PlatformGeometry();
		  pg.addChild(sim_nav);
		  universe.getViewingPlatform().setPlatformGeometry(pg);
		  
		  Transform3D t3d = new Transform3D();
		  t3d.set(new Vector3f(0.0f,50.0f,0.0f));
		  viewtrans.setTransform(t3d);

		 //---------------------------AJOUT DU CONTENU------------------------------
		 //Ajouter les amas
		  for(int i = 0; i<Principe.nb_amas;i++){
			 // if(Principe.amas[0][i].getMvir()>Math.pow(10, 40))
				  objRoot.addChild(create_amas(i));
		  }
		  
		  
		  
		  objRoot.addChild(createLight());
		  objRoot.addChild(createWireCube());
		  objRoot.addChild(createText2D("MPC : 0",new Vector3d(-50.0f,-53.0f,50.0f)));
		  objRoot.addChild(createText2D("Year : 61 165 118",new Vector3d(52.0f,0.0f,50.0f)));

		  return objRoot;
		 }

	 
	 	//Cree une branche contenant la representation d'un amas
	 	//Prend en parametre l'indice de ce dernier dans le tableau rendu par l'exploitation des donnees
	 	//A l'objet rendu est integre le chemin qu'il suivra au cours de la simulation
	 	private BranchGroup create_amas(int amas_index){
	 		BranchGroup objRoot = new BranchGroup();
			TransformGroup tg = new TransformGroup();
			TransformGroup tg2 = new TransformGroup();
			Transform3D t3d = new Transform3D();
			
			//Autoriser la modification de l'objet pendant la simulation
			tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
			tg2.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
			
			//------------------------PATH---------------------------
			//Creer la fonction de rotation au cour du temps
			//La duree de transition est faite sur la duree de la simulation (en millisecondes)
			 Alpha transAlpha=new Alpha(-1,(int)Principe.duree_sim*1000);
			
			//Creation du chemin : un amas prendra autant de positions successives (keyframes, pas position reelle)
			//qu'il y a de frame dans la simulation
			Point3f[] chemin=new Point3f[Principe.amas.length];
			for(int i = 0; i<Principe.amas.length;i++){
				chemin[i]=new Point3f(
						(float)(Principe.amas[i][amas_index].getPos().getX() / RESCALING),
						(float)(Principe.amas[i][amas_index].getPos().getY() / RESCALING),
						(float)(Principe.amas[i][amas_index].getPos().getZ() / RESCALING)
						);
				
			}
			
			//Creer un tableau de correspondance
			//Divise la totalite des positions des frames sur une echelle de 0 a 1
			float[] timePosition= new float[Principe.amas.length];
			for(int i = 0; i<Principe.amas.length;i++){
				timePosition[i] = ((float)i) *(1.0f/(float)(Principe.amas.length-1));
				//La position a t = i est donc ( i * echelle de temps separant deux frames)
				
			}
			
			//On applique les transitions a l'interpolateur
			PositionPathInterpolator interpol=new PositionPathInterpolator(transAlpha,tg,t3d,timePosition,chemin);
			
			//Definir la zone sur laquelle va s'appliquer le chemin.
			//Peut avoir des utilites en terme de performance.
			//Dans la logique d'une simulation, on preferera permettre la mouvement quel que soit la distance a l'objet.
			BoundingSphere bounds=new BoundingSphere();
			bounds.setRadius(500.0);
			interpol.setSchedulingBounds(bounds);
			
			//On applique l'interpolateur a la branche.
			tg.addChild(interpol);
			//--------------------------------RENDERING------------------------------------
			Sphere sphere = new Sphere((float)(Math.sqrt(Principe.amas[0][amas_index].getMvir()/(5*Math.pow(10, 45)))) +0.05f);
			
			
			//Considerations esthetiques
			//Ne semblent avoir que tres peu d'impact sur la performance de rendu de la simulation
			Appearance app = new Appearance();
			  
			Material mat = new Material();
			mat.setAmbientColor(new Color3f((float)Principe.amas[0][amas_index].getMvir(),1-(float)Principe.amas[0][amas_index].getMvir(),0.1f));
			mat.setDiffuseColor(new Color3f((float)Principe.amas[0][amas_index].getMvir(),1-(float)Principe.amas[0][amas_index].getMvir(),0.1f));
			mat.setSpecularColor(new Color3f((float)Principe.amas[0][amas_index].getMvir(),1-(float)Principe.amas[0][amas_index].getMvir(),0.1f));
			mat.setEmissiveColor(new Color3f(1.0f,1.0f,0.1f));
			app.setMaterial(mat);	
			  
			sphere.setAppearance(app);
			
			
			//------------------------------BRANCHGROUP COMPILATION---------------------------
			//Ajouter le Transform Group contenant l'objet a l'interpolateur
			tg.addChild(tg2);
			//Ajouter la sphere representant un corps au TransformGroup inferieur
			tg2.addChild(sphere);
			  
			//Nommer toutes les branches representant des amas de la meme maniere
			//Utile pour stopper par la suite la simulation
			objRoot.setName("amas");
			//Ajouter l'interpolateur lie a l'objet a la branche
			objRoot.addChild(tg);
			objRoot.compile();
			  
			  
			return objRoot;
	 	}
	 	
	 	

		//Ajouter de la lumiere
	 	//Facultatif puisque esthetique
	 	//Faible impact sur les performances de la simulation
		 private Light createLight() {
			 PointLight light = new PointLight(new Color3f(1.0f,
					    1.0f, 1.0f), new Point3f(0.0f,0.0f,0.0f),new Point3f(0.0f,0.0f,0.0f));

					  light.setInfluencingBounds(new BoundingSphere(new Point3d(), 100000.0));
					  light.setName("Lumiere");
					  return light;
		 }
		 
		//Un simple cube pour contenir les objets de la simulation.
		//Cree un cube de cote 100
		private Cube createWireCube(){
			 Appearance app = new Appearance();
			  Material mat = new Material();
			  mat.setAmbientColor(new Color3f(0.9f,0.9f,0.9f));
			  mat.setDiffuseColor(new Color3f(0.3f,0.3f,0.3f));
			  mat.setSpecularColor(new Color3f(0.8f,0.8f,0.8f));
			  app.setMaterial(mat);	
			  PolygonAttributes polyAttrbutes = new PolygonAttributes();
			  polyAttrbutes.setPolygonMode( PolygonAttributes.POLYGON_LINE );
			  polyAttrbutes.setCullFace(PolygonAttributes.CULL_NONE);
			  app.setPolygonAttributes(polyAttrbutes);
			  Cube cc = new Cube(50);
			  cc.setAppearance(app);
			  
			  return cc;
		}
		
		private BranchGroup createText2D(String content,Vector3d pos){
			BranchGroup objRoot = new BranchGroup();
			TransformGroup tg = new TransformGroup();
			Transform3D t3d = new Transform3D();
			t3d.setTranslation(pos);
			
			Text2D txt = new Text2D(content,new Color3f(0.9f,0.9f,0.9f),"TimesRoman", 500, 10);
			tg.setTransform(t3d);
			tg.addChild(txt);
			objRoot.addChild(tg);
			return objRoot;
		}

}
