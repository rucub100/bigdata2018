/**
 * 
 */
package de.hhu.rucub100.bigdata2018.source.data;

/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Sep 19, 2018
 *
 */
public class City {
	
	private final int id;
	private final String name;
	private final String country;
	private final Coord coord;
	
	public City(int id, String name, String country, Coord coord) {
		this.id = id;
		this.name = name;
		this.country = country;
		this.coord = coord;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getCountry() {
		return country;
	}

	public Coord getCoord() {
		return coord;
	}
}
