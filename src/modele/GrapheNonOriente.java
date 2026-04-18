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
		visite[s] = true;
		nbVisites++;

		Sommet<T> sommet = trouverSommet(s);
		if (sommet == null)
			return nbVisites;

		for (Sommet<T> voisin : getVoisins(sommet.getDonnee())) {
			int idVoisin = voisin.getId();
			if (idVoisin < visite.length && !visite[idVoisin]) {
				nbVisites = dfsConnexe(idVoisin, visite, nbVisites);
			}
		}
		return nbVisites;
	}

	private void empiler(int x, int[] pilch) {
		pilch[x] = pilch[0];
		pilch[0] = x;
	}

	public boolean estArbre() {
		int nbsom = this.getOrdre();
		int nbarcs = this.matriceAdjacence[0][1] / 2;
		return estConnexe() && nbarcs == nbsom - 1;

	}

	public boolean estConnexe() {
		if (sommets.isEmpty())
			return true;

		boolean[] visite = new boolean[sommets.size() + 1];
		int nbVisites = 0;

		nbVisites = dfsConnexe(1, visite, nbVisites);

		return nbVisites == sommets.size();
	}

	@Override
	public void ajouterArc(T donnee1, T donnee2) {
		super.ajouterArc(donnee1, donnee2);
		super.ajouterArc(donnee2, donnee1);
	}

	public int[] codagePrufer() throws Exception {
		if (!estArbre()) {
			throw new IllegalStateException("le codage de prufer s'applique seulement aux arbres");
		}
		int nbSommet = this.matriceAdjacence[0][0];
		int[] pr = new int[nbSommet - 1];
		pr[0] = nbSommet - 2;
		
		// on calcule les degrés dans un tableau séparé
	    int[] degre = new int[nbSommet + 1];
	    for(int i = 1; i <= nbSommet; i++) {
	        for(int j = 1; j <= nbSommet; j++) {
	            if(this.matriceAdjacence[i][j] == 1) degre[i]++;
	        }
	    }

	    // on travaille sur une copie de la matrice
	    // pour ne pas modifier l'originale
	    int[][] copie = new int[nbSommet + 1][nbSommet + 1];
	    for(int i = 1; i <= nbSommet; i++)
	        for(int j = 1; j <= nbSommet; j++)
	            copie[i][j] = this.matriceAdjacence[i][j];

		for (int i = 1; i <= nbSommet - 2; i++) {
			int s = 1;

			while (degre[s] != 1)
				s++;
			int j = 1;
			while (copie[s][j] != 1)
				j++;

			pr[i] = j;
			copie[s][j] = 0;
			copie[j][s] = 0;
			degre[s] = 0;
			degre[j]--;
		}

		return pr;

	}

	/**
	 * public GrapheNonOriente<T> decodagePrufer(int [] d){
	 * 
	 * }
	 */

	public void afficher() {
		int nbsom = this.getOrdre();
		System.out.println("Graphe non orienté");
		System.out.println("Sommets : " + nbsom);
		System.out.println("Arcs : " + (this.matriceAdjacence[0][1]));

		for (int i = 1; i <= nbsom; i++) {
			System.out.println("Sommet" + i + "-> voisins : ");
			int it = aps[i];
			int t = fs[it];
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
			int t = fs[it];
			while (t > 0) {
				sb.append(t).append(" ");
				t = fs[++it];
			}
			sb.append("\n");
		}
		return sb.toString();

	}

}
