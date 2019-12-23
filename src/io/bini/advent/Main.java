package io.bini.advent;

import io.bini.advent.day_11.Day11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        List<Day> daysToRun = new ArrayList<>();

        daysToRun.add(new Day11());

        for (Day day : daysToRun) {
            printDay(day);
        }
    }

    private static void printDay(Day day) throws IOException {
        long startTime = System.nanoTime();
        String part1Result = day.runPart1();
        long day1Time = System.nanoTime();
        System.out.println(day.getName() + "_1: " + part1Result + " (" + (day1Time - startTime) / 1_000_000 + "ms)");
        String part2Result = day.runPart2();
        long day2Time = System.nanoTime();
        System.out.println(day.getName() + "_2: " + part2Result + " (" + (day2Time - day1Time) / 1_000_000 + "ms)");
    }
}
