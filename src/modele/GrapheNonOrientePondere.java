package modele;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;

public class GrapheNonOrientePondere<T> extends GrapheNonOriente<T> implements IPondere {

	private double[][] matricePoids;

	
	public GrapheNonOrientePondere(int nbSommets) {
		super(nbSommets);
		matricePoids = new double[nbSommets+1][nbSommets+1];
		this.estPondere = true;
		for(double[]ligne : matricePoids) {
			for(int i = 0; i < ligne.length; i++)
				ligne[i] = MAX_POIDS;
		}
	}
	
	public GrapheNonOrientePondere() {
		this(100);
	}

	
	@Override
	public void afficher() {

	}

	@Override
	public double getPoids(int sommet1, int sommet2) {
		return this.matricePoids[sommet1][sommet2];
	}

	@Override
	public void setPoids(int sommet1, int sommet2, double poids) {
		this.matricePoids[sommet1][sommet2] = poids;
	}

	@Override
	public double[][] getMatricePoids() {
		return this.matricePoids;
	}

	public GrapheNonOrientePondere<T> kruskal() {
	    int nbSommets = this.sommets.size();
	    int nbArcs = this.arcs.size();
	    
	    GrapheNonOrientePondere<T> h = new GrapheNonOrientePondere<T>(nbSommets);
	    for(Sommet<T> s : this.sommets)
	    	h.ajouterSommet(s.donnee);
	    
	    trierArcs();
	    
	    int ig = 0;  
	    int arcsAjoutes = 0;
	    
	    int[] prem = new int[nbSommets + 1];
	    int[] pilch = new int[nbSommets + 1];
	    int[] cfc = new int[nbSommets + 1];
	    int[] nbElem = new int[nbSommets + 1];
	    
	    for (int i = 1; i <= nbSommets; i++) {
	        prem[i] = i;
	        cfc[i] = i;
	        nbElem[i] = 1;
	    }
	    
	    while (ig < nbArcs && arcsAjoutes < nbSommets - 1) {
	        Arc<T> b = this.arcs.get(ig);
	        int x = cfc[b.getSource().getId()];
	        int y = cfc[b.getDestination().getId()];
	        
	        if (x != y) {
	            h.ajouterArc(b.source.donnee, b.destination.donnee, b.poids);
	            arcsAjoutes++;
	            fusion(x, y, prem, pilch, cfc, nbElem);
	        }
	        ig++;
	    }	    
	    return h;
	}

	private void fusion(int i, int j, int[] prem, int[] pilch, int[] cfc, int[] nbElem) {
	    // Assure que i est la composante la plus grande
	    if (nbElem[j] > nbElem[i]) {
	        int temp = nbElem[j];
	        nbElem[j] = nbElem[i];
	        nbElem[i] = temp;
	    }
	    
	    // Fusionne la liste chaînée de j dans i
	    int s = prem[j];
	    cfc[s] = i;
	    while (pilch[s] != 0) {
	        s = pilch[s];
	        cfc[s] = i;
	    }
	    
	    pilch[s] = prem[i];
	    prem[i] = prem[j];
	    
	    nbElem[i] += nbElem[j];
	}		
	
	private void trierArcs() {
	    this.arcs.sort(new Comparator<Arc<T>>() {
	        @Override
	        public int compare(Arc<T> a1, Arc<T> a2) {
	            return Double.compare(a1.getPoids(), a2.getPoids());
	        }
	    });
	}
	
	public void charger(String fichier) {

		super.charger(fichier);
		int n = getOrdre();

		Path path = Path.of(System.getProperty("user.dir"), "src", "ressources", fichier);
		try {
			List<String> lignes = Files.readAllLines(path);

			// Chercher "MATRICE DES POIDS" sans break
			int debutPoids = -1;
			for (int i = 0; i < lignes.size(); i++) {
				if (lignes.get(i).equalsIgnoreCase("MATRICE DES POIDS") && debutPoids == -1) {
					debutPoids = i + 1;
				}
			}

			matricePoids = new double[n + 1][n + 1];

			for (int i = 0; i <= n; i++) {
				String ligne = lignes.get(debutPoids + i);
				String[] vals = ligne.split(";");
				for (int j = 0; j < vals.length && j <= n; j++) {
					this.matricePoids[i][j] = Double.parseDouble(vals[j]);
				}
			}

			for (Arc<?> arc : this.arcs) {
				arc.setPoids(this.matricePoids[arc.getSource().getId()][arc.getDestination().getId()]);
			}

		} catch (java.io.IOException e) {
			throw new IllegalArgumentException("Le chargement a échoué : " + e.getMessage());
		}
	}
	
	
	public void setMatricePoids(double[][] matricePoids) {
		this.matricePoids = matricePoids;
	}

	@Override
	public void sauvegarder(String fichier) {
	    // Initialiser la matrice des poids depuis les arcs
	    int n = this.sommets.size();
	    this.matricePoids = new double[n + 1][n + 1];
	    for (Arc<T> arc : this.arcs) {
	        int i = arc.getSource().getId();
	        int j = arc.getDestination().getId();
	        this.matricePoids[i][j] = arc.getPoids();
	        this.matricePoids[j][i] = arc.getPoids();
	    }

	    super.sauvegarder(fichier);

	    Path path = Path.of(System.getProperty("user.dir"), "src", "ressources", fichier);
	    try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toString(), true))) { // true = append
	        writer.append("MATRICE DES POIDS\n");
	        for (double[] ligne : this.matricePoids) {
	            for (double poids : ligne) writer.append(poids + ";");
	            writer.append("\n");
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("GrapheNonOrientePondere (").append(sommets.size()).append(" sommets, ")
	      .append(arcs.size()).append(" arêtes)\n");

	    for (Arc<T> arc : arcs) {
	        sb.append("  ")
	          .append(arc.getSource().getDonnee())
	          .append(" -- ")
	          .append(arc.getDestination().getDonnee())
	          .append(" [poids=")
	          .append(arc.getPoids())
	          .append("]\n");
	    }
	    return sb.toString();
	}

	
	
	
}
