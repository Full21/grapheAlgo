package modele;

import java.util.List;


public interface IGraphe<T> {
	
    void ajouterSommet(T donnee);
    void supprimerSommet(T donnee);

  
    void ajouterArc(T donnee1, T donnee2);    
    void supprimerArc(T donnee1, T donnee2);
    void supprimerArcs(T donnee1);  

    List<T> getVoisins(T sommet);
    int getOrdre();

    int[][] getMatriceAdjacence();
    int[]   getFs();
    int[]   getAps();
}
