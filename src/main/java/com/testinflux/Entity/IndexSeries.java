package com.testinflux.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Measurement(name = "index_series")
public class IndexSeries {
  @Column(timestamp = true)
  private Instant time;

  @Column(tag = true)
  private String comGroupCode;

  private Double indexValue;

  @Column(tag = true)
  private String tradingDate;

  @Column(tag = true)
  private String timeRange;

  private Double indexChange;
  private Double percentIndexChange;
  private Double referenceIndex;
  private Double openIndex;
  private Double closeIndex;
  private Double highestIndex;
  private Double lowestIndex;
  private Long matchVolume;
  private Long matchValue;
  private Long totalMatchVolume;
  private Long totalMatchValue;
}
