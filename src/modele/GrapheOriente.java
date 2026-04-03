package modele;

import java.util.List;
import java.util.ArrayList;
import java.util.Stack;


public abstract class GrapheOriente<T> extends Graphe<T> {

    protected List<Sommet<T>> sommets;
    protected List<Arc<T>>    arcs;
    protected List<List<T>>   composantesCC; // pour Tarjan

    public GrapheOriente() {
        this.sommets       = new ArrayList<>();
        this.arcs          = new ArrayList<>();
        this.composantesCC = new ArrayList<>();
    }


    // =========================================================================
    // ALGORITHME DE TARJAN — Composantes Fortement Connexes
    //
    // Traduit exactement depuis le cours (Graphe.pdf page 1) :
    //
    //   void traversee(int s)        — DFS récursif avec numérotation et ro[]
    //   void fortconnexe(...)        — appel global et initialisation
    //
    // Structures utilisées (comme dans le cours) :
    //   num[]   : numéro d'ordre DFS de chaque sommet (0 = non visité)
    //   ro[]    : plus petit num[] atteignable depuis s via la DFS + frondes
    //   tarj[]  : pile de Tarjan (indices dans cette pile)
    //   entarj[]: booléen — sommet dans la pile Tarjan
    //   p       : compteur de numérotation DFS global
    //   k       : compteur de CFC (identifiant croissant)
    //   cfc[]   : numéro de CFC de chaque sommet (résultat)
    // =========================================================================

    // Variables d'état Tarjan (partagées entre traversee et fortconnexe)
    private int[]    tarjanNum;
    private int[]    tarjanRo;
    private int[]    tarjanPile;   // pile implémentée par tableau (comme le cours)
    private boolean[]tarjanEntarj;
    private int[]    tarjanCfc;
    private int      tarjanP;     // compteur DFS
    private int      tarjanK;     // compteur CFC
    private int      tarjanTop;   // sommet de pile (index dans tarjanPile)

    // Pile Tarjan — empiler / dépiler (comme empiler/depiler du cours)
    private void empilerTarj(int s) {
        tarjanTop++;
        tarjanPile[tarjanTop] = s;
    }
    private int depilerTarj() {
        int val = tarjanPile[tarjanTop];
        tarjanTop--;
        return val;
    }

    /**
     * traversee — DFS de Tarjan sur le sommet s (0-indexé).
     *
     * Traduit fidèlement depuis le cours :
     *   p++; num[s]=p; ro[s]=p;
     *   empile(s,tarj); entarj[s]=true;
     *   for (successeurs t de s dans fs)
     *     if (num[t]==0) { pred[t]=s; traversee(t);
     *                      if (ro[t]<ro[s]) ro[s]=ro[t]; }  // Z1
     *     else { if (num[t]<ro[s] && entarj[t]) ro[s]=num[t]; } // Z2
     *   if (ro[s]==num[s])  // s est racine d'une CFC
     *   { k++; do { u=depiler(tarj); entarj[u]=false;
     *                cfc[u]=k; } while (u!=s);
     *     prem[k]=pilch[0]; pilch[0]=0; }
     */
    private void traverseeTarjan(int s) {
        tarjanP++;
        tarjanNum[s] = tarjanP;
        tarjanRo[s]  = tarjanP;
        empilerTarj(s);
        tarjanEntarj[s] = true;

        // Parcourir tous les successeurs de s
        for (Arc<T> arc : arcs) {
            if (sommets.indexOf(arc.getDepart()) == s) {
                int t = sommets.indexOf(arc.getArrivee());
                if (tarjanNum[t] == 0) {
                    // t pas encore visité
                    traverseeTarjan(t);
                    if (tarjanRo[t] < tarjanRo[s]) tarjanRo[s] = tarjanRo[t]; // Z1
                } else {
                    // t déjà visité
                    if (tarjanNum[t] < tarjanRo[s] && tarjanEntarj[t])         // Z2
                        tarjanRo[s] = tarjanNum[t];
                }
            }
        }

        // s est racine d'une CFC si ro[s] == num[s]
        if (tarjanRo[s] == tarjanNum[s]) {
            tarjanK++;
            List<T> cfc = new ArrayList<>();
            int u;
            do {
                u = depilerTarj();
                tarjanEntarj[u] = false;
                tarjanCfc[u]    = tarjanK;
                cfc.add(sommets.get(u).getDonnee());
            } while (u != s);
            composantesCC.add(cfc);
        }
    }

