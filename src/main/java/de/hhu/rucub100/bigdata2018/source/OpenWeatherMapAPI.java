package de.hhu.rucub100.bigdata2018.source;

import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Sep 18, 2018
 *
 */
public final class OpenWeatherMapAPI {
	
	private static final String API_URL = "http://api.openweathermap.org";
	private static final String VERSION = "2.5";
	private static final String PREFIX = "data";
	private static final String CURRENT = "weather";
	private static final String FORECAST = "forecast";
	
	private static final String PLACEHOLDER_CITY_NAME = "city_name";
	private static final String PLACEHOLDER_CITY_ID = "city_id";
	private static final String PLACEHOLDER_COUNTRY_CODE = "country_code";
	private static final String PLACEHOLDER_LAT = "lat";
	private static final String PLACEHOLDER_LON = "lon";
	private static final String PLACEHOLDER_ZIP_CODE = "zip_code";
	private static final String PLACEHOLDER_APPID = "appid";
	private static final String PLACEHOLDER_UNITS = "units";
	private static final String PLACEHOLDER_MODE = "mode";
	
	private static final String URL_SEPARATOR = "/";
	private static final String ARGS_SEPARATOR = "&";
	
	private static final String assembleEndpoint(
			String prefix,
			String query, 
			Mode mode, 
			Units units, 
			String appid) {
		StringBuilder endpoint = new StringBuilder();
		endpoint.append(API_URL);
		endpoint.append(URL_SEPARATOR);
		endpoint.append(PREFIX);
		endpoint.append(URL_SEPARATOR);
		endpoint.append(VERSION);
		endpoint.append(URL_SEPARATOR);
		endpoint.append(prefix);
		
		endpoint.append("?");
		endpoint.append(query);
		endpoint.append(ARGS_SEPARATOR);
		endpoint.append(PLACEHOLDER_APPID);
		endpoint.append("=");
		endpoint.append(appid);
		
		if (units != Units.DEFAULT) {
			endpoint.append(ARGS_SEPARATOR);
			endpoint.append(PLACEHOLDER_UNITS);
			endpoint.append("=");
			endpoint.append(units.toString());
		}
		
		if (mode != Mode.JSON) {
			endpoint.append(ARGS_SEPARATOR);
			endpoint.append(PLACEHOLDER_MODE);
			endpoint.append("=");
			endpoint.append(mode.toString());
		}
		
		return endpoint.toString();
	}
	
	private static final String assembleCurrentWeatherEndpoint(
			String query, 
			Mode mode, 
			Units units, 
			String appid) {
		return assembleEndpoint(CURRENT, query, mode, units, appid);
	}
	
	private static final String assembleForecastEndpoint(
			String query, 
			Mode mode, 
			Units units, 
			String appid) {
		return assembleEndpoint(FORECAST, query, mode, units, appid);
	}

	private static final String assembleQueryTemplate(QueryType query) {
		StringBuilder templateBuilder = new StringBuilder();
		
		switch (query) {
		case BY_CITY_ID:
			templateBuilder.append("id=::" + PLACEHOLDER_CITY_ID + "::");
			break;
		case BY_CITY_NAME:
			templateBuilder.append("q=::" + PLACEHOLDER_CITY_NAME + "::");
			templateBuilder.append(",::" + PLACEHOLDER_COUNTRY_CODE + "::");
			break;
		case BY_GEO_COORDS:
			templateBuilder.append("lat=::" + PLACEHOLDER_LAT + "::");
			templateBuilder.append("lon=::" + PLACEHOLDER_LON + "::");
			break;
		case BY_ZIP_CODE:
			templateBuilder.append("zip=::" + PLACEHOLDER_ZIP_CODE + "::");
			templateBuilder.append(",::" + PLACEHOLDER_COUNTRY_CODE + "::");
			break;
		}
		
		return templateBuilder.toString();
	}
	
	private static String assembleQueryByCityName(String cityName, String countryCode) {
		Map<String, String> buildMap = new HashMap<String, String>();
		buildMap.put(PLACEHOLDER_CITY_NAME, cityName);
		buildMap.put(PLACEHOLDER_COUNTRY_CODE, countryCode);
		
		String query = assembleQueryTemplate(QueryType.BY_CITY_NAME);
		return buildQuery(query, buildMap);
	}
	
