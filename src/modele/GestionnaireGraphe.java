package modele;

import java.util.Stack;


/**
 * GestionnaireGraphe — Pattern Singleton
 *
 * Gère le graphe courant et un historique (pile) des graphes précédents.
 * Une seule instance existe dans toute l'application.
 *
 * Responsabilités :
 *  - Conserver le graphe courant (grapheCourant)
 *  - Empiler l'ancien graphe dans historique avant tout changement
 *  - Déléguer sauvegarder/charger à ISauvegardable du graphe courant
 */
public class GestionnaireGraphe {

    // -------------------------------------------------------------------------
    // Singleton — instance unique statique
    // -------------------------------------------------------------------------
    private static GestionnaireGraphe instance;

    private Graphe<?>       grapheCourant;
    private Stack<Graphe<?>> historique;


    // Constructeur privé — empêche toute instanciation externe
    private GestionnaireGraphe() {
        this.grapheCourant = null;
        this.historique    = new Stack<>();
    }


    // -------------------------------------------------------------------------
    // getInstance — point d'accès unique à l'instance (lazy initialization)
    // -------------------------------------------------------------------------
    public static GestionnaireGraphe getInstance() {
        if (instance == null) {
            instance = new GestionnaireGraphe();
        }
        return instance;
    }


    // -------------------------------------------------------------------------
    // setGrapheCourant — remplace le graphe courant
    // L'ancien graphe est empilé dans l'historique avant d'être remplacé
    // -------------------------------------------------------------------------
    public void setGrapheCourant(Graphe<?> graphe) {
        if (this.grapheCourant != null) {
            this.historique.push(this.grapheCourant);
        }
        this.grapheCourant = graphe;
    }


    // -------------------------------------------------------------------------
    // getGrapheCourant — retourne le graphe courant
    // -------------------------------------------------------------------------
    public Graphe<?> getGrapheCourant() {
        return this.grapheCourant;
    }


    // -------------------------------------------------------------------------
    // sauvegarder — délègue à ISauvegardable du graphe courant
    // (Graphe implémente ISauvegardable — méthode sauvegarder(String))
    // -------------------------------------------------------------------------
    public void sauvegarder(String fichier) {
        if (this.grapheCourant == null) {
            System.out.println("Aucun graphe courant à sauvegarder.");
            return;
        }
        this.grapheCourant.sauvegarder(fichier);
    }


    // -------------------------------------------------------------------------
    // charger — délègue à ISauvegardable du graphe courant
    // Retourne le graphe chargé (le graphe courant mis à jour)
    // -------------------------------------------------------------------------
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