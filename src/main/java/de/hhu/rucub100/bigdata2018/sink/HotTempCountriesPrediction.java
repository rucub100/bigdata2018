/**
 * 
 */
package de.hhu.rucub100.bigdata2018.sink;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import de.hhu.rucub100.bigdata2018.source.data.Neighbors;
import de.hhu.rucub100.bigdata2018.utils.DataUtils;
import de.hhu.rucub100.bigdata2018.utils.GeoUtils;

/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Oct 7, 2018
 * 
 * Sink function to predict countries with hot temperature in the next 24 hours.
 */
public class HotTempCountriesPrediction implements SinkFunction<Tuple3<String,Float,Date>> {

	private static final long serialVersionUID = 1L;
	
	private final List<Tuple2<String, Float>> avg;
	private final Tuple3<String, String, Float> min;
	private final Tuple3<String, String, Float> max;
	private final List<Neighbors> neighbors;
	
	public HotTempCountriesPrediction(
			List<Tuple2<String, Float>> avg, 
			Tuple3<String, String, Float> min,
			Tuple3<String, String, Float> max,
			List<Neighbors> neighbors) {
		this.avg = avg;
		this.min = min;
		this.max = max;
		this.neighbors = neighbors;
	}

	/* (non-Javadoc)
	 * @see org.apache.flink.streaming.api.functions.sink.SinkFunction#invoke(java.lang.Object, org.apache.flink.streaming.api.functions.sink.SinkFunction.Context)
	 */
	@Override
	public void invoke(Tuple3<String, Float, Date> value, Context context) throws Exception {
		Set<String> c = new HashSet<String>();
		c.add(value.f0);
		
		for (Neighbors n : neighbors) {
			if (n.getConutry1().getName().equalsIgnoreCase(value.f0)) {
				c.add(n.getConutry2().getName());
			} else if (n.getConutry2().getName().equalsIgnoreCase(value.f0)) {
				c.add(n.getConutry1().getName());
			}
		}
		
		float bAvg = 0;
		
		for (Tuple2<String, Float> t : avg) {
			if (t.f0.equalsIgnoreCase(value.f0)) {
				bAvg = t.f1;
			}
			else if (GeoUtils.getDistance2(
					DataUtils.getCountry(t.f0).getList()[0].getCoord(), 
					DataUtils.getCountry(value.f0).getList()[0].getCoord()) < 800) {
				c.add(t.f0);
			}
		}
		
		// filter
		for (Tuple2<String, Float> t : avg) {
			if (c.contains(t.f0) && t.f1 < bAvg * 0.7f) {
				c.remove(t.f0);
			}
		}
		
		System.out.println(value.f2.toString() + " + 24h: " + c);
	}

}
