package vue;

import java.awt.*;
import javax.swing.*;

public class FenetreDescription extends JDialog {

    public FenetreDescription(java.awt.Frame parent) {
        super(parent, "Description des algorithmes", true);
        setSize(800, 500);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        initialiserComposants();
    }

    private void initialiserComposants() {
        // Titre
        JLabel titre = new JLabel("Description des algorithmes", JLabel.CENTER);
        titre.setFont(new Font("Trebuchet MS", Font.BOLD, 18));
        titre.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titre, BorderLayout.NORTH);

        // Panneau central
        JPanel centre = new JPanel(new GridLayout(1, 2, 10, 0));
        centre.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

        // Liste des algos à gauche
        String[] algos = {
            "Dijkstra", "Dantzig", "Tarjan", "Graphe réduit", "Bases",
            "Kruskal", "Prüfer", "Rangs des sommets", "Distances",
            "Est connexe ?", "Est un arbre ?", "Gantt (ordonnancement)"
        };

        JList<String> listeAlgos = new JList<>(algos);
        listeAlgos.setFont(new Font("Trebuchet MS", Font.BOLD, 13));
        listeAlgos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listeAlgos.setSelectedIndex(0);

        JScrollPane scrollListe = new JScrollPane(listeAlgos);
        scrollListe.setBorder(BorderFactory.createTitledBorder("Algorithmes"));
        centre.add(scrollListe);

        // Zone de description à droite
        JTextArea zoneDesc = new JTextArea();
        zoneDesc.setEditable(false);
        zoneDesc.setLineWrap(true);
        zoneDesc.setWrapStyleWord(true);
        zoneDesc.setFont(new Font("Trebuchet MS", Font.PLAIN, 13));
        zoneDesc.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollDesc = new JScrollPane(zoneDesc);
        scrollDesc.setBorder(BorderFactory.createTitledBorder("Description"));
        centre.add(scrollDesc);

        add(centre, BorderLayout.CENTER);

