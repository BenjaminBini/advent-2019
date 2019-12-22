package io.bini.advent.day_10;

import io.bini.advent.Day;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Day10 extends Day {

    public Day10() { super("day_10"); }

    @Override
    protected String runPart1() throws IOException {
        BufferedReader bf = this.readFile();
        String readLine;
        int i = 0;
        List<Asteroid> asteroids = new ArrayList<>();
        while ((readLine = bf.readLine()) != null) {
            for (int j = 0; j < readLine.length(); j++) {
                char currentChar = readLine.charAt(j);
                if (currentChar == '#') {
                    Asteroid asteroid = new Asteroid(j, i);
                    asteroids.add(asteroid);
                }
            }
            i++;
        }
        int result = 0;
        for (Asteroid asteroid : asteroids) {
            List<Asteroid> reachableAsteroids = asteroid.getReachableAsteroids(asteroids);
            if (reachableAsteroids.size() > result) {
                result = reachableAsteroids.size();
            }
        }
        return String.valueOf(result);
    }

    @Override
    protected String runPart2() throws IOException {
        BufferedReader bf = this.readFile();
        String readLine;
        int i = 0;
        List<Asteroid> asteroids = new ArrayList<>();
        while ((readLine = bf.readLine()) != null) {
            for (int j = 0; j < readLine.length(); j++) {
                char currentChar = readLine.charAt(j);
                if (currentChar == '#') {
                    Asteroid asteroid = new Asteroid(j, i);
                    asteroids.add(asteroid);
                }
            }
            i++;
        }
        List<Asteroid> maxReachableAsteroids = new ArrayList<>();
        Asteroid fromAsteroid = null;
        for (Asteroid asteroid : asteroids) {
            List<Asteroid> reachableAsteroids = asteroid.getReachableAsteroids(asteroids);
            if (reachableAsteroids.size() > maxReachableAsteroids.size()) {
                maxReachableAsteroids = reachableAsteroids;
                fromAsteroid = asteroid;
            }
        }
        for (Asteroid asteroid : maxReachableAsteroids) {
            int relativeX = asteroid.getRelativeX(fromAsteroid);
            int relativeY = asteroid.getRelativeY(fromAsteroid);
            asteroid.setLaserRelativeX(relativeX);
            asteroid.setLaserRelativeY(relativeY);
            double laserAngle = Math.atan2(asteroid.getLaserRelativeY(), asteroid.getLaserRelativeX()) - Math.atan2(-1, 0);
            if (laserAngle < 0) {
                laserAngle += 2 * Math.PI;
            }
            asteroid.setLaserAngle(laserAngle);
        }
        maxReachableAsteroids.sort(Comparator.comparing(Asteroid::getLaserAngle));
        Asteroid twoHundredthDestroyedAsteroid = maxReachableAsteroids.get(199);
        int result = twoHundredthDestroyedAsteroid.getX() * 100 + twoHundredthDestroyedAsteroid.getY();
        return String.valueOf(result);
    }

    private class Asteroid {
        private int x;
        private int y;
        private double laserAngle;

        private double laserRelativeX;
        private double laserRelativeY;

        public Asteroid(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public double getLaserRelativeX() {
            return laserRelativeX;
        }

        public void setLaserRelativeX(double laserRelativeX) {
            this.laserRelativeX = laserRelativeX;
        }

        public double getLaserRelativeY() {
            return laserRelativeY;
        }

        public void setLaserRelativeY(double laserRelativeY) {
            this.laserRelativeY = laserRelativeY;
        }

        public void setLaserAngle(double laserAngle) {
            this.laserAngle = laserAngle;
        }

        public double getLaserAngle() {
            return laserAngle;
        }

        public int getRelativeX(Asteroid asteroid) {
            return this.getX() - asteroid.getX();
        }

        public int getRelativeY(Asteroid asteroid) {
            return this.getY() - asteroid.getY();
        }


        public List<Asteroid> getReachableAsteroids(List<Asteroid> asteroids) {
            List<Asteroid> otherAsteroids = asteroids.stream()
                    .filter(a -> a.getX() != this.getX() || a.getY() != this.getY())
                    .collect(Collectors.toList());
            List<Asteroid> result = new ArrayList<>();
            for (Asteroid asteroid : otherAsteroids) {
                boolean isReachable = true;
                int m = asteroid.getRelativeX(this);
                int n = asteroid.getRelativeY(this);
                double asteroidNorm = Math.sqrt(Math.pow(m, 2) + Math.pow(n, 2));
                List<Asteroid> closerAsteroids = otherAsteroids.stream()
                        .filter(a ->
                                asteroidNorm > (Math.sqrt(Math.pow(a.getRelativeX(this), 2) + Math.pow(a.getRelativeY(this), 2)))
                                && (a.getX() != asteroid.getX() || a.getY() != asteroid.getY()))
                        .collect(Collectors.toList());
                for (Asteroid closerAsteroid : closerAsteroids) {
                    int i = closerAsteroid.getRelativeX(this);
                    int j = closerAsteroid.getRelativeY(this);
                    double closerAsteroidNorm = Math.sqrt(Math.pow(i, 2) + Math.pow(j, 2));
                    double cosine = (m * i + n * j) / (asteroidNorm * closerAsteroidNorm);

                    if (m * j - n * i == 0 && cosine > 0) {
                        isReachable = false;
                    }
                }
                if (isReachable) {
                    result.add(asteroid);
                }
            }
            return result;
        }
    }
}
