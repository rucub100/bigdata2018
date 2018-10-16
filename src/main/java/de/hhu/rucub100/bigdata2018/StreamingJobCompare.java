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

import de.hhu.rucub100.bigdata2018.sink.CompareAvgTemp;
import de.hhu.rucub100.bigdata2018.sink.CompareMaxTemp;
import de.hhu.rucub100.bigdata2018.sink.CompareMinTemp;
import de.hhu.rucub100.bigdata2018.source.CurrentWeatherSource;
import de.hhu.rucub100.bigdata2018.source.data.CurrentWeather;
import de.hhu.rucub100.bigdata2018.transformation.AvgCountryTempPer24h;
import de.hhu.rucub100.bigdata2018.transformation.ColdestCountryPer24h;
import de.hhu.rucub100.bigdata2018.transformation.HottestCountryPer24h;

/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Oct 4, 2018
 * 
 * Streaming job to compare offline values with online events.
 */
public class StreamingJobCompare {

	private static final int PARALLELISM = 4;
	
	public static void main(String[] args) throws Exception {
		// set up the streaming execution environment
		final StreamExecutionEnvironment streamEnv = StreamExecutionEnvironment
				.getExecutionEnvironment();
		streamEnv.setParallelism(PARALLELISM);
		streamEnv.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
		
		compareAvgTemp(streamEnv);
//		compareMaxTemp(streamEnv);
//		compareMinTemp(streamEnv);
	}
	
	private static void compareAvgTemp(final StreamExecutionEnvironment streamEnv) throws Exception {
		CurrentWeatherSource cwSource = new CurrentWeatherSource(
				AvgCountryTempPer24h.SERVING_SPEED, 
				true,
				true);
		
		DataStream<CurrentWeather> cwStream = streamEnv.addSource(cwSource);
		
		AvgCountryTempPer24h
		.fromDataStream(cwStream)
		.apply()
		.addSink(new CompareAvgTemp());
		
		streamEnv.execute();
	}
	
	private static void compareMaxTemp(final StreamExecutionEnvironment streamEnv) throws Exception {
		CurrentWeatherSource cwSource = new CurrentWeatherSource(
				HottestCountryPer24h.SERVING_SPEED, 
				true,
				true);
		
		DataStream<CurrentWeather> cwStream = streamEnv.addSource(cwSource);
		
//		Country bCountry = DataUtils.getCountry(maxTemperatureEurope.f0);
//		List<Neighbors> neighbors = DataUtils.getNeighbors();
		
		HottestCountryPer24h
		.fromDataStream(cwStream)
		.apply()
		.addSink(new CompareMaxTemp());
		
		streamEnv.execute();
	}
	
	private static void compareMinTemp(final StreamExecutionEnvironment streamEnv) throws Exception {
		
		CurrentWeatherSource cwSource = new CurrentWeatherSource(
				ColdestCountryPer24h.SERVING_SPEED, 
				true,
				true);
		DataStream<CurrentWeather> cwStream = streamEnv.addSource(cwSource);
		
//		Country bCountry = DataUtils.getCountry(cwSource.coldestCityInEurope.f0);
//		List<Neighbors> neighbors = DataUtils.getNeighbors();
		
		ColdestCountryPer24h
		.fromDataStream(cwStream)
		.apply()
		.addSink(new CompareMinTemp());
		
		streamEnv.execute();
	}
}
