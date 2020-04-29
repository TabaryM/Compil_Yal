package yal.arbre.expressions.variable.declaration;

import yal.arbre.ArbreAbstrait;

public abstract class Declaration extends ArbreAbstrait {
    private String idf;
    private final String type;
    public Declaration(String idf, int noLigne, String type) {
        super(noLigne);
        this.idf = idf;
        this.type = type;
    }

    public abstract void ajouterTDS();

    public String getIdf() {
        return idf;
    }

    public String getType() {
        return type;
    }

    public abstract void initialisationDuCorpsDeLaVariable(StringBuilder stringBuilder);

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Declaration{");
        sb.append("idf='").append(idf).append('\'');
        sb.append(", noLigne=").append(noLigne);
        sb.append('}');
        return sb.toString();
    }
}