    /**
     * tarjan — lance l'algorithme de Tarjan sur tout le graphe.
     *
     * Traduit de fortconnexe() du cours :
     *   Initialiser num[], ro[], entarj[], tarj[]
     *   Pour tout sommet s non numéroté : traversee(s)
     */
    @Override
    public List<List<T>> tarjan() {
        int n = sommets.size();
        composantesCC.clear();

        tarjanNum    = new int[n];
        tarjanRo     = new int[n];
        tarjanPile   = new int[n + 1]; // 1-indexé, tarjanPile[0] = taille
        tarjanEntarj = new boolean[n];
        tarjanCfc    = new int[n];
        tarjanP      = 0;
        tarjanK      = 0;
        tarjanTop    = 0;  // pile vide

        // Initialisation
        for (int i = 0; i < n; i++) {
            tarjanNum[i]    = 0;
            tarjanRo[i]     = 0;
            tarjanEntarj[i] = false;
        }

        // Lancer la DFS depuis tous les sommets non encore numérotés
        for (int s = 0; s < n; s++) {
            if (tarjanNum[s] == 0) traverseeTarjan(s);
        }

        return composantesCC;
    }


    // =========================================================================
    // grapheReduit — construit le graphe réduit R(G) à partir des CFC
    //
    // Traduit depuis graph_reduit() du cours (Graphe.pdf page 1) :
    //   Les sommets du graphe réduit = les CFC
    //   Un arc (A,B) existe ssi il existe un arc (s,t) dans G avec s∈A et t∈B
    //   (on ignore les boucles : A≠B)
    // =========================================================================
    public GrapheOriente<T> grapheReduit() {
        // S'assurer que Tarjan a été exécuté
        if (composantesCC.isEmpty()) tarjan();

        int nbc = composantesCC.size(); // nombre de CFC = nombre de sommets du graphe réduit

        // Créer un nouveau graphe orienté concret (GrapheOrienteSimple si disponible)
        // On retourne une instance concrète — ici implémentée comme classe interne anonyme
        // pour respecter l'abstraction.
        // En pratique l'étudiant utilisera GrapheOrienteSimple<Integer>.
        GrapheOriente<Integer> gr = new GrapheOriente<Integer>() {
            @Override public void afficher() {
                for (Sommet<Integer> s : sommets) {
                    System.out.print("CFC" + s.getDonnee() + " -> ");
                    for (Arc<Integer> a : arcs) {
                        if (a.getDepart().equals(s))
                            System.out.print("CFC" + a.getArrivee().getDonnee() + " ");
                    }
                    System.out.println();
                }
            }
        };

        // Créer les sommets du graphe réduit (numérotés 1..nbc)
        for (int i = 1; i <= nbc; i++) {
            gr.sommets.add(new Sommet<>(i));
        }

        // Tableau deja_mis pour éviter les arcs en double
        boolean[][] dejaAjoute = new boolean[nbc + 1][nbc + 1];

        // Pour chaque arc (s,t) du graphe initial
        for (Arc<T> arc : arcs) {
            int idxS = sommets.indexOf(arc.getDepart());
            int idxT = sommets.indexOf(arc.getArrivee());
            int cfcS = tarjanCfc[idxS]; // numéro de CFC de s
            int cfcT = tarjanCfc[idxT]; // numéro de CFC de t

            // Ignorer les boucles (même CFC) et les doublons
            if (cfcS != cfcT && !dejaAjoute[cfcS][cfcT]) {
                dejaAjoute[cfcS][cfcT] = true;
                Sommet<Integer> sReduit = gr.sommets.get(cfcS - 1);
                Sommet<Integer> tReduit = gr.sommets.get(cfcT - 1);
                gr.arcs.add(new Arc<>(sReduit, tReduit));
            }
        }

        @SuppressWarnings("unchecked")
        GrapheOriente<T> resultat = (GrapheOriente<T>) gr;
        return resultat;
    }


