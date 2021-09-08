package com.testinflux.Entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.influxdb.annotations.Column;
import com.influxdb.annotations.Measurement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(value = JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Measurement(name = "user")
public class UserEntity {

  @Column(name = "time", timestamp = true)
  private Instant time;

  @Column(name = "username", tag = true)
  private String username;

  @Column(name = "age")
  private Long age;

  @Column(name = "password")
  private Long password;




//  public void setTime(Long time) {
//    System.out.println(time);
//    this.time = Instant.ofEpochMilli(time);
//  }
//
}
