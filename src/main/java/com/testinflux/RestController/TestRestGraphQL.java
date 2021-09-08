package com.testinflux.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.testinflux.Entity.Contribution;
import com.testinflux.Entity.IndexSeries;
import com.testinflux.Entity.InfoChart;
import com.testinflux.Entity.LatestIndices;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Configuration
public class TestRestGraphQL {

  @Autowired private RestTemplate restTemplate;
  @Autowired private ObjectMapper objectMapper;
  @Autowired private Gson gson;

  @Bean
  public void startProvider() {
    log.info("start calling: {}", "SSI");
    //    getChartInfoSSI();
    //    getLatestIndicesSSI();
//        getIndexSeriesSSI();
    getContributionSSI();
  }

  private void getContributionSSI() {
    List<List<Contribution>> listMapContribute = new ArrayList<>();
  AtomicInteger counter= new AtomicInteger();
    listCode()
        .forEach(
            code -> {
              try {
                counter.addAndGet(1);
                log.info("calling Contribution {}/{}: {}", counter, listCode().size(), code);
                URL url =
                    new URL(
                        String.format(
                            "https://fiin-market.ssi.com.vn/MoneyFlow/GetContribution?language=vi&ComGroupCode=%s&time=1629098668796",
                            code));
                ResponseEntity<String> raw =
                    restTemplate.exchange(
                        url.toURI(), HttpMethod.GET, configRequestForSSI(), String.class);
                JSONObject response = new JSONObject(raw.getBody());
                if (response.has("items")) {
                  JSONArray data = response.getJSONArray("items");
                  List<Contribution> contributions =
                      convertToContribution((JSONObject) data.get(0), code);
                  listMapContribute.add(contributions);
                }

              } catch (MalformedURLException | URISyntaxException e) {
                e.printStackTrace();
              }
            });
    List<Contribution> contributions =
        listMapContribute.stream().flatMap(Collection::stream).collect(Collectors.toList());
    System.out.println(contributions.size());
  }

  private List<Contribution> convertToContribution(JSONObject contributionJson, String indexId) {
    List<Contribution> contrib1Day =
        getContributionFromObjectJson(contributionJson, "contrib1Day", indexId);
    List<Contribution> contrib5Day =
        getContributionFromObjectJson(contributionJson, "contrib5Day", indexId);
    List<Contribution> contrib10Day =
        getContributionFromObjectJson(contributionJson, "contrib10Day", indexId);
    List<Contribution> contrib20Day =
        getContributionFromObjectJson(contributionJson, "contrib20Day", indexId);

    return Stream.of(contrib1Day, contrib5Day, contrib10Day, contrib20Day)
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }

  private List<Contribution> getContributionFromObjectJson(
      JSONObject contributionObject, String key, String indexId) {
    JSONObject contrib1DayJson = contributionObject.getJSONObject(key);
    JSONArray gainers = contrib1DayJson.getJSONArray("topGainers");
    JSONArray losers = contrib1DayJson.getJSONArray("topLosers");
    List<Contribution> contributions = new ArrayList<>();
    for (int i = 0; i < gainers.length(); i++) {
      JSONObject gainerObject = (JSONObject) gainers.get(i);
      Contribution gainer = gson.fromJson(gainerObject.toString(), Contribution.class);
      gainer.setIndexId(indexId);
      gainer.setDays(key);
      gainer.setType("gainer");
      gainer.setTime(Instant.now());
      contributions.add(gainer);
    }
    for (int i = 0; i < losers.length(); i++) {
      JSONObject loserObject = (JSONObject) losers.get(i);
      Contribution loser = gson.fromJson(loserObject.toString(), Contribution.class);
      loser.setIndexId(indexId);
      loser.setDays(key);
      loser.setType("loser");
      loser.setTime(Instant.now());
      contributions.add(loser);
    }
    return contributions;
  }

  private void getLatestIndicesSSI() {

    try {

      URL url =
          new URL(
              "https://fiin-market.ssi.com.vn/MarketInDepth/GetLatestIndices?language=vi&pageSize=999999&status=1");
      ResponseEntity<String> raw =
          restTemplate.exchange(url.toURI(), HttpMethod.GET, configRequestForSSI(), String.class);
      JSONObject response = new JSONObject(raw.getBody());
      if (response.has("items")) {
        JSONArray data = response.getJSONArray("items");
        List<LatestIndices> latestIndicesList = convertToLatestIndices(data);
        System.out.println(latestIndicesList);
      }
    } catch (MalformedURLException | URISyntaxException e) {
      log.error(e.getMessage());
    }
  }

  private List<String> listCode() {
    return Arrays.asList(
        "VNINDEX",
        "VN30",
        "HNXIndex",
        "HNX30",
        "UpcomIndex",
        "VNXALL",
        "VN100",
        "VNALL",
        "VNCOND",
        "VNCONS",
        "VNDIAMOND",
        "VNENE",
        "VNFIN",
        "VNFINLEAD",
        "VNFINSELECT",
        "VNHEAL",
        "VNIND",
        "VNIT",
        "VNMAT",
        "VNMID",
        "VNREAL",
        "VNSML",
        "VNUTI",
        "VNX50");
  }

