package de.hhu.rucub100.bigdata2018.source.data;

import java.io.Serializable;

/**
 * 
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Sep 19, 2018
 * 
 * Serializable weather description POJO.
 */
public class Weather implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int id;
	private String main;
	private String description;
	private String icon;
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the main
	 */
	public String getMain() {
		return main;
	}
	/**
	 * @param main the main to set
	 */
	public void setMain(String main) {
		this.main = main;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the icon
	 */
	public String getIcon() {
		return icon;
	}
	/**
	 * @param icon the icon to set
	 */
	public void setIcon(String icon) {
		this.icon = icon;
	}
}