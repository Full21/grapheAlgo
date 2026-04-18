package vue;

import java.util.Scanner;

/**
 * COUCHE 7 — SAISIE
 * Utilitaire de saisie d'un graphe depuis le clavier.
 * Appelle GestionnaireGraphe.setGrapheCourant(construireGraphe())
 */
public class SaisieClavier {

    private Scanner scanner;

    public SaisieClavier() {
        this.scanner = new Scanner(System.in);
    }

    public int lireNbSommets() {
        System.out.print("Nombre de sommets : ");
        return scanner.nextInt();
    }

    public Sommet<?> lireSommet() {
        System.out.print("Nom du sommet : ");
        String nom = scanner.next();
        return new Sommet<>(nom);
    }

    public Arc<?> lireArc() {
        System.out.print("Source : ");
        String src = scanner.next();
        System.out.print("Destination : ");
        String dest = scanner.next();
        return new Arc<>(new Sommet<>(src), new Sommet<>(dest));
    }

    public double lirePoids() {
        System.out.print("Poids : ");
        return scanner.nextDouble();
    }

    public String lireTypeGraphe() {
        System.out.println("Type de graphe :");
        System.out.println("  1. Orienté Simple");
        System.out.println("  2. Orienté Pondéré");
        System.out.println("  3. Non Orienté Simple");
        System.out.println("  4. Non Orienté Pondéré");
        System.out.print("Choix : ");
        int choix = scanner.nextInt();
        switch (choix) {
            case 2:  return "OrientePondere";
            case 3:  return "NonOrienteSimple";
            case 4:  return "NonOrientePondere";
            default: return "OrienteSimple";
        }
    }

    public Graphe<?> construireGraphe() {
        String type  = lireTypeGraphe();
        int    n     = lireNbSommets();

        Graphe<String> g;
        switch (type) {
            case "OrientePondere":   g = new GrapheOrientePondere<>(n);   break;
            case "NonOrienteSimple": g = new GrapheNonOriente<>(n);       break;
            case "NonOrientePondere":g = new GrapheNonOrientePondere<>(n);break;
            default:                 g = new GrapheOriente<>(n);
        }

        System.out.println("Saisir les " + n + " sommets :");
        scanner.nextLine(); // vider le buffer
        for (int i = 0; i < n; i++) {
            System.out.print("  Sommet " + (i + 1) + " : ");
            g.ajouterSommet(scanner.nextLine().trim());
        }

        System.out.print("Nombre d'arcs : ");
        int m = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < m; i++) {
            System.out.print("  Arc " + (i + 1) + " source : ");
            String src = scanner.nextLine().trim();
            System.out.print("  Arc " + (i + 1) + " destination : ");
            String dest = scanner.nextLine().trim();
            g.ajouterArc(src, dest);
        }

        GestionnaireGraphe.getInstance().setGrapheCourant(g);
        return g;
    }
}
