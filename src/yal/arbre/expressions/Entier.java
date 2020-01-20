package yal.arbre.expressions;

public class Entier extends Constante {

    public Entier(String texte, int n) {
        super(texte, n) ;
    }

    @Override
    public String toMIPS() {
        return super.toString();
    }

}
