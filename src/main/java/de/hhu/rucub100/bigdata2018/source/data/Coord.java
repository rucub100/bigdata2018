package de.hhu.rucub100.bigdata2018.source.data;

import java.io.Serializable;

/**
 * 
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Sep 19, 2018
 *
 */
public class Coord implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private float lat;
	private float lon;
	
	/**
	 * @return the lat
	 */
	public float getLat() {
		return lat;
	}
	/**
	 * @param lat the lat to set
	 */
	public void setLat(float lat) {
		this.lat = lat;
	}
	/**
	 * @return the lon
	 */
	public float getLon() {
		return lon;
	}
	/**
	 * @param lon the lon to set
	 */
	public void setLon(float lon) {
		this.lon = lon;
	}
}