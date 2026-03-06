package modele;

public interface ISauvegardable {
	
	void sauvegarder(String fichier);
	void charger(String fichier);
	void exporter(String fichier);
}
