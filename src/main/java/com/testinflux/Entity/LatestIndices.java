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
@Measurement(name = "latest_indices")
public class LatestIndices {
    @Column(timestamp = true)
    private Instant time;
    @Column(tag = true)
    private String comGroupCode;
    private Long indexId;
    private Double indexValue;
    private String tradingDate;
    private Double indexChange;
    private Double percentIndexChange;
    private Double referenceIndex;
    private Double openIndex;
    private Double closeIndex;
    private Double highestIndex;
    private Double lowestIndex;
    private Long totalMatchVolume;
    private Long totalMatchValue;
    private Long totalDealVolume;
    private Long totalDealValue;
    private Long totalVolume;
    private Long totalValue;
    private Long totalStockUpPrice;
    private Long totalStockDownPrice;
    private Long totalStockNoChangePrice;
    private Long foreignBuyValueTotal;
    private Long foreignBuyVolumeTotal;
    private Long foreignSellValueTotal;
    private Long foreignSellVolumeTotal;
    private Long foreignTotalRoom;
    private Long foreignCurrentRoom;
    private Long totalStockUnderFloor;
    private Long totalStockOverCeiling;
    private Long matchVolume;
    private Long matchValue;
    private Long ceiling;
    private Long floor;
    private String marketStatus;
}
