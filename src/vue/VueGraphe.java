package vue;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import modele.Arc;
import modele.Graphe;
import modele.GrapheNonOriente;
import modele.GrapheNonOrientePondere;
import modele.GrapheOrientePondere;
import modele.Sommet;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class VueGraphe extends JPanel {

	private Graphe<?> graphe;
	private int[][] positionsSommets;
	private int sommetSelectionne;
	private double zoom;
	private static int RAYON = 55;

	public VueGraphe(Graphe<?> graphe) {
		this.graphe = graphe;

		int nbSom = graphe.getAps()[0];
		positionsSommets = new int[nbSom][nbSom];
		placerSommetsSurGrilleStatique();

		sommetSelectionne = 1;
		zoom = 1.0;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(new BasicStroke(2));
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(Color.BLACK);

		List<PointID> cs = new ArrayList<>();

		int i = 0, r = RAYON - 1;

		for (int ligne = 0; ligne < positionsSommets.length; ligne++) {
			for (int col = 0; col < positionsSommets[ligne].length; col++) {
				if (positionsSommets[ligne][col] == 1) {
					int x = col * RAYON;
					int y = ligne * RAYON;
					cs.add(new PointID(new Point(x, y), i + 1));
					dessinerSommet(g2, graphe.getSommets().get(i++), x, y, r);
				}
			}
		}

		dessinerArcs(g2, cs);
	}

	public void dessinerSommet(Graphics2D g, Sommet<?> s, int x, int y, int r) {
		g.drawOval(x, y, r, r);

		String texte = String.valueOf(s.getDonnee().toString());
		FontMetrics fm = g.getFontMetrics();

		int textWidth = fm.stringWidth(texte);
		int textHeight = fm.getAscent();

		int xCentre = x + (r - textWidth) / 2;
		int yCentre = y + (r + textHeight) / 2;
				
		g.drawString(texte, xCentre, yCentre);

	}

	public void dessinerArc(Graphics2D g, Arc<?> s, List<PointID> cs) {

	    int idSource = s.getSource().getId();
	    int idDest = s.getDestination().getId();

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

	    double angle = Math.atan2(y2 - y1, x2 - x1);

	    double startX = x1 + rayon * Math.cos(angle);
	    double startY = y1 + rayon * Math.sin(angle);

	    double endX = x2 - rayon * Math.cos(angle);
	    double endY = y2 - rayon * Math.sin(angle);

	    g.setColor(Color.BLACK);
	    g.drawLine((int) startX, (int) startY, (int) endX, (int) endY);

	    if (graphe.isEstOriente()) {
	        dessinerFleche(g, endX, endY, angle);
	    }

	    dessinerPoids(g, s, x1, y1, x2, y2);
	}
	
	public void dessinerPoids(Graphics2D g, Arc<?> s, double x1, double y1, double x2, double y2) {

		if (!graphe.isEstPondere())
			return;

		double mx = (x1 + x2) / 2;
		double my = (y1 + y2) / 2;

		double dx = x2 - x1;
		double dy = y2 - y1;

		double length = Math.sqrt(dx * dx + dy * dy);
		if (length == 0)
			return;

		dx /= length;		
		dy /= length;
		dy += 1;

		double offset = 5;
		double px = -dy * offset;
		double py = dx * offset;

		double tx = mx + px;
		double ty = my + py;

		String poids = String.valueOf(s.getPoids());
		FontMetrics fm = g.getFontMetrics();

		int w = fm.stringWidth(poids);
		int h = fm.getAscent();
		
		g.setColor(Color.RED);
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
	

	private void placerSommetsSurGrilleAleatoire() {
	    int n = positionsSommets.length;
	    int sommetsRestants = n;

	    for (int[] ligne : positionsSommets)
	        Arrays.fill(ligne, 0);

	    Random r = new Random();
	    boolean lignePrecedenteComplete = false;

	    for (int[] ligne : positionsSommets) {
	        if (sommetsRestants == 0) break;

	        if (lignePrecedenteComplete) {
	            lignePrecedenteComplete = false;
	            continue;
	        }

	        boolean placer = r.nextBoolean();
	        if (placer) {
	            int un = r.nextInt(n);
	            ligne[un] = 1;
	            sommetsRestants--;

	            // 2ème sommet : seulement si il en reste et non adjacent
	            if (sommetsRestants > 0 && r.nextBoolean()) {
	                int deux;
	                int tentatives = 0;
	                do {
	                    deux = r.nextInt(n);
	                    tentatives++;
	                } while ((deux == un || Math.abs(deux - un) == 1) && tentatives < 100);

	                if (Math.abs(deux - un) > 1) { // placement valide
	                    ligne[deux] = 1;
	                    sommetsRestants--;
	                    lignePrecedenteComplete = true;
	                }
	            }
	        }
	    }

	    // Fallback
	    while (sommetsRestants != 0) {
	        int rx = r.nextInt(n);
	        int ry = r.nextInt(n);

	        if (positionsSommets[rx][ry] == 0) {
	            positionsSommets[rx][ry] = 1;
	            sommetsRestants--;
	        }
	    }
	}

	private void placerSommetsSurGrilleStatique() {
	    int n = positionsSommets.length;

	    for (int[] ligne : positionsSommets)
	        java.util.Arrays.fill(ligne, 0);

	    double cx = (n - 1) / 2.0; 
	    double cy = (n - 1) / 2.0;
	    double rayon = (n - 1) / 2.0 - 0.5;

	    int nbSommets = graphe.getSommets().size();

	    for (int i = 0; i < nbSommets; i++) {
	        double angle = 2 * Math.PI * i / nbSommets - Math.PI / 2; // commence en haut
	        int col = (int) Math.round(cx + rayon * Math.cos(angle));
	        int ligne = (int) Math.round(cy + rayon * Math.sin(angle));

	        col  = Math.max(0, Math.min(n - 1, col));
	        ligne = Math.max(0, Math.min(n - 1, ligne));

	        positionsSommets[ligne][col] = 1;
	    }
	}
	
	
	public static void affiche2D(int[][] tab) {
		for (int[] ligne : tab) {
			for (int val : ligne) {
				System.out.print(val + " ");
			}
			System.out.println();
		}
	}

	public static void main(String args[]) {
		GrapheOrientePondere<Integer> graphe1 = new GrapheOrientePondere<Integer>(8);

		for (int i = 1; i <= 8 ; i++) {
			graphe1.ajouterSommet(i);			
		}

		/*graphe1.ajouterArc("Mulhouse1", "Mulhouse2", 1);
		graphe1.ajouterArc("Mulhouse1", "Mulhouse5", 1);
		graphe1.ajouterArc("Mulhouse1", "Mulhouse6", 1);*/
		
		graphe1.ajouterArc(1, 2, 6);
		graphe1.ajouterArc(1, 6, 7);
		graphe1.ajouterArc(1, 3, 7);
		graphe1.ajouterArc(5, 4, 7);

		graphe1.ajouterArc(2, 4, 8);
		graphe1.ajouterArc(2, 3, 8);
		graphe1.ajouterArc(3, 4, 8);

		graphe1.ajouterArc(4, 7, 0);		


		VueGraphe vg = new VueGraphe(graphe1);

		//affiche2D(vg.positionsSommets);
		
		Button b = new Button("Replacer");
		b.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				vg.placerSommetsSurGrilleAleatoire();
				vg.repaint();
			} 
		});

		JFrame frame = new JFrame();
		frame.setSize(600, 600);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(vg, BorderLayout.CENTER);
		frame.add(b, BorderLayout.SOUTH);

		frame.setVisible(true);
	}

	private static class Point {
		private double x, y;

		public Point(double x, double y) {
			super();
			this.x = x;
			this.y = y;
		}

		public double getX() {
			return x;
		}

		public void setX(double x) {
			this.x = x;
		}

		public double getY() {
			return y;
		}

		public void setY(double y) {
			this.y = y;
		}

		@Override
		public int hashCode() {
			return Objects.hash(x, y);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Point other = (Point) obj;
			return Double.doubleToLongBits(x) == Double.doubleToLongBits(other.x)
					&& Double.doubleToLongBits(y) == Double.doubleToLongBits(other.y);
		}
	}

	private static class PointID {
		private Point p;
		private int id;

		public PointID(Point p, int id) {
			this.p = p;
			this.id = id;
		}

		public Point getP() {
			return p;
		}

		public void setP(Point p) {
			this.p = p;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		@Override
		public int hashCode() {
			return Objects.hash(id);
		}

		public static List<Integer> listID(List<PointID> cs) {
			List<Integer> ids = new ArrayList<Integer>();
			for (PointID c : cs) {
				ids.add(c.getId());
			}
			return ids;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			PointID other = (PointID) obj;
			return id == other.id;
		}

	}
}