    // =========================================================================
    // calculerRangs — rang d'un sommet = longueur du plus long chemin entrant
    //
    // Traduit depuis le cours (Cours_Graphe_Algo.pdf, algorithme du rang) :
    //   Y = S  (tous les sommets)
    //   R = Y \ Γ(Y)  (sommets de rang 0 = sans prédécesseur)
    //   Marquer R avec rang 0 ; k=0
    //   Tant que R ≠ ∅ :
    //     Y = Y \ R ; k++ ; R = Y \ Γ(Y dans Y)
    //     Marquer R avec rang k
    //
    // Implémentation via demi-degrés intérieurs (ddi) :
    //   ddi[s] = nombre de prédécesseurs de s
    //   Rang 0 : ddi[s] == 0
    //   Éliminer ces sommets, décrémenter ddi de leurs successeurs
    // =========================================================================
    public void calculerRangs() {
        int n = sommets.size();
        int[] ddi = new int[n]; // demi-degré intérieur

        // Calculer ddi pour chaque sommet
        for (Arc<T> arc : arcs) {
            int t = sommets.indexOf(arc.getArrivee());
            ddi[t]++;
        }

        // Initialiser tous les rangs à -1 (non traités)
        for (Sommet<T> s : sommets) s.setRang(-1);

        int rang = 0;
        boolean progression = true;

        while (progression) {
            progression = false;
            // Trouver les sommets de ddi == 0 pas encore traités
            for (int i = 0; i < n; i++) {
                if (ddi[i] == 0 && sommets.get(i).getRang() == -1) {
                    sommets.get(i).setRang(rang);
                    ddi[i] = -1; // marqué comme traité
                    progression = true;

                    // Décrémenter les successeurs
                    for (Arc<T> arc : arcs) {
                        if (sommets.indexOf(arc.getDepart()) == i) {
                            int t = sommets.indexOf(arc.getArrivee());
                            if (ddi[t] > 0) ddi[t]--;
                        }
                    }
                }
            }
            rang++;
        }

        // Les sommets encore à -1 font partie d'un circuit → rang infini
        // (le graphe doit être sans circuit pour que l'algorithme soit valide)
    }


    // =========================================================================
    // calculerDistances — BFS depuis chaque sommet pour obtenir la matrice dist
    //
    // Traduit depuis desc_large1() du cours (Graphe.pdf pages 3 et 4) :
    //   dist[r] = 0 ; tous les autres = -1 (inaccessible)
    //   BFS par blocs ; dist[t] = dist[s] + 1 à chaque découverte
    // =========================================================================
    public int[][] calculerDistances() {
        int n = sommets.size();
        int[][] dist = new int[n][n];

        for (int r = 0; r < n; r++) {
            // Initialisation depuis la source r
            int[] d    = new int[n];
            int[] file = new int[n];
            for (int h = 0; h < n; h++) d[h] = -1;
            d[r] = 0;

            int debut = 0, fin = 0;
            file[fin++] = r;

            while (debut < fin) {
                int s = file[debut++];
                // Parcourir les successeurs de s
                for (Arc<T> arc : arcs) {
                    if (sommets.indexOf(arc.getDepart()) == s) {
                        int t = sommets.indexOf(arc.getArrivee());
                        if (d[t] == -1) {       // t pas encore marqué
                            d[t] = d[s] + 1;
                            file[fin++] = t;
                        }
                    }
                }
            }

            dist[r] = d;
        }

        return dist;
    }


    // =========================================================================
    // getBases — retourne les sommets de rang 0 (aucun prédécesseur)
    //
    // Traduit depuis base_Greduit() du cours (Graphe.pdf page 1) :
    //   ddir[c] = 0 pour tout c → c est une base
    // =========================================================================
    public List<T> getBases() {
        List<T> bases = new ArrayList<>();

        // Calculer les demi-degrés intérieurs
        int n = sommets.size();
        int[] ddi = new int[n];
        for (Arc<T> arc : arcs) {
            int t = sommets.indexOf(arc.getArrivee());
            ddi[t]++;
        }

        // Collecter les sommets sans prédécesseur
        for (int i = 0; i < n; i++) {
            if (ddi[i] == 0) {
                bases.add(sommets.get(i).getDonnee());
            }
        }

        return bases;
    }


    @Override
    public abstract void afficher();
}
