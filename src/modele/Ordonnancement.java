package modele;

import java.util.ArrayList;
import java.util.List;

public class Ordonnancement extends GrapheOriente<Tache> {

    private List<Tache> taches     = new ArrayList<Tache>();
    private int[]       fpcCritique;
    private int[]       appcCritique;

    public Ordonnancement(int nbtaches) {
        super(nbtaches);
    }

    public Ordonnancement() {
        super(100);
    }

    private Tache trouverTacheParNumero(int numero) {
        for (Tache t : taches) {
            if (t.getNumero() == numero) {
                return t;
            }
        }
        return null;
    }

    @Override
    public void ajouterSommet(Tache t) {
        taches.add(t);
        super.ajouterSommet(t);
        for (int ant : t.getAntecedents()) {
            Tache antecedent = trouverTacheParNumero(ant);
            if (antecedent != null) {
                ajouterArc(antecedent, t);
            }
        }
    }

    public void calculerDatesTot() {
        int n      = taches.size();
        int[] l    = new int[n + 1];
        int[] appc = new int[n + 1];
        int[] fpc  = new int[n + arcs.size() + 2];

        l[0]    = n;
        appc[0] = n;
        l[1]    = 0;
        fpc[1]  = 0;
        int kc  = 1;

        calculerRangs();
        ArrayList<Tache> tachesTriees = new ArrayList<>(taches);
        tachesTriees.sort((a, b) -> {
            int rangA = -1, rangB = -1;
            for (Sommet<Tache> s : sommets) {
                if (s.getDonnee().equals(a)) rangA = s.getRang();
                if (s.getDonnee().equals(b)) rangB = s.getRang();
            }
            return rangA - rangB;
        });

        for (int idx = 0; idx < tachesTriees.size(); idx++) {
            Tache t = tachesTriees.get(idx);
            int i   = t.getNumero();

            l[i]    = 0;
            appc[i] = kc + 1;

            for (int antId : t.getAntecedents()) {
                Tache ant = trouverTacheParNumero(antId);
                if (ant == null) continue;
                int j = ant.getNumero();
                int v = l[j] + ant.getDuree();

                if (v > l[i]) {
                    l[i]    = v;
                    kc      = appc[i] - 1;
                    kc++;
                    fpc[kc] = j;
                } else if (v == l[i] && v > 0) {
                    kc++;
                    fpc[kc] = j;
                }
            }

            kc++;
            fpc[kc] = 0;
            t.setDateTot(l[i]);
        }

        fpc[0]            = kc;
        this.fpcCritique  = fpc;
        this.appcCritique = appc;
    }

    public int[] calculerDatesTard() {
        calculerDatesTot();

        int n = taches.size();

        int[] datesTot  = new int[n + 1];
        int[] datesTard = new int[n + 1];

        for (Tache t : taches) {
            datesTot[t.getNumero()] = t.getDateTot();
        }

        int dureeProjet = 0;
        for (Tache t : taches) {
            if (datesTot[t.getNumero()] > dureeProjet) {
                dureeProjet = datesTot[t.getNumero()];
            }
        }

        for (int i = 1; i <= n; i++) {
            datesTard[i] = dureeProjet;
        }

        ArrayList<Tache> tachesInversees = new ArrayList<>(taches);
        tachesInversees.sort((a, b) -> {
            int rangA = -1, rangB = -1;
            for (Sommet<Tache> s : sommets) {
                if (s.getDonnee().equals(a)) rangA = s.getRang();
                if (s.getDonnee().equals(b)) rangB = s.getRang();
            }
            return rangB - rangA;
        });

        for (Tache t : tachesInversees) {
            for (Sommet<Tache> sommetVoisin : getVoisins(t)) {
                Tache successeur = sommetVoisin.getDonnee();
                int tard = datesTard[successeur.getNumero()] - t.getDuree();
                if (tard < datesTard[t.getNumero()]) {
                    datesTard[t.getNumero()] = tard;
                }
            }
            t.setDateTard(datesTard[t.getNumero()]);
        }

        return datesTard;
    }

    private void dfsCheminCritique(Tache courante,
                                   ArrayList<Tache> courant,
                                   ArrayList<ArrayList<Tache>> resultats) {
        boolean aSuccesseur = false;
        for (Sommet<Tache> sommetVoisin : getVoisins(courante)) {
            Tache voisin = sommetVoisin.getDonnee();
            if (voisin.getMarge() == 0
                && voisin.getDateTot() == courante.getDateTot() + courante.getDuree()) {
                aSuccesseur = true;
                courant.add(voisin);
                dfsCheminCritique(voisin, courant, resultats);
                courant.remove(courant.size() - 1);
            }
        }
        if (!aSuccesseur) {
            resultats.add(new ArrayList<>(courant));
        }
    }

    public ArrayList<ArrayList<Tache>> getCheminsCritiques() {
        ArrayList<ArrayList<Tache>> resultats     = new ArrayList<>();
        ArrayList<Tache>            cheminCourant = new ArrayList<>();

        for (Tache t : taches) {
            if (t.isCritique() && t.getAntecedents().length == 0) {
                cheminCourant.add(t);
                dfsCheminCritique(t, cheminCourant, resultats);
                cheminCourant.remove(cheminCourant.size() - 1);
            }
        }
        return resultats;
    }

    public void afficherGantt() {
        calculerDatesTard();

        int dureeProjet = 0;
        for (Tache t : taches)
            dureeProjet = Math.max(dureeProjet, t.getDateTot() + t.getDuree());

        System.out.println("DIAGRAMME DE GANTT");
        System.out.println("Tache\t\tDuree\tDate.tot\tDate.tard\tMarge\tGantt");
        System.out.println("-".repeat(60));

        for (Tache t : taches) {
            String barre = "";
            for (int i = 0; i < dureeProjet; i++) {
                if (i >= t.getDateTot() && i < t.getDateTot() + t.getDuree())
                    barre += t.isCritique() ? "X" : "-";
                else
                    barre += ".";
            }
            System.out.println(t.getNom() + "\t\t" + t.getDuree() + "\t"
                + t.getDateTot() + "\t\t" + t.getDateTard()
                + "\t\t" + t.getMarge() + "\t" + barre);
        }

        System.out.println("-".repeat(60));
        System.out.println("Durée totale du projet : " + dureeProjet);
        System.out.println("X = critique   - = non critique");

        System.out.println("\nChemin(s) critique(s) :");
        for (ArrayList<Tache> chemin : getCheminsCritiques()) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < chemin.size(); i++) {
                sb.append(chemin.get(i).getNom());
                if (i < chemin.size() - 1) sb.append(" → ");
            }
            System.out.println("  " + sb);
        }
    }

    public ArrayList<Tache> getTaches() {
        return new ArrayList<>(taches);
    }

    public int[] getFpcCritique() {
        return fpcCritique;
    }

    public void setFpcCritique(int[] fpcCritique) {
        this.fpcCritique = fpcCritique;
    }

    public int[] getAppcCritique() {
        return appcCritique;
    }

    public void setAppcCritique(int[] appcCritique) {
        this.appcCritique = appcCritique;
    }

   
}