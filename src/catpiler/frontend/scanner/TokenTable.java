/*
 * TokenTable.java Copyright (C) 2010 Stephanie Stroka 
 * 
 * This library is free software; you can redistribute it and/or modify it under 
 * the terms of the GNU Lesser General Public License as published by the 
 * Free Software Foundation; either version 2.1 of the License, or (at your option) 
 * any later version. 
 * 
 * This library is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for 
 * more details. 
 * 
 * You should have received a copy of the GNU Lesser General Public License along 
 * with this library; if not, write to the Free Software Foundation, Inc., 59 
 * Temple Place, Suite 330, Boston, MA 02111-1307 USA 
 */
package catpiler.frontend.scanner;

/**
 * Contains all tokens that the source code may contain.
 * 
 * @author Stephanie Stroka
 * 			(stephanie.stroka@sbg.ac.at)
 *
 */
public class TokenTable {
	
	// increase token count if new keyword tokens are defined
	// (doesn't include string, identifier and integer token)
	public static final int TOKEN_COUNT = 48;

	public static final Token comment = new Token("BTW");
	public static final Token file_begin = new Token("HAI");
	public static final Token file_end = new Token("KTHXBYE");
	public static final Token var_decl_1 = new Token("I");
	public static final Token var_decl_2 = new Token("HAS");
	public static final Token var_decl_3 = new Token("A");
	public static final Token var_assign = new Token("R");
	public static final Token type_string = new Token("YARN");
	public static final Token type_int = new Token("NUMBR");
	public static final Token type_bool = new Token("TROOF");
	public static final Token val_true = new Token("WIN");
	public static final Token val_false = new Token("FAIL");
	public static final Token char_lf = new Token(":)");
	public static final Token char_quote = new Token(":\"");
	public static final Token op_of = new Token("OF");
	public static final Token op_seq = new Token("AN");
	public static final Token op_sum = new Token("SUM");
	public static final Token op_diff = new Token("DIFF");
	public static final Token op_prod = new Token("PRODUKT");
	public static final Token op_quot = new Token("QUOSHUNT");
	public static final Token op_max = new Token("BIGGR");
	public static final Token op_min = new Token("SMALLR");
	public static final Token op_both = new Token("BOTH");
	public static final Token op_or = new Token("EITHER");
	public static final Token op_not = new Token("NOT");
	public static final Token op_all = new Token("ALL");
	public static final Token op_any = new Token("ANY");
	public static final Token op_end = new Token("MKAY");
	public static final Token op_eq = new Token("SAEM");
	public static final Token op_neq = new Token("DIFFRINT");
	public static final Token fc_loop_1 = new Token("IM");
	public static final Token fc_loop_2 = new Token("IN");
	public static final Token fc_loop_3 = new Token("YR");
	public static final Token fc_loop_end = new Token("OUTTA");
	public static final Token fc_if = new Token("ORLY?");
	public static final Token fc_then_1 = new Token("YA");
	public static final Token fc_then_2 = new Token("RLY");
	public static final Token fc_else_if = new Token("MEBBE");
	public static final Token fc_else_1 = new Token("NO");
	public static final Token fc_else_2 = new Token("WAI");
	public static final Token fc_if_end = new Token("OIC");
	public static final Token function_1 = new Token("HOW");
	public static final Token function_2 = new Token("DUZ");
	public static final Token function_end_1 = new Token("IF");
	public static final Token function_end_2 = new Token("YOU");
	public static final Token function_end_3 = new Token("SAY");
	public static final Token function_end_4 = new Token("SO");
	public static final Token whitespace = new Token(" ");
	
	// not to be compared with the input...
	public static final Token id = new Token("identifier");
	public static final Token string = new Token("string");
	public static final Token integer = new Token("int");
}
