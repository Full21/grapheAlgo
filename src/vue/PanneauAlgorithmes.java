package vue;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import modele.*;

public class PanneauAlgorithmes extends JPanel {
    
    private JComboBox<String> choixAlgo;
    private JTextField champSommetDepart;
    private JTextField champSommetArrivee;
    private JButton boutonLancer;
    private JTextArea zoneDescription;
    private JTextArea zoneResultat;
    private JLabel labelDepart;
    private JLabel labelArrivee;
    
    public PanneauAlgorithmes() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createTitledBorder("Algorithmes"));
        
        JLabel titre = new JLabel("Choisir un algorithme :");
        titre.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(titre);
        
        String[] algos = {
            "Dijkstra", "Dantzig", "Tarjan", "Graphe réduit", "Bases",
            "Kruskal", "Prüfer", "Rangs des sommets", "Distances",
            "Est connexe ?", "Est un arbre ?", "Gantt (ordonnancement)"
        };
        choixAlgo = new JComboBox<>(algos);
        choixAlgo.setMaximumSize(new Dimension(300, 30));
        choixAlgo.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(choixAlgo);
        
        add(Box.createVerticalStrut(10));
        
        labelDepart = new JLabel("Sommet de départ (id) :");
        add(labelDepart);
        champSommetDepart = new JTextField();
        champSommetDepart.setMaximumSize(new Dimension(300, 30));
        champSommetDepart.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(champSommetDepart);
        
        add(Box.createVerticalStrut(5));
        
        labelArrivee = new JLabel("Sommet d'arrivée (id) :");
        add(labelArrivee);
        champSommetArrivee = new JTextField();
        champSommetArrivee.setMaximumSize(new Dimension(300, 30));
        champSommetArrivee.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(champSommetArrivee);
        
        add(Box.createVerticalStrut(10));
        
        boutonLancer = new JButton("Lancer l'algorithme");
        boutonLancer.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(boutonLancer);
        
        add(Box.createVerticalStrut(10));
        
        add(new JLabel("Description :"));
        zoneDescription = new JTextArea(4, 20);
        zoneDescription.setEditable(false);
        zoneDescription.setLineWrap(true);
        zoneDescription.setWrapStyleWord(true);
        zoneDescription.setBackground(new Color(240, 240, 240));
        JScrollPane scrollDesc = new JScrollPane(zoneDescription);
        scrollDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollDesc.setMaximumSize(new Dimension(300, 100));
        add(scrollDesc);
        
        add(Box.createVerticalStrut(10));
        
        add(new JLabel("Résultat :"));
        zoneResultat = new JTextArea(12, 20);
        zoneResultat.setEditable(false);
        zoneResultat.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollRes = new JScrollPane(zoneResultat);
        scrollRes.setAlignmentX(Component.LEFT_ALIGNMENT);
        add(scrollRes);
        
        choixAlgo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String algo = (String) choixAlgo.getSelectedItem();
                afficherDescription(algo);
                ajusterChampsSommets(algo);
            }
        });
        
        boutonLancer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                lancerAlgorithme();
            }
        });
        
        afficherDescription("Dijkstra");
        ajusterChampsSommets("Dijkstra");
    }
    
    private void ajusterChampsSommets(String algo) {
        boolean besoinDepart = algo.equals("Dijkstra");
        boolean besoinArrivee = algo.equals("Dijkstra");
        labelDepart.setVisible(besoinDepart);
        champSommetDepart.setVisible(besoinDepart);
        labelArrivee.setVisible(besoinArrivee);
        champSommetArrivee.setVisible(besoinArrivee);
        revalidate();
        repaint();
    }
    
    private void afficherDescription(String algo) {
        String d = "";
        switch (algo) {
            case "Dijkstra": d = "Plus court chemin entre deux sommets. Poids positifs uniquement."; break;
            case "Dantzig": d = "Plus courts chemins entre tous les couples de sommets. Accepte les poids négatifs."; break;
            case "Tarjan": d = "Composantes fortement connexes d'un graphe orienté."; break;
            case "Graphe réduit": d = "Construit le graphe réduit à partir des CFC de Tarjan."; break;
            case "Bases": d = "Sommets sans prédécesseur (demi-degré intérieur nul)."; break;
            case "Kruskal": d = "Arbre couvrant de poids minimal. Graphe non orienté pondéré."; break;
            case "Prüfer": d = "Codage d'un arbre en une séquence de n-2 entiers."; break;
            case "Rangs des sommets": d = "Calcule le rang de chaque sommet (graphe orienté sans circuit)."; break;
            case "Distances": d = "Matrice des distances (nombre d'arcs) entre tous les couples."; break;
            case "Est connexe ?": d = "Vérifie si un graphe non orienté est connexe."; break;
            case "Est un arbre ?": d = "Vérifie si un graphe non orienté est un arbre."; break;
            case "Gantt (ordonnancement)": d = "Affiche le diagramme de Gantt et les chemins critiques."; break;
        }
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
                case "Dijkstra": lancerDijkstra(graphe); break;
                case "Dantzig": lancerDantzig(graphe); break;
                case "Tarjan": lancerTarjan(graphe); break;
                case "Graphe réduit": lancerGrapheReduit(graphe); break;
                case "Bases": lancerBases(graphe); break;
                case "Kruskal": lancerKruskal(graphe); break;
                case "Prüfer": lancerPrufer(graphe); break;
                case "Rangs des sommets": lancerRangs(graphe); break;
                case "Distances": lancerDistances(graphe); break;
                case "Est connexe ?": lancerEstConnexe(graphe); break;
                case "Est un arbre ?": lancerEstArbre(graphe); break;
                case "Gantt (ordonnancement)": lancerGantt(graphe); break;
            }
        } catch (Exception ex) {
            zoneResultat.setText("Erreur : " + ex.getMessage());
        }
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void lancerDijkstra(Graphe<?> g) {
        if (!(g instanceof GrapheOrientePondere)) { erreur("Dijkstra nécessite un graphe orienté pondéré."); return; }
        GrapheOrientePondere gp = (GrapheOrientePondere) g;
        if (!gp.verifierConditions()) { erreur("Dijkstra nécessite des poids positifs."); return; }
        try {
            int s = Integer.parseInt(champSommetDepart.getText().trim());
            int[] d = new int[gp.getOrdre() + 1];
            int[] pred = new int[gp.getOrdre() + 1];
            double[][] matricePoidsD = gp.getMatricePoids();
            int[][] matricePoids = new int[matricePoidsD.length][matricePoidsD.length];
            for (int i = 0; i < matricePoidsD.length; i++)
                for (int j = 0; j < matricePoidsD.length; j++)
                    matricePoids[i][j] = (int) matricePoidsD[i][j];
            gp.djikstra(gp.getFs(), gp.getAps(), matricePoids, s, d, pred);
            StringBuilder sb = new StringBuilder("Dijkstra depuis le sommet " + s + " :\n\nDistances minimales :\n");
            for (int i = 1; i <= gp.getOrdre(); i++) sb.append("  vers ").append(i).append(" : ").append(d[i]).append("\n");
            zoneResultat.setText(sb.toString());
        } catch (NumberFormatException e) { erreur("Sommet de départ invalide."); }
        catch (Exception e) { erreur(e.getMessage()); }
    }
    
    private void lancerDantzig(Graphe<?> g) {
        if (!(g instanceof GrapheOrientePondere)) { erreur("Dantzig nécessite un graphe orienté pondéré."); return; }
        GrapheOrientePondere<?> gp = (GrapheOrientePondere<?>) g;
        double[][] c = gp.initialiserMatriceDistances();
        boolean ok = gp.dantzig(c);
        if (!ok) { zoneResultat.setText("Circuit de poids négatif détecté !"); return; }
        int n = gp.getOrdre();
        StringBuilder sb = new StringBuilder("Matrice des plus courtes distances :\n\n     ");
        for (int j = 1; j <= n; j++) sb.append(String.format("%6d ", j));
        sb.append("\n");
        for (int i = 1; i <= n; i++) {
            sb.append(String.format("%3d: ", i));
            for (int j = 1; j <= n; j++) {
                if (c[i][j] == Double.POSITIVE_INFINITY) sb.append("     ∞ ");
                else sb.append(String.format("%6.1f ", c[i][j]));
            }
            sb.append("\n");
        }
        zoneResultat.setText(sb.toString());
    }
    
    private void lancerTarjan(Graphe<?> g) {
        if (!(g instanceof GrapheOriente)) { erreur("Tarjan nécessite un graphe orienté."); return; }
        GrapheOriente<?> go = (GrapheOriente<?>) g;
        List<? extends List<?>> cfc = go.tarjan();
        StringBuilder sb = new StringBuilder("Composantes fortement connexes :\n\n");
        for (int i = 0; i < cfc.size(); i++) sb.append("CFC ").append(i + 1).append(" : ").append(cfc.get(i)).append("\n");
        zoneResultat.setText(sb.toString());
    }
    
    private void lancerGrapheReduit(Graphe<?> g) {
        if (!(g instanceof GrapheOriente)) { erreur("Nécessite un graphe orienté."); return; }
        GrapheOriente<?> reduit = ((GrapheOriente<?>) g).grapheReduit();
        zoneResultat.setText("Graphe réduit :\n" + reduit.toString());
    }
    
    private void lancerBases(Graphe<?> g) {
        if (!(g instanceof GrapheOriente)) { erreur("Nécessite un graphe orienté."); return; }
        zoneResultat.setText("Bases :\n" + ((GrapheOriente<?>) g).getBases());
    }
    
    private void lancerKruskal(Graphe<?> g) {
        if (!(g instanceof GrapheNonOrientePondere)) { erreur("Kruskal nécessite un graphe non orienté pondéré."); return; }
        Graphe<?> arbre = ((GrapheNonOrientePondere<?>) g).kruskal();
        zoneResultat.setText("Arbre couvrant minimal (Kruskal) :\n" + arbre.toString());
    }
    
    private void lancerPrufer(Graphe<?> g) {
        if (!(g instanceof GrapheNonOriente)) { erreur("Prüfer nécessite un graphe non orienté."); return; }
        try {
            int[] code = ((GrapheNonOriente<?>) g).codagePrufer(g.getMatriceAdjacence());
            StringBuilder sb = new StringBuilder("Codage de Prüfer :\n[");
            for (int i = 1; i < code.length; i++) {
                sb.append(code[i]);
                if (i < code.length - 1) sb.append(", ");
            }
            sb.append("]");
            zoneResultat.setText(sb.toString());
        } catch (Exception e) { erreur(e.getMessage()); }
    }
    
    private void lancerRangs(Graphe<?> g) {
        if (!(g instanceof GrapheOriente)) { erreur("Nécessite un graphe orienté."); return; }
        GrapheOriente<?> go = (GrapheOriente<?>) g;
        go.calculerRangs();
        StringBuilder sb = new StringBuilder("Rangs des sommets :\n");
        for (int i = 1; i <= go.getOrdre(); i++) {
            Sommet<?> s = go.trouverSommet(i);
            if (s != null) {
                sb.append("  ").append(s.getDonnee()).append(" : rang ").append(s.getRang()).append("\n");
            }
        }
        zoneResultat.setText(sb.toString());
    }
    
    private void lancerDistances(Graphe<?> g) {
        if (!(g instanceof GrapheOriente)) { erreur("Nécessite un graphe orienté."); return; }
        int[][] dist = ((GrapheOriente<?>) g).calculerDistances();
        StringBuilder sb = new StringBuilder("Matrice des distances :\n\n");
        for (int i = 0; i < dist.length; i++) {
            sb.append(i + 1).append(" : ");
            for (int j = 0; j < dist.length; j++) sb.append(dist[i][j] == -1 ? "∞ " : dist[i][j] + " ");
            sb.append("\n");
        }
        zoneResultat.setText(sb.toString());
    }
    
    private void lancerEstConnexe(Graphe<?> g) {
        if (!(g instanceof GrapheNonOriente)) { erreur("Nécessite un graphe non orienté."); return; }
        boolean c = ((GrapheNonOriente<?>) g).estConnexe();
        zoneResultat.setText("Le graphe est " + (c ? "CONNEXE" : "NON CONNEXE"));
    }
    
    private void lancerEstArbre(Graphe<?> g) {
        if (!(g instanceof GrapheNonOriente)) { erreur("Nécessite un graphe non orienté."); return; }
        boolean a = ((GrapheNonOriente<?>) g).estArbre();
        zoneResultat.setText("Le graphe est " + (a ? "UN ARBRE" : "PAS UN ARBRE"));
    }
    
    private void lancerGantt(Graphe<?> g) {
        if (!(g instanceof Ordonnancement)) { erreur("Nécessite un graphe de type Ordonnancement."); return; }
        Ordonnancement o = (Ordonnancement) g;
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        java.io.PrintStream ps = new java.io.PrintStream(baos);
        java.io.PrintStream ancien = System.out;
        System.setOut(ps);
        o.afficherGantt();
        System.setOut(ancien);
        zoneResultat.setText(baos.toString());
    }
    
    private void erreur(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur", JOptionPane.ERROR_MESSAGE);
        zoneResultat.setText("Erreur : " + message);
    }
}