	private static String assembleQueryByCityID(String cityId) {
		Map<String, String> buildMap = new HashMap<String, String>();
		buildMap.put(PLACEHOLDER_CITY_ID, cityId);
		
		String query = assembleQueryTemplate(QueryType.BY_CITY_ID);
		return buildQuery(query, buildMap);
	}
	
	private static String assembleQueryByGeoCoords(float lat, float lon) {
		Map<String, String> buildMap = new HashMap<String, String>();
		buildMap.put(PLACEHOLDER_LON, String.valueOf(lon));
		buildMap.put(PLACEHOLDER_LAT, String.valueOf(lat));
		
		String query = assembleQueryTemplate(QueryType.BY_GEO_COORDS);
		return buildQuery(query, buildMap);
	}
	
	private static String assembleQueryByZipCode(String zipCode, String countryCode) {
		Map<String, String> buildMap = new HashMap<String, String>();
		buildMap.put(PLACEHOLDER_ZIP_CODE, zipCode);
		buildMap.put(PLACEHOLDER_COUNTRY_CODE, countryCode);
		
		String query = assembleQueryTemplate(QueryType.BY_ZIP_CODE);
		return buildQuery(query, buildMap);
	}

	private static final String buildQuery(
			String template, 
			Map<String, String> buildMap) {
		for (String placeholder : buildMap.keySet()) {
			template = template.replaceAll("::" + placeholder + "::", buildMap.get(placeholder));
		}
		
		return template;
	}

	public static final List<String> loadAppIDsFromFile() {
		List<String> appIds = new ArrayList<String>();
		
		Path file = Paths.get("appids.txt");
		try (InputStream in = Files.newInputStream(file);
		    BufferedReader reader =
		      new BufferedReader(new InputStreamReader(in))) {
		    String line = null;
		    while ((line = reader.readLine()) != null) {
		    	if (line.length() > 0)
		    		appIds.add(line);
		    }
		} catch (IOException x) {
		    System.err.println(x);
		}
		
		return appIds;
	}
	
	private Mode mode;
	private Units units;
	private final String appid;
	
	public OpenWeatherMapAPI(String appid) {
		this.mode = Mode.JSON;
		this.units = Units.DEFAULT;
		this.appid = appid;
	}
	
	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		this.mode = mode;
	}

	public Units getUnits() {
		return units;
	}

	public void setUnits(Units units) {
		this.units = units;
	}
	
	public String getCurrentWeatherEndpointByCityName(String cityName, String countryCode) {
		return assembleCurrentWeatherEndpoint(
				assembleQueryByCityName(cityName, countryCode), 
				mode, units, appid);
	}
	
	public String getCurrentWeatherEndpointByCityID(String cityId) {
		return assembleCurrentWeatherEndpoint(
				assembleQueryByCityID(cityId), 
				mode, units, appid);
	}
	
	public String getCurrentWeatherEndpointByGeoCoords(float lat, float lon) {
		return assembleCurrentWeatherEndpoint(
				assembleQueryByGeoCoords(lat, lon), 
				mode, units, appid);
	}
	
	public String getCurrentWeatherEndpointByZipCode(String zipCode, String countryCode) {
		return assembleCurrentWeatherEndpoint(
				assembleQueryByZipCode(zipCode, countryCode), 
				mode, units, appid);
	}
	
	public String getForecastEndpointByCityName(String cityName, String countryCode) {
		return assembleForecastEndpoint(
				assembleQueryByCityName(cityName, countryCode), 
				mode, units, appid);
	}
	
	public String getForecastEndpointByCityID(String cityId) {
		return assembleForecastEndpoint(
				assembleQueryByCityID(cityId), 
				mode, units, appid);
	}
	
	public String getForecastEndpointByGeoCoords(float lat, float lon) {
		return assembleForecastEndpoint(
				assembleQueryByGeoCoords(lat, lon), 
				mode, units, appid);
	}
	
	public String getForecastEndpointByZipCode(String zipCode, String countryCode) {
		return assembleForecastEndpoint(
				assembleQueryByZipCode(zipCode, countryCode), 
				mode, units, appid);
	}
}
