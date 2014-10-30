import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashSet;
import java.util.Set;

public class PlanetMouseListener extends JPanel implements MouseListener, MouseMotionListener {
    Gameplay gameplay;
    Set<Planet> fromList = new HashSet<Planet>();
    Planet to;
    boolean start;

    public PlanetMouseListener(Gameplay gameplay) {
        this.gameplay = gameplay;
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        fromList.clear();
        start = true;
//        fromList = null;

        /*for (Planet planet : gameplay.planets) {
            double distance = Math.sqrt(Math.pow(mouseEvent.getX() - planet.getX(), 2) +
                    Math.pow(mouseEvent.getY() - planet.getY(), 2));
            if (distance < planet.radius) {
                fromList = planet;
                break;
            }
        }*/

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

        start = false;

        if (fromList.isEmpty()) {
            return;
        }

        to = null;

        for (Planet planet : gameplay.planets) {
            double distance = Math.sqrt(Math.pow(mouseEvent.getX() - planet.getX(), 2) +
                    Math.pow(mouseEvent.getY() - planet.getY(), 2));
            if (distance < planet.radius) {
                to = planet;
                break;
            }
        }

        if(to==null){
            return;
        }

        fromList.remove(to);

        gameplay.sendShips(fromList, to);
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
    }


    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        mouseMoved(mouseEvent);
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
        for (Planet planet : gameplay.planets) {
            double distance = Math.sqrt(Math.pow(mouseEvent.getX() - planet.getX(), 2) +
                    Math.pow(mouseEvent.getY() - planet.getY(), 2));
            if (distance < planet.radius) {
                mouseEvent.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                if (start) {
                    fromList.add(planet);
                }
                return;
            }
        }
        mouseEvent.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

    }
}
