import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Fenetre extends JFrame {
  private JButton bouton = new JButton("Simulation Du projet Galaxie");

  public Fenetre(){      
    this.setTitle("Ma JFrame");
    this.setSize(300, 100);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setLocationRelativeTo(null);      
    this.getContentPane().setLayout(new FlowLayout());
    this.getContentPane().add(bouton);
    bouton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent arg0) {
    	  Dialog d = new Dialog(null, "Ecran d'informatioin de la simulation", true);
        DialogInfo Info = d.showDialog(); 
        JOptionPane jop = new JOptionPane();
        jop.showMessageDialog(null, Info.toString(), "Informations personnage", JOptionPane.INFORMATION_MESSAGE);
      }         
    });      
    this.setVisible(true);      
  }
   
  public static void main(String[] main){
    Fenetre fen = new Fenetre();
  }   
}