import javax.swing.*;
import java.awt.*;
import java.util.Set;

public class ShipsPanel extends JPanel {

    Set<Set<Ship>> shipSets;

    public ShipsPanel(Set<Set<Ship>> shipSets) {
        this.shipSets = shipSets;
        //setBackground(Color.BLACK);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(800, 600);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

//        g2d.setColor(Color.red);

        for (Set<Ship> ships : shipSets) {
            for (Ship ship : ships) {
                g2d.setColor(Color.green);

                /*if (ship.A != 0) {
                    for (int x = 0; x < 800; x+=5) {
                        for (int y = 0; y < 600; y+=5) {
                            if (Math.abs(ship.A * x + ship.B * y + ship.C) < 10) {
                                g2d.fillOval(x - 3, y - 3, 6, 6);
                            }
                        }
                    }
                }*/

                /*g2d.setColor(Color.blue);
                for (Point point : ship.points) {

                    //System.out.println(point.x + " + " + point.y);
                    g2d.fillOval((int) point.x - 3, (int) point.y - 3, 6, 6);
                }
                g2d.setColor(Color.red);
                for (Point point : ship.helpPoints) {

                    //System.out.println(point.x + " + " + point.y);
                    g2d.fillOval((int) point.x - 3, (int) point.y - 3, 6, 6);
                }*/

                /*if (ship.points.size() > 2) {
                    g2d.fillOval((int) ship.points.get(0).x - 3, (int) ship.points.get(0).y - 3, 6, 6);
                    g2d.drawLine((int) ship.points.get(1).x, (int) ship.points.get(1).y, (int) ship.points.get(2).x, (int) ship.points.get(2).y);
                }*/
                g2d.setColor(Color.red);
                if (!ship.arrived) {
                    g2d.fillOval((int) ship.getX() - 3, (int) ship.getY() - 3, 6, 6);
                }
            }
        }

        g2d.dispose();

    }

}
