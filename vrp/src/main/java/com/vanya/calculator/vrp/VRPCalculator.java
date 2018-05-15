package com.vanya.calculator.vrp;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Table;
import org.springframework.data.geo.Distance;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class VRPCalculator {

    public Multimap<Long, Long> clarckSolver(Table<Long, Long, Double> distances) {
        final ArrayListMultimap<Long, Long> sequences = ArrayListMultimap.create();
        final Map<Long, Long> loadInSequence = new HashMap<>();
        long i = 0;
        for (Long loadId : distances.rowKeySet()) {
            if (loadId != 0L) {
                loadInSequence.put(loadId, i);
                sequences.put(i, loadId);
                ++i;
            }
        }

        Set<Pair<Double, Pair<Long, Long>>> savedDistances = calculateSafeDistance(distances);
        boolean improve = true;

        while (improve) {
            improve = false;
            Iterator<Pair<Double, Pair<Long, Long>>> iterator = savedDistances.iterator();
            while (iterator.hasNext()) {
                Pair<Double, Pair<Long, Long>> savedDistance = iterator.next();
                final Long firstLoadId = savedDistance.getSecond().getFirst();
                final Long secondLoadId = savedDistance.getSecond().getSecond();


                if (unionSequences(firstLoadId,
                        secondLoadId,
                        loadInSequence,
                        sequences)) {
                    improve = true;
                    iterator.remove();
                }
            }
        }

        return sequences;
    }

    protected boolean unionSequences(Long firstLoadId,
                                     Long secondLoadId,
                                     Map<Long, Long> loadInSequence,
                                     ArrayListMultimap<Long, Long> sequences) {
        int firstLoadPosition = loadPosition(firstLoadId, loadInSequence, sequences);
        int secondLoadPosition = loadPosition(secondLoadId, loadInSequence, sequences);
        if (firstLoadPosition == 0) {
            switch (secondLoadPosition) {
                case 0:
                    unionTwoSequence(firstLoadId, secondLoadId, loadInSequence, sequences);
                    break;
                case 1:
                    unionTwoSequence(firstLoadId, secondLoadId, loadInSequence, sequences);
                    break;
                case -1:
                    unionTwoSequence(secondLoadId, firstLoadId, loadInSequence, sequences);
                    break;
                default:
                    return false;
            }
        } else if (firstLoadPosition == 1) {
            switch (secondLoadPosition) {
                case 0:
                    unionTwoSequence(secondLoadId, firstLoadId, loadInSequence, sequences);
                    break;
                case 1:
                    revertSequence(firstLoadId, loadInSequence, sequences);
                    unionTwoSequence(firstLoadId, secondLoadId, loadInSequence, sequences);
                    break;
                case -1:
                    unionTwoSequence(secondLoadId, firstLoadId, loadInSequence, sequences);
                    break;
                default:
                    return false;
            }
        } else if (firstLoadPosition == -1) {
            switch (secondLoadPosition) {
                case 0:
                    unionTwoSequence(firstLoadId, secondLoadId, loadInSequence, sequences);
                    break;
                case 1:
                    unionTwoSequence(firstLoadId, secondLoadId, loadInSequence, sequences);
                    break;
                case -1:
                    revertSequence(firstLoadId, loadInSequence, sequences);
                    unionTwoSequence(secondLoadId, firstLoadId, loadInSequence, sequences);
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

    protected void revertSequence(Long loadId,
                                  Map<Long, Long> loadInSequence,
                                  ArrayListMultimap<Long, Long> sequences) {
        Long sequenceId = loadInSequence.get(loadId);
        List<Long> longs = sequences.removeAll(sequenceId);

        List<Long> reverse = Lists.reverse(longs);
        sequences.putAll(sequenceId, reverse);
    }

    /**
     * always add second sequence to first sequence
     */
    protected void unionTwoSequence(Long firstLoadId,
                                    Long secondLoadId,
                                    Map<Long, Long> loadInSequence,
                                    Multimap<Long, Long> sequences) {
        final long firstSequenceId = loadInSequence.get(firstLoadId);
        final long secondSequenceId = loadInSequence.get(secondLoadId);
        if (firstSequenceId == secondSequenceId) {
            return;
        }
        final Collection<Long> secondSequence = sequences.removeAll(secondSequenceId);

        sequences.putAll(firstSequenceId, secondSequence);


        for (Long loadId : secondSequence) {
            loadInSequence.put(loadId, firstSequenceId);
        }

    }

    /**
     * @return 0 - if load is singe load in sequence
     * 1 - if load is first load in sequence
     * -1 - if load is last load in sequence
     * 100 if load in all other cases
     */
    protected int loadPosition(Long loadId, Map<Long, Long> loadInSequence, ArrayListMultimap<Long, Long> sequences) {
        List<Long> loadSequence = sequences.get(loadInSequence.get(loadId));
        if (loadSequence.size() == 1) {
            return 0;
        }
        if (loadSequence.get(0).equals(loadId)) {
            return 1;
        }
        if (loadSequence.get(loadSequence.size() - 1).equals(loadId)) {
            return -1;
        }
        return 100;
    }

    protected Set<Pair<Double, Pair<Long, Long>>> calculateSafeDistance(Table<Long, Long, Double> distances) {
        Set<Pair<Double, Pair<Long, Long>>> savedDistances = new TreeSet<>((o1, o2) -> {
            if ((o1.getSecond().equals(o2.getSecond()) ||
                    o1.getSecond().equals(Pair.of(o2.getSecond().getSecond(), o2.getSecond().getFirst())))
                    && o1.getFirst().equals(o2.getFirst())) {
                return 0;
            }
            int z = (int) (o2.getFirst() - o1.getFirst());
            if (z == 0) {
                return 1;
            }
            return z;
        });
        for (long firstLoadId : distances.rowKeySet()) {
            if (firstLoadId == 0L) {
                continue;
            }
            for (long secondLoadId : distances.rowKeySet()) {
                if (secondLoadId == 0L || secondLoadId == firstLoadId) {
                    continue;
                }
                double save = distances.get(0L, firstLoadId) + distances.get(secondLoadId, 0L) - distances.get(firstLoadId, secondLoadId);
                if (save > 0) {
                    savedDistances.add(makePair(save, firstLoadId, secondLoadId));
                }

            }
        }
        return savedDistances;
    }

    protected static Pair<Double, Pair<Long, Long>> makePair(double distance, long firstLoadId, long secondLoadId) {
        return Pair.of(distance, Pair.of(firstLoadId, secondLoadId));
    }

}
