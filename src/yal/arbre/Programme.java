package yal.arbre;

import yal.arbre.gestionnaireTDS.TDS;

public class Programme extends ArbreAbstrait {
    private ArbreAbstrait arbreAbstrait;
    public Programme(ArbreAbstrait a, int n) {
        super(n);
        arbreAbstrait = a;
    }

    @Override
    public void verifier() {
        arbreAbstrait.verifier();
    }

    @Override
    public String toMIPS() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(".data\nnewline:\t.asciiz\t\t  \"\\n\"\n");
        stringBuilder.append("vrai : .asciiz \"vrai\"\n");
        stringBuilder.append("faux : .asciiz \"faux\"\n");
        stringBuilder.append("msgDivisionParZero : .asciiz \"Erreur d'execution : Division par zero\"\n");
        stringBuilder.append(".text\nmain:\n\n");

        stringBuilder.append("\t# Allocation mémoire des variables dans la pile\n");
        stringBuilder.append("\tmove, $s7, $sp\n");
        stringBuilder.append("\tadd $sp, $sp, ");
        stringBuilder.append(TDS.getInstance().getCpt());
        stringBuilder.append("\n\n");

        // Création de l'arbre abstrait
        stringBuilder.append(arbreAbstrait.toMIPS());

        // Fin du programme MIPS
        stringBuilder.append("\nend:\n");
        stringBuilder.append("\tli $v0, 10\n");
        stringBuilder.append("\tsyscall\n");

        // TODO : écrire les instructions MIPS des fonctions ici (dans la TDS)
        stringBuilder.append(TDS.getInstance().getFonctions());

        // Affichage de l'erreur de division par Zero
        stringBuilder.append("\nErrDiv:\n");
        stringBuilder.append("\tli $v0, 4\n");
        stringBuilder.append("\tla $a0, msgDivisionParZero\n");
        stringBuilder.append("\tsyscall\n");
        stringBuilder.append("\tj end\n");

        return stringBuilder.toString();
    }
}
