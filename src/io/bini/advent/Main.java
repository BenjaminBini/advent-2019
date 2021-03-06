package io.bini.advent;

import io.bini.advent.day_1.Day1;
import io.bini.advent.day_10.Day10;
import io.bini.advent.day_11.Day11;
import io.bini.advent.day_2.Day2;
import io.bini.advent.day_3.Day3;
import io.bini.advent.day_4.Day4;
import io.bini.advent.day_5.Day5;
import io.bini.advent.day_6.Day6;
import io.bini.advent.day_7.Day7;
import io.bini.advent.day_8.Day8;
import io.bini.advent.day_9.Day9;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        List<Day> daysToRun = new ArrayList<>();
        daysToRun.add(new Day1());
        daysToRun.add(new Day2());
        daysToRun.add(new Day3());
        daysToRun.add(new Day4());
        daysToRun.add(new Day5());
        daysToRun.add(new Day6());
        daysToRun.add(new Day7());
        daysToRun.add(new Day8());
        daysToRun.add(new Day9());
        daysToRun.add(new Day10());
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
