package modele;

public abstract class Sommet<T> {
	
	 
    protected int id;
    protected T   donnee;
    protected int rang;

        public Sommet(int id, T donnee) {
        this.id     = id;
        this.donnee = donnee;
        this.rang   = -1;     }

    
    public int getId()         { 
    	return id; 
    }
    
    public T   getDonnee()     { 
    	return donnee; 
    }
    
    public int getRang()       { 
    	return rang; 
    }
    public void setRang(int r) { 
    	this.rang = r; 
    }

  


}
