# Performance Strategy

## Expected Metrics

Average simultaneous expected clients: 100
Maximum simultaneous clients: 1000
Maximum response time: 150ms
Minimum requests per second: 400 req/sec

## Strategy

- With the maximum of clients: find the minimum of resources required satisfying the response time and requests per second constraints
- Load testing with these minimal resources and record consumption of resources, verify that it satisfies response time and requests per seconds

## Request Generation

The tool `url-generator` will generate URLs used by the performance tool `JMeter` as a `.csv` file. 
JMeter will use randomly these URLs for each client.
Each URL has some filters, and a page number between 0 and 5 as it is uncommon to have clients clicking on pages greater than 5. 
JMeter handles the random `page-number` parameter, so a URL looks like: `http://localhost:8080/bikes?page={page-number}&brands=TREK,CANYON&genders=WOMEN`

See [URL Generator](../java/url-generator/README.md)

## Run performance testing

To run with stress and load testing, see [Running performance tests with JMeter](JMETER.md)



