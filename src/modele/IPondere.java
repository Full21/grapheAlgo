package modele;

public interface IPondere {
   public double getPoids(int sommet1 , int sommet2);
   public void setPoids(int sommet1 , int sommet2 , double poids);
   public double [][]getMatricePoids();
}
