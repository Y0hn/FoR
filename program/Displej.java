import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.ImageIcon;

/**
 * Zobrazovac okna Hry
 * 
 * @author y0hn
 * @version v0.3
 */
public class Displej {
    private JFrame okno;

    /**
     * Vytvori okno Displeja 
     * @param nazovOkna zobrazovany v hlavicke okna
     * @param rozmer Vektor velkosti okna
     */
    public Displej(String ikonaOkna, String nazovOkna, Vektor rozmerOkna) {
        this.okno = new JFrame();

        // hlavicka okna
        if (!ikonaOkna.equals("")) {
            this.okno.setIconImage(new ImageIcon(ikonaOkna).getImage());
        }
        if (!nazovOkna.equals("")) {
            this.okno.setTitle(nazovOkna);
        }

        // nastavenie suradnic a velkost okna
        this.okno.setBounds(0, 0, rozmerOkna.getIntX() + 15, rozmerOkna.getIntY() + 39);
        this.okno.setResizable(false);

        // ukoncenie Hry pri zatvoreni okna
        this.okno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.okno.setVisible(true);        
    }

    /**
     * Vykresli aktivnu Miestnost na Displej
     * @param m aktivna Mistnost
     */
    public void nastavAktivnuMiestnost(Miestnost m) {
        // odstran predoslu Miestnost

        // vykresli grafiku v poradi od najvrchnejsej
        for (JPanel grafika : m.getGrafika()) {
            this.okno.add(grafika);
        }
    }
}
