package yal.arbre.expressions;

import yal.arbre.ArbreAbstrait;
import yal.arbre.BlocDInstructions;
import yal.arbre.gestionnaireTDS.*;
import yal.exceptions.AnalyseSemantiqueException;

public class Fonction extends Expression {
    private BlocDInstructions instructions;
    private Entree idf;

    /**
     * Déclaration d'une fonction dans l'arbre abstrait
     * @param idf identifiant de la fonction
     * @param a la liste d'instruciton de la fonction
     * @param numLig numéro de la ligne de la déclaration de la fonction
     */
    public Fonction(Entree idf, ArbreAbstrait a, int numLig){
        super(numLig);
        instructions = (BlocDInstructions) a;
        this.idf = idf;
        //ajoute le corps de la fonction pour le mettre a la fin du programme
        StringBuilder stringBuilder = new StringBuilder();
        if(instructions != null){
            //dans ce cas c'est une déclaration
            stringBuilder.append("\nfonction_"+idf+":\n");

            stringBuilder.append(instructions.toMIPS());
            TDS.getInstance().ajoutFonction(idf,stringBuilder.toString());
        }
    }

    /**
     *
     * @param idf
     * @param numLig
     */
    public Fonction(Entree idf, int numLig) {
        super(numLig);
        this.idf = idf;
    }

    public void ajouterTDS(){
        try {
            TDS.getInstance().ajouter(idf, new SymboleDeFonction(TDS.getInstance().getCpt()));
        } catch (Exception e) {
            AnalyseSemantiqueException exception = new AnalyseSemantiqueException(super.getNoLigne(), "Double déclaration de la fonction "+idf);
            ErreurSemantique.getInstance().ajouter(exception);
        }
    }

    /**
     *
     * @return
     */
    @Override
    public String getType() {
        return "fonction";
    }

    /**
     *
     */
    @Override
    public void verifier() {
    }

    /**
     *
     * @return
     */
    @Override
    public String toMIPS() {

        return "\tjump fonction_"+ idf + "\n"; //TODO : a faire
    }
}
