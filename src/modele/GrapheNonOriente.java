package modele;

public class GrapheNonOriente extends Graphe {
    
    // Constructeur
    public GrapheNonOriente(int n) {
        super(n); //appelle le constructeur de la classe parente Graphe avec le paramètre n
    }
    
    // Ajouter une arête {s,t}
    @Override
    public void ajouterLien(int s, int t) {
        if (matrice[s][t] == 0) {
            matrice[s][t] = 1;
            matrice[t][s] = 1;  // Symétrie !
            nbArcs++;  // On compte l'arête une seule fois
        }
    }
    
    // Pour la compatibilité
    @Override
    public void ajouterLien(int s, int t, int poids) {
        ajouterLien(s, t);
    }
    
    // Supprimer une arête
    @Override
    public void supprimerLien(int s, int t) {
        if (matrice[s][t] == 1) {
            matrice[s][t] = 0;
            matrice[t][s] = 0;  // Symétrie !
            nbArcs--;
        }
    }
    
    // Vérifier existence arête
    @Override
    public boolean existeLien(int s, int t) {
        return matrice[s][t] == 1;
    }
    
    // Construire FS et APS
    @Override
    public void construireFS_APS() {
        // Même logique que orienté
        int taille = nbSommets + 2 * nbArcs + nbSommets;
        fs = new int[taille + 1];
        aps = new int[nbSommets + 1];
        
        fs[0] = taille;
        aps[0] = nbSommets;
        
        int indice = 1;
        for (int s = 1; s <= nbSommets; s++) {
            aps[s] = indice;
            for (int t = 1; t <= nbSommets; t++) {
                if (matrice[s][t] == 1) {
                    fs[indice] = t;
                    indice++;
                }
            }
            fs[indice] = 0;
            indice++;
        }
    }
    
    // Calculer degré
    public int[] calculerDegres() {
        int[] degres = new int[nbSommets + 1];
        for (int i = 1; i <= nbSommets; i++)
            for (int j = 1; j <= nbSommets; j++)
                if (matrice[i][j] == 1)
                    degres[i]++;
        return degres;
    }
}