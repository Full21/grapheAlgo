package modele;
import java.util.ArrayList;
import java.util.List;

public class GrapheNonOriente<T> extends Graphe<T> {
    private List<Arc<T>> arbreCouvrant = new ArrayList<Arc<T>>();
    
    public GrapheNonOriente(int nbsommets) {
        super(nbsommets);
        this.estOriente = false;
        this.estPondere = false;
    }
    
    private int dfsConnexe(int s, boolean[] visite, int nbVisites) {
        Sommet<T> sommet = trouverSommet(s);
        if (sommet == null) return nbVisites;
        
        int index = sommets.indexOf(sommet);
        if (index < 0 || visite[index]) return nbVisites;
        
        visite[index] = true;
        nbVisites++;
        
        for (Sommet<T> voisin : getVoisins(sommet.getDonnee())) {
            nbVisites = dfsConnexe(voisin.getId(), visite, nbVisites);
        }
        return nbVisites;
    }
    
    private void empiler(int x, int[] pilch) {
        pilch[x] = pilch[0];
        pilch[0] = x;
    }
    
    public boolean estArbre() {
        int nbsom  = this.getOrdre();
        int nbarcs = this.matriceAdjacence[0][1] / 2;
        boolean connexe = estConnexe();        
        return connexe && nbarcs == nbsom - 1;
    } 
    
    public boolean estConnexe() {
        if (sommets.isEmpty()) return true;
                   
        boolean[] visite  = new boolean[sommets.size()];
        int nbVisites     = dfsConnexe(sommets.get(0).getId(), visite, 0);
                
        return nbVisites == sommets.size();
    }
    
    @Override
    public void ajouterArc(T donnee1, T donnee2) {
        super.ajouterArc(donnee1, donnee2);
        super.ajouterArc(donnee2, donnee1);
    }
    
    public int[] codagePrufer() throws Exception {
        if (!estArbre()) {
            throw new IllegalStateException(
                "le codage de prufer s'applique seulement aux arbres");
        }
        
        int nbSommet = this.matriceAdjacence[0][0];
        int[] pr     = new int[nbSommet - 1];
        pr[0]        = nbSommet - 2;
        
        int[] degre = new int[nbSommet + 1];
        for (int i = 1; i <= nbSommet; i++)
            for (int j = 1; j <= nbSommet; j++)
                if (this.matriceAdjacence[i][j] == 1) degre[i]++;
        
        int[][] copie = new int[nbSommet + 1][nbSommet + 1];
        for (int i = 1; i <= nbSommet; i++)
            for (int j = 1; j <= nbSommet; j++)
                copie[i][j] = this.matriceAdjacence[i][j];
        
        int[] idParIndex = new int[nbSommet + 1];
        for (int i = 0; i < sommets.size(); i++) {
            idParIndex[i + 1] = sommets.get(i).getId();
        }
        
        for (int i = 1; i <= nbSommet - 2; i++) {
            int s = 1;
            while (degre[idParIndex[s]] != 1) s++;
            int idS = idParIndex[s];
            
            int j = 1;
            while (copie[idS][idParIndex[j]] != 1) j++;
            int idJ = idParIndex[j];
            
            pr[i] = j;
            copie[idS][idJ] = 0;
            copie[idJ][idS] = 0;
            degre[idS]      = 0;
            degre[idJ]--;
        }
        
        return pr;
    }
    
    public void afficher() {
        int nbsom = this.getOrdre();
        System.out.println("Graphe non orienté");
        System.out.println("Sommets : " + nbsom);
        System.out.println("Arcs : " + (this.matriceAdjacence[0][1]));
        for (int i = 1; i <= nbsom; i++) {
            System.out.println("Sommet" + i + "-> voisins : ");
            int it = aps[i];
            int t  = fs[it];
            while (t > 0) {
                System.out.print(t + "");
                t = fs[++it];
            }
            System.out.println();
        }
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Graphe non oriente [").append(getOrdre()).append("sommets]\n");
        for (int i = 1; i <= getOrdre(); i++) {
            sb.append(i).append("->");
            int it = aps[i];
            int t  = fs[it];
            while (t > 0) {
                sb.append(t).append(" ");
                t = fs[++it];
            }
            sb.append("\n");
        }
        return sb.toString();
    }
      
    
}