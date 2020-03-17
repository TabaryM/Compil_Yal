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
        this.idf = idf;
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
        this.parametresEffectifs = new ArrayList<>();
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

        // On vérifie que la fonction n'a pas déjà été déclarée avec le même nombre de paramètres
        int nbParam = parametresEffectifs.size();
        Entree entreeTmp = new Entree("fonction_"+idf+"_params"+nbParam);
        Symbole symboleDeFonction = TDS.getInstance().identifier(entreeTmp);
        if(symboleDeFonction == null){
            AnalyseSemantiqueException exception = new AnalyseSemantiqueException(super.getNoLigne(), "Aucune fonction "+idf+" n'attend "+parametresEffectifs.size()+" parametres.");
            ErreurSemantique.getInstance().ajouter(exception);
        }
    }

    /**
     * Genere en MIPS l'appel d'une fonction
     * @return un code MIPS
     */
    @Override
    public String toMIPS() {
        StringBuilder stringBuilder = new StringBuilder();
        // TODO : assigner les valeurs des parametres effectifs aux parametre réel de la fonction
        stringBuilder.append("\tjal ");
        stringBuilder.append("fonction_");
        stringBuilder.append(idf);
        stringBuilder.append("_params");
        stringBuilder.append(parametresEffectifs.size());
        stringBuilder.append("\n");
        return stringBuilder.toString();
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
