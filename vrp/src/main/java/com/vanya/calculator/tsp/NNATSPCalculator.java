package com.vanya.calculator.tsp;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.vanya.dto.LoadDTO;
import javafx.util.Pair;
import com.vanya.provider.DistanceProvider;
import com.vanya.provider.GeoDistanceProvider;

import java.util.*;

/**
 * @author ivan.hladush(ihladush)
 * @since 0.11
 */
public class NNATSPCalculator implements TSPCalculator {

    private static DistanceProvider distanceProvider = new GeoDistanceProvider();

    @Override
    public Map<Integer, Long> calculate(Pair<Double, Double> startLocation, List<LoadDTO> loads) {
        final Table<Long, Long, Double> distances = HashBasedTable.create();

        for (LoadDTO load : loads) {
            final Long loadId = load.getLoadId();
            distances.put(0L, loadId, distanceProvider.distance(startLocation, load) + load.getDistance());
            distances.put(loadId, 0L, distanceProvider.distance(startLocation, load));
            final Pair<Double, Double> loadStartLocation = new Pair<>(load.getFinishLatitude(), load.getFinishLongitude());

            for (LoadDTO load1 : loads) {
                if (!load1.getLoadId().equals(loadId)) {
                    distances.put(loadId, load1.getLoadId(),
                            distanceProvider.distance(loadStartLocation, load1) + load1.getDistance());
                }
            }
        }

        return calculateViaDistance(distances);
    }

    @Override
    public Map<Integer, Long> calculateViaDistance(final Table<Long, Long, Double> distances) {
        final Map<Integer, Long> result = new HashMap<>();
        final Set<Long> used = new HashSet<>();

        long start = 0L;

        for (int i = 0; i < distances.size(); ++i) {
            used.add(start);
            result.put(i, start);
            start = findNotUsedMin(distances.row(start), used);
        }

        return result;
    }

    private Long findNotUsedMin(final Map<Long, Double> distances, Set<Long> used) {
        double minDistance = Integer.MAX_VALUE + 1.0;
        Long result = -1L;

        for (Map.Entry<Long, Double> entry : distances.entrySet()) {
            if (entry.getValue() <= minDistance && !used.contains(entry.getKey())) {
                result = entry.getKey();
                minDistance = entry.getValue();
            }
        }

        return result;
    }

}
