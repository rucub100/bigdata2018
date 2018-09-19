/**
 * 
 */
package de.hhu.rucub100.bigdata2018.source;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import com.google.gson.Gson;

/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Sep 18, 2018
 *
 */
public class Collector {
	
	public static void main(String[] args) {
		List<String> appids = OpenWeatherMapAPI.loadAppIDsFromFile();
		
		if (appids.isEmpty()) {
			throw new RuntimeException("No API key found!");
		}
		
		Collector collector = new Collector(appids.get(0));
		//collector.collect();
		
		String json = "{\"coord\":{\"lon\":-0.13,\"lat\":51.51},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"base\":\"stations\",\"main\":{\"temp\":19.18,\"pressure\":1012,\"humidity\":72,\"temp_min\":18,\"temp_max\":20},\"visibility\":10000,\"wind\":{\"speed\":9.3,\"deg\":210,\"gust\":15.4},\"clouds\":{\"all\":75},\"dt\":1537350600,\"sys\":{\"type\":1,\"id\":5091,\"message\":0.0141,\"country\":\"GB\",\"sunrise\":1537335735,\"sunset\":1537380306},\"id\":2643743,\"name\":\"London\",\"cod\":200}\n";
		Gson gson = new Gson();
		CurrentWeather cw = gson.fromJson(json, CurrentWeather.class);
	}
	
	private OpenWeatherMapAPI api;
	private HttpClient httpClient;
	
	public Collector(String appid) {
		httpClient = HttpClients.createDefault();
		api = new OpenWeatherMapAPI(appid);
		api.setUnits(Units.METRIC);
	}
	
	public void collect() {
		HttpGet httpGet = new HttpGet(api.getCurrentWeatherEndpointByCityName("London", "uk"));
		try {
			CloseableHttpResponse response1 = (CloseableHttpResponse) httpClient.execute(httpGet);
			InputStreamReader isr = new InputStreamReader(response1.getEntity().getContent());
			BufferedReader br = new BufferedReader(isr);
			
			String line = null;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
			}
			
			br.close();
			isr.close();
			response1.close();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
