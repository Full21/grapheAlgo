package modele;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;


/**
 * Graphe non orienté abstrait.
 *
 * Algorithmes implémentés d'après le cours du prof (Cours_Graphe_Algo.pdf) :
 *  - estConnexe   : BFS depuis le premier sommet
 *  - calculerRangs: degré de chaque sommet
 *  - codagePrufer : séquence de Prüfer (algorithme du cours)
 *  - decodagePrufer: reconstruction depuis la séquence
 */
public abstract class GrapheNonOriente<T> extends Graphe<T> {

    protected List<Sommet<T>> sommets;
    protected List<Arc<T>>    arcs;
    protected List<Arc<T>>    arbreCouvrant;

    public GrapheNonOriente() {
        this.sommets       = new ArrayList<>();
        this.arcs          = new ArrayList<>();
        this.arbreCouvrant = new ArrayList<>();
    }


    // =========================================================================
    // estConnexe — BFS depuis le premier sommet ; vérifie que tous sont atteints
    //
    // Traduit du parcours en largeur du cours (desc_large1) :
    //   dist[r]=0 ; tous autres = -1 (non marqués)
    //   file d'attente, exploration niveau par niveau
    // =========================================================================
    public boolean estConnexe() {
        if (sommets.isEmpty()) return true;

        int n = sommets.size();
        boolean[] marque = new boolean[n];
        int[]     file   = new int[n];
        int debut = 0, fin = 0;

        marque[0]    = true;
        file[fin++]  = 0;

        while (debut < fin) {
            int idx    = file[debut++];
            Sommet<T> s = sommets.get(idx);

            // Parcourir tous les voisins de s (graphe non orienté : les deux sens)
            for (Arc<T> arc : arcs) {
                int voisinIdx = -1;
                if (arc.getDepart().equals(s)) {
                    voisinIdx = sommets.indexOf(arc.getArrivee());
                } else if (arc.getArrivee().equals(s)) {
                    voisinIdx = sommets.indexOf(arc.getDepart());
                }
                if (voisinIdx >= 0 && !marque[voisinIdx]) {
                    marque[voisinIdx] = true;
                    file[fin++]       = voisinIdx;
                }
            }
        }

        for (boolean m : marque) {
            if (!m) return false;
        }
        return true;
    }


    // =========================================================================
    // calculerRangs — degré de chaque sommet
    //
    // Dans un graphe non orienté le "rang" est le degré :
    // chaque arête incidente contribue +1 pour chacune de ses deux extrémités.
    // =========================================================================
    public void calculerRangs() {
        for (Sommet<T> s : sommets) s.setRang(0);
        for (Arc<T> arc : arcs) {
            arc.getDepart() .setRang(arc.getDepart() .getRang() + 1);
            arc.getArrivee().setRang(arc.getArrivee().getRang() + 1);
        }
    }


    // =========================================================================
    // codagePrufer — séquence de Prüfer de l'arbre
    //
    // Algorithme du cours (slide "Codage de Prüfer") :
    //   Tant qu'il reste plus de 2 sommets :
    //     v = feuille de numéro minimum (degré 1)
    //     Ajouter le voisin unique de v à la suite P
    //     Enlever v de l'arbre
    // =========================================================================
    public int[] codagePrufer() {
        int n = sommets.size();
        if (n <= 2) return new int[0];

        int[] deg      = new int[n];
        boolean[] supprime = new boolean[n];

        // Construire la liste d'adjacence indicée
        List<List<Integer>> adj = new ArrayList<>();
        for (int i = 0; i < n; i++) adj.add(new ArrayList<>());

        for (Arc<T> arc : arcs) {
            int u = sommets.indexOf(arc.getDepart());
            int v = sommets.indexOf(arc.getArrivee());
            adj.get(u).add(v);
            adj.get(v).add(u);
            deg[u]++;
            deg[v]++;
        }

        int[] prufer = new int[n - 2];

        for (int i = 0; i < n - 2; i++) {
            // Feuille de numéro minimum
            int feuille = -1;
            for (int j = 0; j < n; j++) {
                if (!supprime[j] && deg[j] == 1) { feuille = j; break; }
            }
            // Voisin unique de cette feuille
            int voisin = -1;
            for (int v : adj.get(feuille)) {
                if (!supprime[v]) { voisin = v; break; }
            }
            prufer[i]        = voisin + 1; // numéro 1-indexé
            supprime[feuille] = true;
            deg[voisin]--;
        }

        return prufer;
    }


    // =========================================================================
    // decodagePrufer — reconstruit le graphe à partir d'une séquence de Prüfer
    //
    // Algorithme du cours (slide "Décodage") :
    //   I = {1..n}
    //   Tant que P non vide :
    //     j = min(I \ P) → arête (j, P[0])
    //     Retirer j de I, P[0] de P
    //   Fin : arête entre les 2 restants de I
    // =========================================================================
    public GrapheNonOriente<T> decodagePrufer(int[] sequence) {
        GrapheNonOrienteSimple<T> arbre = new GrapheNonOrienteSimple<>();

        int n = sequence.length + 2;

        // Créer n sommets (données entières 1..n)
        for (int i = 1; i <= n; i++) {
            @SuppressWarnings("unchecked")
            Sommet<T> s = new Sommet<>((T)(Integer) i);
            arbre.sommets.add(s);
        }

        // I = liste des sommets encore présents
        List<Integer> I = new ArrayList<>();
        for (int i = 1; i <= n; i++) I.add(i);

        // P = copie mutable de la séquence
        List<Integer> P = new ArrayList<>();
        for (int v : sequence) P.add(v);

        while (!P.isEmpty()) {
            // j = min(I \ P)
            int j = -1;
            for (int val : I) {
                if (!P.contains(val)) { j = val; break; }
            }
            int voisin      = P.get(0);
            Sommet<T> sJ     = arbre.sommets.get(j - 1);
            Sommet<T> sVoisin = arbre.sommets.get(voisin - 1);
            arbre.arcs.add(new Arc<>(sJ, sVoisin));
            I.remove((Integer) j);
            P.remove(0);
        }

        // Dernière arête : 2 sommets restants dans I
        if (I.size() == 2) {
            arbre.arcs.add(new Arc<>(
                arbre.sommets.get(I.get(0) - 1),
                arbre.sommets.get(I.get(1) - 1)
            ));
        }

        return arbre;
    }


    @Override
    public abstract void afficher();
}
