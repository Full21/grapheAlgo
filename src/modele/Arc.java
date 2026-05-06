package modele;

public class Arc<T> {
	
	
    protected Sommet<T> source;
    protected Sommet<T> destination;
    protected double    poids;


    public Arc(Sommet<T> source, Sommet<T> destination, double poids) {
        this.source      = source;
        this.destination = destination;
        this.poids       = poids;
    }
    
    public Arc(Sommet<T> source, Sommet<T> destination) {
    	this(source, destination, 0);
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

    public void setPoids(double poids) {
		this.poids = poids;
	}

	public Arc<T> inverse() {
    	return new Arc<T>(this.destination, this.source, this.poids);
    }

    @Override
	public boolean equals(Object o) {
		if(o == null) return false;
		else if(o.getClass() != Arc.class) return false;
		else {
			Arc arc = (Arc<T>) o;			
			return (this.source.equals(arc.source) && this.destination.equals(arc.destination));
		}
			
	}
}
