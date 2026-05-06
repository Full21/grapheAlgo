package vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import modele.*;

public class PanneauAlgorithmes extends JPanel {

    // ── Palette partagée ────────
    private static final Color BG_PANEL      = new Color(22,  27,  51);
    private static final Color BG_FIELD      = new Color(30,  38,  68);
    private static final Color ACCENT        = new Color(255, 64,  161);
    private static final Color CYAN          = new Color(0,   229, 255);
    private static final Color TEXT_PRIMARY  = new Color(230, 235, 245);
    private static final Color TEXT_RESULT   = new Color(180, 255, 200);
    private static final Color BORDER_FIELD  = new Color(58,  72,  116);

    // ── Typographie partagée ────
    private static final Font FONT_TITLE  = new Font("Segoe UI Black",  Font.PLAIN, 14);
    private static final Font FONT_LABEL  = new Font("Trebuchet MS",    Font.BOLD,  13);
    private static final Font FONT_COMBO  = new Font("Trebuchet MS",    Font.BOLD,  12);
    private static final Font FONT_FIELD  = new Font("Trebuchet MS",    Font.PLAIN, 13);
    private static final Font FONT_BUTTON = new Font("Trebuchet MS",    Font.BOLD,  14);
    private static final Font FONT_RESULT = new Font("Monospaced",      Font.PLAIN, 12);
    private static final Font FONT_DESC   = new Font("Trebuchet MS",    Font.PLAIN, 12);

    // ── Composants ───────
    private JComboBox<String> choixAlgo;
    private JTextField        champSommetDepart;
    private JTextField        champSommetArrivee;
    private JButton           boutonLancer;
    private JTextArea         zoneDescription;
    private JTextArea         zoneResultat;
    private JLabel            labelDepart;
    private JLabel            labelArrivee;

