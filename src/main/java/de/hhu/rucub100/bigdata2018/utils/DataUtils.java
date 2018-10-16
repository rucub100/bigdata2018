/**
 * 
 */
package de.hhu.rucub100.bigdata2018.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;

import com.google.gson.Gson;

import de.hhu.rucub100.bigdata2018.source.data.City;
import de.hhu.rucub100.bigdata2018.source.data.Country;
import de.hhu.rucub100.bigdata2018.source.data.CurrentWeather;
import de.hhu.rucub100.bigdata2018.source.data.Europe;
import de.hhu.rucub100.bigdata2018.source.data.Forecast;
import de.hhu.rucub100.bigdata2018.source.data.Neighbors;

/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Sep 21, 2018
 * 
 * Utils for the data.
 */
public class DataUtils {
	
	private static Europe _EUROPE = null;
	private static List<CurrentWeather> current = null;
	
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
			gzipStream = new GZIPInputStream(new FileInputStream(path));
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
	
	public static final String pathToMaxTemperatureEuropeResult = "./test/batch_1.txt";
	public static final String pathToMaxTemperatureDiffEuropeResult = "./test/batch_2.txt";
	public static final String pathToColdestCityInEuropeResult = "./test/batch_3.txt";
	public static final String pathToMinTemperatureDiffCountriesResult = "./test/batch_4.txt";
	public static final String pathToAvgTemperaturePerCountryResult = "./test/batch_5.txt";
	
	public static float readMaxTemperatureDiffEuropeResult() throws IOException {
		Path p = Paths.get(pathToMaxTemperatureDiffEuropeResult);
		
		if (Files.exists(p)) {
			String asText = new String(Files.readAllBytes(p));
			
			return Float.parseFloat(asText);
			
		} else {
			throw new FileNotFoundException(p.toString());
		}
	}
	
	public static Tuple3<String, String, Float> readMaxTemperatureEuropeResult() throws IOException {
		Path p = Paths.get(pathToMaxTemperatureEuropeResult);
		
		if (Files.exists(p)) {
			String asText = new String(Files.readAllBytes(p));
			
			String[] tmp = asText.substring(1, asText.length() - 2).split(",");
			
			String country = tmp[0];
			String city = tmp[1];
			float temp = Float.parseFloat(tmp[2]);
			
			return new Tuple3<String, String, Float>(country, city, temp);
		} else {
			throw new FileNotFoundException(p.toString());
		}
	}
	
	public static Tuple3<String, String, Float> readColdestCityInEuropeResult() throws IOException {
		Path p = Paths.get(pathToColdestCityInEuropeResult);
		
		if (Files.exists(p)) {
			String asText = new String(Files.readAllBytes(p));
			
			String[] tmp = asText.substring(1, asText.length() - 2).split(",");
			
			String country = tmp[0];
			String city = tmp[1];
			float temp = Float.parseFloat(tmp[2]);
			
			return new Tuple3<String, String, Float>(country, city, temp);
		} else {
			throw new FileNotFoundException(p.toString());
		}
	}
	
	public static List<Tuple2<String, Float>> readMinTemperatureDiffCountriesResult() throws IOException {
		Path p = Paths.get(pathToMinTemperatureDiffCountriesResult);
		List<Tuple2<String, Float>> result = new ArrayList<Tuple2<String, Float>>();
		
		if (Files.isDirectory(p)) {
			for (int i = 0; i < 4; i++) {
				p = Paths.get(pathToMinTemperatureDiffCountriesResult + File.separator + String.valueOf(i));
				
				if (Files.exists(p)) {
					List<String> asText = Files.readAllLines(p);
					
					for (String line : asText) {
						String[] tmp = line.substring(1, line.length() - 2).split(",");
						
						String country = tmp[0];
						float temp = Float.parseFloat(tmp[1]);
						
						result.add(new Tuple2<String, Float>(country, temp));
					}
					
				}
			}
		} else if (Files.exists(p)) {
			List<String> asText = Files.readAllLines(p);
			
			for (String line : asText) {
				String[] tmp = line.substring(1, line.length() - 2).split(",");
				
				String country = tmp[0];
				float temp = Float.parseFloat(tmp[1]);
				
				result.add(new Tuple2<String, Float>(country, temp));
			}
			
		} else {
			throw new FileNotFoundException(p.toString());
		}
		
		return result;
	}
	
