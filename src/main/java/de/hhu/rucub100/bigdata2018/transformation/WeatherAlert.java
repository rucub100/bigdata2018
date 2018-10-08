/**
 * 
 */
package de.hhu.rucub100.bigdata2018.transformation;

import java.util.Date;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import de.hhu.rucub100.bigdata2018.source.data.CurrentWeather;

/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Sep 29, 2018
 *
 * Weather alert observing changes for temperature, wind speed, humidity and rain (via static thresholds, computation over multiple values).
 */
public class WeatherAlert extends StreamTransformationBase<CurrentWeather, String> {

	public static final int SERVING_SPEED = 8 * 5;
	
	public static WeatherAlert fromDataStream(DataStream<CurrentWeather> current) {
		return new WeatherAlert(current);
	}
	
	protected WeatherAlert(DataStream<CurrentWeather> current) {
		super(current);
	}

	@Override
	public DataStream<String> apply() throws Exception {
		return this.data
				.keyBy((CurrentWeather cw) -> cw.getName())
				.timeWindow(Time.hours(2))
				.process(new ProcessWindowFunction<CurrentWeather, String, String, TimeWindow>() {

					// states to detect changes
					private ValueState<Float> tempState;
					private ValueState<Float> windState;
					private ValueState<Float> humidityState;
					private ValueState<Boolean> rainingState;

					@Override
					public void open(Configuration parameters) throws Exception {
						tempState = getRuntimeContext().getState(
								new ValueStateDescriptor<Float>("temp", Float.class));
						windState = getRuntimeContext().getState(
								new ValueStateDescriptor<Float>("wind", Float.class));
						humidityState = getRuntimeContext().getState(
								new ValueStateDescriptor<Float>("humidity", Float.class));
						rainingState = getRuntimeContext().getState(
								new ValueStateDescriptor<Boolean>("raining", Boolean.class));
					}
					
					@Override
					public void process(String key,
							Context ctx,
							Iterable<CurrentWeather> elements, 
							Collector<String> out) throws Exception {
						
						float temp = 0.0f;
						float wind = 0.0f;
						float humidity = 0.0f;
						float rain = 0.0f;
						Date last = null;
						
						int cnt = 0;
						
						for (CurrentWeather cw : elements) {
							temp += cw.getMain().getTemp();
							wind += cw.getWind().getSpeed();
							humidity += cw.getMain().getHumidity();
							rain += cw.getRain() != null ? cw.getRain().get_3h() : 0.0f;
							
							if (last == null || last.compareTo(cw.getDate()) <= 0) {
								last = cw.getDate();
							}
							
							cnt++;
						}
						
						if (cnt > 0) {
							// normalize the data
							temp /= cnt;
							wind /= cnt;
							humidity /= cnt;
							rain /= cnt;
							
							if (tempState.value() != null) {
								float t = tempState.value();
								float w = windState.value();
								float h = humidityState.value();
								boolean r = rainingState.value();
								
								boolean change = false;
								
								StringBuilder sb = new StringBuilder(key + " (" + last.toString() + "): ");
								
								if (Math.abs(t - temp) > 10.0f) {
									change = true;
									sb.append("\n");
									sb.append("\tTemperature delta: " + (temp - t));
								}
								
								if (Math.abs(w - wind) > 5.0f) {
									change = true;
									sb.append("\n");
									sb.append("\tWind delta: " + (wind - w) * 3.6f + "km/h");
								}
								
								if (Math.abs(h - humidity) > 40.0f) {
									change = true;
									sb.append("\n");
									sb.append("\tHumidity delta: " + (humidity - h) + "%");
								}
								
								if (r != (rain > 1.0f)) {
									change = true;
									sb.append("\n");
									if (rain > 1.0f) {
										sb.append("\tLight rain.");
									} else if (rain > 10.0f) {
										sb.append("\tHeavy rain.");
									} else if (rain > 100.0f) {
										sb.append("\tApocalyptic rain.");
									} else {										
										sb.append("\tRain has stopped.");
									}
								}
								
								if (change) {
									out.collect(sb.toString());
								}
							}
							
							// update states
							tempState.update(temp);
							windState.update(wind);
							humidityState.update(humidity);
							rainingState.update(rain > 1.0f);
						}
					}
				});
	}
}
