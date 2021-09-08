package com.testinflux.InfluxConfig;


import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.InfluxDBClientFactory;
import com.influxdb.client.InfluxDBClientOptions;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConditionalOnClass(InfluxDBClient.class)
@EnableConfigurationProperties(InfluxDB2Properties.class)
public class InfluxDB2AutoConfiguration extends AbstractInfluxDB2AutoConfiguration {

    public InfluxDB2AutoConfiguration(final InfluxDB2Properties properties,
                                      final ObjectProvider<InfluxDB2OkHttpClientBuilderProvider> builderProvider) {
        super(properties, builderProvider.getIfAvailable());
    }

    @Bean
    @ConditionalOnProperty("influx.url")
    @ConditionalOnMissingBean(InfluxDBClient.class)
    public InfluxDBClient influxDBClient() {

        InfluxDBClientOptions.Builder influxBuilder = makeBuilder();

        return InfluxDBClientFactory.create(influxBuilder.build()).setLogLevel(properties.getLogLevel());
    }
}