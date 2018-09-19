/**
 * 
 */
package de.hhu.rucub100.bigdata2018.source;

/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Sep 18, 2018
 *
 */
public enum Mode {
	/**
	 * JSON format is used by default
	 */
	JSON(""),
	XML("xml"),
	HTML("html");
	
	private String value;
	
	Mode(String val) {
		value = val;
	}
	
	@Override
	public String toString() {
		return value;
	}
}
