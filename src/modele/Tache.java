package modele;

public class Tache {

	private int numero;
	private String nom;
	private int duree;
	private int[] antecedents;
	private int dateTot;
	private int dateTard;
	public static int NOMBE_DE_TACHES = 0;
	
	
	public Tache(String nom, int duree, int dateTot, int dateTard, int... antecedents) {
		this.numero = ++NOMBE_DE_TACHES;
		this.nom = nom;
		this.duree = duree;
		this.antecedents = antecedents;
		this.dateTot = dateTot;
		this.dateTard = dateTard;
		
	}

	public boolean isCritique() {
		return dateTot == dateTard;
	}
	
	public int getMarge() {
		return dateTard - dateTot;
	}

	public int getNumero() {
		return numero;
	}

	public void setNumero(int numero) {
		this.numero = numero;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public int getDuree() {
		return duree;
	}

	public void setDuree(int duree) {
		this.duree = duree;
	}

	public int[] getAntecedents() {
		return antecedents;
	}

	public void setAntecedents(int[] antecedents) {
		this.antecedents = antecedents;
	}

	public int getDateTot() {
		return dateTot;
	}

	public void setDateTot(int dateTot) {
		this.dateTot = dateTot;
	}

	public int getDateTard() {
		return dateTard;
	}

	public void setDateTard(int dateTard) {
		this.dateTard = dateTard;
	}
	
	
}
