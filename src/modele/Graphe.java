package modele;

import java.util.List;
import java.util.ArrayList;

public abstract class Graphe <T> implements IGraphe<T>, ISauvegardable{
	
	protected List<Sommet<T>> sommets;
	protected List<Arc<T>> arcs;
	protected boolean estOriente;
	protected boolean estPondere;
	protected int[] fs, aps;
	protected int[][] matriceAdjacence;
	
	
	
	public Graphe(int nbSommets){
		this.sommets = new ArrayList<Sommet<T>>();
		this.arcs = new ArrayList<Arc<T>>();
		this.matriceAdjacence = new int[nbSommets + 1][nbSommets + 1];
	}

	@Override
	public void ajouterSommet(T sommet) {
		sommets.add(new Sommet<T>(sommet));
		this.matriceAdjacence[0][0]++;
		construireFsEtAps();
	}
		
	
	@Override
	public void ajouterArc(Sommet<T> sommet1, Sommet<T> sommet2, double poids) {		
		Arc<T> arc = new Arc<T>(sommet1, sommet2, poids);
		arcs.add(arc);
		this.matriceAdjacence[0][1]++;
		this.matriceAdjacence[sommet1.getId()][sommet2.getId()] = 1;
		construireFsEtAps();
	}
	
	@Override
	public void ajouterArc(Sommet<T> sommet1, Sommet<T> sommet2) {		
		ajouterArc(sommet1, sommet2, 0);
	}
	
	@Override
	public void supprimerSommet(T sommet) {		
		Sommet<T> sommetSupprimer = new Sommet<T>(sommet);
		if(sommets.contains(sommetSupprimer)) {
			sommets.remove(sommetSupprimer);
			supprimerArc(sommetSupprimer);
		}
		this.matriceAdjacence[0][0]--;
		construireFsEtAps();
	}
	
	@Override
	public void supprimerArc(Sommet<T> sommet) {			
		for(int i = this.arcs.size() - 1; i >= 0; i--) {	
			Arc<T> arc = arcs.get(i);
			if(arc.source.equals(sommet) || arc.destination.equals(sommet)) {
				arcs.remove(arc);
				this.matriceAdjacence[0][1]--;
				this.matriceAdjacence[arc.source.id][arc.destination.id] = 0;				
			}
		}
		construireFsEtAps();
	}
	
	
	
//	@Override
//	public List<T> getVoisins(T sommet) {
//		
//	}
	
	@Override
	public int getOrdre() {
		return sommets.size();
	}
	
	@Override
	public int[][] getMatriceAdjacence() {
		return this.matriceAdjacence;
	}
	
	@Override
	public int[] getFs() {
		return this.fs;
	}
	
	@Override
	public int[] getAps() {
		return this.aps;
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
	
	private void construireFsEtAps() {		
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
	
	/*	
	
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
