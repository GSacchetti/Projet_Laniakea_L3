package Visualisation;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
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

//Classe dediee a la visualisation du calcul realisee par Donnes.AnalyseDonnees
public class Simulation extends Applet{
	

	private static final long serialVersionUID = 1L;
	private SimpleUniverse universe = null;
	private Canvas3D canvas = null;
	private TransformGroup view_trans = null;
	private BranchGroup view_trans_branch = null;
	private BranchGroup root;
	
	private BranchGroup year;
	
	//Remise a echelle de la simulation
	private static double RESCALING = 1.0*Math.pow(10, 23);
	private static double MEGAPARSEC = 3.086*Math.pow(10, 22);
	//Taille du cube contenant la simulation
	private final static float SIMULATION_SIZE = 50.0f;
	//Coordonnees d'explusion d'un objet sortant du cube
	private final static float EXPULSION_RANGE = (float) Math.pow(10, 5);
	//Masse limite d'affichage des amas
	private static double MASS_LIMIT = 1.0;
	//Masse maximale existante dans les donnees
	private static double MASS_MAX = 5.859151867440001*Math.pow(10, 46);
	//Nombre maximum d'objet rendu
	private static int MAX_RENDU = Integer.MAX_VALUE;
	
	//Keyframe rate de la simulation (temps pour passer d'une keyframe a une autre
	private static int KEYFRAME_RATE = 4;
	
	
	//Amas selectionne pour etre mis en evidence lors de la simulation
	private static int viewable_cluster = 1;
	
	private static int avecmasse = 0;
	private static int sansmasse = 0;
	
	public Simulation() {
		 super.init();
		 setLayout(new BorderLayout());
		 //-------------------3D RELATED------------------------------
		 GraphicsConfiguration config = SimpleUniverse.getPreferredConfiguration();

		 canvas = new Canvas3D(config);

		  
		 universe = new SimpleUniverse(canvas);
		 universe.getViewingPlatform().setNominalViewingTransform();
		 
		 root = createSceneGraph();
		 universe.getViewer().getView().setBackClipDistance(1000.0);
		 universe.addBranchGraph(root);
		 //end of 3D setup	  

		 
		 add(canvas);	
		 setViewerPosition(new Vector3f(0.0f,0.0f,300.0f));
		 
		 }
	 
	
	/**Initialisation de la scene 3D
	*Retourne une branch contenant l'integralite des objets contenus dans la scene
	*/
	 private BranchGroup createSceneGraph() {
		  BranchGroup objRoot = new BranchGroup();
		  BoundingSphere bounds = new BoundingSphere(new Point3d(), 100.0);

		  view_trans_branch = universe.getViewingPlatform();
		  view_trans = universe.getViewingPlatform().getViewPlatformTransform();
		  
			
		  //---------------------------DEPLACEMENT DANS LA SCENE--------------------
		  Simulation_Navigator_Behaviour sim_nav = new Simulation_Navigator_Behaviour(view_trans, new Vector3d(50.0f,50000.0f,50.0f));
		  sim_nav.setSchedulingBounds(bounds);
		  PlatformGeometry pg = new PlatformGeometry();
		  pg.addChild(sim_nav);
		  universe.getViewingPlatform().setPlatformGeometry(pg);
		  

		 //---------------------------AJOUT DU CONTENU------------------------------
		 //Ajouter les amas
		  int nombre_rendu = Math.min(Main.Main.amas[0].length, MAX_RENDU);
		  
		  for(int i = 0; i<nombre_rendu;i++){
			  if(Main.Main.amas[0][i].getMvir()>MASS_LIMIT){
				  objRoot.addChild(create_amas(i));
			  		avecmasse++;
			  }
			  else{
				  sansmasse++;
			  }
		  }
		  
		  //Ajouter les vecteurs vitesses
//		  for(int i = 0; i<nombre_rendu;i++){
//			  if(Main.Main.amas[0][i].getMvir()>MASS_LIMIT){
//				  objRoot.addChild(create_vector(i));
//			  }
//		  }
		  System.out.println("Total des objets representes :" + (avecmasse));
		  System.out.println("	- Superieur a la masse limite : " + avecmasse);
		  System.out.println("	- Inferieur a la masse limite : " + sansmasse);
		  
		  objRoot.addChild(createLight());
		  objRoot.addChild(createWireCube());
		  //Texte a afficher pour echelle de la simulation
		  objRoot.addChild(createText2D(("Unit : \n" + (RESCALING*10.0)) + " m",new Vector3d(-50.0f,-56.0f,50.0f)));
		  objRoot.addChild(createText2D(("Total : \n" + (RESCALING*100.0)) + " m³",new Vector3d(52.0f,0.0f,50.0f)));
		  
		  year = createText2D(("Annee : \n" + (0)) + " Ma",new Vector3d(30.0f,-56.0f,50.0f));
		  objRoot.addChild(year);
		 
		  return objRoot;
		 }

	 
	 	/**
	 	* Cree une branche contenant la representation d'un amas
	 	* Prend en parametre l'indice de ce dernier dans le tableau rendu par l'exploitation des donnees
	 	* A l'objet rendu est integre le chemin qu'il suivra au cours de la simulation
	 	* @param amas_index l'index dans le tableau d'Amas de Main.Main a instancier
	 	*/
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
			 Alpha transAlpha=new Alpha(-1,(int)(Main.Main.nombre_frame*(1000.0/(double)KEYFRAME_RATE)));
			
