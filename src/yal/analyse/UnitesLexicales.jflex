package yal.analyse ;

import java_cup.runtime.*;
import yal.exceptions.AnalyseLexicaleException;
      
%%
   
%class AnalyseurLexical
%public

%line
%column
    
%type Symbol
%eofval{
        return symbol(CodesLexicaux.EOF) ;
%eofval}

%cup

%{

  private StringBuilder chaine ;

  private Symbol symbol(int type) {
	return new Symbol(type, yyline, yycolumn) ;
  }

  private Symbol symbol(int type, Object value) {
	return new Symbol(type, yyline, yycolumn, value) ;
  }
%}

idf = [A-Za-z_][A-Za-z_0-9]*

csteE = [0-9]+

finDeLigne = \r|\n
espace = {finDeLigne}  | [ \t\f]

commentaire = "//".*{finDeLigne}


%%

"programme"            { return symbol(CodesLexicaux.PROGRAMME); }
"debut"                { return symbol(CodesLexicaux.DEBUT); }
"fin"              	   { return symbol(CodesLexicaux.FIN); }

"ecrire"               { return symbol(CodesLexicaux.ECRIRE); }
"lire"                 { return symbol(CodesLexicaux.LIRE); }
"entier"               { return symbol(CodesLexicaux.ENTIER); }

"si"                    { return symbol(CodesLexicaux.CONDITION); }
"alors"                    { return symbol(CodesLexicaux.ALORS); }
"sinon"                    { return symbol(CodesLexicaux.SINON); }
"finsi"                    { return symbol(CodesLexicaux.FINSI); }

"tantque"                    { return symbol(CodesLexicaux.BOUCLE); }
"repeter"                    { return symbol(CodesLexicaux.REPETER); }
"fintantque"                    { return symbol(CodesLexicaux.FINTANTQUE); }

";"                    { return symbol(CodesLexicaux.POINTVIRGULE); }
"="                    { return symbol(CodesLexicaux.EGAL); }

"+"                    { return symbol(CodesLexicaux.ADDITION); }
"-"                    { return symbol(CodesLexicaux.SOUSTRACTION); }
"*"                    { return symbol(CodesLexicaux.MULTIPLICATION); }
">"                    { return symbol(CodesLexicaux.SUPERIEURA); }
"<"                    { return symbol(CodesLexicaux.INFERIEURA); }
"/"                    { return symbol(CodesLexicaux.DIVISION); }
"=="                    { return symbol(CodesLexicaux.EGALITE); }
"!="                    { return symbol(CodesLexicaux.DIFFERENT); }
"et"                    { return symbol(CodesLexicaux.ETLOGIQUE); }
"ou"                    { return symbol(CodesLexicaux.OULOGIQUE); }
"non"                    { return symbol(CodesLexicaux.INVERSE); }
"("                    { return symbol(CodesLexicaux.PAR_OUVRANTE); }
")"                    { return symbol(CodesLexicaux.PAR_FERMANTE); }

{csteE}      	       { return symbol(CodesLexicaux.CSTENTIERE, yytext()); }

{idf}      	           { return symbol(CodesLexicaux.IDF, yytext()); }

{espace}               { }
{commentaire}          { }
.                      { throw new AnalyseLexicaleException(yyline, yycolumn, yytext()) ; }

