/**
 * 
 */
package de.hhu.rucub100.bigdata2018.source.data;

/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Sep 19, 2018
 * Current weather data object.
 */
public class CurrentWeather {
	
	private final long id;
	private final String name;
	private final long dt;
	private final String base;
	private final int visibility;
	private final int cod;
	private final Sys sys;
	private final Main main;
	private final Coord coord;
	private final Weather[] weather;
	private final Wind wind;
	private final Rain rain;
	private final Clouds clouds;
	private final Snow snow;
	
	public CurrentWeather(long id, String name, long dt, String base, int visibility, 
			int cod, Sys sys, Main main, Coord coord, Weather[] weather, 
			Wind wind, Rain rain, Clouds clouds, Snow snow) {
		this.id = id;
		this.name = name;
		this.dt = dt;
		this.base = base;
		this.visibility = visibility;
		this.cod = cod;
		this.sys = sys;
		this.main = main;
		this.coord = coord;
		this.weather = weather;
		this.wind = wind;
		this.rain = rain;
		this.clouds = clouds;
		this.snow = snow;
	}
	
	/**
	 * @return the city id
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * @return the city name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return the data receiving time (UNIX, UTC)
	 */
	public long getDataReceivingTime() {
		return dt;
	}
	
	/**
	 * @return the base (internal parameter)
	 */
	public String getBase() {
		return base;
	}
	
	/**
	 * @return the visibility
	 */
	public int getVisibility() {
		return visibility;
	}
	
	/**
	 * @return the cod (internal parameter)
	 */
	public int getCode() {
		return cod;
	}

	public Sys getSystemInfo() {
		return sys;
	}

	public Main getMain() {
		return main;
	}

	public Coord getCoord() {
		return coord;
	}

	public Weather[] getWeather() {
		return weather;
	}

	public Wind getWind() {
		return wind;
	}

	public Rain getRain() {
		return rain;
	}

	public Clouds getClouds() {
		return clouds;
	}

	public Snow getSnow() {
		return snow;
	}
}
