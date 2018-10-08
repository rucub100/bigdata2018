package de.hhu.rucub100.bigdata2018.source.data;

import java.io.Serializable;

/**
 * 
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Sep 19, 2018
 * 
 * Serializable system information.
 */
public class Sys implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int type;
	private int id;
	private float message;
	private String country;
	private long sunrise;
	private long sunset;
	
	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the message
	 */
	public float getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(float message) {
		this.message = message;
	}
	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}
	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}
	/**
	 * @return the sunrise
	 */
	public long getSunrise() {
		return sunrise;
	}
	/**
	 * @param sunrise the sunrise to set
	 */
	public void setSunrise(long sunrise) {
		this.sunrise = sunrise;
	}
	/**
	 * @return the sunset
	 */
	public long getSunset() {
		return sunset;
	}
	/**
	 * @param sunset the sunset to set
	 */
	public void setSunset(long sunset) {
		this.sunset = sunset;
	}
}