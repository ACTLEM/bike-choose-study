# Recording of resources

This section gives some tools to record the application's resources.
It mainly concerns the registration of the use of the containers.

## Resources to record

Here are the resources to be recorded for each container:

- CPU usage
- CPU energy consumption
- Memory usage
- Disk activity (i/o) 
- Network activity (i/o)

Note that the power consumption of memory and disk activity can be calculated theoretically.

## Record CPU/Memory/Disk Activity of containers

To record the resource usage, [cAdvisor](https://github.com/google/cadvisor) with [Prometheus](https://prometheus.io/) will be used.

To run them:

```shell script
docker-compose up -d
```

Then, go to `http://localhost:3000/`

Add Promotheus datasources (`http://monitoring_prometheus:9090`)

Import Dashboard (+ and import and copy `893`)

## Record CPU power consumption

[PowerAPI](http://powerapi.org/) will be used to record the CPU power consumption of the containers.

## Calculation of power consumption

The calculation of power consumption of memory and disk activity will be defined by formula.

Note that the results will be theoretical.
