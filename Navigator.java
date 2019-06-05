package Interface_Utilisateur;

import javax.swing.JFrame;

public class Navigator extends JFrame{
	
	int width;
	int height;
	
	public Navigator(int x, int y, int width, int height){
		this.setBounds(x, y, width, height);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setAlwaysOnTop(true);
		this.setUndecorated(true);
		this.setOpacity(0.5f);
		
		this.setVisible(true);
	}
}
