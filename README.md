# YAL

  - ~~Commentaires~~
  - ~~Instruction écrire~~
    - ~~Constantes~~
    - ~~Variables~~
    - ~~Vérifier que la variable est déclarée~~
    - ~~Si booléen il faut écrire "vrai" ou "faux"~~
  - ~~Déclaration~~
    - ~~Double déclaration~~
    - ~~Allouer la pile~~
  - ~~Affectation~~
    - ~~Constante~~
    - ~~Variable~~
    - ~~Vérifier que la variable est déclarée~~
  - ~~Lecture~~
    -  ~~Vérifier que la variable est déclarée~~  

  - ~~Expression quelconques~~
    - ~~'-' (moins) EXP~~
    - ~~non EXP -> requiert booléen~~
    - ~~( )~~
    - ~~EXP OPER EXP~~
      - ~~CSTE OPER CSTE~~
      - ~~CSTE OPER EXP~~
      - ~~EXP OPER CSTE~~
      - ~~EXP OPER EXP~~
  - ~~Opérations~~
    - ~~Opérateurs entier X entier retourne entier~~
        - ~~'+'~~
        - ~~'-'~~
        - ~~'*'~~
        - ~~'/'~~ ~~DIV/0 INTERDITE~~
    - ~~Comparateurs retourne booléens~~
    - ~~entier X entier~~
            - ~~'>'~~
            - ~~'<'~~
        - ~~booléen X booléen~~
            - ~~'et'~~
            - ~~'ou'~~
        - ~~entier X entier | booléen X booléen~~
            - ~~'=='~~
            - ~~'!='~~
  - ~~Instruction Conditionnelle~~
    - ~~tant que~~
        - ~~expression type booléen~~
        - ~~obligatoirement évaluée avant toute entrée dans la boucle~~
    - ~~si~~
        - ~~expression type booléen~~
  - ~~Instruction itérative~~
  - ORDRE des opérateurs:
    - '()', 'non', '*' et '/', '+' et '-', '<' et '>', '==' et '!=', 'et', 'ou'
    - Evaluer toutes les expression de gauche à droite par défaut


- TO FIX:
    - Evaluer de gauche à droite ?!
        - soustraction multiple ? (1-2-3-4-5 = 3 au lieu de -13)
        - division en cascade ? (1000/2/4/5 = 25)
    - priorités sans parentheses
    - 1>2>3>4>5 ou 1<2<3<4<5 autorisé ?
    - test ambigus complexe 1+2 == 2+1 ; 1 < 0 + 2
    - corriger les et et ou logique dans les fichier test
    - forcer des trucs nuls genre non non ou --