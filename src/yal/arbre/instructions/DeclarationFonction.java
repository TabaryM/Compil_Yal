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
    private ArrayList<Entier> parametres;

    public DeclarationFonction(String idf, ArbreAbstrait a, ArrayList<Entier> parametres, int n) {
        super(n);
        this.idf = idf;
        instructions = (BlocDInstructions) a;
        this.parametres = parametres;
    }

    public DeclarationFonction(String idf, ArbreAbstrait a, int n) {
        super(n);
        new DeclarationFonction(idf, a, new ArrayList<>(), n);
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
        StringBuilder stringBuilder = new StringBuilder();
        // TODO : ajouter l'empilage des variables locales
        // On stocke l'adresse à laquelle retourner une fois la fonction finie
        stringBuilder.append("\tsw $ra, 0($sp)\n\tadd $sp, $sp, -4\n");
        // Chainage dynamique
        stringBuilder.append("\tsw $sp, 0($sp)\n\tadd $sp, $sp, -4\n");

        return stringBuilder.toString();
    }

    public void ajouterTDS(){
        try {
            TDS.getInstance().ajouter(new Entree(idf, parametres.size()), new SymboleDeFonction(parametres.size(), instructions));
        } catch (AjoutTDSException e) {
            AnalyseSemantiqueException exception = new AnalyseSemantiqueException(super.getNoLigne(), "Double déclaration de la fonction " + idf);
            ErreurSemantique.getInstance().ajouter(exception);
        }
        int cptDepl = 0;
        TDS.getInstance().entreeBloc();
        for(Entier entier : parametres){
            try {
                TDS.getInstance().ajouter(new Entree(entier.getIdf()), new SymboleDeVariable(cptDepl));
            } catch (Exception e) {
                AnalyseSemantiqueException exception = new AnalyseSemantiqueException(super.getNoLigne(), "Double déclaration du parametre " +entier.getIdf()+" dans la fonction "+idf);
                ErreurSemantique.getInstance().ajouter(exception);
            }
            cptDepl -= 4;
        }
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
