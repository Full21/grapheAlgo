package vue;

/**
 * COUCHE 9 — AFFICHAGE CONSOLE
 * Utilitaire d'affichage textuel des graphes et résultats.
 */
public class AffichageConsole {

    private Graphe<?> graphe;

    public AffichageConsole(Graphe<?> graphe) {
        this.graphe = graphe;
    }

    public void afficherMatrice(int[][] matrice) {
        System.out.println("── Matrice d'adjacence ──");
        for (int i = 1; i < matrice.length; i++) {
            System.out.print("  [ ");
            for (int j = 1; j < matrice[i].length; j++) {
                System.out.printf("%2d ", matrice[i][j]);
            }
            System.out.println("]");
        }
    }

    public void afficherFS_APS(int[] fs, int[] aps) {
        System.out.print("FS  : ");
        for (int v : fs)  System.out.print(v + " ");
        System.out.print("\nAPS : ");
        for (int v : aps) System.out.print(v + " ");
        System.out.println();
    }

    public void afficherListeAdjacence() {
        System.out.println("── Liste d'adjacence ──");
        graphe.afficher();
    }

    public void afficherResultatAlgo(String resultat) {
        System.out.println("── Résultat ──");
        System.out.println(resultat);
    }

    public void afficherGantt(Ordonnancement ordo) {
        ordo.afficherGantt();
    }

    public void exporterFichier(String chemin) {
        graphe.sauvegarder(chemin);
        System.out.println("Export vers : " + chemin);
    }
}
