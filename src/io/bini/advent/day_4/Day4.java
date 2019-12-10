package io.bini.advent.day_4;

import io.bini.advent.Day;

import java.io.BufferedReader;
import java.io.IOException;

public class Day4 extends Day {
    private int min;
    private int max;

    public Day4() throws IOException {
        super("day_4");
        BufferedReader bf = this.readFile();
            String input = bf.readLine();
            String[] inputParts = input.split("-");
            this.min = Integer.valueOf(inputParts[0]);
            this.max = Integer.valueOf(inputParts[1]);

    }

    @Override
    protected String runPart1() throws IOException {
        return String.valueOf(this.countCombinations(this.min, this.max, false));
    }

    @Override
    protected String runPart2() throws IOException {
        return String.valueOf(this.countCombinations(this.min, this.max, true));
    }

    private int countCombinations(int min, int max, boolean part2) {
        int count = 0;
        for (int i = min; i <= max; i++) {
            String password = String.valueOf(i);
            boolean passwordOk;
            boolean hasConsecutiveDigits = false;
            boolean hasIncreasingDigits = true;
            boolean isSixDigits = password.length() == 6;
            for (int n = 0; n < password.length() - 1; n++) {
                if (password.charAt(n) == password.charAt(n + 1)) {
                    boolean notBefore = !part2 || (n - 1) < 0 || password.charAt(n - 1) != password.charAt(n);
                    boolean notAfter = !part2 || (n + 2 >= password.length()) || password.charAt(n + 2) != password.charAt(n);
                    hasConsecutiveDigits = hasConsecutiveDigits || notBefore && notAfter;
                }
                if (Integer.valueOf(password.substring(n, n + 1)) > Integer.valueOf(password.substring(n + 1, n + 2))) {
                    hasIncreasingDigits = false;
                }
            }
            passwordOk = hasConsecutiveDigits && hasIncreasingDigits && isSixDigits;
            if (passwordOk) {
                count++;
            }
        }
        return count;
    }
}
