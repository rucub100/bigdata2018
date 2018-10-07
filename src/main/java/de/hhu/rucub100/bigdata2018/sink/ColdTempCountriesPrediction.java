/**
 * 
 */
package de.hhu.rucub100.bigdata2018.sink;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;


/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Oct 7, 2018
 *
 */
public class ColdTempCountriesPrediction implements SinkFunction<Tuple3<String,Float,Date>> {

	private final List<Tuple2<String, Float>> avg;
	private final Tuple3<String, String, Float> min;
	private final Tuple3<String, String, Float> max;
	
	public ColdTempCountriesPrediction(
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
		throw new NotImplementedException("");
	}

}
