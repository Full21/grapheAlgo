package vue;

import java.util.Scanner;

import modele.Arc;
import modele.GestionnaireGraphe;
import modele.Graphe;
import modele.GrapheNonOriente;
import modele.GrapheNonOrientePondere;
import modele.GrapheOriente;
import modele.GrapheOrientePondere;
import modele.Sommet;

/**
 * COUCHE 7 — SAISIE CLAVIER
 *
 * Utilitaire de saisie d'un graphe au clavier (mode console).
 * Gère :
 *   - Les 4 types de graphes (orienté/non-orienté × pondéré/non-pondéré)
 *   - Les sommets en Integer ou String
 *   - La saisie des poids pour les graphes pondérés
 *
 * À la fin, le graphe construit est enregistré dans GestionnaireGraphe.
 */
public class SaisieClavier {

    private Scanner scanner;

    public SaisieClavier() {
        this.scanner = new Scanner(System.in);
    }

    public int lireNbSommets() {
        System.out.print("Nombre de sommets : ");
        int n = scanner.nextInt();
        scanner.nextLine();
        return n;
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
        double p = scanner.nextDouble();
        scanner.nextLine();
        return p;
    }

    public String lireTypeGraphe() {
        System.out.println("Type de graphe :");
        System.out.println("  1. Oriente Simple");
        System.out.println("  2. Oriente Pondere");
        System.out.println("  3. Non Oriente Simple");
        System.out.println("  4. Non Oriente Pondere");
        System.out.print("Choix : ");
        int choix = scanner.nextInt();
        scanner.nextLine();
        switch (choix) {
            case 2:  return "OrientePondere";
            case 3:  return "NonOrienteSimple";
            case 4:  return "NonOrientePondere";
            default: return "OrienteSimple";
        }
    }

    /** Choix : sommets Integer (auto-numerotes) ou String (libres). */
    public String lireTypeDonnees() {
        System.out.println("Type des donnees des sommets :");
        System.out.println("  1. Integer (1, 2, 3, ...)");
        System.out.println("  2. String  (noms libres)");
        System.out.print("Choix : ");
        int choix = scanner.nextInt();
        scanner.nextLine();
        return (choix == 2) ? "String" : "Integer";
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Graphe<?> construireGraphe() {
        String type  = lireTypeGraphe();
        String donnees = lireTypeDonnees();
        int    n     = lireNbSommets();

        // Instancier le bon type de graphe
        Graphe g;
        switch (type) {
            case "OrientePondere":
                g = (donnees.equals("Integer"))
                    ? new GrapheOrientePondere<Integer>(n)
                    : new GrapheOrientePondere<String>(n);
                break;
            case "NonOrienteSimple":
                g = (donnees.equals("Integer"))
                    ? new GrapheNonOriente<Integer>(n)
                    : new GrapheNonOriente<String>(n);
                break;
            case "NonOrientePondere":
                g = (donnees.equals("Integer"))
                    ? new GrapheNonOrientePondere<Integer>(n)
                    : new GrapheNonOrientePondere<String>(n);
                break;
            default:
                g = (donnees.equals("Integer"))
                    ? new GrapheOriente<Integer>(n)
                    : new GrapheOriente<String>(n);
        }

        // -------- Saisie des sommets --------
        System.out.println("\nSaisie des " + n + " sommets :");
        for (int i = 0; i < n; i++) {
            if (donnees.equals("Integer")) {
                // Auto-numerotation 1..n
                g.ajouterSommet(Integer.valueOf(i + 1));
                System.out.println("  Sommet " + (i + 1) + " : " + (i + 1) + " (auto)");
            } else {
                System.out.print("  Sommet " + (i + 1) + " (nom) : ");
                String nom = scanner.nextLine().trim();
                if (nom.isEmpty()) nom = "S" + (i + 1);
                g.ajouterSommet(nom);
            }
        }

        // -------- Saisie des arcs --------
        boolean estPondere = type.contains("Pondere");
        boolean estOriente = !type.contains("NonOriente");

        System.out.print("\nNombre d'" + (estOriente ? "arcs" : "aretes") + " : ");
        int m = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < m; i++) {
            String mot = estOriente ? "Arc" : "Arete";
            System.out.println("  " + mot + " " + (i + 1) + " :");

            Object src, dest;
            if (donnees.equals("Integer")) {
                System.out.print("    Source (numero du sommet, 1.." + n + ") : ");
                src = Integer.valueOf(scanner.nextInt());
                System.out.print("    Destination : ");
                dest = Integer.valueOf(scanner.nextInt());
                scanner.nextLine();
            } else {
                System.out.print("    Source (nom) : ");
                src = scanner.nextLine().trim();
                System.out.print("    Destination (nom) : ");
                dest = scanner.nextLine().trim();
            }

            if (estPondere) {
                System.out.print("    Poids : ");
                double poids = scanner.nextDouble();
                scanner.nextLine();
                g.ajouterArc(src, dest, poids);
            } else {
                g.ajouterArc(src, dest);
            }
        }

        // Enregistrer comme graphe courant
        GestionnaireGraphe.getInstance().setGrapheCourant(g);
        return g;
    }
}