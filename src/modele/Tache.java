package modele;

public class Tache {

	private int numero;
	private String nom;
	private int duree;
	private int[] antecedents;
	private int dateTot;
	private int dateTard;
	private int marge;
	
	public boolean isCritique() {
		return dateTot == dateTard;
	}
	
	public int getMarge() {
		return this.marge;
	}
	
	
}
