package io.bini.advent;

import java.io.*;

public abstract class Day {

    private String name;

    protected Day(String name) {
        this.name = name;
    }

    protected BufferedReader readFile() throws FileNotFoundException {
        File f = new File("src/io/bini/advent/" + name + "/input");
        return new BufferedReader(new FileReader(f));
    }

    protected abstract String runPart1() throws IOException;
    protected abstract String runPart2() throws IOException;

    public String getName() {
        return name;
    }
}
