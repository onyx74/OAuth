package com.vanya.provider;

import com.google.maps.model.LatLng;
import com.vanya.utils.GoogleApiUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("GoogleDistanceProvider")
public class GoogleDistanceProvider extends DistanceProvider {
    @Autowired
    private GoogleApiUtils googleApiUtils;

    @Override
    public double distance(double startLat, double startLng, double finishLat, double finishLng) {
        return googleApiUtils.getDistance(new LatLng(startLat, startLng), new LatLng(finishLat, finishLng));
    }
}
