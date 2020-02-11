package yal.arbre.expressions.operateurs;

public class Division implements Operateur{
    public Division() {}

    @Override
    public String toMips() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\tbeqz $v0, ErrDiv\t#Si l'opérande droite vaut 0 : On affiche un message d'erreur et on ferme le programme\n");
        stringBuilder.append("\t#L'operande droite n'est pas nul\n");
        stringBuilder.append("\tdiv $v0, $t8, $v0");
        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        return "/";
    }

    @Override
    public String getNatureRetour() {
        return "entier";
    }
}
