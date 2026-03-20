package modele;

import java.util.List;
import java.util.ArrayList;


public abstract class GrapheOriente<T> extends Graphe<T> {

    protected List<Sommet<T>> sommets;
    protected List<Arc<T>>    arcs;
    protected List<List<T>>   composantesCC; // pour Tarjan

    public GrapheOriente() {
        this.sommets       = new ArrayList<>();
        this.arcs          = new ArrayList<>();
        this.composantesCC = new ArrayList<>();
    }


    public List<List<T>> tarjan() {
        // TODO : implémenter l'algorithme de Tarjan
        return composantesCC;
    }

    public GrapheOriente<T> grapheReduit() {
        // TODO : construire le graphe réduit à partir des CFC
        return null;
    }

    public void calculerRangs() {
        // TODO : affecter rang à chaque sommet (0 = aucun prédécesseur)
    }


    public int[][] calculerDistances() {
        int n = sommets.size();
        int[][] dist = new int[n][n];
        // TODO : remplir le tableau des distances
        return dist;
    }


    public List<T> getBases() {
        List<T> bases = new ArrayList<>();
        // TODO : identifier les sommets de rang 0
        return bases;
    }


    @Override
    public abstract void afficher();
}