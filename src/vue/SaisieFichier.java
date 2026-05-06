package vue;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import modele.GestionnaireGraphe;
import modele.Graphe;
import modele.GrapheNonOriente;
import modele.GrapheNonOrientePondere;
import modele.GrapheOriente;
import modele.GrapheOrientePondere;

public class SaisieFichier {

    private String cheminFichier;

    public SaisieFichier(String cheminFichier) {
        this.cheminFichier = cheminFichier;
    }

    public Graphe<?> construireGraphe() {
        try {
            String[] donnees = Graphe.donneesGraphe(cheminFichier);

            if (donnees == null || donnees.length < 5) {
                System.err.println("Format invalide : entête manquante.");
                return null;
            }

            Graphe<?> graphe = Graphe.typeGraphe(donnees);
            if (graphe == null) {
                System.err.println("Type de graphe introuvable dans : " + cheminFichier);
                return null;
            }

            graphe.charger(cheminFichier);


            GestionnaireGraphe.getInstance().setGrapheCourant(graphe);

            System.out.println("Graphe charge depuis : " + cheminFichier);
            return graphe;

        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de " + cheminFichier
                + " : " + e.getMessage());
            return null;
        }
    }

    public void ecrireGraphe(Graphe<?> graphe) {
        if (graphe == null) {
            System.err.println("Aucun graphe a ecrire.");
            return;
        }

        try {
            graphe.sauvegarder(cheminFichier);
            System.out.println("Graphe sauvegarde dans : " + cheminFichier);
        } catch (Exception e) {
            System.err.println("Erreur sauvegarde : " + e.getMessage());
        }
    }


    public static List<String> listerFichiersDisponibles() {
        List<String> resultats = new ArrayList<>();
        Path dossier = Path.of(System.getProperty("user.dir"), "src", "ressources");

        try {
            if (Files.exists(dossier) && Files.isDirectory(dossier)) {
                Files.list(dossier)
                     .filter(p -> p.toString().toLowerCase().endsWith(".txt"))
                     .forEach(p -> resultats.add(p.getFileName().toString()));
            }
        } catch (IOException e) {
            System.err.println("Impossible de lister les fichiers : " + e.getMessage());
        }

        return resultats;
    }

    
    public void exporterTexteSimple(Graphe<?> graphe, String cheminCible) {
        if (graphe == null) return;

        Path path = Path.of(System.getProperty("user.dir"), "src", "ressources", cheminCible);

        try (BufferedWriter w = new BufferedWriter(new FileWriter(path.toString()))) {
            w.write("=== Graphe ===");                                                 w.newLine();
            w.write("Type      : " + (graphe.isEstOriente() ? "Oriente" : "Non oriente")
                  + (graphe.isEstPondere() ? " pondere" : " non pondere"));            w.newLine();
            w.write("Sommets   : " + graphe.getSommets().size());                      w.newLine();
            w.write("Arcs      : " + graphe.getArcs().size());                         w.newLine();
            w.newLine();

            w.write("--- Liste des sommets ---");                                      w.newLine();
            for (int i = 0; i < graphe.getSommets().size(); i++) {
                w.write("  " + (i+1) + " : " + graphe.getSommets().get(i).getDonnee());
                w.newLine();
            }

            w.newLine();
            w.write("--- Liste des arcs ---");                                         w.newLine();
            for (modele.Arc<?> a : graphe.getArcs()) {
                String fleche = graphe.isEstOriente() ? " -> " : " -- ";
                String poids  = graphe.isEstPondere() ? "  (poids=" + a.getPoids() + ")" : "";
                w.write("  " + a.getSource().getDonnee() + fleche
                      + a.getDestination().getDonnee() + poids);
                w.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erreur export : " + e.getMessage());
        }
    }

    public String getCheminFichier()              { return cheminFichier; }
    public void   setCheminFichier(String chemin) { this.cheminFichier = chemin; }
}  