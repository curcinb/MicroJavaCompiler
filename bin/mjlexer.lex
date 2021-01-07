package rs.ac.bg.etf.pp1;

import java_cup.runtime.Symbol;

%%

%{
	// ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type) {
		return new Symbol(type, yyline+1, yycolumn);
	}
	
	// ukljucivanje informacije o poziciji tokena
	private Symbol new_symbol(int type, Object value) {
		return new Symbol(type, yyline+1, yycolumn, value);
	}
%}

%cup
%line
%column

%xstate COMMENT

%eofval{
	return new_symbol(sym.EOF);
%eofval}

%%

" " 	{ }
"\b" 	{ }
"\t" 	{ }
"\r\n" 	{ }
"\f" 	{ }

//Kljucne reci:
"program"   { return new_symbol(sym.PROG, yytext());}
"const"		{ return new_symbol(sym.CONST, yytext()); }
"print" 	{ return new_symbol(sym.PRINT, yytext()); }
"read"		{ return new_symbol(sym.READ, yytext()); }
"void"		{ return new_symbol(sym.VOID, yytext()); }
"new"		{ return new_symbol(sym.NEW, yytext()); }

//Operatori:
"+" 		{ return new_symbol(sym.PLUS, yytext()); }
"-"			{ return new_symbol(sym.MINUS, yytext()); }
"*"			{ return new_symbol(sym.MULTIPLY, yytext()); }
"/"			{ return new_symbol(sym.DIVIDE, yytext()); }
"%"			{ return new_symbol(sym.MOD, yytext()); }
"=" 		{ return new_symbol(sym.ASSIGN, yytext()); }
";" 		{ return new_symbol(sym.SEMICOLON, yytext()); }
"," 		{ return new_symbol(sym.COMMA, yytext()); }
"++"		{ return new_symbol(sym.INCREMENT, yytext()); }
"--"		{ return new_symbol(sym.DECREMENT, yytext()); }



//Zagrade:
"(" 		{ return new_symbol(sym.LEFT_PAREN, yytext()); }
")" 		{ return new_symbol(sym.RIGHT_PAREN, yytext()); }
"{" 		{ return new_symbol(sym.LEFT_BRACE, yytext()); }
"}"			{ return new_symbol(sym.RIGHT_BRACE, yytext()); }
"["			{ return new_symbol(sym.LEFT_BRACKET, yytext()); }
"]"			{ return new_symbol(sym.RIGHT_BRACKET, yytext()); }

"#"			{ return new_symbol(sym.HASH, yytext()); }
"@"			{ return new_symbol(sym.AT, yytext()); }
"$" 		{ return new_symbol(sym.DOLAR, yytext()); }
"^"			{ return new_symbol(sym.KAPICA, yytext()); }
"|"			{ return new_symbol(sym.CRTA, yytext()); }
"::" 		{ return new_symbol(sym.DVOTACKA, yytext()); }

"?"			{ return new_symbol(sym.UPITNIK, yytext()); }
":"			{ return new_symbol(sym.DVOTACKA2, yytext()); }

//Relop:
"=="		{ return new_symbol(sym.JEDNAKO, yytext()); }
"!="		{ return new_symbol(sym.NEJEDNAKO, yytext()); }
">"			{ return new_symbol(sym.VECE, yytext()); }
">="		{ return new_symbol(sym.VECEJEDNAKO, yytext()); }
"<"			{ return new_symbol(sym.MANJE, yytext()); }
"<="		{ return new_symbol(sym.MANJEJEDNAKO, yytext()); }
	
//Komentari:
"//" {yybegin(COMMENT);}
<COMMENT> . {yybegin(COMMENT);}
<COMMENT> "\r\n" { yybegin(YYINITIAL); }

//Identifikatori
[0-9]+  { return new_symbol(sym.NUMBER, new Integer (yytext())); }
("true"|"false")  { return new_symbol(sym.BOOLCONST, new String(yytext())); }
"'"."'" {return new_symbol (sym.CHARCONST, new Character (yytext().charAt(1)));}
([a-z]|[A-Z])[a-z|A-Z|0-9|_]* 	{return new_symbol (sym.IDENT, yytext()); }


. { System.err.println("Leksicka greska ("+yytext()+") u liniji "+(yyline+1)); }


