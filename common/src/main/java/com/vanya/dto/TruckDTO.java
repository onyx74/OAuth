package com.vanya.dto;

import com.vanya.enums.TruckType;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;

@Data
public class TruckDTO {
    private long truckId;
    private String ownername;
    private String currentPossition;
    private TruckType truckType;
    private String carModel;
    private String driverName;
    private double latitude;
    private double longitude;
    private double pricePerMile;
    private boolean publicTruck;
    private int carrying;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createDate;
}
