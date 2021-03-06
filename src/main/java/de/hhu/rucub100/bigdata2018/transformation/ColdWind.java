/**
 * 
 */
package de.hhu.rucub100.bigdata2018.transformation;

import java.util.Date;
import java.util.Iterator;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.java.tuple.Tuple4;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.functions.windowing.AllWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.util.Collector;

import de.hhu.rucub100.bigdata2018.source.data.CurrentWeather;

/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Sep 29, 2018
 *
 * Detection filter for cold wind (thresholds via constructor parameters)
 */
public class ColdWind extends StreamTransformationBase<CurrentWeather, Tuple4<String, Float, Float, Date>> {

	private static float tempThreshold;
	private static float windThreshold;
	
	public static final int SERVING_SPEED = 4 * 24;
	
	public static ColdWind fromDataStream(DataStream<CurrentWeather> current, float tempThreshold, float windThreshold) {
		return new ColdWind(current, tempThreshold, windThreshold);
	}
	
	protected ColdWind(DataStream<CurrentWeather> current, float tempThreshold, float windThreshold) {
		super(current);
		ColdWind.tempThreshold = tempThreshold;
		ColdWind.windThreshold = windThreshold;
	}

	@Override
	public DataStream<Tuple4<String, Float, Float, Date>> apply() throws Exception {
		return this.data
				.filter(new FilterFunction<CurrentWeather>() {
					
					@Override
					public boolean filter(CurrentWeather value) throws Exception {
						return value.getMain().getTemp() < tempThreshold
								&& value.getWind().getSpeed() > windThreshold;
					}
				})
				.countWindowAll(161)
				.apply(new AllWindowFunction<CurrentWeather, Tuple4<String, Float, Float, Date>, GlobalWindow>() {
					@Override
					public void apply(GlobalWindow window, Iterable<CurrentWeather> values,
							Collector<Tuple4<String, Float, Float, Date>> out) throws Exception {
						// TODO Auto-generated method stub
						Iterator<CurrentWeather> iterator = values.iterator();
						
						while (iterator.hasNext()) {
							CurrentWeather cw = iterator.next();
							out.collect(new Tuple4<String, Float, Float, Date>(
									cw.getName(), 
									cw.getMain().getTemp(), 
									cw.getWind().getSpeed(), 
									cw.getDate()));
						}
					}
				});
	}
}
