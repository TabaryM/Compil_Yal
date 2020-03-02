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
        verifier();
        try {
            TDS.getInstance().ajouter(idf, new SymboleDeFonction(TDS.getInstance().getCpt(), instructions));
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
            if(!contientRetourne(true)){
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
        // TODO : empiler les parametres
        return "\tjal fonction_"+ idf + "\n";
        // l'adresse de retour est donnée par l'insrtuction jal dans le registre $ra
        // On empile immédiatement après l'appel de la fonction le registre $ra
    }

    @Override
    public boolean contientRetourne(boolean dansUneFonction) {
        return instructions.contientRetourne(true);
    }

    @Override
    public String toString() {
        return idf.getIdf();
    }

    public static String initBlocFonction(){
        StringBuilder stringBuilder = new StringBuilder();
        // On stocke l'adresse à laquelle retourner une fois la fonction finie
        stringBuilder.append("\tsw $ra, 0($sp)\n\tadd $sp, $sp, -4\n");
        // Chainage dynamique
        stringBuilder.append("\tsw $sp, 0($sp)\n\tadd $sp, $sp, -4\n");

        return stringBuilder.toString();

    }
}
