package yal.arbre.expressions;

import yal.arbre.gestionnaireTDS.ErreurSemantique;
import yal.arbre.expressions.operateurs.*;
import yal.exceptions.AnalyseSemantiqueException;

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
        if(!gauche.getType().equals(droite.getType())){
            AnalyseSemantiqueException exception = new AnalyseSemantiqueException(getNoLigne(),
                    "Incompatibilité de types : "+gauche.getType()+" incompatible avec "+droite.getType());
            ErreurSemantique.getInstance().ajouter(exception);
        } else {
            if(gauche.getType().equals("entier") && droite.getType().equals("entier")){
                // Opérandes entiers et opération à retour bool
                if(!op.getNatureRetour().equals(gauche.getType())){
                    // Si l'opérateur est un "et" logique ou un "ou" logique on lance une nouvelle erreur sémantique
                    if(op.toString().equals(new EtLogique().toString())
                            || op.toString().equals(new OuLogique().toString())){
                        AnalyseSemantiqueException exception = new AnalyseSemantiqueException(getNoLigne(),
                                "Opérateur \""+op.toString()+"\" non adapté pour des paramètres de types : "+gauche.getType());
                        ErreurSemantique.getInstance().ajouter(exception);
                    }
                }
                // Opérandes entiers et opération à retour entiers
                else {
                    // Si le programmeur veut faire une division par zéro
                    if(op.toString().equals("/") && droite.toString().equals("0")){
                        AnalyseSemantiqueException exception = new AnalyseSemantiqueException(getNoLigne(), "Division par zéro");
                        ErreurSemantique.getInstance().ajouter(exception);
                    }
                }
            }
            // Les opérandes sont booléens
            else {
                // Opérandes bool et opération à retour entiers
                if (!op.getNatureRetour().equals("bool")){
                    AnalyseSemantiqueException exception = new AnalyseSemantiqueException(getNoLigne(),
                            "Opération arithmétique impossible : "+gauche.getType()+" "+op.toString()+" "+droite.getType());
                    ErreurSemantique.getInstance().ajouter(exception);
                }
                // Opérandes bool et opération à retour bool
                else {
                    if(op.toString().equals(new SuperieurA().toString())
                            || op.toString().equals(new InferieurA().toString())){
                        AnalyseSemantiqueException exception = new AnalyseSemantiqueException(getNoLigne(),
                                "Comparaison impossible : "+gauche.getType()+" "+op.toString()+" "+droite.getType());
                        ErreurSemantique.getInstance().ajouter(exception);
                    }
                }
            }
        }
    }

    @Override
    public String toMIPS() {
        StringBuilder stringBuilder = new StringBuilder();
        // Ecriture du beau commentaire MIPS
        stringBuilder.append("\t# Calcule de  :");
        stringBuilder.append(gauche.toString());
        stringBuilder.append(" ");
        stringBuilder.append(op.toString());
        stringBuilder.append(" ");
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
        stringBuilder.append("\tlw $t8, ($sp)\n");

        stringBuilder.append(op.toMips());
        stringBuilder.append("\n\n");

        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        return gauche.toString()+" "+op.toString()+" "+droite.toString();
    }

    @Override
    public String getType() {
        return op.getNatureRetour();
    }
}
