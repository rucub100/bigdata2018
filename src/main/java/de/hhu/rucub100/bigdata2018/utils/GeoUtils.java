/**
 * 
 */
package de.hhu.rucub100.bigdata2018.utils;

import de.hhu.rucub100.bigdata2018.source.data.Coord;

/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Sep 26, 2018
 * 
 * Utils for the geo information.
 */
public class GeoUtils {

	/**
	 * Calculating the distance between geographical coordinates based on
	 * Great-circle distance.
	 * @see 
	 * <a href="https://en.wikipedia.org/wiki/Geographical_distance">https://en.wikipedia.org/wiki/Geographical_distance</a>
	 * <a href="https://en.wikipedia.org/wiki/Great-circle_distance">https://en.wikipedia.org/wiki/Great-circle_distance</a>
	 * <a href="https://en.wikipedia.org/wiki/Earth_radius">https://en.wikipedia.org/wiki/Earth_radius</a>
	 * @param c1 The first point
	 * @param c2 The second point
	 * @return Great-circle distance in km
	 */
	public static double getDistance(Coord c1, Coord c2) {
		final double r = 6371.0088;
		
		final double lat1 = Math.toRadians(c1.getLat());
		final double lon1 = Math.toRadians(c1.getLon());
		final double lat2 = Math.toRadians(c2.getLat());
		final double lon2 = Math.toRadians(c2.getLon());
		
		final double dLon = lon2 - lon1;
		
		final double dSig = Math.acos(
				Math.sin(lat1) * Math.sin(lat2) + 
				Math.cos(lat1) * Math.cos(lat2) * Math.cos(dLon));
		
		return r * dSig;
	}
	
	/**
	 * Calculating the distance between geographical coordinates based on Thaddeus Vincenty.
	 * @see
	 * <a href="https://de.wikipedia.org/wiki/Orthodrome">https://de.wikipedia.org/wiki/Orthodrome</a>
	 * <a href="https://www.movable-type.co.uk/scripts/latlong-vincenty.html">https://www.movable-type.co.uk/scripts/latlong-vincenty.html</a>
	 * @param c1 The first point
	 * @param c2 The second point
	 * @return The distance in km
	 */
	public static double getDistance2(Coord c1, Coord c2) {
		final double r_a = 6378.137;
		final double f =  1 / 298.257223563; // 0.0033528106647474807198455286185205559557326361780097...
		
		final double lat1 = Math.toRadians(c1.getLat());
		final double lon1 = Math.toRadians(c1.getLon());
		final double lat2 = Math.toRadians(c2.getLat());
		final double lon2 = Math.toRadians(c2.getLon());
		
		final double F = (lat1 + lat2) / 2;
		final double G = (lat1 - lat2) / 2;
		final double l = (lon1 - lon2) / 2;
		
		final double S = (Math.sin(G) * Math.sin(G)) * (Math.cos(l) * Math.cos(l)) + 
				(Math.sin(l) * Math.sin(l)) * (Math.cos(F) * Math.cos(F));
		final double C = (Math.cos(G) * Math.cos(G)) * (Math.cos(l) * Math.cos(l)) + 
				(Math.sin(l) * Math.sin(l)) * (Math.sin(F) * Math.sin(F));
		
		final double w = Math.atan(Math.sqrt(S / C));
		final double D = 2 * w * r_a;
		
		final double T = Math.sqrt(S * C) / w;
		final double H1 = (3 * T - 1) / (2 * C);
		final double H2 = (3 * T + 1) / (2 * S);
		
		final double s = D * (1 + f * H1 * Math.sin(F) * Math.sin(F) * Math.cos(G) * Math.cos(G) -
				f * H2 * Math.cos(F) * Math.cos(F) * Math.sin(G) * Math.sin(G));
		
		return s;
	}
}
