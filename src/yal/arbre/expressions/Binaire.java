package yal.arbre.expressions;

import yal.arbre.expressions.operateurs.Operateur;

public class Binaire extends Expression {

    private Expression gauche;
    private Expression droite;
    private Operateur op;

    public Binaire(Expression g, Operateur o, Expression d, int n) {
        super(n);
        gauche = g;
        droite = d;
        op = o;
    }

    @Override
    public void verifier() {

    }

    @Override
    public String toMIPS() {
        StringBuilder stringBuilder = new StringBuilder();
        // Ecriture du beau commentaire MIPS
        stringBuilder.append("\t# Operation de :");
        stringBuilder.append(gauche.toString());
        stringBuilder.append(" et ");
        stringBuilder.append(droite.toString());
        stringBuilder.append("\n");

        // On stocke l'opérande gauche dans la pile
        stringBuilder.append(gauche.toMIPS());
        stringBuilder.append("\tsw $v0, 0($sp)\n");
        stringBuilder.append("\tadd $sp, $sp, -4\n");

        // On évalue l'opérande droite
        stringBuilder.append(droite.toMIPS());

        // On récupère l'opérande gauche
        stringBuilder.append("\tadd $sp, $sp, 4\n");
        stringBuilder.append("\tlw $t8, 0($sp)\n");

        // On fait l'opération
        stringBuilder.append("\t");
        stringBuilder.append(op.toMips());
        stringBuilder.append(" $v0, $t8, $v0\n\n");

        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        return gauche.toString()+" "+op.toString()+" "+droite.toString();
    }
}
