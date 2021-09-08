package com.testinflux;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.WriteApi;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.reactive.InfluxDBClientReactive;
import com.testinflux.Entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1.0/market")
@AllArgsConstructor
@Slf4j
public class TestInfluxController {

  @Autowired private final InfluxDBClient influxDBClient;
  @Autowired private final InfluxDBClientReactive influxDBClientReactive;

  @PostMapping("/")
  public String createUser(@RequestBody UserEntity userEntity) {
    try (WriteApi writeApi = influxDBClient.getWriteApi()) {
      List<UserEntity> list = new ArrayList<>();

      list.add(
          UserEntity.builder()
              .time(Instant.ofEpochMilli(1511123456785L))
              .username("giang")
              .password(123456L)
              .age(22L)
              .build());

      list.add(
          UserEntity.builder()
              .time(Instant.ofEpochMilli(1511123456789L))
              .username("giang")
              .password(123456L)
              .age(22L)
              .build());

      list.add(
          UserEntity.builder()
              .time(Instant.ofEpochMilli(1511123456788L))
              .username("giang")
              .password(123456L)
              .age(22L)
              .build());

      list.add(
          UserEntity.builder()
              .time(Instant.ofEpochMilli(1511123456787L))
              .username("giang")
              .password(123456L)
              .age(22L)
              .build());
      System.out.println(list);
      writeApi.writeMeasurements(WritePrecision.NS, list);
      return "ok";
    }
  }

  @GetMapping("/")
  public ResponseEntity<?> getUser() {

    String query =
        "from(bucket: \"kss_market\") |> range(start: -30y) |> filter(fn: (r) => r._measurement == \"user\"" +
                " ) |> pivot( rowKey:[\"_time\"], columnKey: [\"_field\"], valueColumn: \"_value\" )";
    List<UserEntity> query1 = influxDBClient.getQueryApi().query(query, UserEntity.class);

    return ResponseEntity.status(HttpStatus.OK).body(query1);
  }

  @DeleteMapping("/")
  public String deleteUsers() {
    OffsetDateTime start = OffsetDateTime.now().minus(30, ChronoUnit.DAYS);
    OffsetDateTime stop = OffsetDateTime.now();
    influxDBClient.getDeleteApi().delete(start, stop, "_measurement=\"user\"", "kss_market", "kss");
    return "Ok";
  }

  //  @PostMapping("/")
  //  public ResponseEntity<?> createUser(@RequestBody UserEntity userEntity) {
  //    InfluxDB influxDb = influxDbUtils.getInfluxDB();
  //    Point builder =
  //        Point.measurementByPOJO(UserEntity.class)
  //            .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
  //            .addFieldsFromPOJO(userEntity)
  //            .build();
  //    influxDb.write(builder);
  //    return ResponseEntity.status(HttpStatus.OK).body("ok");
  //  }
  //
  //  @PostMapping("/multi")
  //  public ResponseEntity<?> createMultiUser(@RequestBody List<UserEntity> listDTO) {
  //    InfluxDB influxDb = influxDbUtils.getInfluxDB();
  //    listDTO.forEach(
  //        userEntity -> {
  //          influxDb.write(
  //              Point.measurementByPOJO(UserEntity.class)
  //                  .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
  //                  .addFieldsFromPOJO(userEntity)
  //                  .build());
  //        });
  //    return ResponseEntity.status(HttpStatus.OK).body("ok");
  //  }
  //
  //  @GetMapping("/")
  //  public ResponseEntity<?> getUser() {
  //    InfluxDB influxDb = influxDbUtils.getInfluxDB();
  //    Query query =
  //        BoundParameterQuery.QueryBuilder.newQuery("select * from \"user\"")
  //            .forDatabase("kss")
  //            .create();
  //    QueryResult result = influxDb.query(query);
  //
  //    InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
  //    List<UserEntity> users = resultMapper.toPOJO(result, UserEntity.class,
  // TimeUnit.MILLISECONDS);
  //    return ResponseEntity.status(HttpStatus.OK).body(users);
  //  }
  //
  //  @DeleteMapping("/{time}")
  //  public ResponseEntity<?> deleteUser(@PathVariable String time) {
  //    InfluxDB influxDb = influxDbUtils.getInfluxDB();
  //    BoundParameterQuery query =
  //        BoundParameterQuery.QueryBuilder.newQuery("delete from \"user\" where time=$time")
  //            .forDatabase("kss")
  //            .bind("time", time)
  //            .create();
  //    influxDb.query(query);
  //    return ResponseEntity.status(HttpStatus.OK).body("ok");
  //  }
  //
  //  @GetMapping("/{time}")
  //  public ResponseEntity<?> getSingleUser(@PathVariable String time) throws ParseException {
  //    InfluxDB influxDb = influxDbUtils.getInfluxDB();
  //    BoundParameterQuery query =
  //        BoundParameterQuery.QueryBuilder.newQuery("select * from \"user\" where time=$time")
  //            .forDatabase("kss")
  //            .bind("time", time)
  //            .create();
  //    QueryResult result = influxDb.query(query);
  //    InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
  //    UserEntity user = resultMapper.toPOJO(result, UserEntity.class,
  // TimeUnit.MILLISECONDS).get(0);
  //    return ResponseEntity.status(HttpStatus.OK).body(user);
  //  }
}
