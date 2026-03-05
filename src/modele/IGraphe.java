package modele;

import java.util.List;


public interface IGraphe<T> {
	
    void ajouterSommet(T donnee);
    void supprimerSommet(T donnee);

  
    void ajouterArc(T source, T destination);
    void supprimerArc(T source, T destination);

    List<T> getVoisins(T sommet);
    int getOrdre();

    int[][] getMatriceAdjacence();
    int[]   getFS();
    int[]   getAPS();
}
