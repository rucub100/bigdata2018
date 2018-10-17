/**
 * 
 */
package de.hhu.rucub100.bigdata2018;

import java.util.List;

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import de.hhu.rucub100.bigdata2018.sink.ColdTempCountriesPrediction;
import de.hhu.rucub100.bigdata2018.sink.HotTempCountriesPrediction;
import de.hhu.rucub100.bigdata2018.sink.TemperatureRangePrediction;
import de.hhu.rucub100.bigdata2018.source.CurrentWeatherSource;
import de.hhu.rucub100.bigdata2018.source.data.CurrentWeather;
import de.hhu.rucub100.bigdata2018.source.data.Neighbors;
import de.hhu.rucub100.bigdata2018.transformation.AvgCountryTempPer24h;
import de.hhu.rucub100.bigdata2018.transformation.AvgTemperaturePerCountry;
import de.hhu.rucub100.bigdata2018.transformation.ColdestCityInEurope;
import de.hhu.rucub100.bigdata2018.transformation.ColdestCountryPer24h;
import de.hhu.rucub100.bigdata2018.transformation.HottestCountryPer24h;
import de.hhu.rucub100.bigdata2018.transformation.MaxTemperatureDiffEurope;
import de.hhu.rucub100.bigdata2018.transformation.MaxTemperatureEurope;
import de.hhu.rucub100.bigdata2018.transformation.MinTemperatureDiffCountries;
import de.hhu.rucub100.bigdata2018.utils.DataUtils;

/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Oct 3, 2018
 * 
 * Fun example prediction using offline values and streaming events.
 */
public class StreamingJobPrediction {

	private static final int PARALLELISM = 4;
	
	public static void main(String[] args) throws Exception {
		// set up the streaming execution environment
		final StreamExecutionEnvironment streamEnv = StreamExecutionEnvironment
				.getExecutionEnvironment();
		streamEnv.setParallelism(PARALLELISM);
		streamEnv.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
		
		runPredictionMode(streamEnv);
	}

	private static void runPredictionMode(
			final StreamExecutionEnvironment streamEnv) throws Exception {
		// predict temperature range for next 24h per country
		predictTemperatureRange(streamEnv);

		// predict list of countries for hottest temperature
//		predictHotTempCountries(streamEnv);
		
		// predict list of countries for coldest temperature
//		predictColdTempCountries(streamEnv);
	}

	private static void predictTemperatureRange(
			StreamExecutionEnvironment streamEnv) throws Exception {
		CurrentWeatherSource cwSource = new CurrentWeatherSource(
				AvgCountryTempPer24h.SERVING_SPEED, 
				true,
				true);
		
		DataStream<CurrentWeather> cwStream = streamEnv.addSource(cwSource);
		
		AvgCountryTempPer24h
		.fromDataStream(cwStream)
		.apply()
		.addSink(new TemperatureRangePrediction());
		
		streamEnv.execute();
	}
	
	private static void predictHotTempCountries(
			StreamExecutionEnvironment streamEnv) throws Exception {
		CurrentWeatherSource cwSource = new CurrentWeatherSource(
				HottestCountryPer24h.SERVING_SPEED, 
				true,
				true);
		
		DataStream<CurrentWeather> cwStream = streamEnv.addSource(cwSource);
		
		HottestCountryPer24h
		.fromDataStream(cwStream)
		.apply()
		.addSink(new HotTempCountriesPrediction());
		
		streamEnv.execute();
	}
	
	private static void predictColdTempCountries(
			StreamExecutionEnvironment streamEnv) throws Exception {
		CurrentWeatherSource cwSource = new CurrentWeatherSource(
				ColdestCountryPer24h.SERVING_SPEED, 
				true,
				true);
		
		DataStream<CurrentWeather> cwStream = streamEnv.addSource(cwSource);
		
		ColdestCountryPer24h
		.fromDataStream(cwStream)
		.apply()
		.addSink(new ColdTempCountriesPrediction());
		
		streamEnv.execute();
	}

}
