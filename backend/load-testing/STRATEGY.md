# Performance Strategy

## Expected Metrics

Nominal maximum simultaneous expected clients: 200
Maximum simultaneous clients during a peak of activity: 1000
99% of response time in nominal case less than: 200ms
95% of response time in stress test less than: 1500ms
Minimum requests per second: 400 req/sec

## Strategy

- With the maximum of clients during a peak of activity: find the minimum of resources required satisfying the response time and requests per second constraints
- Load testing with these minimal resources and record consumption of resources, verify that it satisfies response time and requests per seconds

## Request Generation

The tool `url-parameter-generator` will generate URL parameters used by the performance tool `JMeter` as a `.csv` file. 
JMeter will use randomly these parameters for each client.
JMeter handles the random `page_number` and `page_size` parameter, so a URL looks like: `http://localhost:8080/bikes?page=${page_number}&size=${page_size}&brands=TREK,CANYON&genders=WOMEN`

See [URL Parameters Generator](../java/url-parameter-generator/README.md)

## Run performance testing

To run with stress and load testing, see [Running performance tests with JMeter](JMETER.md)



