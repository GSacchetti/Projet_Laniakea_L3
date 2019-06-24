package Interface_Utilisateur;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;



public class Fenetre extends JFrame {
  private JButton lancer = new JButton("Lancer une simulation du projet Galaxie");
  private JButton quitter = new JButton("Quitter");
  public Dialog dialog;
  
  /**
   * Instancie une fenetre demarrant la simulation
   */
  public Fenetre(){      
    this.setTitle("Galaxies");
    this.setSize(400, 80);
    this.setResizable(false);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLocationRelativeTo(null);      
    this.getContentPane().setLayout(new FlowLayout());
    this.getContentPane().add(lancer, BorderLayout.PAGE_START);
    this.getContentPane().add(quitter, BorderLayout.SOUTH);
    lancer.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent arg0) {
    	dialog = new Dialog(null, "Saisie des parametres de la simulation", true);
        DialogInfo Info = dialog.showDialog(); 
      }         
    });      
    quitter.addActionListener(new ActionListener(){
        public void actionPerformed(ActionEvent arg0) {
        	System.exit(2);
        }         
      }); 
    this.setVisible(true);      
  }
   
  /**
   * Ferme la fenetre de requete de parametres de la simulation
   */
  public void closeDialog(){
	  this.dialog.closeDialog();
  }
  
  public void openSimulationNavigator(){
	  this.setVisible(false);
  }
}