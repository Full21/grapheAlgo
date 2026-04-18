package modele;

import java.util.List;
import java.util.ArrayList;
/*un graphe orienté pondéree est un graphe avec des sommets , arcs et ds 
 *flèches des deux côtés, ainsi qu'un certain poids sur un arc entre 2 sommets
 */

public class GrapheOrientePondere<T> extends GrapheOriente<T> implements IPondere {
	private double[][] matricePoids;
	private final int MAX_POIDS = 100;
     

	// si on connait le nombre de sommets dès le départ
	public GrapheOrientePondere() {
		super(100);
		matricePoids = new double[101][101];
		this.estPondere  = true; 
	};

	public GrapheOrientePondere(int n ) {
		super(n);
		matricePoids = new double[n+1][n+1];
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
	
	
	public boolean verifierConditions() {
		for (int i = 0; i < matricePoids.length; i++) {
			for (int j = 0; j < matricePoids[i].length; j++) {
				if (matricePoids[i][j] < 0) {
					return false;
				}
			}
		}
		return true;
	}
	
	
	
	public void djikstra(int []fs, int []aps, int [][]matricePoids , int s ,int []d , int []pred) throws Exception  {
		if(!verifierConditions()) {
			throw new IllegalStateException( "L'algo de djikstra fonctionne uniquement qu'avec des poids positifs");
		}
		int nbSommets = aps[0];
		d = new int [nbSommets+1]; 
		d[0] = nbSommets; 

		pred = new int [nbSommets+1];
		boolean [] ins = new boolean [nbSommets+1]; 

		for( int i = 1 ; i <= nbSommets ; i++) {
			d[i] = matricePoids[s][i];
			pred[i] = s;
			ins[i] = true;
		}
		int j =  0; 
		ins[s] = false; 
		int cpt = nbSommets-1; 
		while (cpt > 0) {
			int min = MAX_POIDS; 
			for( int i = 1 ; i <= nbSommets ; i++) {
				if((ins[i]) && (d[i] < min)) {
					min = d[i];
					j= i; 
				}
				if(min == MAX_POIDS)
					return ; 
				ins[j] = false;
				int h = 0;
				for(int k = aps[j] ; (h=fs[k]) != 0 ; k++) {
					if(ins[h]) {
						int v = d[j]+ matricePoids[j][h];
						if(v < d[h]) {
							d[h] = v; 
							pred[h] = j;
						}
					}
				}
			}
		}
	}
	
	//methode pour une initialisation parfaite de la matrice dantzig pour l'utilisateur 
	
	public double[][] initialiserMatriceDistances() {
	    int nombreSommet = getOrdre();
	    double[][] c = new double[nombreSommet + 1][nombreSommet + 1];
	    
	    for (int i = 1; i <= nombreSommet; i++) {
	        for (int j = 1; j <= nombreSommet; j++) {
	            if (i == j) {
	                c[i][j] = 0;
	            } else if (this.matriceAdjacence[i][j] == 1) {
	                c[i][j] = this.matricePoids[i][j];
	            } else {
	                c[i][j] = Double.POSITIVE_INFINITY;
	            }
	        }
	    }
	    return c;
	}

	public boolean dantzig(double[][] c) {
	    int nombreSommet = getOrdre();
	    
	    for (int k = 1; k < nombreSommet; k++) {
	        for (int i = 1; i <= k; i++) {
	            for (int j = 1; j <= k; j++) {
	                // Mise à jour de c[i][k+1]
	                double x = c[i][j] + c[j][k + 1];
	                if (x < c[i][k + 1]) {
	                    c[i][k + 1] = x;
	                }
	                // Mise à jour de c[k+1][i]
	                double y = c[k + 1][j] + c[j][i];
	                if (y < c[k + 1][i]) {
	                    c[k + 1][i] = y;
	                }
	            }
	            // Détection d'un circuit de poids négatif
	            if (c[i][k + 1] + c[k + 1][i] < 0) {
	                System.out.println("Circuit de poids négatif détecté !");
	                return false;
	            }
	        }
	        // Mise à jour des distances entre anciens sommets
	        for (int i = 1; i <= k; i++) {
	            for (int j = 1; j <= k; j++) {
	                double x = c[i][k + 1] + c[k + 1][j];
	                if (x < c[i][j]) {
	                    c[i][j] = x;
	                }
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