package modele;

public class Sommet<T> {

	public static int nbSommets = 0;
	protected int id;
	protected T donnee;
	protected int rang;

	public Sommet(T donnee) {
		this.id = ++nbSommets;
		this.donnee = donnee;
		this.rang = -1;
	}

	public int getId() {
		return id;
	}

	public T getDonnee() {
		return donnee;
	}

	public int getRang() {
		return rang;
	}

	public void setRang(int r) {
		this.rang = r;
	}

	@Override
	public boolean equals(Object o) {
		if(o == null) return false;
		else if(o.getClass() != Sommet.class) return false;
		else {
			Sommet sommet = (Sommet<T>)o;			
			return this.donnee.equals(sommet.donnee);
		}
			
	}
	
}
