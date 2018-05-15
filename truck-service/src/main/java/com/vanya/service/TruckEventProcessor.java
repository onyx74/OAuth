package com.vanya.service;

import com.google.maps.model.LatLng;
import com.vanya.model.TruckEntity;
import com.vanya.model.event.NewTruckEvent;
import com.vanya.utils.GoogleApiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class TruckEventProcessor implements ApplicationListener<NewTruckEvent> {
    @Autowired
    private TruckService truckService;
    @Autowired
    private GoogleApiUtils googleApiUtils;

    @Override
    public void onApplicationEvent(NewTruckEvent truckEvent) {
        TruckEntity truckEntity = truckEvent.getTruckEntity();
        Long truckId = truckEntity.getTruckId();
        LatLng startLocation = googleApiUtils.geocod(truckEntity.getCurrentPossition());

        truckService.updateTruckCoordinates(truckId, startLocation);
    }
}
