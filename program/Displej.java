import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

/**
 * Zobrazovac okna Hry
 * 
 * @author y0hn
 * @version v0.1
 */
public class Displej {
    private JFrame okno;

    /**
     * Vytvori okno Displeja 
     * @param nazovOkna zobrazovany v hlavicke okna
     * @param rozmer Vektor velkosti okna
     */
    public Displej(String nazovOkna, Vektor rozmer) {
        this.okno = new JFrame(nazovOkna);
        this.okno.setBounds(0, 0, rozmer.getIntX(), rozmer.getIntY());
        this.okno.setResizable(false);

        // ukoncenie Hry
        this.okno.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.okno.addWindowListener(new WindowAdapter() {
            //@Override
            public void windowClosing(WindowEvent e) {
                Hra.zavri();
                Displej.this.okno.dispose();
            }
        });
        
        this.okno.setVisible(true);
    }

    public void vykresli() {
        // vykresli aktualnu miestnost 
        // vykresli hraca
        // vykresli nepriatelov ;)
    }
}
