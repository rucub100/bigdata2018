/**
 * 
 */
package de.hhu.rucub100.bigdata2018.transformation;

import java.util.Map;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;

import de.hhu.rucub100.bigdata2018.source.data.CurrentWeather;
import de.hhu.rucub100.bigdata2018.utils.DataUtils;

/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Sep 24, 2018
 * 
 * Average temperature per country.
 * 
 * Input:  {@link CurrentWeather}
 * Output: {@link Tuple2<String, Float>} (country, average temperature)
 */
public class AvgTemperaturePerCountry extends BatchTransformationBase<CurrentWeather, Tuple2<String, Float>> {

	public static AvgTemperaturePerCountry fromDataSet(DataSet<CurrentWeather> current) {
		return new AvgTemperaturePerCountry(current);
	}
	
	private AvgTemperaturePerCountry(DataSet<CurrentWeather> current) {
		super(current);
	}

	@Override
	public DataSet<Tuple2<String, Float>> apply() throws Exception {
		return this.data
				.map(new MapFunction<CurrentWeather, Tuple3<String, Float, Integer>>() {
					private final Map<String, String> countryMap = DataUtils.getCountryMap();
					
					@Override
					public Tuple3<String, Float, Integer> map(CurrentWeather value) throws Exception {
						return new Tuple3<String, Float, Integer>(
								countryMap.get(value.getSystemInfo().getCountry()), 
								value.getMain().getTemperature(),
								1);
					}
				})
				.groupBy(0)
				.reduce(new ReduceFunction<Tuple3<String,Float,Integer>>() {
					
					@Override
					public Tuple3<String, Float, Integer> reduce(
							Tuple3<String, Float, Integer> value1,
							Tuple3<String, Float, Integer> value2) throws Exception {
						return new Tuple3<String, Float, Integer>(
								value1.f0, 
								value1.f1 + value2.f1,
								value1.f2 + value2.f2);
					}
				}).map(new MapFunction<Tuple3<String,Float,Integer>, Tuple2<String, Float>>() {

					@Override
					public Tuple2<String, Float> map(Tuple3<String, Float, Integer> value) throws Exception {
						return new Tuple2<String, Float>(value.f0, value.f1 / value.f2);
					}
				});
	}

}
