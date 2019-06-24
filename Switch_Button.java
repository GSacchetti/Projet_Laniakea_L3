package Interface_Utilisateur;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;

/**
 * Classe permettant de creer les boutons necessaires aux reglages de la simulation
 * @author Guillaume Fourniols
 *
 */
public class Switch_Button extends JButton implements MouseListener{

	private String on, off;
	public boolean state;
	/**
	 * Creer un bouton alternant sur deux etats, portant les noms respectivement associes
	 * @param initial_state etat initial du bouton
	 * @param texte1 Nom du bouton lorsqu'il est en etat haut
	 * @param texte2 Nom du bouton lorsqu'il est en etat bas
	 */
	public Switch_Button (boolean initial_state, String texte1, String texte2){
		this.state = initial_state;
		this.on = texte1;
		this.off = texte2;
		this.addMouseListener((MouseListener) this);
		update();
		updateColor();
	}

	/**
	 * Creer un bouton alternant sur deux etats, portant les noms respectivement associes
	 * @param texte1 Nom du bouton lorsqu'il est en etat haut
	 * @param texte2 Nom du bouton lorsqu'il est en etat bas
	 */
	public Switch_Button (String texte1, String texte2){
		this.state = true;
		this.on = texte1;
		this.off = texte2;
		this.addMouseListener((MouseListener) this);
		update();
	}

	/**
	 * Remet a jour la couleur pour la faire correspondre a l'etat du bouton
	 */
	private void updateColor(){
		if(state){
			this.setBackground(new Color(200,240,200));
		}
		else{
			this.setBackground(new Color(220,200,200));
		}
	}
	
	/**
	 * Met a jor le texte du bouton pour le faire correspondre a son etat
	 */
	public void update(){
		if(state){
			this.setText(on);
		}
		else{
			this.setText(off);
		}
	}

	/**
	 * Permet de controler l'etat du bouton
	 */
	public void mouseClicked(MouseEvent arg0) {
		this.state = !this.state;
		update();
		updateColor();
		
	}
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
}
