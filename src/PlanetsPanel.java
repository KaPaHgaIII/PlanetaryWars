import javax.swing.*;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.util.Hashtable;
import java.util.Map;

public class PlanetsPanel extends JPanel {

    int i = 0;

    java.util.List<Planet> planets;


    public PlanetsPanel(java.util.List<Planet> planets) {
        this.planets = planets;
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

        for (Planet planet : planets) {
            g2d.setColor(planet.COLOR);
            g2d.fillOval((int) planet.getX() - planet.radius, (int) planet.getY() - planet.radius, planet.radius * 2, planet.radius * 2);

            String text = planet.shipsCount.toString();

            Map<TextAttribute, Object> attrs = new Hashtable<TextAttribute, Object>();
            attrs.put(TextAttribute.SIZE, 20);
            Font font = Font.getFont(attrs);

            g2d.setFont(font);
            FontMetrics fm = g2d.getFontMetrics(font);
            int fontWidth = (int) fm.getStringBounds(text, g2d).getWidth();
            int fontHeight = fm.getMaxAscent() - fm.getMaxDescent();

            g2d.setColor(Color.white);
            g2d.drawString(text, (int) planet.getX() - fontWidth / 2, (int) planet.getY() + fontHeight / 2);
        }

        g2d.dispose();
    }


}