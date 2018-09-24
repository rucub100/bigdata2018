/**
 * 
 */
package de.hhu.rucub100.bigdata2018.source.data;

/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Sep 21, 2018
 *
 */
public class Country {
	
	private final String name;
	private final City[] list;
	
	public Country(String name, City[] list) {
		this.name = name;
		this.list = list;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	public String getCode() {
		return (list == null || list.length == 0) ? "" : list[0].getCountry();
	}
	
	/**
	 * @return the list
	 */
	public City[] getList() {
		return list;
	}
}
