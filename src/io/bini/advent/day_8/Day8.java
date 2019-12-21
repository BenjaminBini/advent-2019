package io.bini.advent.day_8;

import io.bini.advent.Day;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day8 extends Day {
    public Day8() {
        super("day_8");
    }

    @Override
    protected String runPart1() throws IOException {
        int result = 0;
        BufferedReader bf = readFile();
        String input = bf.readLine();
        int length = 25;
        int width = 6;
        int layer = -1;
        List<Integer> numberOfZerosForLayer = new ArrayList<>();
        List<Integer> numberOfOnesForLayer = new ArrayList<>();
        List<Integer> numberOfTwosForLayer = new ArrayList<>();
        for (int i = 0; i < input.length(); i++) {
            if (i % (length * width) == 0) {
                layer++;
                numberOfZerosForLayer.add(0);
                numberOfOnesForLayer.add(0);
                numberOfTwosForLayer.add(0);
            }
            char pixel = input.charAt(i);
            if (pixel == '0') {
                numberOfZerosForLayer.set(layer, numberOfZerosForLayer.get(layer) + 1);
            } else if (pixel == '1') {
                numberOfOnesForLayer.set(layer, numberOfOnesForLayer.get(layer) + 1);
            } else if (pixel == '2') {
                numberOfTwosForLayer.set(layer, numberOfTwosForLayer.get(layer) + 1);
            }
        }

        int layerWithMinimumOfZeros = 0;
        for (int i = 0; i < layer; i++) {
            if (numberOfZerosForLayer.get(layerWithMinimumOfZeros) >= numberOfZerosForLayer.get(i)) {
                layerWithMinimumOfZeros = i;
            }
        }
        result = numberOfOnesForLayer.get(layerWithMinimumOfZeros) * numberOfTwosForLayer.get(layerWithMinimumOfZeros);
        return String.valueOf(result);
    }

    @Override
    protected String runPart2() throws IOException {
        int result = 0;
        List<int[][]> layers = new ArrayList<>();
        BufferedReader bf = readFile();
        String input = bf.readLine();
        int length = 25;
        int width = 6;
        char[][] picture = new char[width][length];
        for (int i = 0; i < input.length(); i++) {
            char color = input.charAt(i);
            int x = (i % (length * width)) / length;
            int y = (i % (length * width)) % length;
            if (picture[x][y] == 0 && color != '2') {
                if (color == '0') {
                    color = '.';
                }
                picture[x][y] = color;
            }
        }

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < length; j++) {
                System.out.print(picture[i][j]);
            }
            System.out.println();
        }


        return String.valueOf(result);
    }

}
