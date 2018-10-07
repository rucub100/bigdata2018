/**
 * 
 */
package de.hhu.rucub100.bigdata2018.sink;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;


/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Oct 7, 2018
 *
 */
public class TemperatureRangePrediction implements SinkFunction<Tuple3<String,Float,Date>> {

	private final List<Tuple2<String, Float>> avg;
	private final Tuple3<String, String, Float> min;
	private final Tuple3<String, String, Float> max;
	
	public TemperatureRangePrediction(
			List<Tuple2<String, Float>> avg, 
			Tuple3<String, String, Float> min,
			Tuple3<String, String, Float> max) {
		this.avg = avg;
		this.min = min;
		this.max = max;
	}

	/* (non-Javadoc)
	 * @see org.apache.flink.streaming.api.functions.sink.SinkFunction#invoke(java.lang.Object, org.apache.flink.streaming.api.functions.sink.SinkFunction.Context)
	 */
	@Override
	public void invoke(Tuple3<String, Float, Date> value, Context context) throws Exception {
		// predict temperature range for the next 24h per country
		float c = value.f1;
		float d = 10.0f;
		
		for (Tuple2<String, Float> t : avg) {
			if (t.f0.equalsIgnoreCase(value.f0)) {
				d = Math.abs(c - t.f1);
			}
		}
		
		float l = Math.max(min.f2, c - d);
		float r = Math.min(max.f2, c + d);
		
		StringBuilder sbPrediction = new StringBuilder();
		sbPrediction.append("Prediction for ");
		sbPrediction.append(value.f0);
		sbPrediction.append("(" + value.f2 + " + 24h)");
		sbPrediction.append(": ");
		sbPrediction.append("min.: " + l + "°C, ");
		sbPrediction.append("max.: " + r + "°C, ");
		sbPrediction.append("now: " + c + "°C");
		
		System.out.println(sbPrediction.toString());
	}

}
