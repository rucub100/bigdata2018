/**
 * 
 */
package de.hhu.rucub100.bigdata2018.source.data;

/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Oct 3, 2018
 *
 */
public class Neighbors {

	private Country country1;
	private Country country2;
	
	/**
	 * @return the conutry1
	 */
	public Country getConutry1() {
		return country1;
	}
	
	/**
	 * @param conutry1 the conutry1 to set
	 */
	public void setConutry1(Country conutry1) {
		this.country1 = conutry1;
	}
	
	/**
	 * @return the conutry2
	 */
	public Country getConutry2() {
		return country2;
	}
	
	/**
	 * @param conutry2 the conutry2 to set
	 */
	public void setConutry2(Country conutry2) {
		this.country2 = conutry2;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return (country1 != null ? country1.hashCode() : 1) *
				(country2 != null ? country2.hashCode() : 1);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (obj == null || this.getClass() != obj.getClass()) {
			return false;
		}
		
		Neighbors other = (Neighbors) obj;
		
		if (((other.country1.equals(this.country1)) && (other.country2.equals(this.country2))) ||
				((other.country1.equals(this.country2)) && (other.country2.equals(this.country1)))) {
			return true;
		}
		
		return false;
	}
}
