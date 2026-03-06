package modele;

import java.util.List;


public interface IGraphe<T> {
	
    void ajouterSommet(T donnee);
    void supprimerSommet(T donnee);

  
    void ajouterArc(Sommet<T> sommet1, Sommet<T> sommet2, double poids);
    void ajouterArc(Sommet<T> sommet1, Sommet<T> sommet2);
    void supprimerArcs(Sommet<T> sommet);
    void supprimerArc(Sommet<T> sommet1, Sommet<T> sommet2);

    List<T> getVoisins(T sommet);
    int getOrdre();

    int[][] getMatriceAdjacence();
    int[]   getFs();
    int[]   getAps();
}
