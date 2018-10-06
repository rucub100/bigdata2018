# bigdata2018
This Maven project is an adventure trip through the BigData thicket. The technology base is [Flink 1.6](https://ci.apache.org/projects/flink/flink-docs-release-1.6/), a popular framework for both, stream and batch analytics. The data was obtained from the [OpenWeatherMap-API](https://openweathermap.org/api).

At the beginning of this project, the decision was made to cover weather data from 38 european countries or 161 cities in total.
You can find the hand-crafted structure in the [europe.json](europe.json) file.

## System prerequisites
- Maven 3.5 or higher
- JDK >=1.8
- IDE like Eclipse
- **You will need an OpenWeatherMap API KEY**
- Apache Flink
## Setup
1. Clone the project with `git clone ...`
2. Create an `appid.txt` text-file (with the API KEY) in the project root directory; this is mandatory only for
   - data collection and
   - stream processing (not the simulation)
3. Build the project with `mvn clean package`
4. There are different classes with a main method for
   * collecting data - [Collector.java](src/main/java/de/hhu/rucub100/bigdata2018/source/Collector.java)
   * batch processing - [BatchJob.java](src/main/java/de/hhu/rucub100/bigdata2018/BatchJob.java)
   * stream processing - [StreamingJob.java](src/main/java/de/hhu/rucub100/bigdata2018/StreamingJob.java)
   * comparison between online and offline computed statistics - [StreamingJobCompare.java](src/main/java/de/hhu/rucub100/bigdata2018/StreamingJobCompare.java)
   * options for making predictions - [StreamingJobPrediction.java](src/main/java/de/hhu/rucub100/bigdata2018/StreamingJobPrediction.java)
## Overview
### Data collection
The data collection is implemented in [Collector.java](src/main/java/de/hhu/rucub100/bigdata2018/source/Collector.java) and can be executed via [current.sh](current.sh) for the current weather, or [forecast.sh](forecast.sh) for the 5 day / 3 hour forecast.

The collected data is a txt-file with respect to the json format per line. One collector call for the current weather will add the API response for all 161 cities at once. The test-data is compressed via `gzip -k --best currentWeatherCollection.txt` to [currentWeatherCollection.txt.gz](test/currentWeatherCollection.txt.gz) with the approximate compression factor of 9. The call is executed every 15 minutes, which can be achived e.g. via a crontab as follows:
```(bash)
*/15 * * * * /bin/bash /path/to/repo/current.sh >/dev/null
```
In case of forecast, the situation is similar. The JSON serializer/deserializer used here is [gson](https://github.com/google/gson).
### Online Analysis (stream processing)
### Offline Analysis (batch processing)
### Comparison
### Options for making predictions
### Visualization
