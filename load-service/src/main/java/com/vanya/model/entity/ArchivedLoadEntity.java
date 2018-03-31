package com.vanya.model.entity;

import com.vanya.enums.LoadStatus;
import com.vanya.enums.TruckType;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "archived_loads")
public class ArchivedLoadEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long loadId;
    private double startLongitude;
    private double startLatitude;
    private double finishLatitude;
    private double finishLongitude;
    private String username;
    private Date createDate;
    private LoadStatus loadStatus;
    private String startAddress;
    private String finishAddress;
    private TruckType truckType;
    private double weight;
    private boolean publicLoad;
    private long distance;
    private double price;
    private Date deletedDate;
}
