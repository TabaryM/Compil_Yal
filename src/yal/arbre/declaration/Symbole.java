package yal.arbre.declaration;

public class Symbole {
    private String type;
    private int depl;

    /**
     * Instancie un symbole à ranger dans la table des symboles
     * @param type type de l'objet lié au symbole
     * @param depl déplacement par rapport au sommet de la pile initial
     */
    public Symbole(String type, int depl) {
        this.type = type;
        this.depl = depl;
    }

    /**
     * Retourne le type de l'objet décrit par le symbole
     * @return type
     */
    public String getType() {
        return type;
    }

    /**
     * Retourne le déplacement par rapport au sommet de la pile de l'espace mémoire où est stocké la valeur associé au sybole
     * @return depl
     */
    public int getDepl() {
        return depl;
    }
}
