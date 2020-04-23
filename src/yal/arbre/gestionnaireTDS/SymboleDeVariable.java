package yal.arbre.gestionnaireTDS;

public class SymboleDeVariable extends Symbole {
    private int depl;

    /**
     * Instancie un symbole à ranger dans la table des symboles
     *
     * @param depl déplacement par rapport au sommet de la pile initial
     */
    public SymboleDeVariable(int depl, String type) {
        super(type, TDS.getInstance().getNumBloc());
        this.depl = depl;
    }

    /**
     * Retourne le déplacement par rapport au sommet de la pile de l'espace mémoire où est stocké la valeur associé au sybole
     * @return depl
     */
    public int getDepl() {
        return depl;
    }

    public void setDepl(int depl) {
        this.depl = depl;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SymboleDeVariable{");
        sb.append("depl=").append(depl);
        sb.append(",type=entier");
        sb.append(",numBloc=").append(getNumBloc());
        sb.append('}');
        return sb.toString();
    }
}
