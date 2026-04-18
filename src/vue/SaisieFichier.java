package vue;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import modele.GestionnaireGraphe;
import modele.Graphe;
import modele.GrapheNonOriente;
import modele.GrapheNonOrientePondere;
import modele.GrapheOriente;
import modele.GrapheOrientePondere;

import java.util.ArrayList;

/**
 * SaisieFichier — Utility
 *
 * Lit et écrit des graphes depuis/vers un fichier.
 * Utilise le contrat ISauvegardable (déjà implémenté dans Graphe).
 * Appelle GestionnaireGraphe.setGrapheCourant(construireGraphe())
 * une fois le graphe construit.
 *
 * Format de fichier attendu (même format que Graphe.sauvegarder) :
 *   Ligne 0  : nbSommets;nbArcs
 *   Ligne 1  : TYPE          (ORIENTE | NON_ORIENTE | ORIENTE_PONDERE | NON_ORIENTE_PONDERE)
 *   Ligne 2  : FS
 *   Ligne 3  : valeurs fs séparées par ;
 *   Ligne 4  : APS
 *   Ligne 5  : valeurs aps séparées par ;
 *   Ligne 6  : MATRICE D'ADJACENCE
 *   Lignes 7+: lignes de la matrice
 */
public class SaisieFichier {

    private String cheminFichier;
    private String formatFichier; // "standard" — format interne du projet

    // Contenu brut lu depuis le fichier
    private List<String> lignes;

    public SaisieFichier(String cheminFichier) {
        this.cheminFichier = cheminFichier;
        this.formatFichier = "standard";
    }

    public SaisieFichier(String cheminFichier, String formatFichier) {
        this.cheminFichier = cheminFichier;
        this.formatFichier = formatFichier;
    }


    // -------------------------------------------------------------------------
    // lireFichier — charge toutes les lignes du fichier en mémoire
    // -------------------------------------------------------------------------
    public void lireFichier(String chemin) {
        this.cheminFichier = chemin;
        Path path = Path.of(System.getProperty("user.dir"), "src", "ressources", chemin);
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toString()))) {
        	this.lignes = new ArrayList<>();
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                this.lignes.add(ligne);
            }
        } catch (IOException e) {
            System.err.println("Erreur lecture fichier : " + e.getMessage());
            this.lignes = null;
        }
    }


    // -------------------------------------------------------------------------
    // parseLigne — découpe une ligne en tokens séparés par ";"
    // -------------------------------------------------------------------------
    public String[] parseLigne(String ligne) {
        if (ligne == null) return new String[0];
        return ligne.split(";");
    }


    // -------------------------------------------------------------------------
    // validerFormat — vérifie que le fichier chargé est au bon format
    // -------------------------------------------------------------------------
    public boolean validerFormat() {
        if (lignes == null || lignes.size() < 7) return false;

        // Ligne 0 : doit contenir deux entiers séparés par ";"
        String[] entete = parseLigne(lignes.get(0));
        if (entete.length < 2) return false;
        try {
            Integer.parseInt(entete[0].trim());
            Integer.parseInt(entete[1].trim());
        } catch (NumberFormatException e) {
            return false;
        }

        // Ligne 1 : type du graphe connu
        String type = lignes.get(1).trim();
        if (!type.equals("ORIENTE") && !type.equals("NON_ORIENTE")
                && !type.equals("ORIENTE_PONDERE") && !type.equals("NON_ORIENTE_PONDERE")) {
            return false;
        }

        // Ligne 2 : marqueur "FS"
        if (!lignes.get(2).trim().equals("FS")) return false;

        // Ligne 4 : marqueur "APS"
        if (!lignes.get(4).trim().equals("APS")) return false;

        return true;
    }


    // -------------------------------------------------------------------------
    // construireGraphe — crée le bon type de graphe d'après le fichier
    // et appelle GestionnaireGraphe.setGrapheCourant()
    // -------------------------------------------------------------------------
    public Graphe<?> construireGraphe() {
        // Charger si pas déjà fait
        if (lignes == null) {
            lireFichier(cheminFichier);
        }

        if (!validerFormat()) {
            System.err.println("Format de fichier invalide : " + cheminFichier);
            return null;
        }

        String[] entete = parseLigne(lignes.get(0));
        int nbSommets   = Integer.parseInt(entete[0].trim());
        String type     = lignes.get(1).trim();

        // Instancier le bon type de graphe
        Graphe<?> graphe = creerGrapheParType(type, nbSommets);
        if (graphe == null) return null;

        // Déléguer le chargement à ISauvegardable (déjà implémenté dans Graphe)
        // On passe le chemin tel quel — Graphe.charger() reconstruit fs, aps et la matrice
        graphe.charger(cheminFichier);

        // Enregistrer comme graphe courant
        GestionnaireGraphe.getInstance().setGrapheCourant(graphe);

        return graphe;
    }


    // -------------------------------------------------------------------------
    // ecrireGraphe — sérialise un graphe dans le fichier
    // Délègue à ISauvegardable.sauvegarder()
    // Écrit aussi la ligne TYPE en tête de fichier
    // -------------------------------------------------------------------------
    public void ecrireGraphe(Graphe<?> graphe) {
        if (graphe == null) {
            System.err.println("Aucun graphe à écrire.");
            return;
        }

        // D'abord sauvegarder via ISauvegardable (écrit fs, aps, matrice)
        graphe.sauvegarder(cheminFichier);

        // Puis insérer la ligne TYPE juste après la première ligne (nbSom;nbArc)
        Path path = Path.of(System.getProperty("user.dir"), "src", "ressources", cheminFichier);
        try {
            List<String> contenu = Files.readAllLines(path);
            String typeStr = determinerType(graphe);

            // Insérer après la ligne 0
            contenu.add(1, typeStr);

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(path.toString()))) {
                for (String ligne : contenu) {
                    writer.write(ligne);
                    writer.newLine();
                }
            }
            System.out.println("Graphe ecrit dans : " + cheminFichier);

        } catch (IOException e) {
            System.err.println("Erreur ecriture fichier : " + e.getMessage());
        }
    }


    // -------------------------------------------------------------------------
    // Méthodes privées utilitaires
    // -------------------------------------------------------------------------

    private Graphe<?> creerGrapheParType(String type, int nbSommets) {
        switch (type) {
            case "ORIENTE":
                return new GrapheOriente<Integer>(nbSommets);
            case "NON_ORIENTE":
                return new GrapheNonOriente<Integer>(nbSommets);
            case "ORIENTE_PONDERE":
                return new GrapheOrientePondere<Integer>(nbSommets);
            case "NON_ORIENTE_PONDERE":
                return new GrapheNonOrientePondere<Integer>(nbSommets);
            default:
                System.err.println("Type de graphe inconnu : " + type);
                return null;
        }
    }

    private String determinerType(Graphe<?> graphe) {
        if (graphe instanceof GrapheNonOrientePondere) return "NON_ORIENTE_PONDERE";
        if (graphe instanceof GrapheNonOriente)        return "NON_ORIENTE";
        if (graphe instanceof GrapheOrientePondere)    return "ORIENTE_PONDERE";
        if (graphe instanceof GrapheOriente)           return "ORIENTE";
        return "INCONNU";
    }


    // -------------------------------------------------------------------------
    // Getters / Setters
    // -------------------------------------------------------------------------
    public String getCheminFichier() { return cheminFichier; }
    public void   setCheminFichier(String c) { this.cheminFichier = c; }

    public String getFormatFichier() { return formatFichier; }
    public void   setFormatFichier(String f) { this.formatFichier = f; }
}
