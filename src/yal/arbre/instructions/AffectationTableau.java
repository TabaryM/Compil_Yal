package yal.arbre.instructions;

import yal.arbre.expressions.Expression;
import yal.arbre.gestionnaireTDS.Entree;
import yal.arbre.gestionnaireTDS.SymboleDeTableau;
import yal.arbre.gestionnaireTDS.TDS;

public class AffectationTableau extends Affectation {

    private Expression indice;
    private Expression valeurAffectee;

    public AffectationTableau(Expression valeurAffectee, Expression indice, String idf, int n) {
        super(valeurAffectee, "tableau_"+idf, n);
        this.indice = indice;
        this.valeurAffectee = valeurAffectee;
    }

    @Override
    protected String getNomInstruction() {
        return "AffectationTableau";
    }

    @Override
    public void verifier() {
        // TODO verifier que les expressions sont entières
        // TODO verifier que le tableau est déclaré
    }

    @Override
    public String toMIPS() {
        StringBuilder stringBuilder = new StringBuilder();
        // On récupère le symbole du tableau
        SymboleDeTableau symboleDeTableau = (SymboleDeTableau)TDS.getInstance().identifier(new Entree(getIdf()));

        // valeur expression
        stringBuilder.append(valeurAffectee.toMIPS());
        stringBuilder.append("\tsw $v0, ($sp)\n\taddi $sp, $sp, -4\n");

        // indice (*4)+4
        stringBuilder.append("\t# Evaluation de l'indice\n");
        stringBuilder.append(indice.toMIPS());
        stringBuilder.append("\tsw $v0, ($sp)\n\taddi $sp, $sp, -4\n");

        // adresse de base du tableau
        stringBuilder.append("\tlw $v0, ");
        if(symboleDeTableau.getNumBloc() == TDS.getInstance().getRacine().getNumBloc()){
            stringBuilder.append(symboleDeTableau.getDepl());
            stringBuilder.append("($s7)");
        } else {
            stringBuilder.append(symboleDeTableau.getDepl()-4);
            stringBuilder.append("($s2)");
        }
        stringBuilder.append("\n");
        stringBuilder.append("\tsw $v0, ($sp)\n\taddi $sp, $sp, -4\n");

        stringBuilder.append("\tjal VerifIndiceTab\t# Verification que l'indice est valide\n"); // TODO ecrire la fonction de verification de validité d'indice de tableau
        // A partir d'ici, l'indice demandé est valide

/*
        // Empiler la valeur de l'expression à affecter
        stringBuilder.append("");
        stringBuilder.append("jal affectationTab\n");
*/
        return stringBuilder.toString();
    }
}
