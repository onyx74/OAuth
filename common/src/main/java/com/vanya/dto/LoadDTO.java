package com.vanya.dto;

import com.vanya.enums.LoadStatus;
import com.vanya.enums.TruckType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoadDTO {
    private Long loadId;
    private double startLongitude;
    private double startLatitude;
    private double finishLatitude;
    private double finishLongitude;
    private String username;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createDate;
    private LoadStatus loadStatus;
    private String startAddress;
    private String finishAddress;
    private TruckType truckType;
    private double weight;
    private boolean publicLoad;
    private long distance;
    private double price;//todo add accuracy to this

}
