package vue;
import java.awt.*;
import javax.swing.*;
import modele.*;
import javax.swing.table.DefaultTableModel;

public class FenetreOrdonnancement extends JDialog {

    // Tableau de saisie des tâches
    private javax.swing.table.DefaultTableModel tableModel;
    private JTable table;
    
    // Résultat : l'ordonnancement créé (null si annulé)
    private Ordonnancement resultat = null;

    public FenetreOrdonnancement(JFrame parent) {
        super(parent, "Saisie des tâches - Ordonnancement", true);
        setSize(600, 500);
        setLocationRelativeTo(parent);
        getContentPane().setLayout(new BorderLayout(10, 10));
        initialiserComposants();
    }

    private void initialiserComposants() {
        // ── Titre ──
        JLabel titre = new JLabel("Saisissez vos tâches ci-dessous :", JLabel.CENTER);
        titre.setFont(new Font("Trebuchet MS", Font.BOLD, 14));
        titre.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        getContentPane().add(titre, BorderLayout.NORTH);

        // ── Tableau ──
        String[] colonnes = {"ID", "Nom", "Durée", "Antécédents (ex: 1,2)"};
        tableModel = new DefaultTableModel(
        	    new Object[][] {},
        	    new String[] {"ID", "Nom", "Durée", "Antécédents (ex: 1,2)"}
        	) {
        	    Class<?>[] columnTypes = new Class<?>[] {
        	        Integer.class, String.class, String.class, String.class
        	    };
        	    
        	    @Override
        	    public Class<?> getColumnClass(int columnIndex) {
        	        return columnTypes[columnIndex];
        	    }
        	    
        	    @Override
        	    public boolean isCellEditable(int row, int column) {
        	        return column != 0; // colonne ID non éditable
        	    }
        	};
        	table = new JTable(tableModel); // ← même tableModel !
        	table.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        	table.setRowHeight(28);
        table.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        table.setRowHeight(28);
        getContentPane().add(new JScrollPane(table), BorderLayout.CENTER);

        // ── Boutons ──
        JPanel panneauBas = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton boutonAjouter   = creerBouton("+ Ajouter une tâche", new Color(100, 180, 100));
        JButton boutonSupprimer = creerBouton("- Supprimer",          new Color(200, 80, 80));
        JButton boutonValider   = creerBouton("✔ Valider",            new Color(80, 120, 200));
        JButton boutonAnnuler   = creerBouton("✘ Annuler",            new Color(150, 150, 150));

        panneauBas.add(boutonAjouter);
        panneauBas.add(boutonSupprimer);
        panneauBas.add(boutonValider);
        panneauBas.add(boutonAnnuler);
        getContentPane().add(panneauBas, BorderLayout.SOUTH);

        // ── Actions ──
        boutonAjouter.addActionListener(e ->{
        	int prochainID = tableModel.getRowCount() + 1;
            tableModel.addRow(new Object[]{prochainID, "", "", ""});
        });

        boutonSupprimer.addActionListener(e -> {
            int ligne = table.getSelectedRow();
            if (ligne != -1) {
                tableModel.removeRow(ligne);
            } else {
                JOptionPane.showMessageDialog(this,
                    "Sélectionnez une ligne à supprimer.",
                    "Info", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        boutonAnnuler.addActionListener(e -> {
            resultat = null;
            dispose();
        });

        boutonValider.addActionListener(e -> valider());

        // Ligne vide par défaut
        tableModel.addRow(new Object[]{1, "", "", ""});
    }

    /**
     * Crée un bouton stylisé
     */
    private JButton creerBouton(String texte, Color couleur) {
        JButton btn = new JButton(texte);
        btn.setFont(new Font("Trebuchet MS", Font.BOLD, 12));
        btn.setBackground(couleur);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        return btn;
    }

    /**
     * Valide et crée l'ordonnancement
     */
    private void valider() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                "Ajoutez au moins une tâche !",
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Tache.NOMBE_DE_TACHES = 0;
        Ordonnancement ordonnancement = new Ordonnancement();

        try {
            for (int i = 0; i < tableModel.getRowCount(); i++) {
            	
                String nom    = tableModel.getValueAt(i, 1).toString().trim();
                String dureeS = tableModel.getValueAt(i, 2).toString().trim();
                String antsS  = tableModel.getValueAt(i, 3) != null
                                ? tableModel.getValueAt(i, 3).toString().trim()
                                : "";

                if (nom.isEmpty() || dureeS.isEmpty()) {
                    JOptionPane.showMessageDialog(this,
                        "Ligne " + (i + 1) + " : nom et durée obligatoires !",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int duree = Integer.parseInt(dureeS);

                int[] antecedents;
                if (antsS.isEmpty()) {
                    antecedents = new int[0];
                } else {
                    String[] parts = antsS.split(",");
                    antecedents    = new int[parts.length];
                    for (int j = 0; j < parts.length; j++) {
                        antecedents[j] = Integer.parseInt(parts[j].trim());
                    }
                }

                Tache t = new Tache(nom, duree, antecedents);
                ordonnancement.ajouterSommet(t);
            }

            // Succès → stocker le résultat et fermer
            this.resultat = ordonnancement;
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                "Durée et antécédents doivent être des nombres entiers.",
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Retourne l'ordonnancement créé, ou null si annulé
     */
    public Ordonnancement getResultat() {
        return resultat;
    }
}