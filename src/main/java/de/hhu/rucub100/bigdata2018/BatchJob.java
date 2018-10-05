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

import de.hhu.rucub100.bigdata2018.source.data.CurrentWeather;
import de.hhu.rucub100.bigdata2018.transformation.AvgTemperaturePerCountry;
import de.hhu.rucub100.bigdata2018.transformation.ColdestCityInEurope;
import de.hhu.rucub100.bigdata2018.transformation.MaxTemperatureDiffEurope;
import de.hhu.rucub100.bigdata2018.transformation.MaxTemperatureEurope;
import de.hhu.rucub100.bigdata2018.transformation.MinTemperatureDiffCountries;
import de.hhu.rucub100.bigdata2018.utils.DataUtils;

/**
 * Skeleton for a Flink Batch Job.
 *
 * <p>For a tutorial how to write a Flink batch application, check the
 * tutorials and examples on the <a href="http://flink.apache.org/docs/stable/">Flink Website</a>.
 *
 * <p>To package your application into a JAR file for execution,
 * change the main class in the POM.xml file to this class (simply search for 'mainClass')
 * and run 'mvn clean package' on the command line.
 */
public class BatchJob {

	private static final int PARALLELISM = 4;
	
	public static void main(String[] args) throws Exception {
		// set up the batch execution environment
		final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
		env.setParallelism(PARALLELISM);
		
		DataSet<CurrentWeather> current = env.fromCollection(DataUtils.getCurrentWeatherData());
		
		MaxTemperatureEurope
//		MaxTemperatureDiffEurope
//		ColdestCityInEurope
//		MinTemperatureDiffCountries
//		AvgTemperaturePerCountry
		.fromDataSet(current)
		.apply()
		.print();
	}
}
