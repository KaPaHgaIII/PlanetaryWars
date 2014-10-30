public class Point {
    public double x, y;

    public Point() {
    }

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        if (Math.abs(point.x - x) > 0.1) return false;
        if (Math.abs(point.y - y) > 0.1) return false;

        return true;
    }

}
