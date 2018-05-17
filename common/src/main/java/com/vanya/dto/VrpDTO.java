package com.vanya.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.Collection;

@Data
@NoArgsConstructor
public class VrpDTO {
    private long vrpId;
    private String owner;
    private String name;
    private String startLocation;
    private double startLatitude;
    private double startLongitude;
    private double totalDistance;
    private int position;
    private Date createDate;
    private Collection<VrpItemDTO> vrpItemEntities;
}
