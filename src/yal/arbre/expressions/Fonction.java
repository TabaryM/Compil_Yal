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
            stringBuilder.append("\nfonction_"+idf.getIdf()+":\n");

            stringBuilder.append(instructions.toMIPS());
            TDS.getInstance().ajoutFonction(idf,stringBuilder.toString());
        }
    }

    /**
     * Appel de la fonction dans le code
     * @param idf identifiant de la fonction appelé
     * @param numLig numéro de la ligne à laquelle la fonction est appelée
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
     * TODO : trouver une utilité à cette fonction
     * @return un string qui pue
     */
    @Override
    public String getType() {
        return "fonction";
    }

    /**
     * TODO : trouver une liste de vérification correcte de la fonction
     */
    @Override
    public void verifier() {
        if(instructions != null){
            // Verification de l'existence de l'instructino retourne
            boolean contientRetourne = false;
            for(ArbreAbstrait arbreAbstrait : instructions){
                if(arbreAbstrait.getClass().getSimpleName().equals("Retourne")){
                    contientRetourne = true;
                }
            }
            if(!contientRetourne){
                AnalyseSemantiqueException exception = new AnalyseSemantiqueException(super.getNoLigne(), "La fonction "+idf+" ne contient aucune instruction Retourne");
                ErreurSemantique.getInstance().ajouter(exception);
            }
            instructions.verifier();
        }
    }

    /**
     * TODO : trouver si on doit retourner le code avec la liste d'instruction ou le code d'appel de fonction
     * @return un code MIPS
     */
    @Override
    public String toMIPS() {

        return "\tjump fonction_"+ idf.getIdf() + "\n"; //TODO : a faire
    }

    @Override
    public String toString() {
        return idf.getIdf();
    }
}
