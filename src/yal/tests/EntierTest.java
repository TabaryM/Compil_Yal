package yal.tests;

import org.junit.jupiter.api.Test;
import yal.arbre.expressions.Entier;

class EntierTest {

    @Test
    void toMIPS() {
        Entier entier = new Entier("a",1);
        entier.ajouterTDS();
        assert (entier.toMIPS().equals("\tlw $v0, 0($s7)\n")):"Erreur Entier toMips";
        entier = new Entier("b",2);
        entier.ajouterTDS();
        assert (entier.toMIPS().equals("\tlw $v0, -4($s7)\n")):"Erreur Entier toMips";
    }
}