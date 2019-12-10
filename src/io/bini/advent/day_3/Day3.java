package io.bini.advent.day_3;

import io.bini.advent.Day;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Day3 extends Day {
    public Day3() {
        super("day_3");
    }

    @Override
    protected String runPart1() throws IOException {
        BufferedReader bf = this.readFile();
        String line1 = bf.readLine();
        String line2 = bf.readLine();
        List<Coordinates> wire1 = getWireCoordinates(line1);
        List<Coordinates> wire2 = getWireCoordinates(line2);
        List<Coordinates> intersections = getIntersections(wire1, wire2);
        int closestDistance = getClosestIntersectionDistance(intersections);
        return String.valueOf(closestDistance);
    }

    @Override
    protected String runPart2() throws IOException {
        BufferedReader bf = this.readFile();
        String line1 = bf.readLine();
        String line2 = bf.readLine();
        List<Coordinates> wire1 = getWireCoordinates(line1);
        List<Coordinates> wire2 = getWireCoordinates(line2);
        int closestDistance = getClosestIntersectionDistance(wire1, wire2);
        return String.valueOf(closestDistance);
    }



    private static int getClosestIntersectionDistance(List<Coordinates> intersections) {
        int closestDistance = Integer.MAX_VALUE;
        for (Coordinates coordinates : intersections) {
            int distance = Math.abs(coordinates.x) + Math.abs(coordinates.y);
            if (distance < closestDistance) {
                closestDistance = distance;
            }
        }
        return closestDistance;
    }

    private static int getClosestIntersectionDistance(List<Coordinates> wire1, List<Coordinates> wire2) {
        List<Coordinates> intersections = new ArrayList<>();
        int minSteps = Integer.MAX_VALUE;
        int wire1Steps = 0;
        for (int i = 0; i < wire1.size() - 1; i++) {
            Coordinates a1 = wire1.get(i);
            Coordinates b1 = wire1.get(i + 1);
            if (i == 0) {
                wire1Steps += Math.abs(a1.x) + Math.abs(a1.y);
            }
            wire1Steps += Math.abs(b1.x - a1.x) + Math.abs(b1.y - a1.y);
            int wire2Steps = 0;
            for (int j = 0; j < wire2.size() - 1; j++) {
                Coordinates a2 = wire2.get(j);
                Coordinates b2 = wire2.get(j + 1);
                if (j == 0) {
                    wire2Steps += Math.abs(a2.x) + Math.abs(a2.y);
                }
                int interceptionSteps = Integer.MAX_VALUE;
                wire2Steps += Math.abs(b2.x - a2.x) + Math.abs(b2.y - a2.y);
                if (a1.x == b1.x && a2.y == b2.y
                    && (a1.y > a2.y && b1.y < a2.y || a1.y < a2.y && b1.y > a2.y)
                    && (a2.x > a1.x && b2.x < a1.x || a2.x < a1.x && b2.x > a1.x)) {
                    intersections.add(new Coordinates(a1.x, a2.y));
                    interceptionSteps = wire1Steps + wire2Steps - Math.abs(b2.x - a1.x) - Math.abs(b1.y - a2.y);
                } else if (a1.y == b1.y && a2.x == b2.x
                        && (a1.x > a2.x && b1.x < a2.x || a1.x < a2.x && b1.x > a2.x)
                        && (a2.y > a1.y && b2.y < a1.y || a2.y < a1.y && b2.y > a1.y)) {
                    intersections.add(new Coordinates(a2.x, a1.y));
                    interceptionSteps = wire1Steps + wire2Steps - Math.abs(b2.y - a1.y) - Math.abs(b1.x - a2.x);
                }
                if (interceptionSteps < minSteps) {
                    minSteps = interceptionSteps;
                }
            }
        }
        return minSteps;
    }


    private static List<Coordinates> getIntersections(List<Coordinates> wire1, List<Coordinates> wire2) {
        List<Coordinates> intersections = new ArrayList<>();
        for (int i = 0; i < wire1.size() - 1; i++) {
            for (int j = 0; j < wire2.size() - 1; j++) {
                Coordinates a1 = wire1.get(i);
                Coordinates b1 = wire1.get(i + 1);
                Coordinates a2 = wire2.get(j);
                Coordinates b2 = wire2.get(j + 1);
                if (a1.x == b1.x && a2.y == b2.y
                        && (a1.y > a2.y && b1.y < a2.y || a1.y < a2.y && b1.y > a2.y)
                        && (a2.x > a1.x && b2.x < a1.x || a2.x < a1.x && b2.x > a1.x)) {
                    intersections.add(new Coordinates(a1.x, a2.y));
                } else if (a1.y == b1.y && a2.x == b2.x
                        && (a1.x > a2.x && b1.x < a2.x || a1.x < a2.x && b1.x > a2.x)
                        && (a2.y > a1.y && b2.y < a1.y || a2.y < a1.y && b2.y > a1.y)) {
                    intersections.add(new Coordinates(a2.x, a1.y));
                }
            }
        }
        return intersections;
    }

    private static List<Coordinates> getWireCoordinates(String moves) {
        List<Coordinates> coordinates = new ArrayList<>();
        Coordinates previousCoordinates = new Coordinates(0,0);
        for (String move : moves.split(",")) {
            Character direction = move.charAt(0);
            int length = Integer.valueOf(move.substring(1));
            MoveType moveType = MoveType.getMoveType(direction);
            int x = 0;
            int y = 0;
            switch (moveType) {
                case UP:
                    x = previousCoordinates.x;
                    y = previousCoordinates.y + length;
                    break;
                case RIGHT:
                    x = previousCoordinates.x + length;
                    y = previousCoordinates.y;
                    break;
                case DOWN:
                    x = previousCoordinates.x;
                    y = previousCoordinates.y - length;
                    break;
                case LEFT:
                    x = previousCoordinates.x - length;
                    y =  previousCoordinates.y;
                    break;
            }

            Coordinates newCoordinates = new Coordinates(x, y);
            coordinates.add(newCoordinates);
            previousCoordinates = newCoordinates;

        }
        return coordinates;
    }

    private static enum MoveType {
        UP, RIGHT, DOWN, LEFT;

        public static MoveType getMoveType(Character character) {
            switch (character) {
                case 'U':
                    return UP;
                case 'R':
                    return RIGHT;
                case 'D':
                    return DOWN;
                default:
                    return LEFT;
            }
        }
    }

    private static class Coordinates {
        public int x;
        public int y;
        public Coordinates(int x, int y) {
            this.x = x;
            this.y = y;
        }

    }
}
