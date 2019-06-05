package Interface_Utilisateur;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Fenetre extends JFrame {
  private JButton bouton = new JButton("Simulation Du projet Galaxie");
  public Dialog dialog;
  public Navigator navigator;
  
  public Fenetre(){      
    this.setTitle("Ma JFrame");
    this.setSize(400, 100);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLocationRelativeTo(null);      
    this.getContentPane().setLayout(new FlowLayout());
    this.getContentPane().add(bouton);
    bouton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent arg0) {
    	dialog = new Dialog(null, "Saisie des parametres de la simulation", true);
        DialogInfo Info = dialog.showDialog(); 
      }         
    });      
    this.setVisible(true);      
  }
   
  public void closeDialog(){
	  this.dialog.closeDialog();
  }
  
  public void openSimulationNavigator(){
	  this.setVisible(false);
	  this.navigator = new Navigator(0,100,200,200);
  }
}