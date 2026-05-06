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
import java.util.Iterator;

public abstract class Graphe<T> implements IGraphe<T>, ISauvegardable {

	protected List<Sommet<T>> sommets;
	protected List<Arc<T>> arcs;
	protected boolean estOriente;
	protected boolean estPondere;
	protected int[] fs, aps;
	protected int[][] matriceAdjacence;

	public Graphe(int nbSommets) {
		this.sommets = new ArrayList<Sommet<T>>();
		this.arcs = new ArrayList<Arc<T>>();
		this.matriceAdjacence = new int[nbSommets + 1][nbSommets + 1];
		Sommet.setNB_SOMMETS(0);
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

		for (int i = this.sommets.size() - 1; i >= 0; i--) {
			Sommet<T> sommetCourant = sommets.get(i);
			if (sommetCourant.donnee.equals(sommet)) {
				this.sommets.remove(i);
				this.matriceAdjacence[0][0]--;
			}
		}
		construireFsEtAps();
	}

	// Ajout et suppression des arcs

	@Override
	public void ajouterArc(T donnee1, T donnee2, double poids) {
		Sommet<T> sommet1 = null;
		Sommet<T> sommet2 = null;

		for (Sommet<T> som : this.sommets) {
			if (som.donnee.equals(donnee1))
				sommet1 = som;
			if (som.donnee.equals(donnee2))
				sommet2 = som;
		}
		if (sommet1 != null && sommet2 != null)
			ajouterArc(sommet1, sommet2, poids);
	}

	@Override
	public void ajouterArc(T donnee1, T donnee2) {
		ajouterArc(donnee1, donnee2, 0);
	}

	protected void ajouterArc(Sommet<T> sommet1, Sommet<T> sommet2, double poids) {
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

		for (Sommet<T> som : this.sommets) {
			if (som.donnee.equals(donnee1))
				sommet1 = som;
			if (som.donnee.equals(donnee2))
				sommet2 = som;
		}
		if (sommet1 != null && sommet2 != null)
			supprimerArc(sommet1, sommet2);
	}

	@Override
	public void supprimerArcs(T donnee1) {
		Sommet<T> sommet1 = null;
		for (Sommet<T> som : this.sommets) {
			if (som.donnee.equals(donnee1))
				sommet1 = som;
		}
		if (sommet1 != null)
			supprimerArcs(sommet1);
	}

	private void supprimerArcs(Sommet<T> sommet) {
		for (int i = this.arcs.size() - 1; i >= 0; i--) {
			Arc<T> arc = arcs.get(i);
			if (arc.source.equals(sommet) || arc.destination.equals(sommet)) {
				arcs.remove(i);
				this.matriceAdjacence[0][1]--;
				this.matriceAdjacence[arc.source.id][arc.destination.id] = 0;
			}
		}
		construireFsEtAps();
	}