        // Listener sur la liste
        listeAlgos.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                zoneDesc.setText(getDescription(listeAlgos.getSelectedValue()));
                zoneDesc.setCaretPosition(0);
            }
        });

        // Description par défaut
        zoneDesc.setText(getDescription("Dijkstra"));

        // Bouton fermer
        JButton boutonFermer = new JButton("Fermer");
        boutonFermer.setFont(new Font("Trebuchet MS", Font.BOLD, 12));
        boutonFermer.addActionListener(e -> dispose());
        JPanel panneauBas = new JPanel();
        panneauBas.add(boutonFermer);
        add(panneauBas, BorderLayout.SOUTH);
    }

    private String getDescription(String algo) {
        switch (algo) {
            case "Dijkstra":
                return "ALGORITHME DE DIJKSTRA\n\n" +
                    "Problématique :\n" +
                    "Trouver le plus court chemin entre tous les couples de sommets " +
                    "dans un graphe orienté pondéré.\n\n" +
                    "Principe :\n" +
                    "L'algorithme maintient un ensemble de sommets dont la distance " +
                    "minimale depuis la source est connue. À chaque étape, il sélectionne " +
                    "le sommet non visité le plus proche, puis met à jour les distances " +
                    "de ses voisins.\n\n" +
                    "Conditions d'application :\n" +
                    "- Graphe orienté pondéré\n" +
                    "- Tous les poids doivent être POSITIFS ou nuls\n\n" +
                    "Complexité : O(n²)\n\n" +
                    "Résultat : Matrice des plus courts chemins entre tous les couples";

            case "Dantzig":
                return "ALGORITHME DE DANTZIG\n\n" +
                    "Problématique :\n" +
                    "Calculer les plus courts chemins entre tous les couples de sommets " +
                    "d'un graphe orienté pondéré.\n\n" +
                    "Principe :\n" +
                    "L'algorithme ajoute les sommets un par un. Pour chaque nouveau sommet k+1, " +
                    "il met à jour les distances entre les anciens sommets en vérifiant si " +
                    "passer par k+1 offre un chemin plus court. Il détecte les circuits " +
                    "de poids négatif.\n\n" +
                    "Conditions d'application :\n" +
                    "- Graphe orienté pondéré\n" +
                    "- Accepte les poids négatifs\n" +
                    "- Ne fonctionne pas si circuit de poids négatif\n\n" +
                    "Complexité : O(n³)\n\n" +
                    "Résultat : Matrice des plus courtes distances";

            case "Tarjan":
                return "ALGORITHME DE TARJAN\n\n" +
                    "Problématique :\n" +
                    "Déterminer toutes les composantes fortement connexes (CFC) " +
                    "d'un graphe orienté.\n\n" +
                    "Principe :\n" +
                    "L'algorithme utilise un parcours en profondeur (DFS). Il numérote " +
                    "les sommets dans l'ordre de visite et calcule pour chaque sommet " +
                    "le plus petit numéro atteignable. Quand un sommet est racine de sa CFC, " +
                    "on dépile tous les sommets de cette composante.\n\n" +
                    "Conditions d'application :\n" +
                    "- Graphe orienté (pondéré ou non)\n" +
                    "- Les poids sont ignorés\n\n" +
                    "Complexité : O(n + m)\n\n" +
                    "Résultat : Liste des composantes fortement connexes";

            case "Graphe réduit":
                return "GRAPHE RÉDUIT\n\n" +
                    "Problématique :\n" +
                    "Construire le graphe réduit d'un graphe orienté à partir de ses CFC.\n\n" +
                    "Principe :\n" +
                    "Chaque CFC devient un seul sommet dans le graphe réduit. " +
                    "Un arc existe entre deux sommets du graphe réduit si et seulement si " +
                    "il existe au moins un arc entre les CFC correspondantes.\n\n" +
                    "Conditions d'application :\n" +
                    "- Graphe orienté\n" +
                    "- Nécessite d'avoir calculé les CFC (Tarjan) au préalable\n\n" +
                    "Résultat : Un graphe orienté sans circuit (DAG)";

            case "Bases":
                return "BASES DU GRAPHE\n\n" +
                    "Problématique :\n" +
                    "Trouver les bases d'un graphe orienté, c'est-à-dire les sommets " +
                    "sans prédécesseur.\n\n" +
                    "Principe :\n" +
                    "Un sommet est une base si son demi-degré intérieur est nul " +
                    "(aucun arc n'entre dans ce sommet).\n\n" +
                    "Conditions d'application :\n" +
                    "- Graphe orienté\n\n" +
                    "Complexité : O(n + m)\n\n" +
                    "Résultat : Liste des sommets sans prédécesseur";

            case "Kruskal":
                return "ALGORITHME DE KRUSKAL\n\n" +
                    "Problématique :\n" +
                    "Trouver un arbre couvrant de poids minimal dans un graphe " +
                    "non orienté pondéré connexe.\n\n" +
                    "Principe :\n" +
                    "On trie tous les arcs par poids croissant. On ajoute chaque arc " +
                    "à l'arbre couvrant si et seulement si il ne crée pas de cycle. " +
                    "On s'arrête quand l'arbre contient n-1 arcs.\n\n" +
                    "Conditions d'application :\n" +
                    "- Graphe NON orienté pondéré\n" +
                    "- Le graphe doit être connexe\n\n" +
                    "Complexité : O(m log m)\n\n" +
                    "Résultat : Arbre couvrant de poids minimal";

            case "Prüfer":
                return "CODAGE DE PRÜFER\n\n" +
                    "Problématique :\n" +
                    "Encoder un arbre étiqueté en une séquence unique de n-2 entiers.\n\n" +
                    "Principe :\n" +
                    "À chaque étape, on trouve la feuille de plus petit label, " +
                    "on ajoute son voisin dans la séquence, puis on supprime cette feuille. " +
                    "On répète jusqu'à ce qu'il ne reste que 2 sommets.\n\n" +
                    "Conditions d'application :\n" +
                    "- Graphe NON orienté\n" +
                    "- Le graphe doit être un ARBRE\n\n" +
                    "Complexité : O(n²)\n\n" +
                    "Résultat : Séquence de n-2 entiers";

            case "Rangs des sommets":
                return "CALCUL DES RANGS\n\n" +
                    "Problématique :\n" +
                    "Déterminer le rang de chaque sommet dans un graphe orienté sans circuit.\n\n" +
                    "Principe :\n" +
                    "Les sommets sans prédécesseur ont le rang 0. On les supprime et " +
                    "on recommence : les nouveaux sommets sans prédécesseur ont le rang 1, etc.\n\n" +
                    "Conditions d'application :\n" +
                    "- Graphe orienté\n" +
                    "- Le graphe NE DOIT PAS contenir de circuit\n\n" +
                    "Complexité : O(n + m)\n\n" +
                    "Résultat : Rang de chaque sommet (entier >= 0)";

            case "Distances":
                return "CALCUL DES DISTANCES (BFS)\n\n" +
                    "Problématique :\n" +
                    "Calculer le nombre minimum d'arcs entre tous les couples " +
                    "de sommets d'un graphe orienté.\n\n" +
                    "Principe :\n" +
                    "On applique un parcours en largeur (BFS) depuis chaque sommet. " +
                    "Le BFS explore les sommets par niveaux.\n\n" +
                    "Conditions d'application :\n" +
                    "- Graphe orienté\n" +
                    "- Les poids sont ignorés\n\n" +
                    "Complexité : O(n × (n + m))\n\n" +
                    "Résultat : Matrice des distances (-1 si pas de chemin)";

            case "Est connexe ?":
                return "TEST DE CONNEXITÉ\n\n" +
                    "Problématique :\n" +
                    "Vérifier si un graphe non orienté est connexe.\n\n" +
                    "Principe :\n" +
                    "On effectue un DFS depuis le premier sommet. " +
                    "Si tous les sommets sont visités, le graphe est connexe.\n\n" +
                    "Conditions d'application :\n" +
                    "- Graphe NON orienté\n\n" +
                    "Complexité : O(n + m)\n\n" +
                    "Résultat : CONNEXE ou NON CONNEXE";

            case "Est un arbre ?":
                return "TEST D'ARBRE\n\n" +
                    "Problématique :\n" +
                    "Vérifier si un graphe non orienté est un arbre.\n\n" +
                    "Principe :\n" +
                    "Un graphe est un arbre si et seulement si :\n" +
                    "1. Il est CONNEXE\n" +
                    "2. Il a exactement n-1 arcs\n\n" +
                    "Conditions d'application :\n" +
                    "- Graphe NON orienté\n\n" +
                    "Complexité : O(n + m)\n\n" +
                    "Résultat : UN ARBRE ou PAS UN ARBRE";

            case "Gantt (ordonnancement)":
                return "ORDONNANCEMENT ET DIAGRAMME DE GANTT\n\n" +
                    "Problématique :\n" +
                    "Planifier des tâches avec contraintes de précédence " +
                    "et afficher le résultat sous forme de diagramme de Gantt.\n\n" +
                    "Principe :\n" +
                    "1. Dates au plus tôt : pour chaque tâche, date de début = " +
                    "max des dates de fin de ses prédécesseurs.\n\n" +
                    "2. Dates au plus tard : pour chaque tâche, date de début au plus tard = " +
                    "min des dates au plus tard de ses successeurs - sa durée.\n\n" +
                    "3. Marge = dateTard - dateTot\n\n" +
                    "4. Chemin critique = tâches avec marge = 0\n\n" +
                    "Conditions d'application :\n" +
                    "- Graphe orienté sans circuit\n" +
                    "- Sommets = tâches avec durée\n\n" +
                    "Résultat : Diagramme de Gantt avec chemins critiques en rouge";

            default:
                return "Sélectionnez un algorithme dans la liste.";
        }
    }
}