			//Creation du chemin : un amas prendra autant de positions successives (keyframes, pas position reelle)
			//qu'il y a de frame dans la simulation
			Point3f[] chemin=new Point3f[Main.Main.amas.length];
			float x, y,z;
			for(int i = 0; i<Main.Main.amas.length;i++){
				
				x = (float)(Main.Main.amas[i][amas_index].getPos().getX() / RESCALING);
				y = (float)(Main.Main.amas[i][amas_index].getPos().getY() / RESCALING);
				z = (float)(Main.Main.amas[i][amas_index].getPos().getZ() / RESCALING);
				
				//X REG
				if( x > SIMULATION_SIZE){
					x = EXPULSION_RANGE;
				}
				if( x < -SIMULATION_SIZE){
					x = -EXPULSION_RANGE;
				}
				
				//Y REG
				if( y > SIMULATION_SIZE){
					y = EXPULSION_RANGE;
				}
				if( y < -SIMULATION_SIZE){
					y = -EXPULSION_RANGE;
				}
				
				//Z REG
				if( z > SIMULATION_SIZE){
					z = EXPULSION_RANGE;
				}
				if( z < -SIMULATION_SIZE){
					z = -EXPULSION_RANGE;
				}
				
				chemin[i]=new Point3f(x,y,z);
				
			}
			
			//Creer un tableau de correspondance
			//Divise la totalite des positions des frames sur une echelle de 0 a 1
			float[] timePosition= new float[Main.Main.amas.length];
			for(int i = 0; i<Main.Main.amas.length;i++){
				timePosition[i] = ((float)i) *(1.0f/(float)(Main.Main.amas.length-1));
				//La position a t = i est donc ( i * echelle de temps separant deux frames)
				
			}
			
			//On applique les transitions a l'interpolateur
			PositionPathInterpolator interpol=new PositionPathInterpolator(transAlpha,tg,t3d,timePosition,chemin);
			
			//Definir la zone sur laquelle va s'appliquer le chemin.
			//Peut avoir des utilites en terme de performance.
			//Dans la logique d'une simulation, on preferera permettre la mouvement quel que soit la distance a l'objet.
			BoundingSphere bounds=new BoundingSphere();
			bounds.setRadius(1000000.0);
			interpol.setSchedulingBounds(bounds);
			interpol.setName("interpolateur");
			
			//On applique l'interpolateur a la branche. (BRANCH CHILD 0)
			tg.addChild(interpol);
			
			//--------------------------------RENDERING------------------------------------
			
			double masse = Main.Main.amas[0][amas_index].getMvir();
			float mass_visual = (float)(Math.sqrt(Math.sqrt(masse/MASS_MAX)));
			
			Sphere sphere;
			if(masse>0.0){
				sphere = new Sphere(mass_visual +0.05f);
			}
			else{
				sphere = new Sphere(0.05f,1,5);
			}
			
			//Considerations esthetiques
			//Ne semblent avoir que tres peu d'impact sur la performance de rendu de la simulation
			Appearance app = new Appearance();
			
			//Variable relative a la representation d'un amas
			//Calculee a partir de la racine cubique de sa masse sur la plus grande masse existante
			mass_visual = (float)(Math.sqrt(Math.sqrt(Math.sqrt(masse/MASS_MAX))));
			
