package vue;

import java.util.List;

import modele.Arc;
import modele.Graphe;
import modele.Ordonnancement;
import modele.Sommet;

/**
 * COUCHE 9 — AFFICHAGE CONSOLE
 *
 * Utilitaire d'affichage textuel des graphes et des résultats d'algorithmes.
 * Toutes les méthodes écrivent sur System.out avec un formatage homogène.
 */
public class AffichageConsole {

    private Graphe<?> graphe;

    public AffichageConsole(Graphe<?> graphe) {
        this.graphe = graphe;
    }

    /** Affiche un en-tete recapitulatif du graphe (type, sommets, arcs). */
    public void afficherEntete() {
        if (graphe == null) {
            System.out.println("(aucun graphe)");
            return;
        }
        System.out.println("=== Graphe ===");
        System.out.println("  Type    : "
            + (graphe.isEstOriente() ? "Oriente" : "Non oriente")
            + (graphe.isEstPondere() ? " pondere" : " non pondere"));
        System.out.println("  Sommets : " + graphe.getSommets().size());
        System.out.println("  Arcs    : " + graphe.getArcs().size());
    }

    /** Affiche la matrice d'adjacence (en omettant la ligne et colonne 0). */
    public void afficherMatrice(int[][] matrice) {
        System.out.println("-- Matrice d'adjacence --");
        if (matrice == null || matrice.length <= 1) {
            System.out.println("  (vide)");
            return;
        }

        // En-tete avec numeros de colonnes
        System.out.print("       ");
        for (int j = 1; j < matrice.length; j++) {
            System.out.printf("%3d ", j);
        }
        System.out.println();

        // Ligne separatrice
        System.out.print("      +");
        for (int j = 1; j < matrice.length; j++) System.out.print("----");
        System.out.println();

        // Lignes de la matrice
        for (int i = 1; i < matrice.length; i++) {
            System.out.printf("  %3d | ", i);
            for (int j = 1; j < matrice[i].length; j++) {
                System.out.printf("%3d ", matrice[i][j]);
            }
            System.out.println();
        }
    }

    /** Affiche les tableaux FS et APS. */
    public void afficherFS_APS(int[] fs, int[] aps) {
        System.out.print("FS  : [ ");
        for (int v : fs)  System.out.print(v + " ");
        System.out.println("]");

        System.out.print("APS : [ ");
        for (int v : aps) System.out.print(v + " ");
        System.out.println("]");
    }

    /** Affiche la liste des sommets. */
    public void afficherSommets() {
        System.out.println("-- Sommets --");
        if (graphe == null || graphe.getSommets().isEmpty()) {
            System.out.println("  (aucun)");
            return;
        }
        for (int i = 0; i < graphe.getSommets().size(); i++) {
            Sommet<?> s = graphe.getSommets().get(i);
            System.out.println("  " + (i + 1) + ". " + s.getDonnee()
                + "  (id=" + s.getId() + ")");
        }
    }

    /** Affiche la liste des arcs. */
    public void afficherArcs() {
        System.out.println("-- Arcs --");
        if (graphe == null || graphe.getArcs().isEmpty()) {
            System.out.println("  (aucun)");
            return;
        }
        String fleche = graphe.isEstOriente() ? " -> " : " -- ";
        for (Arc<?> a : graphe.getArcs()) {
            String poids = graphe.isEstPondere() ? "  [poids=" + a.getPoids() + "]" : "";
            System.out.println("  " + a.getSource().getDonnee() + fleche
                + a.getDestination().getDonnee() + poids);
        }
    }

    /** Delegue a graphe.afficher() (existant dans le modele). */
    public void afficherListeAdjacence() {
        System.out.println("-- Liste d'adjacence --");
        if (graphe != null) graphe.afficher();
    }

    /** Bandeau encadre pour titrer le resultat d'un algorithme. */
    public void afficherTitreAlgo(String titre) {
        String ligne = "------------------------------------------------------------";
        System.out.println();
        System.out.println(ligne);
        System.out.println("  " + titre.toUpperCase());
        System.out.println(ligne);
    }

    /** Affiche un message de resultat simple. */
    public void afficherResultatAlgo(String resultat) {
        System.out.println("-- Resultat --");
        System.out.println(resultat);
    }

    /** Affiche une matrice de doubles (utilisee par Dijkstra/Dantzig). */
    public void afficherMatriceDouble(double[][] m, int n, double seuilInfini) {
        // En-tete
        System.out.print("       ");
        for (int j = 1; j <= n; j++) System.out.printf("%8d ", j);
        System.out.println();

        System.out.print("      +");
        for (int j = 1; j <= n; j++) System.out.print("---------");
        System.out.println();

        // Lignes
        for (int i = 1; i <= n; i++) {
            System.out.printf("  %3d | ", i);
            for (int j = 1; j <= n; j++) {
                if (m[i][j] >= seuilInfini || Double.isInfinite(m[i][j])) {
                    System.out.print("     INF ");
                } else {
                    System.out.printf("%8.1f ", m[i][j]);
                }
            }
            System.out.println();
        }
    }

    /** Affiche une matrice d'entiers (utilisee par calculerDistances). */
    public void afficherMatriceInt(int[][] m) {
        if (m == null) return;
        for (int i = 0; i < m.length; i++) {
            System.out.print("  " + (i + 1) + " : ");
            for (int j = 0; j < m[i].length; j++) {
                System.out.print((m[i][j] == -1 ? "INF " : m[i][j] + "  "));
            }
            System.out.println();
        }
    }

    /** Affiche une liste de composantes (Tarjan, etc.). */
    public void afficherListeComposantes(List<? extends List<?>> composantes, String nom) {
        for (int i = 0; i < composantes.size(); i++) {
            System.out.println("  " + nom + " " + (i + 1) + " : " + composantes.get(i));
        }
    }

    /** Affiche un tableau d'entiers (par ex : codage de Prufer). */
    public void afficherTableauEntiers(int[] tab, int debut, String nom) {
        System.out.print(nom + " : [");
        for (int i = debut; i < tab.length; i++) {
            System.out.print(tab[i]);
            if (i < tab.length - 1) System.out.print(", ");
        }
        System.out.println("]");
    }

    public void afficherGantt(Ordonnancement ordo) {
        if (ordo != null) ordo.afficherGantt();
    }

    public void exporterFichier(String chemin) {
        if (graphe == null) {
            System.out.println("(aucun graphe a exporter)");
            return;
        }
        graphe.sauvegarder(chemin);
        System.out.println("Export vers : " + chemin);
    }

    /** Affiche un separateur visuel. */
    public void separateur() {
        System.out.println("------------------------------------------------------------");
    }

    public Graphe<?> getGraphe()                    { return graphe; }
    public void     setGraphe(Graphe<?> graphe)     { this.graphe = graphe; }
}  