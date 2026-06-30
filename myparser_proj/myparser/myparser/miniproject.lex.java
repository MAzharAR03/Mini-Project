package myparser;
import java_cup.runtime.*;
import java.io.*;


class Yylex implements java_cup.runtime.Scanner {
	private final int YY_BUFFER_SIZE = 512;
	private final int YY_F = -1;
	private final int YY_NO_STATE = -1;
	private final int YY_NOT_ACCEPT = 0;
	private final int YY_START = 1;
	private final int YY_END = 2;
	private final int YY_NO_ANCHOR = 4;
	private final int YY_BOL = 128;
	private final int YY_EOF = 129;

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
	private java.io.BufferedReader yy_reader;
	private int yy_buffer_index;
	private int yy_buffer_read;
	private int yy_buffer_start;
	private int yy_buffer_end;
	private char yy_buffer[];
	private int yyline;
	private boolean yy_at_bol;
	private int yy_lexical_state;

	Yylex (java.io.Reader reader) {
		this ();
		if (null == reader) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(reader);
	}

	Yylex (java.io.InputStream instream) {
		this ();
		if (null == instream) {
			throw (new Error("Error: Bad input stream initializer."));
		}
		yy_reader = new java.io.BufferedReader(new java.io.InputStreamReader(instream));
	}

	private Yylex () {
		yy_buffer = new char[YY_BUFFER_SIZE];
		yy_buffer_read = 0;
		yy_buffer_index = 0;
		yy_buffer_start = 0;
		yy_buffer_end = 0;
		yyline = 0;
		yy_at_bol = true;
		yy_lexical_state = YYINITIAL;
	}

