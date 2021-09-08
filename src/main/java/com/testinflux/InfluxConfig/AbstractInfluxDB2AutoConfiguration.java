package com.testinflux.InfluxConfig;

import java.util.Collections;
import javax.annotation.Nonnull;

import com.influxdb.client.InfluxDBClientOptions;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import org.springframework.util.StringUtils;

/**
 * @author Jakub Bednar (04/08/2021 11:41)
 */
abstract class AbstractInfluxDB2AutoConfiguration {
    protected final InfluxDB2Properties properties;
    protected final InfluxDB2OkHttpClientBuilderProvider builderProvider;

    protected AbstractInfluxDB2AutoConfiguration(final InfluxDB2Properties properties,
                                                 final InfluxDB2OkHttpClientBuilderProvider builderProvider) {
        this.properties = properties;
        this.builderProvider = builderProvider;
    }

    @Nonnull
    protected InfluxDBClientOptions.Builder makeBuilder() {
        OkHttpClient.Builder okHttpBuilder;
        if (builderProvider == null) {
            okHttpBuilder = new OkHttpClient.Builder()
                    .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                    .readTimeout(properties.getReadTimeout())
                    .writeTimeout(properties.getWriteTimeout())
                    .connectTimeout(properties.getConnectTimeout());
        } else {
            okHttpBuilder = builderProvider.get();
        }

        InfluxDBClientOptions.Builder influxBuilder = InfluxDBClientOptions.builder()
                .url(properties.getUrl())
                .bucket(properties.getBucket())
                .org(properties.getOrg())
                .okHttpClient(okHttpBuilder);

        if (StringUtils.hasLength(properties.getToken())) {
            influxBuilder.authenticateToken(properties.getToken().toCharArray());
        } else if (StringUtils.hasLength(properties.getUsername()) && StringUtils.hasLength(properties.getPassword())) {
            influxBuilder.authenticate(properties.getUsername(), properties.getPassword().toCharArray());
        }
        return influxBuilder;
    }
}
