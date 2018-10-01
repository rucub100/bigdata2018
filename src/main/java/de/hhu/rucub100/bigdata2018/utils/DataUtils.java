/**
 * 
 */
package de.hhu.rucub100.bigdata2018.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.commons.collections.map.HashedMap;

import com.google.gson.Gson;

import de.hhu.rucub100.bigdata2018.source.data.Country;
import de.hhu.rucub100.bigdata2018.source.data.CurrentWeather;
import de.hhu.rucub100.bigdata2018.source.data.Europe;
import de.hhu.rucub100.bigdata2018.source.data.Forecast;

/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Sep 21, 2018
 *
 */
public class DataUtils {
	
	private static Europe _EUROPE = null;
	
	private static Europe loadEuropeFromFile(String fileName) throws IOException {
		String json = new String(Files.readAllBytes(Paths.get(fileName)));
		Gson gson = new Gson();
		return gson.fromJson(json, Europe.class);
	}
	
	private static <T> void getData(String path, Class<T> classOfT, List<T> data) {
		GZIPInputStream gzipStream = null;
		BufferedReader reader = null;
		
		Gson gson = new Gson();
		
		try {
			gzipStream = new GZIPInputStream(new FileInputStream(pathToCurrentWeatherData));
			reader = new BufferedReader(new InputStreamReader(gzipStream, "UTF-8"));
			String line;
		
			while (reader.ready() && (line = reader.readLine()) != null) {
				data.add(gson.fromJson(line, classOfT));
			}
			
			reader.close();
			reader = null;
			gzipStream.close();
			gzipStream = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static final String pathToCurrentWeatherData = "./test/currentWeatherCollection.txt.gz";
	public static final String pathToForecastData = "./test/forecastCollection.txt.gz";
	
	public static List<CurrentWeather> getCurrentWeatherData() {
		List<CurrentWeather> data = new ArrayList<CurrentWeather>();
		getData(pathToCurrentWeatherData, CurrentWeather.class, data);
		
		// check data integrity
		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).getCod() != 200) {
				data.remove(i);
				--i;
			}
		}
		
		return data;
	}
	
	public static List<Forecast> getForecastData() {
		List<Forecast> data = new ArrayList<Forecast>();
		getData(pathToForecastData, Forecast.class, data);
		return data;
	}
	
	public static Europe getEurope() {
		if (_EUROPE == null) {
			try {
				_EUROPE = loadEuropeFromFile("europe.json");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return _EUROPE;
	}
	
	public static Map<String, String> getCountryMap() {
		Map<String, String> countryMap = new HashedMap();
		
		final Europe eu = getEurope();
		for (Country country: eu.getCountries()) {
			countryMap.put(country.getList()[0].getCountry(), country.getName());
		}
		
		return countryMap;
	}
}
