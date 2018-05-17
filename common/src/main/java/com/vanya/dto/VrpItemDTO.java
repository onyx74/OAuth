package com.vanya.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VrpItemDTO {
    private long vrpItemId;

    private long loadId;
    private long solutionId;
    private int position;

    private String startLocation;
    private String finishLocation;

    private double startLongitude;
    private double startLatitude;
    private double finishLatitude;
    private double finishLongitude;
}
