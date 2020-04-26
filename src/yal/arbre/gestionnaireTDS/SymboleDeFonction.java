package yal.arbre.gestionnaireTDS;

import yal.arbre.BlocDInstructions;
import yal.arbre.expressions.variable.declaration.DeclarationEntier;

public class SymboleDeFonction extends Symbole{
    private int nbParametres;
    private BlocDInstructions instructions;

    public SymboleDeFonction(int nbParametres, BlocDInstructions instructions) {
        super("fonction", TDS.getInstance().getNumBloc());
        this.nbParametres = nbParametres;
        this.instructions = instructions;
    }

    public int getNbParametres(){
        return nbParametres;
    }

    public void enteteToMIPS(StringBuilder stringBuilder){
        // On stocke l'adresse à laquelle retourner une fois la fonction finie
        stringBuilder.append("\tsw $ra, 0($sp)\t# On stocke l'adresse de retour de la fonction\n\tadd $sp, $sp, -4\n");
        // Chainage dynamique
        stringBuilder.append("\tsw $s2, 0($sp)\t# On stocke l'adresse de la base des variables locales\n");
        stringBuilder.append("\t# On stocke l'adresse du sommet de pile pour accéder aux variables\n");
        stringBuilder.append("\tmove $s2, $sp\n\tadd $sp, $sp, -4\n");
    }

    public void allocationMemoireVarLocalFonctionToMIPS(StringBuilder stringBuilder){
        // empilage des variables locales
        int place = TDS.getInstance().getTableCourrante().getCptDepl();
        stringBuilder.append("\tadd $sp, $sp, ");
        stringBuilder.append(place);
        stringBuilder.append("\t# Emplacement mémoire pour les variables locales\n");
        stringBuilder.append("\t# Initialisation des variables locales à 0\n");
        for(Entree entree : TDS.getInstance().getTableCourrante()){
            SymboleDeVariable symboleDeVariable = ((SymboleDeVariable) TDS.getInstance().identifier(entree));
            stringBuilder.append("\tli, $v0, 0\n");
            stringBuilder.append("\tsw, $v0, ");
            stringBuilder.append(symboleDeVariable.getDepl()-4);
            stringBuilder.append("($s2)\n");
        }

        // Assigner les paramètres à leur position
        for(int i = 0; i < nbParametres; i++){
            stringBuilder.append("\tlw $v0, ");
            stringBuilder.append(i*4+4);
            stringBuilder.append("($s2)\t#On récupère la valeur du paramètre effectif\n");

            stringBuilder.append("\tsw $v0, ");
            stringBuilder.append(-((nbParametres-i)*4)-4);
            stringBuilder.append("($s2)\t#On récupère la valeur du paramètre effectif\n");
        }
    }

    public void instructionFonctionToMIPS(StringBuilder stringBuilder){
        // Liste d'instructions
        stringBuilder.append(instructions.toMIPS());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SymboleDeFonction{");
        sb.append("nbParametres=").append(nbParametres);
        sb.append('}');
        return sb.toString();
    }
}