			Material mat = new Material();
			mat.setAmbientColor(new Color3f(mass_visual,1-mass_visual,0.1f));
			mat.setDiffuseColor(new Color3f(mass_visual,1-mass_visual,0.1f));
			mat.setSpecularColor(new Color3f(mass_visual,1-mass_visual,0.1f));
			mat.setEmissiveColor(new Color3f(mass_visual/(2-mass_visual),(1-mass_visual)/(2-mass_visual),0.1f));
			
			//Mise en evidence d'un amas selectionne
			if(viewable_cluster == amas_index){
				mat.setEmissiveColor(new Color3f(0.7f,0,1));
			}
			app.setMaterial(mat);	
			  
			sphere.setAppearance(app);
			
			
			//------------------------------BRANCHGROUP COMPILATION---------------------------
			//Ajouter le Transform Group contenant l'objet a l'interpolateur (BRANCH CHILD 1)
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
	 	
	 	/**
	 	 * Cree une branche contenant les vecteurs vitesse (modeles 3d) representant les deplacement de l'amas
	 	 * @param amas_index 
	 	 * @return branche contenant l'ensemble des vecteurs
	 	 */
	 	public BranchGroup create_vector(int amas_index){
	 		BranchGroup objRoot = new BranchGroup();
			TransformGroup tg;
			Transform3D t3d;
	 		float x, y, z;
	 		float x2,y2,z2;
	 		Simulation_Vector vector;
	 		
	 		Appearance app = new Appearance();
			PolygonAttributes polyAttrbutes = new PolygonAttributes();
			polyAttrbutes.setPolygonMode( PolygonAttributes.POLYGON_LINE );
			polyAttrbutes.setCullFace(PolygonAttributes.CULL_NONE);
			app.setPolygonAttributes(polyAttrbutes);

	 		for(int i = 1; i<Main.Main.amas.length;i++){
	 			tg = new TransformGroup();
	 			t3d = new Transform3D();
	 			x = (float)(Main.Main.amas[i-1][amas_index].getPos().getX() / RESCALING);
				y = (float)(Main.Main.amas[i-1][amas_index].getPos().getY() / RESCALING);
				z = (float)(Main.Main.amas[i-1][amas_index].getPos().getZ() / RESCALING);
				
				x2 = (float)(Main.Main.amas[i-1][amas_index].getPos().getX() / RESCALING);
				y2 = (float)(Main.Main.amas[i-1][amas_index].getPos().getY() / RESCALING);
				z2 = (float)(Main.Main.amas[i-1][amas_index].getPos().getZ() / RESCALING);
				
				double d = Math.sqrt((x-x2)*(x-x2)+(y-y2)*(y-y2)+(z-z2)*(z-z2));
				System.out.println(d);
				t3d.set(new Vector3f(x,y,z));
				vector = new Simulation_Vector(d);
				vector.setAppearance(app);
				tg.setTransform(t3d);
				tg.addChild(vector);
				objRoot.addChild(tg);
	 		}
	 		
			objRoot.setName("vectors");
	 		return objRoot;
	 	}
	 	

		/**Ajouter de la lumiere
	 	*Facultatif puisque esthetique
	 	*Faible impact sur les performances de la simulation
	 	*/
		 private Light createLight() {
			 PointLight light = new PointLight(new Color3f(1.0f,
					    1.0f, 1.0f), new Point3f(0.0f,0.0f,0.0f),new Point3f(0.0f,0.0f,0.0f));

					  light.setInfluencingBounds(new BoundingSphere(new Point3d(), 500.0));
					  light.setName("Lumiere");
					  return light;
		 }
		 
		/**Un simple cube pour contenir les objets de la simulation.
		*Cree un cube de cote SIMULATION_SIZE*2
		*/
		private Simulation_Cube createWireCube(){
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
			  Simulation_Cube cc = new Simulation_Cube(SIMULATION_SIZE);
			  cc.setAppearance(app);
			  
			  return cc;
		}
		
		/**
		*Cree un vecteur de taille scale oriente sur l'axe X
		*/
		private Simulation_Vector createVector(double scale){
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
			 Simulation_Vector sv = new Simulation_Vector(scale);
			 sv.setAppearance(app);
			  
			 return sv;
		}
		
		/**
		 * Cree un text en deux dimensions a l'endroit indique
		 * @param content La String a afficher
		 * @param pos position du texte
		 * @return
		 */
		private BranchGroup createText2D(String content,Vector3d pos){
			BranchGroup objRoot = new BranchGroup();
			TransformGroup tg = new TransformGroup();
			Transform3D t3d = new Transform3D();
			t3d.setTranslation(pos);
			
			Text2D txt = new Text2D(content,new Color3f(0.9f,0.9f,0.9f),"Ludica", 500, 10);
			tg.setTransform(t3d);
			tg.addChild(txt);
			objRoot.addChild(tg);
			return objRoot;
		}
		
