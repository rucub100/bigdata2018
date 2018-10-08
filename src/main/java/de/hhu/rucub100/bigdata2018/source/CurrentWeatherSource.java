package de.hhu.rucub100.bigdata2018.source;

import java.util.Iterator;
import java.util.List;

import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.watermark.Watermark;

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
 * Source function, which serves as a transparent source for the weather data and allows a simulation 
 * or real streaming with respect to the adjustable serving speed.
 */
public class CurrentWeatherSource implements SourceFunction<CurrentWeather> {

	private static final long serialVersionUID = 1L;

	private final int servingSpeed;
	private final boolean simulation;
	private final boolean skip;
	
	private transient volatile boolean cancel = false;

	public CurrentWeatherSource(String dataFilePath, int servingSpeed, boolean simulation, boolean skip) {
		this.servingSpeed = servingSpeed;
		this.simulation = simulation;
		this.skip = skip;
	}

	@Override
	public void run(SourceContext<CurrentWeather> ctx) throws Exception {
		if (simulation) {
			simulateStream(ctx);			
		} else {
			startStream(ctx);
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
		} while(cw.getCod() != 200);
		
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
			long minTimestamp = Long.MAX_VALUE;
			
			for (Country country : europe.getCountries()) {
				for (City city : country.getList()) {
					current = fetchCurrentWeather(api, city, gson);
					long timestamp = current.getDt() * 1000L;
					
					if (minTimestamp > timestamp) {
						minTimestamp = timestamp;
					}
					
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
			
			ctx.emitWatermark(new Watermark(minTimestamp));
		}
	}
	
	private void simulateStream(SourceContext<CurrentWeather> ctx) {
		Iterator<CurrentWeather> iterator = 
				(skip ? 
					DataUtils.getOnlineCurrentWeather() : 
					DataUtils.getCurrentWeatherData())
				.iterator();
		
		final int p = 161;
		int s = 0;
		
		while (iterator.hasNext()) {
			if (s % p == 0 && s != 0) {
				// sleep with respect to serving speed
				try {
					Thread.sleep(1000 / servingSpeed);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					if (cancel) {
						break;
					}
				}
				
				s = 0;
			}
			
			if (cancel) {
				break;
			}
			
			CurrentWeather current = iterator.next();
			long timestamp = current.getDt() * 1000L;
			ctx.collectWithTimestamp(current, timestamp);
			
			s++;
			
			// do not accept any event >1h in past from current timestamp
			ctx.emitWatermark(new Watermark(timestamp - 1000 * 60 * 60));
		} 	
	}

	@Override
	public void cancel() {
		cancel = true;
	}
}
