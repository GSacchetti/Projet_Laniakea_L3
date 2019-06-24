package Interface_Utilisateur;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

import javax.swing.*;

/**
 * 
 * Instancie une fenetre de dialogue avec l'utilisateur
 * Permet de regler les parametres de calcul et de rendu de la simulation.
 */
public class Dialog extends JDialog {
	private DialogInfo Info = new DialogInfo();
	private JLabel nombre_millions_annees, nombre_millions_annees_2, dureesimu1, dureesimu2, taille_simu, taille_simu_2, keyframerate, instructions, instructions2, instructions3;
	private JTextField annee, seconde, percent, taille, nombre, framerate;
	private Switch_Button masse_limite, expansion;
	private JButton cancelBouton, okBouton;
	private JPanel content, control, panExpansion;

	private int width = 800, height = 350;

	public Dialog(JFrame parent, String title, boolean modal){
		super(parent, title, modal);
		this.setSize(width, height);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.initComponent();

		this.percent = new JTextField();
	}

	public DialogInfo showDialog(){
		this.setVisible(true);      
		return this.Info;      
	}

	/**
	 * Prepare les composants aux interactions
	 */
	private void initComponent(){
		//Regule le temps parcouru lors de la simulation
		JPanel panAnnee = new JPanel();
		panAnnee.setBackground(Color.white);
		panAnnee.setPreferredSize(new Dimension(350, 60));
		annee = new JTextField("10000");
		annee.setPreferredSize(new Dimension(100, 25));
		panAnnee.setBorder(BorderFactory.createTitledBorder("Temps a parcourir (en millions d'annees)"));
		nombre_millions_annees = new JLabel("Saisir la duree :");
		nombre_millions_annees_2 = new JLabel("Ma");
		panAnnee.add(nombre_millions_annees);
		panAnnee.add(annee);
		panAnnee.add(nombre_millions_annees_2);

		//Regule la duree de la simulation
		JPanel panDuree = new JPanel();
		panDuree.setBackground(Color.white);
		panDuree.setPreferredSize(new Dimension(350, 60));
		panDuree.setBorder(BorderFactory.createTitledBorder("Duree de la simulation (en secondes)"));
		dureesimu1 = new JLabel("Saisir la duree : ");
		dureesimu2 = new JLabel(" secondes");
		seconde = new JTextField("10");
		seconde.setPreferredSize(new Dimension(90, 25));
		panDuree.add(dureesimu1);
		panDuree.add(seconde);
		panDuree.add(dureesimu2);


		//Regule la taille de la simulation
		JPanel panTaille = new JPanel();
		panTaille.setBackground(Color.white);
		panTaille.setPreferredSize(new Dimension(350, 60));
		panTaille.setBorder(BorderFactory.createTitledBorder("Volume de la simulation (en MegaParsec)"));
		taille = new JTextField("100");
		taille.setPreferredSize(new Dimension(90, 25));
		taille_simu = new JLabel("Saisir le volume :");
		taille_simu_2 = new JLabel("Mpc³");
		panTaille.add(taille_simu);
		panTaille.add(taille);
		panTaille.add(taille_simu_2);


		//Regule le nombre d'objets representes dans la simulation
		JPanel panNombre = new JPanel();
		panNombre.setBackground(Color.white);
		panNombre.setPreferredSize(new Dimension(350, 60));
		panNombre.setBorder(BorderFactory.createTitledBorder("Maximum d'objets rendus (affecte uniquement le rendu)"));
		nombre = new JTextField("maximal");
		nombre.setPreferredSize(new Dimension(90, 25));
		panNombre.add(nombre);

		//Regule le nombre d'objets representes dans la simulation
		JPanel panFramerate = new JPanel();
		panFramerate.setBackground(Color.white);
		panFramerate.setPreferredSize(new Dimension(350, 60));
		panFramerate.setBorder(BorderFactory.createTitledBorder("Keyframe rate (nombre de pas par seconde)"));
		keyframerate = new JLabel("iterations/seconde");
		framerate = new JTextField("10");
		framerate.setPreferredSize(new Dimension(90, 25));
		panFramerate.add(framerate);
		panFramerate.add(keyframerate);
		
		//Permet ou pas le rendu des objets sans masse connue
		JPanel panMasse = new JPanel();
		panMasse.setBackground(Color.white);
		panMasse.setPreferredSize(new Dimension(350, 60));
		panMasse.setBorder(BorderFactory.createTitledBorder("Representer les objets sans masse connue ?"));
		masse_limite = new Switch_Button(true, "Oui", "Non");
		masse_limite.setPreferredSize(new Dimension(90, 25));
		panMasse.add(masse_limite);
		
		//Permet ou pas le rendu des objets sans masse connue
		panExpansion = new JPanel();
		panExpansion.setBackground(Color.white);
		panExpansion.setPreferredSize(new Dimension(350, 60));
		panExpansion.setBorder(BorderFactory.createTitledBorder("Simuler l'expansion de l'univers ?"));
		expansion = new Switch_Button(true, "Oui", "Non");
		expansion.setPreferredSize(new Dimension(90, 25));
		panExpansion.add(expansion);

		
		cancelBouton = new JButton("Annuler");
		cancelBouton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}      
		});

		content = new JPanel();
		content.setBackground(Color.white);
		content.add(panAnnee);
		content.add(panDuree);
		content.add(panTaille);
		content.add(panFramerate);
		content.add(panNombre);
		content.add(panMasse);
		content.add(panExpansion);


		control = new JPanel(){
			//Barre de chargement
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.setColor(new Color(Color.lightGray.getRed(),	(Color.lightGray.getGreen())+30, Color.lightGray.getBlue()));
				g.fillRect(0, 0, (int)((width/100.0) * Donnees.AnalyseDonnees.avancement), 100);
			}
		};
		okBouton = new JButton("OK");

		okBouton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {        
				if(!nombre.getText().equals("maximal")){
					Visualisation.Simulation.setMaxRendu(Integer.parseInt(nombre.getText()));
				}
				Visualisation.Simulation.setLimitMass(masse_limite.state);
				Visualisation.Simulation.setRescaling(Integer.parseInt(taille.getText()));
				Visualisation.Simulation.setKeyFramerate(Integer.parseInt(framerate.getText()));
				Main.Main.annee_fin = Integer.parseInt(annee.getText());
				Main.Main.duree_simulation = Integer.parseInt(seconde.getText());
				
				Main.Main.expansion = (expansion.state) ? 1 : 0;
				
				Main.Main.nombre_frame = Main.Main.duree_simulation * Integer.parseInt(framerate.getText());
				
				Main.Main.delta_t = Main.Main.annee_fin / Main.Main.nombre_frame;
				hideComponents();
				try {Main.Main.lancer_calcul();} 
				catch (IOException e) {e.printStackTrace();}
			}
		});


		control.add(okBouton);
		control.add(cancelBouton);

		this.getContentPane().add(content, BorderLayout.CENTER);
		this.getContentPane().add(control, BorderLayout.SOUTH);
	}

	/**
	 * cache les composants inutiles pendant le calcul de la simulation
	 * empeche l'ecriture des zones de texte
	 */
	public void hideComponents(){
		seconde.setEditable(false);
		annee.setEditable(false);
		taille.setEditable(false);
		nombre.setEditable(false);
		framerate.setEditable(false);
		expansion.setVisible(false);
		panExpansion.setVisible(false);

		//Permet d'afficher les instructions
		String str = "Pour vous diriger dans l'espace de la simulation, utilisez les touches flechees pour vous deplacer.";
		instructions = new JLabel();
		instructions.setText(str);
		
		str = "Pour mettre la simulation en pause (ou remettre en route), utilisez la barre d'espace.";
		instructions2 = new JLabel();
		instructions2.setText(str);
		
		str = "Utilisez la touche ECHAP pour quitter la simulation et fermer le programme.";
		instructions3 = new JLabel();
		instructions3.setText(str);
		
		content.add(instructions);
		content.add(instructions2);
		content.add(instructions3);
		
		cancelBouton.setVisible(false);
		okBouton.setVisible(false);
		percent.setText("		Progression : " + String.valueOf(Donnees.AnalyseDonnees.avancement) + "%		");
		percent.setSize(new Dimension(200, percent.getSize().height));
		percent.setEditable(false);
		control.add(percent);	  
	}

	/**
	 * Met a jour le texte contenant le pourcentage de calcul de la simulation
	 */
	public void updateComponents(){ 
		percent.setText("		Progression : " + String.valueOf(Donnees.AnalyseDonnees.avancement) + "%		");
		control.repaint();
	}

	public void closeDialog() {
		this.setVisible(false);
	}



}