/**
 * 
 */
package de.hhu.rucub100.bigdata2018.source.data;

import java.io.Serializable;

/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Sep 20, 2018
 *
 */
public class ForecastItem implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private long dt;
	private String dt_txt;
	private Main main;
	private Weather[] weather;
	private Clouds clouds;
	private Wind wind;
	private Rain rain;
	private Snow snow;
	private Sys sys;
	
	/**
	 * @return the dt
	 */
	public long getDt() {
		return dt;
	}
	
	/**
	 * @return the dt_txt
	 */
	public String getDt_txt() {
		return dt_txt;
	}
	
	/**
	 * @return the main
	 */
	public Main getMain() {
		return main;
	}
	
	/**
	 * @return the weather
	 */
	public Weather[] getWeather() {
		return weather;
	}
	
	/**
	 * @return the clouds
	 */
	public Clouds getClouds() {
		return clouds;
	}
	
	/**
	 * @return the wind
	 */
	public Wind getWind() {
		return wind;
	}
	
	/**
	 * @return the rain
	 */
	public Rain getRain() {
		return rain;
	}
	
	/**
	 * @return the snow
	 */
	public Snow getSnow() {
		return snow;
	}
	
	/**
	 * @return the sys
	 */
	public Sys getSys() {
		return sys;
	}
}
