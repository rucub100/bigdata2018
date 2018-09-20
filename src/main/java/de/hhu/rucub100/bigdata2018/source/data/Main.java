package de.hhu.rucub100.bigdata2018.source.data;

/**
 * 
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Sep 19, 2018
 *
 */
public class Main {
	
	private final float temp;
	private final float pressure;
	private final float humidity;
	private final float temp_min;
	private final float temp_max;
	private final float sea_level;
	private final float grnd_level;
	
	public Main(float temp, float pressure, float humidity, float temp_min, 
			float temp_max, float sea_level, float grnd_level) {
		this.temp = temp;
		this.pressure = pressure;
		this.humidity = humidity;
		this.temp_min = temp_min;
		this.temp_max = temp_max;
		this.sea_level = sea_level;
		this.grnd_level = grnd_level;
	}

	public float getTemperature() {
		return temp;
	}

	public float getPressure() {
		return pressure;
	}

	public float getHumidity() {
		return humidity;
	}

	public float getTemperatureMin() {
		return temp_min;
	}

	public float getTemperatureMax() {
		return temp_max;
	}

	public float getPressureSeaLevel() {
		return sea_level;
	}

	public float getPressureGroundLevel() {
		return grnd_level;
	}
}