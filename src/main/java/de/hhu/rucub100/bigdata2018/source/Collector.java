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
		collector.collect();
	}
	
	private OpenWeatherMapAPI api;
	private HttpClient httpClient;
	
	public Collector(String appid) {
		httpClient = HttpClients.createDefault();
		api = new OpenWeatherMapAPI(appid);
		api.setUnits(Units.METRIC);
	}
	
	public void collect() {
		HttpGet httpGet = new HttpGet(api.getCurrentWeatherEndpointByCityName("Düsseldorf", "de"));
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
