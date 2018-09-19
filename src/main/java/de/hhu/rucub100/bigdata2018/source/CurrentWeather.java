/**
 * 
 */
package de.hhu.rucub100.bigdata2018.source;

import com.google.gson.annotations.SerializedName;

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

	private class Coord {
		private float lat;
		private float lon;		
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
	public int getCod() {
		return cod;
	}
	
	/* coord properties */
	
	/**
	 * @return the geo location, longitude
	 */
	public float getLongitude() {
		return coord == null ? Float.NaN : coord.lon;
	}
	
	/**
	 * @return the geo location, latitude
	 */
	public float getLatitude() {
		return coord == null ? Float.NaN : coord.lat;
	}
	
	/* main properties */
	
	public float getTemperature() {
		return main == null ? Float.NaN : main.temp;
	}
	
	public float getTemperatureMin() {
		return main == null ? Float.NaN : main.temp_min;
	}
	
	public float getTemperatureMax() {
		return main == null ? Float.NaN : main.temp_max;
	}
	
	public float getHumidity() {
		return main == null ? Float.NaN : main.humidity;
	}
	
	public float getPressure() {
		return main == null ? Float.NaN : main.pressure;
	}
	
	public float getPressureSeaLevel() {
		return main == null ? Float.NaN : main.sea_level;
	}
	
	public float getPressureGroundLevel() {
		return main == null ? Float.NaN : main.grnd_level;
	}
	
	/* weather properties */
	
	public int getWeatherLength() {
		return weather == null ? 0: weather.length;
	}
	
	public int getWeatherId(int index) {
		return (weather == null || index < 0 || weather.length >= index) ? 
				0 : weather[index].id;
	}
	
	public String getWeatherMain(int index) {
		return (weather == null || index < 0 || weather.length >= index) ? 
				"" : weather[index].main;
	}
	
	public String getWeatherDescription(int index) {
		return (weather == null || index < 0 || weather.length >= index) ? 
				"" : weather[index].description;
	}
	
	public String getWeatherIcon(int index) {
		return (weather == null || index < 0 || weather.length >= index) ? 
				"" : weather[index].icon;
	}
	
	/* wind properties */
	
	public float getWindSpeed() {
		return wind == null ? Float.NaN : wind.speed;
	}
	
	public float getWindGust() {
		return wind == null ? Float.NaN : wind.gust;
	}
	
	public float getWindDirection() {
		return wind == null ? Float.NaN : wind.deg;
	}
	
	/* clouds properties */
	
	public float getCloudiness() {
		return clouds == null ? Float.NaN : clouds.all;
	}
	
	/* rain properties */
	
	public float getRainVolume() {
		return rain == null ? 0 : rain._3h;
	}
	
	/* snow properties */
	
	public float getSnowVolume() {
		return snow == null ? 0 : snow._3h;
	}
	
	/* sys properties */
	
	public int getSystemId() {
		return sys == null ? 0 : sys.id;
	}
	
	public float getSystemMessage() {
		return sys == null ? 0 : sys.message;
	}
	
	public int getSystemType() {
		return sys == null ? 0 : sys.type;
	}
	
	public long getSunriseType() {
		return sys == null ? 0 : sys.sunrise;
	}
	
	public long getSunsetType() {
		return sys == null ? 0 : sys.sunset;
	}
	
	public String getSystemCountry() {
		return sys == null ? ""  : sys.country;
	}
	
	/* private classes */

	private class Main {
		private float temp;
		private float pressure;
		private float humidity;
		private float temp_min;
		private float temp_max;
		private float sea_level;
		private float grnd_level;
	}
	
	private class Wind {
		private float speed;
		private float deg;
		private float gust;
	}
	
	private class Weather {
		private int id;
	    private String main;
	    private String description;
	    private String icon;
	}
	
	private class Clouds {
		private float all;
	}
	
	private class Sys {
		private int type;
	    private int id;
	    private float message;
	    private String country;
	    private long sunrise;
	    private long sunset;
	}
	
	private class Rain {
		@SerializedName("3h")
		private float _3h;
	}
	
	private class Snow {
		@SerializedName("3h")
		private float _3h;
	}
}
