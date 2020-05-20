# Running performance tests with JMeter

## Prerequisites

- Install JMETER, see [Download JMETER](http://jmeter.apache.org/download_jmeter.cgi)
- The application to test started and its database must be populated, see [Bike Generator](../java/bike-generator/README.md)

## Run Stress Testing

Generate a csv with the URL parameters to tests, see [URL Parameters Generator](../java/url-parameter-generator/README.md)

Run the following command with `url_parameters.csv` as your generated csv file copied in `backend/load-testing`:

```shell script
cd backend/load-testing
jmeter -Jurl_parameters_filepath=url_parameters.csv -n -t stress.jmx -l stress_results.jtl -e -o stress_report
```
