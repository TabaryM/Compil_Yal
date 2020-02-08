package yal.arbre;

import yal.arbre.declaration.TDS;

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
        stringBuilder.append(".text\nmain:\n\n");

        stringBuilder.append("\t# Allocation mémoire des variables dans la pile\n");
        stringBuilder.append("\tmove, $s7, $sp\n");
        stringBuilder.append("\tadd $sp, $sp, ");
        stringBuilder.append(TDS.getInstance().getCpt());
        stringBuilder.append("\n\n");

        stringBuilder.append(arbreAbstrait.toMIPS());

        stringBuilder.append("\nend:\n");
        stringBuilder.append("\tli $v0, 10\n");
        stringBuilder.append("\tsyscall\n");
        return stringBuilder.toString();
    }
}
