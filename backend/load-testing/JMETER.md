# Running performance tests with JMeter

## Prerequisites

- Install JMETER, see [Download JMETER](http://jmeter.apache.org/download_jmeter.cgi)
- The application to test started and its database must be populated, see [Bike Generator](../java/bike-generator/README.md)

## Run Performance Tests

Generate a csv with the URL parameters to tests, see [URL Parameters Generator](../java/url-parameter-generator/README.md)

Run the following command with `url_parameters.csv` as your generated csv file copied in `backend/load-testing` to run stress test:

```shell script
cd backend/load-testing
jmeter -Jurl_parameters_filepath=url_parameters.csv -n -t stress.jmx -l stress_results.jtl -e -o stress_report
```

To run load tests:

```shell script
cd backend/load-testing
jmeter -Jurl_parameters_filepath=url_parameters.csv -n -t load.jmx -l load_results.jtl -e -o load_report
```

The following reports will be generated:

-`aggregate_default.csv`: Aggregation of responses for the Bike Page without filters
-`aggregate_with_filters.csv`: Aggregation of responses for the Bike Page with filters
-`result_tree.csv`: Detail for each query sent
