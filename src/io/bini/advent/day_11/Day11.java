package io.bini.advent.day_11;

import io.bini.advent.Day;
import io.bini.advent.day_2.Day2;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class Day11 extends Day {

    public Day11() { super("day_11"); }

    private Set<String> paintedPositions;
    private Map<Integer, Map<Integer, Integer>> paintZone;
    private int minX;
    private int maxX;
    private int minY;
    private int maxY;

    @Override
    protected String runPart1() throws IOException {
        BufferedReader bf = this.readFile();
        String program = bf.readLine();
        runProgram(program, 0);
        return String.valueOf(paintedPositions.size());
    }

    @Override
    protected String runPart2() throws IOException {
        BufferedReader bf = this.readFile();
        String program = bf.readLine();
        runProgram(program, 1);
        String result = "";
        for (int y = minY; y < maxY; y++) {
            result += '\n';
            for (int x = minX; x < maxX; x++) {
                if (getColor(x, y, paintZone) == 1) {
                    result += '#';
                } else {
                    result += '.';
                }
            }
        }
        return result;
    }

    private void runProgram(String program, int input) {
        minX = Integer.MAX_VALUE;
        maxX = Integer.MIN_VALUE;
        minY = Integer.MAX_VALUE;
        maxY = Integer.MIN_VALUE;
        long[] opCodes = Arrays.stream(program.split(",")).mapToLong(Long::parseLong).toArray();
        paintZone = new HashMap<>();
        int pointer = 0;
        int relativeBase = 0;
        int toggle = 0;
        int x = 0;
        int y = 0;
        int direction = 0; // 0: UP, 1: RIGHT, 2:BOTTOM, 3: LEFT
        paintedPositions = new HashSet<>();
        Day2.ProgramResult programResult;
        while (true) {
            programResult = Day2.runProgram(null, opCodes, null, null, new Integer[]{input}, pointer, relativeBase, true);
            pointer = programResult.getPointer() + programResult.getNextStep();
            relativeBase = programResult.getRelativeBase();
            if (toggle == 0) { // paint
                paintedPositions.add(x + ";" + y);
                paint(x, y, (int) programResult.getResult(), paintZone);
            } else { // move
                if (programResult.getResult() == 0) {
                    direction = (direction - 1) % 4;
                } else {
                    direction = (direction + 1) % 4;
                }
                if (direction < 0) {
                    direction += 4;
                }
                switch (direction) {
                    case 0:
                        y += 1;
                        break;
                    case 1:
                        x += 1;
                        break;
                    case 2:
                        y -= 1;
                        break;
                    case 3:
                        x -= 1;
                        break;
                }
                input = getColor(x, y, paintZone);
            }
            if (programResult.getPointer() == -1) {
                break;
            }
            toggle = 1 - toggle;
        }
    }


    private void paint(int x, int y, int color, Map<Integer, Map<Integer, Integer>> zone) {
        Map<Integer, Integer> zoneLine;
        if (!zone.containsKey(y)) {
            zoneLine = new HashMap<>();
            zone.put(y, zoneLine);
        } else {
            zoneLine= zone.get(y);
        }
        zoneLine.put(x, color);
        if (x < minX) {
            minX = x;
        } else if (x > maxX) {
            maxX = x;
        }
        if (y < minY) {
            minY = y;
        } else if (y > maxY) {
            maxY = y;
        }
    }

    private int getColor(int x, int y, Map<Integer, Map<Integer, Integer>> zone) {
        if (!zone.containsKey(y)) {
            return 0;
        } else {
            Map<Integer, Integer> zoneLine = zone.get(y);
            if (zoneLine.containsKey(x)) {
                return zoneLine.get(x);
            } else {
                return 0;
            }
        }
    }

}
