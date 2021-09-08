package com.testinflux.InfluxConfig;


import java.util.function.Supplier;

import okhttp3.OkHttpClient;


@FunctionalInterface
public interface InfluxDB2OkHttpClientBuilderProvider extends Supplier<OkHttpClient.Builder> {

}