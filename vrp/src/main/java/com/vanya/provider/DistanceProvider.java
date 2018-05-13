package com.vanya.provider;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Multimap;
import com.google.common.collect.Table;
import com.vanya.dto.LoadDTO;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;

public abstract class DistanceProvider {

    public double calculateDistance(Pair<Double, Double> startPosition,
                                    Map<Long, LoadDTO> loads,
                                    Map<Long, Long> sequence) {
        final LoadDTO lastLoad = loads.get(sequence.get(sequence.size() - 1));
        double result = distance(startPosition.getKey(),
                startPosition.getValue(),
                lastLoad.getFinishLatitude(),
                lastLoad.getFinishLongitude());

        for (int i = 1; i < sequence.size(); ++i) {
            final LoadDTO load = loads.get(sequence.get(i));
            result += distance(startPosition, load) + load.getDistance();
            startPosition = new Pair<>(load.getFinishLatitude(), load.getFinishLongitude());
        }

        return result;
    }

    public double calculateDistance(Pair<Double, Double> startPosition,
                                    Multimap<Long, Long> sequences,
                                    Map<Long, LoadDTO> loads) {
        double result = 0;
        for (Long sequenceId : sequences.keySet()) {
            Pair<Double, Double> privious = startPosition;
            for (Long loadId : sequences.get(sequenceId)) {
                LoadDTO loadDTO = loads.get(loadId);
                result += distance(privious.getKey(), privious.getValue(), loadDTO.getStartLatitude(), loadDTO.getFinishLongitude());
                privious = new Pair<>(loadDTO.getStartLatitude(), loadDTO.getFinishLongitude());
            }
            result += distance(privious.getKey(), privious.getValue(), startPosition.getKey(), startPosition.getValue());
        }
        return result;
    }

    public double distance(Pair<Double, Double> startPosition, LoadDTO load) {
        return distance(startPosition.getKey(), startPosition.getValue(), load.getStartLatitude(), load.getStartLongitude()) +
                distance(load.getStartLatitude(), load.getStartLongitude(), load.getFinishLatitude(), load.getFinishLongitude());
    }

    public double distance(LoadDTO load1, LoadDTO load2) {
        return distance(load1.getFinishLatitude(), load1.getFinishLatitude(), load2.getStartLatitude(), load2.getStartLongitude()) +
                distance(load2.getStartLatitude(), load2.getStartLongitude(), load2.getFinishLatitude(), load2.getFinishLongitude());
    }

    public Table<Long, Long, Double> calculateDistance(Pair<Double, Double> startLocation, List<LoadDTO> loads) {
        HashBasedTable<Long, Long, Double> result = HashBasedTable.create();
        for (LoadDTO load1 : loads) {
            double distance = distance(startLocation.getKey(), startLocation.getValue(), load1.getStartLatitude(), load1.getStartLongitude());
            result.put(load1.getLoadId(), 0L, distance);
            result.put(0L, load1.getLoadId(), distance);
            for (LoadDTO load2 : loads) {
                distance = distance(load2.getStartLatitude(), load2.getStartLongitude(), load1.getStartLatitude(), load1.getStartLongitude());
                result.put(load1.getLoadId(), load2.getLoadId(), distance);
                result.put(load2.getLoadId(), load1.getLoadId(), distance);
            }
        }
        return result;

    }

    public abstract double distance(double startLat, double startLng, double finishLat, double finishLng);
}
