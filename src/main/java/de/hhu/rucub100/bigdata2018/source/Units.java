/**
 * 
 */
package de.hhu.rucub100.bigdata2018.source;

/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Sep 18, 2018
 * 
 * The units for the API response.
 */
public enum Units {
	/**
	 * Temperature in Kelvin is used by default
	 */
	DEFAULT(""), 
	/**
	 * For temperature in Celsius
	 */
	METRIC("metric"), 
	/**
	 * For temperature in Fahrenheit
	 */
	IMPERIAL("imperial");
	
	private String value;
	
	Units(String val) {
		value = val;
	}
	
	@Override
	public String toString() {
		return value;
	}
}
