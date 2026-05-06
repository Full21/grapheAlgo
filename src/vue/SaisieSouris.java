package vue;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;

import modele.Arc;
import modele.Graphe;
import modele.GrapheNonOriente;
import modele.GrapheNonOrientePondere;
import modele.GrapheOriente;
import modele.GrapheOrientePondere;
import modele.Sommet;

public class SaisieSouris extends MouseAdapter implements MouseMotionListener {

    private final VueGraphe vueGraphe;
    private Sommet<?>       sommetSourceSelectionne;
    private Sommet<?>       sommetEnDeplacement;
    private boolean         estEnTrainDeDragger;

    public SaisieSouris(VueGraphe vueGraphe) {
        this.vueGraphe               = vueGraphe;
        this.sommetSourceSelectionne = null;
        this.sommetEnDeplacement     = null;
        this.estEnTrainDeDragger     = false;
    }

    
    @Override
    public void mousePressed(MouseEvent e) {
        if (javax.swing.SwingUtilities.isLeftMouseButton(e)) {
            Sommet<?> sommet = vueGraphe.trouverSommetClique(e.getX(), e.getY());
            if (sommet != null) {
                sommetEnDeplacement = sommet;
                estEnTrainDeDragger = false;
            }
        }
        
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (sommetEnDeplacement != null) {
            estEnTrainDeDragger = true;
            vueGraphe.deplacerSommet(sommetEnDeplacement, e.getX(), e.getY());
            vueGraphe.repaint();
        }
        vueGraphe.notifierModification();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (estEnTrainDeDragger) {
            sommetEnDeplacement = null;
            estEnTrainDeDragger = false;
            return;
        }
        sommetEnDeplacement = null;
        vueGraphe.notifierModification();
    }

    @Override
    public void mouseMoved(MouseEvent e) {}

   
    @Override
    public void mouseClicked(MouseEvent e) {
        if (estEnTrainDeDragger) return;

        Graphe<?> graphe = vueGraphe.getGraphe();
        if (graphe == null) return;

        int x = e.getX();
        int y = e.getY();

        Sommet<?> sommetClique = vueGraphe.trouverSommetClique(x, y);
        Arc<?>    arcClique    = vueGraphe.trouverArcClique(x, y);

        if (javax.swing.SwingUtilities.isRightMouseButton(e)) {
            sommetSourceSelectionne = null;
            vueGraphe.setSommetSourceSelectionne(null);

            if (sommetClique != null) {
                afficherMenuSommet(e, sommetClique);
            } else if (arcClique != null) {
                afficherMenuArc(e, arcClique);
            } else {
                afficherMenuFond(e);
            }
            return;
        }

        // Double clic gauche sur un sommet String → renommer
        if (e.getClickCount() == 2 && sommetClique != null) {
            if (sommetClique.getDonnee() instanceof String) {
                renommerSommet(sommetClique);
                return;
            }
        }

        // Clic simple gauche → arc uniquement (plus d'ajout de sommet)
        if (e.getClickCount() == 1 && sommetClique != null) {
            gererClicSommet(sommetClique);
        }

        vueGraphe.notifierModification();
    }