	private void supprimerArc(Sommet<T> sommet1, Sommet<T> sommet2) {
		for (int i = this.arcs.size() - 1; i >= 0; i--) {
			Arc<T> arc = arcs.get(i);
			if (arc.source.equals(sommet1) && arc.destination.equals(sommet2)) {
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

		while (this.fs[voisinIndex] != 0) {
			Sommet<T> voisin = this.sommets.get(this.fs[voisinIndex] - 1);
			voisins.add(voisin);
			voisinIndex++;
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
		for (int a : this.fs) {
			sbFs.append(a + ";");
		}

		StringBuilder sbAps = new StringBuilder("\nAPS\n");
		for (int a : this.aps) {
			sbAps.append(a + ";");
		}

		StringBuilder sbMatAdj = new StringBuilder("\nMATRICE D'ADJACENCE\n");
		for (int ligne[] : this.matriceAdjacence) {
			for (int chiffre : ligne) {
				sbMatAdj.append(chiffre + ";");
			}
			sbMatAdj.append("\n");
		}
		Path path = Path.of(System.getProperty("user.dir"), "src", "ressources", fichier);
		try {
			Files.createDirectories(path.getParent());
			try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toString()))) {
				writer.append(sommets.getFirst().donnee.getClass().getSimpleName() + ";");
				writer.append(estOriente + ";" + estPondere + ";");
				writer.append(this.matriceAdjacence[0][0] + ";" + this.matriceAdjacence[0][1] + "\n");
				for (Sommet<?> s : sommets) {
					writer.append(s.donnee + ";");
				}
				writer.append("\n");				
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

	    try {
	        List<String> lignes = Files.readAllLines(path);

	        String[] donnees = lignes.get(0).split(";");
	        String type    = donnees[0];
	        String oriente = donnees[1];
	        String pondere = donnees[2];
	        int nbSom = Integer.parseInt(donnees[3]);
	        int nbArc = Integer.parseInt(donnees[4]);

	        this.sommets.clear();
	        this.arcs.clear();
	        Sommet.setNB_SOMMETS(0);

	        // Ligne 1 : sommets
	        String[] sommetsDonnees = lignes.get(1).split(";");
	        for (int i = 0; i < nbSom; i++) {
	            if (type.equalsIgnoreCase("Integer")) {
	                ((Graphe<Integer>) this).ajouterSommet(Integer.parseInt(sommetsDonnees[i]));
	            } else {
	                ((Graphe<String>) this).ajouterSommet(sommetsDonnees[i]);
	            }
	        }

	        this.fs = new int[nbSom + nbArc + 1];
	        chargerTableau(lignes.get(3), this.fs);

	        this.aps = new int[nbSom + 1];
	        chargerTableau(lignes.get(5), this.aps);

	        this.matriceAdjacence = new int[nbSom + 1][nbSom + 1];
	        for (int i = 0; i <= nbSom; i++) {
	            chargerTableau(lignes.get(7 + i), this.matriceAdjacence[i]);
	        }

	        // Reconstruire les arcs depuis la matrice
	        for (int i = 1; i <= nbSom; i++) {
	            for (int j = 1; j <= nbSom; j++) {
	                if (this.matriceAdjacence[i][j] == 1) {
	                    Sommet s1 = this.trouverSommet(i);
	                    Sommet s2 = this.trouverSommet(j);
	                    if (s1 != null && s2 != null)
	                        this.arcs.add(new Arc(s1, s2, 0));
	                }
	            }
	        }	        

	    } catch (IOException e) {
	        throw new IllegalArgumentException("Le chargement a échoué : " + e.getMessage());
	    }
	}

	public static void chargerTableau(String ligne, int[] tab) {
	    String[] valeurs = ligne.split(";");
	    for (int i = 0; i < valeurs.length && i < tab.length; i++) {
	        tab[i] = Integer.parseInt(valeurs[i]);
	    }
	}

	private static void charger(String ligne, int[] tab) {
		String[] fsLigne = ligne.split(";");

		for (int i = 0; i < fsLigne.length; i++) {
			tab[i] = Integer.parseInt(fsLigne[i]);
		}
	}

	public void construireFsEtAps() {
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

	protected void construireMatriceAdjacence() {
		int n = aps[0];
		this.matriceAdjacence = new int[n + 1][n + 1];
		this.matriceAdjacence[0][0] = n;
		for (int i = 1; i <= n; i++) {
			int k = aps[i];

			while (fs[k] != 0) {
				int successeur = fs[k];
				matriceAdjacence[i][successeur] = 1;
				k++;
			}
		}
	}

	public abstract void afficher();

	public Sommet<T> trouverSommet(int id) {
		for (Sommet<T> sommet : this.sommets) {
			if (sommet.getId() == id) {
				return sommet;
			}
		}
		return null;
	}

	public List<Sommet<T>> getSommets() {
		return sommets;
	}

	public List<Arc<T>> getArcs() {
		return arcs;
	}

	public boolean isEstOriente() {
		return estOriente;
	}

	public boolean isEstPondere() {
		return estPondere;
	}
	
	public static String[] donneesGraphe(String fichier) {
	    Path path = Path.of(System.getProperty("user.dir"), "src", "ressources", fichier);
	    try {
	        String premiereLigne = Files.readAllLines(path).get(0);
	        return premiereLigne.split(";");
	    } catch (IOException e) {
	        throw new IllegalArgumentException("Impossible de lire le fichier : " + e.getMessage());
	    }
	}
	
	public static Graphe<?> typeGraphe(String [] donnees) {		

		Graphe<?> graphe;
		
		String type = donnees[0];
		String oriente = donnees[1];
		String pondere = donnees[2];
		int nbSom = Integer.parseInt(donnees[3]);

		if (type.equalsIgnoreCase("Integer")) {
		    if (oriente.equalsIgnoreCase("true")) {
		        if (pondere.equalsIgnoreCase("true")) {
		            graphe = new GrapheOrientePondere<Integer>(nbSom);
		        } else {
		            graphe = new GrapheOriente<Integer>(nbSom);
		        }
		    } else {
		        if (pondere.equalsIgnoreCase("true")) {
		            graphe = new GrapheNonOrientePondere<Integer>(nbSom);
		        } else {
		            graphe = new GrapheNonOriente<Integer>(nbSom);
		        }
		    }
		} else { // String
		    if (oriente.equalsIgnoreCase("true")) {
		        if (pondere.equalsIgnoreCase("true")) {
		            graphe = new GrapheOrientePondere<String>(nbSom);
		        } else {
		            graphe = new GrapheOriente<String>(nbSom);
		        }
		    } else {
		        if (pondere.equalsIgnoreCase("true")) {
		            graphe = new GrapheNonOrientePondere<String>(nbSom);
		        } else {
		            graphe = new GrapheNonOriente<String>(nbSom);
		        }
		    }
		}
		
		return graphe;
	}

	public void setMatriceAdjacence(int[][] nouvelleAdj) {
		this.matriceAdjacence = nouvelleAdj;
		
	}

}