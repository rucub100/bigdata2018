/**
 * 
 */
package de.hhu.rucub100.bigdata2018.transformation;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.functions.RichReduceFunction;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.api.java.tuple.Tuple5;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.functions.windowing.AllWindowFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessAllWindowFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import de.hhu.rucub100.bigdata2018.source.data.CurrentWeather;
import de.hhu.rucub100.bigdata2018.source.data.Neighbors;
import de.hhu.rucub100.bigdata2018.utils.DataUtils;

/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Oct 03, 2018
 *
 */
public class NeighborsDiff extends StreamTransformationBase<CurrentWeather, Tuple4<String, String, Float, Date>> {
	
	public static final int SERVING_SPEED = 4 * 24;
	
	public static NeighborsDiff fromDataStream(DataStream<CurrentWeather> current) {
		return new NeighborsDiff(current);
	}
	
	protected NeighborsDiff(DataStream<CurrentWeather> current) {
		super(current);
	}

	@Override
	public DataStream<Tuple4<String, String, Float, Date>> apply() throws Exception {
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
				.keyBy((Tuple4<String, Float, Integer, Date> t) -> t.f0)
				.timeWindow(Time.hours(24))
				.reduce(new ReduceFunction<Tuple4<String,Float,Integer,Date>>() {
					@Override
					public Tuple4<String, Float, Integer, Date> reduce(
							Tuple4<String, Float, Integer, Date> value1,
							Tuple4<String, Float, Integer, Date> value2) throws Exception {
						return new Tuple4<String, Float, Integer, Date>(
								value1.f0, 
								value1.f1 + value2.f1, 
								value1.f2 + value2.f2, 
								value1.f3.compareTo(value2.f3) > 0 ? value1.f3 : value2.f3);
								
					}
				})
				.map(new MapFunction<Tuple4<String,Float,Integer,Date>, Tuple3<String, Float, Date>>() {

					@Override
					public Tuple3<String, Float, Date> map(Tuple4<String, Float, Integer, Date> value)
							throws Exception {
						return new Tuple3<String, Float, Date>(value.f0, value.f1 / value.f2, value.f3);
					}
				})
				.timeWindowAll(Time.hours(24))
				.process(new ProcessAllWindowFunction<
						Tuple3<String,Float,Date>, 
						Tuple5<String,String,Float,Float,Date>, TimeWindow>() {

							private List<Neighbors> neighbors;
					
							
							
							/* (non-Javadoc)
							 * @see org.apache.flink.api.common.functions.AbstractRichFunction#open(org.apache.flink.configuration.Configuration)
							 */
							@Override
							public void open(Configuration parameters) throws Exception {
								neighbors = DataUtils.getNeighbors();
							}

							@Override
							public void process(
									Context ctx,
									Iterable<Tuple3<String, Float, Date>> elements,
									Collector<Tuple5<String, String, Float, Float, Date>> out) throws Exception {
								
								Map<Neighbors, Tuple5<String, String, Float, Float, Date>> map = 
										new HashMap<Neighbors, Tuple5<String, String, Float, Float, Date>>();

								for (Tuple3<String, Float, Date> t3 : elements) {
									for (Neighbors n : neighbors) {
										if (n.getConutry1().getName().equals(t3.f0)) {
											if (map.containsKey(n)) {
												map.get(n).f0 = t3.f0;
												map.get(n).f2 = t3.f1;
												
												if (t3.f2.compareTo(map.get(n).f4) > 0) {
													map.get(n).f4 = t3.f2;
												}
											} else {
												map.put(n, new Tuple5<String, String, Float, Float, Date>(
														t3.f0, 
														null, 
														t3.f1, 
														null, 
														t3.f2));
											}
										} else if (n.getConutry2().getName().equals(t3.f0)) {
											if (map.containsKey(n)) {
												map.get(n).f1 = t3.f0;
												map.get(n).f3 = t3.f1;
												
												if (t3.f2.compareTo(map.get(n).f4) > 0) {
													map.get(n).f4 = t3.f2;
												}
											} else {
												map.put(n, new Tuple5<String, String, Float, Float, Date>(
														null,
														t3.f0, 
														null, 
														t3.f1, 
														t3.f2));
											}
										}
									}
								}
								
								for (Neighbors n : map.keySet()) {
									out.collect(map.get(n));
								}
							}
				})
				.map(new MapFunction<Tuple5<String,String,Float,Float,Date>, Tuple4<String,String,Float,Date>>() {

					@Override
					public Tuple4<String, String, Float, Date> map(
							Tuple5<String, String, Float, Float, Date> value)
							throws Exception {
						return new Tuple4<String, String, Float, Date>(
								value.f0, 
								value.f1, 
								Math.abs(value.f2 - value.f3), 
								value.f4);
					}
				})
				.timeWindowAll(Time.hours(24))
				.maxBy(2);
	}
}
