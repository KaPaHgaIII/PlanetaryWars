public class Waypoint extends Point {
    public double distance;
    public boolean last = false;
    public boolean tangency = false;
    public Planet nearest;

    Waypoint(Point point) {
        distance = 0;
        this.x = point.x;
        this.y = point.y;
    }

    Waypoint(Point point, double distance) {
        this.x = point.x;
        this.y = point.y;
        this.distance = distance;
    }

    public Waypoint(Point point, double distance, boolean tangency, Planet nearest) {
        this.x = point.x;
        this.y = point.y;
        this.tangency = tangency;
        this.distance = distance;
        this.nearest = nearest;
    }
}
