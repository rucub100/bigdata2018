/**
 * 
 */
package de.hhu.rucub100.bigdata2018.source.data;

import java.util.Date;

/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Sep 19, 2018
 * Current weather data object.
 */
public class CurrentWeather {
	
	private long id;
	private String name;
	private long dt;
	private String base;
	private int visibility;
	private int cod;
	private Sys sys;
	private Main main;
	private Coord coord;
	private Weather[] weather;
	private Wind wind;
	private Rain rain;
	private Clouds clouds;
	private Snow snow;
	
	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
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
	 * @return the data receiving time (UNIX UTC)
	 */
	public long getDt() {
		return dt;
	}
	
	public Date getDate() {
		return new Date(dt * 1000L);
	}
	
	/**
	 * @param dt the data receiving time to set
	 */
	public void setDt(long dt) {
		this.dt = dt;
	}
	
	/**
	 * @return the base
	 */
	public String getBase() {
		return base;
	}
	
	/**
	 * @param base the base to set
	 */
	public void setBase(String base) {
		this.base = base;
	}
	
	/**
	 * @return the visibility
	 */
	public int getVisibility() {
		return visibility;
	}
	
	/**
	 * @param visibility the visibility to set
	 */
	public void setVisibility(int visibility) {
		this.visibility = visibility;
	}
	
	/**
	 * @return the code
	 */
	public int getCod() {
		return cod;
	}
	
	/**
	 * @param cod the code to set
	 */
	public void setCod(int cod) {
		this.cod = cod;
	}
	
	/**
	 * @return the system information
	 */
	public Sys getSys() {
		return sys;
	}
	
	/**
	 * @param sys the system information to set
	 */
	public void setSys(Sys sys) {
		this.sys = sys;
	}
	
	/**
	 * @return the main
	 */
	public Main getMain() {
		return main;
	}
	
	/**
	 * @param main the main to set
	 */
	public void setMain(Main main) {
		this.main = main;
	}
	
	/**
	 * @return the coord
	 */
	public Coord getCoord() {
		return coord;
	}
	
	/**
	 * @param coord the coord to set
	 */
	public void setCoord(Coord coord) {
		this.coord = coord;
	}
	
	/**
	 * @return the weather
	 */
	public Weather[] getWeather() {
		return weather;
	}
	
	/**
	 * @param weather the weather to set
	 */
	public void setWeather(Weather[] weather) {
		this.weather = weather;
	}
	
	/**
	 * @return the wind
	 */
	public Wind getWind() {
		return wind;
	}
	
	/**
	 * @param wind the wind to set
	 */
	public void setWind(Wind wind) {
		this.wind = wind;
	}
	
	/**
	 * @return the rain
	 */
	public Rain getRain() {
		return rain;
	}
	
	/**
	 * @param rain the rain to set
	 */
	public void setRain(Rain rain) {
		this.rain = rain;
	}
	
	/**
	 * @return the clouds
	 */
	public Clouds getClouds() {
		return clouds;
	}
	
	/**
	 * @param clouds the clouds to set
	 */
	public void setClouds(Clouds clouds) {
		this.clouds = clouds;
	}
	
	/**
	 * @return the snow
	 */
	public Snow getSnow() {
		return snow;
	}
	
	/**
	 * @param snow the snow to set
	 */
	public void setSnow(Snow snow) {
		this.snow = snow;
	}
}
