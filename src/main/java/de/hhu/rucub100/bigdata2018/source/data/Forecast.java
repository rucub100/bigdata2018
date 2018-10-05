/**
 * 
 */
package de.hhu.rucub100.bigdata2018.source.data;

import java.io.Serializable;

/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Sep 19, 2018
 *
 */
public class Forecast implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int cod;
	private float message;
	private int cnt;
	private ForecastItem[] list;
	/**
	 * @return the cod
	 */
	public int getCod() {
		return cod;
	}
	/**
	 * @param cod the cod to set
	 */
	public void setCod(int cod) {
		this.cod = cod;
	}
	/**
	 * @return the message
	 */
	public float getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(float message) {
		this.message = message;
	}
	/**
	 * @return the cnt
	 */
	public int getCnt() {
		return cnt;
	}
	/**
	 * @param cnt the cnt to set
	 */
	public void setCnt(int cnt) {
		this.cnt = cnt;
	}
	/**
	 * @return the list
	 */
	public ForecastItem[] getList() {
		return list;
	}
	/**
	 * @param list the list to set
	 */
	public void setList(ForecastItem[] list) {
		this.list = list;
	}
}
