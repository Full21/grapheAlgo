package vue;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.BasicStroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import modele.Arc;
import modele.GrapheNonOrientePondere;
import modele.Sommet;

/**
 * FenetreReseauRer — Application : simulation du réseau RER de Paris.
 *
 * 24 stations, 4 lignes (A, B, D, E).
 * Calcule le plus court chemin par Dijkstra et affiche aussi les alternatives
 *
 */
public class FenetreReseauRer extends JFrame {

    
    private static final Color COULEUR_RER_A = new Color(225, 37, 27);
    private static final Color COULEUR_RER_B = new Color(0, 110, 184);
    private static final Color COULEUR_RER_D = new Color(0, 145, 77);
    private static final Color COULEUR_RER_E = new Color(214, 130, 188);

    private static final Color FOND          = new Color(245, 247, 250);
    private static final Color STATION_FILL  = new Color(255, 255, 255);
    private static final Color STATION_BORD  = new Color(31, 41, 55);
    private static final Color CHEMIN_HIGHLIGHT = new Color(255, 215, 0);

    private GrapheNonOrientePondere<String> reseau;
    private Map<String, int[]>      positions;
    private Map<String, List<String>> lignesParArete;
    private List<String>             cheminActuel;
    private double                    tempsActuel;

    private JComboBox<String> comboDepart;
    private JComboBox<String> comboArrivee;
    private JTextArea         zoneResultat;
    private PanneauCarte      panneauCarte;

