package com.vanya.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;


@Component
@Lazy
@Slf4j
public class GoogleApiUtils {
    @Value("${google.maps.geocoding.key}")
    private String googleMapsGeocodeKey;
    @Value("${google.maps.distance.key}")
    private String googleMapsDistanceKey;

    public LatLng geocod(String address) {
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(googleMapsGeocodeKey)
                .build();
        try {
            GeocodingResult[] results = GeocodingApi.geocode(context,
                    address).await();
            return results[0].geometry.location;
        } catch (Exception e) {
            log.error("Error during geocode ", e);
            throw new RuntimeException(e);
        }
    }

    public String decoding(LatLng latLng) {
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(googleMapsGeocodeKey)
                .build();
        try {
            GeocodingResult[] results = GeocodingApi.reverseGeocode(context,
                    latLng).await();
            return results[0].formattedAddress;
        } catch (Exception e) {
            log.error("Error during reverse geocode", e);
            throw new RuntimeException(e);
        }
    }

    public long getDistance(LatLng firstPlace, LatLng secondPlace) {
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(googleMapsDistanceKey)
                .build();
        try {
            DistanceMatrix results = DistanceMatrixApi.getDistanceMatrix(context, new String[]{firstPlace.toString()},
                    new String[]{secondPlace.toString()}).await();
            return results.rows[0].elements[0].distance.inMeters;
        } catch (Exception e) {
            log.error("Error during reverse geocode", e);
//            throw new RuntimeException(e);
        }
        return 0;
    }

}
