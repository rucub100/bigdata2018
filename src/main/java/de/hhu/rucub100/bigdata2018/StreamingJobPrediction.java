/**
 * 
 */
package de.hhu.rucub100.bigdata2018;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import de.hhu.rucub100.bigdata2018.source.data.CurrentWeather;
import de.hhu.rucub100.bigdata2018.transformation.AvgTemperaturePerCountry;
import de.hhu.rucub100.bigdata2018.transformation.ColdestCityInEurope;
import de.hhu.rucub100.bigdata2018.transformation.MaxTemperatureDiffEurope;
import de.hhu.rucub100.bigdata2018.transformation.MaxTemperatureEurope;
import de.hhu.rucub100.bigdata2018.transformation.MinTemperatureDiffCountries;
import de.hhu.rucub100.bigdata2018.utils.DataUtils;

/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Oct 3, 2018
 *
 */
public class StreamingJobPrediction {

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
		
		runPredictionMode(batchEnv, streamEnv);
	}

	private static void runPredictionMode(
			final ExecutionEnvironment batchEnv, 
			final StreamExecutionEnvironment streamEnv) {
		
		// consider all offline statistics for the prediction
		DataSet<CurrentWeather> cwSet = batchEnv.fromCollection(DataUtils.getOfflineCurrentWeather());
		MaxTemperatureEurope bMaxTempEu = MaxTemperatureEurope.fromDataSet(cwSet);
		MaxTemperatureDiffEurope bMaxTDiffEu = MaxTemperatureDiffEurope.fromDataSet(cwSet);
		ColdestCityInEurope bMinTCityEu = ColdestCityInEurope.fromDataSet(cwSet);
		MinTemperatureDiffCountries bTDiffCountries = MinTemperatureDiffCountries.fromDataSet(cwSet);
		AvgTemperaturePerCountry bAvgTPerCountry = AvgTemperaturePerCountry.fromDataSet(cwSet);
		
		throw new NotImplementedException("");
	}
}
