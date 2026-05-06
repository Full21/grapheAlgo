package modele;

import java.util.List;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/*un graphe orienté pondéree est un graphe avec des sommets , arcs et ds 
 *flèches des deux côtés, ainsi qu'un certain poids sur un arc entre 2 sommets
 */

public class GrapheOrientePondere<T> extends GrapheOriente<T> implements IPondere {
	private double[][] matricePoids;

	// si on connait le nombre de sommets dès le départ
	public GrapheOrientePondere() {
		this(100);
	};

	public GrapheOrientePondere(int n) {
		super(n);
		matricePoids = new double[n + 1][n + 1];
		this.estPondere = true;

		for (double[] ligne : matricePoids) {
			for (int i = 0; i < ligne.length; i++)
				ligne[i] = MAX_POIDS;
		}
	}

	@Override
	public double getPoids(int sommet1, int sommet2) {
		return matricePoids[sommet1][sommet2];
	}

	@Override
	public void setPoids(int sommet1, int sommet2, double poids) {
		matricePoids[sommet1][sommet2] = poids;
	}

	@Override
	public double[][] getMatricePoids() {
		return matricePoids;
	}

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
	public void ajouterArc(Sommet<T> sommet1, Sommet<T> sommet2, double poids) {
		super.ajouterArc(sommet1, sommet2, poids);
		this.matricePoids[sommet1.id][sommet2.id] = poids;
	}

	public boolean verifierConditions() {
		for (int i = 0; i < matricePoids.length; i++) {
			for (int j = 0; j < matricePoids[i].length; j++) {
				if (matricePoids[i][j] < 0) {
					return false;
				}
			}
		}
		return true;
	}

	public double[][] dijkstraMatrice() {
		if (!verifierConditions()) {
			throw new IllegalStateException("Dijkstra fonctionne uniquement avec des poids positifs");
		}

		int n = getOrdre();
		double[][] matrice = new double[n + 1][n + 1];
		double[] d = new double[n + 1];
		int[] pred = new int[n + 1];

		for (int source = 1; source <= n; source++) {

			try {
				dijkstra(this.fs, this.aps, source, d, pred);
			} catch (Exception e) {
				e.printStackTrace();
			}

			for (int destination = 1; destination <= n; destination++) {
				matrice[source][destination] = d[destination];
			}

			d = new double[n + 1];
			pred = new int[n + 1];
		}

		return matrice;
	}

	public void dijkstra(int[] fs, int[] aps, int s, double[] d, int[] pred) throws Exception {
		if (!verifierConditions()) {
			throw new IllegalStateException("L'algo de dijkstra fonctionne uniquement avec des poids positifs");
		}

		int nbSommets = aps[0];

		for (int i = 1; i <= nbSommets; i++) {
			d[i] = (this.matriceAdjacence[s][i] == 1) ? matricePoids[s][i] : MAX_POIDS;
			pred[i] = s;
		}
		d[s] = 0;

		boolean[] ins = new boolean[nbSommets + 1];
		for (int i = 1; i <= nbSommets; i++) {
			ins[i] = true;
		}

		d[s] = 0;
		ins[s] = false;

		int j = 0;
		int cpt = nbSommets - 1;

		while (cpt > 0) {
			double min = MAX_POIDS;
			for (int i = 1; i <= nbSommets; i++) {
				if (ins[i] && d[i] < min) {
					min = d[i];
					j = i;
				}
			}

			if (min == MAX_POIDS)
				return;

			ins[j] = false;

			int h = 0;
			for (int k = aps[j]; (h = fs[k]) != 0; k++) {
				if (ins[h]) {
					double v = d[j] + matricePoids[j][h];
					if (v < d[h]) {
						d[h] = v;
						pred[h] = j;
					}
				}
			}

			cpt--;
		}
	}

	// Méthode pour une initialisation parfaite de la matrice dantzig pour
	// l'utilisateur

	public double[][] initialiserMatriceDistances() {
		int nombreSommet = getOrdre();
		double[][] c = new double[nombreSommet + 1][nombreSommet + 1];

		for (int i = 1; i <= nombreSommet; i++) {
			for (int j = 1; j <= nombreSommet; j++) {
				if (i == j) {
					c[i][j] = 0;
				} else if (this.matriceAdjacence[i][j] == 1) {
					c[i][j] = this.matricePoids[i][j];
				} else {
					c[i][j] = Double.POSITIVE_INFINITY;
				}
			}
		}
		return c;
	}

	public boolean dantzig(double[][] c) {
		int nombreSommet = getOrdre();

		for (int k = 1; k < nombreSommet; k++) {
			for (int i = 1; i <= k; i++) {
				for (int j = 1; j <= k; j++) {
					// Mise à jour de c[i][k+1]
					double x = c[i][j] + c[j][k + 1];
					if (x < c[i][k + 1]) {
						c[i][k + 1] = x;
					}
					// Mise à jour de c[k+1][i]
					double y = c[k + 1][j] + c[j][i];
					if (y < c[k + 1][i]) {
						c[k + 1][i] = y;
					}
				}
				// Détection d'un circuit de poids négatif
				if (c[i][k + 1] + c[k + 1][i] < 0) {
					System.out.println("Circuit de poids négatif détecté !");
					return false;
				}
			}
			// Mise à jour des distances entre anciens sommets
			for (int i = 1; i <= k; i++) {
				for (int j = 1; j <= k; j++) {
					double x = c[i][k + 1] + c[k + 1][j];
					if (x < c[i][j]) {
						c[i][j] = x;
					}
				}
			}
		}

		return true;
	}

	// Affichage complet du graphe
	@Override
	public void afficher() {
		for (int i = 0; i < matricePoids.length; i++) {
			System.out.print("Sommet " + i + "->");
			for (int j = 0; j < matricePoids[i].length; j++) {
				System.out.print(j + "(" + matricePoids[i][j] + ")");
			}
			System.out.println();
		}
	}

	@Override
	public void sauvegarder(String fichier) {

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
			writer.append("\nMATRICE DES POIDS\n");
			for (double[] ligne : this.matricePoids) {
				for (double poids : ligne)
					writer.append(poids + ";");
				writer.append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void charger(String fichier) {

		super.charger(fichier);
		int n = this.getOrdre();

		Path path = Path.of(System.getProperty("user.dir"), "src", "ressources", fichier);
		try {
			List<String> lignes = Files.readAllLines(path);

			// Chercher "MATRICE DES POIDS" 
			int debutPoids = -1;
			for (int i = 0; i < lignes.size(); i++) {
				if (lignes.get(i).equalsIgnoreCase("MATRICE DES POIDS") && debutPoids == -1) {
					debutPoids = i + 1;
				}
			}

			this.matricePoids = new double[n + 1][n + 1];

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

	// Affichage bref du graphe
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Sommets : ");
		sb.append(getOrdre());
		sb.append(", arcs : ");
		sb.append(arcs.size());
		return sb.toString();

	}

}