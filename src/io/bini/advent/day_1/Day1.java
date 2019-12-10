package io.bini.advent.day_1;

import io.bini.advent.Day;

import java.io.BufferedReader;
import java.io.IOException;

public class Day1 extends Day {

    public Day1() {
        super("day_1");
    }

    @Override
    protected String runPart1() throws IOException {
        BufferedReader bf = this.readFile();
        int requiredFuel = 0;
        String readLine;
        while ((readLine = bf.readLine()) != null) {
            int moduleMass = Integer.valueOf(readLine);
            requiredFuel += Math.floorDiv(moduleMass, 3) - 2;
        }
        return String.valueOf(requiredFuel);
    }

    @Override
    protected String runPart2() throws IOException {
        BufferedReader bf = this.readFile();
        String readLine;
        int requiredFuel = 0;
        while ((readLine = bf.readLine()) != null) {
            int moduleMass = Integer.valueOf(readLine);
            requiredFuel += getFuelForMass(moduleMass);
        }
        return String.valueOf(requiredFuel);
    }

    public static int getFuelForMass(int mass) {
        int fuel = Math.floorDiv(mass, 3) - 2;
        if (fuel > 0) {
            return fuel + getFuelForMass(fuel);
        } else {
            return 0;
        }
    }
}
