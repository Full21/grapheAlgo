package modele;

import java.util.List;
import java.util.ArrayList;


public class GrapheOriente<T> extends Graphe<T> {

    protected List<Sommet<T>> sommets;
    protected List<Arc<T>>    arcs;
    protected List<List<T>>   composantesCC; // pour Tarjan

    public GrapheOriente(int nbSommets) {
        super(nbSommets);
        this.sommets       = new ArrayList<>();
        this.arcs          = new ArrayList<>();
        this.composantesCC = new ArrayList<>();
    }


    // =========================================================================
    // afficher — liste d'adjacence (successeurs de chaque sommet)
    // =========================================================================
    @Override
    public void afficher() {
        for (Sommet<T> s : sommets) {
            System.out.print(s.getDonnee() + " -> ");
            for (Arc<T> arc : arcs) {
                if (arc.source.equals(s)) {
                    System.out.print(arc.destination.getDonnee() + " ");
                }
            }
            System.out.println();
        }
    }


    // =========================================================================
    // toString
    // =========================================================================
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Sommets : ").append(sommets.size());
        sb.append(", Arcs : ").append(arcs.size());
        return sb.toString();
    }


    // =========================================================================
    // ALGORITHME DE TARJAN — Composantes Fortement Connexes
    //
    // Traduit exactement depuis le cours (Graphe.pdf page 1) :
    //   void traversee(int s)      — DFS récursif avec numérotation et ro[]
    //   void fortconnexe(...)      — appel global et initialisation
    //
    // Structures :
    //   num[]    : numéro d'ordre DFS (0 = non visité)
    //   ro[]     : plus petit num[] atteignable depuis s
    //   tarjPile : pile de Tarjan
    //   entarj[] : booléen — sommet dans la pile
    //   tarjanP  : compteur DFS
    //   tarjanK  : compteur CFC
    //   tarjanCfc[]: numéro de CFC de chaque sommet
    // =========================================================================

    private int[]     tarjanNum;
    private int[]     tarjanRo;
    private int[]     tarjanPile;
    private boolean[] tarjanEntarj;
    private int[]     tarjanCfc;
    private int       tarjanP;
    private int       tarjanK;
    private int       tarjanTop;

    private void empilerTarj(int s) {
        tarjanPile[++tarjanTop] = s;
    }

    private int depilerTarj() {
        return tarjanPile[tarjanTop--];
    }

    /**
     * traverseeTarjan — DFS de Tarjan sur le sommet s (0-indexé).
     *
     * Traduit fidèlement depuis le cours :
     *   p++; num[s]=p; ro[s]=p;
     *   empile(s,tarj); entarj[s]=true;
     *   for (successeurs t de s)
     *     if (num[t]==0) { traversee(t); if (ro[t]<ro[s]) ro[s]=ro[t]; }  // Z1
     *     else { if (num[t]<ro[s] && entarj[t]) ro[s]=num[t]; }           // Z2
     *   if (ro[s]==num[s])   // s est racine d'une CFC
     *   { k++; do { u=depiler; entarj[u]=false; cfc[u]=k; } while(u!=s); }
     */
    private void traverseeTarjan(int s) {
        tarjanP++;
        tarjanNum[s]    = tarjanP;
        tarjanRo[s]     = tarjanP;
        empilerTarj(s);
        tarjanEntarj[s] = true;

        for (Arc<T> arc : arcs) {
            if (sommets.indexOf(arc.source) == s) {
                int t = sommets.indexOf(arc.destination);
                if (tarjanNum[t] == 0) {
                    traverseeTarjan(t);
                    if (tarjanRo[t] < tarjanRo[s]) tarjanRo[s] = tarjanRo[t]; // Z1
                } else {
                    if (tarjanNum[t] < tarjanRo[s] && tarjanEntarj[t])         // Z2
                        tarjanRo[s] = tarjanNum[t];
                }
            }
        }

        // s est racine d'une CFC
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
     * tarjan — lance l'algorithme sur tout le graphe.
     * Traduit de fortconnexe() du cours.
     */
    public List<List<T>> tarjan() {
        int n = sommets.size();
        composantesCC.clear();

        tarjanNum    = new int[n];
        tarjanRo     = new int[n];
        tarjanPile   = new int[n + 1];
        tarjanEntarj = new boolean[n];
        tarjanCfc    = new int[n];
        tarjanP      = 0;
        tarjanK      = 0;
        tarjanTop    = 0;

        for (int i = 0; i < n; i++) {
            tarjanNum[i]    = 0;
            tarjanRo[i]     = 0;
            tarjanEntarj[i] = false;
        }

        for (int s = 0; s < n; s++) {
            if (tarjanNum[s] == 0) traverseeTarjan(s);
        }

        return composantesCC;
    }


    // =========================================================================
    // grapheReduit — construit le graphe réduit R(G) à partir des CFC
    //
    // Traduit depuis graph_reduit() du cours (Graphe.pdf page 1) :
    //   Sommets du réduit = les CFC
    //   Arc (A,B) ssi arc (s,t) dans G avec s∈A, t∈B et A≠B
    // =========================================================================
    public GrapheOriente<T> grapheReduit() {
        if (composantesCC.isEmpty()) tarjan();

        int nbc = composantesCC.size();
        GrapheOriente<Integer> gr = new GrapheOriente<>(nbc);

        for (int i = 1; i <= nbc; i++) {
            gr.sommets.add(new Sommet<>(i));
        }

        boolean[][] dejaAjoute = new boolean[nbc + 1][nbc + 1];

        for (Arc<T> arc : arcs) {
            int idxS = sommets.indexOf(arc.source);
            int idxT = sommets.indexOf(arc.destination);
            int cfcS = tarjanCfc[idxS];
            int cfcT = tarjanCfc[idxT];

            if (cfcS != cfcT && !dejaAjoute[cfcS][cfcT]) {
                dejaAjoute[cfcS][cfcT] = true;
                gr.arcs.add(new Arc<>(
                    gr.sommets.get(cfcS - 1),
                    gr.sommets.get(cfcT - 1)
                ));
            }
        }

        @SuppressWarnings("unchecked")
        GrapheOriente<T> resultat = (GrapheOriente<T>) gr;
        return resultat;
    }


    // =========================================================================
    // calculerRangs — via demi-degrés intérieurs
    //
    // Traduit depuis l'algorithme du rang du cours :
    //   ddi[s] = nombre de prédécesseurs
    //   Rang 0 : ddi[s] == 0 → éliminer, décrémenter successeurs, rang++
    // =========================================================================
    public void calculerRangs() {
        int n = sommets.size();
        int[] ddi = new int[n];

        for (Arc<T> arc : arcs) {
            int t = sommets.indexOf(arc.destination);
            ddi[t]++;
        }

        for (Sommet<T> s : sommets) s.setRang(-1);

        int rang = 0;
        boolean progression = true;

        while (progression) {
            progression = false;
            for (int i = 0; i < n; i++) {
                if (ddi[i] == 0 && sommets.get(i).getRang() == -1) {
                    sommets.get(i).setRang(rang);
                    ddi[i] = -1;
                    progression = true;
                    for (Arc<T> arc : arcs) {
                        if (sommets.indexOf(arc.source) == i) {
                            int t = sommets.indexOf(arc.destination);
                            if (ddi[t] > 0) ddi[t]--;
                        }
                    }
                }
            }
            rang++;
        }
    }


    // =========================================================================
    // calculerDistances — BFS depuis chaque sommet
    //
    // Traduit depuis desc_large1() du cours (Graphe.pdf pages 3-4) :
    //   dist[r]=0 ; autres = -1 ; BFS niveau par niveau
    // =========================================================================
    public int[][] calculerDistances() {
        int n = sommets.size();
        int[][] dist = new int[n][n];

        for (int r = 0; r < n; r++) {
            int[] d    = new int[n];
            int[] file = new int[n];
            for (int h = 0; h < n; h++) d[h] = -1;
            d[r] = 0;

            int debut = 0, fin = 0;
            file[fin++] = r;

            while (debut < fin) {
                int s = file[debut++];
                for (Arc<T> arc : arcs) {
                    if (sommets.indexOf(arc.source) == s) {
                        int t = sommets.indexOf(arc.destination);
                        if (d[t] == -1) {
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
    // getBases — sommets sans prédécesseur (ddi == 0)
    //
    // Traduit depuis base_Greduit() du cours (Graphe.pdf page 1)
    // =========================================================================
    public List<T> getBases() {
        List<T> bases = new ArrayList<>();
        int n = sommets.size();
        int[] ddi = new int[n];

        for (Arc<T> arc : arcs) {
            int t = sommets.indexOf(arc.destination);
            ddi[t]++;
        }

        for (int i = 0; i < n; i++) {
            if (ddi[i] == 0) bases.add(sommets.get(i).getDonnee());
        }

        return bases;
    }
}