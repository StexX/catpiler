/*
 * Symboltable.java Copyright (C) 2010 Stephanie Stroka 
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
package catpiler.frontend.parser.symboltable;

import java.util.HashMap;

import catpiler.frontend.exception.TypeCheckingException;

/**
 * This class represents a symbol table for a particular code block,
 * containing all identifiers and their attributes, types and 
 * the outter symbol table that are accessible in this particular scope.
 * 
 * @author Stephanie Stroka
 * 			(stephanie.stroka@sbg.ac.at)
 *
 */
public class Symboltable {

	/**
	 * the outter table that is accessible
	 * from this scope
	 */
	private Symboltable outterTable;
	
	/**
	 * (generated) name for symbol table
	 */
//	private String name;
	
	/**
	 * The hashEntry containing a set of identifier names and
	 * their identifier objects
	 */
	private HashMap<String,SymboltableEntry> hashEntry;
	
	/**
	 * The constructor for the Symboltable
	 * @param name
	 * @param outterTable
	 */
	public Symboltable(Symboltable outterTable) {
//		this.name = name;
		this.outterTable = outterTable;
		
		this.hashEntry = new HashMap<String, SymboltableEntry>();
	}

	/**
	 * The put method inserts a new entry into
	 * the hashEntry map.
	 * @param id
	 * @throws TypeCheckingException
	 */
	public void put(SymboltableEntry id) {
		System.out.println(
				"Put " + id.getName() + 
				" : " + id.getAttribute() + 
				" into symbolTable");
		hashEntry.put(id.getName(), id);
	}

	/**
	 * This method checks whether a
	 * variable exists already in this scope
	 * @param varName
	 * @return
	 */
	public boolean exists(String varName) {
		if(hashEntry.containsKey(varName))
			return true;
		else if(outterTable != null){
			return outterTable.exists(varName);
		} else {
			return false;
		}
	}
	
	/**
	 * Returns the identifier object belonging to
	 * a particular variable name
	 * 
	 * @param varName
	 * @return
	 */
	public SymboltableEntry get(String varName) {
		if(hashEntry.containsKey(varName))
			return hashEntry.get(varName);
		else if(outterTable != null) {
			return outterTable.get(varName);
		} else {
			return null;
		}
	}
	
	/**
	 * Returns the outter symbol table
	 * @return
	 */
	public Symboltable getOutterTable() {
		return outterTable;
	}

	/**
	 * Sets the outter symbol table
	 * @param outterTable
	 */
	public void setOutterTable(Symboltable outterTable) {
		this.outterTable = outterTable;
	}

//	/**
//	 * Returns the symbol table name
//	 * @return
//	 */
//	public String getName() {
//		return name;
//	}
//
//	/**
//	 * Sets the symbol table name
//	 * @param name
//	 */
//	public void setName(String name) {
//		this.name = name;
//	}
}
