import java.util.*;
import java.util.concurrent.*;

public class Gameplay {

    public List<Planet> planets;
    public Set<Set<Ship>> shipSets;

    public Gameplay() {
        planets = new ArrayList<Planet>();
        planets.add(new Planet(200, 400, 50));
        planets.add(new Planet(510, 220, 30));
        planets.add(new Planet(380, 300, 70));
        planets.add(new Planet(600, 240, 40));
        planets.add(new Planet(456, 453, 45));
        planets.add(new Planet(148, 259, 78));
        planets.add(new Planet(587, 367, 68));


        shipSets = Collections.newSetFromMap(new ConcurrentHashMap<Set<Ship>, Boolean>());


    }

    void startPlanets() {
        (new Thread(new ShipsIncrementer())).start();
    }

    private class ShipsIncrementer implements Runnable {
        @Override
        public void run() {
            Random random = new Random();
            while (true) {
                for (Planet planet : planets) {
                    planet.shipsCount.incrementAndGet();
                    try {
                        Thread.sleep((long) random.nextInt(100));
                    } catch (InterruptedException ignored) {
                    }
                }

            }
        }
    }

    void sendShips(Set<Planet> fromList, Planet to) {
        double interval = 12;
        double radiusDelta = 30;

        Set<Ship> ships = new HashSet<Ship>();

        for (Planet from : fromList) {
            double radius = from.radius + 10;
            int sendCount = from.shipsCount.get() / 2;
            from.shipsCount.addAndGet(-sendCount);
            int multiplier = 0;


            double distance = Math.sqrt(Math.pow(from.getX() - to.getX(), 2) + Math.pow(from.getY() - to.getY(), 2));
            double offset = Math.acos((to.getX() - from.getX()) / distance);
            if (Math.asin((to.getY() - from.getY()) / distance) < 0) {
                offset *= -1;
            }

            for (int i = 0; i < sendCount; i++) {

                Ship ship = new Ship(to);

                //определяем стартовую позицию
                double degree = Math.acos(1 - Math.pow(interval / radius, 2) / 2);

                if (Math.abs(degree * multiplier) > Math.PI / 2) {
                    radius += radiusDelta;
                    multiplier = 0;
                }

                ship.setX(from.getX() + radius * Math.cos(degree * multiplier + offset));
                ship.setY(from.getY() + radius * Math.sin(degree * multiplier + offset));

                if (multiplier > 0) {
                    multiplier *= -1;
                } else {
                    multiplier *= -1;
                    multiplier++;
                }

                double minDistToPlanet = 10;
                double[] params = getDistanceToNearestPlanetAndItsRadius(ship.pos);
                if (params[0] < params[1] + minDistToPlanet) {
                    i--;
                    continue;
                }

                //рассчитываем путь
                ship.findWay(this, from, to);


                //добавляем в список
                ships.add(ship);
            }

            shipSets.add(ships);
        }
        ShipsProcessor shipsProcessor = new ShipsProcessor(this, ships);
        shipsProcessor.start();

    }

    public Planet findNearestPlanetOnTheWayTo(Point point, Point to, double minDistToPlanet, Set<Planet> exclude) {
        Planet nearest = null;
        double distToNearest = Geometry.distance(to, point);
        for (Planet planet : this.planets) {
            if (exclude == null || !exclude.contains(planet)) {
                if (Geometry.distance(planet.pos, to) < Geometry.distance(to, point) &&
                        Geometry.isLineAndCircleIntersect(point, to, planet.pos, planet.radius + minDistToPlanet)
                        ) {
                    if (Geometry.distance(planet.pos, point) < distToNearest) {
                        distToNearest = Geometry.distance(planet.pos, point);
                        nearest = planet;
                    }
                }
            }
        }
        return nearest;
    }

    public Planet findNearestPlanetOnTheWayTo(Point point, Point to, double minDistToPlanet) {
        return findNearestPlanetOnTheWayTo(point, to, minDistToPlanet, null);
    }


    public Planet findNearestPlanet(Point point) {
        Planet nearest = null;
        double distToNearest = Double.MAX_VALUE;
        for (Planet planet : this.planets) {
            if (Geometry.distance(planet.pos, point) < distToNearest) {
                distToNearest = Geometry.distance(planet.pos, point);
                nearest = planet;
            }
        }
        return nearest;
    }

    public double getDistanceToNearestPlanet(Point point) {
        double distToNearest = Double.MAX_VALUE;
        for (Planet planet : this.planets) {
            if (Geometry.distance(planet.pos, point) < distToNearest) {
                distToNearest = Geometry.distance(planet.pos, point);
            }
        }
        return distToNearest;
    }

    public double[] getDistanceToNearestPlanetAndItsRadius(Point point) {
        double distToNearest = Double.MAX_VALUE;
        double radius = 0;
        for (Planet planet : this.planets) {
            if (Geometry.distance(planet.pos, point) < distToNearest) {
                distToNearest = Geometry.distance(planet.pos, point);
                radius = planet.radius;
            }
        }
        double[] result = new double[2];
        result[0] = distToNearest;
        result[1] = radius;
        return result;
    }


}
