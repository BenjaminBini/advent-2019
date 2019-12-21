package io.bini.advent.day_7;

import io.bini.advent.Day;
import io.bini.advent.day_2.Day2;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day7 extends Day {
    public Day7() {
        super("day_7");
    }

    @Override
    protected String runPart1() throws IOException {
        BufferedReader bf = this.readFile();
        String program = bf.readLine();

        int[] inputs = new int[]{4, 3, 2, 1, 0};
        List<int[]> permutations = permute(inputs, 0, inputs.length - 1, new ArrayList<>());
        int maxOutput = 0;
        for (int[] perm : permutations) {
            int output = 0;
            int[] opCodes = Arrays.stream(program.split(",")).mapToInt(Integer::parseInt).toArray();
            for (int i = 0; i < 5; i++) {
                output = Day2.runProgram(null, opCodes, null, null, new Integer[]{perm[i], output}, 0).getResult();
            }
            if (output > maxOutput) {
                maxOutput = output;
            }
        }

        return String.valueOf(maxOutput);
    }

    @Override
    protected String runPart2() throws IOException {
        BufferedReader bf = this.readFile();
        String program = bf.readLine();


        int[] inputs = new int[]{5, 6, 7, 8, 9};
        List<int[]> permutations = permute(inputs, 0, inputs.length - 1, new ArrayList<>());
        int maxOutput = 0;
        int[] opCodes = Arrays.stream(program.split(",")).mapToInt(Integer::parseInt).toArray();
        for (int i = 0; i < permutations.size(); i++) {
            List<int[]> programs = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                int[] copiedProgram = new int[opCodes.length];
                System.arraycopy(opCodes, 0, copiedProgram, 0, opCodes.length);
                programs.add(copiedProgram);
            }
            int output = 0;
            int[] permutation = permutations.get(i);
            int inputIndex = 0;
            int pointer;
            int nextStep;
            int[] pointers = new int[]{0, 0, 0, 0, 0};
            for (int amplifierIndex = 0; true; amplifierIndex = (amplifierIndex + 1) % 5) {
                Day2.ProgramResult result;
                int[] opCodesInput = programs.get(amplifierIndex);
                if (inputIndex < permutation.length) {
                    result = Day2.runProgram(null, opCodesInput, null, null, new Integer[]{permutation[inputIndex], output}, pointers[amplifierIndex]);
                } else {
                    result = Day2.runProgram(null, opCodesInput, null, null, new Integer[]{output}, pointers[amplifierIndex]);
                }
                inputIndex++;
                pointer = result.getPointer();
                if (pointer == -1) {
                    break;
                }
                output = result.getResult();
                nextStep = result.getNextStep();
                pointers[amplifierIndex] = pointer + nextStep;
            }
            if (output > maxOutput) {
                maxOutput = output;
            }
        }
        return String.valueOf(maxOutput);
    }

    private List<int[]> permute(int[] arr, int i, int n, List<int[]> perms) {
        int j;
        if (i == n) {
            int[] copiedArray = new int[5];
            System.arraycopy(arr, 0, copiedArray, 0, 5);
            perms.add(copiedArray);
        } else {
            for (j = i; j <= n; j++) {
                swap(arr, i, j);
                permute(arr, i + 1, n, perms);
                swap(arr, i, j);
            }
        }
        return perms;
    }

    void swap(int[] arr, int x, int y) {
        int temp = arr[x];
        arr[x] = arr[y];
        arr[y] = temp;
    }

}
