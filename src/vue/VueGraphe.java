package vue;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JPanel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

import modele.Arc;
import modele.Graphe;
import modele.Sommet;


public class VueGraphe extends JPanel {

 
    private Graphe<?>  graphe;
    private double     zoom;
    private static int RAYON = 55;

    private int[] positionsX;
    private int[] positionsY;


    private Sommet<?> sommetSourceSelectionne;

    private SaisieSouris saisieSouris;

    public VueGraphe(Graphe<?> graphe) {
        this.graphe = graphe;
        int nbSom = graphe.getSommets().size();
        this.positionsX = new int[nbSom];
        this.positionsY = new int[nbSom];
        this.zoom = 1.0;
        this.sommetSourceSelectionne = null;

        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                placerSommetsSurGrilleStatique();
                repaint();
            }
        });

        setBackground(new Color(13, 17, 38));
        this.saisieSouris = new SaisieSouris(this);
        addMouseListener(this.saisieSouris);
        addMouseMotionListener(this.saisieSouris);
    }

    public VueGraphe() {
        this.positionsX = new int[0];
        this.positionsY = new int[0];
        this.zoom = 1.0;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (graphe == null) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.BLACK);

        List<PointID> cs = new ArrayList<>();
        int r = RAYON - 1;

        for (int i = 0; i < graphe.getSommets().size() && i < positionsX.length; i++) {
            int x = positionsX[i];
            int y = positionsY[i];
            cs.add(new PointID(new Point(x, y), graphe.getSommets().get(i).getId()));
            dessinerSommet(g2, graphe.getSommets().get(i), x, y, r);
        }

        dessinerArcs(g2, cs);
    }

    public void dessinerSommet(Graphics2D g, Sommet<?> s, int x, int y, int r) {
	   if (s == sommetSourceSelectionne) {
	        g.setColor(new Color(255, 64, 161)); 
	        g.fillOval(x, y, r, r);
	        g.setColor(new Color(255, 150, 200));
	        g.drawOval(x, y, r, r);
	    } else {
	        g.setColor(new Color(30, 38, 68));   
	        g.fillOval(x, y, r, r);
	        g.setColor(new Color(0, 229, 255)); 
	        g.drawOval(x, y, r, r);
	    }
	   
	   String texte = "";
	   if(s.getDonnee().getClass().getSimpleName().equalsIgnoreCase("Integer")) {
		   texte = String.valueOf(s.getDonnee().toString());
	   } else {
		   texte = String.valueOf(s.getDonnee().toString()+"("+s.getId()+")");
	   }
               
        FontMetrics fm = g.getFontMetrics();

        int textWidth  = fm.stringWidth(texte);
        int textHeight = fm.getAscent();

        int xCentre = x + (r - textWidth) / 2;
        int yCentre = y + (r + textHeight) / 2;
        
        g.setColor(new Color(230, 235, 245));
        g.drawString(texte, xCentre, yCentre);
    }

    public void dessinerArc(Graphics2D g, Arc<?> s, List<PointID> cs) {
        int idSource = s.getSource().getId();
        int idDest   = s.getDestination().getId();

        PointID p1 = null, p2 = null;
        for (PointID c : cs) {
            if (c.getId() == idSource) p1 = c;
            if (c.getId() == idDest)   p2 = c;
        }
        if (p1 == null || p2 == null) return;

        double rayon = (RAYON - 1) / 2.0;

        double x1 = p1.getP().getX() + rayon;
        double y1 = p1.getP().getY() + rayon;
        double x2 = p2.getP().getX() + rayon;
        double y2 = p2.getP().getY() + rayon;

        if (idSource == idDest) {
            dessinerBoucle(g, s, x1, y1, rayon);
            return;
        }

        double angle = Math.atan2(y2 - y1, x2 - x1);

        double startX = x1 + rayon * Math.cos(angle);
        double startY = y1 + rayon * Math.sin(angle);
        double endX   = x2 - rayon * Math.cos(angle);
        double endY   = y2 - rayon * Math.sin(angle);

        g.setColor(new Color(102, 240, 255));
        g.drawLine((int) startX, (int) startY, (int) endX, (int) endY);

        if (graphe.isEstOriente()) {
            dessinerFleche(g, endX, endY, angle);
        }

        dessinerPoids(g, s, x1, y1, x2, y2);
    }

    
    private void dessinerBoucle(Graphics2D g, Arc<?> s, double cx, double cy, double rayonSommet) {
        g.setColor(new Color(102, 240, 255));

       
        double rayonBoucle = rayonSommet * 0.6;
        double centreXBoucle = cx + rayonSommet * 0.7;
        double centreYBoucle = cy - rayonSommet * 0.7;

        int diam = (int) (rayonBoucle * 2);
        g.drawOval(
            (int) (centreXBoucle - rayonBoucle),
            (int) (centreYBoucle - rayonBoucle),
            diam, diam);

        // Si orienté, dessiner une petite flèche au point où la boucle "rentre"
        // dans le sommet (en bas à gauche du petit cercle)
        if (graphe.isEstOriente()) {
            double angleEntree = Math.toRadians(225);  // direction sud-ouest
            double fx = centreXBoucle + rayonBoucle * Math.cos(angleEntree);
            double fy = centreYBoucle + rayonBoucle * Math.sin(angleEntree);

            double angleFleche = angleEntree + Math.PI / 2;
            dessinerFleche(g, fx, fy, angleFleche);
        }

        // Afficher le poids à côté de la boucle si pondéré
        if (graphe.isEstPondere()) {
            g.setColor(new Color(255, 213, 0));
            String poids = String.valueOf(s.getPoids());
            FontMetrics fm = g.getFontMetrics();
            int w = fm.stringWidth(poids);
            g.drawString(poids,
                (int) (centreXBoucle - w / 2.0),
                (int) (centreYBoucle - rayonBoucle - 4));
        }
    }

    public void dessinerPoids(Graphics2D g, Arc<?> s, double x1, double y1, double x2, double y2) {
        if (!graphe.isEstPondere()) return;

        double mx = (x1 + x2) / 2;
        double my = (y1 + y2) / 2;
        double dx = x2 - x1;
        double dy = y2 - y1;
        double length = Math.sqrt(dx * dx + dy * dy);
        if (length == 0) return;

        dx /= length;
        dy /= length;
        dy += 1;

        double offset = 5;
        double px = -dy * offset;
        double py =  dx * offset;

        double tx = mx + px;
        double ty = my + py;

        String poids = String.valueOf(s.getPoids());
        FontMetrics fm = g.getFontMetrics();

        int w = fm.stringWidth(poids);
        int h = fm.getAscent();

        g.setColor(new Color(255, 213, 0));
        g.drawString(poids, (int) (tx - w / 2.0), (int) (ty + h / 2.0));
    }

    public void dessinerArcs(Graphics2D g, List<PointID> cs) {
        for (Arc<?> c : graphe.getArcs()) {
            dessinerArc(g, c, cs);
        }
    }

    private void dessinerFleche(Graphics2D g, double endX, double endY, double angle) {
        int taille = 10;
        double ouverture = Math.PI / 6;

        int x1 = (int) (endX - taille * Math.cos(angle - ouverture));
        int y1 = (int) (endY - taille * Math.sin(angle - ouverture));
        int x2 = (int) (endX - taille * Math.cos(angle + ouverture));
        int y2 = (int) (endY - taille * Math.sin(angle + ouverture));

        g.drawLine((int) endX, (int) endY, x1, y1);
        g.drawLine((int) endX, (int) endY, x2, y2);
    }

    public Sommet<?> trouverSommetClique(int mouseX, int mouseY) {
        if (graphe == null) return null;
        int r = RAYON - 1;
        for (int i = 0; i < graphe.getSommets().size() && i < positionsX.length; i++) {
            double cx = positionsX[i] + r / 2.0;
            double cy = positionsY[i] + r / 2.0;
            double dist = Math.sqrt(Math.pow(mouseX - cx, 2) + Math.pow(mouseY - cy, 2));
            if (dist <= r / 2.0) {
                return graphe.getSommets().get(i);
            }
        }
        return null;
    }

    public Arc<?> trouverArcClique(int mouseX, int mouseY) {
        if (graphe == null) return null;
        double rayon = (RAYON - 1) / 2.0;
        double tolerance = 8.0;

        for (Arc<?> arc : graphe.getArcs()) {
            int idSrc  = arc.getSource().getId();
            int idDest = arc.getDestination().getId();

            int idxSrc  = trouverIndexParId(idSrc);
            int idxDest = trouverIndexParId(idDest);
            if (idxSrc == -1 || idxDest == -1) continue;

            double x1 = positionsX[idxSrc]  + rayon;
            double y1 = positionsY[idxSrc]  + rayon;
            double x2 = positionsX[idxDest] + rayon;
            double y2 = positionsY[idxDest] + rayon;

            double dist = distancePointSegment(mouseX, mouseY, x1, y1, x2, y2);
            if (dist <= tolerance) {
                return arc;
            }
        }
        return null;
    }

    public void notifierModification() {
        repaint();
        firePropertyChange("grapheModifie", null, graphe);
    }
    
    private double distancePointSegment(double px, double py,
                                        double x1, double y1,
                                        double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        double lenSq = dx * dx + dy * dy;
        if (lenSq == 0) {
            return Math.hypot(px - x1, py - y1);
        }
        double t = ((px - x1) * dx + (py - y1) * dy) / lenSq;
        t = Math.max(0, Math.min(1, t));
        double projX = x1 + t * dx;
        double projY = y1 + t * dy;
        return Math.hypot(px - projX, py - projY);
    }
    
    public void deplacerSommet(Sommet<?> sommet, int mouseX, int mouseY) {
        int index = trouverIndexParId(sommet.getId());
        if (index == -1) return;

        positionsX[index] = mouseX - RAYON / 2;
        positionsY[index] = mouseY - RAYON / 2;

        positionsX[index] = Math.max(0, Math.min(getWidth()  - RAYON, positionsX[index]));
        positionsY[index] = Math.max(0, Math.min(getHeight() - RAYON, positionsY[index]));
    }

    public void placerSommetsSurGrilleAleatoire() {
	    if (graphe == null) return;
	    int nbSommets = graphe.getSommets().size();
	    int largeur = getWidth()  > 0 ? getWidth()  : 500;
	    int hauteur = getHeight() > 0 ? getHeight() : 400;
	
	    if (positionsX.length != nbSommets) {
	        positionsX = new int[nbSommets];
	        positionsY = new int[nbSommets];
	    }
	
	    Random rand = new Random();
	    for (int i = 0; i < nbSommets; i++) {
	        positionsX[i] = rand.nextInt(Math.max(1, largeur  - RAYON));
	        positionsY[i] = rand.nextInt(Math.max(1, hauteur - RAYON));
	    }
	}

	public int trouverIndexParId(int id) { // private → public
        for (int i = 0; i < graphe.getSommets().size(); i++) {
            if (graphe.getSommets().get(i).getId() == id) return i;
        }
        return -1;
    }

	private void placerSommetsSurGrilleStatique() {
        if (graphe == null) return;
        int nbSommets = graphe.getSommets().size();
        if (nbSommets == 0) {
            positionsX = new int[0];
            positionsY = new int[0];
            return;
        }

        int largeur = getWidth()  > 0 ? getWidth()  : 500;
        int hauteur = getHeight() > 0 ? getHeight() : 400;
        int tailleDispo = Math.min(largeur, hauteur);

        double rayonCercle = (tailleDispo / 2.0) * 0.85;

        double perimetre = 2 * Math.PI * rayonCercle;
        int rayonMax = (int) (perimetre / (nbSommets * 2.2));
        RAYON = Math.max(20, Math.min(55, rayonMax));

        double cx = largeur / 2.0;
        double cy = hauteur / 2.0;

        positionsX = new int[nbSommets];
        positionsY = new int[nbSommets];

        for (int i = 0; i < nbSommets; i++) {
            double angle = 2 * Math.PI * i / nbSommets - Math.PI / 2;
            positionsX[i] = (int) Math.round(cx + rayonCercle * Math.cos(angle)) - RAYON / 2;
            positionsY[i] = (int) Math.round(cy + rayonCercle * Math.sin(angle)) - RAYON / 2;
        }
    }
   
    /** Ajoute une position pour un nouveau sommet ajouté graphiquement. */
    public void ajouterPositionSommet(int x, int y) {
        // Centrer le sommet sur le point cliqué
        int xPlace = x - RAYON / 2;
        int yPlace = y - RAYON / 2;

        positionsX = Arrays.copyOf(positionsX, positionsX.length + 1);
        positionsY = Arrays.copyOf(positionsY, positionsY.length + 1);
        positionsX[positionsX.length - 1] = xPlace;
        positionsY[positionsY.length - 1] = yPlace;
    }

   
    public void supprimerPositionSommet(int idSupprime) {
  
        int indexASupprimer = idSupprime - 1;
        if (indexASupprimer < 0 || indexASupprimer >= positionsX.length) return;

        int n = positionsX.length;
        int[] newX = new int[n - 1];
        int[] newY = new int[n - 1];
        int k = 0;
        for (int i = 0; i < n; i++) {
            if (i == indexASupprimer) continue;
            newX[k] = positionsX[i];
            newY[k] = positionsY[i];
            k++;
        }
        positionsX = newX;
        positionsY = newY;
    }

    /** Réinitialise complètement les positions (graphe vidé). */
    public void reinitialiserPositions() {
        positionsX = new int[0];
        positionsY = new int[0];
    }

    public Graphe<?> getGraphe() {
        return graphe;
    }

    public void setGraphe(Graphe<?> grapheCourant) {
        this.graphe = grapheCourant;
        if (grapheCourant != null) {
            int nbSom = grapheCourant.getSommets().size();
            positionsX = new int[nbSom];
            positionsY = new int[nbSom];
            placerSommetsSurGrilleStatique();
        } else {
            positionsX = new int[0];
            positionsY = new int[0];
        }
        sommetSourceSelectionne = null;
    }

    public void setSommetSourceSelectionne(Sommet<?> s) {
        this.sommetSourceSelectionne = s;
    }

    public Sommet<?> getSommetSourceSelectionne() {
        return sommetSourceSelectionne;
    }

    public void centrerVue() {
        placerSommetsSurGrilleStatique();
        repaint();
    }

    public void mettreAJour() {
        repaint();
    }

    private static class Point {
        private double x, y;
        public Point(double x, double y) { this.x = x; this.y = y; }
        public double getX() { return x; }
        public double getY() { return y; }
        @Override public int hashCode() { return Objects.hash(x, y); }
        @Override public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Point)) return false;
            Point other = (Point) obj;
            return Double.doubleToLongBits(x) == Double.doubleToLongBits(other.x)
                && Double.doubleToLongBits(y) == Double.doubleToLongBits(other.y);
        }
    }

    private static class PointID {
        private Point p;
        private int id;
        public PointID(Point p, int id) { this.p = p; this.id = id; }
        public Point getP() { return p; }
        public int   getId() { return id; }
        @Override public int hashCode() { return Objects.hash(id); }
        @Override public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof PointID)) return false;
            return id == ((PointID) obj).id;
        }
    }
}
