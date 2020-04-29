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
        Symbole symbole = TDS.getInstance().identifier(new Entree(idf));
        if(symbole == null){
            AnalyseSemantiqueException exception = new AnalyseSemantiqueException(getNoLigne(), "Variable "+idf+" non déclarée");
            ErreurSemantique.getInstance().ajouter(exception);
        } else {
            if(!symbole.getType().equals(e.getType()) && e.getType() != null) {
                AnalyseSemantiqueException exception = new AnalyseSemantiqueException(getNoLigne()
                        , "Types incompatibles : variable " + symbole.getType() + " avec : " + e.getType());
                ErreurSemantique.getInstance().ajouter(exception);
            }
        }
    }

    @Override
    public String toMIPS() {
        StringBuilder stringBuilder = new StringBuilder();
        // Récupération du symbole de la variable
        Entree entree = new Entree(idf);
        SymboleDeVariable symboleDeVariable = (SymboleDeVariable) TDS.getInstance().identifier(entree);
        if(symboleDeVariable.getType().equals("entier")){
            // Commentaire du code mips
            stringBuilder.append("\t#Assigner à ");
            stringBuilder.append(idf).append(" la valeur ");
            stringBuilder.append(e.toString());
            stringBuilder.append("\n");

            // chargement en mémoire de l'expression
            stringBuilder.append(e.toMIPS());

            // Stockage en mémoire de la variable
            stringBuilder.append("\tsw $v0, ");
            if(symboleDeVariable.getNumBloc() == TDS.getInstance().getRacine().getNumBloc()){
                stringBuilder.append(symboleDeVariable.getDepl());
                stringBuilder.append("($s7)");
            } else {
                stringBuilder.append(symboleDeVariable.getDepl() - 4);
                stringBuilder.append("($s2)");
            }
        } else if(symboleDeVariable.getType().equals("tableau")){
            // Verifier que les tailles sont compatibles (mêmes tailles)
            stringBuilder.append("\t#On charge le pointeur du tableau original\n");
            stringBuilder.append(e.toMIPS());
            stringBuilder.append("\tsw $v0, ($sp)\n\taddi $sp, $sp, -4\n");

            stringBuilder.append("\t#On charge le pointeur de la copie\n");
            stringBuilder.append("\tlw $v0, ");
            if(symboleDeVariable.getNumBloc() == TDS.getInstance().getRacine().getNumBloc()){
                stringBuilder.append(symboleDeVariable.getDepl());
                stringBuilder.append("($s7)");
            } else {
                stringBuilder.append(symboleDeVariable.getDepl() - 4);
                stringBuilder.append("($s2)");
            }
            stringBuilder.append("\n\tsw $v0, ($sp)\n\taddi $sp, $sp, -4\n");

            stringBuilder.append("\tjal VerifDimTabs\n");
            // recopier case par case un tableau dans l'autre
            stringBuilder.append("\tjal CopieTab\n");

            // On dépile les pointeurs des tableaux
            stringBuilder.append("\taddi $sp, $sp, 8");
        }

        stringBuilder.append("\n\n");
        return stringBuilder.toString();
    }

    @Override
    public boolean contientRetourne() {
        return false;
    }

    public String getIdf(){
        return idf;
    }
}
