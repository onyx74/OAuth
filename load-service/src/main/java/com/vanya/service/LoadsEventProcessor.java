package com.vanya.service;

import com.google.maps.model.LatLng;
import com.vanya.model.entity.LoadEntity;
import com.vanya.model.event.NewLoadEvent;
import com.vanya.utils.GoogleApiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class LoadsEventProcessor implements ApplicationListener<NewLoadEvent> {
    @Autowired
    private LoadService loadService;
    @Autowired
    private GoogleApiUtils googleApiUtils;

    @Override
    public void onApplicationEvent(NewLoadEvent loadEvent) {
        LoadEntity loadEntity = loadEvent.getLoadEntity();
        Long loadId = loadEntity.getLoadId();
        LatLng startLocation = googleApiUtils.geocod(loadEntity.getStartAddress());
        LatLng finishLocation = googleApiUtils.geocod(loadEntity.getFinishAddress());
        long distance = googleApiUtils.getDistance(startLocation, finishLocation);

        loadService.updateLoadCoordinates(loadId, startLocation, finishLocation, distance);
    }
}
