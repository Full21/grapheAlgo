package modele;

import java.util.List;
import java.util.ArrayList;


public class GrapheOriente<T> extends Graphe<T> {

    protected List<List<T>> composantesCC;

    // tarjanCfc reste attribut car grapheReduit() en a besoin après tarjan()
    private int[] tarjanCfc;

    public GrapheOriente(int nbSommets) {
        super(nbSommets);
        this.composantesCC = new ArrayList<>();
    }


    // =========================================================================
    // afficher — liste d'adjacence
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
    // Traduit depuis le cours (Graphe.pdf page 1)
    // =========================================================================

    private void empilerTarj(int s, int[] tarjanPile, int[] tarjanTop) {
        tarjanPile[++tarjanTop[0]] = s;
    }

    private int depilerTarj(int[] tarjanPile, int[] tarjanTop) {
        return tarjanPile[tarjanTop[0]--];
    }

    /**
     * traverseeTarjan — DFS de Tarjan sur le sommet s.
     *
     * Traduit fidèlement depuis le cours :
     *   p++; num[s]=p; ro[s]=p;
     *   empile(s,tarj); entarj[s]=true;
     *   for (successeurs t de s)
     *     if (num[t]==0) { traversee(t); if (ro[t]<ro[s]) ro[s]=ro[t]; }  // Z1
     *     else { if (num[t]<ro[s] && entarj[t]) ro[s]=num[t]; }           // Z2
     *   if (ro[s]==num[s])
     *   { k++; do { u=depiler; entarj[u]=false; cfc[u]=k; } while(u!=s); }
     */
    private void traverseeTarjan(int s,
                                  int[]     tarjanNum,
                                  int[]     tarjanRo,
                                  int[]     tarjanPile,
                                  boolean[] tarjanEntarj,
                                  int[]     tarjanP,
                                  int[]     tarjanK,
                                  int[]     tarjanTop) {
        tarjanP[0]++;
        tarjanNum[s]    = tarjanP[0];
        tarjanRo[s]     = tarjanP[0];
        empilerTarj(s, tarjanPile, tarjanTop);
        tarjanEntarj[s] = true;

        for (Arc<T> arc : arcs) {
            if (sommets.indexOf(arc.source) == s) {
                int t = sommets.indexOf(arc.destination);
                if (tarjanNum[t] == 0) {
                    traverseeTarjan(t, tarjanNum, tarjanRo, tarjanPile,
                                    tarjanEntarj, tarjanP, tarjanK, tarjanTop);
                    if (tarjanRo[t] < tarjanRo[s]) tarjanRo[s] = tarjanRo[t]; // Z1
                } else {
                    if (tarjanNum[t] < tarjanRo[s] && tarjanEntarj[t])         // Z2
                        tarjanRo[s] = tarjanNum[t];
                }
            }
        }

        // s est racine d'une CFC
        if (tarjanRo[s] == tarjanNum[s]) {
            tarjanK[0]++;
            List<T> cfc = new ArrayList<>();
            int u;
            do {
                u = depilerTarj(tarjanPile, tarjanTop);
                tarjanEntarj[u] = false;
                tarjanCfc[u]    = tarjanK[0];
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
        tarjanCfc = new int[n];

        int[]     tarjanNum    = new int[n];
        int[]     tarjanRo     = new int[n];
        int[]     tarjanPile   = new int[n + 1];
        boolean[] tarjanEntarj = new boolean[n];
        int[]     tarjanP      = {0};
        int[]     tarjanK      = {0};
        int[]     tarjanTop    = {0};

        for (int i = 0; i < n; i++) {
            tarjanNum[i]    = 0;
            tarjanRo[i]     = 0;
            tarjanEntarj[i] = false;
        }

        for (int s = 0; s < n; s++) {
            if (tarjanNum[s] == 0)
                traverseeTarjan(s, tarjanNum, tarjanRo, tarjanPile,
                                tarjanEntarj, tarjanP, tarjanK, tarjanTop);
        }

        return composantesCC;
    }


    // =========================================================================
    // grapheReduit — construit le graphe réduit R(G) à partir des CFC
    // Traduit depuis graph_reduit() du cours (Graphe.pdf page 1)
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
    // Traduit depuis l'algorithme du rang du cours
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

        List<Integer> courant = new ArrayList<>();

        // initialisation
        for (int i = 0; i < n; i++) {
            if (ddi[i] == 0) courant.add(i);
        }

        while (!courant.isEmpty()) {
            List<Integer> suivant = new ArrayList<>();

            for (int i : courant) {
                sommets.get(i).setRang(rang);

                for (Arc<T> arc : arcs) {
                    if (sommets.indexOf(arc.source) == i) {
                        int t = sommets.indexOf(arc.destination);
                        ddi[t]--;
                        if (ddi[t] == 0) {
                            suivant.add(t);
                        }
                    }
                }
            }

            courant = suivant;
            rang++;
        }
    }


    // =========================================================================
    // calculerDistances — BFS depuis chaque sommet
    // Traduit depuis desc_large1() du cours (Graphe.pdf pages 3-4)
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