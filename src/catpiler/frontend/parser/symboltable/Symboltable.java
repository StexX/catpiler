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
import java.util.HashSet;
import java.util.Set;

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
	 * the global table that is 
	 * always accessible
	 */
	private Symboltable globalTable;
	
	/**
	 * the outter table that is accessible
	 * from this scope
	 */
	private Symboltable outterTable;
	
	/**
	 * The hashEntry containing a set of identifier names and
	 * their symboltableEntries
	 */
	private HashMap<String,SymboltableEntry> hashEntry;
	
	/**
	 * The type containing a set of type names and
	 * their typeItems
	 */
	private HashMap<String,TypeItem> types;
	
	private String name;
	
	/**
	 * The constructor for the Symboltable
	 * @param name
	 * @param outterTable
	 */
	public Symboltable(String name, Symboltable outterTable) {
		this.outterTable = outterTable;
		if(outterTable != null) {
			this.globalTable = outterTable.getGlobalTable();
		} else {
			this.globalTable = this;
		}
		
		this.hashEntry = new HashMap<String, SymboltableEntry>();
		this.types = new HashMap<String, TypeItem>();
		this.name = name;
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
	 * The put method inserts a new entry into
	 * the hashEntry map.
	 * @param id
	 * @throws TypeCheckingException
	 */
	public void put(TypeItem id) {
		System.out.println(
				"Put type " + id.getName() + 
				" into symbolTable");
		types.put(id.getName(), id);
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
		} else if(!globalTable.equals(this)) {
			return globalTable.exists(varName);
		} else {
			return false;
		}
	}
	
	/**
	 * This method checks whether a
	 * variable exists already in this scope
	 * @param varName
	 * @return
	 */
	public boolean existsType(String typeName) {
		if(types.containsKey(typeName))
			return true;
		else if(outterTable != null){
			return outterTable.existsType(typeName);
		} else if(!globalTable.equals(this)) {
			return globalTable.existsType(typeName);
		} else {
			return false;
		}
	}
	
	/**
	 * This method checks whether a
	 * variable exists already in this scope
	 * @param varName
	 * @return
	 */
	public boolean isInScopeLevel(String varName) {
		if(hashEntry.containsKey(varName))
			return true;
		else 
			return false;
	}
	
	/**
	 * This method checks whether a
	 * variable exists already in this scope
	 * @param varName
	 * @return
	 */
	public Set<String> getRegInScopeLevel() {
		Set<String> registers = new HashSet<String>();
		for(String s : hashEntry.keySet()) {
			if(hashEntry.get(s).getCategory().equals("reg")) {
				registers.add(hashEntry.get(s).getReg());
			}
		}
		return registers;
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
		} else if(!globalTable.equals(this)) {
			return globalTable.get(varName);
		} else {
			return null;
		}
	}
	
	/**
	 * Returns the identifier object belonging to
	 * a particular variable name
	 * 
	 * @param varName
	 * @return
	 */
	public TypeItem getType(String varName) {
		if(types.containsKey(varName))
			return types.get(varName);
		else if(outterTable != null) {
			return outterTable.getType(varName);
		} else if(!globalTable.equals(this)) {
			return globalTable.getType(varName);
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

	public Symboltable getGlobalTable() {
		return globalTable;
	}

	public void setGlobalTable(Symboltable globalTable) {
		this.globalTable = globalTable;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public HashMap<String, SymboltableEntry> getHashEntry() {
		return hashEntry;
	}
	
}
