package yal.arbre.instructions;

import yal.FabriqueDeNumero;
import yal.arbre.expressions.Expression;
import yal.arbre.gestionnaireTDS.ErreurSemantique;
import yal.exceptions.AnalyseSemantiqueException;

public class Ecrire extends Instruction {

    protected Expression exp ;

    public Ecrire (Expression e, int n) {
        super(n) ;
        exp = e ;
    }

    @Override
    public void verifier() {
        exp.verifier();
        if(exp.getType() != null){
            if(exp.getType().equals("tableau")){
                AnalyseSemantiqueException exception = new AnalyseSemantiqueException(getNoLigne(), "Ecriture d'un tableau sans indice");
                ErreurSemantique.getInstance().ajouter(exception);
            }
        }
    }

    @Override
    public String toMIPS() {
        StringBuilder stringBuilder = new StringBuilder("\t# Ecrire ");
        stringBuilder.append(exp.toString());
        stringBuilder.append("\n");
        // On charge dans $v0 la valeur évaluée de l'expression que l'on veut afficher
        stringBuilder.append(exp.toMIPS());
        if(exp.getType().equals("bool")){
            // L'expression est booléen, donc il faut afficher l'interprétation de cette valeur booléenne
            stringBuilder.append(ecrireBoolEnMIPS());
        } else {
            // On met la valeur évaluée de l'expression dans $a0
            stringBuilder.append("\tmove $a0, $v0\n");
            // On dit à MIPS que l'on veut appeller la commande "afficher"
            stringBuilder.append("\tli $v0, 1\n\tsyscall\n");
        }
        // On ajoute une nouvelle ligne pour la lisibilité du code MIPS
        stringBuilder.append("\n\t# new line\n\tli $v0, 4\n\tla $a0, newline\n\tsyscall\n");
        return stringBuilder.toString();
    }

    @Override
    public boolean contientRetourne() {
        return false;
    }

    @Override
    protected String getNomInstruction() {
        return "Ecrire";
    }

    private String ecrireBoolEnMIPS(){
        int numLabel = FabriqueDeNumero.getInstance().getNumeroLabelCondition();
        return "\t# Si $v0 s'évalue à vrai alors \n" +
                "\tbeqz $v0, faux" +
                numLabel +
                "\t# Ici l'expression s'évalue à vrai\n" +
                "\tli $v0, 4\n" +
                "\tla $a0, vrai\n" +
                "\tj finSi" +
                numLabel+
                "\n# Sinon\n" +
                "faux" +
                numLabel +
                ":\n" +
                "# Ici l'expression s'évalue à faux\n" +
                "\tli $v0, 4\n" +
                "\tla $a0, faux\n" +
                "\n" +
                "finSi" +
                numLabel +
                ":\nsyscall\n";
    }
}
