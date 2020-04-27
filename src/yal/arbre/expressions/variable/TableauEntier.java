package yal.arbre.expressions.variable;

import yal.arbre.expressions.Expression;
import yal.arbre.expressions.Idf;
import yal.arbre.gestionnaireTDS.*;
import yal.exceptions.AnalyseSemantiqueException;

public class TableauEntier extends Idf {

    private Expression expression;

    public TableauEntier(String idf, Expression expression, int numLig) {
        super("tableau_"+idf, numLig);
        this.expression = expression;
    }

    @Override
    public String getType() {
        return "tableau";
    }

    @Override
    public void verifier() {
        Symbole symbole = TDS.getInstance().identifier(new Entree(getIdf()));
        if(symbole == null){
            AnalyseSemantiqueException exception = new AnalyseSemantiqueException(super.getNoLigne(), "Tableau "+getIdf()+" non déclaré.");
            ErreurSemantique.getInstance().ajouter(exception);
        }
        if(!expression.getType().equals("entier")){
            AnalyseSemantiqueException exception = new AnalyseSemantiqueException(super.getNoLigne(), "Format de l'indexe du tableau incorrecte. Attendue : entier\tReçu : "+expression.getType());
            ErreurSemantique.getInstance().ajouter(exception);
        }

    }

    @Override
    public String toMIPS() {
        StringBuilder stringBuilder = new StringBuilder();
        // On récupère le symbole du tableau
        SymboleDeTableau symboleDeTableau = (SymboleDeTableau)TDS.getInstance().identifier(new Entree(getIdf()));

        // indice
        stringBuilder.append("\t# Evaluation de l'indice\n");
        stringBuilder.append(expression.toMIPS());
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

        stringBuilder.append("\tjal VerifIndiceTab\t# Verification que l'indice est valide\n");
        // L'indice demandé est valide

        stringBuilder.append("\tlw $v0, 8($sp)\t# Récupération de l'indice demandé\n");
        stringBuilder.append("\taddi $v0, $v0, 1\t# Conversion de l'indice en décalage par rapport au début du corps du tableau\n");
        stringBuilder.append("\tli $t7, 4\n");
        stringBuilder.append("\tmul $v0, $v0, $t7\n");
        // Récupération de l'adresse de début du tableau
        stringBuilder.append("\tlw $t7, 4($sp)\t# Récupération de l'adresse du tableau\n");
        stringBuilder.append("\tsub $v0, $t7, $v0\t# Calcul de l'adresse de la case du tableau modifiée\n");
        stringBuilder.append("\tlw $v0, ($v0)\t# Récupération de la valeur de la case demandé du tableau\n");

        // Dépilage des éléments ajouté pour l'instruction
        // 4 pour l'indice demandé
        // 4 pour la valeur affectée
        stringBuilder.append("\taddi $sp, $sp, 8\n");
        return stringBuilder.toString();
    }

    @Override
    public boolean contientRetourne() {
        return false;
    }
}
