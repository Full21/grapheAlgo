package vue;

import modele.GestionnaireGraphe;
import modele.Graphe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * FenetreApplication — extends JFrame
 *
 * Fenêtre principale de l'application.
 * Contient (composition) :
 *   - vueGraphe      : VueGraphe        (dessin du graphe)
 *   - panneauAlgos   : PanneauAlgorithmes (choix et lancement des algos)
 *   - barreMenus     : JMenuBar
 *   - zoneResultats  : JTextArea
 *
 * Relie toutes les couches : Saisie → Modèle → Affichage.
 */
public class FenetreApplication extends JFrame {

    // -------------------------------------------------------------------------
    // Attributs (UML)
    // -------------------------------------------------------------------------
    private VueGraphe            vueGraphe;
    private PanneauAlgorithmes   panneauAlgos;
    private JMenuBar             barreMenus;
    private JTextArea            zoneResultats;

    // Référence au singleton gestionnaire
    private final GestionnaireGraphe gestionnaire = GestionnaireGraphe.getInstance();


    // -------------------------------------------------------------------------
    // Constructeur
    // -------------------------------------------------------------------------
    public FenetreApplication() {
        super("Graphes & Algorithmes");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 750);
        setLocationRelativeTo(null);

        initialiserMenus();
        initialiserPanneaux();

