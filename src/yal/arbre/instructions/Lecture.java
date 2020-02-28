package yal.arbre.instructions;

import yal.arbre.gestionnaireTDS.Entree;
import yal.arbre.gestionnaireTDS.ErreurSemantique;
import yal.arbre.gestionnaireTDS.TDS;
import yal.exceptions.AnalyseSemantiqueException;

public class Lecture extends Instruction {
    private String idf;
    public Lecture(String idf, int n) {
        super(n);
        this.idf = idf;
    }

    @Override
    protected String getNomInstruction() {
        return "Lire";
    }

    @Override
    public void verifier() {
        if(TDS.getInstance().identifier(new Entree(idf)) == null){
            AnalyseSemantiqueException exception = new AnalyseSemantiqueException(getNoLigne(), "Variable "+idf+" non déclarée");
            ErreurSemantique.getInstance().ajouter(exception);
        }
    }

    @Override
    public String toMIPS() {
        StringBuilder stringBuilder = new StringBuilder("\t#Lire un entier depuis l'entrée utilisateur et l'affecter à ");
        stringBuilder.append(idf);
        stringBuilder.append("\n\tli, $v0, 5\n\tsyscall\n");
        // On bourre ce que l'on viens de lire à l'emplacement mémoire de l'entier
        stringBuilder.append("\tsw $v0, ");
        stringBuilder.append(TDS.getInstance().identifier(new Entree(idf)).getDepl());
        stringBuilder.append("($s7)\n\n");
        return stringBuilder.toString();
    }
}
