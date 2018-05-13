package com.vanya.calculator.tsp;

import com.google.common.collect.Table;
import com.vanya.dto.LoadDTO;
import javafx.util.Pair;

import java.util.List;
import java.util.Map;

/**
 * @author ivan.hladush(ihladush)
 * @since 0.11
 */
public interface TSPCalculator {

    Map<Integer, Long> calculate(Pair<Double, Double> startLocation, List<LoadDTO> loads);

    Map<Integer, Long> calculateViaDistance(final Table<Long, Long, Double> distances);
}
