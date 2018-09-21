package de.hhu.rucub100.bigdata2018.source;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import de.hhu.rucub100.bigdata2018.source.data.CurrentWeather;

/**
 * 
 * @author Ruslan Curbanov, ruslan.curbanov@uni-duesseldorf.de, Sep 18, 2018
 * 
 */
public class CurrentWeatherSource implements SourceFunction<CurrentWeather> {

	private final String dataFilePath;
	private final int servingSpeed;
	
	private transient BufferedReader reader;
	private transient InputStream gzipStream;
	
	public CurrentWeatherSource(String dataFilePath, int servingSpeed) {
		this.dataFilePath = dataFilePath;
		this.servingSpeed = servingSpeed;
	}

	@Override
	public void run(SourceContext<CurrentWeather> ctx) throws Exception {
		gzipStream = new GZIPInputStream(new FileInputStream(dataFilePath));
		reader = new BufferedReader(new InputStreamReader(gzipStream, "UTF-8"));

		simulateUnorderedStream(ctx);

		this.reader.close();
		this.reader = null;
		this.gzipStream.close();
		this.gzipStream = null;
	}

	@Override
	public void cancel() {
		try {
			if (this.reader != null) {
				this.reader.close();
			}
			if (this.gzipStream != null) {
				this.gzipStream.close();
			}
		} catch(IOException ioe) {
			throw new RuntimeException("Could not cancel SourceFunction", ioe);
		} finally {
			this.reader = null;
			this.gzipStream = null;
		}
	}

	private void simulateUnorderedStream(SourceContext<CurrentWeather> ctx) {
		// TODO Auto-generated method stub
		throw new NotImplementedException("");
	}
}
