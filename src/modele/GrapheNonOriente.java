package modele;

import java.util.ArrayList;
import java.util.List;

public class GrapheNonOriente<T> extends Graphe<T> {
	private List<Arc<T>> arbreCouvrant = new ArrayList<Arc<T>>();
	private int dfsConnexe(int s , boolean[] visite, int nbVisites) {
		visite[s] = true; 
		nbVisites++;
		
		for(int t: getVoisins(s)) {
			if(!visite[t]) 
				nbVisites = dfsConnexe(t, visite, nbVisites);
			
		}
		return nbVisites;
	}
	
	private 	void empiler( int x , int []pilch) {
		pilch[x] = pilch[0];
		pilch[0] = x; 
	}

	public GrapheNonOriente(int nbsommets) {
		super(nbsommets);
		this.estOriente = false;
		this.estPondere = false;

	}

	public boolean estArbre() {
		int nbsom = this.getOrdre();
		int nbarcs= this.matriceAdjacence[0][1];
		return estConnexe() && nbarcs == nbsom-1;

	}


	public boolean estConnexe() {
        if(sommets.isEmpty())
        	return true; 
        
        boolean[] visite = new boolean[sommets.size()+1]; 
        int nbVisites = 0;
        
        nbVisites = dfsConnexe(1,visite,nbVisites);
        
        return nbVisites == sommets.size();
	}

     
	public void calculerRangs() {
		int nbsommets = aps[0];
		int taillefs = fs[0];
		int [] rang = new int [nbsommets+1];
		int [] ddi = new int [nbsommets+1];
		int [] pilch = new int [nbsommets+1];
		
		for( int i = 1 ; i <= nbsommets ; i++)
			ddi[i] = 0; 
		 
		for(int i = 1 ; i <= taillefs ; i++) {
			int s = fs[i]; 
			if(s > 0) 
				ddi[s]++;
		}
		
		pilch[0] = 0; 
		for( int i = 1 ; i <= nbsommets ; i++) {
			rang[i] = -1;
			if(ddi[i] == 0)
				empiler(i, pilch);
		}
		
	 int k = -1;
	 int s = pilch[0]; 
	 
	 while(pilch[0] > 0) {
		 k++;
		 pilch[0] = 0; 
		 
		 while( s > 0) {
			 rang[s] = k; 
			 int h = aps[s]; 
			 int t = fs[h];
			 
			 while(t > 0) {
				 ddi[t]--; 
				 if(ddi[t] == 0)
					 empiler(t, pilch);
				 h++;
				 t = fs[h];
			 }
			 s = pilch[s]; 
		 }
		 s = pilch[0];
	 }
	 
	 for( int i = 0 ; i < this.sommets.size(); i++) {
		 this.sommets.get(i).setRang(rang[i+1]);
	 }

	}

	@Override
	public void ajouterArc(T donnee1, T donnee2) {
		super.ajouterArc(donnee1, donnee2);
		super.ajouterArc(donnee2, donnee1);
	}
	
	
	public int [] codagePrufer(int [][]a)throws Exception  {
		 if(!estArbre()) {
			 throw new IllegalStateException("le codage de prufer s'applique seulement aux arbres");
		 
		 }
			  int nbSommet = a[0][0];
			int []  pr = new int [nbSommet-1] ; 
			  pr[0] = nbSommet-2;
			  
			  for( int i = 1; i <= nbSommet-2 ; i++) {
				  int s= 1; 
				  
				  while (a[s][0] != 1)
					  s++; 
				  int j = 1; 
				  while( a[s][j] != 1)
					  j++; 
				  
				  pr[i] = j; 
				  a[s][j] = 0;
				  a[j][s] = 0;
				  a[s][0] = 0;
				  a[j][0]--;
			  }
			 
		 return pr;

	}

	public GrapheNonOriente<T> decodagePrufer(int []){
    	  
      }

	public void afficher() {
        int nbsom = this.getOrdre(); 
        System.out.println("Graphe non orienté"); 
        System.out.println("Sommets : " + nbsom); 
        System.out.println("Arcs : " + (this.matriceAdjacence[0][1]));
        
        for( int i = 1 ; i <= nbsom ; i++) {
        	System.out.println("Sommet" + i + "-> voisins : ");
        	int it = aps[i]; 
        	int t = fs[it]; 
        	while(t > 0) {
        		System.out.print(t + "");
        		t = fs[++it];
        	}
        	System.out.println();
        }
        
	}
 
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Graphe non oriente [").append(getOrdre()).append("sommets]\n");
		
		for( int i = 1 ; i <= getOrdre() ; i++) {
			sb.append(i).append("->");
			int it = aps[i]; 
			int t = fs[it]; 
			while( t > 0) {
				sb.append(t).append(" ");
				t = fs[++it];
			}
			sb.append("\n");
		}
		return sb.toString();

	}

	@Override
	public List<T> getVoisins(T sommet) {
		// TODO Auto-generated method stub
		return null;
	}

}
