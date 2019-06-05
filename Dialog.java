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

public class Dialog extends JDialog {
	private DialogInfo Info = new DialogInfo();
	private JLabel nomLabel, dureesimu1, dureesimu2;
	private JTextField annee, seconde, percent, taille;
	private JButton cancelBouton, okBouton;
	private JPanel control;

	private int width = 800, height = 300;

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

	private void initComponent(){
		//Regule le temps parcouru lors de la simulation
		JPanel panAnnee = new JPanel();
		panAnnee.setBackground(Color.white);
		panAnnee.setPreferredSize(new Dimension(350, 60));
		annee = new JTextField("100");
		annee.setPreferredSize(new Dimension(100, 25));
		panAnnee.setBorder(BorderFactory.createTitledBorder("Temps a parcourir (en millions d'annees)"));
		nomLabel = new JLabel("Saisir la Date :");
		panAnnee.add(nomLabel);
		panAnnee.add(annee);

		//Regule la duree de la simulation
		JPanel panDuree = new JPanel();
		panDuree.setBackground(Color.white);
		panDuree.setPreferredSize(new Dimension(350, 60));
		panDuree.setBorder(BorderFactory.createTitledBorder("Duree de la simulation (en secondes)"));
		dureesimu1 = new JLabel("Duree : ");
		dureesimu2 = new JLabel(" seconde");
		seconde = new JTextField("5");
		seconde.setPreferredSize(new Dimension(90, 25));
		panDuree.add(dureesimu1);
		panDuree.add(seconde);
		panDuree.add(dureesimu2);


		//Regule la taille de la simulation
		JPanel panTaille = new JPanel();
		panTaille.setBackground(Color.white);
		panTaille.setPreferredSize(new Dimension(350, 60));
		panTaille.setBorder(BorderFactory.createTitledBorder("Taille de la simulation (en MegaParsec)"));
		taille = new JTextField("100");
		taille.setPreferredSize(new Dimension(90, 25));
		panTaille.add(taille);

		cancelBouton = new JButton("Annuler");
		cancelBouton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
			}      
		});

		JPanel content = new JPanel();
		content.setBackground(Color.white);
		content.add(panAnnee);
		content.add(panDuree);
		content.add(panTaille);


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

				Visualisation.Simulation.setRescaling(Integer.parseInt(taille.getText()));
				Main.Main.annee_fin = Integer.parseInt(annee.getText());
				Main.Main.duree_simulation = Integer.parseInt(seconde.getText());
				Main.Main.delta_t = Main.Main.annee_fin / Main.Main.duree_simulation;
				hideComponents();
				try {
					Main.Main.lancer_calcul();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});


		control.add(okBouton);
		control.add(cancelBouton);

		this.getContentPane().add(content, BorderLayout.CENTER);
		this.getContentPane().add(control, BorderLayout.SOUTH);
	}

	/**
	 * cache les composants inutiles pendant le calcul de la simulation
	 */
	public void hideComponents(){
		seconde.setEditable(false);
		annee.setEditable(false);
		taille.setEditable(false);
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