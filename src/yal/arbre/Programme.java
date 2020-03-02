package yal.arbre;

import yal.arbre.expressions.Fonction;
import yal.arbre.gestionnaireTDS.*;
import yal.exceptions.AnalyseSemantiqueException;

import java.util.Iterator;

public class Programme extends ArbreAbstrait {
    private BlocDInstructions instructions;
    public Programme(ArbreAbstrait a, int n) {
        super(n);
        instructions = (BlocDInstructions) a;
    }

    @Override
    public void verifier() {
        instructions.verifier();

        this.contientRetourne();
        // Doit être à la fin de la méthode
        if(!ErreurSemantique.getInstance().isEmpty()){
            ErreurSemantique.getInstance().afficherErreurs();
        }
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
        stringBuilder.append(instructions.toMIPS());

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
        Iterator<ArbreAbstrait> iterator = instructions.iterator();
        ArbreAbstrait instruction;
        boolean res = false;

        // Permet de by-pass la non déclaration de instruction pour le second if
        if(iterator.hasNext()){
            instruction = iterator.next();
            res |= instruction.contientRetourne();
            while (iterator.hasNext()){
                instruction = iterator.next();
                res |= instruction.contientRetourne();
            }
            // Il faut vérifier qu'il n'y a pas d'occurence de l'instruction retourne hors d'une fonction
            if(res){
                AnalyseSemantiqueException exception = new AnalyseSemantiqueException(instruction.getNoLigne(),
                        "Instruction Retourne hors d'une fonction ");
                ErreurSemantique.getInstance().ajouter(exception);
            }
        }
        return res;
    }
}
