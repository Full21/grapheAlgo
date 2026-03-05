package modele;

public abstract class Arc<T> {
	
	
    protected Sommet<T> source;
    protected Sommet<T> destination;
    protected double    poids;


    public Arc(Sommet<T> source, Sommet<T> destination, double poids) {
        this.source      = source;
        this.destination = destination;
        this.poids       = poids;
    }


    public Sommet<T> getSource()      { 
    	return source; 
    }
    public Sommet<T> getDestination() { 
    	return destination; 
    }
    public double    getPoids()       { 
    	return poids; 
    }


    public abstract Arc<T> inverse();

}
