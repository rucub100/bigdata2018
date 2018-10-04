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
   * collecting data
   * batch processing
   * stream processing
   * comparison between online and offline computed statistics
   * options for making predictions
## Overview
### Data collection
### Online Analysis (stream processing)
### Offline Analysis (batch processing)
### Comparison
### Options for making predictions
### Visualization
