package yal.arbre;

import yal.arbre.expressions.variable.declaration.Declaration;
import yal.arbre.gestionnaireTDS.*;
import yal.exceptions.AnalyseSemantiqueException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class Programme extends ArbreAbstrait {
    private BlocDInstructions instructions;
    private ArrayList<Declaration> declarations;
    public Programme(ArrayList<Declaration> declarations, ArbreAbstrait a, int n) {
        super(n);
        instructions = (BlocDInstructions) a;
        this.declarations = declarations;
        for (Declaration declaration : declarations){
            declaration.ajouterTDS();
        }
    }

    public Programme(ArbreAbstrait a, int n) {
        this(new ArrayList<>(), a, n);
    }

    @Override
    public void verifier() {
        for(Declaration declaration : declarations){
            declaration.verifier();
        }
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
        ajouterTextes(stringBuilder);

        stringBuilder.append("\t# Allocation mémoire des variables globales dans la pile\n");
        stringBuilder.append("\tmove $s7, $sp\n");
        stringBuilder.append("\tadd $sp, $sp, ");
        stringBuilder.append(TDS.getInstance().getDepl());
        stringBuilder.append("\n");

        stringBuilder.append("\t# Récupération de l'adresse de début du tas\n");
        stringBuilder.append("\tmove $s6, $gp\n");

        // Initialisation des variables à 0
        for(Declaration declaration : declarations){
            stringBuilder.append(declaration.toMIPS());
        }

        // Création de l'arbre abstrait
        stringBuilder.append(instructions.toMIPS());

        // Fin du programme MIPS
        stringBuilder.append("\nend:\n");
        stringBuilder.append("\tli $v0, 10\n");
        stringBuilder.append("\tsyscall\n");

        for(Entree entree : TDS.getInstance().getRacine()){
            Symbole symbole = TDS.getInstance().getRacine().identifier(entree);
            // Si l'entree regardé correspond à une fonction
            if(symbole.getType().equals("fonction")){
                // Declaration de l'etiquette de la fonction
                stringBuilder.append("\n");
                stringBuilder.append(entree.getIdf());
                stringBuilder.append("_params_");
                stringBuilder.append(entree.getNbParam());
                stringBuilder.append(":\n");

                // Debut du bloc + corps de fonction
                stringBuilder.append(((SymboleDeFonction) symbole).toMIPS());
                // Retour au programme principal Normlement après l'instruction retourne
                // Si aucune instruction retourne, une erreur d'execution est déclanchée
                finFonction(stringBuilder);
            }
        }

        ajouterErreurs(stringBuilder);

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
            res = instruction.contientRetourne();
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

    private void ajouterTextes(StringBuilder stringBuilder){
        stringBuilder.append(".data\nnewline:\t.asciiz\t\t  \"\\n\"\n");
        stringBuilder.append("vrai : .asciiz \"vrai\"\n");
        stringBuilder.append("faux : .asciiz \"faux\"\n");
        stringBuilder.append("msgDivisionParZero : .asciiz \"Erreur d'execution : Division par zero\"\n");
        stringBuilder.append("msgFonctionSansRetour : .asciiz \"Erreur d'execution : Fonction terminée sans rencontrer de retour\"\n");
        stringBuilder.append(".text\nmain:\n\n");
    }

    private void ajouterErreurs(StringBuilder stringBuilder){
        // Affichage de l'erreur de fonction finie sans recontrer d'instruction Retourne
        stringBuilder.append("\nErrRetour:\n");
        stringBuilder.append("\tli $v0, 4\n");
        stringBuilder.append("\tla $a0, msgFonctionSansRetour\n");
        stringBuilder.append("\tsyscall\n");
        stringBuilder.append("\tj end\n");

        // Affichage de l'erreur de division par Zero
        stringBuilder.append("\nErrDiv:\n");
        stringBuilder.append("\tli $v0, 4\n");
        stringBuilder.append("\tla $a0, msgDivisionParZero\n");
        stringBuilder.append("\tsyscall\n");
        stringBuilder.append("\tj end\n");

    }

    private void finFonction(StringBuilder stringBuilder){
        stringBuilder.append("\nj ErrRetour\n");
    }
}
