public class Geometry {
    public static double distance(Point a, Point b) {
        return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }

    public static int sign(double x) {
        if (x > 0) {
            return 1;
        } else if (x < 0) {
            return -1;
        } else {
            return 0;
        }
    }

    public static Point[] getCirclesIntersection(Point circle1, double r1, Point circle2, double r2, double EPS) {
        Point[] points = null;
        double d = Geometry.distance(circle1, circle2);
        if (Math.abs(d - Math.abs(r1 - r2)) < EPS / 2) {
            int nSign = Geometry.sign(r1 - r2);
            double nDelta = (d - Math.abs(r1 - r2)) / 2;
            points = new Point[1];
            points[0] = new Point(circle1.x + (r1 - nDelta) * (circle2.x - circle1.x) / d * nSign,
                    circle1.y + (r1 - nDelta) * (circle2.y - circle1.y) / d * nSign);
        } else if (Math.abs(d - (r1 + r2)) < EPS / 2) {
            double nDelta = (d - Math.abs(r1 + r2)) / 2;
            points = new Point[1];
            points[0] = new Point(circle1.x + (r1 + nDelta) * (circle2.x - circle1.x) / d,
                    circle1.y + (r1 + nDelta) * (circle2.y - circle1.y) / d);
        } else if (d < r1 + r2 && d > Math.abs(r1 - r2)) {
            double b = (r2 * r2 - r1 * r1 + d * d) / (2 * d);
            double a = d - b;
            double h = Math.sqrt(r1 * r1 - a * a);
            Point oPos0 = new Point(circle1.x + a / d * (circle2.x - circle1.x),
                    circle1.y + a / d * (circle2.y - circle1.y));
            points = new Point[2];
            points[0] = new Point(oPos0.x + h / d * (circle2.y - circle1.y),
                    oPos0.y - h / d * (circle2.x - circle1.x));
            points[1] = new Point(oPos0.x - h / d * (circle2.y - circle1.y),
                    oPos0.y + h / d * (circle2.x - circle1.x));
        }
        return points;
    }



    public static Point[] getCirclesIntersection(Point circle1, double r1, Point circle2, double r2) {
        return getCirclesIntersection(circle1, r1, circle2, r2, 0.001);
    }

    public static Point[] getLineAndCircleIntersection(Point point1, Point point2, Point circle, double radius, boolean perpendicular, double EPS) {

        Point[] points; // результат

        //высчитываем параметры прямой
        double A = point1.y - point2.y;
        double B = point2.x - point1.x;
        //последние два слагаемых - смещаем прямую так, чтобы центр окружности оказалась в начале координат
        double C = point1.x * point2.y - point2.x * point1.y + A * circle.x + B * circle.y;

        if (perpendicular) {
            double temp = B;
            B = -A;
            A = temp;
            C = 0;
        }
        //точка прямой, ближайщая к центру окружности
        double x0 = -A / (A * A + B * B) * C + circle.x;
        double y0 = -B / (A * A + B * B) * C + circle.y;

        //расстояние от центра планеты до прямой
        double l = Math.abs(C) / Math.sqrt(A * A + B * B);

        if (l > radius) {
            points = null;
        } else if (Math.abs(l - radius) < EPS) {
            points = new Point[2];
            points[0] = new Point(x0, y0);
            points[1] = new Point(x0, y0);
        } else {
            //расстояние от нужных нам точек до точки (x0,y0), возведённое в квадрат
            double sqrd = Math.pow(radius, 2) - C * C / (A * A + B * B);
            //нормирование вектора AB к расстоянию d
            double mult = Math.sqrt(sqrd / (A * A + B * B));

            points = new Point[2];
            points[0] = new Point(x0 + B * mult, y0 - A * mult);
            points[1] = new Point(x0 - B * mult, y0 + A * mult);
        }
        return points;
    }

    public static Point[] getLineAndCircleIntersection(Point point1, Point point2, Point circle, double radius, boolean perpendicular) {
        return getLineAndCircleIntersection(point1, point2, circle, radius, perpendicular, 0.5);
    }

    public static Point[] getLineAndCircleIntersection(Point point1, Point point2, Point circle, double radius, double EPS) {
        return getLineAndCircleIntersection(point1, point2, circle, radius, false, EPS);

    }

    public static Point[] getLineAndCircleIntersection(Point point1, Point point2, Point circle, double radius) {
        return getLineAndCircleIntersection(point1, point2, circle, radius, false, 0.5);
    }

    public static boolean isLineAndCircleIntersect(Point point1, Point point2, Point circle, double radius, double EPS) {
        double A = point1.y - point2.y;
        double B = point2.x - point1.x;
        //последние два слагаемых - смещаем прямую так, чтобы центр окружности оказалась в начале координат
        double C = point1.x * point2.y - point2.x * point1.y + A * circle.x + B * circle.y;
        //расстояние от центра планеты до прямой
        double l = Math.abs(C) / Math.sqrt(A * A + B * B);
        return (l < radius || Math.abs(l - radius) < EPS);
    }

    public static boolean isLineAndCircleIntersect(Point point1, Point point2, Point circle, double radius) {
        return isLineAndCircleIntersect(point1, point2, circle, radius, 0.5);
    }

    public static Point getMiddlePoint(Point point1, Point point2) {
        return new Point((point1.x + point2.x) / 2.0, (point1.y + point2.y) / 2.0);
    }

    public static Point[] getTangencyPoints(Point point, Point circle, double radius) {
        double r = Geometry.distance(point, circle) / 2;
        Point tempCircle = getMiddlePoint(point, circle);
        return Geometry.getCirclesIntersection(tempCircle, r, circle, radius);
    }

    public static Point subtract(Point from, Point value) {
        return new Point(from.x - value.x, from.y - value.y);
    }

    public static Point divideBy(Point point, double devider) {
        return new Point(point.x / devider, point.y / devider);
    }
}
