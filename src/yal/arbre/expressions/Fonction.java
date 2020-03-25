package yal.arbre.expressions;

import yal.arbre.gestionnaireTDS.*;
import yal.exceptions.AnalyseSemantiqueException;

import java.util.ArrayList;

public class Fonction extends Expression {
    private String idf;
    private ArrayList<Expression> parametresEffectifs;

    /**
     * Appel de la fonction dans le code
     * @param idf identifiant de la fonction appelé
     * @param numLig numéro de la ligne à laquelle la fonction est appelée
     */
    public Fonction(String idf, int numLig) {
        this(idf, new ArrayList<>(), numLig);
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
        // On vérifie que la fonction n'a pas déjà été déclarée avec le même nombre de paramètres
        Entree entreeTmp = new Entree("fonction_"+idf, parametresEffectifs.size());
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
        // Empiler l'@dresse de retour
        // Empiler le chainage dynamique
        // Aller au code source de la fonction
        // Associer les valeur des paramètres effectifs aux paramètres (dans la pile)
        // Alouer la place pour les variables locales (dans la pile)
        // TODO : assigner les valeurs des parametres effectifs aux parametre réel de la fonction
        for(Expression expression : parametresEffectifs){
            stringBuilder.append("\t# Empilage du parametre effectif : ");
            stringBuilder.append(expression.toString());
            stringBuilder.append("\n");
            stringBuilder.append(expression.toMIPS());
            stringBuilder.append("\tsw $v0, 0($sp)\n\tadd $sp, $sp, -4\n");
        }
        stringBuilder.append("\t# On stocke l'adresse du sommet de pile pour accéder aux variables\n");
        stringBuilder.append("\tmove $s2, $sp\n");

        stringBuilder.append("\tjal ");
        stringBuilder.append("fonction_");
        stringBuilder.append(idf);
        stringBuilder.append("_params_");
        stringBuilder.append(parametresEffectifs.size());
        stringBuilder.append("\n");

        return stringBuilder.toString();
    }

    @Override
    public boolean contientRetourne() {
        return false;
    }

    @Override
    public String toString() {
        return idf;
    }
}
