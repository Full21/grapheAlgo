package main;

import java.util.List;

import modele.GrapheNonOriente;
import modele.GrapheOriente;
import modele.Sommet;

public class MainNonOriente {

	public static void main(String[] args) {
		GrapheNonOriente<Integer> graphe = new GrapheNonOriente<Integer>(7);
		
		graphe.ajouterSommet(1);
		graphe.ajouterSommet(2);
		graphe.ajouterSommet(3);
		graphe.ajouterSommet(4);
		graphe.ajouterSommet(5);
		graphe.ajouterSommet(6);
		graphe.ajouterSommet(7);
		
		graphe.ajouterArc(1, 2);
		graphe.ajouterArc(1, 5);
				
		graphe.ajouterArc(3, 4);
		
		graphe.ajouterArc(4, 2);
				
		graphe.ajouterArc(5, 4);
		
		graphe.ajouterArc(6, 1);
		
		graphe.ajouterArc(7, 2);
		graphe.ajouterArc(7, 4);
		
		affiche2D(graphe.getMatriceAdjacence());
		
		String arbre = graphe.estArbre() ? "Oui, c'est un arbre" : "Non, c'en est pas un";
		System.out.println(arbre);
		
		String connexe = graphe.estConnexe() ? "Oui, c'est un graphe connnexe" : "Non, ce n'est pas connexe";
		System.out.println(connexe);
		
//		try {
//			//affiche1D(graphe.codagePrufer());
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		
		GrapheNonOriente<Integer> graphe1 = new GrapheNonOriente<Integer>(7);
		
		graphe1.ajouterSommet(1);
		graphe1.ajouterSommet(2);
		graphe1.ajouterSommet(3);
		graphe1.ajouterSommet(4);
		graphe1.ajouterSommet(5);
		graphe1.ajouterSommet(6);
		graphe1.ajouterSommet(7);
		
		graphe1.ajouterArc(1, 2);
		graphe1.ajouterArc(1, 5);
		graphe1.ajouterArc(1, 6);
		
		graphe1.ajouterArc(2, 4);
		
		graphe1.ajouterArc(4, 3);
		graphe1.ajouterArc(4, 7);
					
		
		affiche2D(graphe1.getMatriceAdjacence());
		
		String arbre1 = graphe1.estArbre() ? "Oui, c'est un arbre" : "Non, c'en est pas un";
		System.out.println(arbre1);
		
		String connexe1 = graphe1.estConnexe() ? "Oui, c'est un graphe connnexe" : "Non, ce n'est pas connexe";
		System.out.println(connexe1);
		
		try {
			affiche1D(graphe1.codagePrufer());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void affiche1D(int[] tab) {
	    for (int t : tab) {
	        System.out.print(t + " ");
	    }
	    System.out.println();
	}
	
	public static void affiche2D(int[][] tab) {
	    for (int[] ligne : tab) {
	        for (int val : ligne) {
	            System.out.print(val + " ");
	        }
	        System.out.println();
	    }
	}
	
	public static void afficheListe2D(List<List<Integer>> liste) {
	    for (List<Integer> ligne : liste) {
	        for (Integer val : ligne) {
	            System.out.print(val + " ");
	        }
	        System.out.println(); // nouvelle ligne après chaque sous-liste
	    }
	}
	
	public static void afficherRangsSommets(GrapheOriente g) {
		List liste = g.getSommets();
		for(Object o : liste) {
			Sommet s = (Sommet)o;
			System.out.println("Sommet "+s.getId()+"; Rang : "+s.getRang());
		}
	}
	
	public static void afficheListe1D(List<Integer> liste) {
	    for (Integer val : liste) {
	        System.out.print(val + " ");
	    }
	    System.out.println(); // retour à la ligne
	}

}
