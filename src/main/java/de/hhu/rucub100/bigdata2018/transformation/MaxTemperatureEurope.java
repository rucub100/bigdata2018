/**
 * 
 */
package de.hhu.rucub100.bigdata2018.transformation;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.tuple.Tuple3;

import de.hhu.rucub100.bigdata2018.source.data.CurrentWeather;
import de.hhu.rucub100.bigdata2018.utils.DataUtils;

/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Sep 24, 2018
 *
 * The max temperature in Europe (country, city, temperature).
 */
public class MaxTemperatureEurope extends BatchTransformationBase<CurrentWeather, Tuple3<String, String, Float>> {

	public static MaxTemperatureEurope fromDataSet(DataSet<CurrentWeather> current) {
		return new MaxTemperatureEurope(current);
	}
	
	protected MaxTemperatureEurope(DataSet<CurrentWeather> current) {
		super(current);
	}

	@Override
	public DataSet<Tuple3<String, String, Float>> apply() throws Exception {
		return this.data
				.map(new MapFunction<CurrentWeather, Tuple3<String, String, Float>>() {
					@Override
					public Tuple3<String, String, Float> map(CurrentWeather value) throws Exception {
						DataUtils.setCurrentWeatherTags(value);
						return new Tuple3<String, String, Float>(
								value.getCountry().getName(),
								value.getName(), 
								value.getMain().getTemp());
					}
				})
				.reduce(new ReduceFunction<Tuple3<String, String, Float>>() {
					@Override
					public Tuple3<String, String, Float> reduce(Tuple3<String, String, Float> value1,
							Tuple3<String, String, Float> value2) throws Exception {
						return value1.f2 >= value2.f2 ? value1 : value2;
					}
				});
	}
}
