package vue;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import modele.Ordonnancement;
import modele.Tache;

/**
 * PanneauGantt — Affiche le diagramme de Gantt graphiquement.
 * Les tâches critiques sont en ROUGE, les non critiques en BLEU.
 */
public class PanneauGantt extends JPanel {

    // Couleurs
    private static final Color COULEUR_CRITIQUE     = new Color(220, 50, 50);
    private static final Color COULEUR_NON_CRITIQUE = new Color(50, 120, 220);
    private static final Color COULEUR_FOND_LIGNE   = new Color(245, 245, 245);

    // Dimensions
    private static final int MARGE_GAUCHE  = 150;
    private static final int MARGE_HAUT    = 50;
    private static final int MARGE_BAS     = 80;
    private static final int HAUTEUR_BARRE = 30;
    private static final int ESPACEMENT    = 10;

    // Données
    private Ordonnancement ordonnancement;
    private java.util.List<Tache> taches;
    private int dureeProjet;
    private ArrayList<ArrayList<Tache>> cheminsCritiques;

    public PanneauGantt() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(800, 400));
    }

    /**
     * Charge un ordonnancement et recalcule tout
     */
    public void setOrdonnancement(Ordonnancement o) {
        this.ordonnancement = o;
        calculerDonnees();
        int hauteurNecessaire = MARGE_HAUT
            + taches.size() * (HAUTEUR_BARRE + ESPACEMENT)
            + MARGE_BAS + 50;
        setPreferredSize(new Dimension(800, hauteurNecessaire));
        revalidate();
        repaint();
    }

    private void calculerDonnees() {
        if (ordonnancement == null) return;
        ordonnancement.calculerDatesTard();
        this.cheminsCritiques = ordonnancement.getCheminsCritiques();
        this.taches           = ordonnancement.getTaches();
        this.dureeProjet      = 0;
        for (Tache t : taches) {
            int fin = t.getDateTot() + t.getDuree();
            if (fin > dureeProjet) dureeProjet = fin;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (ordonnancement == null || taches == null || taches.isEmpty()) {
            g.setColor(Color.GRAY);
            g.setFont(new Font("Arial", Font.ITALIC, 14));
            g.drawString("Aucun ordonnancement chargé.", 20, 30);
            return;
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int largeurDisponible  = getWidth() - MARGE_GAUCHE - 20;
        double pixelsParUnite  = (double) largeurDisponible / dureeProjet;

        dessinerEnTete(g2, pixelsParUnite);
        dessinerTaches(g2, pixelsParUnite);
        dessinerCheminsCritiques(g2);
        dessinerLegende(g2);
    }

    private void dessinerEnTete(Graphics2D g, double pixelsParUnite) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.drawString("Tâche", 5, MARGE_HAUT - 10);

        g.setFont(new Font("Arial", Font.PLAIN, 11));
        for (int t = 0; t <= dureeProjet; t++) {
            int x = MARGE_GAUCHE + (int)(t * pixelsParUnite);
            g.setColor(Color.LIGHT_GRAY);
            g.drawLine(x, MARGE_HAUT, x, MARGE_HAUT + taches.size() * (HAUTEUR_BARRE + ESPACEMENT));
            g.setColor(Color.BLACK);
            g.drawString(String.valueOf(t), x - 3, MARGE_HAUT - 5);
        }

        g.setColor(Color.BLACK);
        g.drawLine(MARGE_GAUCHE, MARGE_HAUT - 20, getWidth() - 10, MARGE_HAUT - 20);
    }

    private void dessinerTaches(Graphics2D g, double pixelsParUnite) {
        for (int idx = 0; idx < taches.size(); idx++) {
            Tache t = taches.get(idx);
            int y   = MARGE_HAUT + idx * (HAUTEUR_BARRE + ESPACEMENT);

            // Fond de ligne alternée
            if (idx % 2 == 0) {
                g.setColor(COULEUR_FOND_LIGNE);
                g.fillRect(MARGE_GAUCHE, y, getWidth() - MARGE_GAUCHE - 10, HAUTEUR_BARRE);
            }

            // Nom de la tâche
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 12));
            g.drawString(t.getNom() + " (d=" + t.getDuree() + ")", 5, y + HAUTEUR_BARRE - 8);

            // Barre de la tâche
            int xDebut  = MARGE_GAUCHE + (int)(t.getDateTot() * pixelsParUnite);
            int largeur = (int)(t.getDuree() * pixelsParUnite);

            Color couleur = t.isCritique() ? COULEUR_CRITIQUE : COULEUR_NON_CRITIQUE;
            g.setColor(couleur);
            g.fillRoundRect(xDebut, y + 2, largeur, HAUTEUR_BARRE - 4, 8, 8);

            g.setColor(couleur.darker());
            g.drawRoundRect(xDebut, y + 2, largeur, HAUTEUR_BARRE - 4, 8, 8);

            // Texte dans la barre
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 10));
            String infos = t.getDateTot() + " → " + (t.getDateTot() + t.getDuree());
            if (largeur > 40) {
                g.drawString(infos, xDebut + 4, y + HAUTEUR_BARRE - 8);
            }

            // Marge flottante (trait pointillé)
            if (!t.isCritique() && t.getMarge() > 0) {
                int xMarge      = MARGE_GAUCHE + (int)(t.getDateTard() * pixelsParUnite);
                int largeurMarge = (int)(t.getDuree() * pixelsParUnite);
                g.setColor(new Color(50, 120, 220, 80));
                g.fillRoundRect(xMarge, y + 2, largeurMarge, HAUTEUR_BARRE - 4, 8, 8);

                g.setColor(new Color(50, 120, 220, 150));
                float[] dash = {4f, 4f};
                g.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER, 10f, dash, 0f));
                g.drawLine(xDebut + largeur, y + HAUTEUR_BARRE / 2,
                           xMarge,           y + HAUTEUR_BARRE / 2);
                g.setStroke(new BasicStroke(1f));
            }

            // Dates tard et marge
            g.setColor(Color.DARK_GRAY);
            g.setFont(new Font("Arial", Font.PLAIN, 9));
            g.drawString("tard=" + t.getDateTard(), xDebut + largeur + 3, y + 12);
            g.drawString("marge=" + t.getMarge(),   xDebut + largeur + 3, y + 22);
        }
    }

    private void dessinerCheminsCritiques(Graphics2D g) {
        if (cheminsCritiques == null || cheminsCritiques.isEmpty()) return;

        int yBase = MARGE_HAUT + taches.size() * (HAUTEUR_BARRE + ESPACEMENT) + 20;

        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        g.drawString("Chemin(s) critique(s) :", 5, yBase);

        g.setFont(new Font("Arial", Font.PLAIN, 11));
        for (int i = 0; i < cheminsCritiques.size(); i++) {
            ArrayList<Tache> chemin = cheminsCritiques.get(i);
            StringBuilder sb = new StringBuilder("  → ");
            for (int j = 0; j < chemin.size(); j++) {
                sb.append(chemin.get(j).getNom());
                if (j < chemin.size() - 1) sb.append(" → ");
            }
            g.setColor(COULEUR_CRITIQUE);
            g.drawString(sb.toString(), 5, yBase + 20 + i * 18);
        }
    }

    private void dessinerLegende(Graphics2D g) {
        int xLegende = getWidth() - 200;
        int yLegende = MARGE_HAUT + taches.size() * (HAUTEUR_BARRE + ESPACEMENT) + 15;

        g.setFont(new Font("Arial", Font.BOLD, 11));
        g.setColor(Color.BLACK);
        g.drawString("Légende :", xLegende, yLegende);

        g.setColor(COULEUR_CRITIQUE);
        g.fillRoundRect(xLegende, yLegende + 8, 20, 12, 4, 4);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 11));
        g.drawString("Tâche critique", xLegende + 25, yLegende + 19);

        g.setColor(COULEUR_NON_CRITIQUE);
        g.fillRoundRect(xLegende, yLegende + 26, 20, 12, 4, 4);
        g.setColor(Color.BLACK);
        g.drawString("Tâche non critique", xLegende + 25, yLegende + 37);

        g.setColor(new Color(50, 120, 220, 80));
        g.fillRoundRect(xLegende, yLegende + 44, 20, 12, 4, 4);
        g.setColor(Color.BLACK);
        g.drawString("Marge disponible", xLegende + 25, yLegende + 55);
    }
}