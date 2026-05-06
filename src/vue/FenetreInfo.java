package vue;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import modele.Graphe;

public class FenetreInfo extends JPanel {

    private static final long serialVersionUID = 1L;


    private static final Color BG_PANEL     = new Color(22,  27,  51);
    private static final Color BG_FIELD     = new Color(30,  38,  68);
    private static final Color CYAN         = new Color(0,   229, 255);
    private static final Color TEXT_PRIMARY = new Color(230, 235, 245);
    private static final Color BORDER_FIELD = new Color(58,  72,  116);


    private static final Font FONT_LABEL  = new Font("Trebuchet MS", Font.BOLD,  13);
    private static final Font FONT_VALUE  = new Font("Trebuchet MS", Font.PLAIN, 13);
    private static final Font FONT_MATRIX = new Font("Monospaced",   Font.PLAIN, 12);
    private static final Font FONT_TITLE  = new Font("Segoe UI Black", Font.PLAIN, 13);


    private JLabel    labelType;
    private JLabel    labelSommets;
    private JLabel    labelArcs;
    private JLabel    labelFs;
    private JLabel    labelAps;
    private JTextArea aireMatrice;

    public FenetreInfo() {
        setLayout(new BorderLayout(12, 0));
        setBackground(BG_PANEL);
        setOpaque(true);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_FIELD), // séparateur haut
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));

        // ── Titre ──
        JLabel titre = new JLabel("INFORMATIONS");
        titre.setFont(FONT_TITLE);
        titre.setForeground(CYAN);

        // ── Panneau gauche : infos textuelles ───
        JPanel panneauGauche = new JPanel();
        panneauGauche.setLayout(new BoxLayout(panneauGauche, BoxLayout.Y_AXIS));
        panneauGauche.setOpaque(false);

        panneauGauche.add(titre);
        panneauGauche.add(Box.createVerticalStrut(10));

        labelType    = creerLabel("Type");    panneauGauche.add(labelType);
        panneauGauche.add(Box.createVerticalStrut(6));
        labelSommets = creerLabel("Sommets"); panneauGauche.add(labelSommets);
        panneauGauche.add(Box.createVerticalStrut(6));
        labelArcs    = creerLabel("Arcs");    panneauGauche.add(labelArcs);
        panneauGauche.add(Box.createVerticalStrut(6));
        labelFs      = creerLabel("FS");      panneauGauche.add(labelFs);
        panneauGauche.add(Box.createVerticalStrut(6));
        labelAps     = creerLabel("APS");     panneauGauche.add(labelAps);

        // ── Panneau droit : matrice d'adjacence ───────
        aireMatrice = new JTextArea();
        aireMatrice.setEditable(false);
        aireMatrice.setFont(FONT_MATRIX);
        aireMatrice.setBackground(BG_FIELD);
        aireMatrice.setForeground(TEXT_PRIMARY);
        aireMatrice.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));

        TitledBorder titreBordure = BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(BORDER_FIELD),
            "Matrice d'adjacence",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Trebuchet MS", Font.BOLD, 11),
            CYAN
        );

        JScrollPane scroll = new JScrollPane(aireMatrice);
        scroll.setPreferredSize(new Dimension(220, 0));
        scroll.setBorder(titreBordure);
        scroll.setBackground(BG_FIELD);
        scroll.getViewport().setBackground(BG_FIELD);

        // Barres de défilement stylisées
        scroll.getVerticalScrollBar().setBackground(BG_FIELD);
        scroll.getHorizontalScrollBar().setBackground(BG_FIELD);

        add(panneauGauche, BorderLayout.CENTER);
        add(scroll,        BorderLayout.EAST);
    }

    public void mettreAJour(Graphe<?> graphe) {
        if (graphe == null) {
            setTexte(labelType,    "Type",    "—");
            setTexte(labelSommets, "Sommets", "—");
            setTexte(labelArcs,    "Arcs",    "—");
            setTexte(labelFs,      "FS",      "—");
            setTexte(labelAps,     "APS",     "—");
            aireMatrice.setText("—");
            return;
        }

        setTexte(labelType,    "Type",    typeGraphe(graphe));
        setTexte(labelSommets, "Sommets", String.valueOf(graphe.getOrdre()));
        setTexte(labelArcs,    "Arcs",    String.valueOf(graphe.getArcs().size()));

        // FS
        int[] fs = graphe.getFs();
        if (fs != null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < fs.length; i++) {
                sb.append(fs[i]);
                if (i < fs.length - 1) sb.append(";");
            }
            setTexte(labelFs, "FS", sb.toString());
        } else {
            setTexte(labelFs, "FS", "—");
        }

        // APS
        int[] aps = graphe.getAps();
        if (aps != null) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < aps.length; i++) {
                sb.append(aps[i]);
                if (i < aps.length - 1) sb.append(";");
            }
            setTexte(labelAps, "APS", sb.toString());
        } else {
            setTexte(labelAps, "APS", "—");
        }

        // Matrice d'adjacence
        int[][] mat = graphe.getMatriceAdjacence();
        int n = graphe.getOrdre();
        if (mat != null && n > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append("   ");
            for (int j = 1; j <= n; j++)
                sb.append(String.format("%2d", j));
            sb.append("\n   ");
            sb.append("──".repeat(n)).append("\n");
            for (int i = 1; i <= n; i++) {
                sb.append(String.format("%2d|", i));
                for (int j = 1; j <= n; j++)
                    sb.append(String.format("%2d", mat[i][j]));
                sb.append("\n");
            }
            aireMatrice.setText(sb.toString());
            aireMatrice.setCaretPosition(0);
        } else {
            aireMatrice.setText("—");
        }

        revalidate();
        repaint();
    }

    private JLabel creerLabel(String cle) {
        JLabel label = new JLabel(formatLabel(cle, "—"));
        label.setFont(FONT_VALUE); // base (le HTML prend le dessus)
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    /**
     * Met à jour un label avec la clé en cyan et la valeur en blanc.
     */
    private void setTexte(JLabel label, String cle, String valeur) {
        label.setText(formatLabel(cle, valeur));
    }

    private String formatLabel(String cle, String valeur) {
        return String.format(
            "<html><span style='color:#00E5FF; font-family:Trebuchet MS; font-size:10pt; font-weight:bold;'>%s</span>"
            + "<span style='color:#E6EBF5; font-family:Trebuchet MS; font-size:10pt;'> : %s</span></html>",
            cle, valeur
        );
    }

    private String typeGraphe(Graphe<?> graphe) {
        if (graphe.isEstOriente() && graphe.isEstPondere()) return "Orienté pondéré";
        if (graphe.isEstOriente())                          return "Orienté";
        if (graphe.isEstPondere())                          return "Non orienté pondéré";
        return "Non orienté";
    }
}