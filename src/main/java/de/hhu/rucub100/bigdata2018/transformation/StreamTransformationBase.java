/**
 * 
 */
package de.hhu.rucub100.bigdata2018.transformation;

import org.apache.flink.streaming.api.datastream.DataStream;

/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Sep 24, 2018
 * 
 * Base class for stream transformations.
 */
public abstract class StreamTransformationBase<IN, OUT> {

	protected final DataStream<IN> data;
	
	protected StreamTransformationBase(DataStream<IN> data) {
		this.data = data;
	}
	
	public abstract DataStream<OUT> apply() throws Exception;
}
