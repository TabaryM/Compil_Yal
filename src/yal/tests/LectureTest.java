package yal.tests;

import org.junit.jupiter.api.Test;
import yal.arbre.expressions.Entier;
import yal.arbre.instructions.Lecture;

import static org.junit.jupiter.api.Assertions.*;

class LectureTest {

    @Test
    void toMIPS() {
        Entier entier = new Entier("a",2);
        entier.ajouterTDS();
        Lecture lecture = new Lecture("a",4);
        assert (lecture.toMIPS().equals("\t#Lire un entier depuis l'entrée utilisateur et l'affecter à a\n\tli, $v0, 5\n\tsyscall\n\tsw $v0, 0($s7)\n\n")):"Erreur lecture";
        entier = new Entier("b",3);
        entier.ajouterTDS();
        lecture = new Lecture("b",5);
        assert (lecture.toMIPS().equals("\t#Lire un entier depuis l'entrée utilisateur et l'affecter à b\n\tli, $v0, 5\n\tsyscall\n\tsw $v0, -4($s7)\n\n")):"Erreur lecture";
    }
}