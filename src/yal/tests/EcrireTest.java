package yal.tests;

import org.junit.jupiter.api.Test;
import yal.arbre.expressions.ConstanteEntiere;
import yal.arbre.expressions.Entier;
import yal.arbre.expressions.Expression;
import yal.arbre.instructions.Ecrire;

import static org.junit.jupiter.api.Assertions.*;

class EcrireTest {

    @Test
    void toMIPS() {
        //test constante
        Ecrire ecrire = new Ecrire(new ConstanteEntiere("1",0),0);
        assert (ecrire.toMIPS().equals("\t# Ecrire la valeur : 1\n\tli $v0, 1\n\tmove $a0, $v0\n\tli $v0, 1\n\tsyscall\n\n\t# new line\n\tli $v0, 4\n\tla $a0, newline\n\tsyscall\n")):"Erreur Ecrire toMips constante";
        ecrire = new Ecrire(new ConstanteEntiere("9",0),0);
        assert (ecrire.toMIPS().equals("\t# Ecrire la valeur : 9\n\tli $v0, 9\n\tmove $a0, $v0\n\tli $v0, 1\n\tsyscall\n\n\t# new line\n\tli $v0, 4\n\tla $a0, newline\n\tsyscall\n")):"Erreur Ecrire toMips constante";
        //test variable
        Entier entier = new Entier("a",2);
        entier.ajouterTDS();
        ecrire = new Ecrire(entier,0);
        assert(ecrire.toMIPS().equals("\t# Ecrire la valeur : a\n\tlw $v0, 0($s7)\n\tmove $a0, $v0\n\tli $v0, 1\n\tsyscall\n\n\t# new line\n\tli $v0, 4\n\tla $a0, newline\n\tsyscall\n")):"Erreur Ecrire toMips variable";
        entier = new Entier("b",3);
        entier.ajouterTDS();
        ecrire = new Ecrire(entier,0);
        assert(ecrire.toMIPS().equals("\t# Ecrire la valeur : b\n\tlw $v0, -4($s7)\n\tmove $a0, $v0\n\tli $v0, 1\n\tsyscall\n\n\t# new line\n\tli $v0, 4\n\tla $a0, newline\n\tsyscall\n")):"Erreur Ecrire toMips variable";
    }
}