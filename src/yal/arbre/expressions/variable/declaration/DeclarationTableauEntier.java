package yal.arbre.expressions.variable.declaration;

import yal.FabriqueDeNumero;
import yal.arbre.expressions.Expression;
import yal.arbre.gestionnaireTDS.*;
import yal.arbre.instructions.Boucle;
import yal.exceptions.AnalyseSemantiqueException;

public class DeclarationTableauEntier extends Declaration {
    private Expression tailleMaxDuTableau;

    public DeclarationTableauEntier(String idf, Expression tailleMaxDuTableau, int noLigne) {
        super(idf, noLigne);
        this.tailleMaxDuTableau = tailleMaxDuTableau;
    }

    @Override
    public void ajouterTDS() {
        Entree entree = new Entree("tableau_"+getIdf());
        SymboleDeTableau symboleDeTableau;
        symboleDeTableau = new SymboleDeTableau(TDS.getInstance().getDepl());
        try {
            TDS.getInstance().ajouter(entree, symboleDeTableau);
        } catch (Exception e) {
            AnalyseSemantiqueException exception = new AnalyseSemantiqueException(getNoLigne(), "Double déclaration du tableau : "+getIdf());
            ErreurSemantique.getInstance().ajouter(exception);
        }
    }

    @Override
    public void verifier() {
        if(!tailleMaxDuTableau.getType().equals("entier")){
            AnalyseSemantiqueException exception = new AnalyseSemantiqueException(super.getNoLigne(), "Format de la taille du tableau incorrecte. Attendue : entier\tReçu : "+ tailleMaxDuTableau.getType());
            ErreurSemantique.getInstance().ajouter(exception);
        }
        if(TDS.getInstance().getTableCourrante().equals(TDS.getInstance().getRacine())){
            if(!tailleMaxDuTableau.getClass().getSimpleName().equals("ConstanteEntiere")){
                AnalyseSemantiqueException exception = new AnalyseSemantiqueException(super.getNoLigne(), "Expression de la dimension du tableau "+getIdf()+" incorrecte. Attendue : constante entiere.\tReçu : "+ tailleMaxDuTableau.getClass().getSimpleName());
                ErreurSemantique.getInstance().ajouter(exception);
            }
        }
        //TODO : si on est pas dans le bloc principale...
    }

    @Override
    public String toMIPS() {
        SymboleDeVariable symboleDeVariable = ((SymboleDeVariable) TDS.getInstance().identifier(new Entree("tableau_"+getIdf())));
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\t# Déclaration du tableau ");
        stringBuilder.append(getIdf());
        stringBuilder.append("\n");
        stringBuilder.append("\tli $v0, 0\n");
        stringBuilder.append("\tsw $v0, ");
        // La référence pour le décalage
        if(symboleDeVariable.getNumBloc() == TDS.getInstance().getRacine().getNumBloc()){
            stringBuilder.append(symboleDeVariable.getDepl());
            stringBuilder.append("($s7)");
        } else {
            stringBuilder.append(symboleDeVariable.getDepl()-8);
            stringBuilder.append("($s2)");
        }
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    public String affecterPointeur(){
        StringBuilder stringBuilder = new StringBuilder();
        // On stock dans la case mémoire l'adresse du début du tableau. (le sommet de pile actuel)
        SymboleDeVariable symboleDeVariable = ((SymboleDeVariable) TDS.getInstance().identifier(new Entree("tableau_"+getIdf())));
        stringBuilder.append("\tmove $v0, $sp\n\tsw $v0, ");
        if(symboleDeVariable.getNumBloc() == TDS.getInstance().getRacine().getNumBloc()){
            stringBuilder.append(symboleDeVariable.getDepl());
            stringBuilder.append("($s7)");
        } else {
            stringBuilder.append(symboleDeVariable.getDepl()-8);
            stringBuilder.append("($s2)");
        }
        stringBuilder.append("\n");

        // On initialise toutes les cases du tableau a 0, et on met a jour le sommet de pile.
        initialiserValeurs(stringBuilder);
        return stringBuilder.toString();
    }

    /**
     * Ajoute au code MIPS l'initialisation des valeurs à 0 du tableau
     * Ainsi que la taille du tableau avant la première valeur
     * @param stringBuilder la où on met le merdier
     */
    private void initialiserValeurs(StringBuilder stringBuilder){
        stringBuilder.append("\t#Evaluation de la taille du tableau\n");
        stringBuilder.append(tailleMaxDuTableau.toMIPS());
        stringBuilder.append("\tsw $v0, ($sp)\n");
        stringBuilder.append("\taddi $sp, $sp, -4\n");

        // On initialise toutes les cases du tableau
        int numLabel = FabriqueDeNumero.getInstance().getNumeroLabelCondition();
        stringBuilder.append("\tmove $t7, $v0\n");
        stringBuilder.append("tantQue");
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
        stringBuilder.append("j tantQue");
        stringBuilder.append(numLabel);
        // label de fin
        stringBuilder.append("\nfinTantQue");
        stringBuilder.append(numLabel);
        stringBuilder.append(":\n");
    }

    @Override
    public boolean contientRetourne() {
        return false;
    }
}
