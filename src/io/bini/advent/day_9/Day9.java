package io.bini.advent.day_9;

import io.bini.advent.Day;
import io.bini.advent.day_2.Day2;

import java.io.BufferedReader;
import java.io.IOException;

public class Day9 extends Day {
    public Day9() {
        super("day_9");
    }

    @Override
    protected String runPart1() throws IOException {
        BufferedReader bf = this.readFile();
        String program = bf.readLine();
        long output = Day2.runProgram(program, null, null, null, new Integer[]{1}).getResult();
        return String.valueOf(output);
    }

    @Override
    protected String runPart2() throws IOException {
        BufferedReader bf = this.readFile();
        String program = bf.readLine();
        long output = Day2.runProgram(program, null, null, null, new Integer[]{2}).getResult();
        return String.valueOf(output);
    }

}
