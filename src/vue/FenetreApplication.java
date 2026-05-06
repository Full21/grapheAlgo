package vue;

import modele.GestionnaireGraphe;

/**
 *
 * @author marco
 */

import modele.Graphe;
import modele.GrapheNonOriente;
import modele.GrapheNonOrientePondere;
import modele.GrapheOriente;
import modele.GrapheOrientePondere;
import modele.Ordonnancement;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.GroupLayout.Alignment;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.GroupLayout;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JRadioButton;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.logging.Logger;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;

public class FenetreApplication extends javax.swing.JFrame {
    
    

    /**
     * Creates new form NewJFrame
     */
    public FenetreApplication() {
        initComponents();
        this.radioOriente.setSelected(true);
        this.oriente = true;
        this.radioNon.setSelected(true);
        this.pondere = false;
        

        // Activation du bouton à chaque modification du champ
        nombreSommets.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e)  { mettreAJourBouton(); }
            public void removeUpdate(DocumentEvent e)  { mettreAJourBouton(); }
            public void changedUpdate(DocumentEvent e) { mettreAJourBouton(); }
        });
        
     // Dans le constructeur, après initComponents() :
        nombreSommets.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER
                        && boutonGenerer.isEnabled()) {
                    creerEtAfficherGraphe();
                }
            }
        });
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        groupRadiotypeGraphe = new javax.swing.ButtonGroup();
        groupRadioPondere = new javax.swing.ButtonGroup();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel4.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
        jLabel1 = new javax.swing.JLabel();
        jLabel1.setForeground(new Color(0, 229, 255));
        jLabel1.setBackground(new Color(0, 229, 255));
        jLabel2 = new javax.swing.JLabel();
        jLabel2.setForeground(new Color(0, 229, 255));
        jLabel3 = new javax.swing.JLabel();
        jLabel3.setForeground(new Color(0, 229, 255));
        jLabel4 = new javax.swing.JLabel();
        jLabel4.setForeground(new Color(0, 229, 255));
        radioOriente = new javax.swing.JRadioButton();
        radioOriente.setForeground(new Color(230, 235, 245));
        radioOriente.setBackground(new Color(22, 27, 51));
        radioNonOriente = new javax.swing.JRadioButton();
        radioNonOriente.setForeground(new Color(230, 235, 245));
        radioNonOriente.setBackground(new Color(22, 27, 51));
        radioOui = new javax.swing.JRadioButton();
        radioOui.setForeground(new Color(230, 235, 245));
        radioOui.setBackground(new Color(22, 27, 51));
        radioNon = new javax.swing.JRadioButton();
        radioNon.setForeground(new Color(230, 235, 245));
        radioNon.setBackground(new Color(22, 27, 51));
        nombreSommets = new javax.swing.JTextField();
        nombreSommets.setForeground(new Color(230, 235, 245));
        nombreSommets.setBackground(new Color(30, 38, 68));
        jLabel5 = new javax.swing.JLabel();
        jLabel5.setForeground(new Color(0, 229, 255));
        comboDonnees = new javax.swing.JComboBox<>();
        comboDonnees.setForeground(new Color(230, 235, 245));
        comboDonnees.setBackground(new Color(22, 27, 51));                       
        boutonGenerer = new JButton();
        boutonGenerer.setEnabled(false);
        jPanel3 = new javax.swing.JPanel();
        vueDuGraphe = new javax.swing.JPanel();
        vueDuGraphe.setBackground(new Color(13, 17, 38));
        fenetreInfo = new FenetreInfo();
        fenetreInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        fenetreInfo.setBackground(new Color(22, 27, 51));
        jPanel1 = new javax.swing.JPanel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenu2 = new javax.swing.JMenu();

        // Fenêtre principale
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1000, 670));
        setResizable(false);
        //setLocationRelativeTo(null);
        getContentPane().setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));

        // jPanel2 : conteneur principal
        jPanel2.setBackground(new java.awt.Color(204, 255, 0));
        jPanel2.setPreferredSize(new Dimension(1000, 650));
        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 0, 0));

        // ---- jPanel4 : contrôles gauche ----
        jPanel4.setBackground(new Color(22, 27, 51));
        jPanel4.setPreferredSize(new Dimension(200, 650));

        jLabel1.setFont(new java.awt.Font("Segoe UI Black", 0, 16));
        jLabel1.setText("INTERFACE DE GRAPHE");

        jLabel2.setFont(new Font("Trebuchet MS", Font.BOLD, 15));
        jLabel2.setText("Type de graphe");

        jLabel3.setFont(new Font("Trebuchet MS", Font.BOLD, 15));
        jLabel3.setText("Pondéré");

        jLabel4.setFont(new Font("Trebuchet MS", Font.BOLD, 15));
        jLabel4.setText("Nombre de sommets");

        groupRadiotypeGraphe.add(radioOriente);
        radioOriente.setFont(new Font("Trebuchet MS", Font.BOLD, 14));
        radioOriente.setText("Orienté");
        radioOriente.addActionListener(this::radioOrienteActionPerformed);

        groupRadiotypeGraphe.add(radioNonOriente);
        radioNonOriente.setFont(new Font("Trebuchet MS", Font.BOLD, 14));
        radioNonOriente.setText("Non orienté");
        radioNonOriente.addActionListener(this::radioNonOrienteActionPerformed);

        groupRadioPondere.add(radioOui);
        radioOui.setFont(new Font("Trebuchet MS", Font.BOLD, 14));
        radioOui.setText("Oui");

        groupRadioPondere.add(radioNon);
        radioNon.setFont(new Font("Trebuchet MS", Font.BOLD, 14));
        radioNon.setText("Non");

        nombreSommets.setFont(new java.awt.Font("Trebuchet MS", 0, 14));
        nombreSommets.addActionListener(this::nombreSommetsActionPerformed);

        jLabel5.setFont(new Font("Trebuchet MS", Font.BOLD, 15));
        jLabel5.setText("Données de graphe");

        comboDonnees.setFont(new java.awt.Font("Trebuchet MS", 1, 12));
        comboDonnees.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Integer", "String" }));
        comboDonnees.setBackground(new Color(30,  38,  68));
        comboDonnees.setForeground(new Color(230, 235, 245));
        comboDonnees.setBorder(BorderFactory.createLineBorder(new Color(58,  72,  116)));
        comboDonnees.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(
                        list, value, index, isSelected, cellHasFocus);
                c.setFont(new Font("Trebuchet MS",    Font.BOLD,  12));
                if (isSelected) {
                    c.setBackground(new Color(255, 64,  161));
                    c.setForeground(Color.WHITE);
                } else {
                    c.setBackground(new Color(30,  38,  68));
                    c.setForeground(new Color(230, 235, 245));
                }
                return c;
            }
        });
        
        boutonGenerer.setBackground(new Color(255, 64, 161));
        boutonGenerer.setFont(new Font("Trebuchet MS", Font.BOLD, 15));
        boutonGenerer.setForeground(new Color(255, 255, 255));
        boutonGenerer.setText("Générer Graphe");
        boutonGenerer.setBorder(null);
        boutonGenerer.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        boutonGenerer.addActionListener(this::boutonGenererActionPerformed);
        
        JRadioButton radioOrdonnancement = new JRadioButton("Ordonnancement");
        radioOrdonnancement.setForeground(new Color(230, 235, 245));
        radioOrdonnancement.setBackground(new Color(22, 27, 51));
        radioOrdonnancement.setFont(new Font("Trebuchet MS", Font.BOLD, 14));
        groupRadiotypeGraphe.add(radioOrdonnancement);
        radioOrdonnancement.addActionListener(e -> ouvrirFenetreOrdonnancement());

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4Layout.setHorizontalGroup(
        	jPanel4Layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(jPanel4Layout.createSequentialGroup()
        			.addContainerGap()
        			.addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING)
        				.addGroup(jPanel4Layout.createSequentialGroup()
        					.addGap(6)
        					.addComponent(comboDonnees, GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE))
        				.addGroup(jPanel4Layout.createSequentialGroup()
        					.addGap(6)
        					.addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING)
        						.addComponent(radioOrdonnancement)
        						.addGroup(jPanel4Layout.createParallelGroup(Alignment.TRAILING)
        							.addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, 114, GroupLayout.PREFERRED_SIZE)
        							.addGroup(jPanel4Layout.createSequentialGroup()
        								.addComponent(radioOui)
        								.addGap(42)
        								.addComponent(radioNon)))))
        				.addGroup(jPanel4Layout.createSequentialGroup()
        					.addGap(18)
        					.addGroup(jPanel4Layout.createParallelGroup(Alignment.TRAILING)
        						.addComponent(boutonGenerer, GroupLayout.PREFERRED_SIZE, 133, GroupLayout.PREFERRED_SIZE)
        						.addComponent(jLabel4))))
        			.addGap(18, 18, Short.MAX_VALUE))
        		.addGroup(jPanel4Layout.createSequentialGroup()
        			.addComponent(jLabel1, GroupLayout.DEFAULT_SIZE, 190, Short.MAX_VALUE)
        			.addGap(12))
        		.addGroup(jPanel4Layout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(radioOriente)
        			.addContainerGap(127, Short.MAX_VALUE))
        		.addGroup(jPanel4Layout.createSequentialGroup()
        			.addContainerGap()
        			.addComponent(radioNonOriente)
        			.addContainerGap(102, Short.MAX_VALUE))
        		.addGroup(jPanel4Layout.createSequentialGroup()
        			.addGap(8)
        			.addGroup(jPanel4Layout.createParallelGroup(Alignment.LEADING)
        				.addGroup(jPanel4Layout.createSequentialGroup()
        					.addGap(6)
        					.addComponent(jLabel5))
        				.addComponent(nombreSommets, GroupLayout.PREFERRED_SIZE, 164, GroupLayout.PREFERRED_SIZE))
        			.addContainerGap(14, Short.MAX_VALUE))
        		.addGroup(jPanel4Layout.createSequentialGroup()
        			.addGap(18)
        			.addComponent(jLabel3)
        			.addContainerGap(114, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
        	jPanel4Layout.createParallelGroup(Alignment.LEADING)
        		.addGroup(jPanel4Layout.createSequentialGroup()
        			.addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE)
        			.addGap(18)
        			.addComponent(jLabel2)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(radioOriente)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(radioNonOriente)
        			.addGap(10)
        			.addComponent(radioOrdonnancement)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(jLabel3)
        			.addGap(7)
        			.addGroup(jPanel4Layout.createParallelGroup(Alignment.BASELINE)
        				.addComponent(radioNon)
        				.addComponent(radioOui))
        			.addGap(18)
        			.addComponent(jLabel4)
        			.addGap(9)
        			.addComponent(nombreSommets, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
        			.addPreferredGap(ComponentPlacement.UNRELATED)
        			.addComponent(jLabel5)
        			.addGap(12)
        			.addComponent(comboDonnees, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
        			.addGap(53)
        			.addComponent(boutonGenerer, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
        			.addContainerGap(133, Short.MAX_VALUE))
        );
        jPanel4.setLayout(jPanel4Layout);

        jPanel2.add(jPanel4);

        // ---- jPanel3 : zone centrale avec BorderLayout ----
        jPanel3.setBackground(new java.awt.Color(0, 153, 153));
        jPanel3.setPreferredSize(new Dimension(500, 650));
        jPanel3.setLayout(new java.awt.BorderLayout());

        // jPanel5 : vue graphe (CENTER)
        vueDuGraphe.setPreferredSize(new Dimension(500, 400));
        jPanel3.add(vueDuGraphe, BorderLayout.CENTER);

        // jPanel6 : infos/résultats (SOUTH)
        fenetreInfo.setPreferredSize(new Dimension(500, 250));        
        
        jPanel3.add(fenetreInfo, BorderLayout.SOUTH);

        jPanel2.add(jPanel3);

        // ---- jPanel1 : panneau droit ----
        jPanel1.setPreferredSize(new Dimension(300, 650));
        jPanel1.setLayout(new BorderLayout());
        panneauAlgorithmes = new PanneauAlgorithmes();
        panneauAlgorithmes.setBorder(null);
        panneauAlgorithmes.setBackground(new Color(22, 27, 51));
        jPanel1.add(panneauAlgorithmes, BorderLayout.CENTER);
        jPanel2.add(jPanel1);

        getContentPane().add(jPanel2);

     // Menu Fichier
        jMenu1.setText("Fichier");
        javax.swing.JMenuItem itemCharger     = new javax.swing.JMenuItem("Charger un graphe");
        javax.swing.JMenuItem itemSauvegarder = new javax.swing.JMenuItem("Sauvegarder le graphe");
        itemCharger.addActionListener(e -> chargerGraphe());
        itemSauvegarder.addActionListener(e -> sauvegarderGraphe());
        jMenu1.add(itemCharger);
        jMenu1.add(itemSauvegarder);
        jMenuBar1.add(jMenu1);

        // Menu Description
        javax.swing.JMenu menuDescription = new javax.swing.JMenu("Description des algorithmes");
        javax.swing.JMenuItem itemDescription = new javax.swing.JMenuItem("Voir les descriptions");
        itemDescription.addActionListener(e -> 
            new FenetreDescription(FenetreApplication.this).setVisible(true)
        );
        menuDescription.add(itemDescription);
        jMenuBar1.add(menuDescription);
        
        
     // Menu Extras
        javax.swing.JMenu menuExtras = new javax.swing.JMenu("Problème réel");
        javax.swing.JMenuItem itemReseauRer = new javax.swing.JMenuItem("Simulateur Réseau RER");
        itemReseauRer.addActionListener(e -> new vue.FenetreReseauRer());
        menuExtras.add(itemReseauRer);
        jMenuBar1.add(menuExtras);
        setJMenuBar(jMenuBar1);

        pack();
    }             

    private void radioOrienteActionPerformed(java.awt.event.ActionEvent evt) {                                             
        this.oriente = true;
    }                                            

    private void radioNonOrienteActionPerformed(java.awt.event.ActionEvent evt) {                                                
        this.oriente = false;
    }                                               

    private void nombreSommetsActionPerformed(java.awt.event.ActionEvent evt) {                                              
        String texte = nombreSommets.getText().trim();
        boutonGenerer.setEnabled(!texte.isEmpty() && texte.matches("\\d+") && Integer.parseInt(texte) > 0);        
    }              

    private void boutonGenererActionPerformed(ActionEvent evt) {                                              
        creerEtAfficherGraphe();
        
    }                                           

    private void mettreAJourBouton() {
        String texte = nombreSommets.getText().trim();
        boolean valide = !texte.isEmpty() && texte.matches("\\d+") && Integer.parseInt(texte) > 0;
        boutonGenerer.setEnabled(valide);     
    }

    private void creerEtAfficherGraphe() {
        this.pondere = radioOui.isSelected();
        int nbSommets = Integer.parseInt(nombreSommets.getText().trim());
        String typeDonnees = (String) comboDonnees.getSelectedItem();

        // 1. Créer le graphe
        if (this.pondere) {
            if (this.oriente) {
                if (typeDonnees.equals("Integer")) {
                    this.graphe = new GrapheOrientePondere<Integer>(nbSommets);
                } else {
                    this.graphe = new GrapheOrientePondere<String>(nbSommets);
                }
            } else {
                if (typeDonnees.equals("Integer")) {
                    this.graphe = new GrapheNonOrientePondere<Integer>(nbSommets);
                } else {
                    this.graphe = new GrapheNonOrientePondere<String>(nbSommets);
                }
            }
        } else {
            if (this.oriente) {
                if (typeDonnees.equals("Integer")) {
                    this.graphe = new GrapheOriente<Integer>(nbSommets);
                } else {
                    this.graphe = new GrapheOriente<String>(nbSommets);
                }
            } else {
                if (typeDonnees.equals("Integer")) {
                    this.graphe = new GrapheNonOriente<Integer>(nbSommets);
                } else {
                    this.graphe = new GrapheNonOriente<String>(nbSommets);
                }
            }
        }

        GestionnaireGraphe.getInstance().setGrapheCourant(graphe);
        
        // 2. Ajouter les sommets AVANT de créer la VueGraphe
        if (typeDonnees.equals("Integer")) {
            for (int i = 1; i <= nbSommets; i++) {
                ((Graphe<Integer>) this.graphe).ajouterSommet(i);
            }
        } else {
            for (int i = 1; i <= nbSommets; i++) {
                ((Graphe<String>) this.graphe).ajouterSommet("S" + i);
            }
        }

     // 4. Créer ou mettre à jour la VueGraphe
        if (vueGraphe == null) {
            vueGraphe = new VueGraphe(graphe);

            vueDuGraphe.setLayout(new BorderLayout());
            vueDuGraphe.add(vueGraphe, BorderLayout.CENTER);            
        } else {
            vueGraphe.setGraphe(graphe);
        }

        vueGraphe.addPropertyChangeListener("grapheModifie", e -> rafraichirInfo());
        rafraichirInfo();
        vueDuGraphe.revalidate();
        vueDuGraphe.repaint();
    }
    
    private void ouvrirFenetreOrdonnancement() {
        // Désactiver les champs inutiles
        nombreSommets.setEnabled(false);
        comboDonnees.setEnabled(false);
        radioOui.setEnabled(false);
        radioNon.setEnabled(false);

        // Ouvrir la fenêtre de saisie
        FenetreOrdonnancement fenetre = new FenetreOrdonnancement(this);
        fenetre.setVisible(true);

        // Récupérer le résultat après fermeture
        Ordonnancement ordonnancement = fenetre.getResultat();

        // Réactiver les champs
        nombreSommets.setEnabled(true);
        comboDonnees.setEnabled(true);
        radioOui.setEnabled(true);
        radioNon.setEnabled(true);

        if (ordonnancement == null) return; // annulé

        // Mettre à jour le graphe courant
        this.graphe = ordonnancement;
        GestionnaireGraphe.getInstance().setGrapheCourant(ordonnancement);

        // Afficher dans VueGraphe
        if (vueGraphe == null) {
            vueGraphe = new VueGraphe(ordonnancement);
            vueDuGraphe.setLayout(new BorderLayout());
            vueDuGraphe.add(vueGraphe, BorderLayout.CENTER);
        } else {
            vueGraphe.setGraphe(ordonnancement);
        }

        vueDuGraphe.revalidate();
        vueDuGraphe.repaint();
        rafraichirInfo();

        JOptionPane.showMessageDialog(this,
            "Ordonnancement créé avec " + ordonnancement.getOrdre() + " tâche(s) !\n" +
            "Sélectionnez 'Gantt (ordonnancement)' dans le panneau algorithmes.",
            "Succès", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void chargerGraphe() {
    	 
    	    
        javax.swing.JFileChooser fc = new javax.swing.JFileChooser();
        fc.setDialogTitle("Charger un graphe");
        fc.setCurrentDirectory(new java.io.File(
            System.getProperty("user.dir") + "/src/ressources"
        ));
        
        if (fc.showOpenDialog(this) != javax.swing.JFileChooser.APPROVE_OPTION) return;
        
        String fichier = fc.getSelectedFile().getName();
        
        try {
            // 1. Lire les métadonnées et créer le bon type de graphe
            String[] donnees = Graphe.donneesGraphe(fichier);
            Graphe<?> grapheCharge = Graphe.typeGraphe(donnees);
            grapheCharge.charger(fichier);
            
            // 2. Mettre à jour l'état interne de la fenêtre
            this.graphe = grapheCharge;
            this.oriente = grapheCharge.isEstOriente();
            this.pondere = grapheCharge.isEstPondere();
            GestionnaireGraphe.getInstance().setGrapheCourant(grapheCharge);
            
            // 3. Synchroniser les contrôles UI avec le graphe chargé
            if (this.oriente) radioOriente.setSelected(true);
            else              radioNonOriente.setSelected(true);
            if (this.pondere) radioOui.setSelected(true);
            else              radioNon.setSelected(true);
            nombreSommets.setText(String.valueOf(grapheCharge.getOrdre()));
            comboDonnees.setSelectedItem(donnees[0]);
            
            // 4. Créer la VueGraphe si elle n'existe pas encore (cas du chargement direct)
            if (vueGraphe == null) {
                vueGraphe = new VueGraphe(grapheCharge);
                vueDuGraphe.setLayout(new BorderLayout());
                vueDuGraphe.add(vueGraphe, BorderLayout.CENTER);
            } else {
                vueGraphe.setGraphe(grapheCharge);
            }
            
            // 5. Forcer le rafraichissement
            vueDuGraphe.revalidate();
            vueDuGraphe.repaint();
            
            javax.swing.JOptionPane.showMessageDialog(this,
                "Graphe chargé : " + fichier,
                "Succès", javax.swing.JOptionPane.INFORMATION_MESSAGE);
                
        } catch (Exception ex) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Erreur lors du chargement : " + ex.getMessage(),
                "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        rafraichirInfo();
    }
    private void sauvegarderGraphe() {
        if (graphe == null) {
            javax.swing.JOptionPane.showMessageDialog(this,
                "Aucun graphe à sauvegarder !",
                "Erreur", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        JFileChooser fc = new javax.swing.JFileChooser();
        fc.setDialogTitle("Sauvegarder le graphe");
        fc.setCurrentDirectory(new java.io.File(
            System.getProperty("user.dir") + "/src/ressources"
        ));
        fc.setSelectedFile(new java.io.File("monGraphe.txt"));
        
        if (fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        
        String fichier = fc.getSelectedFile().getName();
        graphe.sauvegarder(fichier);
        
        javax.swing.JOptionPane.showMessageDialog(this,
            "Graphe sauvegardé : " + fichier,
            "Succès", javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void rafraichirInfo() {
        if (fenetreInfo != null)
            fenetreInfo.mettreAJour(this.graphe);
    }   

    private static final Logger logger = Logger.getLogger(FenetreApplication.class.getName());
    private VueGraphe vueGraphe;  
    private FenetreInfo fenetreInfo;
    private PanneauAlgorithmes panneauAlgorithmes;
    private Graphe<?> graphe; 
    private boolean oriente, pondere;
    private javax.swing.JButton boutonGenerer;
    private javax.swing.JComboBox<String> comboDonnees;
    private javax.swing.ButtonGroup groupRadioPondere;
    private javax.swing.ButtonGroup groupRadiotypeGraphe;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel vueDuGraphe;
    private javax.swing.JPanel info;
    private javax.swing.JTextField nombreSommets;
    private javax.swing.JRadioButton radioNon;
    private javax.swing.JRadioButton radioNonOriente;
    private javax.swing.JRadioButton radioOriente;
    private javax.swing.JRadioButton radioOui;
}
