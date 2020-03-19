package yal.arbre.declaration;

import yal.arbre.ArbreAbstrait;

public abstract class Declaration extends ArbreAbstrait {
    private String idf;
    public Declaration(String idf, int noLigne) {
        super(noLigne);
        this.idf = idf;
    }

    public abstract void ajouterTDS();

    public String getIdf() {
        return idf;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Declaration{");
        sb.append("idf='").append(idf).append('\'');
        sb.append(", noLigne=").append(noLigne);
        sb.append('}');
        return sb.toString();
    }
}

