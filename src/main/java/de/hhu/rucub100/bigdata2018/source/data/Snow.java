package de.hhu.rucub100.bigdata2018.source.data;

import com.google.gson.annotations.SerializedName;

/**
 * 
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Sep 19, 2018
 *
 */
public class Snow {
	
	@SerializedName("3h") 
	private float _3h;

	/**
	 * @return the _3h
	 */
	public float get_3h() {
		return _3h;
	}

	/**
	 * @param _3h the _3h to set
	 */
	public void set_3h(float _3h) {
		this._3h = _3h;
	}
}