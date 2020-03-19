package yal.arbre.declaration;

import yal.arbre.ArbreAbstrait;
import yal.arbre.BlocDInstructions;
import yal.arbre.expressions.Entier;
import yal.arbre.gestionnaireTDS.*;
import yal.exceptions.AjoutTDSException;
import yal.exceptions.AnalyseSemantiqueException;
import yal.exceptions.AnalyseSyntaxiqueException;

import java.util.ArrayList;

public class DeclarationFonction extends Declaration {
    private BlocDInstructions instructions;
    private ArrayList<Declaration> variablesLocales;
    private ArrayList<DeclarationEntier> parametres;

    /**
     * Declaration de fontion avec parametres et variables locales.
     * @param idf String : identifiant de la fonction
     * @param variablesLocales BlocDInstructions : liste des variables locales
     * @param parametres ArrayList<Entier> : liste des variables parametres
     * @param instructions ArbreAbstrait : Arbre des instructions
     * @param numLig int : numéro de la ligne où la fonction a été déclarée
     */
    public DeclarationFonction(String idf, ArrayList<Declaration> variablesLocales, ArrayList<DeclarationEntier> parametres, ArbreAbstrait instructions, int numLig){
        super(idf, numLig);
        this.variablesLocales = variablesLocales;
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
    public DeclarationFonction(String idf, ArrayList<DeclarationEntier> parametres, ArbreAbstrait instructions, int numLig){
        this(idf, new ArrayList<>(), parametres, instructions, numLig);
    }

    /**
     * Declaration de fontion sans parametres, avec variables locales.
     * @param idf String : identifiant de la fonction
     * @param instructions ArbreAbstrait : Arbre des instructions
     * @param variablesLocales BlocDInstructions : liste des variables locales
     * @param numLig int : numéro de la ligne où la fonction a été déclarée
     */
    public DeclarationFonction(String idf, ArbreAbstrait instructions, ArrayList<Declaration> variablesLocales, int numLig){
        this(idf, variablesLocales, new ArrayList<>(), instructions, numLig);
    }

    /**
     * Declaration de fonction sans variables locales ni parametres
     * @param idf String : identifiant de la fonction
     * @param instructions ArbreAbstrait : Arbre des instructions
     * @param numLig int : numéro de la ligne où la fonction a été déclarée
     */
    public DeclarationFonction(String idf, ArbreAbstrait instructions, int numLig){
        this(idf, new ArrayList<>(), new ArrayList<>(), instructions, numLig);
    }

    @Override
    public void verifier() {
        // Verification de l'existence de l'instruction retourne
        if(!contientRetourne()){
            AnalyseSemantiqueException exception = new AnalyseSemantiqueException(super.getNoLigne(), "La fonction "+getIdf()+" ne contient aucune instruction Retourne");
            ErreurSemantique.getInstance().ajouter(exception);
        }
        instructions.verifier();
    }

    @Override
    public String toMIPS() {
        return "";
    }

    public void ajouterTDS(){
        SymboleDeFonction symboleDeFonction = new SymboleDeFonction(parametres.size(), instructions);
        try {
            TDS.getInstance().ajouter(new Entree("fonction_"+getIdf(), parametres.size()), symboleDeFonction);
        } catch (AjoutTDSException e) {
            AnalyseSemantiqueException exception = new AnalyseSemantiqueException(super.getNoLigne(), "Double déclaration de la fonction " + getIdf());
            ErreurSemantique.getInstance().ajouter(exception);
        }
        TDS.getInstance().entreeBloc();
        symboleDeFonction.setNumBloc(TDS.getInstance().getNumBloc());
        for(DeclarationEntier entier : parametres){
            try {
                entier.ajouterTDS();
            } catch (Exception e) {
                AnalyseSemantiqueException exception = new AnalyseSemantiqueException(super.getNoLigne(), "Double déclaration du parametre " +entier.getIdf()+" dans la fonction "+getIdf());
                ErreurSemantique.getInstance().ajouter(exception);
            }
        }
        for(Declaration entier : variablesLocales){
            try {
                if(!entier.getClass().getSimpleName().equals("DeclarationEntier")){
                    AnalyseSemantiqueException exception = new AnalyseSemantiqueException(getNoLigne(), "Dans la fonction "+getIdf()+" un parametre ne peut pas être une fonction");
                    ErreurSemantique.getInstance().ajouter(exception);
                }
                entier.ajouterTDS();
            } catch (Exception e) {
                AnalyseSemantiqueException exception = new AnalyseSemantiqueException(getNoLigne(), "Double déclaration du parametre " +entier.getIdf()+" dans la fonction "+getIdf());
                ErreurSemantique.getInstance().ajouter(exception);
            }
        }
        // Ajouter à la TDS les variables locales
        TDS.getInstance().sortieBloc();
    }

    @Override
    public boolean contientRetourne() {
        return instructions.contientRetourne();
    }
}
