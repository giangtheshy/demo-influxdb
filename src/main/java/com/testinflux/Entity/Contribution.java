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
@Measurement(name = "contribution")
public class Contribution {
  @Column(timestamp = true)
  private Instant time;

  @Column(tag = true)
  private String indexId;

  @Column(tag = true)
  private String days;

  private String type;
  private String organCode;
  private String ticker;
  private Long rank;
  private Double contribution;
  private Double contributionPercent;
  private Long rtd11;
  private Long totalMatchVolume;
  private Long averageVolume1Week;
  private Double percentPriceChange;
  private Long priceChange;
  private Long matchPrice;
  private Long totalMatchValue;
}
