package de.hhu.rucub100.bigdata2018.source.data;

/**
 * 
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Sep 19, 2018
 *
 */
public class Sys {
	
	private final int type;
	private final int id;
	private final float message;
	private final String country;
	private final long sunrise;
	private final long sunset;
	
	public Sys(int type, int id, float message, String country, long sunrise, long sunset) {
		this.type = type;
		this.id = id;
		this.message = message;
		this.country = country;
		this.sunrise = sunrise;
		this.sunset = sunset;
	}

	public int getType() {
		return type;
	}

	public int getId() {
		return id;
	}

	public float getMessage() {
		return message;
	}

	public String getCountry() {
		return country;
	}

	public long getSunrise() {
		return sunrise;
	}

	public long getSunset() {
		return sunset;
	}
}