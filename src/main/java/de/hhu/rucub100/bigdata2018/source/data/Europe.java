/**
 * 
 */
package de.hhu.rucub100.bigdata2018.source.data;

/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Sep 21, 2018
 *
 * Europe.
 */
public class Europe {
	private final Country[] countries;

	public Europe(Country[] countries) {
		this.countries = countries;
	}

	/**
	 * @return the countries
	 */
	public Country[] getCountries() {
		return countries;
	}
}
