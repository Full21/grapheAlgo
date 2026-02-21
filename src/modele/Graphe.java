package modele;

public abstract class Graphe {
    
    // Attributs communs
    protected int nbSommets;           // n
    protected int nbArcs;              // m (ou arêtes)
    protected int[][] matrice;         // matrice d'adjacence
    protected int[] fs;                // file des successeurs/voisins
    protected int[] aps;               // adresses premiers successeurs/voisins
    protected String[] nomsSommets;    // noms des sommets
    
    // Constructeurs
    public Graphe(int n) {
        this.nbSommets = n;
        this.matrice = new int[n + 1][n + 1];
        this.nomsSommets = new String[n + 1];
        
        for (int i = 1; i <= n; i++) {
            nomsSommets[i] = "S" + i;
        }
    }
    
    // Getters/Setters
    public int getNbSommets() { return nbSommets; }
    public int getNbArcs() { return nbArcs; }
    public int[][] getMatrice() { return matrice; }
    public int[] getFS() { return fs; }
    public int[] getAPS() { return aps; }
    public String getNomSommet(int s) { return nomsSommets[s]; }
    public void setNomSommet(int s, String nom) { nomsSommets[s] = nom; }
    
    // Méthodes abstraites (à implémenter dans les classes filles)
    public abstract void ajouterLien(int s, int t);
    public abstract void ajouterLien(int s, int t, int poids);
    public abstract void supprimerLien(int s, int t);
    public abstract boolean existeLien(int s, int t);
    public abstract void construireFS_APS();

}

/*
    
    // Méthodes concrètes communes (sauf pour nonorientepondere il faut redefinir avec un override et on met pas abstract devant ici sinon faut redefinir partout)
    public void afficherMatrice() {
        System.out.println("\nMatrice d'adjacence :");
        System.out.print("    ");
        for (int j = 1; j <= nbSommets; j++)
            System.out.printf("%3d", j);
        System.out.println();
        
        for (int i = 1; i <= nbSommets; i++) {
            System.out.printf("%3d ", i);
            for (int j = 1; j <= nbSommets; j++)
                System.out.printf("%3d", matrice[i][j]);
            System.out.println();
        }
    }
    
    public void afficherFS_APS() {
        System.out.println("\nFS  : " + java.util.Arrays.toString(fs));
        System.out.println("APS : " + java.util.Arrays.toString(aps));
    }
*/
