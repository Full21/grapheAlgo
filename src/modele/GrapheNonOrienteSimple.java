package modele;

import java.util.List;


public class GrapheNonOrienteSimple<T> extends GrapheNonOriente<T> {

    public GrapheNonOrienteSimple() {
        super();
    }


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


    public boolean estArbre() {
        if (sommets.isEmpty()) return false;
        boolean connexe = estConnexe();
        boolean bonNombreAretes = (arcs.size() == sommets.size() - 1);
        return connexe && bonNombreAretes;
    }
}