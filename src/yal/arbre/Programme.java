package yal.arbre;

import yal.FabriqueDeNumero;
import yal.arbre.expressions.variable.declaration.Declaration;
import yal.arbre.gestionnaireTDS.*;
import yal.exceptions.AnalyseSemantiqueException;

import java.util.ArrayList;
import java.util.Iterator;

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

        // Initialisation des variables à 0
        for(Declaration declaration : declarations){
            if(!declaration.getClass().getSimpleName().equals("DeclarationFonction")){
                stringBuilder.append(declaration.toMIPS());
            }
        }

        // Seconde partie de la déclaration des tableaux
        for(Declaration declaration : declarations){
            if(declaration.getClass().getSimpleName().equals("DeclarationTableauEntier")){
                declaration.initialisationDuCorpsDeLaVariable(stringBuilder);
            }
        }

        // Création de l'arbre abstrait
        stringBuilder.append(instructions.toMIPS());

        // Fin du programme MIPS
        stringBuilder.append("\nend:\n");
        stringBuilder.append("\tli $v0, 10\n");
        stringBuilder.append("\tsyscall\n");

        for(Entree entree : TDS.getInstance().getRacine()){
            // On ajoute l'initialisation de la mémoire des variables
            for(Declaration declaration : declarations){
                if(declaration.getIdf().equals(entree.getIdf())){
                    Symbole symbole = TDS.getInstance().getRacine().identifier(entree);
                    // Si l'entree regardé correspond à une fonction
                    if(symbole.getType().equals("fonction")){
                        // On utilise la table des symboles de la fonction
                        TDS.getInstance().entreeBloc(symbole.getNumBloc());
                        // Declaration de l'etiquette de la fonction
                        stringBuilder.append("\n");
                        stringBuilder.append(entree.getIdf());
                        stringBuilder.append(":\n");

                        // Création de l'entete de la fonction (adresse de retour et merdier en tout genre)
                        ((SymboleDeFonction) symbole).enteteToMIPS(stringBuilder);

                        // Debut du bloc + corps de fonction
                        ((SymboleDeFonction) symbole).allocationMemoireVarLocalFonctionToMIPS(stringBuilder);

                        // On déclare la mémoire pour les variables de la fonction
                        declaration.initialisationDuCorpsDeLaVariable(stringBuilder);

                        // Ajout des instructions de la fonction
                        ((SymboleDeFonction) symbole).instructionFonctionToMIPS(stringBuilder);

                        // Retour au programme principal Normlement après l'instruction retourne
                        // Si aucune instruction retourne, une erreur d'execution est déclanchée
                        finFonction(stringBuilder);
                        TDS.getInstance().sortieBloc();
                    }
                }
            }
        }

        ajouterErreurs(stringBuilder);
        fonctionsTableauToMIPS(stringBuilder);

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
        stringBuilder.append("msgTableauDimensionIncorrect : .asciiz \"Erreur d'execution : Taille de tableau incorrecte (inférieur à 1)\"\n");
        stringBuilder.append("msgTableauIndiceIncorrectInfZero : .asciiz \"Erreur d'execution : indice de tableau incorrecte (inférieur à 0)\"\n");
        stringBuilder.append("msgTableauIndiceIncorrectHorsLimite : .asciiz \"Erreur d'execution : indice de tableau incorrecte (hors limite)\"\n");
        stringBuilder.append("msgErrCopieTab : .asciiz \"Erreur d'execution : copie de tableau de dimensions incompatibles\"\n");
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

        // Affichage de l'erreur de dimension de tableau
        stringBuilder.append("\nErrDimTab:\n");
        stringBuilder.append("\tli $v0, 4\n");
        stringBuilder.append("\tla $a0, msgTableauDimensionIncorrect\n");
        stringBuilder.append("\tsyscall\n");
        stringBuilder.append("\tj end\n");

        // Affichage de l'erreur d'indice d'acces au tableau
        stringBuilder.append("\nErrIndiceTabInfZero:\n");
        stringBuilder.append("\tli $v0, 4\n");
        stringBuilder.append("\tla $a0, msgTableauIndiceIncorrectInfZero\n");
        stringBuilder.append("\tsyscall\n");
        stringBuilder.append("\tj end\n");

        // Affichage de l'erreur d'indice d'acces au tableau
        stringBuilder.append("\nErrIndiceTabHorsLimite:\n");
        stringBuilder.append("\tli $v0, 4\n");
        stringBuilder.append("\tla $a0, msgTableauIndiceIncorrectHorsLimite\n");
        stringBuilder.append("\tsyscall\n");
        stringBuilder.append("\tj end\n");

        // Affichage de l'erreur d'indice d'acces au tableau
        stringBuilder.append("\nErrCopieTab:\n");
        stringBuilder.append("\tli $v0, 4\n");
        stringBuilder.append("\tla $a0, msgErrCopieTab\n");
        stringBuilder.append("\tsyscall\n");
        stringBuilder.append("\tj end\n");
    }

    private void fonctionsTableauToMIPS(StringBuilder stringBuilder){
        stringBuilder.append("\nVerifDimTab:\n");
        stringBuilder.append("\tblez $v0, ErrDimTab\t#Si la valeur de dimension du tableau est inférieure ou égale à 0, on déclenche une erreur d'execution\n");
        stringBuilder.append("\t#Sinon, on initialise le tableau\n");

        stringBuilder.append("\tsw $v0, ($sp)\n");
        stringBuilder.append("\taddi $sp, $sp, -4\n");
        // On initialise toutes les cases du tableau
        int numLabel = FabriqueDeNumero.getInstance().getNumeroLabelCondition();
        stringBuilder.append("\tmove $t7, $v0\n");
        stringBuilder.append("\ntantQue");
        stringBuilder.append(numLabel);
        stringBuilder.append(":\n");
        // On se permet d'utiliser le registre $t7
        stringBuilder.append("\taddi $t7, $t7, -1\n");
        stringBuilder.append("\tbltz $t7, finTantQue");
        stringBuilder.append(numLabel);
        stringBuilder.append("\t#Si on atteint la fin de la boucle on sort\n");
        stringBuilder.append("\t#Sinon on ajoute une place de plus dans la pile\n");
        stringBuilder.append("\tli $v0, 0\n");
        stringBuilder.append("\tsw $v0, ($sp)\n");
        stringBuilder.append("\taddi $sp, $sp, -4\n");
        // On recommence
        stringBuilder.append("\tj tantQue");
        stringBuilder.append(numLabel);
        stringBuilder.append("\n");
        // label de fin
        stringBuilder.append("\nfinTantQue");
        stringBuilder.append(numLabel);
        stringBuilder.append(":\n");
        // On retourne là où la fonction a été appelée
        stringBuilder.append("\tjr $ra\n");


        // Fonction de vérification de la validité de l'indice du tableau
        stringBuilder.append("\nVerifIndiceTab:\n");
        stringBuilder.append("\tlw $v0, 8($sp)\t# Récupération de l'indice demandé\n");
        stringBuilder.append("\t# Vérification que l'indice est supérieur à 0\n");
        stringBuilder.append("\tbltz $v0, ErrIndiceTabInfZero\n");
        stringBuilder.append("\tlw $t8, 4($sp)\n");
        stringBuilder.append("\tlw $t8, ($t8)\t# Récupération de la taille du tableau\n");
        stringBuilder.append("\tble $t8, $v0 ErrIndiceTabHorsLimite\n");
        stringBuilder.append("\tjr $ra\n");

        // Fonction d'affectation d'une valeur dans un tableau (à un indice valide)
        stringBuilder.append("\nAffectationTab:\n");
        stringBuilder.append("\tlw $v0, 8($sp)\t# Récupération de l'indice demandé\n");
        stringBuilder.append("\tlw $t8, 4($sp)\t# Récupération de l'adresse du tableau\n");
        stringBuilder.append("\taddi $v0, $v0, 1\t# Conversion de l'indice en décalage par rapport au début du corps du tableau\n");
        stringBuilder.append("\tli $t7, 4\n"); // Excusez nous, on sait que utiliser $t7 c'est pas bien
        stringBuilder.append("\tmul $v0, $v0, $t7\n");
        stringBuilder.append("\tsub $t8, $t8, $v0\t# Calcul de l'adresse de la case du tableau modifiée\n");
        stringBuilder.append("\tlw $v0, 12($sp)\t# Récupération de la valeur a affecter à la case du tableau\n");
        stringBuilder.append("\tsw $v0, ($t8)\n");
        stringBuilder.append("\tjr $ra\n");

        // Fonction de vérification des dimensions de deux tableaux
        stringBuilder.append("\nVerifDimTabs:\n");
        stringBuilder.append("\tlw $v0, 8($sp)\t# Récupération du tableau a copier\n");
        stringBuilder.append("\tlw $v0, ($v0)\t# Récupération de la taille du tableau a copier\n");
        stringBuilder.append("\tlw $t8, 4($sp)\t# Récupération du tableau receveur\n");
        stringBuilder.append("\tlw $t8, ($t8)\t# Récupération de la taille du tableau a copier\n");
        stringBuilder.append("\tbne $v0, $t8, ErrCopieTab\n");
        stringBuilder.append("\tjr $ra\n");

        // Fonction de test d'égalité de dimensions de deux tableaux
        stringBuilder.append("\nTestDimTabs:\n");
        stringBuilder.append("\tlw $v0, 8($sp)\t# Récupération du premier tableau\n");
        stringBuilder.append("\tlw $v0, ($v0)\t# Récupération de la taille du premier tableau\n");
        stringBuilder.append("\tlw $t8, 4($sp)\t# Récupération du second tableau\n");
        stringBuilder.append("\tlw $t8, ($t8)\t# Récupération de la taille du second tableau\n");
        stringBuilder.append("\tseq $v0, $t8, $v0\n");
        stringBuilder.append("\tjr $ra\n");


        // Fonction de test d'égalité des valeurs de deux tableaux TODO : finir de corriger
        stringBuilder.append("\nTestValsTabs:\n");
        stringBuilder.append("\tlw $t8, 4($sp)\t# Récupération du second tableau\n");
        stringBuilder.append("\tlw $t8, ($t8)\t# Récupération de la taille du second tableau\n");
        // On empile la taille du tableau (qui servira de compteur)
        stringBuilder.append("\tsw $t8, ($sp)\n\taddi $sp, $sp, -4\n");
        stringBuilder.append("\tli $v0, 4\n\tmul $v0, $t8, $v0\n\taddi $v0, $v0, 4\n");
        // On empile le décalage mémoire
        stringBuilder.append("\tsw $v0, ($sp)\n\taddi $sp, $sp, -4\n");
        // Boucle tant que
        numLabel = FabriqueDeNumero.getInstance().getNumeroLabelCondition();
        stringBuilder.append("\ntantQue");
        stringBuilder.append(numLabel);
        stringBuilder.append(":\n");
        // Actualisation des valeurs
        stringBuilder.append("\tlw $t8, 4($sp)\t# Récupération du décalage mémoire\n");
        stringBuilder.append("\taddi $t8, $t8, -4\n");
        stringBuilder.append("\tsw $t8, 4($sp)\t# On rempile le décalage mémoire\n");
        stringBuilder.append("\tlw $t8, 8($sp)\t# Récupération du compteur\n");
        stringBuilder.append("\taddi $t8, $t8, -1\n");
        stringBuilder.append("\tsw $t8, 8($sp)\t# On rempile la valeur du compteur\n");
        // Faire
        stringBuilder.append("\tbltz $t8, finTantQue");
        stringBuilder.append(numLabel);
        stringBuilder.append("\t#Si on atteint la fin de la boucle on sort\n");
        stringBuilder.append("\t#Sinon on recupere la valeur a copier et on la copie\n");
        stringBuilder.append("\tlw $t8, 16($sp)\t#Récupération du pointeur du début du premier tableau\n"); // devrait pointer à 20
        stringBuilder.append("\tlw $v0, 4($sp)\t# Récupération du décalage mémoire\n");
        stringBuilder.append("\tsub $t8, $t8, $v0\n");
        stringBuilder.append("\tlw $t8, ($t8)\n");
        stringBuilder.append("\tsw $t8, ($sp)\n\taddi $sp, $sp, -4\t#On empile la valeur du premier tableau\n");
        stringBuilder.append("\tlw $t8, 16($sp)\t#Récupération du pointeur du début du second tableau\n");
        stringBuilder.append("\tsub $t8, $t8, $v0\n");
        stringBuilder.append("\tlw $t8, ($t8)\n");
        stringBuilder.append("\tlw $v0, 4($sp)\n\taddi $sp, $sp, 4\n");
        // Comparaison
        stringBuilder.append("\t#Comparaison des deux valeurs\n");
        stringBuilder.append("\tseq $v0, $t8, $v0\t#Premier tableau dans $v0, second dans $t8\n");
        stringBuilder.append("\tblez $v0,finTantQue").append(numLabel).append("\n");
        // On recommence
        stringBuilder.append("\tj tantQue");
        stringBuilder.append(numLabel);
        stringBuilder.append("\n");
        // label de fin
        stringBuilder.append("\nfinTantQue");
        stringBuilder.append(numLabel);
        stringBuilder.append(":\n");
        // Dépilage du décalage mémoire et du compteur
        stringBuilder.append("\taddi $sp, $sp, 8\n");
        stringBuilder.append("\tjr $ra\n");


        // Fonction de copie profonde de tableaux
        stringBuilder.append("\nCopieTab:\n");
        stringBuilder.append("\tlw $t8, 4($sp)\t# Récupération du tableau receveur\n");
        stringBuilder.append("\tlw $t8, ($t8)\t# Récupération de la taille du tableau a copier\n");
        // On empile la taille du tableau (qui servira de compteur)
        stringBuilder.append("\tsw $t8, ($sp)\n\taddi $sp, $sp, -4\n");
        stringBuilder.append("\tli $v0, 4\n\tmul $v0, $t8, $v0\n\taddi $v0, $v0, 4\n");
        // On empile le décalage mémoire
        stringBuilder.append("\tsw $v0, ($sp)\n\taddi $sp, $sp, -4\n");
        // Boucle tant que
        numLabel = FabriqueDeNumero.getInstance().getNumeroLabelCondition();
        stringBuilder.append("\ntantQue");
        stringBuilder.append(numLabel);
        stringBuilder.append(":\n");
        // Actualisation des valeurs
        stringBuilder.append("\tlw $t8, 4($sp)\t# Récupération du décalage mémoire\n");
        stringBuilder.append("\taddi $t8, $t8, -4\n");
        stringBuilder.append("\tsw $t8, 4($sp)\t# On rempile le décalage mémoire\n");
        stringBuilder.append("\tlw $t8, 8($sp)\t# Récupération du compteur\n");
        stringBuilder.append("\taddi $t8, $t8, -1\n");
        stringBuilder.append("\tsw $t8, 8($sp)\t# On rempile le du compteur\n");
        // Faire
        stringBuilder.append("\tbltz $t8, finTantQue");
        stringBuilder.append(numLabel);
        stringBuilder.append("\t#Si on atteint la fin de la boucle on sort\n");
        stringBuilder.append("\t#Sinon on recupere la valeur a copier et on la copie\n");
        stringBuilder.append("\tlw $t8, 16($sp)\t#Récupération du pointeur du début du tableau initial\n");
        stringBuilder.append("\tlw $v0, 4($sp)\t# Récupération du décalage mémoire\n");
        stringBuilder.append("\tsub $t8, $t8, $v0\n");
        stringBuilder.append("\tlw $t8, ($t8)\n");
        // Copie
        stringBuilder.append("\tsw $t8, ($sp)\t#Empilage de la valeur a copier\n");
        stringBuilder.append("\taddi $sp, $sp, -4\n");
        stringBuilder.append("\tlw $t8, 16($sp)\t#Récupération du pointeur du début du tableau receveur\n");
        stringBuilder.append("\tsub $t8, $t8, $v0\n");
        stringBuilder.append("\tlw $v0, 4($sp)\t#Récupération de la valeur a copier\n");
        stringBuilder.append("\taddi $sp, $sp, 4\n");
        stringBuilder.append("\tsw $v0, ($t8)\t#Copie de la valeur dans le tableau receveur\n");
        // On recommence
        stringBuilder.append("\tj tantQue");
        stringBuilder.append(numLabel);
        stringBuilder.append("\n");
        // label de fin
        stringBuilder.append("\nfinTantQue");
        stringBuilder.append(numLabel);
        stringBuilder.append(":\n");
        // Dépilage du décalage mémoire et du compteur
        stringBuilder.append("\taddi $sp, $sp, 8\n");
        stringBuilder.append("\tjr $ra\n");
    }

    private void finFonction(StringBuilder stringBuilder){
        stringBuilder.append("\nj ErrRetour\n");
    }
}