	private boolean yy_eof_done = false;
	private final int YYINITIAL = 0;
	private final int yy_state_dtrans[] = {
		0
	};
	private void yybegin (int state) {
		yy_lexical_state = state;
	}
	private int yy_advance ()
		throws java.io.IOException {
		int next_read;
		int i;
		int j;

		if (yy_buffer_index < yy_buffer_read) {
			return yy_buffer[yy_buffer_index++];
		}

		if (0 != yy_buffer_start) {
			i = yy_buffer_start;
			j = 0;
			while (i < yy_buffer_read) {
				yy_buffer[j] = yy_buffer[i];
				++i;
				++j;
			}
			yy_buffer_end = yy_buffer_end - yy_buffer_start;
			yy_buffer_start = 0;
			yy_buffer_read = j;
			yy_buffer_index = j;
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}

		while (yy_buffer_index >= yy_buffer_read) {
			if (yy_buffer_index >= yy_buffer.length) {
				yy_buffer = yy_double(yy_buffer);
			}
			next_read = yy_reader.read(yy_buffer,
					yy_buffer_read,
					yy_buffer.length - yy_buffer_read);
			if (-1 == next_read) {
				return YY_EOF;
			}
			yy_buffer_read = yy_buffer_read + next_read;
		}
		return yy_buffer[yy_buffer_index++];
	}
	private void yy_move_end () {
		if (yy_buffer_end > yy_buffer_start &&
		    '\n' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
		if (yy_buffer_end > yy_buffer_start &&
		    '\r' == yy_buffer[yy_buffer_end-1])
			yy_buffer_end--;
	}
	private boolean yy_last_was_cr=false;
	private void yy_mark_start () {
		int i;
		for (i = yy_buffer_start; i < yy_buffer_index; ++i) {
			if ('\n' == yy_buffer[i] && !yy_last_was_cr) {
				++yyline;
			}
			if ('\r' == yy_buffer[i]) {
				++yyline;
				yy_last_was_cr=true;
			} else yy_last_was_cr=false;
		}
		yy_buffer_start = yy_buffer_index;
	}
	private void yy_mark_end () {
		yy_buffer_end = yy_buffer_index;
	}
	private void yy_to_mark () {
		yy_buffer_index = yy_buffer_end;
		yy_at_bol = (yy_buffer_end > yy_buffer_start) &&
		            ('\r' == yy_buffer[yy_buffer_end-1] ||
		             '\n' == yy_buffer[yy_buffer_end-1] ||
		             2028/*LS*/ == yy_buffer[yy_buffer_end-1] ||
		             2029/*PS*/ == yy_buffer[yy_buffer_end-1]);
	}
	private java.lang.String yytext () {
		return (new java.lang.String(yy_buffer,
			yy_buffer_start,
			yy_buffer_end - yy_buffer_start));
	}
	private int yylength () {
		return yy_buffer_end - yy_buffer_start;
	}
	private char[] yy_double (char buf[]) {
		int i;
		char newbuf[];
		newbuf = new char[2*buf.length];
		for (i = 0; i < buf.length; ++i) {
			newbuf[i] = buf[i];
		}
		return newbuf;
	}
	private final int YY_E_INTERNAL = 0;
	private final int YY_E_MATCH = 1;
	private java.lang.String yy_error_string[] = {
		"Error: Internal error.\n",
		"Error: Unmatched input.\n"
	};
	private void yy_error (int code,boolean fatal) {
		java.lang.System.out.print(yy_error_string[code]);
		java.lang.System.out.flush();
		if (fatal) {
			throw new Error("Fatal Error.\n");
		}
	}
	private int[][] unpackFromString(int size1, int size2, String st) {
		int colonIndex = -1;
		String lengthString;
		int sequenceLength = 0;
		int sequenceInteger = 0;

		int commaIndex;
		String workString;

		int res[][] = new int[size1][size2];
		for (int i= 0; i < size1; i++) {
			for (int j= 0; j < size2; j++) {
				if (sequenceLength != 0) {
					res[i][j] = sequenceInteger;
					sequenceLength--;
					continue;
				}
				commaIndex = st.indexOf(',');
				workString = (commaIndex==-1) ? st :
					st.substring(0, commaIndex);
				st = st.substring(commaIndex+1);
				colonIndex = workString.indexOf(':');
				if (colonIndex == -1) {
					res[i][j]=Integer.parseInt(workString);
					continue;
				}
				lengthString =
					workString.substring(colonIndex+1);
				sequenceLength=Integer.parseInt(lengthString);
				workString=workString.substring(0,colonIndex);
				sequenceInteger=Integer.parseInt(workString);
				res[i][j] = sequenceInteger;
				sequenceLength--;
			}
		}
		return res;
	}
	private int yy_acpt[] = {
		/* 0 */ YY_NOT_ACCEPT,
		/* 1 */ YY_NO_ANCHOR,
		/* 2 */ YY_NO_ANCHOR,
		/* 3 */ YY_NO_ANCHOR,
		/* 4 */ YY_NO_ANCHOR,
		/* 5 */ YY_NO_ANCHOR,
		/* 6 */ YY_NO_ANCHOR,
		/* 7 */ YY_NO_ANCHOR,
		/* 8 */ YY_NO_ANCHOR,
		/* 9 */ YY_NO_ANCHOR,
		/* 10 */ YY_NO_ANCHOR,
		/* 11 */ YY_NO_ANCHOR,
		/* 12 */ YY_NO_ANCHOR,
		/* 13 */ YY_NO_ANCHOR,
		/* 14 */ YY_NO_ANCHOR,
		/* 15 */ YY_NO_ANCHOR,
		/* 16 */ YY_NO_ANCHOR,
		/* 17 */ YY_NO_ANCHOR,
		/* 18 */ YY_NO_ANCHOR,
		/* 19 */ YY_NO_ANCHOR,
		/* 20 */ YY_NO_ANCHOR,
		/* 21 */ YY_NO_ANCHOR,
		/* 22 */ YY_NO_ANCHOR,
		/* 23 */ YY_NO_ANCHOR,
		/* 24 */ YY_NO_ANCHOR,
		/* 25 */ YY_NO_ANCHOR,
		/* 26 */ YY_NO_ANCHOR,
		/* 27 */ YY_NO_ANCHOR,
		/* 28 */ YY_NO_ANCHOR,
		/* 29 */ YY_NO_ANCHOR,
		/* 30 */ YY_NO_ANCHOR,
		/* 31 */ YY_NO_ANCHOR,
		/* 32 */ YY_NO_ANCHOR,
		/* 33 */ YY_NO_ANCHOR,
		/* 34 */ YY_NO_ANCHOR,
		/* 35 */ YY_NO_ANCHOR,
		/* 36 */ YY_NO_ANCHOR,
		/* 37 */ YY_NO_ANCHOR,
		/* 38 */ YY_NO_ANCHOR,
		/* 39 */ YY_NO_ANCHOR,
		/* 40 */ YY_NO_ANCHOR,
		/* 41 */ YY_NO_ANCHOR,
		/* 42 */ YY_NO_ANCHOR,
		/* 43 */ YY_NO_ANCHOR,
		/* 44 */ YY_NO_ANCHOR,
		/* 45 */ YY_NO_ANCHOR,
		/* 46 */ YY_NO_ANCHOR,
		/* 47 */ YY_NO_ANCHOR,
		/* 48 */ YY_NO_ANCHOR,
		/* 49 */ YY_NO_ANCHOR,
		/* 50 */ YY_NO_ANCHOR,
		/* 51 */ YY_NO_ANCHOR,
		/* 52 */ YY_NO_ANCHOR,
		/* 53 */ YY_NO_ANCHOR,
		/* 54 */ YY_NO_ANCHOR,
		/* 55 */ YY_NO_ANCHOR,
		/* 56 */ YY_NO_ANCHOR,
		/* 57 */ YY_NO_ANCHOR,
		/* 58 */ YY_NO_ANCHOR,
		/* 59 */ YY_NO_ANCHOR,
		/* 60 */ YY_NO_ANCHOR,
		/* 61 */ YY_NO_ANCHOR,
		/* 62 */ YY_NO_ANCHOR,
		/* 63 */ YY_NO_ANCHOR,
		/* 64 */ YY_NO_ANCHOR,
		/* 65 */ YY_NO_ANCHOR,
		/* 66 */ YY_NO_ANCHOR,
		/* 67 */ YY_NO_ANCHOR,
		/* 68 */ YY_NO_ANCHOR,
		/* 69 */ YY_NO_ANCHOR,
		/* 70 */ YY_NO_ANCHOR,
		/* 71 */ YY_NO_ANCHOR,
		/* 72 */ YY_NO_ANCHOR,
		/* 73 */ YY_NO_ANCHOR
	};
	private int yy_cmap[] = unpackFromString(1,130,
"33:9,32:2,33,32:2,33:18,32,20,33:2,3,33:3,24,25,29,26,21,27,33,28,2:10,33:2" +
",31,19,30,33:2,1:26,33:6,5,1:2,9,7,17,14,1,8,1:2,16,6,4,18,15,1,13,11,12,10" +
",1:5,22,33,23,33:2,0:2")[0];

	private int yy_rmap[] = unpackFromString(1,74,
"0,1,2,3,1,4,1:12,5:3,1:4,6:10,7,8,9,5,10,6,11,12,13,14,15,16,17,18,19,20,21" +
",22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43")[0];

	private int yy_nxt[][] = unpackFromString(44,34,
"1,2,3,4,50,2:2,60,35,2,68,72,37,73,2,70,2,63,2,5,36,6,7,8,9,10,11,12,13,14," +
"15,16,17,4,-1:35,38,-1:2,38:15,-1:17,3,-1:50,21,-1:15,40,23,24,40:15,-1:16," +
"40,-1:2,40:15,-1:16,38,-1:2,61,38:4,18,38:7,19,38,-1:34,22,-1:15,38,-1:2,38" +
":4,66,38:9,20,-1:16,40,23,24,40:2,25,40:12,-1:16,40,-1:2,40:3,26,40:11,-1:1" +
"6,40,-1:2,40:3,27,40:11,-1:16,40,-1:2,40:2,28,40:12,-1:16,40,-1:2,40:8,29,4" +
"0:6,-1:16,40,-1:2,40:12,30,40:2,-1:16,40,-1:2,40:7,31,40:7,-1:16,40,-1:2,40" +
":8,32,40:6,-1:16,40,-1:2,40:10,33,40:4,-1:16,40,-1:2,40:8,34,40:6,-1:16,38," +
"-1:2,38,51,38:4,39,38:8,-1:16,40,23,24,40:2,41,40:12,-1:16,40,-1:2,40:6,44," +
"40:8,-1:16,40,23,24,40:7,42,40:7,-1:16,40,-1:2,40:4,45,40:10,-1:16,40,23,24" +
",40:14,43,-1:16,40,-1:2,40:3,46,40:11,-1:16,40,-1:2,47,40:14,-1:16,40,-1:2," +
"48,40:14,-1:16,40,-1:2,40,49,40:13,-1:16,38,-1:2,38:12,53,38:2,-1:16,40,23," +
"24,40:11,52,40:3,-1:16,40,-1:2,40:4,58,40:10,-1:16,38,-1:2,38:9,55,38:5,-1:" +
"16,40,23,24,40:8,54,40:6,-1:16,40,-1:2,40:3,59,40:11,-1:16,40,23,24,40:2,56" +
",40:12,-1:16,40,23,24,40:4,57,40:10,-1:16,38,-1:2,64,38:14,-1:16,40,23,24,4" +
"0:9,62,40:5,-1:16,38,-1:2,38:9,67,38:5,-1:16,40,23,24,40:11,65,40:3,-1:16,3" +
"8,-1:2,38:8,69,38:6,-1:16,38,-1:2,38:3,71,38:11,-1:15");

	public java_cup.runtime.Symbol next_token ()
		throws java.io.IOException {
		int yy_lookahead;
		int yy_anchor = YY_NO_ANCHOR;
		int yy_state = yy_state_dtrans[yy_lexical_state];
		int yy_next_state = YY_NO_STATE;
		int yy_last_accept_state = YY_NO_STATE;
		boolean yy_initial = true;
		int yy_this_accept;

		yy_mark_start();
		yy_this_accept = yy_acpt[yy_state];
		if (YY_NOT_ACCEPT != yy_this_accept) {
			yy_last_accept_state = yy_state;
			yy_mark_end();
		}
		while (true) {
			if (yy_initial && yy_at_bol) yy_lookahead = YY_BOL;
			else yy_lookahead = yy_advance();
			yy_next_state = YY_F;
			yy_next_state = yy_nxt[yy_rmap[yy_state]][yy_cmap[yy_lookahead]];
			if (YY_EOF == yy_lookahead && true == yy_initial) {
				return null;
			}
			if (YY_F != yy_next_state) {
				yy_state = yy_next_state;
				yy_initial = false;
				yy_this_accept = yy_acpt[yy_state];
				if (YY_NOT_ACCEPT != yy_this_accept) {
					yy_last_accept_state = yy_state;
					yy_mark_end();
				}
			}
			else {
				if (YY_NO_STATE == yy_last_accept_state) {
					throw (new Error("Lexical Error: Unmatched Input."));
				}
				else {
					yy_anchor = yy_acpt[yy_last_accept_state];
					if (0 != (YY_END & yy_anchor)) {
						yy_move_end();
					}
					yy_to_mark();
					switch (yy_last_accept_state) {
					case 1:
						
					case -2:
						break;
					case 2:
						{ return symbol(sym.STRING_LITERAL, yytext()); }
					case -3:
						break;
					case 3:
						{ return symbol(sym.NUMBER, Integer.parseInt(yytext())); }
					case -4:
						break;
					case 4:
						{ System.err.println("Illegal Character: "+yytext()+" at line "+yyline); }
					case -5:
						break;
					case 5:
						{return symbol(sym.ASSIGN);}
					case -6:
						break;
					case 6:
						{return symbol(sym.COMMA);}
					case -7:
						break;
					case 7:
						{return symbol(sym.LBRACE);}
					case -8:
						break;
					case 8:
						{return symbol(sym.RBRACE);}
					case -9:
						break;
					case 9:
						{return symbol(sym.LPAREN);}
					case -10:
						break;
					case 10:
						{return symbol(sym.RPAREN);}
					case -11:
						break;
					case 11:
						{return symbol(sym.PLUS);}
					case -12:
						break;
					case 12:
						{return symbol(sym.MINUS);}
					case -13:
						break;
					case 13:
						{return symbol(sym.DIVIDE);}
					case -14:
						break;
					case 14:
						{return symbol(sym.MULTIPLY);}
					case -15:
						break;
					case 15:
						{return symbol(sym.GREATER_THAN);}
					case -16:
						break;
					case 16:
						{return symbol(sym.LESS_THAN);}
					case -17:
						break;
					case 17:
						{ /* ignore white space. */ }
					case -18:
						break;
					case 18:
						{return symbol(sym.ID);}
					case -19:
						break;
					case 19:
						{return symbol(sym.IF_TOKEN);}
					case -20:
						break;
					case 20:
						{return symbol(sym.TO);}
					case -21:
						break;
					case 21:
						{return symbol(sym.EQUALS);}
					case -22:
						break;
					case 22:
						{return symbol(sym.NOT_EQUALS);}
					case -23:
						break;
					case 23:
						{ return symbol(sym.NUMVAL, yytext()); }
					case -24:
						break;
					case 24:
						{ return symbol(sym.TEXTVAL, yytext()); }
					case -25:
						break;
					case 25:
						{return symbol(sym.NUM);}
					case -26:
						break;
					case 26:
						{return symbol(sym.NAME);}
					case -27:
						break;
					case 27:
						{return symbol(sym.ELSE_TOKEN);}
					case -28:
						break;
					case 28:
						{return symbol(sym.FROM);}
					case -29:
						break;
					case 29:
						{return symbol(sym.INPUT_TOKEN);}
					case -30:
						break;
					case 30:
						{return symbol(sym.UNTIL);}
					case -31:
						break;
					case 31:
						{return symbol(sym.TIMES);}
					case -32:
						break;
					case 32:
						{return symbol(sym.PRINT_TOKEN);}
					case -33:
						break;
					case 33:
						{return symbol(sym.STRING);}
					case -34:
						break;
					case 34:
						{return symbol(sym.REPEAT);}
					case -35:
						break;
					case 35:
						{ return symbol(sym.STRING_LITERAL, yytext()); }
					case -36:
						break;
					case 36:
						{ System.err.println("Illegal Character: "+yytext()+" at line "+yyline); }
					case -37:
						break;
					case 37:
						{ return symbol(sym.STRING_LITERAL, yytext()); }
					case -38:
						break;
					case 38:
						{ return symbol(sym.STRING_LITERAL, yytext()); }
					case -39:
						break;
					case 39:
						{ return symbol(sym.STRING_LITERAL, yytext()); }
					case -40:
						break;
					case 40:
						{ return symbol(sym.STRING_LITERAL, yytext()); }
					case -41:
						break;
					case 41:
						{ return symbol(sym.STRING_LITERAL, yytext()); }
					case -42:
						break;
					case 42:
						{ return symbol(sym.STRING_LITERAL, yytext()); }
					case -43:
						break;
					case 43:
						{ return symbol(sym.STRING_LITERAL, yytext()); }
					case -44:
						break;
					case 44:
						{ return symbol(sym.STRING_LITERAL, yytext()); }
					case -45:
						break;
					case 45:
						{ return symbol(sym.STRING_LITERAL, yytext()); }
					case -46:
						break;
					case 46:
						{ return symbol(sym.STRING_LITERAL, yytext()); }
					case -47:
						break;
					case 47:
						{ return symbol(sym.STRING_LITERAL, yytext()); }
					case -48:
						break;
					case 48:
						{ return symbol(sym.STRING_LITERAL, yytext()); }
					case -49:
						break;
					case 49:
						{ return symbol(sym.STRING_LITERAL, yytext()); }
					case -50:
						break;
					case 50:
						{ return symbol(sym.STRING_LITERAL, yytext()); }
					case -51:
						break;
					case 51:
						{ return symbol(sym.STRING_LITERAL, yytext()); }
					case -52:
						break;
					case 52:
						{ return symbol(sym.STRING_LITERAL, yytext()); }
					case -53:
						break;
					case 53:
						{ return symbol(sym.STRING_LITERAL, yytext()); }
					case -54:
						break;
					case 54:
						{ return symbol(sym.STRING_LITERAL, yytext()); }
					case -55:
						break;
					case 55:
						{ return symbol(sym.STRING_LITERAL, yytext()); }
					case -56:
						break;
					case 56:
						{ return symbol(sym.STRING_LITERAL, yytext()); }
					case -57:
						break;
					case 57:
						{ return symbol(sym.STRING_LITERAL, yytext()); }
					case -58:
						break;
					case 58:
						{ return symbol(sym.STRING_LITERAL, yytext()); }
					case -59:
						break;
					case 59:
						{ return symbol(sym.STRING_LITERAL, yytext()); }
					case -60:
						break;
					case 60:
						{ return symbol(sym.STRING_LITERAL, yytext()); }
					case -61:
						break;
					case 61:
						{ return symbol(sym.STRING_LITERAL, yytext()); }
					case -62:
						break;
					case 62:
						{ return symbol(sym.STRING_LITERAL, yytext()); }
					case -63:
						break;
					case 63:
						{ return symbol(sym.STRING_LITERAL, yytext()); }
					case -64:
						break;
					case 64:
						{ return symbol(sym.STRING_LITERAL, yytext()); }
					case -65:
						break;
					case 65:
						{ return symbol(sym.STRING_LITERAL, yytext()); }
					case -66:
						break;
					case 66:
						{ return symbol(sym.STRING_LITERAL, yytext()); }
					case -67:
						break;
					case 67:
						{ return symbol(sym.STRING_LITERAL, yytext()); }
					case -68:
						break;
					case 68:
						{ return symbol(sym.STRING_LITERAL, yytext()); }
					case -69:
						break;
					case 69:
						{ return symbol(sym.STRING_LITERAL, yytext()); }
					case -70:
						break;
					case 70:
						{ return symbol(sym.STRING_LITERAL, yytext()); }
					case -71:
						break;
					case 71:
						{ return symbol(sym.STRING_LITERAL, yytext()); }
					case -72:
						break;
					case 72:
						{ return symbol(sym.STRING_LITERAL, yytext()); }
					case -73:
						break;
					case 73:
						{ return symbol(sym.STRING_LITERAL, yytext()); }
					case -74:
						break;
					default:
						yy_error(YY_E_INTERNAL,false);
					case -1:
					}
					yy_initial = true;
					yy_state = yy_state_dtrans[yy_lexical_state];
					yy_next_state = YY_NO_STATE;
					yy_last_accept_state = YY_NO_STATE;
					yy_mark_start();
					yy_this_accept = yy_acpt[yy_state];
					if (YY_NOT_ACCEPT != yy_this_accept) {
						yy_last_accept_state = yy_state;
						yy_mark_end();
					}
				}
			}
		}
	}
}
