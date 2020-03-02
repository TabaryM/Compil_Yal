package yal.arbre.expressions;

public class ConstanteEntiere extends Constante {
    
    public ConstanteEntiere(String texte, int n) {
        super(texte, n) ;
    }

    @Override
    public String toMIPS() {
        StringBuilder stringBuilder = new StringBuilder("\tli $v0, ");
        stringBuilder.append(super.toString());
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    @Override
    public boolean contientRetourne(boolean dansUneFonction) {
        return false;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
