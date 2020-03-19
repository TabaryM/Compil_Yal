package yal.arbre.instructions;

import yal.arbre.ArbreAbstrait;
import yal.arbre.BlocDInstructions;
import yal.arbre.expressions.Entier;
import yal.arbre.gestionnaireTDS.*;
import yal.exceptions.AjoutTDSException;
import yal.exceptions.AnalyseSemantiqueException;

import java.util.ArrayList;

public class DeclarationFonction extends ArbreAbstrait {
    private String idf;
    private BlocDInstructions instructions;
    private BlocDInstructions variablesLocales;
    private ArrayList<Entier> parametres;

    /**
     * Declaration de fontion avec parametres et variables locales.
     * @param idf String : identifiant de la fonction
     * @param variablesLocales BlocDInstructions : liste des variables locales
     * @param parametres ArrayList<Entier> : liste des variables parametres
     * @param instructions ArbreAbstrait : Arbre des instructions
     * @param numLig int : numéro de la ligne où la fonction a été déclarée
     */
    public DeclarationFonction(String idf, ArbreAbstrait variablesLocales, ArrayList<Entier> parametres, ArbreAbstrait instructions, int numLig){
        super(numLig);
        this.idf = idf;
        this.variablesLocales = (BlocDInstructions) variablesLocales;
        this.parametres = parametres;
        this.instructions = (BlocDInstructions) instructions;
    }

    /**
     * Declaration de fontion avec parametres, sans variables locales.
     * @param idf String : identifiant de la fonction
     * @param parametres ArrayList<Entier> : liste des variables parametres
     * @param instructions ArbreAbstrait : Arbre des instructions
     * @param numLig int : numéro de la ligne où la fonction a été déclarée
     */
    public DeclarationFonction(String idf, ArrayList<Entier> parametres, ArbreAbstrait instructions, int numLig){
        this(idf, new BlocDInstructions(numLig), parametres, instructions, numLig);
    }

    /**
     * Declaration de fontion sans parametres, avec variables locales.
     * @param idf String : identifiant de la fonction
     * @param instructions ArbreAbstrait : Arbre des instructions
     * @param variablesLocales BlocDInstructions : liste des variables locales
     * @param numLig int : numéro de la ligne où la fonction a été déclarée
     */
    public DeclarationFonction(String idf, ArbreAbstrait instructions, ArbreAbstrait variablesLocales, int numLig){
        this(idf, (BlocDInstructions) variablesLocales, new ArrayList<>(), instructions, numLig);
    }

    /**
     * Declaration de fonction sans variables locales ni parametres
     * @param idf String : identifiant de la fonction
     * @param instructions ArbreAbstrait : Arbre des instructions
     * @param numLig int : numéro de la ligne où la fonction a été déclarée
     */
    public DeclarationFonction(String idf, ArbreAbstrait instructions, int numLig){
        this(idf, new BlocDInstructions(numLig), new ArrayList<>(), instructions, numLig);
    }

    @Override
    public void verifier() {
        // Verification de l'existence de l'instruction retourne
        if(!contientRetourne()){
            AnalyseSemantiqueException exception = new AnalyseSemantiqueException(super.getNoLigne(), "La fonction "+idf+" ne contient aucune instruction Retourne");
            ErreurSemantique.getInstance().ajouter(exception);
        }
        instructions.verifier();
    }

    @Override
    public String toMIPS() {
        return "";
    }

    public void ajouterTDS(){
        try {
            TDS.getInstance().ajouter(new Entree("fonction_"+idf, parametres.size()), new SymboleDeFonction(parametres.size(), instructions, parametres, variablesLocales));
        } catch (AjoutTDSException e) {
            AnalyseSemantiqueException exception = new AnalyseSemantiqueException(super.getNoLigne(), "Double déclaration de la fonction " + idf);
            ErreurSemantique.getInstance().ajouter(exception);
        }
        TDS.getInstance().entreeBloc();
        for(Entier entier : parametres){
            try {
                entier.ajouterTDS();
            } catch (Exception e) {
                AnalyseSemantiqueException exception = new AnalyseSemantiqueException(super.getNoLigne(), "Double déclaration du parametre " +entier.getIdf()+" dans la fonction "+idf);
                ErreurSemantique.getInstance().ajouter(exception);
            }
        }
        // Ajouter à la TDS les variables locales
        TDS.getInstance().sortieBloc();
    }

    @Override
    public String toString() {
        return idf;
    }

    @Override
    public boolean contientRetourne() {
        return instructions.contientRetourne();
    }
}
