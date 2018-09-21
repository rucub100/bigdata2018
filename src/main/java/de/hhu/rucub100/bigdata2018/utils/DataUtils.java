/**
 * 
 */
package de.hhu.rucub100.bigdata2018.utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import com.google.gson.Gson;

import de.hhu.rucub100.bigdata2018.source.data.CurrentWeather;
import de.hhu.rucub100.bigdata2018.source.data.Forecast;

/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Sep 21, 2018
 *
 */
public class DataUtils {
	
	public static final String pathToCurrentWeatherData = "./test/currentWeatherCollection.txt.gz";
	public static final String pathToForecastData = "./test/forecastCollection.txt.gz";
	
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
	
	public static List<CurrentWeather> getCurrentWeatherData() {
		List<CurrentWeather> data = new ArrayList<CurrentWeather>();
		getData(pathToCurrentWeatherData, CurrentWeather.class, data);
		return data;
	}
	
	public static List<Forecast> getForecastData() {
		List<Forecast> data = new ArrayList<Forecast>();
		getData(pathToForecastData, Forecast.class, data);
		return data;
	}
}
