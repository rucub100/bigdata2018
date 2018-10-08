/**
 * 
 */
package de.hhu.rucub100.bigdata2018.source;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

import de.hhu.rucub100.bigdata2018.source.data.City;
import de.hhu.rucub100.bigdata2018.source.data.Country;
import de.hhu.rucub100.bigdata2018.source.data.Europe;
import de.hhu.rucub100.bigdata2018.utils.DataUtils;

/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Sep 18, 2018
 * 
 * The collector uses the OpenWeatherMap-API and allows retrieval of current weather or forecast data.
 */
public class Collector {
	
	private static void collectForecast(
			Europe europe, OpenWeatherMapAPI api) throws IOException {
		
		String outFile = "forecastCollection.txt";
		try {
			Files.createFile(Paths.get(outFile));
		} catch (FileAlreadyExistsException e1) {
			System.out.println("File already exists...");
		}
		
		for (Country country : europe.getCountries()) {
			System.out.println("Country: " + country.getName() + "...\n");
			
			for (City city : country.getList()) {
				int cityId = city.getId();
				System.out.println("  " + city.getName());
				
				String jsonWeather = api.getForecastByCityID(cityId);

				if (jsonWeather != null) {
					try {
						Files.write(
								Paths.get(outFile), 
								(jsonWeather + "\n").getBytes(), 
								StandardOpenOption.APPEND);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					System.err.println("No response for the selected city!");
				}
			}
			
			System.out.println("");
		}
	}
	
	private static void collectCurrentWeather(
			Europe europe, OpenWeatherMapAPI api) throws IOException {
		
		String outFile = "currentWeatherCollection.txt";
		try {
			Files.createFile(Paths.get(outFile));
		} catch (FileAlreadyExistsException e1) {
			System.out.println("File already exists...");
		}
		
		for (Country country : europe.getCountries()) {
			System.out.println("Country: " + country.getName() + "...\n");
			
			for (City city : country.getList()) {
				int cityId = city.getId();
				System.out.println("  " + city.getName());
				
				String jsonWeather = api.getCurrentWeatherByCityID(cityId);

				if (jsonWeather != null) {
					try {
						Files.write(
								Paths.get(outFile), 
								(jsonWeather + "\n").getBytes(), 
								StandardOpenOption.APPEND);
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					System.err.println("No response for the selected city!");
				}
			}
			
			System.out.println("");
		}
	}
	
	public static void main(String[] args) {
		List<String> appids = OpenWeatherMapAPI.loadAppIDsFromFile();
		
		if (appids.isEmpty()) {
			throw new RuntimeException("No API key found!");
		}
		
		int mode = -1;
		
		if (args.length > 0) {
			if (args[0].equals("--forecast")) {
				mode = 0;
			} else if (args[0].equals("--current")) {
				mode = 1;
			}
			
			Collector collector = new Collector(appids.get(0));
			collector.collect(mode);
		}
	}
	
	private final OpenWeatherMapAPI api;
	
	public Collector(String appid) {
		api = new OpenWeatherMapAPI(appid);
		api.setUnits(Units.METRIC);
	}
	
	public void collect(int mode) {
		try {
			Europe eu = DataUtils.getEurope();
			switch (mode) {
			case 0: collectForecast(eu, api);
				break;
			case 1: collectCurrentWeather(eu, api);
				break;
			default:
				System.err.println("No collector-mode specified!");
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
