package yal.tests;

import org.junit.jupiter.api.Test;
import yal.arbre.expressions.ConstanteEntiere;
import yal.arbre.expressions.Entier;
import yal.arbre.instructions.Affectation;

import static org.junit.jupiter.api.Assertions.*;

class AffectationTest {

    @Test
    void toMIPS() {
        //affecter constante
        Entier entier = new Entier("a",2);
        entier.ajouterTDS();
        Affectation affectation = new Affectation(new ConstanteEntiere("3",4),"a",4);
        assert (affectation.toMIPS().equals("\t#Assigner à a la valeur 3\n\tli $v0, 3\n\tsw $v0, 0($s7)\n\n")):"Erreur affectation constante";
        affectation = new Affectation(new ConstanteEntiere("4",5),"a",5);
        assert (affectation.toMIPS().equals("\t#Assigner à a la valeur 4\n\tli $v0, 4\n\tsw $v0, 0($s7)\n\n")):"Erreur affectation constante";
        entier = new Entier("b",3);
        entier.ajouterTDS();
        affectation = new Affectation(new ConstanteEntiere("9",6),"b",6);
        assert (affectation.toMIPS().equals("\t#Assigner à b la valeur 9\n\tli $v0, 9\n\tsw $v0, -4($s7)\n\n")):"Erreur affectation constante";
        //affecter variable
        affectation = new Affectation(entier,"a",7);
        assert (affectation.toMIPS().equals("\t#Assigner à a la valeur b\n\tlw $v0, -4($s7)\n\tsw $v0, 0($s7)\n\n")):"Erreur affectation variable";
        affectation = new Affectation(entier,"b",8);
        assert (affectation.toMIPS().equals("\t#Assigner à b la valeur b\n\tlw $v0, -4($s7)\n\tsw $v0, -4($s7)\n\n")):"Erreur affectation variable";
    }
}