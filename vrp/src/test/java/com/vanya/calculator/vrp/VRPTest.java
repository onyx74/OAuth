package com.vanya.calculator.vrp;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Table;
import com.vanya.dto.LoadDTO;
import com.vanya.provider.DescartesDistanceProvider;
import javafx.util.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class VRPTest {
    private static DescartesDistanceProvider distanceProvider = new DescartesDistanceProvider();
    private static VRPCalculator vrpCalculator = new VRPCalculator();

    public static void main(String[] args) {
        File directory = new File("./vrp/src/test/resources/A-VRP");
        for (File file1 : directory.listFiles()) {
            Table<Long, Long, Double> distances = readFile(file1);

            //calculateAnswer
            //print
//            distanceProvider.calculateDistance();
//            break;
        }
    }

    private static Table<Long, Long, Double> readFile(File file) {
        List<LoadDTO> loads = new ArrayList<>();
        System.out.println("Start process file: " + file.getName());
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine();
            line = br.readLine();
            System.out.println("Optimal result: " + line.split(" ")[12].split("\\)"));
            br.readLine();
            int count = Integer.parseInt(br.readLine().split(" ")[2]);
            br.readLine();
            br.readLine();
            br.readLine();
            line = br.readLine();
            String[] values = line.split(" ");
            Pair<Double, Double> startLocation = new Pair<>(Double.parseDouble(values[2]), Double.parseDouble(values[3]));
            for (int i = 1; i < count; ++i) {
                line = br.readLine();
                values = line.split(" ");
                LoadDTO loadDTO = new LoadDTO();
                loadDTO.setLoadId(Long.parseLong(values[1]));
                loadDTO.setStartLatitude(Double.parseDouble(values[2]));
                loadDTO.setFinishLatitude(Double.parseDouble(values[2]));
                loadDTO.setStartLongitude(Double.parseDouble(values[3]));
                loadDTO.setFinishLongitude(Double.parseDouble(values[3]));
                loads.add(loadDTO);
            }
            Table<Long, Long, Double> distances = distanceProvider.calculateDistance(startLocation, loads);
            printDistances(distances);
            Multimap<Long, Long> longLongMultimap = vrpCalculator.clarckSolver(distances);
            System.out.println(longLongMultimap);
            Map<Long, LoadDTO> collect = loads.stream().collect(Collectors.toMap(LoadDTO::getLoadId, load -> load,
                    (oldValue, newValue) -> oldValue));
            System.out.println(distanceProvider.calculateDistance(startLocation, longLongMultimap, collect));
            return distances;
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new RuntimeException();
    }

    static int i = 0;

    private static void printDistances(Table<Long, Long, Double> distances)  {

        try (FileWriter fileWriter = new FileWriter(new File(i + ".txt"))) {
            System.out.println("Start pring Distance");
            for (Table.Cell<Long, Long, Double> cell : distances.cellSet()) {

                fileWriter.write(cell.getColumnKey() + "    " + cell.getRowKey() + "    " + cell.getValue()+ "                                     0.0");
                fileWriter.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Finish pring Distance");
        i++;
    }
}
