package io.bini.advent.day_6;

import io.bini.advent.Day;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

public class Day6 extends Day {
    public Day6() {
        super("day_6");
    }

    @Override
    protected String runPart1() throws IOException {
        constructSystem();
        Iterator<Planet> planetIterator = planets.iterator();
        int result = 0;
        while(planetIterator.hasNext()) {
            Planet planet = planetIterator.next();
            while (planet.getOrbitAround() != null) {
                result++;
                planet = planet.getOrbitAround();
            }
        }
        return String.valueOf(result);
    }

    @Override
    protected String runPart2() throws IOException {
        constructSystem();
        Planet santa = planets.stream().filter(p -> p.getName().equals("SAN")).findFirst().get();
        LinkedList<Planet> santaOrbits = new LinkedList<>();
        Planet orbittingPlanet = santa;
        while (orbittingPlanet != null) {
            orbittingPlanet = orbittingPlanet.getOrbitAround();
            santaOrbits.add(orbittingPlanet);
        }
        Planet you = planets.stream().filter(p -> p.getName().equals("YOU")).findFirst().get();
        int result = 0;
        orbittingPlanet = you.getOrbitAround();
        while (!santaOrbits.contains(orbittingPlanet)) {
            result++;
            orbittingPlanet = orbittingPlanet.getOrbitAround();
        }
        while (santaOrbits.removeFirst() != orbittingPlanet) {
            result++;
        }
        return String.valueOf(result);
    }

    private void constructSystem() throws IOException {
        BufferedReader bf = this.readFile();
        String line;
        while ((line = bf.readLine()) != null) {
            String[] lineParts = line.split("\\)");
            createPlanet(lineParts[0], lineParts[1]);
        }
    }

    private class Planet {

        private Planet orbitAround;

        private String name;

        public Planet getOrbitAround() {
            return orbitAround;
        }

        public void setOrbitAround(Planet orbitAround) {
            this.orbitAround = orbitAround;
        }

        public String getName() {
            return name;
        }

        public Planet(String name) {
            this.name = name;
        }
    }

    private Set<Planet> planets = new HashSet<>();

    private void createPlanet(String name, String orbit) {
        Optional<Planet> matchingPlanet = planets.stream().filter(p -> p.getName().equals(name)).findFirst();
        if (matchingPlanet.isEmpty()) {
            matchingPlanet = Optional.of(new Planet(name));
        }
        planets.add(matchingPlanet.get());

        if (orbit != null && !orbit.equals("")) {
            Optional<Planet> matchingOrbit = planets.stream().filter(p -> p.getName().equals(orbit)).findFirst();
            if (matchingOrbit.isEmpty()) {
                matchingOrbit = Optional.of(new Planet(orbit));
            }
            matchingOrbit.get().setOrbitAround(matchingPlanet.get());
            planets.add(matchingOrbit.get());
        }
    }
}
