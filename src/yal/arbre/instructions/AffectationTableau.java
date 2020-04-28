package yal.arbre.instructions;

import yal.arbre.expressions.Expression;
import yal.arbre.gestionnaireTDS.*;
import yal.exceptions.AnalyseSemantiqueException;

public class AffectationTableau extends Affectation {

    private Expression indice;
    private Expression valeurAffectee;

    public AffectationTableau(Expression valeurAffectee, Expression indice, String idf, int n) {
        super(valeurAffectee, idf, n);
        this.indice = indice;
        this.valeurAffectee = valeurAffectee;
    }

    @Override
    protected String getNomInstruction() {
        return "AffectationTableau";
    }

    @Override
    public void verifier() {
        Symbole symbole = TDS.getInstance().identifier(new Entree(getIdf()));
        if(symbole == null){
            AnalyseSemantiqueException exception = new AnalyseSemantiqueException(super.getNoLigne(), "Tableau "+getIdf()+" non déclaré.");
            ErreurSemantique.getInstance().ajouter(exception);
        }
        if(!indice.getType().equals("entier")){
            AnalyseSemantiqueException exception = new AnalyseSemantiqueException(getNoLigne(),
                    "Incompatibilité de type : l'indice d'un tableau doit être un entier");
            ErreurSemantique.getInstance().ajouter(exception);
        }
        if(!valeurAffectee.getType().equals("entier")){
            AnalyseSemantiqueException exception = new AnalyseSemantiqueException(getNoLigne(),
                    "Incompatibilité de type : la valeur affectée à une case d'un tableau doit être un entier");
            ErreurSemantique.getInstance().ajouter(exception);
        }
    }

    @Override
    public String toMIPS() {
        StringBuilder stringBuilder = new StringBuilder();
        // On récupère le symbole du tableau
        SymboleDeTableau symboleDeTableau = (SymboleDeTableau)TDS.getInstance().identifier(new Entree(getIdf()));

        // valeur expression
        stringBuilder.append(valeurAffectee.toMIPS());
        stringBuilder.append("\tsw $v0, ($sp)\n\taddi $sp, $sp, -4\n");

        // indice
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

        stringBuilder.append("\tjal VerifIndiceTab\t# Verification que l'indice est valide\n");
        // A partir d'ici, l'indice demandé est valide

        // Empiler la valeur de l'expression à affecter
        stringBuilder.append("jal AffectationTab\n");

        // Dépilage des éléments ajouté pour l'instruction
        // 4 pour l'adresse de la base du corps du tableau
        // 4 pour l'indice demandé
        // 4 pour la valeur affectée
        stringBuilder.append("\taddi $sp, $sp, 12\n");

        return stringBuilder.toString();
    }
}
