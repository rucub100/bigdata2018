/**
 * 
 */
package de.hhu.rucub100.bigdata2018.transformation;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.flink.api.common.functions.GroupReduceFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.operators.Order;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.aggregation.Aggregations;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.util.Collector;

import de.hhu.rucub100.bigdata2018.source.data.CurrentWeather;
import de.hhu.rucub100.bigdata2018.utils.DataUtils;

/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Sep 24, 2018
 *
 * (country, city, temperature)
 */
public class MinTemperatureDiffCountries extends BatchTransformationBase<CurrentWeather, Tuple2<String, Float>> {

	public static MinTemperatureDiffCountries fromDataSet(DataSet<CurrentWeather> current) {
		return new MinTemperatureDiffCountries(current);
	}
	
	protected MinTemperatureDiffCountries(DataSet<CurrentWeather> current) {
		super(current);
	}

	@Override
	public DataSet<Tuple2<String, Float>> apply() throws Exception {
		return this.data
				.map(new MapFunction<CurrentWeather, Tuple3<String, Float, Float>>() {

					private final Map<String, String> countryMap = DataUtils.getCountryMap();
					
					@Override
					public Tuple3<String, Float, Float> map(CurrentWeather value) throws Exception {
						return new Tuple3<String, Float, Float>(
								countryMap.get(value.getSys().getCountry()),
								value.getMain().getTemp(),
								value.getMain().getTemp());
					}
				})
				.groupBy(0)
				.reduce(new ReduceFunction<Tuple3<String,Float,Float>>() {
					
					@Override
					public Tuple3<String, Float, Float> reduce(
							Tuple3<String, Float, Float> value1, 
							Tuple3<String, Float, Float> value2)
							throws Exception {
						return new Tuple3<String, Float, Float>(
								value1.f0,
								value1.f1 < value2.f1 ? value1.f1 : value2.f1, 
								value1.f2 > value2.f2 ? value1.f2 : value2.f2);
					}
				})
				.map(new MapFunction<Tuple3<String,Float,Float>, Tuple2<String, Float>>() {

					@Override
					public Tuple2<String, Float> map(Tuple3<String, Float, Float> value) throws Exception {
						return new Tuple2<String, Float>(value.f0, value.f2 - value.f1);
					}
				})
				.reduceGroup(new GroupReduceFunction<Tuple2<String,Float>, Tuple2<String,Float>>() {

					@Override
					public void reduce(Iterable<Tuple2<String, Float>> values, Collector<Tuple2<String, Float>> out)
							throws Exception {
						SortedSet<Tuple2<String, Float>> sorted = new TreeSet<Tuple2<String, Float>>(
								new Comparator<Tuple2<String, Float>>() {

									@Override
									public int compare(Tuple2<String, Float> o1, Tuple2<String, Float> o2) {
										return o1.f1 < o2.f1 ? -1 : (o1.f1 > o2.f1 ? 1 : 0);
									}
						});
						
						for (Tuple2<String, Float> tuple : values) {
							sorted.add(tuple);
							
							if (sorted.size() > 5) {
								sorted.remove(sorted.last());
							}
						}
						
						for (Tuple2<String, Float> tuple : sorted) {
							out.collect(tuple);
						}
					}
				});
	}
}
