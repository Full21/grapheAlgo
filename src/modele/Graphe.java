package modele;

import java.util.List;
import java.util.ArrayList;

public abstract class Graphe <T> implements IGraphe<T>, ISauvegardable{
	
	protected List<Sommet<T>> sommets;
	protected List<Arc<T>> arcs;
	protected boolean estOriente;
	protected boolean estPondere;
	
	@Override
	public void ajouterSommet(T sommet) {
		sommets.add(sommet);
	}
	
	@Override
	public void ajouterArc(T sommet1, T sommet2) {
		Arc arc = new Arc<T>(sommet1, sommet2);
		arcs.add(arc);
	}
	
	@Override
	public void supprimerSommet(T sommet) {
		if(sommets.contains(sommet)) {
			sommets.remove(sommet);
		}
	}
	
	@Override
	public void supprimerArc(T sommet1, T sommet2) {
		Arc arc = new Arc<T>(sommet1, sommet2);
		if(arcs.contains(arc)) {
			arcs.remove(arc);
		}
	}
	
	@Override
	public List<T> getVoisins(T sommet) {
		
	}
	
	@Override
	public int getOrdre() {
		return sommets.size();
	}
	
	@Override
	public int[][] getMatriceAdjacence() {
		
	}
	
	@Override
	public int[] getFs() {
		
	}
	
	@Override
	public int[] getAPS() {
		
	}
	
	@Override
	public void sauvegarder(String fichier) {
		
	}
	
	@Override
	public void charger(String fichier) {
		
	}
	
	@Override
	public void exporter(String fichier) {
		
	}
	
	/*private int [] fs, aps;
	private int [][] matriceAdjacence, matriceDistance;
	
	public Graphe(int [] fs, int [] aps) {
		this.fs = fs;
		this.aps = aps;
		initialiserMatriceAdjacence();
		initialiserMatriceDistance();
	}
	
	public Graphe(int [][] matriceAdjacence) {
		this.matriceAdjacence = matriceAdjacence;
		initialiserFsEtAps();
		initialiserMatriceDistance();
	}
	
	private void initialiserFsEtAps() {		
		int nbSom = this.matriceAdjacence[0][0];
	    int nbArc = this.matriceAdjacence[0][1];
		this.fs = new int[nbSom + nbArc + 1];	
		this.aps = new int[nbSom];

	    int k = 0;
	    for (int ligne = 1; ligne < nbSom; ligne++) {
	        aps[ligne] = ++k;

	        for (int colonne = 1; colonne < nbSom; colonne++) {
	            if (this.matriceAdjacence[ligne][colonne] == 1) {
	                fs[k++] = colonne;
	            }
	        }
	        fs[k] = 0;
	    }

	    fs[0] = k;
	}
	
	private void initialiserMatriceAdjacence() {
		int nbSommets = aps[0];
	    int nbArcs = fs[0] - aps[0];
	    
	    this.matriceAdjacence = new int[nbSommets + 1][nbSommets + 1];
	    this.matriceAdjacence[0][0] = nbSommets;
	    this.matriceAdjacence[0][1] = nbArcs;
	    for (int i = 1; i <= nbSommets; i++) {
	        int j = aps[i];

	        while (fs[j] != 0) {
	            int k = fs[j];
	            this.matriceAdjacence[i][k] = 1;
	            j++;
	        }
	    }
	    
	}
	
    private void initialiserMatriceDistance() {
    	int nbSom = this.aps[0];
    	this.matriceDistance = new int[nbSom + 1][nbSom + 1];
    	matriceDistance[0][0] = nbSom;
    	matriceDistance[0][1] = this.matriceAdjacence[0][1];
    	
    	for(int sommet = 1; sommet <= nbSom; sommet++) {
    		this.matriceDistance[sommet] = distanceDuSommetR(sommet);
    	}    	    	
    }

	
	private int[] distanceDuSommetR(int r) {
				
		int nbSom = this.aps[0];
	    int i = 0, j = 1, k = 0, ifin, s, t, it;

	    int[] fil = new int[nbSom + 1];
	    int[] dist = new int[nbSom + 1];

	    fil[0] = nbSom;
	    dist[0] = nbSom;

	    fil[1] = r;

	    for (int h = 1; h <= nbSom; h++) {
	        dist[h] = -1;
	    }

	    dist[r] = 0;

	    while (i < j) {
	        k++;
	        ifin = j;

	        while (i < ifin) {
	            i++;
	            s = fil[i];
	            it = aps[s];
	            t = fs[it];

	            while (t > 0) {
	                if (dist[t] == -1) {
	                    j++;
	                    fil[j] = t;
	                    dist[t] = k;
	                }
	                t = fs[++it];
	            }
	        }
	    }

	    return dist;
		
	}	*/

	
}
