package modele;

import java.util.ArrayList;
import java.util.List;

public class Ordonnancement extends GrapheOriente<Tache>{
	private List<Tache> taches = new ArrayList<Tache>();

	public Ordonnancement(int nbtaches) {
		super(nbtaches);
	}
	
	public Ordonnancement() {
		super(100);
	}
	
	
	private void dfsCheminCritique(Tache courante, ArrayList<Tache> courant, ArrayList<ArrayList<Tache>> resultats) {
		boolean aSuccesseur = false;
		for (Sommet<Tache> sommetvoisin :getVoisins(courante)) {

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
	
    @Override
	public void ajouterSommet(Tache t) {
    	     taches.add(t);
        super.ajouterSommet(t);
      

		for (int ant : t.getAntecedents()) {
			Tache antecedent = taches.get(ant - 1);
			ajouterArc(antecedent, t);
		}
	}

	public int[] calculerDatesTot() {
		int n = taches.size();
		int[] datesTot = new int[n + 1];

		calculerRangs();
		
		
	    ArrayList<Tache> tachesTriees = new ArrayList<>(taches);
	    tachesTriees.sort((a, b) -> {

	        int rangA = -1, rangB = -1;
	        for(Sommet<Tache> s : sommets) {
	            if(s.getDonnee().equals(a)) rangA = s.getRang();
	            if(s.getDonnee().equals(b)) rangB = s.getRang();
	        }
	        return rangA - rangB;
	    });
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
		
         calculerRangs(); 
         ArrayList<Tache> tachesInversees = new ArrayList<>(taches);
         tachesInversees.sort((a, b) -> {
             // on cherche le sommet correspondant à la tâche
             // et on lit son rang
             int rangA = -1, rangB = -1;
             for(Sommet<Tache> s : sommets) {
                 if(s.getDonnee().equals(a)) rangA = s.getRang();
                 if(s.getDonnee().equals(b)) rangB = s.getRang();
             }
             return rangB - rangA;
         });
         for ( Tache t : tachesInversees) {
        	 for(Sommet<Tache> sommetVoisin : getVoisins(t)) {
        		 Tache successeur = sommetVoisin.getDonnee(); 
        		  int tard = datesTard[successeur.getNumero()] - t.getDuree();
        		  if(tard < datesTard[t.getNumero()]) {
        			  datesTard[t.getNumero()] = tard; 
        		  }
        	 }
        	 t.setDateTard(datesTard[t.getNumero()]); 
         }
          return datesTard;
	}

	public ArrayList<ArrayList<Tache>> getCheminsCritiques() {
		calculerDatesTard();

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
