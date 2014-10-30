import javax.swing.*;
import java.awt.*;

public class Test extends JFrame {
    public static void main(String[] args) {
        JFrame frame = new JFrame();

        MyPanel p = new MyPanel();
        frame.add(p);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    static class MyPanel extends JPanel {

        double A = -1, B = 2, C = -4;

        MyPanel() {
        }


        @Override
        public void paintComponent(Graphics g) {
            g.setColor(Color.red);

            int topOffset = 600;
            int x1 = 50, x2 = 500;

            g.drawLine(x1, (int) y(x1) + topOffset, x2, (int) y(x2) + topOffset);
        }

        double y(double x) {
            double y = (A * x + C) / B;
            System.out.println("x: " + x);
            System.out.println("y: " + y);
            return y;
        }
    }

    static Point[] findPoints(Point point1, Point point2, Point circle, double radius, boolean perpendicular) {

        Point[] points; // результат
        double EPS = 1; // погрешность

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
            points = new Point[1];
            points[0] = new Point(x0, y0);
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
}
