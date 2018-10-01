/**
 * 
 */
package de.hhu.rucub100.bigdata2018.transformation;

import java.util.Date;
import java.util.Map;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.windowing.time.Time;

import de.hhu.rucub100.bigdata2018.source.data.CurrentWeather;
import de.hhu.rucub100.bigdata2018.utils.DataUtils;

/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Sep 29, 2018
 *
 */
public class ColdestCountryPer24h extends StreamTransformationBase<CurrentWeather, Tuple3<String, Float, Date>> {

	public static final int SERVING_SPEED = 24 * 4;
	
	public static ColdestCountryPer24h fromDataStream(DataStream<CurrentWeather> current) {
		return new ColdestCountryPer24h(current);
	}
	
	protected ColdestCountryPer24h(DataStream<CurrentWeather> current) {
		super(current);
	}

	@Override
	public DataStream<Tuple3<String, Float, Date>> apply() throws Exception {
		return this.data
				.map(new MapFunction<CurrentWeather, Tuple4<String, Float, Integer, Date>>() {
					private final Map<String, String> countryMap = DataUtils.getCountryMap();
					
					@Override
					public Tuple4<String, Float, Integer, Date> map(CurrentWeather value) throws Exception {
						return new Tuple4<String, Float, Integer, Date>(
								countryMap.get(value.getSys().getCountry()), 
								value.getMain().getTemp(),
								1,
								value.getDate());
					}
				})
				.keyBy(0)
				.timeWindow(Time.hours(24))
				.reduce(new ReduceFunction<Tuple4<String,Float,Integer, Date>>() {
					
					@Override
					public Tuple4<String, Float, Integer, Date> reduce(
							Tuple4<String, Float, Integer,Date> value1,
							Tuple4<String, Float, Integer, Date> value2) throws Exception {
						return new Tuple4<String, Float, Integer, Date>(
								value1.f0, 
								value1.f1 + value2.f1,
								value1.f2 + value2.f2,
								value1.f3.compareTo(value2.f3) <= 0 ? value1.f3 : value2.f3);
					}
				})
				.map(new MapFunction<Tuple4<String,Float,Integer, Date>, Tuple3<String, Float, Date>>() {

					@Override
					public Tuple3<String, Float, Date> map(Tuple4<String, Float, Integer, Date> value) throws Exception {
						return new Tuple3<String, Float, Date>(value.f0, value.f1 / value.f2, value.f3);
					}
				})
				.timeWindowAll(Time.hours(24))
				.minBy(1);
	}
}
