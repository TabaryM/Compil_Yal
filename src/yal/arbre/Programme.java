package yal.arbre;

import yal.arbre.expressions.Fonction;
import yal.arbre.gestionnaireTDS.Entree;
import yal.arbre.gestionnaireTDS.Symbole;
import yal.arbre.gestionnaireTDS.SymboleDeFonction;
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
        stringBuilder.append("\tmove $s7, $sp\n");
        stringBuilder.append("\tadd $sp, $sp, ");
        stringBuilder.append(TDS.getInstance().getCpt());
        stringBuilder.append("\n\n");

        // Création de l'arbre abstrait
        stringBuilder.append(arbreAbstrait.toMIPS());

        // Fin du programme MIPS
        stringBuilder.append("\nend:\n");
        stringBuilder.append("\tli $v0, 10\n");
        stringBuilder.append("\tsyscall\n");

        for(Entree entree : TDS.getInstance()){
            Symbole symbole = TDS.getInstance().identifier(entree);
            // Si l'entree regardé correspond à une fonction
            if(symbole.getType().equals("fonction")){
                // Declaration de l'etiquette de la fonction
                stringBuilder.append("\nfonction_");
                stringBuilder.append(entree.getIdf());
                stringBuilder.append(":\n");

                // Debut du bloc
                stringBuilder.append(Fonction.initBlocFonction());

                // on ajoute le corps de fonction au fichier
                stringBuilder.append(((SymboleDeFonction) symbole).toMIPS());
                // Retour au programme principal Normlement dans le RETOURNE
            }
        }

        // Affichage de l'erreur de division par Zero
        stringBuilder.append("\nErrDiv:\n");
        stringBuilder.append("\tli $v0, 4\n");
        stringBuilder.append("\tla $a0, msgDivisionParZero\n");
        stringBuilder.append("\tsyscall\n");
        stringBuilder.append("\tj end\n");

        return stringBuilder.toString();
    }

    @Override
    public boolean contientRetourne() {
        return arbreAbstrait.contientRetourne();
    }

}
