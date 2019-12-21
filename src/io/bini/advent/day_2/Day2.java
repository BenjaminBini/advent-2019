package io.bini.advent.day_2;

import io.bini.advent.Day;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;

public class Day2 extends Day {
    public Day2() {
        super("day_2");
    }

    @Override
    protected String runPart1() throws IOException {
        BufferedReader bf = this.readFile();
        String program = bf.readLine();
        int output = runProgram(program, null, 12, 2, null).getResult();
        return String.valueOf(output);
    }

    @Override
    protected String runPart2() throws IOException {
        BufferedReader bf = this.readFile();
        String program = bf.readLine();
        int noun = 0;
        int verb = 0;
        int output = 0;
        int requiredOutput = 19690720;
        for (noun = 0; noun <= 99 && output != requiredOutput; noun++) {
            for (verb = 0; verb <= 99 && output != requiredOutput; verb++) {
                output = runProgram(program, null, noun, verb, null).getResult();
            }
        }
        String result = String.valueOf(100 * (noun - 1) + (verb - 1));
        return result;
    }


    public static ProgramResult runProgram(String program, int[] opCodes, Integer noun, Integer verb, Integer[] inputs) {
        return runProgram(program, opCodes, noun, verb, inputs, 0);
    }

    public static ProgramResult runProgram(String program, int[] opCodes, Integer noun, Integer verb, Integer[] inputs, int initialPosition) {
        if (opCodes == null) {
            opCodes = Arrays.stream(program.split(",")).mapToInt(Integer::parseInt).toArray();
        }
        if (noun != null) {
            opCodes[1] = noun;
        }
        if (verb != null) {
            opCodes[2] = verb;
        }

        int step;
        int inputIndex = 0;
        for (int i = initialPosition; opCodes[i] != 99; i += step) {
            String instruction = leftPad(String.valueOf(opCodes[i]), 5);
            Operation operation = Operation.getOperation(instruction.charAt(instruction.length() - 1));
            step = operation.getParametersCount() + 1;
            int[] parameters = operation.getParameters(opCodes, i, instruction);

            if (operation.equals(Operation.ADD)) {
                int result = parameters[0] + parameters[1];
                opCodes[parameters[2]] = result;
            } else if (operation.equals(Operation.MULTIPLY)) {
                int result = parameters[0] * parameters[1];
                opCodes[parameters[2]] = result;
            } else if (operation.equals(Operation.OUTPUT)) {
                if (noun == null && verb == null && parameters[0] > 0) {
                    return new ProgramResult(opCodes, i, step, parameters[0]);
                }
            } else if (operation.equals(Operation.INPUT)) {
                if (inputIndex >= inputs.length) {
                    return new ProgramResult(opCodes, -1, 0, 0);
                }
                int inputPosition = parameters[0];
                int inputValue = inputs[inputIndex];
                opCodes[inputPosition] = inputValue;
                inputIndex++;
            } else if (operation.equals(Operation.JUMP_IF_TRUE)) {
                if (parameters[0] != 0) {
                    step = 0;
                    i = parameters[1];
                }
            } else if (operation.equals(Operation.JUMP_IF_FALSE)) {
                if (parameters[0] == 0) {
                    step = 0;
                    i = parameters[1];
                }
            } else if (operation.equals(Operation.LESS_THAN)) {
                if (parameters[0] < parameters[1]) {
                    opCodes[parameters[2]] = 1;
                } else {
                    opCodes[parameters[2]] = 0;
                }
            } else if (operation.equals(Operation.EQUALS)) {
                if (parameters[0] == parameters[1]) {
                    opCodes[parameters[2]] = 1;
                } else {
                    opCodes[parameters[2]] = 0;
                }
            }
        }
        return new ProgramResult(opCodes, -1, 0, opCodes[0]);
        //return opCodes[0];
    }

    public static class ProgramResult {
        private int[] opCodes;
        private int pointer;
        private int nextStep;
        private int result;

        public ProgramResult(int[] opCodes, int pointer, int nextStep, int result) {
            this.opCodes = opCodes;
            this.pointer = pointer;
            this.nextStep = nextStep;
            this.result = result;
        }

        public int[] getOpCodes() {
            return opCodes;
        }

        public void setOpCodes(int[] opCodes) {
            this.opCodes = opCodes;
        }

        public int getPointer() {
            return pointer;
        }

        public void setPointer(int pointer) {
            this.pointer = pointer;
        }

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public int getNextStep() {
            return nextStep;
        }

        public void setNextStep(int nextStep) {
            this.nextStep = nextStep;
        }
    }

    private enum Operation {
        INPUT(1, 0), OUTPUT(1, -1), ADD(3, 2), MULTIPLY(3, 2),
        JUMP_IF_TRUE(2, -1), JUMP_IF_FALSE(2, -1),
        LESS_THAN(3, 2), EQUALS(3, 2);

        private int parametersCount;
        private int outputIndex;

        public int getParametersCount() {
            return this.parametersCount;
        }

        Operation(int parametersCount, int outputIndex) {
            this.parametersCount = parametersCount;
            this.outputIndex = outputIndex;
        }

        public static Operation getOperation(char c) {
            switch (c) {
                case '2':
                    return MULTIPLY;
                case '3':
                    return INPUT;
                case '4':
                    return OUTPUT;
                case '5':
                    return JUMP_IF_TRUE;
                case '6':
                    return JUMP_IF_FALSE;
                case '7':
                    return LESS_THAN;
                case '8':
                    return EQUALS;
                default:
                    return ADD;
            }
        }

        public int[] getParameters(int[] program, int instructionCounter, String instruction) {
            int[] parameters = new int[this.parametersCount];
            for (int i = 0; i < this.parametersCount; i++) {
                ParameterType parameterType = ParameterType.getParameterType(instruction, i);
                if (i == outputIndex) {
                    parameters[i] = program[instructionCounter + 1 + i];
                } else {
                    parameters[i] = parameterType.getValue(program, program[instructionCounter + 1 + i]);
                }
            }
            return parameters;
        }
    }

    private enum ParameterType {
        REF, VALUE;

        public static ParameterType getParameterType(String instruction, int parameterIndex) {
            char c = instruction.charAt(instruction.length() - parameterIndex - 3);
            if (c == '0') {
                return REF;
            }
            return VALUE;
        }

        public int getValue(int[] program, int n) {
            if (this.equals(REF)) {
                return program[n];
            } else {
                return n;
            }
        }

    }

    private static String leftPad(String inputString, int length) {
        if (inputString.length() >= length) {
            return inputString;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputString.length()) {
            sb.append('0');
        }
        sb.append(inputString);

        return sb.toString();
    }
}