		/**
		 * Met a jour le texte contenant la date via l'alpha value des interpolateurs
		 */
		public void update_text(){
			int year = (int)(this.getAlpha()*Main.Main.annee_fin);
			String year_str;
			year_str = ("Annee : \n" + (year) + " Ma");
			((Text2D)((TransformGroup)this.year.getChild(0)).getChild(0)).setString(year_str);;
		}
		
		
		/**
		 * Met en arret tout les interpolateurs de position de la simulation
		 * (ou les remet en marche de la meme maniere.)
		 * Pour des raisons inconnues, la remise en marche est dramatiquement plus lente
		 * que la mise en arret.
		 * @param pause_state
		 */
		public void setPause(boolean pause_state){
			for(int i = 0;i<this.root.numChildren();i++){
				if(root.getChild(i).getName()!=null){
					if(root.getChild(i).getName().equals("amas")){
						if(pause_state){
							((PositionPathInterpolator)(((TransformGroup)  ((BranchGroup)root.getChild(i)).getChild(0))  .getChild(0))).setEnable(false);
							((PositionPathInterpolator)(((TransformGroup)  ((BranchGroup)root.getChild(i)).getChild(0))  .getChild(0))).getAlpha().pause();
						}
						else{
							((PositionPathInterpolator)(((TransformGroup)  ((BranchGroup)root.getChild(i)).getChild(0))  .getChild(0))).setEnable(true);
							((PositionPathInterpolator)(((TransformGroup)  ((BranchGroup)root.getChild(i)).getChild(0))  .getChild(0))).getAlpha().resume();
						}
					}
				}
			}
		}
		
		/**
		 * Actuellement non-fonctionnel
		 * devrait permettre le reglage direct de l'alpha value des interpolateurs de position d'Amas
		 * @param alpha
		 */
		public void setAlpha(float alpha){
			for(int i = 0;i<this.root.numChildren();i++){
				if(root.getChild(i).getName()!=null)
				if(root.getChild(i).getName().equals("amas")){
					((PositionPathInterpolator)(((TransformGroup)  ((BranchGroup)root.getChild(i)).getChild(0))  .getChild(0))).computeTransform(alpha, new Transform3D());
				}
			}
		}
		
		/**
		 * Recupere la valeur alpha des interpolateurs de position.
		 * Permet de connaitre l'avancee de la simulation dans la range 0-Main.Main.anneefin
		 * @return Valeur alpha des interpolateurs (estimee egale pour tout les interpolateurs d'amas)
		 */
		public float getAlpha(){
			Alpha alpha = new Alpha();
			for(int i = 0;i<this.root.numChildren();i++){
				if(root.getChild(i).getName()!=null)
				if(root.getChild(i).getName().equals("amas")){
					alpha = ((PositionPathInterpolator)(((TransformGroup)  ((BranchGroup)root.getChild(i)).getChild(0))  .getChild(0))).getAlpha();
					break;
				}
			}
			return alpha.value();
		}
		
		/**
		 * Permet de mettre le ViewerPlatform a l'endroit desire
		 * @param v3
		 */
		public void setViewerPosition(Vector3f v3){
			  Transform3D t3d = new Transform3D();
			  t3d.setTranslation(v3);
			  view_trans.setTransform(t3d);
		}
		
		
		/**
		 * Modifie la taille de la zone a simuler
		 * @param MPCs taille totale en MegaParsec d'une arrete du cube contenant la simulation
		 */
		public static void setRescaling(int MPCs){
			RESCALING = (((double)MPCs)/100.0)*MEGAPARSEC;
		}
		
		/**
		 * Modifie le nombre de key frame a utiliser par seconde
		 * affecte uniquement les interpolateurs de position des amas
		 * @param kfr
		 */
		public static void setKeyFramerate(int kfr){
			KEYFRAME_RATE = kfr;
		}
		
		/**
		 * Redefini le nombre maximum d'objets represente dans la simulation
		 * @param max
		 */
		public static void setMaxRendu(int max){
			MAX_RENDU = max;
		}

		/**
		 * Permet de choisir la representation des objets sans masse connue
		 * @param state
		 */
		public static void setLimitMass(boolean state) {
			if(state){
				MASS_LIMIT = -1.0;
			}
			else{
				MASS_LIMIT = 1.0;
			}
			
		}
}
