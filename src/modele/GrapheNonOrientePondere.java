package modele;

public class GrapheNonOrientePondere extends GrapheNonOriente {
    
    private static final int INFINI = Integer.MAX_VALUE;
    
    // Constructeur
    public GrapheNonOrientePondere(int n) {
        super(n);
        for (int i = 1; i <= n; i++)
            for (int j = 1; j <= n; j++)
                if (i != j)
                    matrice[i][j] = INFINI;
    }
    
    // Ajouter arête avec poids
    @Override
    public void ajouterLien(int s, int t, int poids) {
        if (matrice[s][t] == INFINI) {
            matrice[s][t] = poids;
            matrice[t][s] = poids;  // Symétrie !
            nbArcs++;
        } else {
            matrice[s][t] = poids;
            matrice[t][s] = poids;
        }
    }
    
    @Override
    public void ajouterLien(int s, int t) {
        ajouterLien(s, t, 1);
    }
    
    @Override
    public void supprimerLien(int s, int t) {
        if (matrice[s][t] != INFINI) {
            matrice[s][t] = INFINI;
            matrice[t][s] = INFINI;
            nbArcs--;
        }
    }
    
    @Override
    public boolean existeLien(int s, int t) {
        return matrice[s][t] != INFINI;
    }
    
    public int getPoids(int s, int t) {
        return matrice[s][t];
    }
}


/*

 @Override
    public void afficherMatrice() {
        System.out.println("\nMatrice des poids (symétrique) :");
        System.out.print("    ");
        for (int j = 1; j <= nbSommets; j++)
            System.out.printf("%5d", j);
        System.out.println();
        
        for (int i = 1; i <= nbSommets; i++) {
            System.out.printf("%3d ", i);
            for (int j = 1; j <= nbSommets; j++) {
                if (matrice[i][j] == INFINI)
                    System.out.print("  ∞  ");
                else
                    System.out.printf("%5d", matrice[i][j]);
            }
            System.out.println();
        }
    }

*/