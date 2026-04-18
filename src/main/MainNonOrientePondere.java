package main;

import java.util.List;

import modele.GrapheNonOrientePondere;

public class MainNonOrientePondere {

	public static void main(String[] args) {

		GrapheNonOrientePondere<Integer> graphe = new GrapheNonOrientePondere<Integer>(7);
		
		graphe.ajouterSommet(1);
		graphe.ajouterSommet(2);
		graphe.ajouterSommet(3);
		graphe.ajouterSommet(4);
		graphe.ajouterSommet(5);
		graphe.ajouterSommet(6);
		graphe.ajouterSommet(7);
		
		graphe.ajouterArc(1, 5, 4);
		graphe.ajouterArc(1, 3, -1);
		graphe.ajouterArc(1, 6, 2);
		
		graphe.ajouterArc(2, 3, 1);
		graphe.ajouterArc(2, 4, 3);
		graphe.ajouterArc(2, 5, 1);
		
		graphe.ajouterArc(4, 7, 5);
		
		GrapheNonOrientePondere<Integer> h = graphe.kruskal();
		
		affiche1D(h.getAps());
		System.out.println();
		
		affiche1D(h.getFs());
		System.out.println();
		
		affiche2D(h.getMatriceAdjacence());
		
		
		
	}

	public static void affiche2D(int[][] tab) {
	    for (int[] ligne : tab) {
	        for (int val : ligne) {
	            System.out.print(val + " ");
	        }
	        System.out.println();
	    }
	}
	
	public static void affiche1D(int[] tab) {
	    for (int t : tab) {
	        System.out.print(t + " ");
	    }
	    System.out.println();
	}
}
