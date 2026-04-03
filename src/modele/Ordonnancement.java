package modele;

import java.util.ArrayList;
import java.util.List;

public class Ordonnancement {
	private List<Tache> taches = new ArrayList<Tache>();
	private GrapheOriente<Tache> graphe;

	private void dfsCheminCritique(Tache courante, ArrayList<Tache> courant, ArrayList<ArrayList<Tache>> resultats) {
		boolean aSuccesseur = false;
		for (Sommet<Tache> sommetvoisin : graphe.getVoisins(courante)) {

			Tache voisin = sommetvoisin.getDonnee();

			if (voisin.getMarge() == 0) {
				aSuccesseur = true;
				courant.add(voisin);
				dfsCheminCritique(voisin, courant, resultats);
				courant.remove(courant.size() - 1);
			}
		}
		if (!aSuccesseur) {
			resultats.add(new ArrayList<>(courant));
		}

	}

	public Ordonnancement() {
		this.taches = new ArrayList<>();
		this.graphe = new GrapheOriente<>();
	}

	public void ajouterTache(String nom, int duree, int[] antecedents) {
		int numero = taches.size() + 1;
		Tache t = new Tache(numero, nom, duree, antecedents);
		taches.add(t);

		Sommet<Tache> s = new Sommet<>(t);
		graphe.ajouterSommet(t);

		for (int ant : antecedents) {
			Tache antecedent = taches.get(ant - 1);
			Arc<Tache> arc = new Arc<>(antecedent, t, antecedent.getDuree());
			graphe.ajouterArc(antecedent, t);
		}
	}

	public int[] calculerDatesTot() {
		int n = taches.size();
		int[] datesTot = new int[n + 1];

		int[] rangs = graphe.calculerRangs();

		ArrayList<Tache> tachesTriees = new ArrayList<>(taches);
		tachesTriees.sort((a, b) -> rangs[a.getNumero()] - rangs[b.getNumero()]);

		for (Tache t : tachesTriees) {
			int max = 0;

			for (int ant : t.getAntecedents()) {
				Tache tant = taches.get(ant - 1);
				int fin = datesTot[ant] + tant.getDuree();
				if (fin > max)
					max = fin;
			}
			datesTot[t.getNumero()] = max;
			t.setDateTot(max);
		}
		return datesTot;
	}

	public int[] calculerDatesTard() {
		int n = taches.size(); 
		int [] datesTot = calculerDatesTot(); 
		int [] datesTard = new int [n+1]; 
		
		int dureeProjet = 0; 
         for(Tache t: taches ) {
        	   int fin = datesTot[t.getNumero()] + t.getDuree();
        	    if( fin > dureeProjet)
        	    	dureeProjet = fin; 
         }
         
         for( int i = 1 ; i <=  n ; i++) {
        	    datesTard[i] = dureeProjet; 
         }
		
         int[] rangs = graphe.calculerRangs(); 
         ArrayList<Tache> tachesInversees = new ArrayList<>(taches);
         tachesInversees.sort((a, b) -> rangs[b.getNumero()] - rangs[a.getNumero()]);
         
         for ( Tache t : tachesInversees) {
        	 for(Sommet<Tache> sommetVoisin : graphe.getVoisins(t)) {
        		 Tache successeur = sommetVoisin.getDonnee(); 
        		  int tard = datesTard[successeur.getNumero()] - t.getDuree();
        		  if(tard < datesTard[t.getNumero()]) {
        			  datesTard[t.getNumero()] = tard; 
        		  }
        	 }
        	 t.setDateTard(datesTard[t.getNumero()]); 
        	 t.setMarge(datesTard[t.getNumero()] - datesTot[t.getNumero()]);
         }
          return datesTard;
	}

	public ArrayList<ArrayList<Tache>> getCheminsCritiques() {
		calculerDatesTard();


		int dureeProjet = 0;
		for (Tache t : taches)
			dureeProjet = Math.max(dureeProjet, t.getDateTot() + t.getDuree());

		ArrayList<ArrayList<Tache>> resultats = new ArrayList<>();
		ArrayList<Tache> cheminCourant = new ArrayList<>();

		for (Tache t : taches) {
			if (t.isCritique() && t.getAntecedents().length == 0) {
				cheminCourant.add(t);
				dfsCheminCritique(t, cheminCourant, resultats);
				cheminCourant.remove(cheminCourant.size() - 1);

			}
		}
		return resultats;
	}

	public void afficherGantt() {
		calculerDatesTard();

		int dureeProjet = 0;
		for (Tache t : taches)
			dureeProjet = Math.max(dureeProjet, t.getDateTot()+t.getDuree());

		System.out.println("DIAGRAMME DE GANTT");
		System.out.println("Tache\t\tDuree\tDate.tot\tDate.tard\tMarge\tGantt");
		System.out.println("-".repeat(60));

		for (Tache t : taches) {
			String barre = "";
			for (int i = 0; i < dureeProjet; i++) {
				if (i >= t.getDateTot() && i < t.getDateTot() + t.getDuree())
					barre += t.isCritique() ? "X" : "-";
				else
					barre += ".";
			}
			// affichage de la ligne
			System.out.println(t.getNom() + "\t\t" + t.getDuree() + "\t" + t.getDateTot() + "\t" + t.getDateTard()
					+ "\t" + t.getMarge() + "\t" + barre);
		}

		System.out.println("-".repeat(60));
		System.out.println("Duree totale du projet : " + dureeProjet);
		System.out.println("X = critique   - = non critique");

		// chemins critiques
		System.out.println("\nChemin(s) critique(s) :");
		for (ArrayList<Tache> chemin : getCheminsCritiques()) {
			String ligne = "";
			for (int i = 0; i < chemin.size(); i++) {
				ligne += chemin.get(i).getNom();
				if (i < chemin.size() - 1)
					ligne += " -> ";
			}
			System.out.println("  " + ligne);
		}

	}

}
