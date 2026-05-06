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
		this.estOriente = true;
	}

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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Sommets : ").append(sommets.size());
		sb.append(", Arcs : ").append(arcs.size());
		return sb.toString();
	}

	private void empilerTarj(int s, int[] tarjanPile, int[] tarjanTop) {
		tarjanPile[++tarjanTop[0]] = s;
	}

	private int depilerTarj(int[] tarjanPile, int[] tarjanTop) {
		return tarjanPile[tarjanTop[0]--];
	}

	private void traverseeTarjan(int s, int[] tarjanNum, int[] tarjanRo, int[] tarjanPile, boolean[] tarjanEntarj,
			int[] tarjanP, int[] tarjanK, int[] tarjanTop) {
		tarjanP[0]++;
		tarjanNum[s] = tarjanP[0];
		tarjanRo[s] = tarjanP[0];
		empilerTarj(s, tarjanPile, tarjanTop);
		tarjanEntarj[s] = true;

		for (Arc<T> arc : arcs) {
			if (sommets.indexOf(arc.source) == s) {
				int t = sommets.indexOf(arc.destination);
				if (tarjanNum[t] == 0) {
					traverseeTarjan(t, tarjanNum, tarjanRo, tarjanPile, tarjanEntarj, tarjanP, tarjanK, tarjanTop);
					if (tarjanRo[t] < tarjanRo[s])
						tarjanRo[s] = tarjanRo[t];
				} else {
					if (tarjanNum[t] < tarjanRo[s] && tarjanEntarj[t])
						tarjanRo[s] = tarjanNum[t];
				}
			}
		}

		if (tarjanRo[s] == tarjanNum[s]) {
			tarjanK[0]++;
			List<T> cfc = new ArrayList<>();
			int u;
			do {
				u = depilerTarj(tarjanPile, tarjanTop);
				tarjanEntarj[u] = false;
				tarjanCfc[u] = tarjanK[0];
				cfc.add(sommets.get(u).getDonnee());
			} while (u != s);
			composantesCC.add(cfc);
		}
	}

	public List<List<T>> tarjan() {
		int n = sommets.size();
		composantesCC.clear();
		tarjanCfc = new int[n];

		int[] tarjanNum = new int[n];
		int[] tarjanRo = new int[n];
		int[] tarjanPile = new int[n + 1];
		boolean[] tarjanEntarj = new boolean[n];
		int[] tarjanP = { 0 };
		int[] tarjanK = { 0 };
		int[] tarjanTop = { 0 };

		for (int s = 0; s < n; s++) {
			if (tarjanNum[s] == 0)
				traverseeTarjan(s, tarjanNum, tarjanRo, tarjanPile, tarjanEntarj, tarjanP, tarjanK, tarjanTop);
		}

		return composantesCC;
	}

	public GrapheOriente<Integer> grapheReduit() {
		if (composantesCC.isEmpty())
			tarjan();

		int nbc = composantesCC.size();

		// Créer les sommets du graphe réduit avec des données Integer (1..nbc)
		// en utilisant ajouterSommet pour éviter les problèmes d'ID
		GrapheOriente<Integer> gr = new GrapheOriente<>(nbc);
		for (int i = 1; i <= nbc; i++) {
			gr.ajouterSommet(i);
		}

		boolean[][] dejaAjoute = new boolean[nbc + 1][nbc + 1];

		for (Arc<T> arc : arcs) {
			int idxS = sommets.indexOf(arc.source);
			int idxT = sommets.indexOf(arc.destination);
			int cfcS = tarjanCfc[idxS];
			int cfcT = tarjanCfc[idxT];

			if (cfcS != cfcT && !dejaAjoute[cfcS][cfcT]) {
				dejaAjoute[cfcS][cfcT] = true;
				// Trouver les sommets par leur donnée plutôt que par index
				Sommet<Integer> src = gr.trouverSommetParDonnee(cfcS);
				Sommet<Integer> dest = gr.trouverSommetParDonnee(cfcT);
				if (src != null && dest != null) {
					gr.arcs.add(new Arc<>(src, dest));
				}
			}
		}

		return gr;
	}

	public Sommet<T> trouverSommetParDonnee(T donnee) {
		for (Sommet<T> s : sommets) {
			if (s.getDonnee().equals(donnee))
				return s;
		}
		return null;
	}

	public void calculerRangs() {
		int n = sommets.size();
		int[] ddi = new int[n];

		for (Arc<T> arc : arcs) {
			int t = sommets.indexOf(arc.destination);
			ddi[t]++;
		}

		for (Sommet<T> s : sommets)
			s.setRang(-1);

		int rang = 0;
		List<Integer> courant = new ArrayList<>();

		for (int i = 0; i < n; i++) {
			if (ddi[i] == 0)
				courant.add(i);
		}

		while (!courant.isEmpty()) {
			List<Integer> suivant = new ArrayList<>();
			for (int i : courant) {
				sommets.get(i).setRang(rang);
				for (Arc<T> arc : arcs) {
					if (sommets.indexOf(arc.source) == i) {
						int t = sommets.indexOf(arc.destination);
						ddi[t]--;
						if (ddi[t] == 0)
							suivant.add(t);
					}
				}
			}
			courant = suivant;
			rang++;
		}
	}

	public int[][] calculerDistances() {
		int n = sommets.size();
		int[][] dist = new int[n][n];

		for (int r = 0; r < n; r++) {
			int[] d = new int[n];
			int[] file = new int[n];
			for (int h = 0; h < n; h++)
				d[h] = -1;
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

	public List<List<T>> getBases() {
		if (composantesCC.isEmpty())
			tarjan();

		int nbc = composantesCC.size();
		int[] ddi = new int[nbc + 1]; // indexé par numéro de CFC (1..nbc)

		for (Arc<T> arc : arcs) {
			int idxS = sommets.indexOf(arc.source);
			int idxT = sommets.indexOf(arc.destination);
			int cfcS = tarjanCfc[idxS];
			int cfcT = tarjanCfc[idxT];
			if (cfcS != cfcT) {
				ddi[cfcT]++;
			}
		}

		List<List<T>> bases = new ArrayList<>();
		for (int k = 1; k <= nbc; k++) {
			if (ddi[k] == 0) {
				bases.add(composantesCC.get(k - 1));
			}
		}
		return bases;
	}
}