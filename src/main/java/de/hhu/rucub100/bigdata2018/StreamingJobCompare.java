/**
 * 
 */
package de.hhu.rucub100.bigdata2018;

import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.shaded.zookeeper.org.apache.zookeeper.server.UnimplementedRequestProcessor;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import de.hhu.rucub100.bigdata2018.source.CurrentWeatherSource;
import de.hhu.rucub100.bigdata2018.source.data.Country;
import de.hhu.rucub100.bigdata2018.source.data.CurrentWeather;
import de.hhu.rucub100.bigdata2018.source.data.Europe;
import de.hhu.rucub100.bigdata2018.source.data.Neighbors;
import de.hhu.rucub100.bigdata2018.transformation.AvgCountryTempPer24h;
import de.hhu.rucub100.bigdata2018.transformation.AvgTemperaturePerCountry;
import de.hhu.rucub100.bigdata2018.transformation.ColdestCityInEurope;
import de.hhu.rucub100.bigdata2018.transformation.ColdestCountryPer24h;
import de.hhu.rucub100.bigdata2018.transformation.HottestCountryPer24h;
import de.hhu.rucub100.bigdata2018.transformation.MaxTemperatureEurope;
import de.hhu.rucub100.bigdata2018.utils.DataUtils;
import de.hhu.rucub100.bigdata2018.utils.GeoUtils;

/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Oct 4, 2018
 *
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
				DataUtils.pathToCurrentWeatherData, 
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
		.addSink(new SinkFunction<Tuple3<String,Float,Date>>() {
			/* (non-Javadoc)
			 * @see org.apache.flink.streaming.api.functions.sink.SinkFunction#invoke(java.lang.Object, org.apache.flink.streaming.api.functions.sink.SinkFunction.Context)
			 */
			@Override
			public void invoke(Tuple3<String, Float, Date> value, Context context) throws Exception {
				String country = value.f0;
				float avgTemp = value.f1;
				Date d = value.f2;
				Tuple2<String, Float> batchTuple = null;
				
				for (Tuple2<String, Float> t : batchResult) {
					if (!t.f0.equals(country)) {
						continue;
					} else {
						batchTuple = t;
						break;
					}
				}
				
				if (batchTuple != null) {
					System.out.println("Country (" + d.toString() + "): " + country + ", batch-avg: " + batchTuple.f1 + 
							", stream-24h-avg: " + avgTemp + ", diff: " + Math.abs(avgTemp - batchTuple.f1));
				}
			}
			
		});
		
		streamEnv.execute("Flink Streaming");
	}
	
	private static void compareMaxTemp(
			final ExecutionEnvironment batchEnv,
			final StreamExecutionEnvironment streamEnv) throws Exception {
		
		CurrentWeatherSource cwSource = new CurrentWeatherSource(
				DataUtils.pathToCurrentWeatherData, 
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
		.addSink(new SinkFunction<Tuple3<String,Float,Date>>() {
			@Override
			public void invoke(Tuple3<String, Float, Date> value, Context context) throws Exception {
				Country sCountry = DataUtils.getCountry(value.f0);
				
				if (bCountry.getName().equals(sCountry.getName())) {
					System.out.println("Country (" + value.f2 + "): " + value.f0 + "=match! - " + "batch-result: " + 
							batchResult.f2 + ", stream-result: " + value.f1);
				} else if (neighbors.stream().anyMatch(new Predicate<Neighbors>() {

					@Override
					public boolean test(Neighbors t) {
						if (t.getConutry1().getName().equalsIgnoreCase(bCountry.getName()) && 
								t.getConutry2().getName().equalsIgnoreCase(sCountry.getName())) {
							return true;
						}
						
						if (t.getConutry2().getName().equalsIgnoreCase(bCountry.getName()) && 
								t.getConutry1().getName().equalsIgnoreCase(sCountry.getName())) {
							return true;
						}
						
						return false;
					}
				})) {
					System.out.println("Country (" + value.f2 + "): neighbor: " + sCountry.getName() + " - " + 
							"batch-result: " +  batchResult.f2 + ", stream-result: " + value.f1);
				} else {
					// calculate distance via first city in list
					double dist = GeoUtils.getDistance2(
							bCountry.getList()[0].getCoord(), 
							sCountry.getList()[0].getCoord());
					
					System.out.println("Country (" + value.f2 + "): " + value.f0 + ", distance: " + String.valueOf(dist) + "km - " + 
							"batch-result: " +  batchResult.f2 + ", stream-result: " + value.f1);
				}
			}
		});
		
		streamEnv.execute("Flink Streaming");
	}
	
	private static void compareMinTemp(
			final ExecutionEnvironment batchEnv,
			final StreamExecutionEnvironment streamEnv) throws Exception {
		
		CurrentWeatherSource cwSource = new CurrentWeatherSource(
				DataUtils.pathToCurrentWeatherData, 
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
		.addSink(new SinkFunction<Tuple3<String,Float,Date>>() {
			@Override
			public void invoke(Tuple3<String, Float, Date> value, Context context) throws Exception {
				Country sCountry = DataUtils.getCountry(value.f0);
				
				if (bCountry.getName().equals(sCountry.getName())) {
					System.out.println("Country (" + value.f2 + "): " + value.f0 + "=match! - " + "batch-result: " + 
							batchResult.f2 + ", stream-result: " + value.f1);
				} else if (neighbors.stream().anyMatch(new Predicate<Neighbors>() {

					@Override
					public boolean test(Neighbors t) {
						if (t.getConutry1().getName().equalsIgnoreCase(bCountry.getName()) && 
								t.getConutry2().getName().equalsIgnoreCase(sCountry.getName())) {
							return true;
						}
						
						if (t.getConutry2().getName().equalsIgnoreCase(bCountry.getName()) && 
								t.getConutry1().getName().equalsIgnoreCase(sCountry.getName())) {
							return true;
						}
						
						return false;
					}
				})) {
					System.out.println("Country (" + value.f2 + "): neighbor: " + sCountry.getName() + " - " + 
							"batch-result: " +  batchResult.f2 + ", stream-result: " + value.f1);
				} else {
					// calculate distance via first city in list
					double dist = GeoUtils.getDistance2(
							bCountry.getList()[0].getCoord(), 
							sCountry.getList()[0].getCoord());
					
					System.out.println("Country (" + value.f2 + "): " + value.f0 + ", distance: " + String.valueOf(dist) + "km - " + 
							"batch-result: " +  batchResult.f2 + ", stream-result: " + value.f1);
				}
			}
		});
		
		streamEnv.execute("Flink Streaming");
	}
}
