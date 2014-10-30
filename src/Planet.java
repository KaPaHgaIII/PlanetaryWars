import java.awt.*;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Planet extends SpaceObject {
    public int radius;
    public Color COLOR;
    public AtomicInteger shipsCount;


    public Planet() {
        //button = new PlanetButton(this);
        Random random = new Random();
        COLOR = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat());
        radius = 30 + random.nextInt(50);
        shipsCount = new AtomicInteger(random.nextInt(100));
        pos.x = random.nextInt(800);
        pos.y = random.nextInt(600);
    }

    public Planet(double x, double y) {
        this();
        pos.x = x;
        pos.y = y;
    }
    public Planet(double x, double y, int radius) {
        this();
        pos.x = x;
        pos.y = y;
        this.radius = radius;
    }
}
