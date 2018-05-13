package com.vanya.calculator.vrp;

import com.google.common.collect.*;
import org.junit.Test;
import org.springframework.data.util.Pair;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class VRPCalculatorTest {
    private VRPCalculator calculator = new VRPCalculator();

    @Test
    public void testMakePair() {
        Pair<Double, Pair<Long, Long>> doublePairPair = VRPCalculator.makePair(3, 40L, 5432L);
        assertEquals(3.0, (double) doublePairPair.getFirst(), 0.00003);
        assertEquals(40L, (long) doublePairPair.getSecond().getFirst());
        assertEquals(5432L, (long) doublePairPair.getSecond().getSecond());
//        assertEq
    }

    @Test
    public void calculateSafeDistance() {
        final Table<Long, Long, Double> distances = HashBasedTable.create();
        distances.put(0L, 1L, 10.);
        distances.put(0L, 2L, 20.);
        distances.put(0L, 3L, 30.);

        distances.put(1L, 0L, 10.);
        distances.put(1L, 2L, 12.);
        distances.put(1L, 3L, 5.);

        distances.put(2L, 0L, 20.);
        distances.put(2L, 1L, 12.);
        distances.put(2L, 3L, 7.);

        distances.put(3L, 0L, 30.);
        distances.put(3L, 1L, 5.);
        distances.put(3L, 2L, 7.);
        Set<Pair<Double, Pair<Long, Long>>> pairs = calculator.calculateSafeDistance(distances);
        assertEquals(3, pairs.size());


        Pair<Double, Pair<Long, Long>>[] a = new Pair[]{};
        Pair<Double, Pair<Long, Long>>[] objects = pairs.toArray(a);
        Pair<Double, Pair<Long, Long>> first = objects[0];
        assertEquals(43.0, first.getFirst(), 0.01);
        assertEquals(2L, (long) first.getSecond().getFirst());
        assertEquals(3L, (long) first.getSecond().getSecond());

        Pair<Double, Pair<Long, Long>> second = objects[1];
        assertEquals(35, second.getFirst(), 0.01);
        assertEquals(1L, (long) second.getSecond().getFirst());
        assertEquals(3L, (long) second.getSecond().getSecond());

        Pair<Double, Pair<Long, Long>> third = objects[2];
        assertEquals(18.0, third.getFirst(), 0.01);
        assertEquals(1L, (long) third.getSecond().getFirst());
        assertEquals(2L, (long) third.getSecond().getSecond());

    }

    @Test
    public void testLoadPositionIsAlone() {
        HashMap<Long, Long> loadInSequence = new HashMap<>();
        loadInSequence.put(10L, 4L);
        ArrayListMultimap<Long, Long> sequences = ArrayListMultimap.create();
        sequences.put(4L, 10L);
        int position = calculator.loadPosition(10L, loadInSequence, sequences);
        assertEquals(0, position);
    }

    @Test
    public void testLoadPositionIsFirst() {
        HashMap<Long, Long> loadInSequence = new HashMap<>();
        loadInSequence.put(10L, 4L);
        ArrayListMultimap<Long, Long> sequences = ArrayListMultimap.create();
        sequences.put(4L, 10L);
        sequences.put(4L, 11L);
        sequences.put(4L, 12L);
        int position = calculator.loadPosition(10L, loadInSequence, sequences);
        assertEquals(1, position);
    }

    @Test
    public void testLoadPositionIsLast() {
        HashMap<Long, Long> loadInSequence = new HashMap<>();
        loadInSequence.put(10L, 4L);
        ArrayListMultimap<Long, Long> sequences = ArrayListMultimap.create();
        sequences.put(4L, 11L);
        sequences.put(4L, 12L);
        sequences.put(4L, 10L);
        int position = calculator.loadPosition(10L, loadInSequence, sequences);
        assertEquals(-1, position);
    }

    @Test
    public void testLoadPositionIsMiddle() {
        HashMap<Long, Long> loadInSequence = new HashMap<>();
        loadInSequence.put(10L, 4L);
        ArrayListMultimap<Long, Long> sequences = ArrayListMultimap.create();
        sequences.put(4L, 11L);
        sequences.put(4L, 10L);
        sequences.put(4L, 12L);
        int position = calculator.loadPosition(10L, loadInSequence, sequences);
        assertEquals(100, position);
    }

    @Test
    public void test_union_two_sequence() {
        HashMap<Long, Long> loadInSequence = new HashMap<>();
        loadInSequence.put(1L, 4L);
        loadInSequence.put(3L, 4L);
        loadInSequence.put(2L, 4L);
        loadInSequence.put(40L, 5L);
        ArrayListMultimap<Long, Long> sequences = ArrayListMultimap.create();
        sequences.put(4L, 3L);
        sequences.put(4L, 1L);
        sequences.put(4L, 2L);
        sequences.put(5L, 41L);
        sequences.put(5L, 40L);
        sequences.put(5L, 42L);
        calculator.unionTwoSequence(1L, 40L, loadInSequence, sequences);

        assertEquals(6, loadInSequence.size());
        assertEquals(4L, (long) loadInSequence.get(41L));
        assertEquals(4L, (long) loadInSequence.get(40L));
        assertEquals(4L, (long) loadInSequence.get(42L));
        assertEquals(4L, (long) loadInSequence.get(1L));
        assertEquals(4L, (long) loadInSequence.get(3L));
        assertEquals(4L, (long) loadInSequence.get(2L));

        assertFalse(sequences.containsKey(5L));
        List<Long> actual = sequences.get(4L);
        assertEquals(6, actual.size());
        ArrayList<Long> expect = Lists.newArrayList(3L, 1L, 2L, 41L, 40L, 42L);
        assertEquals(expect, actual);
    }

    @Test
    public void test_revert_sequence() {
        HashMap<Long, Long> loadInSequence = new HashMap<>();
        loadInSequence.put(1L, 4L);
        loadInSequence.put(3L, 4L);
        loadInSequence.put(2L, 4L);
        ArrayListMultimap<Long, Long> sequences = ArrayListMultimap.create();
        sequences.put(4L, 3L);
        sequences.put(4L, 1L);
        sequences.put(4L, 2L);
        calculator.revertSequence(3L, loadInSequence, sequences);
        List<Long> actual = sequences.get(4L);
        ArrayList<Long> expect = Lists.newArrayList(2L, 1L, 3L);
        assertEquals(expect, actual);
    }

    /**
     * 0|  1|  2|  3|  4|  5|  6|
     * 0| -  10   20  30  40  50  60
     * 1|10   -   4   5   70 100 200
     * 2|20  4    -   10 300 400 500
     * 3|30  5   10   -  500 500 500
     * 4|40  70 300 500   -   10  20
     * 5|50 100 400 500  10   -   30
     * 6|60 200 500 500  20   30   -
     */
    @Test
    public void test_vrp_clarck_calculator_first() {
        final Table<Long, Long, Double> distances = HashBasedTable.create();
        distances.put(0L, 1L, 10.);
        distances.put(0L, 2L, 20.);
        distances.put(0L, 3L, 30.);
        distances.put(0L, 4L, 40.);
        distances.put(0L, 5L, 50.);
        distances.put(0L, 6L, 60.);

        distances.put(1L, 0L, 10.);
        distances.put(1L, 2L, 17.);
        distances.put(1L, 3L, 5.);
        distances.put(1L, 4L, 70.);
        distances.put(1L, 5L, 100.);
        distances.put(1L, 6L, 200.);

        distances.put(2L, 0L, 20.);
        distances.put(2L, 1L, 17.);
        distances.put(2L, 3L, 10.);
        distances.put(2L, 4L, 300.);
        distances.put(2L, 5L, 400.);
        distances.put(2L, 6L, 500.);

        distances.put(3L, 0L, 30.);
        distances.put(3L, 1L, 5.);
        distances.put(3L, 2L, 10.);
        distances.put(3L, 4L, 500.);
        distances.put(3L, 5L, 500.);
        distances.put(3L, 6L, 500.);

        distances.put(4L, 0L, 40.);
        distances.put(4L, 1L, 70.);
        distances.put(4L, 2L, 300.);
        distances.put(4L, 3L, 500.);
        distances.put(4L, 5L, 10.);
        distances.put(4L, 6L, 20.);

        distances.put(5L, 0L, 50.);
        distances.put(5L, 1L, 100.);
        distances.put(5L, 2L, 400.);
        distances.put(5L, 3L, 500.);
        distances.put(5L, 4L, 10.);
        distances.put(5L, 6L, 300.);

        distances.put(6L, 0L, 60.);
        distances.put(6L, 1L, 200.);
        distances.put(6L, 2L, 500.);
        distances.put(6L, 3L, 500.);
        distances.put(6L, 4L, 200.);
        distances.put(6L, 5L, 300.);

        Multimap<Long, Long> actual = calculator.clarckSolver(distances);
        assertEquals(2, actual.keySet().size());
        Collection<Long> firstSequenceActual = actual.get(1L);
        ArrayList<Long> firstSequenceExpect = Lists.newArrayList(2L, 3L, 1L);
        assertEquals(firstSequenceExpect, firstSequenceActual);
        Collection<Long> secondSequenceActual = actual.get(5L);
        ArrayList<Long> secondSequenceExpect = Lists.newArrayList(6L, 4L, 5L);
        assertEquals(secondSequenceExpect, secondSequenceActual);
    }

    /**
     * 0|   1|   2|   3|   4|   5|   6|
     * 0| -  300   300  300  300  300  300
     * 1|300   -   10   900  900  900  900
     * 2|300  10    -   60   900  900  900
     * 3|300 900   60    -    10  900  900
     * 4|300 900  900    10   -    50  900
     * 5|300 900  900   900   50    -   10
     * 6|300 900  900   900  900   10    -
     */
    @Test
    public void test_vrp_clarck_calculator_second() {
        final Table<Long, Long, Double> distances = HashBasedTable.create();
        distances.put(0L, 1L, 300.);
        distances.put(0L, 2L, 300.);
        distances.put(0L, 3L, 300.);
        distances.put(0L, 4L, 300.);
        distances.put(0L, 5L, 300.);
        distances.put(0L, 6L, 300.);

        distances.put(1L, 0L, 300.);
        distances.put(1L, 2L, 10.);
        distances.put(1L, 3L, 900.);
        distances.put(1L, 4L, 900.);
        distances.put(1L, 5L, 900.);
        distances.put(1L, 6L, 900.);

        distances.put(2L, 0L, 300.);
        distances.put(2L, 1L, 10.);
        distances.put(2L, 3L, 60.);
        distances.put(2L, 4L, 900.);
        distances.put(2L, 5L, 900.);
        distances.put(2L, 6L, 900.);

        distances.put(3L, 0L, 300.);
        distances.put(3L, 1L, 900.);
        distances.put(3L, 2L, 60.);
        distances.put(3L, 4L, 10.);
        distances.put(3L, 5L, 900.);
        distances.put(3L, 6L, 900.);

        distances.put(4L, 0L, 300.);
        distances.put(4L, 1L, 900.);
        distances.put(4L, 2L, 900.);
        distances.put(4L, 3L, 10.);
        distances.put(4L, 5L, 50.);
        distances.put(4L, 6L, 900.);

        distances.put(5L, 0L, 300.);
        distances.put(5L, 1L, 900.);
        distances.put(5L, 2L, 900.);
        distances.put(5L, 3L, 900.);
        distances.put(5L, 4L, 50.);
        distances.put(5L, 6L, 10.);

        distances.put(6L, 0L, 300.);
        distances.put(6L, 1L, 900.);
        distances.put(6L, 2L, 900.);
        distances.put(6L, 3L, 900.);
        distances.put(6L, 4L, 900.);
        distances.put(6L, 5L, 10.);

        Multimap<Long, Long> actual = calculator.clarckSolver(distances);
        assertEquals(1, actual.keySet().size());
        Collection<Long> firstSequenceActual = actual.get(0L);
        ArrayList<Long> firstSequenceExpect = Lists.newArrayList(1L, 2L, 3L, 4L, 5L, 6L);
        assertEquals(firstSequenceExpect, firstSequenceActual);
    }

    /**
     * 0|   1|   2|   3|   4|   5|   6|
     * 0| -  300   300  300  300  300  300
     * 1|300   -   10    30  900  900  900
     * 2|300  10    -   900  900  900  900
     * 3|300  30   900    -   10  900  900
     * 4|300 900   900    10   -  900   50
     * 5|300 900   900   900  900  -    10
     * 6|300 900   900   900   50  10    -
     */
    @Test
    public void test_vrp_clarck_calculator_ершкв() {
        final Table<Long, Long, Double> distances = HashBasedTable.create();
        distances.put(0L, 1L, 300.);
        distances.put(0L, 2L, 300.);
        distances.put(0L, 3L, 300.);
        distances.put(0L, 4L, 300.);
        distances.put(0L, 5L, 300.);
        distances.put(0L, 6L, 300.);

        distances.put(1L, 0L, 300.);
        distances.put(1L, 2L, 10.);
        distances.put(1L, 3L, 30.);
        distances.put(1L, 4L, 900.);
        distances.put(1L, 5L, 900.);
        distances.put(1L, 6L, 900.);

        distances.put(2L, 0L, 300.);
        distances.put(2L, 1L, 10.);
        distances.put(2L, 3L, 900.);
        distances.put(2L, 4L, 900.);
        distances.put(2L, 5L, 900.);
        distances.put(2L, 6L, 900.);

        distances.put(3L, 0L, 300.);
        distances.put(3L, 1L, 30.);
        distances.put(3L, 2L, 900.);
        distances.put(3L, 4L, 10.);
        distances.put(3L, 5L, 900.);
        distances.put(3L, 6L, 900.);

        distances.put(4L, 0L, 300.);
        distances.put(4L, 1L, 900.);
        distances.put(4L, 2L, 900.);
        distances.put(4L, 3L, 10.);
        distances.put(4L, 5L, 900.);
        distances.put(4L, 6L, 50.);

        distances.put(5L, 0L, 300.);
        distances.put(5L, 1L, 900.);
        distances.put(5L, 2L, 900.);
        distances.put(5L, 3L, 900.);
        distances.put(5L, 4L, 900.);
        distances.put(5L, 6L, 10.);

        distances.put(6L, 0L, 300.);
        distances.put(6L, 1L, 900.);
        distances.put(6L, 2L, 900.);
        distances.put(6L, 3L, 900.);
        distances.put(6L, 4L, 50.);
        distances.put(6L, 5L, 10.);

        Multimap<Long, Long> actual = calculator.clarckSolver(distances);
        assertEquals(1, actual.keySet().size());
        Collection<Long> firstSequenceActual = actual.get(4L);
        ArrayList<Long> firstSequenceExpect = Lists.newArrayList(5L, 6L, 4L, 3L, 1L, 2L);
        assertEquals(firstSequenceExpect, firstSequenceActual);
    }
}
