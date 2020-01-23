package yal.arbre.declaration;

public class Entree {
    private String idf;

    public Entree(String idf){
        this.idf = idf;
    }

    public String getIdf() {
        return idf;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Entree entree = (Entree) o;

        return getIdf().equals(entree.getIdf());
    }

    @Override
    public int hashCode() {
        return getIdf().hashCode();
    }
}
