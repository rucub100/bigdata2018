package de.hhu.rucub100.bigdata2018.source.data;

import com.google.gson.annotations.SerializedName;

/**
 * 
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Sep 19, 2018
 *
 */
public class Rain {
	
	@SerializedName("3h") 
	private final float _3h;

	public Rain(float _3h) {
		this._3h = _3h;
	}

	public float get_3h() {
		return _3h;
	}
}