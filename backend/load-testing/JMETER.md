# Running performance tests with JMeter

## Prerequisites

Install JMETER, see [Download JMETER](http://jmeter.apache.org/download_jmeter.cgi)

## Run Stress Testing

Run the following command:

```shell script
jmeter -n -t stress.jmlx -l stress_results.jtl -e -o stress_report
```
