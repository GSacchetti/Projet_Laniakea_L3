package Interface_Utilisateur;

public class DialogInfo {
  private String annee, second;

  public DialogInfo(){}
  public DialogInfo(String annee){
    this.annee = annee;
    this.second = second;
  }

  public String toString(){
    String str;
    if(this.annee != null){
      str = "Description de l'objet InfoDialog";
      str += "annee : " + this.annee + "\n";
      str += "second :" + this.second + "\n";
    }
    else{
      str = "Aucune information !";
    }
    return str;
  }
}