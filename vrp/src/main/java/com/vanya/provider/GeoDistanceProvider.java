/*
 * Copyright (c) 2018 Tideworks Technology, Inc.
 */
package com.vanya.provider;

/**
 * @author ivan.hladush(ihladush)
 * @since 0.11
 */
public class GeoDistanceProvider extends DistanceProvider {

    private static final double R = 6371e3;

    @Override
    public double distance(double startLat, double startLng, double finishLat, double finishLng) {
        double φ1 = Math.toRadians(startLat);
        double φ2 = Math.toRadians(finishLat);

        double Δφ = Math.toRadians(finishLat - startLat);
        double Δλ = Math.toRadians(finishLng - startLng);

        double a = Math.sin(Δφ / 2) * Math.sin(Δφ / 2) +
                Math.cos(φ1) * Math.cos(φ2) *
                        Math.sin(Δλ / 2) * Math.sin(Δλ / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

}
