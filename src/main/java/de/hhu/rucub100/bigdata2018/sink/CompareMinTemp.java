/**
 * 
 */
package de.hhu.rucub100.bigdata2018.sink;

import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import de.hhu.rucub100.bigdata2018.source.data.Country;
import de.hhu.rucub100.bigdata2018.source.data.Neighbors;
import de.hhu.rucub100.bigdata2018.utils.DataUtils;
import de.hhu.rucub100.bigdata2018.utils.GeoUtils;

/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Oct 7, 2018
 *
 */
public class CompareMinTemp implements SinkFunction<Tuple3<String,Float,Date>> {

	private static final long serialVersionUID = 1L;

	private final Tuple3<String, String, Float> batchResult;
	private final Country bCountry;
	private final List<Neighbors> neighbors;
	
	public CompareMinTemp(
			Tuple3<String, String, Float> batchResult,
			Country bCountry,
			List<Neighbors> neighbors) {
		this.batchResult = batchResult;
		this.bCountry = bCountry;
		this.neighbors = neighbors;
	}
	
	@Override
	public void invoke(Tuple3<String, Float, Date> value, Context context) throws Exception {
		Country sCountry = DataUtils.getCountry(value.f0);
		
		if (bCountry.getName().equals(sCountry.getName())) {
			System.out.println("Country (" + value.f2 + "): " + value.f0 + "=match! - " + "batch-result: " + 
					batchResult.f2 + ", stream-result: " + value.f1);
		} else if (neighbors.stream().anyMatch(new Predicate<Neighbors>() {

			@Override
			public boolean test(Neighbors t) {
				if (t.getConutry1().getName().equalsIgnoreCase(bCountry.getName()) && 
						t.getConutry2().getName().equalsIgnoreCase(sCountry.getName())) {
					return true;
				}
				
				if (t.getConutry2().getName().equalsIgnoreCase(bCountry.getName()) && 
						t.getConutry1().getName().equalsIgnoreCase(sCountry.getName())) {
					return true;
				}
				
				return false;
			}
		})) {
			System.out.println("Country (" + value.f2 + "): neighbor: " + sCountry.getName() + " - " + 
					"batch-result: " +  batchResult.f2 + ", stream-result: " + value.f1);
		} else {
			// calculate distance via first city in list
			double dist = GeoUtils.getDistance2(
					bCountry.getList()[0].getCoord(), 
					sCountry.getList()[0].getCoord());
			
			System.out.println("Country (" + value.f2 + "): " + value.f0 + ", distance: " + String.valueOf(dist) + "km - " + 
					"batch-result: " +  batchResult.f2 + ", stream-result: " + value.f1);
		}
	}
}
