package de.hhu.rucub100.bigdata2018.source.data;

import java.io.Serializable;

/**
 * 
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Sep 19, 2018
 * 
 * A clouds city POJO.
 */
public class Clouds implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private float all;

	/**
	 * @return the all
	 */
	public float getAll() {
		return all;
	}

	/**
	 * @param all the all to set
	 */
	public void setAll(float all) {
		this.all = all;
	}
}