  private void getIndexSeriesSSI() {

    List<String> listTimeRange =
        Arrays.asList(
            "OneDay",
            "OneWeek",
            "OneMonth",
            "ThreeMonths",
            "SixMonths",
            "YearToDate",
            "OneYear",
            "ThreeYears",
            "FiveYears");

    List<List<IndexSeries>> listMapIndexSeries = new ArrayList<>();
    AtomicInteger counter= new AtomicInteger();
    for (String code : listCode()) {
        counter.addAndGet(1);
        log.info("calling IndexSeries {}/{}: {} ...",counter,listCode().size(),code);
      for (String time : listTimeRange) {
        String url =
            String.format(
                "https://fiin-market.ssi.com.vn/MarketInDepth/GetIndexSeries?language=vi&ComGroupCode=%s&TimeRange=%s&id=1",
                code, time);
        try {
          ResponseEntity<String> raw =
              restTemplate.exchange(url, HttpMethod.GET, configRequestForSSI(), String.class);
          JSONObject response = new JSONObject(raw.getBody());
          if (response.has("items")) {
            if (!response.isNull("items")) {

              JSONArray data = response.getJSONArray("items");
              List<IndexSeries> list = null;
              list = convertToIndexSeries(data, time);
              listMapIndexSeries.add(list);
            }
          }
        } catch (ParseException e) {
          e.printStackTrace();
        }
      }
    }
    List<IndexSeries> indexSeries =
        listMapIndexSeries.stream().flatMap(Collection::stream).collect(Collectors.toList());
    System.out.println(indexSeries.size());
  }

  private HttpEntity<String> configRequestForSSI() {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add("Referer", "https://iboard.ssi.com.vn");
    return new HttpEntity<>(httpHeaders);
  }

  private void getChartInfoSSI() {
    String fieldsQuery = buildFieldQuery();
    JSONObject variables = new JSONObject();
    JSONObject request = new JSONObject();
    JSONArray indexIds = new JSONArray();
    indexIds.put("VN30");
    indexIds.put("VNINDEX");
    indexIds.put("HNX30");
    indexIds.put("HNXIndex");
    indexIds.put("HNXUpcomIndex");
    indexIds.put("VNXALL");
    variables.put("indexIds", indexIds);

    request.put("operationName", "IndexDetail");
    request.put("variables", variables);
    StringBuilder query = new StringBuilder();
    query.append("query  IndexDetail($indexIds: [String!]!) { ").append(" ");
    query.append("indexRealtimeLatestByArray(indexIds: $indexIds)").append(fieldsQuery).append(" ");
    query.append("indexRealtimeFirstByArray(indexIds: $indexIds)").append(fieldsQuery).append(" ");
    query.append("}").append(" ");
    request.put("query", query.toString());
    String raw =
        restTemplate.postForObject(
            "https://gateway-iboard.ssi.com.vn/graphql", request.toMap(), String.class);
    JSONObject response = new JSONObject(raw);
    List<InfoChart> infoChartsFirst = null;
    List<InfoChart> infoChartsLast = null;
    if (response.has("data")) {
      JSONObject data = response.getJSONObject("data");
      if (data.has("indexRealtimeLatestByArray")) {

        JSONArray indexRealtimeLatestByArray = data.getJSONArray("indexRealtimeLatestByArray");
        infoChartsLast = convertToInfoChart(indexRealtimeLatestByArray, "last");
      }
      if (data.has("indexRealtimeFirstByArray")) {

        JSONArray indexRealtimeFirstByArray = data.getJSONArray("indexRealtimeFirstByArray");
        infoChartsFirst = convertToInfoChart(indexRealtimeFirstByArray, "first");
      }
    }
    assert infoChartsFirst != null;
    assert infoChartsLast != null;
    List<InfoChart> resultListInfoChart =
        Stream.concat(infoChartsFirst.stream(), infoChartsLast.stream())
            .collect(Collectors.toList());
  }

  private String buildFieldQuery() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append("{");
    stringBuilder.append("indexID").append(" ");
    stringBuilder.append("indexValue").append(" ");
    stringBuilder.append("allQty").append(" ");
    stringBuilder.append("allValue").append(" ");
    stringBuilder.append("totalQtty").append(" ");
    stringBuilder.append("totalValue").append(" ");
    stringBuilder.append("advances").append(" ");
    stringBuilder.append("declines").append(" ");
    stringBuilder.append("nochanges").append(" ");
    stringBuilder.append("ceiling").append(" ");
    stringBuilder.append("floor").append(" ");
    stringBuilder.append("__typename").append(" ");
    stringBuilder.append("}");

    return stringBuilder.toString();
  }

  private List<InfoChart> convertToInfoChart(JSONArray listJson, String order) {
    List<InfoChart> listInfoChart = new ArrayList<>();
    for (int i = 0; i < listJson.length(); i++) {
      JSONObject infoChartJson = (JSONObject) listJson.get(i);
      InfoChart infoChart = gson.fromJson(infoChartJson.toString(), InfoChart.class);
      infoChart.setTime(Instant.now());
      infoChart.setOrder(order);
      listInfoChart.add(infoChart);
    }
    return listInfoChart;
  }

  private List<LatestIndices> convertToLatestIndices(JSONArray listJson) {
    List<LatestIndices> latestIndicesList = new ArrayList<>();
    for (int i = 0; i < listJson.length(); i++) {
      JSONObject latestIndicesJson = (JSONObject) listJson.get(i);
      LatestIndices latestIndices =
          gson.fromJson(latestIndicesJson.toString(), LatestIndices.class);
      latestIndices.setTime(Instant.now());
      latestIndicesList.add(latestIndices);
    }
    return latestIndicesList;
  }

  private List<IndexSeries> convertToIndexSeries(JSONArray listJson, String time)
      throws ParseException {
    List<IndexSeries> indexSeriesList = new ArrayList<>();
    for (int i = 0; i < listJson.length(); i++) {
      JSONObject indexSeriesJson = (JSONObject) listJson.get(i);
      IndexSeries indexSeries = gson.fromJson(indexSeriesJson.toString(), IndexSeries.class);
      Instant date = Instant.parse(indexSeries.getTradingDate() + "Z");
      indexSeries.setTime(date);
      indexSeries.setTimeRange(time);
      indexSeriesList.add(indexSeries);
    }
    return indexSeriesList;
  }
}
