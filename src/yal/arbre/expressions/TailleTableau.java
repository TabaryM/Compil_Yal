package yal.arbre.expressions;

public class TailleTableau extends Expression {

    private String identifiant;

    public TailleTableau(String idf, int n) {
        super(n);
        this.identifiant = idf;
    }

    @Override
    public String getType() {
        return "entier";
    }

    @Override
    public void verifier() {
        //TODO : vérifier que le tableau existe
    }

    @Override
    public String toMIPS() {
        return null;
    }

    @Override
    public boolean contientRetourne() {
        return false;
    }
}
