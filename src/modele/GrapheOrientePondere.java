package modele;

import java.util.List;
import java.util.ArrayList;
/*un graphe orienté pondéree est un graphe avec des sommets , arcs et ds 
 *flèches des deux côtés, ainsi qu'un certain poids sur un arc entre 2 sommets
 */

public class GrapheOrientePondere<T> extends GrapheOriente<T> implements IPondere {
	private double[][] matricePoids;

	private void initialiserMatrice(int n) {
		matricePoids = new double[n+1][n+1];
		for(int i = 1 ; i <= n ; i++) {
			for(int j = 1 ; j <= n ; j++) {
				if( i == j) {
					matricePoids[i][j] = 0; // la distance d'un sommet à lui même				}
				}else {
					matricePoids[i][j] = Double.POSITIVE_INFINITY;// initialiser tous les poids à infini
				}


			}

	private void redimensionnerMatrice() {
				int n = getOrdre(); // si on ajoute des sommets il faut redimensionner la matrice donc avoir nouveau nombre de sommets  
				double[][] ancienne = matricePoids;

				initialiserMatrice(n);

				for(int i = 1 ; i < ancienne.length ; i++ ) {
					for( int j = 1 ; j < ancienne[i].length ; j++) {
						matricePoids[i][j] = ancienne[i][j];
					}

				}

	// si on connait le nombre de sommets dès le départ
	public GrapheOrientePondere(int n) {
		super();
		initialiserMatrice(n);
	};

	public GrapheOrientePondere() {
		super();
		matricePoids = new double[1][1];
	}

	@Override
	public void ajouterSommet(T sommet) {
		super.ajouterSommet(donnee);
		redimensionnerMatrice();
	}

	@Override
	public void supprimerSommet(T sommet) {
		super.supprimerSommet(donnee);
		redimensionnerMatrice();
	}

	@Override
	public double getPoids(int sommet1, int sommet2) {
		return matricePoids[sommet1][sommet2];
	}

	@Override
	public void setPoids(int sommet1, int sommet2, double poids) {
		matricePoids[sommet1][sommet2] = poids;
	}

	@Override
	public double[][] getMatricePoids() {
		return matricePoids;
	}

	/**public List<T> djikstra(T depart , T arrivee ) {
    	List<T> tableaudjikstra = new ArrayList<>();
    	//algo de djikstra
    	return tableaudjikstra;
    }*/

	public void djikstra(int *fs, int *aps, int **matricePoids , int s ,int *&d , int *&pred) throws Exception  {
		if(!verifierConditions()) {
			throw new IllegalStateException( "L'algo de djikstra fonctionne uniquement qu'avec des poids positifs");
		}
		int nbSommets = aps[0];
		d = new int [nbSommets+1]; 
		d[0] = nbSommets; 

		pred = new int [nbSommets+1];
		bool *ins = new bool [nbSommets+1]; 

		for( int i = 1 ; i <= nbSommets ; i++) {
			d[i] = matricePoids[s][i];
			pred[i] = s;
			ins[i] = true;
		}
		ins[s] = false; 
		int cpt = nbSommets-1; 
		while (cpt > 0) {
			int min = MaxPoids; 
			for( int i = 1 ; i <= nbSommets ; i++) {
				if((ins[i]) && (d[i] < min)) {
					min = d[i];
					j= i; 
				}
				if(min == MaxPoids)
					return ; 
				ins[j] = false;
				for(int k = aps[j] ; (h=fs[k]) != 0 ; k++) {
					if(ins[h]) {
						int r = d[j]+ matricePoids[j][h];
						if(v < d[h]) {
							d[h] = v; 
							pred[h] = j;
						}
					}
				}
			}
		}
	}

	public double[][] dantzig() {
		int nombreSommet = getOrdre();
		double[][] tableaudantzig = new Double[nombreSommet][nombreSommet];
		// algo de dantzig
		return tableaudantzig;
	}

	public boolean vérifierConditions() {
		for (int i = 0; i < matricePoids.length; i++) {
			for (int j = 0; j < matricePoids[i].length; j++) {
				if (matricePoids[i][j] < 0) {
					return false;
				}
			}
		}
		return true;
	}

	// affichage complet du graphe
	@Override
	public void afficher() {
		for (int i = 0; i < matricePoids.length; i++) {
			System.out.print("Sommet " + i + "->");
			for (int j = 0; j < matricePoids[i].length; j++) {
				System.out.print(j + "(" + matricePoids[i][j] + ")");
			}
			System.out.println();
		}
	}

	};

	// affichage bref du graphe
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Sommets : ");
		sb.append(getOrdre());
		sb.append(", arcs : ");
		sb.append(arcs.size());
		return sb.toString();

	}
}