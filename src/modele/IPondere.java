package modele;

public interface IPondere {
   public double getPoids(int sommet1 , int sommet2);
   public void setPoids(int sommet1 , int sommet2 , double poids);
   public double [][]getMatricePoids();
   final int MAX_POIDS = 100;
   
   public static void chargerTableauDouble(String ligne, double[] tab) {
	    String[] valeurs = ligne.split(";");
	    for (int i = 0; i < valeurs.length && i < tab.length; i++) {
	        tab[i] = Double.parseDouble(valeurs[i]);
	    }
	}
   
   
}
