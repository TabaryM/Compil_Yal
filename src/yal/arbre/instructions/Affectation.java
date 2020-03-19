package yal.arbre.instructions;

import yal.arbre.gestionnaireTDS.*;
import yal.arbre.expressions.Expression;
import yal.exceptions.AnalyseSemantiqueException;

public class Affectation extends Instruction {
    private String idf;
    private Expression e;

    public Affectation(Expression e, String idf, int n) {
        super(n);
        this.idf = idf;
        this.e = e;
    }

    @Override
    protected String getNomInstruction() {
        return "Affectation";
    }

    @Override
    public void verifier() {
        e.verifier();
        if(TDS.getInstance().identifier(new Entree("entier_"+idf)) == null){
            AnalyseSemantiqueException exception = new AnalyseSemantiqueException(getNoLigne(), "Variable "+idf+" non déclarée");
            ErreurSemantique.getInstance().ajouter(exception);
        }
    }

    @Override
    public String toMIPS() {
        StringBuilder stringBuilder = new StringBuilder();
        // Commentaire du code mips
        stringBuilder.append("\t#Assigner à ");
        stringBuilder.append(idf).append(" la valeur ");
        stringBuilder.append(e.toString());
        stringBuilder.append("\n");

        // chargement en mémoire de l'expression
        stringBuilder.append(e.toMIPS());

        // Récupération du déplacement en mémoire de la variable
        Entree entree = new Entree("entier_"+idf);
        SymboleDeVariable tmp = (SymboleDeVariable) TDS.getInstance().identifier(entree);
        // Stockage en mémoire de la variable
        stringBuilder.append("\tsw $v0, ");
        if(TDS.getInstance().getTableCourrante() == TDS.getInstance().getRacine()){
            stringBuilder.append(tmp.getDepl());
            stringBuilder.append("($s7)");
        } else {
            stringBuilder.append(tmp.getDepl()-8);
            stringBuilder.append("($s2)");
        }

        stringBuilder.append("\n\n");
        return stringBuilder.toString();
    }

    @Override
    public boolean contientRetourne() {
        return false;
    }
}
