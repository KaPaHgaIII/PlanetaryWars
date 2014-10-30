import java.util.Calendar;
import java.util.Set;

public class ShipsProcessor extends Thread {
    Set<Ship> ships;
    Gameplay gameplay;
    Calendar startTime;
    boolean inProcess;

    public ShipsProcessor(Gameplay gameplay, Set<Ship> ships) {
        this.ships = ships;
        this.gameplay = gameplay;
    }

    @Override
    public void run() {
        inProcess = true;
        while (true) {
            Calendar stopTime = Calendar.getInstance();
            long delay = stopTime.getTimeInMillis() - startTime.getTimeInMillis();
            startTime = stopTime;

            double delta = 0.2;
            double deltaX, deltaY;

            inProcess = false;
            for (Ship ship : ships) {
                if (ship.points.size() > 1) {


                    Point nextPoint = ship.points.get(0);

//                    System.out.println("Next point: " + nextPoint.x + " + " + nextPoint.y);
//                    System.out.println("Current pos: " + ship.pos.x + " + " + ship.pos.y);

                    double distanceToPoint = Geometry.distance(ship.pos,nextPoint);
                    if(distanceToPoint<5){
                        ship.points.remove(0);
                    }
                    ship.pos.x += (nextPoint.x - ship.pos.x) * delta * delay / distanceToPoint;
                    ship.pos.y += (nextPoint.y - ship.pos.y) * delta * delay / distanceToPoint;
                    inProcess = true;
                } else {
                    double distanceToDestination = Math.sqrt(Math.pow(ship.getX() - ship.destination.getX(), 2) +
                            Math.pow(ship.getY() - ship.destination.getY(), 2));
                    if (distanceToDestination >= ship.destination.radius) {
                        deltaX = (ship.destination.getX() - ship.getX()) * delta * delay / distanceToDestination;
                        deltaY = (ship.destination.getY() - ship.getY()) * delta * delay / distanceToDestination;
                        ship.setX(ship.getX() + deltaX);
                        ship.setY(ship.getY() + deltaY);
                        inProcess = true;
                    } else if (!ship.arrived) {
                        ship.destination.shipsCount.getAndIncrement();
//                    System.out.println("ship arrived");
                        ship.arrived = true;
                    }
                }


            }
            if (!inProcess) {
                //System.out.println("break: " + gameplay.shipSets.size());
                gameplay.shipSets.remove(ships);
                //gameplay.shipSets.put(ships, false);
                break;
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException ignored) {
            }
        }

    }

    @Override
    public synchronized void start() {
        startTime = Calendar.getInstance();
        super.start();
    }
}
