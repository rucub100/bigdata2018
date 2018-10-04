/**
 * 
 */
package de.hhu.rucub100.bigdata2018;

import java.util.Date;
import java.util.List;

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import de.hhu.rucub100.bigdata2018.source.CurrentWeatherSource;
import de.hhu.rucub100.bigdata2018.source.data.CurrentWeather;
import de.hhu.rucub100.bigdata2018.transformation.AvgCountryTempPer24h;
import de.hhu.rucub100.bigdata2018.transformation.AvgTemperaturePerCountry;
import de.hhu.rucub100.bigdata2018.utils.DataUtils;

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
	}
	
	public static void compareAvgTemp(
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
}
