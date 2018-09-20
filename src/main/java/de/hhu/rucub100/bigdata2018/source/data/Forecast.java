/**
 * 
 */
package de.hhu.rucub100.bigdata2018.source.data;

/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Sep 19, 2018
 *
 */
public class Forecast {
	
	private final int cod;
	private final float message;
	private final int cnt;
	private final ForecastItem[] list;
	
	public Forecast(int cod, float message, int cnt, ForecastItem[] list) {
		this.cod = cod;
		this.message = message;
		this.cnt = cnt;
		this.list = list;
	}

	/**
	 * @return the cod
	 */
	public int getCode() {
		return cod;
	}

	/**
	 * @return the message
	 */
	public float getMessage() {
		return message;
	}

	/**
	 * @return the cnt
	 */
	public int getCount() {
		return cnt;
	}

	/**
	 * @return the list
	 */
	public ForecastItem[] getList() {
		return list;
	}
}
