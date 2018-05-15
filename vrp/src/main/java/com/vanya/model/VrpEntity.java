package com.vanya.model;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.util.Collection;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vrp_results")
public class VrpEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long vrpId;
    private String owner;
    private String name;
    private double startLatitude;
    private double startLongitude;
    private Date createDate;
    @OneToMany(mappedBy = "vrp", cascade = CascadeType.ALL)
    private Collection<VrpItemEntity> vrpItemEntities;

}
