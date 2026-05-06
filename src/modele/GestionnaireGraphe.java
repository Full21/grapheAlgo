package modele;

import java.util.Stack;


public class GestionnaireGraphe {

  
    private static GestionnaireGraphe instance;

    private Graphe<?>       grapheCourant;
    private Stack<Graphe<?>> historique;


    private GestionnaireGraphe() {
        this.grapheCourant = null;
        this.historique    = new Stack<>();
    }

    public static GestionnaireGraphe getInstance() {
        if (instance == null) {
            instance = new GestionnaireGraphe();
        }
        return instance;
    }

    public void setGrapheCourant(Graphe<?> graphe) {
        if (this.grapheCourant != null) {
            this.historique.push(this.grapheCourant);
        }
        this.grapheCourant = graphe;
    }

    public Graphe<?> getGrapheCourant() {
        return this.grapheCourant;
    }

    public void sauvegarder(String fichier) {
        if (this.grapheCourant == null) {
            System.out.println("Aucun graphe courant à sauvegarder.");
            return;
        }
        this.grapheCourant.sauvegarder(fichier);
    }


    public Graphe<?> charger(String fichier) {
        if (this.grapheCourant == null) {
            System.out.println("Aucun graphe courant. Impossible de charger.");
            return null;
        }
        // Empiler l'état avant chargement
        this.historique.push(this.grapheCourant);
        this.grapheCourant.charger(fichier);
        return this.grapheCourant;
    }
}