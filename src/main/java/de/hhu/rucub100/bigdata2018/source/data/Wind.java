package de.hhu.rucub100.bigdata2018.source.data;

import java.io.Serializable;

/**
 * 
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Sep 19, 2018
 *
 * Serializable wind information.
 */
public class Wind implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private float speed;
	private float deg;
	private float gust;
	
	/**
	 * @return the speed
	 */
	public float getSpeed() {
		return speed;
	}
	/**
	 * @param speed the speed to set
	 */
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	/**
	 * @return the deg
	 */
	public float getDeg() {
		return deg;
	}
	/**
	 * @param deg the deg to set
	 */
	public void setDeg(float deg) {
		this.deg = deg;
	}
	/**
	 * @return the gust
	 */
	public float getGust() {
		return gust;
	}
	/**
	 * @param gust the gust to set
	 */
	public void setGust(float gust) {
		this.gust = gust;
	}
}