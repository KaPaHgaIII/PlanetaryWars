import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UI {
    Gameplay gameplay;

    public UI(Gameplay gameplay) {
        this.gameplay = gameplay;
    }

    public void start() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception ignored) {
                }


                final JFrame frame = new JFrame("Planetary Wars");

                //frame.addMouseMotionListener(new MyMouseMotionListener());

                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());

                frame.setSize(800, 600);

                PlanetsPanel pp = new PlanetsPanel(gameplay.planets);
//                pp.setLayout(null);

                PlanetMouseListener pml = new PlanetMouseListener(gameplay);
//                PlanetMouseMotionListener pmml = new PlanetMouseMotionListener(gameplay);

                pp.addMouseMotionListener(pml);
                pp.addMouseListener(pml);


                ShipsPanel sp = new ShipsPanel(gameplay.shipSets);

                JLayeredPane lp = frame.getLayeredPane();

                pp.setSize(800,600);
                sp.setSize(800,600);

                pp.setOpaque(false);
                sp.setOpaque(false);

                lp.add(sp, new Integer(1));
                lp.add(pp, new Integer(2));

                Timer timer = new Timer(40, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        frame.repaint();
                    }

                });
                timer.setCoalesce(true);
                timer.start();



                //frame.pack();
                frame.setSize(800,600);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }

        });
    }


}
