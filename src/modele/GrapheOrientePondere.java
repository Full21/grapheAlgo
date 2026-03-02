package modele;


import java.util.List;
import java.util.ArrayList;
/*un graphe orienté pondéree est un graphe avec des sommets , arcs et ds 
 *flèches des deux côtés, ainsi qu'un certain poids sur un arc entre 2 sommets
 */

public class GrapheOrientePondere<T> extends GrapheOriente<T> implements IPondere {
	private Double [][] matricePoids;
    public GrapheOrientePondere() {
    	super();
    };
    
    @override
    public Double getPoids(int sommet1 , int sommet2) {
    	return matricePoids[sommet1][sommet2];
    	}
    
    @override
    public void setPoids(int sommet1 , int sommet2 , double poids) {
    	matricePoids[sommet1][sommet2] = poids;
    }
    
    
    public List<T> djikstra(T depart , T arrivee ) {
    	List<T> tableaudjikstra = new ArrayList<>();
    	//algo de djikstra
    	return tableaudjikstra;
    }
    
    public double[][] dantzig(){
    	int nombreSommet = getOrdre();
    	double[][] tableaudantzig = new Double[nombreSommet][nombreSommet];
    	//algo de dantzig
    	return tableaudantzig;
    }
    
    public boolean vérifierConditions() {
    	for(int i=0 ; i< matricePoids.length ; i++) {
           for (int j = 0 ; j<matricePoids[i].length ; j++) {
    			if(matricePoids[i][j] != null && matricePoids[i][j] < 0) {
    				return false ;
    			}
    		}
    	}
    	return true; 
    }
    		
   @override
    public void afficher() {
    		 for (int i = 0 ; i < matricePoids.length ; i++) {
    			 System.out.print("Sommet " + i + "->");
    			 for(int j = 0 ; j < matricePoids[i].length ; j++) {
    			   System.out.print(j + "(" + matricePoids[i][j] + ")");
    			 }
    			 System.out.println();
    		 }
    		}
    };
    
    @override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	sb.append("Sommets : ");
    	sb.append(getOrdre());
    	sb.append(", arcs : ");
    	sb.append(arcs.size());
    	return sb.toString();

}
