package main;

import java.util.List;
import java.util.Scanner;

import modele.Arc;
import modele.GestionnaireGraphe;
import modele.Graphe;
import modele.GrapheNonOriente;
import modele.GrapheNonOrientePondere;
import modele.GrapheOriente;
import modele.GrapheOrientePondere;
import modele.Sommet;

import vue.AffichageConsole;
import vue.SaisieClavier;
import vue.SaisieFichier;

/**
 * MainConsole — Interface console interactive du projet Graphes & Algos.
 *
 * Permet à l'utilisateur de :
 *   1) Saisir un graphe au clavier      (via SaisieClavier)
 *   2) Charger un graphe depuis fichier (via SaisieFichier de vue)
 *   3) Sauvegarder le graphe courant    (via SaisieFichier de vue)
 *   4) Afficher le graphe               (via AffichageConsole)
 *   5) Lancer un algorithme             (via AffichageConsole)
 */
public class MainConsole {

    private final Scanner scanner;
    private final SaisieClavier saisieClavier;

    public MainConsole() {
        this.scanner       = new Scanner(System.in);
        this.saisieClavier = new SaisieClavier();
    }

    public void lancer() {
        afficherBanniere();
        boolean continuer = true;

        while (continuer) {
            afficherMenuPrincipal();
            int choix = lireEntier("Votre choix : ");

            switch (choix) {
                case 1: menuSaisieClavier();   break;
                case 2: menuChargerFichier();  break;
                case 3: menuSauvegarder();     break;
                case 4: menuAfficher();        break;
                case 5: menuAlgorithmes();     break;
                case 0:
                    System.out.println("\nFermeture du programme. Au revoir !");
                    continuer = false;
                    break;
                default:
                    System.out.println("Choix invalide. Veuillez recommencer.\n");
            }
        }
    }

    private void afficherBanniere() {
        System.out.println();
        System.out.println("============================================================");
        System.out.println("       PROJET GRAPHES & ALGOS - INTERFACE CONSOLE");
        System.out.println("                  Licence 3 MIAGE / INFO");
        System.out.println("============================================================");
        System.out.println();
    }

    private void afficherMenuPrincipal() {
        Graphe<?> g = GestionnaireGraphe.getInstance().getGrapheCourant();
        String etat = (g == null)
            ? "(aucun graphe charge)"
            : "(graphe courant : " + decrireGraphe(g) + ")";

        System.out.println("------------------------------------------------------------");
        System.out.println("  MENU PRINCIPAL  " + etat);
        System.out.println("------------------------------------------------------------");
        System.out.println("  1. Saisir un nouveau graphe au clavier");
        System.out.println("  2. Charger un graphe depuis un fichier");
        System.out.println("  3. Sauvegarder le graphe courant dans un fichier");
        System.out.println("  4. Afficher le graphe courant");
        System.out.println("  5. Lancer un algorithme");
        System.out.println("  0. Quitter");
        System.out.println();
    }

    private void menuSaisieClavier() {
        System.out.println("\n--- SAISIE D'UN GRAPHE AU CLAVIER ---");
        try {
            Graphe<?> g = saisieClavier.construireGraphe();
            System.out.println("\nGraphe cree : " + decrireGraphe(g));
        } catch (Exception e) {
            System.out.println("Erreur lors de la saisie : " + e.getMessage());
        }
        System.out.println();
    }

    private void menuChargerFichier() {
        System.out.println("\n--- CHARGEMENT D'UN GRAPHE ---");

        List<String> fichiers = SaisieFichier.listerFichiersDisponibles();
        if (fichiers.isEmpty()) {
            System.out.println("Aucun fichier .txt trouve dans src/ressources/\n");
            return;
        }

        System.out.println("Fichiers disponibles :");
        for (int i = 0; i < fichiers.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + fichiers.get(i));
        }
        System.out.println("  0. Annuler\n");

        int choix = lireEntier("Numero du fichier a charger : ");
        if (choix == 0 || choix < 1 || choix > fichiers.size()) {
            System.out.println("Annule.\n");
            return;
        }

        SaisieFichier sf = new SaisieFichier(fichiers.get(choix - 1));
        Graphe<?> g = sf.construireGraphe();

