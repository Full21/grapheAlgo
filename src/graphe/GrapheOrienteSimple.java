package graphe;

public class GrapheOrienteSimple<T> extends GrapheOriente<T> {

	
	public GrapheOrienteSimple() {};
	
	@Override
	public void afficher() {
		
		for(Sommet<T> s : sommets) {
		List<T> tableausommet = new Sommet[s.getVoisins];
		System.out.print(a);
		 for(Sommet<T> b : tableausommet) {
			 System.out.print("->");
			 System.out.print(b);
		 }
		}
	};
	/*afficher un graphe orienté sous forme de string
	 * un graphe orienté est défini par son nombre de sommets et son 
	 * nombre d'arc
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Sommets : ");
	    sb.append(sommets.size())
	    sb.append(", "Arcs : ");
	    sb.append(arcs.size());
			
     	sb.toString();
	    return sb;
		
		}

}
