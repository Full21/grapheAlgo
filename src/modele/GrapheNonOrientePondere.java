package modele;

import java.util.List;

public class GrapheNonOrientePondere<T> /* extends GrapheNonOriente<T> implements IPondere */{

	private double[][] matricePoids;
	
	//@Override
	public void afficher() {
		
	}
	
	//@Override		
	public double getPoids(int sommet1, int sommet2) {
		return this.matricePoids[sommet1][sommet2];
	}
	
	//@Override 
	public void setPoids(int sommet1, int sommet2, double poids) {
		this.matricePoids[sommet1][sommet2] = poids;
	}
	
	//@Override
	public double[][] getMatricePoids(){
		return this.matricePoids;
	}
	
	 /* public List<Arc<T>> kruskal() {
		// Attente de la méthode du prof 
	 } */
}