        if (g != null) {
            System.out.println("Charge avec succes : " + decrireGraphe(g));
        } else {
            System.out.println("Echec du chargement.");
        }
        System.out.println();
    }

    private void menuSauvegarder() {
        Graphe<?> g = GestionnaireGraphe.getInstance().getGrapheCourant();
        if (g == null) {
            System.out.println("\nAucun graphe a sauvegarder.\n");
            return;
        }

        System.out.println("\n--- SAUVEGARDE DU GRAPHE ---");
        System.out.print("Nom du fichier (ex: monGraphe.txt) : ");
        String nom = scanner.nextLine().trim();
        if (nom.isEmpty()) {
            System.out.println("Nom vide. Annule.\n");
            return;
        }
        if (!nom.endsWith(".txt")) nom = nom + ".txt";

        new SaisieFichier(nom).ecrireGraphe(g);
        System.out.println();
    }

    private void menuAfficher() {
        Graphe<?> g = GestionnaireGraphe.getInstance().getGrapheCourant();
        if (g == null) {
            System.out.println("\nAucun graphe a afficher.\n");
            return;
        }

        AffichageConsole ac = new AffichageConsole(g);

        System.out.println();
        ac.afficherEntete();
        System.out.println();
        ac.afficherSommets();
        System.out.println();
        ac.afficherArcs();
        System.out.println();
        ac.afficherMatrice(g.getMatriceAdjacence());
        System.out.println();
        ac.afficherFS_APS(g.getFs(), g.getAps());
        System.out.println();
    }

    private void menuAlgorithmes() {
        Graphe<?> g = GestionnaireGraphe.getInstance().getGrapheCourant();
        if (g == null) {
            System.out.println("\nAucun graphe charge.\n");
            return;
        }

        System.out.println("\n------------------------------------------------------------");
        System.out.println("  MENU ALGORITHMES");
        System.out.println("------------------------------------------------------------");
        System.out.println("  1. Dijkstra (oriente pondere)");
        System.out.println("  2. Dantzig  (oriente pondere)");
        System.out.println("  3. Tarjan - composantes fortement connexes (oriente)");
        System.out.println("  4. Graphe reduit");
        System.out.println("  5. Bases du graphe");
        System.out.println("  6. Kruskal - arbre couvrant minimal (non oriente pondere)");
        System.out.println("  7. Codage de Prufer (arbre)");
        System.out.println("  8. Decodage de Prufer");
        System.out.println("  9. Rangs des sommets");
        System.out.println(" 10. Distances (matrice)");
        System.out.println(" 11. Est-il connexe ?");
        System.out.println(" 12. Est-il un arbre ?");
        System.out.println("  0. Retour");
        System.out.println();

        int choix = lireEntier("Votre choix : ");
        AffichageConsole ac = new AffichageConsole(g);

        try {
            switch (choix) {
                case 1:  algoDijkstra(g, ac);        break;
                case 2:  algoDantzig(g, ac);         break;
                case 3:  algoTarjan(g, ac);          break;
                case 4:  algoGrapheReduit(g, ac);    break;
                case 5:  algoBases(g, ac);           break;
                case 6:  algoKruskal(g, ac);         break;
                case 7:  algoCodagePrufer(g, ac);    break;
                //case 8:  algoDecodagePrufer(g, ac);  break;
                case 9:  algoRangs(g, ac);           break;
                case 10: algoDistances(g, ac);       break;
                case 11: algoEstConnexe(g, ac);      break;
                case 12: algoEstArbre(g, ac);        break;
                case 0:  return;
                default: System.out.println("Choix invalide.\n");
            }
        } catch (Exception e) {
            System.out.println("Erreur : " + e.getMessage());
        }
        System.out.println();
    }

    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void algoDijkstra(Graphe<?> g, AffichageConsole ac) {
        if (!(g instanceof GrapheOrientePondere)) {
            System.out.println("Dijkstra necessite un graphe oriente pondere."); return;
        }
        GrapheOrientePondere gp = (GrapheOrientePondere) g;
        if (!gp.verifierConditions()) {
            System.out.println("Dijkstra necessite des poids positifs."); return;
        }
        ac.afficherTitreAlgo("Dijkstra - Plus courts chemins");
        ac.afficherMatriceDouble(gp.dijkstraMatrice(), gp.getOrdre(), 100);
    }

    private void algoDantzig(Graphe<?> g, AffichageConsole ac) {
        if (!(g instanceof GrapheOrientePondere)) {
            System.out.println("Dantzig necessite un graphe oriente pondere."); return;
        }
        GrapheOrientePondere<?> gp = (GrapheOrientePondere<?>) g;
        double[][] c = gp.initialiserMatriceDistances();
        if (!gp.dantzig(c)) {
            System.out.println("Circuit de poids negatif detecte !"); return;
        }
        ac.afficherTitreAlgo("Dantzig - Plus courtes distances");
        ac.afficherMatriceDouble(c, gp.getOrdre(), Double.POSITIVE_INFINITY);
    }

    private void algoTarjan(Graphe<?> g, AffichageConsole ac) {
        if (!(g instanceof GrapheOriente)) {
            System.out.println("Tarjan necessite un graphe oriente."); return;
        }
        ac.afficherTitreAlgo("Tarjan - Composantes fortement connexes");
        ac.afficherListeComposantes(((GrapheOriente<?>) g).tarjan(), "CFC");
    }

    private void algoGrapheReduit(Graphe<?> g, AffichageConsole ac) {
        if (!(g instanceof GrapheOriente)) {
            System.out.println("Necessite un graphe oriente."); return;
        }
        ac.afficherTitreAlgo("Graphe reduit");
        ac.afficherResultatAlgo(((GrapheOriente<?>) g).grapheReduit().toString());
    }

    private void algoBases(Graphe<?> g, AffichageConsole ac) {
        if (!(g instanceof GrapheOriente)) {
            System.out.println("Necessite un graphe oriente."); return;
        }
        ac.afficherTitreAlgo("Bases du graphe");
        ac.afficherResultatAlgo("  " + ((GrapheOriente<?>) g).getBases());
    }

    private void algoKruskal(Graphe<?> g, AffichageConsole ac) {
        if (!(g instanceof GrapheNonOrientePondere)) {
            System.out.println("Kruskal necessite un graphe non oriente pondere."); return;
        }
        ac.afficherTitreAlgo("Kruskal - Arbre couvrant minimal");
        ac.afficherResultatAlgo(((GrapheNonOrientePondere<?>) g).kruskal().toString());
    }

    private void algoCodagePrufer(Graphe<?> g, AffichageConsole ac) throws Exception {
        if (!(g instanceof GrapheNonOriente)) {
            System.out.println("Prufer necessite un graphe non oriente."); return;
        }
        ac.afficherTitreAlgo("Codage de Prufer");
        ac.afficherTableauEntiers(((GrapheNonOriente<?>) g).codagePrufer(), 1, "Code");
    }   

    private void algoRangs(Graphe<?> g, AffichageConsole ac) {
        if (!(g instanceof GrapheOriente)) {
            System.out.println("Necessite un graphe oriente."); return;
        }
        GrapheOriente<?> go = (GrapheOriente<?>) g;
        go.calculerRangs();
        ac.afficherTitreAlgo("Rangs des sommets");
        for (int i = 1; i <= go.getOrdre(); i++) {
            Sommet<?> s = go.trouverSommet(i);
            if (s != null) {
                System.out.println("  " + s.getDonnee() + " : rang " + s.getRang());
            }
        }
    }

    private void algoDistances(Graphe<?> g, AffichageConsole ac) {
        if (!(g instanceof GrapheOriente)) {
            System.out.println("Necessite un graphe oriente."); return;
        }
        ac.afficherTitreAlgo("Matrice des distances");
        ac.afficherMatriceInt(((GrapheOriente<?>) g).calculerDistances());
    }

    private void algoEstConnexe(Graphe<?> g, AffichageConsole ac) {
        if (!(g instanceof GrapheNonOriente)) {
            System.out.println("Necessite un graphe non oriente."); return;
        }
        boolean c = ((GrapheNonOriente<?>) g).estConnexe();
        ac.afficherTitreAlgo("Connexite");
        System.out.println("  Le graphe est " + (c ? "CONNEXE" : "NON CONNEXE"));
    }

    private void algoEstArbre(Graphe<?> g, AffichageConsole ac) {
        if (!(g instanceof GrapheNonOriente)) {
            System.out.println("Necessite un graphe non oriente."); return;
        }
        boolean a = ((GrapheNonOriente<?>) g).estArbre();
        ac.afficherTitreAlgo("Test d'arbre");
        System.out.println("  Le graphe est " + (a ? "UN ARBRE" : "PAS UN ARBRE"));
    }
   
    private String decrireGraphe(Graphe<?> g) {
        StringBuilder sb = new StringBuilder();
        sb.append(g.isEstOriente() ? "Oriente" : "Non oriente");
        if (g.isEstPondere()) sb.append(" pondere");
        sb.append(", ").append(g.getSommets().size()).append(" sommets");
        sb.append(", ").append(g.getArcs().size()).append(" arcs");
        return sb.toString();
    }

    private int lireEntier(String prompt) {
        System.out.print(prompt);
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public static void main(String[] args) {
        new MainConsole().lancer();
    }
}