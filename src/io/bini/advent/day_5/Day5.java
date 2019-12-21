package io.bini.advent.day_5;

import io.bini.advent.Day;
import io.bini.advent.day_2.Day2;

import java.io.BufferedReader;
import java.io.IOException;

public class Day5 extends Day {
    public Day5() {
        super("day_5");
    }

    @Override
    protected String runPart1() throws IOException {
        BufferedReader bf = this.readFile();
        String program = bf.readLine();
        int output = Day2.runProgram(program, null, null, null, new Integer[]{1}).getResult();
        return String.valueOf(output);
    }

    @Override
    protected String runPart2() throws IOException {
        BufferedReader bf = this.readFile();
        String program = bf.readLine();
        int output = Day2.runProgram(program, null, null, null, new Integer[]{5}).getResult();
        return String.valueOf(output);
    }
}
