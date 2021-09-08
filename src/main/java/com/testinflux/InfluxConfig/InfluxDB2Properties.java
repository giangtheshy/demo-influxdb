
package com.testinflux.InfluxConfig;

import java.time.Duration;

import com.influxdb.LogLevel;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Data
@ConfigurationProperties(prefix = "influx")
public class InfluxDB2Properties {

    private static final int DEFAULT_TIMEOUT = 10_000;


    private String url;


    private String username;


    private String password;


    private String token;

    private String org;


    private String bucket;

    private LogLevel logLevel = LogLevel.NONE;


    private Duration readTimeout = Duration.ofMillis(DEFAULT_TIMEOUT);


    private Duration writeTimeout = Duration.ofMillis(DEFAULT_TIMEOUT);


    private Duration connectTimeout = Duration.ofMillis(DEFAULT_TIMEOUT);


}