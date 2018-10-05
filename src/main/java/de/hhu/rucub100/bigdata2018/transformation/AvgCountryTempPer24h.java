/**
 * 
 */
package de.hhu.rucub100.bigdata2018.transformation;

import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.functions.RichReduceFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.functions.windowing.AllWindowFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import de.hhu.rucub100.bigdata2018.source.data.CurrentWeather;
import de.hhu.rucub100.bigdata2018.utils.DataUtils;

/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Sep 29, 2018
 *
 */
public class AvgCountryTempPer24h extends StreamTransformationBase<CurrentWeather, Tuple3<String, Float, Date>> {
	
	public static final int SERVING_SPEED = 4 * 24;
	
	public static AvgCountryTempPer24h fromDataStream(DataStream<CurrentWeather> current) {
		return new AvgCountryTempPer24h(current);
	}
	
	protected AvgCountryTempPer24h(DataStream<CurrentWeather> current) {
		super(current);
	}

	@Override
	public DataStream<Tuple3<String, Float, Date>> apply() throws Exception {
		return this.data
				.map(new MapFunction<CurrentWeather, Tuple4<String, Float, Integer, Date>>() {
					@Override
					public Tuple4<String, Float, Integer, Date> map(CurrentWeather value) throws Exception {
						DataUtils.setCurrentWeatherTags(value);
						return new Tuple4<String, Float, Integer, Date>(
								value.getCountry().getName(), 
								value.getMain().getTemp(),
								1,
								value.getDate());
					}
				})
				.keyBy((Tuple4<String, Float, Integer, Date> t) -> t.f0)
				.timeWindow(Time.hours(24))
				.apply(new WindowFunction<Tuple4<String,Float,Integer,Date>, Tuple4<String,Float,Integer,Date>, String, TimeWindow>() {

					@Override
					public void apply(String key, TimeWindow window,
							Iterable<Tuple4<String, Float, Integer, Date>> input,
							Collector<Tuple4<String, Float, Integer, Date>> out) throws Exception {
						
						float t_sum = 0.0f;
						int cnt = 0;
						Date last = null;
						
						for (Tuple4<String, Float, Integer, Date> t : input) {
							t_sum += t.f1;
							cnt += t.f2;
							
							if (last == null || t.f3.compareTo(last) > 0) {
								last = t.f3;
							}
						}
						
						out.collect(new Tuple4<String, Float, Integer, Date>(key, t_sum, cnt, last));
					}
				})
				.map(new MapFunction<Tuple4<String,Float,Integer,Date>, Tuple3<String, Float, Date>>() {

					@Override
					public Tuple3<String, Float, Date> map(Tuple4<String, Float, Integer, Date> value)
							throws Exception {
						return new Tuple3<String, Float, Date>(value.f0,  value.f1 / value.f2, value.f3);
					}
				});
				
	}
}
