package de.hhu.rucub100.bigdata2018.source;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.zip.GZIPInputStream;

import org.apache.flink.streaming.api.functions.source.SourceFunction;

import com.google.gson.Gson;

import de.hhu.rucub100.bigdata2018.source.data.City;
import de.hhu.rucub100.bigdata2018.source.data.Country;
import de.hhu.rucub100.bigdata2018.source.data.CurrentWeather;
import de.hhu.rucub100.bigdata2018.source.data.Europe;
import de.hhu.rucub100.bigdata2018.utils.DataUtils;

/**
 * 
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Sep 18, 2018
 * 
 */
public class CurrentWeatherSource implements SourceFunction<CurrentWeather> {

	private final String dataFilePath;
	private final int servingSpeed;
	private final boolean simulation;
	
	private transient BufferedReader reader;
	private transient InputStream gzipStream;
	
	public CurrentWeatherSource(String dataFilePath, int servingSpeed, boolean simulation) {
		this.dataFilePath = dataFilePath;
		this.servingSpeed = servingSpeed;
		this.simulation = simulation;
	}

	@Override
	public void run(SourceContext<CurrentWeather> ctx) throws Exception {
		gzipStream = new GZIPInputStream(new FileInputStream(dataFilePath));
		reader = new BufferedReader(new InputStreamReader(gzipStream, "UTF-8"));

		if (simulation) {
			simulateStream(ctx);			
		} else {
			startStream(ctx);
		}

		this.reader.close();
		this.reader = null;
		this.gzipStream.close();
		this.gzipStream = null;
	}

	@Override
	public void cancel() {
		try {
			if (this.reader != null) {
				this.reader.close();
			}
			
			if (this.gzipStream != null) {
				this.gzipStream.close();
			}
		} catch(IOException ioe) {
			throw new RuntimeException("Could not cancel SourceFunction", ioe);
		} finally {
			this.reader = null;
			this.gzipStream = null;
		}
	}

	private static CurrentWeather fetchCurrentWeather(
			OpenWeatherMapAPI api, 
			City city, 
			Gson gson) {
		String json;
		CurrentWeather cw;
		
		do {
			json = api.getCurrentWeatherByCityID(city.getId());	
			cw = gson.fromJson(json, CurrentWeather.class);
		} while(cw.getCode() != 200);
		
		return cw;
	}
	
	private void startStream(SourceContext<CurrentWeather> ctx) {
		final List<String> appids = OpenWeatherMapAPI.loadAppIDsFromFile();
		final Europe europe = DataUtils.getEurope();
		final Gson gson = new Gson();
		
		if (appids.isEmpty()) {
			throw new RuntimeException("No API key found!");
		}
		
		final OpenWeatherMapAPI api = new OpenWeatherMapAPI(appids.get(0));
		api.setMode(Mode.JSON);
		api.setUnits(Units.METRIC);
		
		CurrentWeather current;
		
		while(true) {
			for (Country country : europe.getCountries()) {
				for (City city : country.getList()) {
					current = fetchCurrentWeather(api, city, gson);
					long timestamp = current.getDataReceivingTime() * 1000L;
					ctx.collectWithTimestamp(current, timestamp);
					
					// sleep with respect to serving speed
					try {
						Thread.sleep(1000 / servingSpeed);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	private void simulateStream(SourceContext<CurrentWeather> ctx) {
		Iterator<CurrentWeather> iterator = DataUtils.getCurrentWeatherData().iterator();
		
		while (iterator.hasNext()) {
			CurrentWeather current = iterator.next();
			long timestamp = current.getDataReceivingTime() * 1000L;
			ctx.collectWithTimestamp(current, timestamp);
			
			// sleep with respect to serving speed
			try {
				Thread.sleep(1000 / servingSpeed);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} 	
	}
}
