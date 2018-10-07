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

import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import de.hhu.rucub100.bigdata2018.source.CurrentWeatherSource;
import de.hhu.rucub100.bigdata2018.source.data.CurrentWeather;
import de.hhu.rucub100.bigdata2018.transformation.AvgCountryTempPer24h;
import de.hhu.rucub100.bigdata2018.transformation.ColdWind;
import de.hhu.rucub100.bigdata2018.transformation.ColdestCountryPer24h;
import de.hhu.rucub100.bigdata2018.transformation.HotAndDry;
import de.hhu.rucub100.bigdata2018.transformation.HottestCountryPer24h;
import de.hhu.rucub100.bigdata2018.transformation.NeighborsDiff;
import de.hhu.rucub100.bigdata2018.transformation.WeatherAlert;
import de.hhu.rucub100.bigdata2018.utils.DataUtils;

/**
 * Skeleton for a Flink Streaming Job.
 *
 * <p>For a tutorial how to write a Flink streaming application, check the
 * tutorials and examples on the <a href="http://flink.apache.org/docs/stable/">Flink Website</a>.
 *
 * <p>To package your application into a JAR file for execution, run
 * 'mvn clean package' on the command line.
 *
 * <p>If you change the name of the main class (with the public static void main(String[] args))
 * method, change the respective entry in the POM.xml file (simply search for 'mainClass').
 */
public class StreamingJob {
	
	private static final int PARALLELISM = 4;
	
	public static void main(String[] args) throws Exception {
		// set up the streaming execution environment
		final StreamExecutionEnvironment env = StreamExecutionEnvironment
				.getExecutionEnvironment();
		env.setParallelism(PARALLELISM);
		env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
		
		DataStream<CurrentWeather> current = env.addSource(
				new CurrentWeatherSource(
						DataUtils.pathToCurrentWeatherData, 
						ColdestCountryPer24h.SERVING_SPEED, 
						true,
						false));
		
		// README: servingSpeed in CurrentWeatherSource is specific to the stream job, e.g. AvgCountryTempPer24h.SERVING_SPEED
		
		ColdestCountryPer24h
//		HottestCountryPer24h
//		AvgCountryTempPer24h
//		WeatherAlert
//		NeighborsDiff
//		ColdWind.fromDataStream(current, 10.0f, 10.0f)
//		HotAndDry.fromDataStream(current, 28.0f, 40.0f)
		.fromDataStream(current)
		.apply()
		.print();
		
		// execute program
		env.execute();
	}
}
