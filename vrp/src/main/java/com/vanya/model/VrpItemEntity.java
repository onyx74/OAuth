package com.vanya.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vrp_items")
public class VrpItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long vrpItemId;

    private long loadId;
    private long solutionId;
    private int position;

    @ManyToOne
    private VrpEntity vrp;

    private double startLongitude;
    private double startLatitude;
    private double finishLatitude;
    private double finishLongitude;

}
