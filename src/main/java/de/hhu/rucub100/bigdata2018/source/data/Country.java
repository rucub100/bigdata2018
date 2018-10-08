/**
 * 
 */
package de.hhu.rucub100.bigdata2018.source.data;

import java.io.Serializable;

/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Sep 21, 2018
 *
 * A serializable country POJO.
 */
public class Country implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String[] neighbors;
	private City[] list;
	
	/**
	 * @return the neighbors
	 */
	public String[] getNeighbors() {
		return neighbors;
	}
	
	/**
	 * @param neighbors the neighbors to set
	 */
	public void setNeighbors(String[] neighbors) {
		this.neighbors = neighbors;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the list
	 */
	public City[] getList() {
		return list;
	}
	/**
	 * @param list the list to set
	 */
	public void setList(City[] list) {
		this.list = list;
	}
}
