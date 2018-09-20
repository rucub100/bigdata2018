package de.hhu.rucub100.bigdata2018.source.data;

/**
 * 
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Sep 19, 2018
 *
 */
public class Wind {
	
	private final float speed;
	private final float deg;
	private final float gust;
	
	public Wind(float speed, float deg, float gust) {
		this.speed = speed;
		this.deg = deg;
		this.gust = gust;
	}

	public float getSpeed() {
		return speed;
	}

	public float getDirection() {
		return deg;
	}

	public float getGust() {
		return gust;
	}
}