package de.hhu.rucub100.bigdata2018.source;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

/**
 * 
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Sep 18, 2018
 *
 * @param <T>
 */
public class OpenWeatherMapSource<T> implements SourceFunction<T> {

	@Override
	public void run(SourceContext<T> ctx) throws Exception {
		// TODO Auto-generated method stub
		throw new NotImplementedException("");
	}

	@Override
	public void cancel() {
		// TODO Auto-generated method stub
		throw new NotImplementedException("");
	}

}