        setVisible(true);
    }


    // -------------------------------------------------------------------------
    // initialiserMenus — construit la JMenuBar et ses menus
    // -------------------------------------------------------------------------
    public void initialiserMenus() {
        barreMenus = new JMenuBar();

        // ---- Menu Fichier ----
        JMenu menuFichier = new JMenu("Fichier");

        JMenuItem itemNouveau = new JMenuItem("Nouveau graphe");
        itemNouveau.addActionListener((ActionEvent e) -> {
            String type = JOptionPane.showInputDialog(this,
                "Type de graphe :\n1 - Orienté\n2 - Non orienté\n3 - Orienté pondéré\n4 - Non orienté pondéré",
                "Nouveau graphe", JOptionPane.QUESTION_MESSAGE);
            if (type != null) {
                String nbStr = JOptionPane.showInputDialog(this,
                    "Nombre de sommets :", "Nouveau graphe", JOptionPane.QUESTION_MESSAGE);
                try {
                    int nb = Integer.parseInt(nbStr.trim());
                    Graphe<?> g = creerGrapheParType(type.trim(), nb);
                    if (g != null) {
                        gestionnaire.setGrapheCourant(g);
                        rafraichir();
                        afficherResultat("Nouveau graphe créé (" + nb + " sommets).");
                    }
                } catch (NumberFormatException ex) {
                    afficherErreur("Nombre de sommets invalide.");
                }
            }
        });

        JMenuItem itemOuvrir = new JMenuItem("Ouvrir...");
        itemOuvrir.addActionListener((ActionEvent e) -> {
            JFileChooser fc = new JFileChooser();
            fc.setDialogTitle("Ouvrir un graphe");
            int res = fc.showOpenDialog(this);
            if (res == JFileChooser.APPROVE_OPTION) {
                String chemin = fc.getSelectedFile().getName();
                modele.SaisieFichier sf = new modele.SaisieFichier(chemin);
                Graphe<?> g = sf.construireGraphe();
                if (g != null) {
                    rafraichir();
                    afficherResultat("Graphe chargé depuis : " + chemin);
                } else {
                    afficherErreur("Impossible de charger le fichier.");
                }
            }
        });

        JMenuItem itemSauvegarder = new JMenuItem("Sauvegarder...");
        itemSauvegarder.addActionListener((ActionEvent e) -> {
            Graphe<?> g = gestionnaire.getGrapheCourant();
            if (g == null) { afficherErreur("Aucun graphe courant."); return; }
            String nom = JOptionPane.showInputDialog(this,
                "Nom du fichier :", "Sauvegarder", JOptionPane.QUESTION_MESSAGE);
            if (nom != null && !nom.isBlank()) {
                modele.SaisieFichier sf = new modele.SaisieFichier(nom.trim());
                sf.ecrireGraphe(g);
                afficherResultat("Graphe sauvegardé dans : " + nom.trim());
            }
        });

        JMenuItem itemQuitter = new JMenuItem("Quitter");
        itemQuitter.addActionListener(e -> System.exit(0));

        menuFichier.add(itemNouveau);
        menuFichier.add(itemOuvrir);
        menuFichier.add(itemSauvegarder);
        menuFichier.addSeparator();
        menuFichier.add(itemQuitter);

        // ---- Menu Affichage ----
        JMenu menuAffichage = new JMenu("Affichage");

        JMenuItem itemCentrer = new JMenuItem("Centrer la vue");
        itemCentrer.addActionListener(e -> { if (vueGraphe != null) vueGraphe.centrerVue(); });

        JMenuItem itemActualiser = new JMenuItem("Actualiser");
        itemActualiser.addActionListener(e -> rafraichir());

        menuAffichage.add(itemCentrer);
        menuAffichage.add(itemActualiser);

        // ---- Menu Aide ----
        JMenu menuAide = new JMenu("Aide");
        JMenuItem itemAPropos = new JMenuItem("À propos");
        itemAPropos.addActionListener(e ->
            JOptionPane.showMessageDialog(this,
                "Projet Graphes & Algorithmes\nL3 MIAGE / INFO\nUniversité de Haute-Alsace",
                "À propos", JOptionPane.INFORMATION_MESSAGE));
        menuAide.add(itemAPropos);

        barreMenus.add(menuFichier);
        barreMenus.add(menuAffichage);
        barreMenus.add(menuAide);

        setJMenuBar(barreMenus);
    }


    // -------------------------------------------------------------------------
    // initialiserPanneaux — construit et place les panneaux dans la fenêtre
    //
    // Layout :
    //   ┌────────────────────────┬──────────────────┐
    //   │      VueGraphe         │ PanneauAlgorithmes│
    //   │      (centre)          │  (droite 300px)   │
    //   ├────────────────────────┴──────────────────┤
    //   │           zoneResultats (bas 150px)        │
    //   └────────────────────────────────────────────┘
    // -------------------------------------------------------------------------
    public void initialiserPanneaux() {
        // Zone de résultats (bas)
        zoneResultats = new JTextArea(6, 40);
        zoneResultats.setEditable(false);
        zoneResultats.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        zoneResultats.setBorder(BorderFactory.createTitledBorder("Résultats"));
        JScrollPane scrollResultats = new JScrollPane(zoneResultats);
        scrollResultats.setPreferredSize(new Dimension(0, 150));

        // Vue graphe (centre)
        vueGraphe = new VueGraphe();
        vueGraphe.setBorder(BorderFactory.createTitledBorder("Graphe"));

        // Panneau algorithmes (droite)
        panneauAlgos = new PanneauAlgorithmes(this);
        panneauAlgos.setPreferredSize(new Dimension(300, 0));
        panneauAlgos.setBorder(BorderFactory.createTitledBorder("Algorithmes"));

        // Panneau central (vue + algos côte à côte)
        JPanel panneauCentral = new JPanel(new BorderLayout());
        panneauCentral.add(vueGraphe,    BorderLayout.CENTER);
        panneauCentral.add(panneauAlgos, BorderLayout.EAST);

        // Assemblage général
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panneauCentral,   BorderLayout.CENTER);
        getContentPane().add(scrollResultats,  BorderLayout.SOUTH);
    }


    // -------------------------------------------------------------------------
    // afficherResultat — écrit un message dans la zone de résultats
    // -------------------------------------------------------------------------
    public void afficherResultat(String message) {
        zoneResultats.append(message + "\n");
        // Scroller vers le bas automatiquement
        zoneResultats.setCaretPosition(zoneResultats.getDocument().getLength());
    }


    // -------------------------------------------------------------------------
    // afficherErreur — ouvre une boîte de dialogue d'erreur
    // -------------------------------------------------------------------------
    public void afficherErreur(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
    }


    // -------------------------------------------------------------------------
    // rafraichir — met à jour la vue graphe et le panneau algos
    // -------------------------------------------------------------------------
    public void rafraichir() {
        if (vueGraphe != null) {
            vueGraphe.setGraphe(gestionnaire.getGrapheCourant());
            vueGraphe.mettreAJour();
        }
        if (panneauAlgos != null) {
            panneauAlgos.verifierConditions();
        }
        repaint();
    }


    // -------------------------------------------------------------------------
    // Méthode privée utilitaire — crée un graphe selon le type choisi
    // -------------------------------------------------------------------------
    private Graphe<?> creerGrapheParType(String type, int nb) {
        switch (type) {
            case "1": return new modele.GrapheOriente<Integer>(nb);
            case "2": return new modele.GrapheNonOriente<Integer>(nb);
            case "3": return new modele.GrapheOrientePondere<Integer>(nb);
            case "4": return new modele.GrapheNonOrientePondere<Integer>(nb);
            default:
                afficherErreur("Type inconnu : " + type);
                return null;
        }
    }


    // -------------------------------------------------------------------------
    // Getters — pour que les panneaux accèdent aux composants si besoin
    // -------------------------------------------------------------------------
    public VueGraphe          getVueGraphe()    { return vueGraphe; }
    public PanneauAlgorithmes getPanneauAlgos() { return panneauAlgos; }
    public JTextArea          getZoneResultats(){ return zoneResultats; }
}
