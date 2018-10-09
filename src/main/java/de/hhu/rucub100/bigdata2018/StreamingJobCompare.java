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
import de.hhu.rucub100.bigdata2018.source.data.Country;
import de.hhu.rucub100.bigdata2018.source.data.CurrentWeather;
import de.hhu.rucub100.bigdata2018.source.data.Neighbors;
import de.hhu.rucub100.bigdata2018.transformation.AvgCountryTempPer24h;
import de.hhu.rucub100.bigdata2018.transformation.AvgTemperaturePerCountry;
import de.hhu.rucub100.bigdata2018.transformation.ColdestCityInEurope;
import de.hhu.rucub100.bigdata2018.transformation.ColdestCountryPer24h;
import de.hhu.rucub100.bigdata2018.transformation.HottestCountryPer24h;
import de.hhu.rucub100.bigdata2018.transformation.MaxTemperatureEurope;
import de.hhu.rucub100.bigdata2018.utils.DataUtils;

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
		
		// set up the batch execution environment
		final ExecutionEnvironment batchEnv = ExecutionEnvironment.getExecutionEnvironment();
		batchEnv.setParallelism(PARALLELISM);
		
		compareAvgTemp(batchEnv, streamEnv);
//		compareMaxTemp(batchEnv, streamEnv);
//		compareMinTemp(batchEnv, streamEnv);
	}
	
	private static void compareAvgTemp(
			final ExecutionEnvironment batchEnv,
			final StreamExecutionEnvironment streamEnv) throws Exception {
		
		CurrentWeatherSource cwSource = new CurrentWeatherSource(
				AvgCountryTempPer24h.SERVING_SPEED, 
				true,
				true);
		
		DataSet<CurrentWeather> cwSet = batchEnv.fromCollection(DataUtils.getOfflineCurrentWeather());
		DataStream<CurrentWeather> cwStream = streamEnv.addSource(cwSource);
		
		List<Tuple2<String, Float>> batchResult =  AvgTemperaturePerCountry
		.fromDataSet(cwSet)
		.apply()
		.collect();
		
		AvgCountryTempPer24h
		.fromDataStream(cwStream)
		.apply()
		.addSink(new CompareAvgTemp(batchResult));
		
		streamEnv.execute();
	}
	
	private static void compareMaxTemp(
			final ExecutionEnvironment batchEnv,
			final StreamExecutionEnvironment streamEnv) throws Exception {
		
		CurrentWeatherSource cwSource = new CurrentWeatherSource(
				HottestCountryPer24h.SERVING_SPEED, 
				true,
				true);
		
		DataSet<CurrentWeather> cwSet = batchEnv.fromCollection(DataUtils.getOfflineCurrentWeather());
		DataStream<CurrentWeather> cwStream = streamEnv.addSource(cwSource);
		
		Tuple3<String, String, Float> batchResult = MaxTemperatureEurope
		.fromDataSet(cwSet)
		.apply()
		.collect().get(0);
		
		System.out.println(batchResult);
		
		Country bCountry = DataUtils.getCountry(batchResult.f0);
		List<Neighbors> neighbors = DataUtils.getNeighbors();
		
		HottestCountryPer24h
		.fromDataStream(cwStream)
		.apply()
		.addSink(new CompareMaxTemp(batchResult, bCountry, neighbors));
		
		streamEnv.execute();
	}
	
	private static void compareMinTemp(
			final ExecutionEnvironment batchEnv,
			final StreamExecutionEnvironment streamEnv) throws Exception {
		
		CurrentWeatherSource cwSource = new CurrentWeatherSource(
				ColdestCountryPer24h.SERVING_SPEED, 
				true,
				true);
		
		DataSet<CurrentWeather> cwSet = batchEnv.fromCollection(DataUtils.getOfflineCurrentWeather());
		DataStream<CurrentWeather> cwStream = streamEnv.addSource(cwSource);
		
		Tuple3<String, String, Float> batchResult = ColdestCityInEurope
		.fromDataSet(cwSet)
		.apply()
		.collect().get(0);
		
		System.out.println(batchResult);
		
		Country bCountry = DataUtils.getCountry(batchResult.f0);
		List<Neighbors> neighbors = DataUtils.getNeighbors();
		
		ColdestCountryPer24h
		.fromDataStream(cwStream)
		.apply()
		.addSink(new CompareMinTemp(batchResult, bCountry, neighbors));
		
		streamEnv.execute();
	}
}
