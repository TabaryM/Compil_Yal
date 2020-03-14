package yal.arbre.expressions;

import yal.arbre.ArbreAbstrait;
import yal.arbre.BlocDInstructions;
import yal.arbre.gestionnaireTDS.*;
import yal.arbre.instructions.Affectation;
import yal.exceptions.AnalyseSemantiqueException;

import java.util.ArrayList;

public class Fonction extends Expression {
    private BlocDInstructions instructions;
    private String idf;
    private ArrayList<Entier> parametres;
    private ArrayList<Expression> parametresEffectifs;

    /**
     * Déclaration d'une fonction dans l'arbre abstrait
     * @param idf identifiant de la fonction
     * @param a la liste d'instruciton de la fonction
     * @param numLig numéro de la ligne de la déclaration de la fonction
     */
    public Fonction(String idf, ArbreAbstrait a, int numLig){
        super(numLig);
        instructions = (BlocDInstructions) a;
        this.idf = idf;
        parametres = new ArrayList<>();
    }

    /**
     * Déclaration d'une fonction dans l'arbre abstrait
     * @param idf identifiant de la fonction
     * @param a la liste d'instruciton de la fonction
     * @param parametres la liste de parametres de la fonction
     * @param numLig numéro de la ligne de la déclaration de la fonction
     */
    public Fonction(String idf, ArbreAbstrait a, ArrayList<Entier> parametres, int numLig){
        super(numLig);
        instructions = (BlocDInstructions) a;
        this.idf = "fonction_" + idf + parametres.size();
        this.parametres = parametres;
    }

    /**
     * Appel de la fonction dans le code
     * @param idf identifiant de la fonction appelé
     * @param numLig numéro de la ligne à laquelle la fonction est appelée
     */
    public Fonction(String idf, int numLig) {
        super(numLig);
        this.idf = idf;
    }

    /**
     * Appel de la fonction dans le code
     * @param idf identifiant de la fonction appelé
     * @param numLig numéro de la ligne à laquelle la fonction est appelée
     */
    public Fonction(String idf, ArrayList<Expression> parametresEffectifs, int numLig) {
        super(numLig);
        this.idf = idf;
        this.parametresEffectifs = parametresEffectifs;
    }

    /**
     * Ajoute la fonction et ses parametres (si parametres il y a) à la TDS
     */
    public void ajouterTDS() {
        verifier();
        try {
            TDS.getInstance().ajouter(new Entree(idf), new SymboleDeFonction(parametres.size(), instructions));
        } catch (Exception e) {
            AnalyseSemantiqueException exception = new AnalyseSemantiqueException(super.getNoLigne(), "Double déclaration de la fonction " + idf);
            ErreurSemantique.getInstance().ajouter(exception);
        }
        for (Entier e : parametres) {
            try {
                TDS.getInstance().ajouter(new Entree(idf + e.getIdf()), new SymboleDeVariable(TDS.getInstance().getCpt()));
            }
            catch(Exception exce){
                AnalyseSemantiqueException exception = new AnalyseSemantiqueException(super.getNoLigne(), "Double déclaration du parametre " +e.getIdf()+" dans la fonction "+idf);
                ErreurSemantique.getInstance().ajouter(exception);
            }
        }
    }

    /**
     * Renvoie le type de retour de la fonction
     * @return un string qui pue
     */
    @Override
    public String getType() {
        return "entier";
    }

    /**
     * Cherche si dans une fonction il y a un retourne
     */
    @Override
    public void verifier() {
        if(instructions != null){
            // Verification de l'existence de l'instruction retourne
            if(!contientRetourne()){
                AnalyseSemantiqueException exception = new AnalyseSemantiqueException(super.getNoLigne(), "La fonction "+idf+" ne contient aucune instruction Retourne");
                ErreurSemantique.getInstance().ajouter(exception);
            }
            instructions.verifier();
        }
    }

    /**
     * Genere en MIPS l'appel d'une fonction
     * @return un code MIPS
     */
    @Override
    public String toMIPS() {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < parametresEffectifs.size(); i++){
            TDS tds = TDS.getInstance();
            Symbole tmp = tds.identifier(new Entree(idf));

            Affectation a = new Affectation(parametresEffectifs.get(i), parametres.get(i).getIdf(), getNoLigne());
        }
        return "\tjal "+ idf + "\n";
    }

    @Override
    public boolean contientRetourne() {
        return instructions.contientRetourne();
    }

    @Override
    public String toString() {
        return idf;
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
