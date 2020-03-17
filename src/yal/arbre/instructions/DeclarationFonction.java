package yal.arbre.instructions;

import yal.arbre.ArbreAbstrait;
import yal.arbre.BlocDInstructions;
import yal.arbre.expressions.Entier;
import yal.arbre.gestionnaireTDS.ErreurSemantique;
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

    @Override
    public String toString() {
        return idf;
    }

    @Override
    public boolean contientRetourne() {
        return instructions.contientRetourne();
    }
}
