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
        long output = runProgram(program, null, 12, 2, null).getResult();
        return String.valueOf(output);
    }

    @Override
    protected String runPart2() throws IOException {
        BufferedReader bf = this.readFile();
        String program = bf.readLine();
        int noun = 0;
        int verb = 0;
        long output = 0;
        int requiredOutput = 19690720;
        for (noun = 0; noun <= 99 && output != requiredOutput; noun++) {
            for (verb = 0; verb <= 99 && output != requiredOutput; verb++) {
                output = runProgram(program, null, noun, verb, null).getResult();
            }
        }
        String result = String.valueOf(100 * (noun - 1) + (verb - 1));
        return result;
    }

    public static ProgramResult runProgram(String program, long[] opCodes, Integer noun, Integer verb, Integer[] inputs, int initialPosition) {
        return runProgram(program, opCodes, noun, verb, inputs, initialPosition, 0, false);
    }

    public static ProgramResult runProgram(String program, long[] opCodes, Integer noun, Integer verb, Integer[] inputs) {
        return runProgram(program, opCodes, noun, verb, inputs, 0, 0, false);
    }

    public static ProgramResult runProgram(String program, long[] opCodes, Integer noun, Integer verb, Integer[] inputs, int initialPosition, int initialRelativeBase, boolean returnZeros) {
        if (opCodes == null) {
            opCodes = Arrays.stream(program.split(",")).mapToLong(Long::parseLong).toArray();
        }
        if (noun != null) {
            opCodes[1] = noun;
        }
        if (verb != null) {
            opCodes[2] = verb;
        }

        int step;
        int inputIndex = 0;
        int relativeBase = initialRelativeBase;
        for (int i = initialPosition; getProgramValue(opCodes, i) != 99; i += step) {
            String instruction = leftPad(String.valueOf(getProgramValue(opCodes, i)), 5);
            Operation operation = Operation.getOperation(instruction.charAt(instruction.length() - 1));
            step = operation.getParametersCount() + 1;
            long[] parameters = operation.getParameters(opCodes, i, instruction, relativeBase);

            if (operation.equals(Operation.ADD)) {
                long result = parameters[0] + parameters[1];
                opCodes = setProgramValue(opCodes, (int) parameters[2], result);
            } else if (operation.equals(Operation.MULTIPLY)) {
                long result = parameters[0] * parameters[1];
                opCodes = setProgramValue(opCodes, (int) parameters[2], result);
            } else if (operation.equals(Operation.OUTPUT)) {
                if (noun == null && verb == null && parameters[0] > 0 || returnZeros) {
                    return new ProgramResult(opCodes, i, step, relativeBase, parameters[0]);
                }
            } else if (operation.equals(Operation.INPUT)) {
                if (inputIndex >= inputs.length) {
                    return new ProgramResult(opCodes, -1, 0, relativeBase, 0);
                }
                long inputPosition = parameters[0];
                int inputValue = inputs[inputIndex];
                opCodes = setProgramValue(opCodes, (int) inputPosition, inputValue);
                inputIndex++;
            } else if (operation.equals(Operation.JUMP_IF_TRUE)) {
                if (parameters[0] != 0) {
                    step = 0;
                    i = (int) parameters[1];
                }
            } else if (operation.equals(Operation.JUMP_IF_FALSE)) {
                if (parameters[0] == 0) {
                    step = 0;
                    i = (int) parameters[1];
                }
            } else if (operation.equals(Operation.LESS_THAN)) {
                if (parameters[0] < parameters[1]) {
                    opCodes = setProgramValue(opCodes, (int) parameters[2], 1);
                } else {
                    opCodes = setProgramValue(opCodes, (int) parameters[2], 0);
                }
            } else if (operation.equals(Operation.EQUALS)) {
                if (parameters[0] == parameters[1]) {
                    opCodes = setProgramValue(opCodes, (int) parameters[2], 1);
                } else {
                    opCodes = setProgramValue(opCodes, (int) parameters[2], 0);
                }
            } else if (operation.equals(Operation.SET_RELATIVE_BASE)) {
                relativeBase += parameters[0];
            }
        }
        return new ProgramResult(opCodes, -1, 0, relativeBase, getProgramValue(opCodes, 0));
    }

    public static class ProgramResult {
        private long[] opCodes;
        private int pointer;
        private int nextStep;
        private int relativeBase;
        private long result;

        public ProgramResult(long[] opCodes, int pointer, int nextStep, int relativeBase, long result) {
            this.opCodes = opCodes;
            this.pointer = pointer;
            this.nextStep = nextStep;
            this.relativeBase = relativeBase;
            this.result = result;
        }

        public long[] getOpCodes() {
            return opCodes;
        }

        public int getPointer() {
            return pointer;
        }

        public long getResult() {
            return result;
        }

        public int getNextStep() {
            return nextStep;
        }

        public int getRelativeBase() {
            return relativeBase;
        }
    }

    private enum Operation {
        INPUT(1, 0), OUTPUT(1, -1), ADD(3, 2), MULTIPLY(3, 2),
        JUMP_IF_TRUE(2, -1), JUMP_IF_FALSE(2, -1),
        LESS_THAN(3, 2), EQUALS(3, 2),
        SET_RELATIVE_BASE(1, -1);

        private int parametersCount;
        private int writeIndex;

        public int getParametersCount() {
            return this.parametersCount;
        }

        Operation(int parametersCount, int writeIndex) {
            this.parametersCount = parametersCount;
            this.writeIndex = writeIndex;
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
                case '9':
                    return SET_RELATIVE_BASE;
                default:
                    return ADD;
            }
        }

        public long[] getParameters(long[] program, int instructionCounter, String instruction, int relativeBase) {
            long[] parameters = new long[this.parametersCount];
            for (int i = 0; i < this.parametersCount; i++) {
                ParameterType parameterType = ParameterType.getParameterType(instruction, i);
                int parameterIndex = instructionCounter + i + 1;
                int base = 0;
                if (parameterType.equals(ParameterType.RELATIVE)) {
                    base = relativeBase;
                }
                if (i == writeIndex) {
                    if (parameterType == ParameterType.REF) {
                        parameters[i] = getProgramValue(program, parameterIndex);
                    } else if (parameterType == ParameterType.RELATIVE) {
                        parameters[i] = getProgramValue(program, parameterIndex) + base;
                    }
                } else if (parameterType == ParameterType.VALUE) {
                    parameters[i] = getProgramValue(program, parameterIndex);  // parameterType.getValue(program, (int) program[instructionCounter + 1 + i], relativeBase);
                } else if (parameterType == ParameterType.REF) {
                    parameters[i] = getProgramValue(program, (int) getProgramValue(program, parameterIndex));
                } else if (parameterType == ParameterType.RELATIVE) {
                    parameters[i] = getProgramValue(program, (int) getProgramValue(program, parameterIndex) + base);
                }
            }
            return parameters;
        }
    }

    private enum ParameterType {
        REF, VALUE, RELATIVE;

        public static ParameterType getParameterType(String instruction, int parameterIndex) {
            char c = instruction.charAt(instruction.length() - parameterIndex - 3);
            if (c == '0') {
                return REF;
            } else if (c == '1') {
                return VALUE;
            } else {
                return RELATIVE;
            }
        }
    }

    private static long[] setProgramValue(long[] program, int index, long value) {
        long[] newProgram;
        if (index >= program.length) {
            newProgram = Arrays.copyOf(program, index + 1);
        } else {
            newProgram = program;
        }
        newProgram[index] = value;
        return newProgram;
    }

    private static long getProgramValue(long[] program, int index) {
        if (index >= program.length) {
            return 0;
        } else {
            return program[index];
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