	public static List<Tuple2<String, Float>> readAvgTemperaturePerCountryResult() throws IOException {
		Path p = Paths.get(pathToAvgTemperaturePerCountryResult);
		List<Tuple2<String, Float>> result = new ArrayList<Tuple2<String, Float>>();
		
		if (Files.isDirectory(p)) {
			for (int i = 0; i < 4; i++) {
				p = Paths.get(pathToAvgTemperaturePerCountryResult + File.separator + String.valueOf(i));
				
				if (Files.exists(p)) {
					List<String> asText = Files.readAllLines(p);
					
					for (String line : asText) {
						String[] tmp = line.substring(1, line.length() - 2).split(",");
						
						String country = tmp[0];
						float temp = Float.parseFloat(tmp[1]);
						
						result.add(new Tuple2<String, Float>(country, temp));
					}
					
				}
			}
		} else if (Files.exists(p)) {
			List<String> asText = Files.readAllLines(p);
			
			for (String line : asText) {
				String[] tmp = line.substring(1, line.length() - 2).split(",");
				
				String country = tmp[0];
				float temp = Float.parseFloat(tmp[1]);
				
				result.add(new Tuple2<String, Float>(country, temp));
			}
			
		} else {
			throw new FileNotFoundException(p.toString());
		}
		
		return result;
	}
	
	public static List<CurrentWeather> getCurrentWeatherData() {
		if (current == null) {
			current = new ArrayList<CurrentWeather>();
			getData(pathToCurrentWeatherData, CurrentWeather.class, current);
			
			// check data integrity
			for (int i = 0; i < current.size(); i++) {
				if (current.get(i).getCod() != 200) {
					current.remove(i);
					--i;
				}
			}		
		}
		
		return Collections.unmodifiableList(current);
	}
	
	public static List<CurrentWeather> getOfflineCurrentWeather() {
		List<CurrentWeather> cw = getCurrentWeatherData();
		return cw.subList(0, (cw.size() / 2) - 1);
	}
	
	public static List<CurrentWeather> getOnlineCurrentWeather() {
		List<CurrentWeather> cw = getCurrentWeatherData();
		return cw.subList(cw.size() / 2, cw.size() - 1);
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
	
	public static List<Neighbors> getNeighbors() {
		Set<Neighbors> neighbors = new HashSet<Neighbors>();
		
		Europe eu = getEurope();
		
		for (Country c1 : eu.getCountries()) {
			for (Country c2 : eu.getCountries()) {
				if (!Arrays.asList(c1.getNeighbors()).contains(c2.getName())) {
					continue;
				}
				
				Neighbors n = new Neighbors();
				n.setConutry1(c1);
				n.setConutry2(c2);
				
				neighbors.add(n);
			}
		}
		
		return new ArrayList<Neighbors>(neighbors);
	}
	
	public static Country getCountry(String country) {
		Country result = null;
		for (Country c : getEurope().getCountries()) {
			if (country.equalsIgnoreCase(c.getName()) ||
					country.equalsIgnoreCase(c.getList()[0].getCountry())) {
				result = c;
				break;
			}
		}
			
		return result;
	}
	
	public static City getCity(String city) {
		City result = null;
		
		for (Country country : getEurope().getCountries()) {
			for (City c : country.getList()) {
				if (c.getName().equalsIgnoreCase(city)) {
					result = c;
					break;
				}
			}
		}
		
		return result;
	}
	
	public static void setCurrentWeatherTags(CurrentWeather current) {
		current.setCity(getCity(current.getName()));
		current.setCountry(getCountry(current.getSys().getCountry()));
	}
}
