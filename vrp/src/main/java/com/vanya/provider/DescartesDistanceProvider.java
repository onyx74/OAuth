/*
 * Copyright (c) 2018 Tideworks Technology, Inc.
 */
package com.vanya.provider;

import org.springframework.stereotype.Component;

/**
 * @author ivan.hladush(ihladush)
 */
@Component("DescartesDistanceProvider")
public class DescartesDistanceProvider extends DistanceProvider {

    @Override
    public double distance(final double startLat, final double startLng, final double finishLat,
                           final double finishLng) {
        return Math.sqrt((finishLat - startLat) * (finishLat - startLat) +
                (finishLng - startLng) * (finishLng - startLng));
    }
}
