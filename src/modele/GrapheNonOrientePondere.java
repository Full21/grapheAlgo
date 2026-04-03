package modele;

import java.util.Comparator;

public class GrapheNonOrientePondere<T> extends GrapheNonOriente<T> implements IPondere {

	public GrapheNonOrientePondere(int nbSommets) {
		super(nbSommets);
	}

	private double[][] matricePoids;

	// @Override
	public void afficher() {

	}

	// @Override
	public double getPoids(int sommet1, int sommet2) {
		return this.matricePoids[sommet1][sommet2];
	}

	// @Override
	public void setPoids(int sommet1, int sommet2, double poids) {
		this.matricePoids[sommet1][sommet2] = poids;
	}

	// @Override
	public double[][] getMatricePoids() {
		return this.matricePoids;
	}

	public Graphe<T> kruskal() {
		
		int nbSommets = this.sommets.size();
		int nbArcs = this.arcs.size();
		
		GrapheNonOrientePondere<T> h = new GrapheNonOrientePondere<T>(nbSommets);
		trierArcs();
		int ig = 0, jh = 0;

		int[] prem = new int[nbSommets + 1];
		int[] pilch = new int[nbSommets + 1];
		int[] cfc = new int[nbSommets + 1];
		int[] nbElem = new int[nbSommets + 1];

		for (int i = 1; i <= nbSommets; i++) {
			prem[i] = i;
			cfc[i] = i;
			nbElem[i] = 1;
		}

		while (jh < nbArcs) {
			Arc<T> b = this.arcs.get(ig + 1);
			int x = cfc[b.getSource().getId()];
			int y = cfc[b.getDestination().getId()];

			if (x != y) {
				h.arcs.add(b);
				jh++;
				fusion(x, y, prem, pilch, cfc, nbElem);
			}

		}

		return h;
	}

	private void fusion(int i, int j, int[] prem, int[] pilch, int[] cfc, int[] nbElem) {
		if (nbElem[j] > nbElem[i]) {
			int x = nbElem[j];
			nbElem[j] = nbElem[i];
			nbElem[i] = x;
		}
		int s = prem[j];
		cfc[s] = i;
		while (pilch[s] != 0) {
			s = pilch[s];
			cfc[s] = i;
		}

		pilch[s] = prem[i];
		prem[i] = prem[j];
		nbElem[i] += nbElem[j];

	}
	
	private void trierArcs() {
	    this.arcs.sort(new Comparator<Arc<T>>() {
	        @Override
	        public int compare(Arc<T> a1, Arc<T> a2) {
	            return Double.compare(a1.getPoids(), a2.getPoids());
	        }
	    });
	}
}
