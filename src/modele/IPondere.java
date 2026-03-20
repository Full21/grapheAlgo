package modele;

public interface IPondere {
    double getPoids(int i, int j);
    void setPoids(int i, int j, double poids);
    double[][] getMatricePoids();
}