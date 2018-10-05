package de.hhu.rucub100.bigdata2018.source.data;

import java.io.Serializable;

/**
 * 
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Sep 19, 2018
 *
 */
public class Main implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private float temp;
	private float pressure;
	private float humidity;
	private float temp_min;
	private float temp_max;
	private float sea_level;
	private float grnd_level;
	
	/**
	 * @return the temp
	 */
	public float getTemp() {
		return temp;
	}
	/**
	 * @param temp the temp to set
	 */
	public void setTemp(float temp) {
		this.temp = temp;
	}
	/**
	 * @return the pressure
	 */
	public float getPressure() {
		return pressure;
	}
	/**
	 * @param pressure the pressure to set
	 */
	public void setPressure(float pressure) {
		this.pressure = pressure;
	}
	/**
	 * @return the humidity
	 */
	public float getHumidity() {
		return humidity;
	}
	/**
	 * @param humidity the humidity to set
	 */
	public void setHumidity(float humidity) {
		this.humidity = humidity;
	}
	/**
	 * @return the temp_min
	 */
	public float getTemp_min() {
		return temp_min;
	}
	/**
	 * @param temp_min the temp_min to set
	 */
	public void setTemp_min(float temp_min) {
		this.temp_min = temp_min;
	}
	/**
	 * @return the temp_max
	 */
	public float getTemp_max() {
		return temp_max;
	}
	/**
	 * @param temp_max the temp_max to set
	 */
	public void setTemp_max(float temp_max) {
		this.temp_max = temp_max;
	}
	/**
	 * @return the sea_level
	 */
	public float getSea_level() {
		return sea_level;
	}
	/**
	 * @param sea_level the sea_level to set
	 */
	public void setSea_level(float sea_level) {
		this.sea_level = sea_level;
	}
	/**
	 * @return the grnd_level
	 */
	public float getGrnd_level() {
		return grnd_level;
	}
	/**
	 * @param grnd_level the grnd_level to set
	 */
	public void setGrnd_level(float grnd_level) {
		this.grnd_level = grnd_level;
	}
}