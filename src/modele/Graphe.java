package modele;

import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
	
	// Ajout et suppression de sommet
	
	@Override
	public void ajouterSommet(T sommet) {
		sommets.add(new Sommet<T>(sommet));
		this.matriceAdjacence[0][0]++;
		construireFsEtAps();
	}
	
	@Override
	public void supprimerSommet(T sommet) {		

		for(int i = this.sommets.size() - 1; i >= 0; i--) {
			Sommet<T> sommetCourant = sommets.get(i);
			if(sommetCourant.donnee.equals(sommet)) {
				this.sommets.remove(i);
				this.matriceAdjacence[0][0]--;
			}
		}
		construireFsEtAps();
	}
			
	// Ajout et suppression des arcs
	
	@Override
	public void ajouterArc(T donnee1, T donnee2) {
		Sommet<T> sommet1 = null;
		Sommet<T> sommet2 = null;
		
		for(Sommet<T> som : this.sommets) {
			if(som.donnee.equals(donnee1)) sommet1 = som;
			if(som.donnee.equals(donnee2)) sommet2 = som;
		}
		if(sommet1 != null && sommet2 != null)
			ajouterArc(sommet1, sommet2);
	}
	

	private void ajouterArc(Sommet<T> sommet1, Sommet<T> sommet2, double poids) {		
		Arc<T> arc = new Arc<T>(sommet1, sommet2, poids);
		arcs.add(arc);
		this.matriceAdjacence[0][1]++;
		this.matriceAdjacence[sommet1.getId()][sommet2.getId()] = 1;
		construireFsEtAps();
	}
	

	private void ajouterArc(Sommet<T> sommet1, Sommet<T> sommet2) {		
		ajouterArc(sommet1, sommet2, 0);
	}
	

	@Override
	public void supprimerArc(T donnee1, T donnee2) {
		Sommet<T> sommet1 = null;
		Sommet<T> sommet2 = null;
		
		for(Sommet<T> som : this.sommets) {
			if(som.donnee.equals(donnee1)) sommet1 = som;
			if(som.donnee.equals(donnee2)) sommet2 = som;
		}
		if(sommet1 != null && sommet2 != null)
			supprimerArc(sommet1, sommet2);
	}
	
	@Override
	public void supprimerArcs(T donnee1) {
		Sommet<T> sommet1 = null;
		for(Sommet<T> som : this.sommets) {
			if(som.donnee.equals(donnee1)) sommet1 = som;
		}
		if(sommet1 != null)
			supprimerArcs(sommet1);
	}
	
	private void supprimerArcs(Sommet<T> sommet) {			
		for(int i = this.arcs.size() - 1; i >= 0; i--) {	
			Arc<T> arc = arcs.get(i);
			if(arc.source.equals(sommet) || arc.destination.equals(sommet)) {
				arcs.remove(i);
				this.matriceAdjacence[0][1]--;
				this.matriceAdjacence[arc.source.id][arc.destination.id] = 0;				
			}
		}
		construireFsEtAps();
	}
	

	private void supprimerArc(Sommet<T> sommet1, Sommet<T> sommet2) {			
		for(int i = this.arcs.size() - 1; i >= 0; i--) {	
			Arc<T> arc = arcs.get(i);
			if(arc.source.equals(sommet1) && arc.destination.equals(sommet2)) {
				arcs.remove(i);
				this.matriceAdjacence[0][1]--;
				this.matriceAdjacence[arc.source.id][arc.destination.id] = 0;				
			}
		}
		construireFsEtAps();
	}
	
	// Les getteurs 
	@Override
	public List<Sommet<T>> getVoisins(T sommetData) {
	    List<Sommet<T>> voisins = new ArrayList<>();
	    
	    Sommet<T> sommet = null;
	    for (Sommet<T> s : this.sommets) {
	        if (s.getDonnee().equals(sommetData)) {
	            sommet = s;
	            break;
	        }
	    }
	    
	    if (sommet == null) {
	        return voisins;
	    }
	    
	    int id = sommet.getId();
	    int voisinIndex = this.aps[id];
	    
	    while (voisinIndex != 0) { 
	        Sommet<T> voisin = this.sommets.get(voisinIndex - 1); 
	        voisins.add(voisin);
	        voisinIndex = this.fs[voisinIndex];
	    }
	    
	    return voisins;
	}
	
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
		StringBuilder sbFs = new StringBuilder("FS\n");		
		for(int a : this.fs) {
			sbFs.append(a+";");
		}
					
		StringBuilder sbAps = new StringBuilder("\nAPS\n");
		for(int a : this.aps) {
			sbAps.append(a+";");
		}
		
		StringBuilder sbMatAdj = new StringBuilder("\nMATRICE D'ADJACENCE\n");
		for(int ligne[] : this.matriceAdjacence) {
			for(int chiffre : ligne) {
				sbMatAdj.append(chiffre+";");
			}
			sbMatAdj.append("\n");
		}
		Path path = Path.of(System.getProperty("user.dir"), "src", "ressources", fichier);
		try {
			Files.createDirectories(path.getParent());
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toString()))) {
				writer.append(this.matriceAdjacence[0][0]+";"+this.matriceAdjacence[0][1]+"\n");
				writer.append(sbFs);
				writer.append(sbAps);
				writer.append(sbMatAdj);
			}		
            System.out.println("Sauvegarde réussie");            
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	@Override
	public void charger(String fichier) {
		Path path = Path.of(System.getProperty("user.dir"), "src", "ressources", fichier);
		try(BufferedReader reader = new BufferedReader(new FileReader(path.toString()))){			
			List<String> lignes = reader.readAllLines();
			
			String[] donnees = lignes.get(0).split(";"); 
			int nbSom = Integer.parseInt(donnees[0]);
			int nbArc = Integer.parseInt(donnees[1]);
			
			// Construire FS			
			this.fs = new int[nbSom + nbArc + 1];
			charger(lignes.get(2), fs);					
			
			// Construire APS
			this.aps = new int[nbSom + 1];
			charger(lignes.get(4), aps);
			
			// Construire Matrice Adjacence
			this.matriceAdjacence = new int[nbSom + 1][nbSom + 1];
			for(int i = 6; i <= nbSom; i++) {
				charger(lignes.get(i), matriceAdjacence[i - 6]);
			}					
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void charger(String ligne, int[] tab) {
		String [] fsLigne = ligne.split(";");	
		
		for(int i = 0; i < fsLigne.length; i++) {
			tab[i] = Integer.parseInt(fsLigne[i]);				
		}
	}
	
	private void construireFsEtAps() {		
		int nbSom = this.matriceAdjacence[0][0];
	    int nbArc = this.matriceAdjacence[0][1];
		this.fs = new int[nbSom + nbArc + 1];	
		this.aps = new int[nbSom + 1];
		this.aps[0] = nbSom;
		fs[0] = nbSom + nbArc;

	    int k = 1;
	    for (int ligne = 1; ligne <= nbSom; ligne++) {
	        aps[ligne] = k;

	        for (int colonne = 1; colonne <= nbSom; colonne++) {
	            if (this.matriceAdjacence[ligne][colonne] != 0) {
	                fs[k++] = colonne;
	            }
	        }
	        fs[k++] = 0;
	    }

	}
	
	public abstract void afficher(); 
	
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
