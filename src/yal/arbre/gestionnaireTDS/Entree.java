package yal.arbre.gestionnaireTDS;

public class Entree {
    private String idf;
    private int nbParam;

    /**
     * Instancie une nouvelle entrée pour la TDS
     * @param idf identifiant de l'entrée dans la TDS
     */
    public Entree(String idf){
        this.idf = idf;
    }

    /**
     * Instancie une nouvelle entrée pour la TDS
     * @param idf identifiant de l'entrée dans la TDS
     */
    public Entree(String idf, int nbParam){
        this.idf = idf;
        this.nbParam = nbParam;
    }

    /**
     * Retourne l'identifiant de l'entrée
     * @return idf
     */
    public String getIdf() {
        return idf;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Entree entree = (Entree) o;

        if (nbParam != entree.nbParam) return false;
        return getIdf() != null ? getIdf().equals(entree.getIdf()) : entree.getIdf() == null;
    }

    @Override
    public int hashCode() {
        int result = getIdf() != null ? getIdf().hashCode() : 0;
        result = 31 * result + nbParam;
        return result;
    }

    @Override
    public String toString() {
        return idf;
    }
}
