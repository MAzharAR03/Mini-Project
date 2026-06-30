package myparser;
import java_cup.runtime.*;
import java.io.*;

%%
%cup
%line
%{
    private static final String[] TOKEN_NAMES = new String[33];
    static {
        TOKEN_NAMES[2]  = "NAME";
        TOKEN_NAMES[3]  = "ID";
        TOKEN_NAMES[4]  = "NUM";
        TOKEN_NAMES[5]  = "STRING";
        TOKEN_NAMES[6]  = "COMMA";
        TOKEN_NAMES[7]  = "ASSIGN";
        TOKEN_NAMES[8]  = "REPEAT";
        TOKEN_NAMES[9]  = "TIMES";
        TOKEN_NAMES[10] = "UNTIL";
        TOKEN_NAMES[11] = "FROM";
        TOKEN_NAMES[12] = "TO";
        TOKEN_NAMES[13] = "IF_TOKEN";
        TOKEN_NAMES[14] = "ELSE_TOKEN";
        TOKEN_NAMES[15] = "INPUT_TOKEN";
        TOKEN_NAMES[16] = "PRINT_TOKEN";
        TOKEN_NAMES[17] = "LBRACE";
        TOKEN_NAMES[18] = "RBRACE";
        TOKEN_NAMES[19] = "LPAREN";
        TOKEN_NAMES[20] = "RPAREN";
        TOKEN_NAMES[21] = "PLUS";
        TOKEN_NAMES[22] = "MINUS";
        TOKEN_NAMES[23] = "DIVIDE";
        TOKEN_NAMES[24] = "MULTIPLY";
        TOKEN_NAMES[25] = "LESS_THAN";
        TOKEN_NAMES[26] = "GREATER_THAN";
        TOKEN_NAMES[27] = "EQUALS";
        TOKEN_NAMES[28] = "NOT_EQUALS";
        TOKEN_NAMES[29] = "NUMBER";
        TOKEN_NAMES[30] = "STRING_LITERAL";
        TOKEN_NAMES[31] = "NUMVAL";
        TOKEN_NAMES[32] = "TEXTVAL";
    }

    private Symbol symbol(int type) {
        System.out.println("TOKEN: " + TOKEN_NAMES[type]);
        Node node = new Node(TOKEN_NAMES[type], TOKEN_NAMES[type], yyline);
        return new Symbol(type, node);
    }

    private Symbol symbol(int type, Object value) {
        System.out.println("TOKEN: " + TOKEN_NAMES[type] + ":" + value);
        Node node = new Node(String.valueOf(value), TOKEN_NAMES[type], yyline);
        return new Symbol(type, node);
    }
%}

WhiteSpace = [ \t\r\n\f]
LETTER = [a-zA-Z]
DIGIT = [0-9]
NUMBER = {DIGIT}+
NUMVAL = {LETTER}{LETTER}{DIGIT}
TEXTVAL = {LETTER}{LETTER}"$"
STRING_LITERAL = {LETTER}+
%%

{NUMVAL} { return symbol(sym.NUMVAL, yytext()); }
{TEXTVAL}  { return symbol(sym.TEXTVAL, yytext()); }
{NUMBER}  { return symbol(sym.NUMBER, Integer.parseInt(yytext())); }


"name" {return symbol(sym.NAME);}
"id" {return symbol(sym.ID);}
"num" {return symbol(sym.NUM);}
"string" {return symbol(sym.STRING);}
"repeat" {return symbol(sym.REPEAT);}
"times" {return symbol(sym.TIMES);}
"until" {return symbol(sym.UNTIL);}
"from" {return symbol(sym.FROM);}
"to" {return symbol(sym.TO);}
"if" {return symbol(sym.IF_TOKEN);}
"else" {return symbol(sym.ELSE_TOKEN);}
"input" {return symbol(sym.INPUT_TOKEN);}
"print" {return symbol(sym.PRINT_TOKEN);}

{STRING_LITERAL}  { return symbol(sym.STRING_LITERAL, yytext()); }

"==" {return symbol(sym.EQUALS);}
"!=" {return symbol(sym.NOT_EQUALS);}

","  {return symbol(sym.COMMA);}
"=" {return symbol(sym.ASSIGN);}
"{" {return symbol(sym.LBRACE);}
"}" {return symbol(sym.RBRACE);}
"(" {return symbol(sym.LPAREN);}
")" {return symbol(sym.RPAREN);}
"+" {return symbol(sym.PLUS);}
"-" {return symbol(sym.MINUS);}
"/" {return symbol(sym.DIVIDE);}
"*" {return symbol(sym.MULTIPLY);}
">" {return symbol(sym.GREATER_THAN);}
"<" {return symbol(sym.LESS_THAN);}


{WhiteSpace} { /* ignore white space. */ }
. { System.err.println("Illegal Character: "+yytext()+" at line "+yyline); }