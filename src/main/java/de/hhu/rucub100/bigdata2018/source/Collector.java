/**
 * 
 */
package de.hhu.rucub100.bigdata2018.source;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import com.google.gson.Gson;

import de.hhu.rucub100.bigdata2018.source.data.CurrentWeather;
import de.hhu.rucub100.bigdata2018.source.data.Europe;
import de.hhu.rucub100.bigdata2018.source.data.Forecast;

/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Sep 18, 2018
 *
 */
public class Collector {
	
	private static Europe loadEuropeFromFile(String fileName) throws IOException {
		String json = new String(Files.readAllBytes(Paths.get(fileName)));
		Gson gson = new Gson();
		return gson.fromJson(json, Europe.class);
	}
	
	public static void main(String[] args) {
		List<String> appids = OpenWeatherMapAPI.loadAppIDsFromFile();
		
		if (appids.isEmpty()) {
			throw new RuntimeException("No API key found!");
		}
		
		Collector collector = new Collector(appids.get(0));
		//collector.collect();
		
		try {
			Europe eu = loadEuropeFromFile("europe.json");
			System.out.println(eu);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String json1 = "{\"coord\":{\"lon\":-0.13,\"lat\":51.51},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"base\":\"stations\",\"main\":{\"temp\":19.18,\"pressure\":1012,\"humidity\":72,\"temp_min\":18,\"temp_max\":20},\"visibility\":10000,\"wind\":{\"speed\":9.3,\"deg\":210,\"gust\":15.4},\"clouds\":{\"all\":75},\"dt\":1537350600,\"sys\":{\"type\":1,\"id\":5091,\"message\":0.0141,\"country\":\"GB\",\"sunrise\":1537335735,\"sunset\":1537380306},\"id\":2643743,\"name\":\"London\",\"cod\":200}\n";
		String json2 = "{\"cod\":\"200\",\"message\":0.0146,\"cnt\":37,\"list\":[{\"dt\":1537434000,\"main\":{\"temp\":18.5,\"temp_min\":18.5,\"temp_max\":18.69,\"pressure\":1017.82,\"sea_level\":1025.19,\"grnd_level\":1017.82,\"humidity\":74,\"temp_kf\":-0.19},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"clouds\":{\"all\":88},\"wind\":{\"speed\":6.81,\"deg\":222.013},\"rain\":{\"3h\":0.03},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2018-09-20 09:00:00\"},{\"dt\":1537444800,\"main\":{\"temp\":19.75,\"temp_min\":19.75,\"temp_max\":19.88,\"pressure\":1016.61,\"sea_level\":1024.01,\"grnd_level\":1016.61,\"humidity\":69,\"temp_kf\":-0.13},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"clouds\":{\"all\":100},\"wind\":{\"speed\":7.86,\"deg\":217.5},\"rain\":{\"3h\":0.01},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2018-09-20 12:00:00\"},{\"dt\":1537455600,\"main\":{\"temp\":20.14,\"temp_min\":20.14,\"temp_max\":20.2,\"pressure\":1015.2,\"sea_level\":1022.52,\"grnd_level\":1015.2,\"humidity\":67,\"temp_kf\":-0.06},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"clouds\":{\"all\":92},\"wind\":{\"speed\":7.97,\"deg\":219.001},\"rain\":{\"3h\":0.005},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2018-09-20 15:00:00\"},{\"dt\":1537466400,\"main\":{\"temp\":19.09,\"temp_min\":19.09,\"temp_max\":19.09,\"pressure\":1012.86,\"sea_level\":1020.18,\"grnd_level\":1012.86,\"humidity\":73,\"temp_kf\":0},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"clouds\":{\"all\":92},\"wind\":{\"speed\":8.52,\"deg\":215.008},\"rain\":{\"3h\":0.025},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2018-09-20 18:00:00\"},{\"dt\":1537477200,\"main\":{\"temp\":18.89,\"temp_min\":18.89,\"temp_max\":18.89,\"pressure\":1009.98,\"sea_level\":1017.43,\"grnd_level\":1009.98,\"humidity\":77,\"temp_kf\":0},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10n\"}],\"clouds\":{\"all\":92},\"wind\":{\"speed\":8.81,\"deg\":211.503},\"rain\":{\"3h\":0.13},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2018-09-20 21:00:00\"},{\"dt\":1537488000,\"main\":{\"temp\":18.28,\"temp_min\":18.28,\"temp_max\":18.28,\"pressure\":1007.09,\"sea_level\":1014.41,\"grnd_level\":1007.09,\"humidity\":84,\"temp_kf\":0},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10n\"}],\"clouds\":{\"all\":92},\"wind\":{\"speed\":10.1,\"deg\":213.501},\"rain\":{\"3h\":0.64},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2018-09-21 00:00:00\"},{\"dt\":1537498800,\"main\":{\"temp\":16.29,\"temp_min\":16.29,\"temp_max\":16.29,\"pressure\":1004.78,\"sea_level\":1012.07,\"grnd_level\":1004.78,\"humidity\":98,\"temp_kf\":0},\"weather\":[{\"id\":501,\"main\":\"Rain\",\"description\":\"moderate rain\",\"icon\":\"10n\"}],\"clouds\":{\"all\":92},\"wind\":{\"speed\":8.52,\"deg\":227.002},\"rain\":{\"3h\":4.915},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2018-09-21 03:00:00\"},{\"dt\":1537509600,\"main\":{\"temp\":13.31,\"temp_min\":13.31,\"temp_max\":13.31,\"pressure\":1007.06,\"sea_level\":1014.49,\"grnd_level\":1007.06,\"humidity\":93,\"temp_kf\":0},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"clouds\":{\"all\":36},\"wind\":{\"speed\":7.5,\"deg\":254.005},\"rain\":{\"3h\":0.655},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2018-09-21 06:00:00\"},{\"dt\":1537520400,\"main\":{\"temp\":12.67,\"temp_min\":12.67,\"temp_max\":12.67,\"pressure\":1010.19,\"sea_level\":1017.74,\"grnd_level\":1010.19,\"humidity\":92,\"temp_kf\":0},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"clouds\":{\"all\":92},\"wind\":{\"speed\":4.41,\"deg\":252.505},\"rain\":{\"3h\":0.455},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2018-09-21 09:00:00\"},{\"dt\":1537531200,\"main\":{\"temp\":11.14,\"temp_min\":11.14,\"temp_max\":11.14,\"pressure\":1012.57,\"sea_level\":1020.02,\"grnd_level\":1012.57,\"humidity\":100,\"temp_kf\":0},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"clouds\":{\"all\":80},\"wind\":{\"speed\":6.17,\"deg\":285.501},\"rain\":{\"3h\":0.695},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2018-09-21 12:00:00\"},{\"dt\":1537542000,\"main\":{\"temp\":13.37,\"temp_min\":13.37,\"temp_max\":13.37,\"pressure\":1014.79,\"sea_level\":1022.23,\"grnd_level\":1014.79,\"humidity\":76,\"temp_kf\":0},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"clouds\":{\"all\":8},\"wind\":{\"speed\":6.61,\"deg\":265.001},\"rain\":{\"3h\":0.06},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2018-09-21 15:00:00\"},{\"dt\":1537552800,\"main\":{\"temp\":11.89,\"temp_min\":11.89,\"temp_max\":11.89,\"pressure\":1016.67,\"sea_level\":1024.25,\"grnd_level\":1016.67,\"humidity\":71,\"temp_kf\":0},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"clouds\":{\"all\":36},\"wind\":{\"speed\":7.16,\"deg\":260.001},\"rain\":{\"3h\":0.0099999999999998},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2018-09-21 18:00:00\"},{\"dt\":1537563600,\"main\":{\"temp\":11.42,\"temp_min\":11.42,\"temp_max\":11.42,\"pressure\":1019.56,\"sea_level\":1027.09,\"grnd_level\":1019.56,\"humidity\":73,\"temp_kf\":0},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10n\"}],\"clouds\":{\"all\":36},\"wind\":{\"speed\":5.71,\"deg\":277},\"rain\":{\"3h\":0.004999999999999},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2018-09-21 21:00:00\"},{\"dt\":1537574400,\"main\":{\"temp\":10.37,\"temp_min\":10.37,\"temp_max\":10.37,\"pressure\":1022.16,\"sea_level\":1029.74,\"grnd_level\":1022.16,\"humidity\":79,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":4.26,\"deg\":275},\"rain\":{},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2018-09-22 00:00:00\"},{\"dt\":1537585200,\"main\":{\"temp\":9.6,\"temp_min\":9.6,\"temp_max\":9.6,\"pressure\":1023.67,\"sea_level\":1031.31,\"grnd_level\":1023.67,\"humidity\":83,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":4.11,\"deg\":265.503},\"rain\":{},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2018-09-22 03:00:00\"},{\"dt\":1537596000,\"main\":{\"temp\":8.87,\"temp_min\":8.87,\"temp_max\":8.87,\"pressure\":1024.81,\"sea_level\":1032.47,\"grnd_level\":1024.81,\"humidity\":80,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":4.26,\"deg\":268},\"rain\":{},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2018-09-22 06:00:00\"},{\"dt\":1537606800,\"main\":{\"temp\":12.07,\"temp_min\":12.07,\"temp_max\":12.07,\"pressure\":1026.74,\"sea_level\":1034.37,\"grnd_level\":1026.74,\"humidity\":73,\"temp_kf\":0},\"weather\":[{\"id\":802,\"main\":\"Clouds\",\"description\":\"scattered clouds\",\"icon\":\"03d\"}],\"clouds\":{\"all\":32},\"wind\":{\"speed\":4.47,\"deg\":282.002},\"rain\":{},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2018-09-22 09:00:00\"},{\"dt\":1537617600,\"main\":{\"temp\":13.69,\"temp_min\":13.69,\"temp_max\":13.69,\"pressure\":1027.15,\"sea_level\":1034.74,\"grnd_level\":1027.15,\"humidity\":65,\"temp_kf\":0},\"weather\":[{\"id\":802,\"main\":\"Clouds\",\"description\":\"scattered clouds\",\"icon\":\"03d\"}],\"clouds\":{\"all\":44},\"wind\":{\"speed\":4.72,\"deg\":275.002},\"rain\":{},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2018-09-22 12:00:00\"},{\"dt\":1537628400,\"main\":{\"temp\":14.41,\"temp_min\":14.41,\"temp_max\":14.41,\"pressure\":1027.04,\"sea_level\":1034.59,\"grnd_level\":1027.04,\"humidity\":59,\"temp_kf\":0},\"weather\":[{\"id\":802,\"main\":\"Clouds\",\"description\":\"scattered clouds\",\"icon\":\"03d\"}],\"clouds\":{\"all\":36},\"wind\":{\"speed\":5.26,\"deg\":272.004},\"rain\":{},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2018-09-22 15:00:00\"},{\"dt\":1537639200,\"main\":{\"temp\":12.63,\"temp_min\":12.63,\"temp_max\":12.63,\"pressure\":1027.19,\"sea_level\":1034.86,\"grnd_level\":1027.19,\"humidity\":57,\"temp_kf\":0},\"weather\":[{\"id\":802,\"main\":\"Clouds\",\"description\":\"scattered clouds\",\"icon\":\"03n\"}],\"clouds\":{\"all\":36},\"wind\":{\"speed\":3.66,\"deg\":278.001},\"rain\":{},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2018-09-22 18:00:00\"},{\"dt\":1537650000,\"main\":{\"temp\":9.4,\"temp_min\":9.4,\"temp_max\":9.4,\"pressure\":1028.14,\"sea_level\":1035.83,\"grnd_level\":1028.14,\"humidity\":72,\"temp_kf\":0},\"weather\":[{\"id\":802,\"main\":\"Clouds\",\"description\":\"scattered clouds\",\"icon\":\"03n\"}],\"clouds\":{\"all\":44},\"wind\":{\"speed\":1.87,\"deg\":265.502},\"rain\":{},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2018-09-22 21:00:00\"},{\"dt\":1537660800,\"main\":{\"temp\":7.23,\"temp_min\":7.23,\"temp_max\":7.23,\"pressure\":1027.8,\"sea_level\":1035.5,\"grnd_level\":1027.8,\"humidity\":87,\"temp_kf\":0},\"weather\":[{\"id\":803,\"main\":\"Clouds\",\"description\":\"broken clouds\",\"icon\":\"04n\"}],\"clouds\":{\"all\":68},\"wind\":{\"speed\":1.16,\"deg\":192},\"rain\":{},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2018-09-23 00:00:00\"},{\"dt\":1537671600,\"main\":{\"temp\":8.32,\"temp_min\":8.32,\"temp_max\":8.32,\"pressure\":1025.55,\"sea_level\":1033.25,\"grnd_level\":1025.55,\"humidity\":83,\"temp_kf\":0},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10n\"}],\"clouds\":{\"all\":88},\"wind\":{\"speed\":1.55,\"deg\":134.501},\"rain\":{\"3h\":0.12},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2018-09-23 03:00:00\"},{\"dt\":1537682400,\"main\":{\"temp\":9.25,\"temp_min\":9.25,\"temp_max\":9.25,\"pressure\":1022.19,\"sea_level\":1029.68,\"grnd_level\":1022.19,\"humidity\":99,\"temp_kf\":0},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"clouds\":{\"all\":92},\"wind\":{\"speed\":3.31,\"deg\":128.003},\"rain\":{\"3h\":2.57},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2018-09-23 06:00:00\"},{\"dt\":1537693200,\"main\":{\"temp\":12.26,\"temp_min\":12.26,\"temp_max\":12.26,\"pressure\":1015.48,\"sea_level\":1023.11,\"grnd_level\":1015.48,\"humidity\":100,\"temp_kf\":0},\"weather\":[{\"id\":501,\"main\":\"Rain\",\"description\":\"moderate rain\",\"icon\":\"10d\"}],\"clouds\":{\"all\":92},\"wind\":{\"speed\":6.77,\"deg\":165.501},\"rain\":{\"3h\":4.16},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2018-09-23 09:00:00\"},{\"dt\":1537704000,\"main\":{\"temp\":17.23,\"temp_min\":17.23,\"temp_max\":17.23,\"pressure\":1010.06,\"sea_level\":1017.54,\"grnd_level\":1010.06,\"humidity\":96,\"temp_kf\":0},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"clouds\":{\"all\":92},\"wind\":{\"speed\":7.62,\"deg\":219.503},\"rain\":{\"3h\":2.45},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2018-09-23 12:00:00\"},{\"dt\":1537714800,\"main\":{\"temp\":17.87,\"temp_min\":17.87,\"temp_max\":17.87,\"pressure\":1006.18,\"sea_level\":1013.54,\"grnd_level\":1006.18,\"humidity\":95,\"temp_kf\":0},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10d\"}],\"clouds\":{\"all\":92},\"wind\":{\"speed\":9.06,\"deg\":223.004},\"rain\":{\"3h\":0.96},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2018-09-23 15:00:00\"},{\"dt\":1537725600,\"main\":{\"temp\":17.08,\"temp_min\":17.08,\"temp_max\":17.08,\"pressure\":1000.6,\"sea_level\":1007.86,\"grnd_level\":1000.6,\"humidity\":96,\"temp_kf\":0},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10n\"}],\"clouds\":{\"all\":92},\"wind\":{\"speed\":10.87,\"deg\":220.002},\"rain\":{\"3h\":0.63},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2018-09-23 18:00:00\"},{\"dt\":1537736400,\"main\":{\"temp\":15.22,\"temp_min\":15.22,\"temp_max\":15.22,\"pressure\":997.25,\"sea_level\":1004.71,\"grnd_level\":997.25,\"humidity\":96,\"temp_kf\":0},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10n\"}],\"clouds\":{\"all\":92},\"wind\":{\"speed\":7.8,\"deg\":243.002},\"rain\":{\"3h\":1.49},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2018-09-23 21:00:00\"},{\"dt\":1537747200,\"main\":{\"temp\":10.02,\"temp_min\":10.02,\"temp_max\":10.02,\"pressure\":1003.84,\"sea_level\":1011.17,\"grnd_level\":1003.84,\"humidity\":100,\"temp_kf\":0},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10n\"}],\"clouds\":{\"all\":92},\"wind\":{\"speed\":12.56,\"deg\":326.003},\"rain\":{\"3h\":0.51},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2018-09-24 00:00:00\"},{\"dt\":1537758000,\"main\":{\"temp\":10.17,\"temp_min\":10.17,\"temp_max\":10.17,\"pressure\":1013.35,\"sea_level\":1020.81,\"grnd_level\":1013.35,\"humidity\":88,\"temp_kf\":0},\"weather\":[{\"id\":500,\"main\":\"Rain\",\"description\":\"light rain\",\"icon\":\"10n\"}],\"clouds\":{\"all\":88},\"wind\":{\"speed\":11.91,\"deg\":335},\"rain\":{\"3h\":0.27},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2018-09-24 03:00:00\"},{\"dt\":1537768800,\"main\":{\"temp\":10.08,\"temp_min\":10.08,\"temp_max\":10.08,\"pressure\":1021.19,\"sea_level\":1028.71,\"grnd_level\":1021.19,\"humidity\":79,\"temp_kf\":0},\"weather\":[{\"id\":803,\"main\":\"Clouds\",\"description\":\"broken clouds\",\"icon\":\"04d\"}],\"clouds\":{\"all\":56},\"wind\":{\"speed\":9.51,\"deg\":337.502},\"rain\":{},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2018-09-24 06:00:00\"},{\"dt\":1537779600,\"main\":{\"temp\":11.51,\"temp_min\":11.51,\"temp_max\":11.51,\"pressure\":1027.22,\"sea_level\":1034.74,\"grnd_level\":1027.22,\"humidity\":75,\"temp_kf\":0},\"weather\":[{\"id\":801,\"main\":\"Clouds\",\"description\":\"few clouds\",\"icon\":\"02d\"}],\"clouds\":{\"all\":20},\"wind\":{\"speed\":8.22,\"deg\":343.001},\"rain\":{},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2018-09-24 09:00:00\"},{\"dt\":1537790400,\"main\":{\"temp\":13.06,\"temp_min\":13.06,\"temp_max\":13.06,\"pressure\":1030.99,\"sea_level\":1038.68,\"grnd_level\":1030.99,\"humidity\":68,\"temp_kf\":0},\"weather\":[{\"id\":801,\"main\":\"Clouds\",\"description\":\"few clouds\",\"icon\":\"02d\"}],\"clouds\":{\"all\":24},\"wind\":{\"speed\":7.21,\"deg\":349.004},\"rain\":{},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2018-09-24 12:00:00\"},{\"dt\":1537801200,\"main\":{\"temp\":13.37,\"temp_min\":13.37,\"temp_max\":13.37,\"pressure\":1033.63,\"sea_level\":1041.29,\"grnd_level\":1033.63,\"humidity\":61,\"temp_kf\":0},\"weather\":[{\"id\":802,\"main\":\"Clouds\",\"description\":\"scattered clouds\",\"icon\":\"03d\"}],\"clouds\":{\"all\":36},\"wind\":{\"speed\":6.37,\"deg\":351.004},\"rain\":{},\"sys\":{\"pod\":\"d\"},\"dt_txt\":\"2018-09-24 15:00:00\"},{\"dt\":1537812000,\"main\":{\"temp\":11.36,\"temp_min\":11.36,\"temp_max\":11.36,\"pressure\":1036.44,\"sea_level\":1044.11,\"grnd_level\":1036.44,\"humidity\":61,\"temp_kf\":0},\"weather\":[{\"id\":803,\"main\":\"Clouds\",\"description\":\"broken clouds\",\"icon\":\"04n\"}],\"clouds\":{\"all\":68},\"wind\":{\"speed\":5.46,\"deg\":354.502},\"rain\":{},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2018-09-24 18:00:00\"},{\"dt\":1537822800,\"main\":{\"temp\":9.28,\"temp_min\":9.28,\"temp_max\":9.28,\"pressure\":1038.93,\"sea_level\":1046.67,\"grnd_level\":1038.93,\"humidity\":68,\"temp_kf\":0},\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01n\"}],\"clouds\":{\"all\":0},\"wind\":{\"speed\":4.56,\"deg\":356.503},\"rain\":{},\"sys\":{\"pod\":\"n\"},\"dt_txt\":\"2018-09-24 21:00:00\"}],\"city\":{\"id\":2643743,\"name\":\"London\",\"coord\":{\"lat\":51.5085,\"lon\":-0.1258},\"country\":\"GB\",\"population\":1000000}}";
		Gson gson = new Gson();
		CurrentWeather cw = gson.fromJson(json1, CurrentWeather.class);
		Forecast fc = gson.fromJson(json2, Forecast.class);
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