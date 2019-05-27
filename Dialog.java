import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JTextField;

public class Dialog extends JDialog {
  private DialogInfo Info = new DialogInfo();
  private boolean sendData;
  private JLabel nomLabel, dureesimu1, dureesimu2;
  private JTextField annee, seconde;

  public Dialog(JFrame parent, String title, boolean modal){
    super(parent, title, modal);
    this.setSize(550, 270);
    this.setLocationRelativeTo(null);
    this.setResizable(false);
    this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
    this.initComponent();
  }

  public DialogInfo showDialog(){
    this.sendData = false;
    this.setVisible(true);      
    return this.Info;      
  }

  private void initComponent(){
    //L'annee
    JPanel panAnnee = new JPanel();
    panAnnee.setBackground(Color.white);
    panAnnee.setPreferredSize(new Dimension(220, 60));
    annee = new JTextField();
    annee.setPreferredSize(new Dimension(100, 25));
    panAnnee.setBorder(BorderFactory.createTitledBorder("annee de fin de la simulation"));
    nomLabel = new JLabel("Saisir la Date :");
    panAnnee.add(nomLabel);
    panAnnee.add(annee);
    
    JPanel panDuree = new JPanel();
    panDuree.setBackground(Color.white);
    panDuree.setPreferredSize(new Dimension(220, 60));
    panDuree.setBorder(BorderFactory.createTitledBorder("Duree de la simulation"));
    dureesimu1 = new JLabel("Duree : ");
    dureesimu2 = new JLabel(" seconde");
    seconde = new JTextField("180");
    seconde.setPreferredSize(new Dimension(90, 25));
    panDuree.add(dureesimu1);
    panDuree.add(seconde);
    panDuree.add(dureesimu2);
 
    JButton cancelBouton = new JButton("Annuler");
    cancelBouton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent arg0) {
        setVisible(false);
      }      
    });
    
    JPanel content = new JPanel();
    content.setBackground(Color.white);
    content.add(panAnnee);
    content.add(panDuree);


    
    
    
    
    
    
    JPanel control = new JPanel();
    JButton okBouton = new JButton("OK");
    
    okBouton.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent arg0) {        
    	  Info = new DialogInfo(annee.getText());
        setVisible(false);
        
        
      }
    });

    
    control.add(okBouton);
    control.add(cancelBouton);

    this.getContentPane().add(content, BorderLayout.CENTER);
    this.getContentPane().add(control, BorderLayout.SOUTH);
  }
  
}