import java.awt.BorderLayout;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
//import java.awt.Image;
//import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Zobrazovac okna Hry
 * 
 * @author y0hn
 * @version v0.2
 */
public class Displej {
    private JFrame okno;
    private JPanel panel;
    private Graphics2D grafika;

    /**
     * Vytvori okno Displeja 
     * @param nazovOkna zobrazovany v hlavicke okna
     * @param rozmer Vektor velkosti okna
     */
    public Displej(String nazovOkna, Vektor rozmer) {
        this.okno = new JFrame(nazovOkna);
        this.okno.setBounds(0, 0, rozmer.getIntX(), rozmer.getIntY());
        this.okno.setResizable(false);

        this.panel = new JPanel(new BorderLayout());

        this.grafika = (Graphics2D)this.panel.getGraphics();

        // ukoncenie Hry
        this.okno.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.okno.addWindowListener(new WindowAdapter() {
            //@Override
            public void windowClosing(WindowEvent e) {
                Displej.this.okno.dispose();
                System.exit(0);
            }
        });        
        this.okno.setVisible(true);
    }

    /**
     * Vykresli vec na Displej
     */
    public void vykresli() {
        // vykresli aktualnu miestnost 
        // vykresli hraca
        // vykresli nepriatelov ;)
    }
}