    public FenetreReseauRer() {
        super("Simulation Réseau RER Paris — Plus court chemin (Dijkstra)");
        construireReseau();
        construireInterface();

        setSize(1300, 820);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void construireReseau() {
        reseau = new GrapheNonOrientePondere<>(24);

        String[] stations = {
            "Cergy", "La Défense", "Auber", "Saint-Lazare", "Haussmann",
            "Aéroport CDG", "Gare du Nord", "Magenta", "Gare de l'Est",
            "Châtelet-Les Halles", "Gare de Lyon",
            "Stade de France", "Sarcelles",
            "Luxembourg", "Denfert-Rochereau",
            "Massy-Palaiseau", "Saint-Rémy-lès-Chevreuse",
            "Nation", "Vincennes", "Val de Fontenay",
            "Fontenay-sous-Bois", "Noisy-le-Grand",
            "Marne-la-Vallée", "Boissy-Saint-Léger"
        };
        for (String s : stations) reseau.ajouterSommet(s);

        positions = new HashMap<>();
        positions.put("Cergy",                  new int[]{  60, 280 });
        positions.put("La Défense",             new int[]{ 170, 320 });
        positions.put("Saint-Lazare",           new int[]{ 320, 250 });
        positions.put("Haussmann",              new int[]{ 410, 230 });
        positions.put("Magenta",                new int[]{ 500, 200 });
        positions.put("Auber",                  new int[]{ 320, 340 });
        positions.put("Aéroport CDG",           new int[]{ 600,  50 });
        positions.put("Gare du Nord",           new int[]{ 540, 150 });
        positions.put("Gare de l'Est",          new int[]{ 540, 250 });
        positions.put("Châtelet-Les Halles",    new int[]{ 480, 360 });
        positions.put("Gare de Lyon",           new int[]{ 590, 410 });
        positions.put("Stade de France",        new int[]{ 440, 100 });
        positions.put("Sarcelles",              new int[]{ 360,  50 });
        positions.put("Luxembourg",             new int[]{ 430, 470 });
        positions.put("Denfert-Rochereau",      new int[]{ 380, 560 });
        positions.put("Massy-Palaiseau",        new int[]{ 240, 620 });
        positions.put("Saint-Rémy-lès-Chevreuse", new int[]{ 80, 560 });
        positions.put("Nation",                 new int[]{ 700, 430 });
        positions.put("Vincennes",              new int[]{ 780, 400 });
        positions.put("Val de Fontenay",        new int[]{ 860, 360 });
        positions.put("Fontenay-sous-Bois",     new int[]{ 920, 380 });
        positions.put("Noisy-le-Grand",         new int[]{ 980, 320 });
        positions.put("Marne-la-Vallée",        new int[]{1050, 280 });
        positions.put("Boissy-Saint-Léger",     new int[]{ 920, 480 });

        lignesParArete = new HashMap<>();

        // ===== RER A =====
        ajouterLigne("Cergy",                "La Défense",            18, "RER A");
        ajouterLigne("La Défense",           "Auber",                  4, "RER A");
        ajouterLigne("Auber",                "Châtelet-Les Halles",    3, "RER A");
        ajouterLigne("Châtelet-Les Halles",  "Gare de Lyon",           3, "RER A");
        ajouterLigne("Gare de Lyon",         "Nation",                 2, "RER A");
        ajouterLigne("Nation",               "Vincennes",              3, "RER A");
        ajouterLigne("Vincennes",            "Val de Fontenay",        3, "RER A");
        ajouterLigne("Val de Fontenay",      "Fontenay-sous-Bois",     2, "RER A");
        ajouterLigne("Fontenay-sous-Bois",   "Noisy-le-Grand",         4, "RER A");
        ajouterLigne("Noisy-le-Grand",       "Marne-la-Vallée",        4, "RER A");
        ajouterLigne("Val de Fontenay",      "Boissy-Saint-Léger",    10, "RER A");

        // ===== RER B =====
        ajouterLigne("Aéroport CDG",         "Gare du Nord",          25, "RER B");
        ajouterLigne("Gare du Nord",         "Châtelet-Les Halles",    4, "RER B");
        ajouterLigne("Châtelet-Les Halles",  "Luxembourg",             3, "RER B");
        ajouterLigne("Luxembourg",           "Denfert-Rochereau",      3, "RER B");
        ajouterLigne("Denfert-Rochereau",    "Massy-Palaiseau",       16, "RER B");
        ajouterLigne("Denfert-Rochereau",    "Saint-Rémy-lès-Chevreuse", 28, "RER B");

        // ===== RER D =====
        ajouterLigne("Sarcelles",            "Stade de France",       10, "RER D");
        ajouterLigne("Stade de France",      "Gare du Nord",           5, "RER D");
        ajouterLigne("Gare du Nord",         "Magenta",                2, "RER D");
        ajouterLigne("Magenta",              "Gare de l'Est",          2, "RER D");
        ajouterLigne("Gare de l'Est",        "Châtelet-Les Halles",    4, "RER D");
        ajouterLigne("Châtelet-Les Halles",  "Gare de Lyon",           3, "RER D");

        // ===== RER E =====
        ajouterLigne("La Défense",           "Saint-Lazare",           5, "RER E");
        ajouterLigne("Saint-Lazare",         "Haussmann",              2, "RER E");
        ajouterLigne("Haussmann",            "Magenta",                3, "RER E");
        ajouterLigne("Magenta",              "Gare de l'Est",          2, "RER E");
        ajouterLigne("Gare de l'Est",        "Châtelet-Les Halles",    4, "RER E");
        ajouterLigne("Châtelet-Les Halles",  "Gare de Lyon",           3, "RER E");
        ajouterLigne("Gare de Lyon",         "Val de Fontenay",        9, "RER E");
        ajouterLigne("Val de Fontenay",      "Fontenay-sous-Bois",     2, "RER E");
    }

    private void ajouterLigne(String s1, String s2, double temps, String ligne) {
        Arc<?> arcExistant = null;
        for (Arc<?> a : reseau.getArcs()) {
            String src  = (String) a.getSource().getDonnee();
            String dest = (String) a.getDestination().getDonnee();
            if ((src.equals(s1) && dest.equals(s2)) || (src.equals(s2) && dest.equals(s1))) {
                arcExistant = a;
                break;
            }
        }
        if (arcExistant == null) {
            reseau.ajouterArc(s1, s2, temps);
        } else if (temps < arcExistant.getPoids()) {
            arcExistant.setPoids(temps);
        }
        String cle = (s1.compareTo(s2) < 0) ? s1 + "|" + s2 : s2 + "|" + s1;
        List<String> liste = lignesParArete.computeIfAbsent(cle, k -> new ArrayList<>());
        if (!liste.contains(ligne)) liste.add(ligne);
    }

    private void construireInterface() {
        getContentPane().setBackground(FOND);
        setLayout(new BorderLayout(10, 10));

        JPanel haut = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 10));
        haut.setBackground(FOND);
        haut.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));

        String[] noms = new String[reseau.getSommets().size()];
        for (int i = 0; i < noms.length; i++) {
            noms[i] = (String) reseau.getSommets().get(i).getDonnee();
        }
        Arrays.sort(noms);

        comboDepart  = new JComboBox<>(noms);
        comboArrivee = new JComboBox<>(noms);
        comboArrivee.setSelectedIndex(noms.length - 1);

        JButton boutonCalculer = new JButton("Calculer le trajet");
        boutonCalculer.setBackground(new Color(99, 102, 241));
        boutonCalculer.setForeground(Color.WHITE);
        boutonCalculer.setFont(new Font("Segoe UI", Font.BOLD, 13));
        boutonCalculer.setFocusPainted(false);
        boutonCalculer.setBorder(BorderFactory.createEmptyBorder(6, 16, 6, 16));
        boutonCalculer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { lancerDijkstra(); }
        });

        JLabel labelDepart  = new JLabel("Départ :");
        JLabel labelArrivee = new JLabel("Arrivée :");
        labelDepart.setFont(new Font("Segoe UI", Font.BOLD, 13));
        labelArrivee.setFont(new Font("Segoe UI", Font.BOLD, 13));

        haut.add(labelDepart);  haut.add(comboDepart);
        haut.add(labelArrivee); haut.add(comboArrivee);
        haut.add(boutonCalculer);
        add(haut, BorderLayout.NORTH);

        panneauCarte = new PanneauCarte();
        panneauCarte.setPreferredSize(new Dimension(1000, 660));
        add(panneauCarte, BorderLayout.CENTER);

        zoneResultat = new JTextArea(20, 25);
        zoneResultat.setEditable(false);
        zoneResultat.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        zoneResultat.setBackground(Color.WHITE);
        zoneResultat.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        zoneResultat.setText("  Choisissez deux stations et\n  cliquez sur 'Calculer le trajet'.\n\n"
                           + "  Le réseau comporte :\n"
                           + "  - 24 stations\n"
                           + "  - 4 lignes (A, B, D, E)\n\n"
                           + "  Légende :\n"
                           + "  ● Rouge   = RER A\n"
                           + "  ● Bleu    = RER B\n"
                           + "  ● Vert    = RER D\n"
                           + "  ● Rose    = RER E\n");

        JScrollPane scroll = new JScrollPane(zoneResultat);
        scroll.setBorder(BorderFactory.createTitledBorder("Itinéraire"));
        scroll.setPreferredSize(new Dimension(300, 660));
        add(scroll, BorderLayout.EAST);
    }

    /** Dijkstra normal sur tout le réseau. Retourne le chemin ou null. */
    private List<String> dijkstra(String depart, String arrivee, double[] tempsTotal) {
        Map<String, Double> distances  = new HashMap<>();
        Map<String, String> precedents = new HashMap<>();
        List<String>        nonVisites = new ArrayList<>();

        for (Sommet<?> s : reseau.getSommets()) {
            String nom = (String) s.getDonnee();
            distances.put(nom, Double.POSITIVE_INFINITY);
            nonVisites.add(nom);
        }
        distances.put(depart, 0.0);

        while (!nonVisites.isEmpty()) {
            String courant = null;
            double minDist = Double.POSITIVE_INFINITY;
            for (String nom : nonVisites) {
                if (distances.get(nom) < minDist) {
                    minDist = distances.get(nom);
                    courant = nom;
                }
            }
            if (courant == null) break;
            nonVisites.remove(courant);
            if (courant.equals(arrivee)) break;

            for (Arc<?> a : reseau.getArcs()) {
                String src  = (String) a.getSource().getDonnee();
                String dest = (String) a.getDestination().getDonnee();
                String voisin = null;
                if (src.equals(courant) && nonVisites.contains(dest))      voisin = dest;
                else if (dest.equals(courant) && nonVisites.contains(src)) voisin = src;
                if (voisin == null) continue;

                double nouvelleDist = distances.get(courant) + a.getPoids();
                if (nouvelleDist < distances.get(voisin)) {
                    distances.put(voisin, nouvelleDist);
                    precedents.put(voisin, courant);
                }
            }
        }

        if (distances.get(arrivee) == Double.POSITIVE_INFINITY) return null;

        List<String> chemin = new ArrayList<>();
        String c = arrivee;
        while (c != null) {
            chemin.add(0, c);
            c = precedents.get(c);
        }
        tempsTotal[0] = distances.get(arrivee);
        return chemin;
    }

    /**
     * Dijkstra restreint : n'utilise QUE les arêtes desservies par la ligne donnée.
     * Retourne null si aucun chemin n'existe en restant sur cette ligne.
     */
    private List<String> dijkstraRestreint(String depart, String arrivee,
                                           String ligneAutorisee, double[] tempsTotal) {
        Map<String, Double> distances  = new HashMap<>();
        Map<String, String> precedents = new HashMap<>();
        List<String>        nonVisites = new ArrayList<>();

        for (Sommet<?> s : reseau.getSommets()) {
            String nom = (String) s.getDonnee();
            distances.put(nom, Double.POSITIVE_INFINITY);
            nonVisites.add(nom);
        }
        distances.put(depart, 0.0);

        while (!nonVisites.isEmpty()) {
            String courant = null;
            double minDist = Double.POSITIVE_INFINITY;
            for (String nom : nonVisites) {
                if (distances.get(nom) < minDist) {
                    minDist = distances.get(nom);
                    courant = nom;
                }
            }
            if (courant == null) break;
            nonVisites.remove(courant);
            if (courant.equals(arrivee)) break;

            for (Arc<?> a : reseau.getArcs()) {
                String src  = (String) a.getSource().getDonnee();
                String dest = (String) a.getDestination().getDonnee();
                String voisin = null;
                if (src.equals(courant) && nonVisites.contains(dest))      voisin = dest;
                else if (dest.equals(courant) && nonVisites.contains(src)) voisin = src;
                if (voisin == null) continue;

                // Ne prendre que les arêtes desservies par la ligne demandée
                String cle = (src.compareTo(dest) < 0) ? src + "|" + dest : dest + "|" + src;
                List<String> lignes = lignesParArete.getOrDefault(cle, new ArrayList<>());
                if (!lignes.contains(ligneAutorisee)) continue;

                double nouvelleDist = distances.get(courant) + a.getPoids();
                if (nouvelleDist < distances.get(voisin)) {
                    distances.put(voisin, nouvelleDist);
                    precedents.put(voisin, courant);
                }
            }
        }

        if (distances.get(arrivee) == Double.POSITIVE_INFINITY) return null;

        List<String> chemin = new ArrayList<>();
        String c = arrivee;
        while (c != null) {
            chemin.add(0, c);
            c = precedents.get(c);
        }
        tempsTotal[0] = distances.get(arrivee);
        return chemin;
    }

    /**
     * Compte le nombre de correspondances dans un chemin
     * (changements de ligne entre arêtes consécutives).
     */
    private int compterCorrespondances(List<String> chemin) {
        if (chemin == null || chemin.size() < 3) return 0;
        int corr = 0;
        String ligneActuelle = null;

        for (int i = 0; i < chemin.size() - 1; i++) {
            String s1 = chemin.get(i);
            String s2 = chemin.get(i + 1);
            String cle = (s1.compareTo(s2) < 0) ? s1 + "|" + s2 : s2 + "|" + s1;
            List<String> lignes = lignesParArete.getOrDefault(cle, new ArrayList<>());

            if (ligneActuelle == null) {
                if (!lignes.isEmpty()) ligneActuelle = lignes.get(0);
            } else if (!lignes.contains(ligneActuelle)) {
                corr++;
                ligneActuelle = lignes.isEmpty() ? null : lignes.get(0);
            }
        }
        return corr;
    }
    
    private void lancerDijkstra() {
        String depart  = (String) comboDepart.getSelectedItem();
        String arrivee = (String) comboArrivee.getSelectedItem();

        if (depart.equals(arrivee)) {
            zoneResultat.setText("  Le départ et l'arrivée sont identiques !");
            cheminActuel = null;
            panneauCarte.repaint();
            return;
        }

        // 1) Trajet optimal global
        double[] temps = new double[1];
        List<String> chemin = dijkstra(depart, arrivee, temps);
        if (chemin == null) {
            zoneResultat.setText("  Aucun chemin trouvé entre :\n  " + depart + "\n  et\n  " + arrivee);
            cheminActuel = null;
            panneauCarte.repaint();
            return;
        }

        cheminActuel = chemin;
        tempsActuel  = temps[0];

        // 2) Alternatives par ligne unique
        Map<String, AlternativeTrajet> alternatives = new LinkedHashMap<>();
        for (String ligne : new String[]{"RER A", "RER B", "RER D", "RER E"}) {
            double[] t = new double[1];
            List<String> alt = dijkstraRestreint(depart, arrivee, ligne, t);
            if (alt != null) {
                alternatives.put(ligne, new AlternativeTrajet(alt, t[0]));
            }
        }

        afficherItineraire(alternatives);
        panneauCarte.repaint();
    }

    /** Petit objet pour contenir un trajet alternatif. */
    private static class AlternativeTrajet {
        final List<String> chemin;
        final double       temps;
        AlternativeTrajet(List<String> c, double t) { chemin = c; temps = t; }
    }

    private void afficherItineraire(Map<String, AlternativeTrajet> alternatives) {
        StringBuilder sb = new StringBuilder();

        // ===== Trajet optimal =====
        sb.append("  TRAJET OPTIMAL\n");
        sb.append("  ──────────────\n\n");
        sb.append("  De : ").append(cheminActuel.get(0)).append("\n");
        sb.append("  À  : ").append(cheminActuel.get(cheminActuel.size() - 1)).append("\n\n");
        sb.append("  Temps total    : ").append((int) tempsActuel).append(" min\n");
        sb.append("  Stations       : ").append(cheminActuel.size()).append("\n");
        sb.append("  Correspondances: ").append(compterCorrespondances(cheminActuel)).append("\n\n");
        sb.append("  Étapes :\n");

        String ligneActuelle = null;
        for (int i = 0; i < cheminActuel.size() - 1; i++) {
            String s1 = cheminActuel.get(i);
            String s2 = cheminActuel.get(i + 1);
            String cle = (s1.compareTo(s2) < 0) ? s1 + "|" + s2 : s2 + "|" + s1;
            List<String> lignes = lignesParArete.getOrDefault(cle, new ArrayList<>());
            String lignesStr = String.join(" ou ", lignes);

            String prochaineLigne = lignes.isEmpty() ? "" : lignes.get(0);
            boolean changement = (ligneActuelle != null && !lignes.contains(ligneActuelle));

            double tempsEtape = 0;
            for (Arc<?> a : reseau.getArcs()) {
                String src  = (String) a.getSource().getDonnee();
                String dest = (String) a.getDestination().getDonnee();
                if ((src.equals(s1) && dest.equals(s2)) || (src.equals(s2) && dest.equals(s1))) {
                    tempsEtape = a.getPoids();
                    break;
                }
            }

            sb.append("\n  ").append(i + 1).append(". ").append(s1);
            if (changement) sb.append("  (correspondance)");
            sb.append("\n");
            sb.append("       │  ").append(lignesStr).append("  (").append((int) tempsEtape).append(" min)\n");
            ligneActuelle = prochaineLigne;

            if (i == cheminActuel.size() - 2) {
                sb.append("       ▼\n  ").append(i + 2).append(". ").append(s2).append("\n");
            }
        }

        // ===== Alternatives =====
        sb.append("\n");
        sb.append("  ────────────────\n");
        sb.append("  AUTRES OPTIONS\n");
        sb.append("  ────────────────\n");

        if (alternatives.isEmpty()) {
            sb.append("\n  (aucune ligne ne permet\n  un trajet sans correspondance)\n");
        } else {
            sb.append("\n  Sans correspondance :\n");
            boolean trouveAlt = false;
            for (Map.Entry<String, AlternativeTrajet> e : alternatives.entrySet()) {
                AlternativeTrajet alt = e.getValue();
                if (Math.abs(alt.temps - tempsActuel) < 0.01) {
                    // C'est le trajet optimal lui-même, on saute
                    continue;
                }
                trouveAlt = true;
                String marqueur = (alt.temps < tempsActuel) ? " ⚡ (plus rapide)"
                                : (alt.temps > tempsActuel) ? "" : " (égal)";
                sb.append("  • ").append(e.getKey()).append(" seul : ")
                  .append((int) alt.temps).append(" min")
                  .append(marqueur).append("\n");
            }
            if (!trouveAlt) {
                sb.append("  (le trajet optimal utilise déjà\n   une seule ligne)\n");
            }
        }

        sb.append("\n  → Le trajet propose est\n     ");
        sb.append("le plus rapide trouvé\n     par Dijkstra parmi tous\n     les chemins possibles.\n");

        zoneResultat.setText(sb.toString());
    }

    private class PanneauCarte extends JPanel {

        public PanneauCarte() { setBackground(FOND); }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);

            for (Arc<?> a : reseau.getArcs()) {
                String s1 = (String) a.getSource().getDonnee();
                String s2 = (String) a.getDestination().getDonnee();
                int[] p1 = positions.get(s1);
                int[] p2 = positions.get(s2);
                if (p1 == null || p2 == null) continue;

                String cle = (s1.compareTo(s2) < 0) ? s1 + "|" + s2 : s2 + "|" + s1;
                List<String> lignes = lignesParArete.getOrDefault(cle, new ArrayList<>());

                int decalage = -((lignes.size() - 1) * 4) / 2;
                for (String ligne : lignes) {
                    Color couleur;
                    switch (ligne) {
                        case "RER A": couleur = COULEUR_RER_A; break;
                        case "RER B": couleur = COULEUR_RER_B; break;
                        case "RER D": couleur = COULEUR_RER_D; break;
                        case "RER E": couleur = COULEUR_RER_E; break;
                        default:      couleur = Color.GRAY;
                    }
                    g2.setColor(couleur);
                    g2.setStroke(new BasicStroke(5, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

                    double dx = p2[0] - p1[0];
                    double dy = p2[1] - p1[1];
                    double len = Math.sqrt(dx*dx + dy*dy);
                    if (len > 0) {
                        double nx = -dy / len * decalage;
                        double ny =  dx / len * decalage;
                        g2.drawLine((int)(p1[0]+nx), (int)(p1[1]+ny),
                                    (int)(p2[0]+nx), (int)(p2[1]+ny));
                    } else {
                        g2.drawLine(p1[0], p1[1], p2[0], p2[1]);
                    }
                    decalage += 4;
                }

                int mx = (p1[0] + p2[0]) / 2;
                int my = (p1[1] + p2[1]) / 2;
                g2.setColor(new Color(80, 80, 80));
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                g2.drawString((int) a.getPoids() + " min", mx + 6, my - 4);
            }

            if (cheminActuel != null && cheminActuel.size() >= 2) {
                g2.setColor(CHEMIN_HIGHLIGHT);
                g2.setStroke(new BasicStroke(8, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                for (int i = 0; i < cheminActuel.size() - 1; i++) {
                    int[] p1 = positions.get(cheminActuel.get(i));
                    int[] p2 = positions.get(cheminActuel.get(i + 1));
                    if (p1 != null && p2 != null) {
                        g2.drawLine(p1[0], p1[1], p2[0], p2[1]);
                    }
                }
            }

            int rayon = 12;
            g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
            for (Map.Entry<String, int[]> entry : positions.entrySet()) {
                String nom = entry.getKey();
                int[] p = entry.getValue();

                if (cheminActuel != null && cheminActuel.contains(nom)) {
                    g2.setColor(CHEMIN_HIGHLIGHT);
                    g2.fillOval(p[0] - rayon - 2, p[1] - rayon - 2,
                                (rayon + 2) * 2, (rayon + 2) * 2);
                }

                g2.setColor(STATION_FILL);
                g2.fillOval(p[0] - rayon, p[1] - rayon, rayon * 2, rayon * 2);
                g2.setColor(STATION_BORD);
                g2.setStroke(new BasicStroke(2));
                g2.drawOval(p[0] - rayon, p[1] - rayon, rayon * 2, rayon * 2);

                g2.setColor(STATION_BORD);
                int largeurTexte = g2.getFontMetrics().stringWidth(nom);
                g2.drawString(nom, p[0] - largeurTexte / 2, p[1] + rayon + 11);
            }

            int yL = getHeight() - 92;
            g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
            g2.setColor(COULEUR_RER_A); g2.fillRect(10, yL, 20, 8);
            g2.setColor(STATION_BORD);  g2.drawString("RER A", 35, yL + 8);
            g2.setColor(COULEUR_RER_B); g2.fillRect(10, yL + 18, 20, 8);
            g2.setColor(STATION_BORD);  g2.drawString("RER B", 35, yL + 26);
            g2.setColor(COULEUR_RER_D); g2.fillRect(10, yL + 36, 20, 8);
            g2.setColor(STATION_BORD);  g2.drawString("RER D", 35, yL + 44);
            g2.setColor(COULEUR_RER_E); g2.fillRect(10, yL + 54, 20, 8);
            g2.setColor(STATION_BORD);  g2.drawString("RER E", 35, yL + 62);
        }
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(FenetreReseauRer::new);
    }
}