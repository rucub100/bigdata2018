/**
 * 
 */
package de.hhu.rucub100.bigdata2018.sink;

import java.util.Date;
import java.util.List;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Oct 7, 2018
 *
 */
public class CompareAvgTemp implements SinkFunction<Tuple3<String,Float,Date>> {

	private static final long serialVersionUID = 1L;
	
	private final List<Tuple2<String, Float>> avg;
	
	public CompareAvgTemp(List<Tuple2<String, Float>> avg) {
		this.avg = avg;
	}
	
	/* (non-Javadoc)
	 * @see org.apache.flink.streaming.api.functions.sink.SinkFunction#invoke(java.lang.Object, org.apache.flink.streaming.api.functions.sink.SinkFunction.Context)
	 */
	@Override
	public void invoke(Tuple3<String, Float, Date> value, Context context) throws Exception {
		String country = value.f0;
		float avgTemp = value.f1;
		Date d = value.f2;
		Tuple2<String, Float> batchTuple = null;
		
		for (Tuple2<String, Float> t : avg) {
			if (!t.f0.equals(country)) {
				continue;
			} else {
				batchTuple = t;
				break;
			}
		}
		
		if (batchTuple != null) {
			System.out.println("Country (" + d.toString() + "): " + country + ", batch-avg: " + batchTuple.f1 + 
					", stream-24h-avg: " + avgTemp + ", diff: " + Math.abs(avgTemp - batchTuple.f1));
		}
	}
}
