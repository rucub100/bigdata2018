/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.hhu.rucub100.bigdata2018;

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.core.fs.FileSystem.WriteMode;

import de.hhu.rucub100.bigdata2018.source.data.CurrentWeather;
import de.hhu.rucub100.bigdata2018.transformation.AvgTemperaturePerCountry;
import de.hhu.rucub100.bigdata2018.transformation.ColdestCityInEurope;
import de.hhu.rucub100.bigdata2018.transformation.MaxTemperatureDiffEurope;
import de.hhu.rucub100.bigdata2018.transformation.MaxTemperatureEurope;
import de.hhu.rucub100.bigdata2018.transformation.MinTemperatureDiffCountries;
import de.hhu.rucub100.bigdata2018.utils.DataUtils;

/**
 * Batch job for some example transformations.
 *
 * <p>To package your application into a JAR file for execution,
 * change the main class in the POM.xml file to this class (simply search for 'mainClass')
 * and run 'mvn clean package' on the command line.
 */
public class BatchJob2 {

	private static final int PARALLELISM = 4;
	
	public static void main(String[] args) throws Exception {
		// set up the batch execution environment
		final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
		env.setParallelism(PARALLELISM);
		
		DataSet<CurrentWeather> current = env.fromCollection(DataUtils.getCurrentWeatherData());
		
		// save data to store it offline
		saveMaxTemperatureEurope(MaxTemperatureEurope.fromDataSet(current).apply());
		saveMaxTemperatureDiffEurope(MaxTemperatureDiffEurope.fromDataSet(current).apply());
		saveColdestCityInEurope(ColdestCityInEurope.fromDataSet(current).apply());
		saveMinTemperatureDiffCountries(MinTemperatureDiffCountries.fromDataSet(current).apply());
		saveAvgTemperaturePerCountry(AvgTemperaturePerCountry.fromDataSet(current).apply());
		
		// now this is needed, WTF, why?
		env.execute();
	}
	
	public static void saveMaxTemperatureEurope(DataSet<Tuple3<String, String, Float>> data) {
		data.writeAsText(DataUtils.pathToMaxTemperatureEuropeResult, WriteMode.OVERWRITE);
	}
	
	public static void saveMaxTemperatureDiffEurope(DataSet<Float> data) {
		data.writeAsText(DataUtils.pathToMaxTemperatureDiffEuropeResult, WriteMode.OVERWRITE);
	}

	public static void saveColdestCityInEurope(DataSet<Tuple3<String, String, Float>> data) {
		data.writeAsText(DataUtils.pathToColdestCityInEuropeResult, WriteMode.OVERWRITE);
	}
	
	public static void saveMinTemperatureDiffCountries(DataSet<Tuple2<String, Float>> data) {
		data.writeAsText(DataUtils.pathToMinTemperatureDiffCountriesResult, WriteMode.OVERWRITE);
	}
	
	public static void saveAvgTemperaturePerCountry(DataSet<Tuple2<String,Float>> data) {
		data.writeAsText(DataUtils.pathToAvgTemperaturePerCountryResult, WriteMode.OVERWRITE);
	}
}
