package modele;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class GrapheNonOrienteSimple<T> extends GrapheNonOriente<T> {

    public GrapheNonOrienteSimple() {
        super();
    }


    // -------------------------------------------------------------------------
    // afficher — liste d'adjacence
    // -------------------------------------------------------------------------
    @Override
    public void afficher() {
        for (Sommet<T> s : sommets) {
            System.out.print(s.getDonnee() + " -> ");
            List<T> voisins = getVoisins(s.getDonnee());
            for (T v : voisins) {
                System.out.print(v + " ");
            }
            System.out.println();
        }
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Sommets : ");
        sb.append(sommets.size());
        sb.append(", Aretes : ");
        sb.append(arcs.size());
        return sb.toString();
    }


    // -------------------------------------------------------------------------
    // estArbre — connexe ET n-1 arêtes
    // -------------------------------------------------------------------------
    public boolean estArbre() {
        if (sommets.isEmpty()) return false;
        boolean connexe        = estConnexe();
        boolean bonNombreAretes = (arcs.size() == sommets.size() - 1);
        return connexe && bonNombreAretes;
    }


    // =========================================================================
    // ALGORITHME DE KRUSKAL
    // Source : cours du prof (Graphe.pdf, page 12)
    //
    // Idée : trier les arêtes par poids croissant (règle de départage sur les
    // extrémités), puis ajouter chaque arête si elle ne crée pas de cycle.
    // La détection de cycle repose sur les tableaux prem / pilch / cfc
    // (composantes connexes par liste chaînée) traduits fidèlement du cours.
    //
    // Initialement : cfc[s] = s, prem[s] = s, pilch[s] = 0  pour tout s.
    // =========================================================================

    /**
     * Trie les arêtes du graphe par poids croissant.
     * À poids égal : on favorise l'arête avec la plus petite extrémité min,
     * puis en cas d'égalité la plus petite extrémité max.
     * (règle du cours : "favoriser celle qui a une des extrémités la plus petite")
     */
    private void trierArcs() {
        Collections.sort(arcs, new Comparator<Arc<T>>() {
            @Override
            public int compare(Arc<T> a1, Arc<T> a2) {
                double diff = a1.getPoids() - a2.getPoids();
                if (diff != 0) return diff < 0 ? -1 : 1;

                // À poids égal : comparer les indices des sommets
                int u1 = Math.min(sommets.indexOf(a1.getDepart()), sommets.indexOf(a1.getArrivee()));
                int u2 = Math.min(sommets.indexOf(a2.getDepart()), sommets.indexOf(a2.getArrivee()));
                if (u1 != u2) return u1 - u2;

                int v1 = Math.max(sommets.indexOf(a1.getDepart()), sommets.indexOf(a1.getArrivee()));
                int v2 = Math.max(sommets.indexOf(a2.getDepart()), sommets.indexOf(a2.getArrivee()));
                return v1 - v2;
            }
        });
    }

    /**
     * fusionner — fusionne les composantes i et j en une seule portant le
     * numéro le plus petit des deux.
     *
     * Traduit exactement depuis le cours (Graphe.pdf page 5) :
     *
     *   void fusionner(int i, int j, int *prem, int *pilch, int *cfc)
     *   {
     *       if (j < i) { int aux=i; i=j; j=aux; }
     *       int s = prem[j];
     *       cfc[s] = i;
     *       while (pilch[s] != 0) { s = pilch[s]; cfc[s] = i; }
     *       pilch[s] = prem[i];
     *       prem[i]  = prem[j];
     *   }
     *
     * @param i     numéro de la 1ère composante  (1-indexé)
     * @param j     numéro de la 2ème composante  (1-indexé)
     * @param prem  premier sommet de chaque composante
     * @param pilch prochain sommet dans la liste chaînée
     * @param cfc   composante de chaque sommet
     */
    private void fusionner(int i, int j, int[] prem, int[] pilch, int[] cfc) {
        // On veut garder le plus petit numéro comme identifiant de composante
        if (j < i) {
            int aux = i;
            i = j;
            j = aux;
        }
        // Parcourir la liste de la composante j et lui affecter la composante i
        int s = prem[j];
        cfc[s] = i;
        while (pilch[s] != 0) {
            s = pilch[s];
            cfc[s] = i;
        }
        // Chaîner la liste de j à la tête de la liste de i
        pilch[s] = prem[i];
        prem[i]  = prem[j];
    }

    /**
     * kruskal — construit l'arbre recouvrant minimal et le stocke dans
     * arbreCouvrant (liste des arêtes sélectionnées).
     *
     * Traduit du cours (Graphe.pdf page 12) :
     *
     *   Initialement : cfc[s]=s, prem[s]=s, pilch[s]=0
     *   Pour chaque arête (s,t) dans l'ordre trié :
     *     Si cfc[s] != cfc[t] :
     *       Ajouter (s,t) à t
     *       fusionner(cfc[s], cfc[t], prem, pilch, cfc)
     *     Si n-1 arêtes ajoutées : stopper
     */
    public void kruskal() {
        arbreCouvrant.clear();

        int n = sommets.size();
        // Tableaux 1-indexés (indice 0 inutilisé, comme dans le cours C++)
        int[] prem  = new int[n + 1];
        int[] pilch = new int[n + 1];
        int[] cfc   = new int[n + 1];

        // Initialisation : chaque sommet est sa propre composante
        for (int s = 1; s <= n; s++) {
            cfc[s]   = s;
            prem[s]  = s;
            pilch[s] = 0;
        }

        // Trier les arêtes
        trierArcs();

        int nbAretesAjoutees = 0;

        for (Arc<T> arc : arcs) {
            if (nbAretesAjoutees == n - 1) break;

            // Indices 1-indexés des deux extrémités
            int idxS = sommets.indexOf(arc.getDepart())   + 1;
            int idxT = sommets.indexOf(arc.getArrivee())  + 1;

            // L'arête est acceptée si les deux sommets sont dans des composantes différentes
            if (cfc[idxS] != cfc[idxT]) {
                arbreCouvrant.add(arc);
                fusionner(cfc[idxS], cfc[idxT], prem, pilch, cfc);
                nbAretesAjoutees++;
            }
        }
    }


    // -------------------------------------------------------------------------
    // afficherArbreCouvrant — affiche l'ACM calculé par kruskal()
    // -------------------------------------------------------------------------
    public void afficherArbreCouvrant() {
        System.out.println("Arbre recouvrant minimal (Kruskal) :");
        double poidsTotal = 0;
        for (Arc<T> arc : arbreCouvrant) {
            System.out.printf("  %s -- %s  [%.2f]%n",
                arc.getDepart().getDonnee(),
                arc.getArrivee().getDonnee(),
                arc.getPoids());
            poidsTotal += arc.getPoids();
        }
        System.out.printf("Poids total : %.2f%n", poidsTotal);
    }
}
