/**
 * 
 */
package de.hhu.rucub100.bigdata2018.transformation;

import org.apache.flink.api.java.DataSet;

/**
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Sep 24, 2018
 *
 */
public abstract class BatchTransformationBase<IN, OUT> {

	protected final DataSet<IN> data;
	
	protected BatchTransformationBase(DataSet<IN> data) {
		this.data = data;
	}
	
	public abstract DataSet<OUT> apply() throws Exception;
}
