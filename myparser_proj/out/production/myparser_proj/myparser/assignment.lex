package myparser;
import java_cup.runtime.*;
import java.io.*;

%%
%cup
%line
%{
    private static final String[] TOKEN_NAMES = new String[24];
    static {
        TOKEN_NAMES[2]  = "AT_800";
        TOKEN_NAMES[3]  = "AT_B";
        TOKEN_NAMES[4]  = "AT_E";
        TOKEN_NAMES[5]  = "AT_F";
        TOKEN_NAMES[6]  = "AT_I";
        TOKEN_NAMES[7]  = "LPAREN";
        TOKEN_NAMES[8]  = "RPAREN";
        TOKEN_NAMES[9]  = "LBRACE";
        TOKEN_NAMES[10] = "RBRACE";
        TOKEN_NAMES[11] = "COMMA";
        TOKEN_NAMES[12] = "DOT";
        TOKEN_NAMES[13] = "ASSIGN";
        TOKEN_NAMES[14] = "PLUS";
        TOKEN_NAMES[15] = "TIMES";
        TOKEN_NAMES[16] = "OP_LT";
        TOKEN_NAMES[17] = "OP_GT";
        TOKEN_NAMES[18] = "OP_EQ";
        TOKEN_NAMES[19] = "OP_NEQ";
        TOKEN_NAMES[20] = "KW_NUM";
        TOKEN_NAMES[21] = "KW_CHAR";
        TOKEN_NAMES[22] = "IDENTIFIER";
        TOKEN_NAMES[23] = "NUMBER";
    }

    private Symbol symbol(int type) {
        System.out.println("TOKEN: " + TOKEN_NAMES[type]);
        return new Symbol(type);
    }

    private Symbol symbol(int type, Object value) {
        System.out.println("TOKEN: " + TOKEN_NAMES[type] + " -> " + value);
        return new Symbol(type, value);
    }
C:\Users\maros\.jdks\openjdk-23.0.1\bin\java.exe "-javaagent:D:\Program Files\IntelliJ IDEA Community Edition 2024.3.1.1\lib\idea_rt.jar=63645:D:\Program Files\IntelliJ IDEA Community Edition 2024.3.1.1\bin" -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 jlex.Main
Error: Could not find or load main class jlex.Main
Caused by: java.lang.ClassNotFoundException: jlex.Main

NUM = [0-3]
WhiteSpace = [ \t\r\n\f]
%%

aa[0-9] {return symbol(sym.IDENTIFIER, yytext());}
{NUM}+  {return symbol(sym.NUMBER, Integer.parseInt(yytext()));}

"aa<" {return symbol(sym.OP_LT);}
"aa>" {return symbol(sym.OP_GT);}
"aa=" {return symbol(sym.OP_EQ);}
"aa!" {return symbol(sym.OP_NEQ);}

"num"  {return symbol(sym.KW_NUM);}
"char" {return symbol(sym.KW_CHAR);}

"@b"    {return symbol(sym.AT_B);}
"@f"    {return symbol(sym.AT_F);}
"@800@" {return symbol(sym.AT_800);}
"@e"    {return symbol(sym.AT_E);}
"@i"    {return symbol(sym.AT_I);}

"(" {return symbol(sym.LPAREN);}
")" {return symbol(sym.RPAREN);}
"{" {return symbol(sym.LBRACE);}
"}" {return symbol(sym.RBRACE);}
"*" {return symbol(sym.TIMES);}
"+" {return symbol(sym.PLUS);}
"=" {return symbol(sym.ASSIGN);}
"." {return symbol(sym.DOT);}
"," {return symbol(sym.COMMA);}

{WhiteSpace} { /* ignore white space. */ }
. { System.err.println("Illegal Character: "+yytext()+" at line "+yyline); }