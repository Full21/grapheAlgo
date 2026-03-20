package modele;

import java.util.List;
import java.util.ArrayList;


public abstract class GrapheNonOriente<T> extends Graphe<T> {

    protected List<Sommet<T>> sommets;
    protected List<Arc<T>>    arcs;
    protected List<Arc<T>>    arbreCouvrant;

    public GrapheNonOriente() {
        this.sommets       = new ArrayList<>();
        this.arcs          = new ArrayList<>();
        this.arbreCouvrant = new ArrayList<>();
    }


    public boolean estConnexe() {
        // TODO : BFS/DFS depuis un sommet ; vérifier que tous sont atteints
        return false;
    }


    public void calculerRangs() {
        // TODO : affecter rang (degré) à chaque sommet
    }

    public int[] codagePrufer() {
        // TODO : produire la séquence de Prüfer de l'arbre
        return new int[0];
    }


    public GrapheNonOriente<T> decodagePrufer(int[] sequence) {
        // TODO : reconstruire le graphe à partir de la séquence
        return null;
    }


    @Override
    public abstract void afficher();
}