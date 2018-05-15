package com.vanya.model;

import com.vanya.enums.TruckType;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "trucks")
public class TruckEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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
    private Date createDate;

}
