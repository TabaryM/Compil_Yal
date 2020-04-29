package yal.arbre.expressions.variable.declaration;

import yal.FabriqueDeNumero;
import yal.arbre.expressions.Expression;
import yal.arbre.gestionnaireTDS.*;
import yal.exceptions.AjoutTDSException;
import yal.exceptions.AnalyseSemantiqueException;

public class DeclarationTableauEntier extends Declaration {
    private Expression tailleMaxDuTableau;

    public DeclarationTableauEntier(String idf, Expression tailleMaxDuTableau, int noLigne) {
        super(idf, noLigne, "tableau");
        this.tailleMaxDuTableau = tailleMaxDuTableau;
    }

    @Override
    public void ajouterTDS() {
        Entree entree = new Entree(getIdf());
        SymboleDeTableau symboleDeTableau;
        symboleDeTableau = new SymboleDeTableau(TDS.getInstance().getDepl());
        try {
            TDS.getInstance().ajouter(entree, symboleDeTableau);
        } catch (AjoutTDSException e) {
            AnalyseSemantiqueException exception = new AnalyseSemantiqueException(getNoLigne(), e.getMessage());
            ErreurSemantique.getInstance().ajouter(exception);
        }
    }

    @Override
    public void initialisationDuCorpsDeLaVariable(StringBuilder stringBuilder) {
        stringBuilder.append(affecterPointeur());
    }

    @Override
    public void verifier() {
        if(!tailleMaxDuTableau.getType().equals("entier")){
            AnalyseSemantiqueException exception = new AnalyseSemantiqueException(super.getNoLigne(), "Expression de la dimension du tableau "+getIdf()+" incorrecte. Attendue : entier.\tReçu : "+ tailleMaxDuTableau.getType());
            ErreurSemantique.getInstance().ajouter(exception);
        }

        // Si l'expression est une constante entiere :
        if(tailleMaxDuTableau.getClass().getSimpleName().equals("ConstanteEntiere")){
            int taille = Integer.parseInt(tailleMaxDuTableau.toString());
            if(taille <= 0){
                AnalyseSemantiqueException exception = new AnalyseSemantiqueException(super.getNoLigne(), "Expression de la dimension du tableau "+getIdf()+" incorrecte. Attendue : ConstanteEntiere > 0.\tReçu : "+ tailleMaxDuTableau.toString());
                ErreurSemantique.getInstance().ajouter(exception);
            }
        }
        // Si l'instruction se trouve dans le programme principal, l'expression indiquant la taille du tableau doit être une constante
        else if(TDS.getInstance().getTableCourrante().equals(TDS.getInstance().getRacine())){
            if(!tailleMaxDuTableau.getClass().getSimpleName().equals("ConstanteEntiere")){
                AnalyseSemantiqueException exception = new AnalyseSemantiqueException(super.getNoLigne(), "Expression de la dimension du tableau "+getIdf()+" incorrecte. Attendue : ConstanteEntiere > 0.\tReçu : "+ tailleMaxDuTableau.getClass().getSimpleName());
                ErreurSemantique.getInstance().ajouter(exception);
            }
        }
    }

    @Override
    public String toMIPS() {
        SymboleDeVariable symboleDeVariable = ((SymboleDeVariable) TDS.getInstance().identifier(new Entree(getIdf())));
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
            stringBuilder.append(symboleDeVariable.getDepl()-4);
            stringBuilder.append("($s2)");
        }
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    public String affecterPointeur(){
        StringBuilder stringBuilder = new StringBuilder();
        // On stock dans la case mémoire l'adresse du début du tableau. (le sommet de pile actuel)
        SymboleDeVariable symboleDeVariable = ((SymboleDeVariable) TDS.getInstance().identifier(new Entree(getIdf())));
        stringBuilder.append("\tmove $v0, $sp\t#On récupère l'adresse du début du tableau\n\tsw $v0, ");
        if(symboleDeVariable.getNumBloc() == TDS.getInstance().getRacine().getNumBloc()){
            stringBuilder.append(symboleDeVariable.getDepl());
            stringBuilder.append("($s7)");
        } else {
            stringBuilder.append(symboleDeVariable.getDepl()-4);
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

        // Vérification que la taille du tableau est correcte (>=1)
        appelVerificationDimensionToMIPS(stringBuilder);
    }

    @Override
    public boolean contientRetourne() {
        return false;
    }

    public static void appelVerificationDimensionToMIPS(StringBuilder stringBuilder){
        stringBuilder.append("\t#Vérification que la taille du tableau est correcte\n");
        stringBuilder.append("\tjal VerifDimTab\n");
    }
}
