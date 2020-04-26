package yal.arbre.gestionnaireTDS;

public class SymboleDeTableau extends SymboleDeVariable {

    /**
     * Instancie un symbole à ranger dans la table des symboles
     *
     * @param depl déplacement par rapport au sommet de la pile initial
     */
    public SymboleDeTableau(int depl) {
        super(depl, "tableau");
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public void depilageToMIPS(StringBuilder stringBuilder) {
        // On dépile (4*k)+4 (k la taille du tableau)
        // Récupèration de la taille du tableau -> Récupération l'adresse du pointeur -> récupération du déplacement de la variable dans la Table Locale
        stringBuilder.append("\tlw $t8, ");
        if(getNumBloc() == TDS.getInstance().getRacine().getNumBloc()){
            stringBuilder.append(getDepl());
            stringBuilder.append("($s7)");
        } else {
            stringBuilder.append(getDepl()-4);
            stringBuilder.append("($s2)");
        }
        stringBuilder.append("\n");
        // On a l'adresse du début du tableau dans $t8

        // On récupère la taille du tableau
        stringBuilder.append("\tlw $t8, ($t8)\n");

        // On calcul le nombre de mot a libérer
        stringBuilder.append("\taddi $t8, $t8, 2\n");
        stringBuilder.append("\tli $t7, 4\n");
        stringBuilder.append("\tmul $t8, $t8, $t7\n");

        // On retire toussa de la pile
        stringBuilder.append("\tadd $sp, $sp, $t8\n");
    }
}
