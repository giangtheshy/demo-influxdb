package com.testinflux.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Measurement(name = "info_chart")
public class InfoChart {
  @Column(timestamp = true)
  private Instant time;

  @Column(tag = true)
  private String indexID;

  @Column(tag = true)
  private String order;

  private Double indexValue;
  private Long allQty;
  private Long allValue;
  private Long totalQtty;
  private Long totalValue;
  private Long advances;
  private Long declines;
  private Long nochanges;
  private Long ceiling;
  private Long floor;
  private String __typename;

}
