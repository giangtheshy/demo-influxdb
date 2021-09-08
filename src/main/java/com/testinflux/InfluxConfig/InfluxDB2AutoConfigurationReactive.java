package com.testinflux.InfluxConfig;

import com.influxdb.client.InfluxDBClientOptions;
import com.influxdb.client.reactive.InfluxDBClientReactive;
import com.influxdb.client.reactive.InfluxDBClientReactiveFactory;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConditionalOnClass(name = "com.influxdb.client.reactive.InfluxDBClientReactive")
@EnableConfigurationProperties(InfluxDB2Properties.class)
public class InfluxDB2AutoConfigurationReactive extends AbstractInfluxDB2AutoConfiguration {

    public InfluxDB2AutoConfigurationReactive(final InfluxDB2Properties properties,
                                              final ObjectProvider<InfluxDB2OkHttpClientBuilderProvider>
                                                      builderProvider) {
        super(properties, builderProvider.getIfAvailable());
    }

    @Bean
    @ConditionalOnProperty("influx.url")
    @ConditionalOnMissingBean(InfluxDBClientReactive.class)
    public InfluxDBClientReactive influxDBClientReactive() {
        InfluxDBClientOptions.Builder influxBuilder = makeBuilder();

        return InfluxDBClientReactiveFactory.create(influxBuilder.build()).setLogLevel(properties.getLogLevel());
    }
}
