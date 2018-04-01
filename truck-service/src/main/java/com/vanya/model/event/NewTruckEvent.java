package com.vanya.model.event;

import com.vanya.model.TruckEntity;
import lombok.Data;
import org.springframework.context.ApplicationEvent;

@Data
public class NewTruckEvent extends ApplicationEvent {
    private TruckEntity truckEntity;

    public NewTruckEvent(TruckEntity truckEntity) {
        super(truckEntity);
        this.truckEntity = truckEntity;
    }
}