    public PanneauAlgorithmes() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(12, 10, 10, 10));
        setBackground(BG_PANEL);
        setOpaque(true);

        // ── Titre du panneau ────
        JLabel titrePanel = new JLabel("ALGORITHMES");
        titrePanel.setFont(new Font("Segoe UI Black", Font.PLAIN, 16));
        titrePanel.setForeground(CYAN);
        titrePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(titrePanel);

        add(Box.createVerticalStrut(14));

        // ── Combo algorithmes ─────
        JLabel labelCombo = makeLabel("Choisir un algorithme :");
        add(labelCombo);
        add(Box.createVerticalStrut(5));

        String[] algos = {
            "Dijkstra", "Dantzig", "Tarjan", "Graphe réduit",
            "Bases", "Kruskal", "Prüfer", "Rangs des sommets",
            "Distances", "Est connexe ?", "Est un arbre ?",
            "Gantt (ordonnancement)",
            "Conversion : Matrice → FS/APS",
            "Conversion : FS/APS → Matrice"
        };
        choixAlgo = new JComboBox<>(algos);
        choixAlgo.setFont(FONT_COMBO);
        choixAlgo.setMaximumSize(new Dimension(280, 30));
        choixAlgo.setAlignmentX(Component.LEFT_ALIGNMENT);
        choixAlgo.setBackground(BG_FIELD);
        choixAlgo.setForeground(TEXT_PRIMARY);
        choixAlgo.setBorder(BorderFactory.createLineBorder(BORDER_FIELD));
        choixAlgo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);
                c.setFont(FONT_COMBO);
                if (isSelected) {
                    c.setBackground(ACCENT);
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(BG_FIELD);
                    c.setForeground(TEXT_PRIMARY);
                }
                return c;
            }
        });
        add(choixAlgo);

        add(Box.createVerticalStrut(12));

        // ── Champs sommet départ / arrivée (masqués par défaut) ──
        labelDepart = makeLabel("Sommet de départ (id) :");
        add(labelDepart);
        add(Box.createVerticalStrut(4));
        champSommetDepart = makeTextField();
        add(champSommetDepart);

        add(Box.createVerticalStrut(8));

        labelArrivee = makeLabel("Sommet d'arrivée (id) :");
        add(labelArrivee);
        add(Box.createVerticalStrut(4));
        champSommetArrivee = makeTextField();
        add(champSommetArrivee);

        add(Box.createVerticalStrut(12));

        // ── Bouton Lancer ───
        boutonLancer = new JButton("Lancer l'algorithme");
        boutonLancer.setFont(FONT_BUTTON);
        boutonLancer.setForeground(Color.WHITE);
        boutonLancer.setBackground(ACCENT);
        boutonLancer.setOpaque(true);
        boutonLancer.setBorder(null);
        boutonLancer.setBorderPainted(false);
        boutonLancer.setFocusPainted(false);
        boutonLancer.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        boutonLancer.setAlignmentX(Component.LEFT_ALIGNMENT);
        boutonLancer.setMaximumSize(new Dimension(260, 36));
        add(boutonLancer);

        add(Box.createVerticalStrut(14));

        // ── Zone description ────
        add(makeLabel("Description :"));
        add(Box.createVerticalStrut(4));
        zoneDescription = new JTextArea(4, 18);
        zoneDescription.setFont(FONT_DESC);
        zoneDescription.setEditable(false);
        zoneDescription.setLineWrap(true);
        zoneDescription.setWrapStyleWord(true);
        zoneDescription.setBackground(BG_FIELD);
        zoneDescription.setForeground(TEXT_PRIMARY);
        zoneDescription.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        JScrollPane scrollDesc = new JScrollPane(zoneDescription);
        scrollDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollDesc.setMaximumSize(new Dimension(280, 95));
        scrollDesc.setBorder(BorderFactory.createLineBorder(BORDER_FIELD));
        add(scrollDesc);

        add(Box.createVerticalStrut(12));

        // ── Zone résultat ──────
        add(makeLabel("Résultat :"));
        add(Box.createVerticalStrut(4));
        zoneResultat = new JTextArea(12, 20);
        zoneResultat.setFont(FONT_RESULT);
        zoneResultat.setEditable(false);
        zoneResultat.setBackground(BG_FIELD);
        zoneResultat.setForeground(TEXT_RESULT);
        zoneResultat.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));
        JScrollPane scrollRes = new JScrollPane(zoneResultat);
        scrollRes.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollRes.setMaximumSize(new Dimension(280, 330));
        scrollRes.setBorder(BorderFactory.createLineBorder(BORDER_FIELD));
        add(scrollRes);

        // ── Listeners ──────
        choixAlgo.addActionListener(e -> {
            String algo = (String) choixAlgo.getSelectedItem();
            afficherDescription(algo);
            ajusterChampsSommets(algo);
        });

        boutonLancer.addActionListener(e -> lancerAlgorithme());

        // ── État initial ───
        afficherDescription("Dijkstra");
        ajusterChampsSommets("Dijkstra");
    }

    // ── Helpers de création de composants ───
    private JLabel makeLabel(String texte) {
        JLabel l = new JLabel(texte);
        l.setFont(new Font("Trebuchet MS", Font.BOLD, 15));
        l.setForeground(CYAN);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        return l;
    }

    private JTextField makeTextField() {
        JTextField tf = new JTextField();
        tf.setFont(FONT_FIELD);
        tf.setForeground(TEXT_PRIMARY);
        tf.setBackground(BG_FIELD);
        tf.setCaretColor(TEXT_PRIMARY);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_FIELD),
            BorderFactory.createEmptyBorder(4, 8, 4, 8)
        ));
        tf.setMaximumSize(new Dimension(280, 30));
        tf.setAlignmentX(Component.LEFT_ALIGNMENT);
        return tf;
    }

    private void ajusterChampsSommets(String algo) {
        boolean besoinDepart  = false;
        boolean besoinArrivee = false;

        labelDepart.setVisible(besoinDepart);
        champSommetDepart.setVisible(besoinDepart);
        labelArrivee.setVisible(besoinArrivee);
        champSommetArrivee.setVisible(besoinArrivee);

        revalidate();
        repaint();
    }

    private void afficherDescription(String algo) {
        String d = switch (algo) {
            case "Dijkstra"             -> "Plus court chemin entre deux sommets. Poids positifs uniquement.";
            case "Dantzig"              -> "Plus courts chemins entre tous les couples de sommets. Accepte les poids négatifs.";
            case "Tarjan"               -> "Composantes fortement connexes d'un graphe orienté.";
            case "Graphe réduit"        -> "Construit le graphe réduit à partir des CFC de Tarjan.";
            case "Bases"                -> "Sommets sans prédécesseur (demi-degré intérieur nul).";
            case "Kruskal"              -> "Arbre couvrant de poids minimal. Graphe non orienté pondéré.";
            case "Prüfer"               -> "Codage d'un arbre en une séquence de n-2 entiers.";
            case "Rangs des sommets"    -> "Calcule le rang de chaque sommet (graphe orienté sans circuit).";
            case "Distances"            -> "Matrice des distances (nombre d'arcs) entre tous les couples.";
            case "Est connexe ?"        -> "Vérifie si un graphe non orienté est connexe.";
            case "Est un arbre ?"       -> "Vérifie si un graphe non orienté est un arbre.";
            case "Gantt (ordonnancement)" -> "Affiche le diagramme de Gantt et les chemins critiques.";
            case "Conversion : Matrice → FS/APS" -> "Reconstruit les tableaux FS et APS à partir de la matrice d'adjacence courante.";
            case "Conversion : FS/APS → Matrice" -> "Reconstruit la matrice d'adjacence à partir des tableaux FS et APS courants.";
            default                     -> "";
        };
        zoneDescription.setText(d);
    }

    private void lancerAlgorithme() {
        Graphe<?> graphe = GestionnaireGraphe.getInstance().getGrapheCourant();
        if (graphe == null) {
            JOptionPane.showMessageDialog(this, "Aucun graphe chargé !", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String algo = (String) choixAlgo.getSelectedItem();
        zoneResultat.setText("");
        try {
            switch (algo) {
                case "Dijkstra"               -> lancerDijkstra(graphe);
                case "Dantzig"                -> lancerDantzig(graphe);
                case "Tarjan"                 -> lancerTarjan(graphe);
                case "Graphe réduit"          -> lancerGrapheReduit(graphe);
                case "Bases"                  -> lancerBases(graphe);
                case "Kruskal"                -> lancerKruskal(graphe);
                case "Prüfer"                 -> lancerPrufer(graphe);
                case "Rangs des sommets"      -> lancerRangs(graphe);
                case "Distances"              -> lancerDistances(graphe);
                case "Est connexe ?"          -> lancerEstConnexe(graphe);
                case "Est un arbre ?"         -> lancerEstArbre(graphe);
                case "Gantt (ordonnancement)" -> lancerGantt(graphe);
                case "Conversion : Matrice → FS/APS" -> lancerConversionMatriceVersFs(graphe);
                case "Conversion : FS/APS → Matrice" -> lancerConversionFsVersMatrice(graphe);
            }
        } catch (Exception ex) {
            zoneResultat.setText("Erreur : " + ex.getMessage());
        }
    }

    // ── Algorithmes ────

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void lancerDijkstra(Graphe<?> g) {
        if (!(g instanceof GrapheOrientePondere)) {
            erreur("Dijkstra nécessite un graphe orienté pondéré.");
            return;
        }
        GrapheOrientePondere gp = (GrapheOrientePondere) g;
        if (!gp.verifierConditions()) {
            erreur("Dijkstra nécessite des poids positifs.");
            return;
        }
        try {
            double[][] distances = gp.dijkstraMatrice();
            int n = gp.getOrdre();
            StringBuilder sb = new StringBuilder("Matrice des plus courts chemins (Dijkstra) :\n\n");
            sb.append("      ");
            for (int j = 1; j <= n; j++) sb.append(String.format("%7d ", j));
            sb.append("\n");
            for (int i = 1; i <= n; i++) {
                sb.append(String.format("%3d : ", i));
                for (int j = 1; j <= n; j++) {
                    if (distances[i][j] >= 100) sb.append("      ∞ ");
                    else                        sb.append(String.format("%7.1f ", distances[i][j]));
                }
                sb.append("\n");
            }
            zoneResultat.setText(sb.toString());
        } catch (Exception e) {
            erreur(e.getMessage());
        }
    }

    private void lancerDantzig(Graphe<?> g) {
        if (!(g instanceof GrapheOrientePondere)) {
            erreur("Dantzig nécessite un graphe orienté pondéré.");
            return;
        }
        GrapheOrientePondere<?> gp = (GrapheOrientePondere<?>) g;
        double[][] c = gp.initialiserMatriceDistances();
        boolean ok = gp.dantzig(c);
        if (!ok) {
            zoneResultat.setText("Circuit de poids négatif détecté !");
            return;
        }
        int n = gp.getOrdre();
        StringBuilder sb = new StringBuilder("Matrice des plus courtes distances :\n\n     ");
        for (int j = 1; j <= n; j++) sb.append(String.format("%6d ", j));
        sb.append("\n");
        for (int i = 1; i <= n; i++) {
            sb.append(String.format("%3d: ", i));
            for (int j = 1; j <= n; j++) {
                if (c[i][j] == Double.POSITIVE_INFINITY) sb.append("     ∞ ");
                else                                     sb.append(String.format("%6.1f ", c[i][j]));
            }
            sb.append("\n");
        }
        zoneResultat.setText(sb.toString());
    }

    private void lancerTarjan(Graphe<?> g) {
        if (!(g instanceof GrapheOriente)) {
            erreur("Tarjan nécessite un graphe orienté.");
            return;
        }
        GrapheOriente<?> go = (GrapheOriente<?>) g;
        List<? extends List<?>> cfc = go.tarjan();
        StringBuilder sb = new StringBuilder("Composantes fortement connexes :\n\n");
        for (int i = 0; i < cfc.size(); i++)
            sb.append("CFC ").append(i + 1).append(" : ").append(cfc.get(i)).append("\n");
        zoneResultat.setText(sb.toString());
    }

    private void lancerGrapheReduit(Graphe<?> g) {
        if (!(g instanceof GrapheOriente)) {
            erreur("Nécessite un graphe orienté.");
            return;
        }
        GrapheOriente<?> go = (GrapheOriente<?>) g;
        List<? extends List<?>> cfcs = go.tarjan();
        GrapheOriente<?> reduit = go.grapheReduit();

        StringBuilder sb = new StringBuilder("Graphe réduit :\n\n");

        sb.append("Composantes fortement connexes :\n");
        for (int i = 0; i < cfcs.size(); i++) {
            sb.append("  CFC").append(i + 1)
              .append(" : ").append(cfcs.get(i)).append("\n");
        }

        sb.append("\nArcs du graphe réduit (").append(reduit.getArcs().size()).append(") :\n");
        if (reduit.getArcs().isEmpty()) {
            sb.append("  (aucun arc — toutes les CFC sont isolées)\n");
        } else {
            for (var arc : reduit.getArcs()) {
                sb.append("  CFC").append(arc.getSource().getDonnee())
                  .append(" → CFC").append(arc.getDestination().getDonnee())
                  .append("\n");
            }
        }

        zoneResultat.setText(sb.toString());
    }

    private void lancerBases(Graphe<?> g) {
        if (!(g instanceof GrapheOriente)) {
            erreur("Nécessite un graphe orienté.");
            return;
        }
        GrapheOriente<?> go = (GrapheOriente<?>) g;
        var bases = go.getBases();

        StringBuilder sb = new StringBuilder("Bases du graphe :\n");
        sb.append("(Sommets sans prédécesseur)\n\n");
        if (bases.isEmpty()) {
            sb.append("Aucune base — le graphe contient un circuit.");
        } else {
            for (var b : bases) {
                sb.append("  → ").append(b).append("\n");
            }
        }
        zoneResultat.setText(sb.toString());
    }

    private void lancerKruskal(Graphe<?> g) {
        if (!(g instanceof GrapheNonOrientePondere)) {
            erreur("Kruskal nécessite un graphe non orienté pondéré.");
            return;
        }
        Graphe<?> arbre = ((GrapheNonOrientePondere<?>) g).kruskal();
        zoneResultat.setText("Arbre couvrant minimal (Kruskal) :\n" + arbre.toString());
    }

    private void lancerPrufer(Graphe<?> g) {
        if (!(g instanceof GrapheNonOriente)) {
            erreur("Prüfer nécessite un graphe non orienté.");
            return;
        }
        try {
            int[] code = ((GrapheNonOriente<?>) g).codagePrufer();
            StringBuilder sb = new StringBuilder("Codage de Prüfer :\n[");
            for (int i = 1; i < code.length; i++) {
                sb.append(code[i]);
                if (i < code.length - 1) sb.append(", ");
            }
            sb.append("]");
            zoneResultat.setText(sb.toString());
        } catch (Exception e) {
            erreur(e.getMessage());
        }
    }

    private void lancerRangs(Graphe<?> g) {
        if (!(g instanceof GrapheOriente)) {
            erreur("Nécessite un graphe orienté.");
            return;
        }
        GrapheOriente<?> go = (GrapheOriente<?>) g;
        go.calculerRangs();
        StringBuilder sb = new StringBuilder("Rangs des sommets :\n");
        for (int i = 1; i <= go.getOrdre(); i++) {
            Sommet<?> s = go.trouverSommet(i);
            if (s != null)
                sb.append("  ").append(s.getDonnee()).append(" : rang ").append(s.getRang()).append("\n");
        }
        zoneResultat.setText(sb.toString());
    }

    private void lancerDistances(Graphe<?> g) {
        if (!(g instanceof GrapheOriente)) {
            erreur("Nécessite un graphe orienté.");
            return;
        }
        int[][] dist = ((GrapheOriente<?>) g).calculerDistances();
        StringBuilder sb = new StringBuilder("Matrice des distances :\n\n");
        for (int i = 0; i < dist.length; i++) {
            sb.append(i + 1).append(" : ");
            for (int j = 0; j < dist.length; j++)
                sb.append(dist[i][j] == -1 ? "∞ " : dist[i][j] + " ");
            sb.append("\n");
        }
        zoneResultat.setText(sb.toString());
    }

    private void lancerEstConnexe(Graphe<?> g) {
        if (!(g instanceof GrapheNonOriente)) {
            erreur("Nécessite un graphe non orienté.");
            return;
        }
        boolean c = ((GrapheNonOriente<?>) g).estConnexe();
        zoneResultat.setText("Le graphe est " + (c ? "CONNEXE" : "NON CONNEXE"));
    }

    private void lancerEstArbre(Graphe<?> g) {
        if (!(g instanceof GrapheNonOriente)) {
            erreur("Nécessite un graphe non orienté.");
            return;
        }
        boolean a = ((GrapheNonOriente<?>) g).estArbre();
        zoneResultat.setText("Le graphe est " + (a ? "UN ARBRE" : "PAS UN ARBRE"));
    }

    private void lancerGantt(Graphe<?> g) {
        if (!(g instanceof Ordonnancement)) {
            erreur("Nécessite un graphe de type Ordonnancement.");
            return;
        }
        Ordonnancement o = (Ordonnancement) g;
        PanneauGantt panneauGantt = new PanneauGantt();
        panneauGantt.setOrdonnancement(o);
        JDialog dialog = new JDialog(
            (java.awt.Frame) SwingUtilities.getWindowAncestor(this),
            "Diagramme de Gantt", true
        );
        dialog.getContentPane().add(new JScrollPane(panneauGantt));
        dialog.setSize(1000, 500);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void lancerConversionMatriceVersFs(Graphe<?> g) {
        StringBuilder sb = new StringBuilder();
        sb.append("CONVERSION : Matrice -> FS / APS\n");
        sb.append("---------------------------------\n\n");

        sb.append("Matrice d'adjacence (avant) :\n");
        int[][] m = g.getMatriceAdjacence();
        for (int i = 1; i < m.length; i++) {
            sb.append("  ");
            for (int j = 1; j < m[i].length; j++) {
                sb.append(m[i][j]).append(" ");
            }
            sb.append("\n");
        }

        g.construireFsEtAps();

        sb.append("\nFS reconstruit  : [ ");
        for (int v : g.getFs()) sb.append(v).append(" ");
        sb.append("]\n");

        sb.append("\nAPS reconstruit : [ ");
        for (int v : g.getAps()) sb.append(v).append(" ");
        sb.append("]\n");

        sb.append("\nConversion effectuee avec succes.");
        zoneResultat.setText(sb.toString());
    }

    private void lancerConversionFsVersMatrice(Graphe<?> g) {
        StringBuilder sb = new StringBuilder();
        sb.append("CONVERSION : FS / APS -> Matrice\n");
        sb.append("---------------------------------\n\n");

        sb.append("FS (avant)  : [ ");
        for (int v : g.getFs()) sb.append(v).append(" ");
        sb.append("]\n");

        sb.append("APS (avant) : [ ");
        for (int v : g.getAps()) sb.append(v).append(" ");
        sb.append("]\n");

        int n = g.getAps()[0];
        int[][] nouvelleMatrice = new int[n + 1][n + 1];
        nouvelleMatrice[0][0] = n;
        int nbArcs = 0;

        for (int i = 1; i <= n; i++) {
            int k = g.getAps()[i];
            while (k < g.getFs().length && g.getFs()[k] != 0) {
                int successeur = g.getFs()[k];
                nouvelleMatrice[i][successeur] = 1;
                nbArcs++;
                k++;
            }
        }
        nouvelleMatrice[0][1] = nbArcs;
        g.setMatriceAdjacence(nouvelleMatrice);

        sb.append("\nMatrice d'adjacence reconstruite :\n");
        for (int i = 1; i < nouvelleMatrice.length; i++) {
            sb.append("  ");
            for (int j = 1; j < nouvelleMatrice[i].length; j++) {
                sb.append(nouvelleMatrice[i][j]).append(" ");
            }
            sb.append("\n");
        }

        sb.append("\nConversion effectuee avec succes.");
        zoneResultat.setText(sb.toString());
    }

    private void erreur(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
        zoneResultat.setText("Erreur : " + message);
    }

}