    private void gererClicSommet(Sommet<?> sommetClique) {
        Graphe<?> graphe = vueGraphe.getGraphe();

        if (sommetSourceSelectionne == null) {
            // Premier sommet sélectionné
            sommetSourceSelectionne = sommetClique;
            vueGraphe.setSommetSourceSelectionne(sommetClique);
            vueGraphe.repaint();
        } else {
            // Deuxième sommet sélectionné : on crée l'arc
            // (peut être le même sommet → crée une boucle)

            double poids = 1.0;
            if (graphe.isEstPondere()) {
                String input = JOptionPane.showInputDialog(
                    vueGraphe,
                    "Poids de l'arête entre " + sommetSourceSelectionne.getDonnee()
                        + " et " + sommetClique.getDonnee() + " :",
                    "Poids",
                    JOptionPane.PLAIN_MESSAGE);

                if (input == null) {
                    sommetSourceSelectionne = null;
                    vueGraphe.setSommetSourceSelectionne(null);
                    vueGraphe.repaint();
                    return;
                }

                try {
                    poids = Double.parseDouble(input.trim());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(vueGraphe,
                        "Poids invalide, veuillez entrer un nombre.",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                    sommetSourceSelectionne = null;
                    vueGraphe.setSommetSourceSelectionne(null);
                    vueGraphe.repaint();
                    return;
                }
            }

            ajouterArcDansGraphe(sommetSourceSelectionne.getDonnee(),
                                 sommetClique.getDonnee(), poids);

            sommetSourceSelectionne = null;
            vueGraphe.setSommetSourceSelectionne(null);
            vueGraphe.repaint();
        }
    }

    private void afficherMenuSommet(MouseEvent e, Sommet<?> sommet) {
        JPopupMenu menu = new JPopupMenu();
        JMenuItem itemSupprimer = new JMenuItem("Supprimer ce sommet");
        itemSupprimer.addActionListener(ev -> supprimerSommetGraphique(sommet));
        menu.add(itemSupprimer);
        menu.show(vueGraphe, e.getX(), e.getY());
    }

    private void afficherMenuArc(MouseEvent e, Arc<?> arc) {
        JPopupMenu menu = new JPopupMenu();
        Graphe<?> graphe = vueGraphe.getGraphe();

        JMenuItem itemSupprimer = new JMenuItem("Supprimer cet arc");
        itemSupprimer.addActionListener(ev -> supprimerArcGraphique(arc));
        menu.add(itemSupprimer);

        if (graphe.isEstOriente()) {
            JMenuItem itemInverser = new JMenuItem("Inverser la direction");
            itemInverser.addActionListener(ev -> inverserArcGraphique(arc));
            menu.add(itemInverser);
        }

        if (graphe.isEstPondere()) {
            JMenuItem itemPoids = new JMenuItem("Modifier le poids");
            itemPoids.addActionListener(ev -> modifierPoidsArcGraphique(arc));
            menu.add(itemPoids);
        }

        menu.show(vueGraphe, e.getX(), e.getY());
    }

    private void afficherMenuFond(MouseEvent e) {
        JPopupMenu menu = new JPopupMenu();

        JMenuItem itemEffacer = new JMenuItem("Effacer le graphe");
        itemEffacer.addActionListener(ev -> effacerGraphe());
        menu.add(itemEffacer);

        JMenuItem itemAjouterSommet = new JMenuItem("Ajouter un sommet ici");
        itemAjouterSommet.addActionListener(ev -> ajouterSommetGraphique(e.getX(), e.getY()));
        menu.add(itemAjouterSommet);

        menu.show(vueGraphe, e.getX(), e.getY());
    }
  
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void ajouterArcDansGraphe(Object donnee1, Object donnee2, double poids) {
        Graphe<?> graphe = vueGraphe.getGraphe();

        if (arcExisteDeja(donnee1, donnee2)) {
            JOptionPane.showMessageDialog(vueGraphe,
                "Cet arc existe déjà.", "Information",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (graphe.isEstOriente()) {
            if (graphe.isEstPondere()) {
                ((GrapheOrientePondere) graphe).ajouterArc(donnee1, donnee2, poids);
            } else {
                ((GrapheOriente) graphe).ajouterArc(donnee1, donnee2);
            }
        } else {
            if (graphe.isEstPondere()) {
                ((GrapheNonOrientePondere) graphe).ajouterArc(donnee1, donnee2, poids);
            } else {
                ((GrapheNonOriente) graphe).ajouterArc(donnee1, donnee2);
            }
        }
    }

    private boolean arcExisteDeja(Object donnee1, Object donnee2) {
        Graphe<?> graphe = vueGraphe.getGraphe();
        for (Arc<?> a : graphe.getArcs()) {
            if (a.getSource().getDonnee().equals(donnee1)
                && a.getDestination().getDonnee().equals(donnee2)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void ajouterSommetGraphique(int x, int y) {
        Graphe<?> graphe = vueGraphe.getGraphe();

        Object nouvelleDonnee = genererNouvelleDonneeSommet();
        agrandirMatricesGraphe(graphe);
        ((Graphe) graphe).ajouterSommet(nouvelleDonnee);

        vueGraphe.ajouterPositionSommet(x, y);
        vueGraphe.repaint();
    }

    private void agrandirMatricesGraphe(Graphe<?> graphe) {
        int[][] ancienneAdj  = graphe.getMatriceAdjacence();
        int     ancienneTaille = ancienneAdj.length;
        int     nouvelleTaille = ancienneTaille + 1;

        int[][] nouvelleAdj = new int[nouvelleTaille][nouvelleTaille];
        for (int i = 0; i < ancienneTaille; i++)
            for (int j = 0; j < ancienneTaille; j++)
                nouvelleAdj[i][j] = ancienneAdj[i][j];
        graphe.setMatriceAdjacence(nouvelleAdj);

        if (graphe instanceof GrapheOrientePondere) {
            agrandirMatricePoidsOriente((GrapheOrientePondere<?>) graphe, ancienneTaille, nouvelleTaille);
        } else if (graphe instanceof GrapheNonOrientePondere) {
            agrandirMatricePoidsNonOriente((GrapheNonOrientePondere<?>) graphe, ancienneTaille, nouvelleTaille);
        }
    }

    private void agrandirMatricePoidsOriente(GrapheOrientePondere<?> graphe,
                                             int ancienneTaille, int nouvelleTaille) {
        double[][] ancien  = graphe.getMatricePoids();
        double[][] nouveau = new double[nouvelleTaille][nouvelleTaille];
        for (int i = 0; i < ancienneTaille && i < ancien.length; i++)
            for (int j = 0; j < ancienneTaille && j < ancien[i].length; j++)
                nouveau[i][j] = ancien[i][j];
        graphe.setMatricePoids(nouveau);
    }

    private void agrandirMatricePoidsNonOriente(GrapheNonOrientePondere<?> graphe,
                                                int ancienneTaille, int nouvelleTaille) {
        double[][] ancien  = graphe.getMatricePoids();
        double[][] nouveau = new double[nouvelleTaille][nouvelleTaille];
        for (int i = 0; i < ancienneTaille && i < ancien.length; i++)
            for (int j = 0; j < ancienneTaille && j < ancien[i].length; j++)
                nouveau[i][j] = ancien[i][j];
        graphe.setMatricePoids(nouveau);
    }

    private Object genererNouvelleDonneeSommet() {
        Graphe<?> graphe = vueGraphe.getGraphe();

        if (graphe.getSommets().isEmpty()) return Integer.valueOf(1);

        Object exemple = graphe.getSommets().get(0).getDonnee();

        if (exemple instanceof Integer) {
            int max = 0;
            for (Sommet<?> s : graphe.getSommets()) {
                int val = (Integer) s.getDonnee();
                if (val > max) max = val;
            }
            return Integer.valueOf(max + 1);
        } else {
            return "S" + (graphe.getSommets().size() + 1);
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void supprimerSommetGraphique(Sommet<?> sommet) {
        Graphe<?> graphe = vueGraphe.getGraphe();

        int idSupprime = sommet.getId();

        ((Graphe) graphe).supprimerArcs(sommet.getDonnee());
        ((Graphe) graphe).supprimerSommet(sommet.getDonnee());

        java.util.List<Sommet<?>> sommetsRestants =
            (java.util.List<Sommet<?>>)(java.util.List<?>) graphe.getSommets();

        for (int i = 0; i < sommetsRestants.size(); i++)
            sommetsRestants.get(i).setId(i + 1);
        Sommet.setNB_SOMMETS(sommetsRestants.size());

        reconstruireMatricesApresSuppression(graphe);

        vueGraphe.supprimerPositionSommet(idSupprime);
        vueGraphe.repaint();
    }

    private void reconstruireMatricesApresSuppression(Graphe<?> graphe) {
        int n = graphe.getSommets().size();

        int[][] nouvelleAdj = new int[n + 1][n + 1];
        nouvelleAdj[0][0] = n;
        nouvelleAdj[0][1] = graphe.getArcs().size();

        for (Arc<?> a : graphe.getArcs()) {
            int i = a.getSource().getId();
            int j = a.getDestination().getId();
            if (i >= 1 && i <= n && j >= 1 && j <= n)
                nouvelleAdj[i][j] = 1;
        }
        graphe.setMatriceAdjacence(nouvelleAdj);

        if (graphe instanceof GrapheOrientePondere) {
            double[][] nouvellePoids = new double[n + 1][n + 1];
            for (Arc<?> a : graphe.getArcs()) {
                int i = a.getSource().getId();
                int j = a.getDestination().getId();
                if (i >= 1 && i <= n && j >= 1 && j <= n)
                    nouvellePoids[i][j] = a.getPoids();
            }
            ((GrapheOrientePondere<?>) graphe).setMatricePoids(nouvellePoids);
        } else if (graphe instanceof GrapheNonOrientePondere) {
            double[][] nouvellePoids = new double[n + 1][n + 1];
            for (Arc<?> a : graphe.getArcs()) {
                int i = a.getSource().getId();
                int j = a.getDestination().getId();
                if (i >= 1 && i <= n && j >= 1 && j <= n) {
                    nouvellePoids[i][j] = a.getPoids();
                    nouvellePoids[j][i] = a.getPoids();
                }
            }
            ((GrapheNonOrientePondere<?>) graphe).setMatricePoids(nouvellePoids);
        }

        graphe.construireFsEtAps();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void supprimerArcGraphique(Arc<?> arc) {
        Graphe<?> graphe = vueGraphe.getGraphe();
        Object src  = arc.getSource().getDonnee();
        Object dest = arc.getDestination().getDonnee();

        ((Graphe) graphe).supprimerArc(src, dest);
        if (!graphe.isEstOriente())
            ((Graphe) graphe).supprimerArc(dest, src);

        vueGraphe.repaint();
    }

    private void inverserArcGraphique(Arc<?> arc) {
        Graphe<?> graphe = vueGraphe.getGraphe();
        if (!graphe.isEstOriente()) return;

        Object src   = arc.getSource().getDonnee();
        Object dest  = arc.getDestination().getDonnee();
        double poids = arc.getPoids();

        @SuppressWarnings({ "unchecked", "rawtypes" })
        Graphe g = (Graphe) graphe;
        g.supprimerArc(src, dest);
        ajouterArcDansGraphe(dest, src, poids);

        vueGraphe.repaint();
    }

    private void modifierPoidsArcGraphique(Arc<?> arc) {
        String input = JOptionPane.showInputDialog(vueGraphe,
            "Nouveau poids :", String.valueOf(arc.getPoids()));
        if (input == null) return;

        try {
            double poids = Double.parseDouble(input.trim());
            arc.setPoids(poids);

            Graphe<?> graphe = vueGraphe.getGraphe();
            int i = arc.getSource().getId();
            int j = arc.getDestination().getId();

            if (graphe instanceof GrapheOrientePondere) {
                ((GrapheOrientePondere<?>) graphe).setPoids(i, j, poids);
            } else if (graphe instanceof GrapheNonOrientePondere) {
                ((GrapheNonOrientePondere<?>) graphe).setPoids(i, j, poids);
                ((GrapheNonOrientePondere<?>) graphe).setPoids(j, i, poids);
            }

            vueGraphe.repaint();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(vueGraphe,
                "Poids invalide.", "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void effacerGraphe() {
        Graphe<?> graphe = vueGraphe.getGraphe();
        if (graphe == null) return;

        int rep = JOptionPane.showConfirmDialog(vueGraphe,
            "Voulez-vous vraiment effacer tout le graphe ?",
            "Confirmation", JOptionPane.YES_NO_OPTION);
        if (rep != JOptionPane.YES_OPTION) return;

        graphe.getSommets().clear();
        graphe.getArcs().clear();
        Sommet.setNB_SOMMETS(0);
        graphe.setMatriceAdjacence(new int[2][2]);

        if (graphe instanceof GrapheOrientePondere)
            ((GrapheOrientePondere<?>) graphe).setMatricePoids(new double[2][2]);
        else if (graphe instanceof GrapheNonOrientePondere)
            ((GrapheNonOrientePondere<?>) graphe).setMatricePoids(new double[2][2]);

        // Pas d'appel à construireFsEtAps() : graphe vide = rien à reconstruire
        vueGraphe.reinitialiserPositions();
        vueGraphe.repaint();
    }
    
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void renommerSommet(Sommet<?> sommet) {
        String ancienNom = (String) sommet.getDonnee();
        String nouveauNom = JOptionPane.showInputDialog(
            vueGraphe,
            "Nouveau nom du sommet :",
            ancienNom
        );

        if (nouveauNom == null || nouveauNom.trim().isEmpty()) return;
        nouveauNom = nouveauNom.trim();

        // Vérifier que le nom n'existe pas déjà
        for (Sommet<?> s : vueGraphe.getGraphe().getSommets()) {
            if (s != sommet && s.getDonnee().equals(nouveauNom)) {
                JOptionPane.showMessageDialog(vueGraphe,
                    "Ce nom existe déjà.", "Erreur", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        ((Sommet<String>) sommet).setDonnee(nouveauNom);
        vueGraphe.repaint();
        vueGraphe.notifierModification();
    }
}