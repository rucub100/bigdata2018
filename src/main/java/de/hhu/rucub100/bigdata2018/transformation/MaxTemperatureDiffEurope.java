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
import org.apache.flink.api.java.tuple.Tuple4;

import de.hhu.rucub100.bigdata2018.source.data.CurrentWeather;
import de.hhu.rucub100.bigdata2018.utils.DataUtils;

/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Sep 24, 2018
 *
 * (country, city, temperature)
 */
public class MaxTemperatureDiffEurope extends BatchTransformationBase<CurrentWeather, Float> {

	public static MaxTemperatureDiffEurope fromDataSet(DataSet<CurrentWeather> current) {
		return new MaxTemperatureDiffEurope(current);
	}
	
	protected MaxTemperatureDiffEurope(DataSet<CurrentWeather> current) {
		super(current);
	}

	@Override
	public DataSet<Float> apply() throws Exception {
		return this.data
				.map(new MapFunction<CurrentWeather, Tuple2<Float, Float>>() {
					@Override
					public Tuple2<Float, Float> map(CurrentWeather value) throws Exception {
						return new Tuple2<Float, Float>(
								value.getMain().getTemp(),
								value.getMain().getTemp());
					}
				})
				.reduce(new ReduceFunction<Tuple2<Float, Float>>() {
					@Override
					public Tuple2<Float, Float> reduce(
							Tuple2<Float, Float> value1,
							Tuple2<Float, Float> value2) throws Exception {
						return new Tuple2<Float, Float>(
								value1.f0 < value2.f0 ? value1.f0 : value2.f0, 
								value1.f1 > value2.f1 ? value1.f1 : value2.f1);
					}
				}).map(new MapFunction<Tuple2<Float,Float>, Float>() {

					@Override
					public Float map(Tuple2<Float, Float> value) {
						return value.f1 - value.f0;
					}
				});
	}
}
