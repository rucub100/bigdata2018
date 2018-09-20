/**
 * 
 */
package de.hhu.rucub100.bigdata2018.source.data;

/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Sep 20, 2018
 *
 */
public class ForecastItem {
	
	private final long dt;
	private final String dt_txt;
	private final Main main;
	private final Weather[] weather;
	private final Clouds clouds;
	private final Wind wind;
	private final Rain rain;
	private final Snow snow;
	private final Sys sys;
	
	public ForecastItem(long dt, String dt_txt, Main main, Weather[] weather, 
			Clouds clouds, Wind wind, Rain rain, Snow snow, Sys sys) {
		this.dt = dt;
		this.dt_txt = dt_txt;
		this.main = main;
		this.weather = weather;
		this.clouds = clouds;
		this.wind = wind;
		this.rain = rain;
		this.snow = snow;
		this.sys = sys;
	}

	/**
	 * @return the dt
	 */
	public long getDataTime() {
		return dt;
	}

	/**
	 * @return the dt_txt
	 */
	public String getDataTimeTxt() {
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
	public Sys getSystemInfo() {
		return sys;
	}
}
