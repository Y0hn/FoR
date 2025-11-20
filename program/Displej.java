import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JLayeredPane;

/**
 * Zobrazovac okna Hry
 * 
 * @author y0hn
 * @version v0.4
 */
public class Displej {
    private JFrame okno;
    private JLayeredPane platno;

    /**
     * Vytvori okno Displeja 
     * @param nazovOkna zobrazovany v hlavicke okna
     * @param rozmer Vektor velkosti okna
     */
    public Displej(String ikonaOkna, String nazovOkna, Vektor rozmerOkna) {
        this.okno = new JFrame();
        this.platno = new JLayeredPane();

        // hlavicka okna
        if (!ikonaOkna.equals("")) {
            this.okno.setIconImage(new ImageIcon(ikonaOkna).getImage());
        }
        if (!nazovOkna.equals("")) {
            this.okno.setTitle(nazovOkna);
        }

        // nastavenie suradnic a velkost okna
        this.okno.setBounds(0, 0, rozmerOkna.getIntX() + 14, rozmerOkna.getIntY() + 37);
        this.okno.setResizable(false);
        
        // ukoncenie Hry pri zatvoreni okna
        this.okno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.okno.add(this.platno);
        this.okno.setVisible(true);        
    }

    /**
     * Vykresli aktivnu Miestnost na Displej
     * @param m aktivna Mistnost
     */
    public void nastavAktivnuMiestnost(Miestnost m) {
        // odstran predoslu Miestnost
        
        // vykresli grafiku v poradi od najvrchnejsej
        /*for (JPanel grafika : m.getGrafika()) {
            this.okno.add(grafika);
        }
        JPanel[] jp = m.getGrafika(); 
        for (int i = 0; i < jp.length; i++) {
            this.platno.add(jp[i], i);
        }*/
        this.okno.add(vytvorGrafikuMiestnosti(m));
    }
    /**
     * Odstrani predoslu Miestnost z Displeja
     * @param m predosla Miestnost
     */
    public void odstranAktivnuMiestnost(Miestnost m) {
        /*if (m == null) {
            return;
        }
        // odstrani grafiku v poradi od najvrchnejsej
        for (JPanel grafika : m.getGrafika()) {
            this.okno.remove(grafika);
        }*/
    }
    private JLayeredPane vytvorGrafikuMiestnosti(Miestnost m) {
        JLayeredPane miestnost = new JLayeredPane();
        miestnost.setBounds(0, 0, Hra.getRozmerDisplay().getIntX(), Hra.getRozmerDisplay().getIntY());

        for (int i = 0; i < 4; i++) {
            Vektor[][] rozmery = m.getStena(Smer.toSmer(i)).getRozmery();

            for (int ii = 0; ii < rozmery.length; ii++) {
                JPanel stena = this.vytvorGrafikuSteny(rozmery[ii], (ii+1)*(i+1)*10);
                miestnost.setLayer(stena, 1);
                miestnost.add(stena);
            }
        }

        JPanel podlaha = new JPanel();
        podlaha.setBackground(Color.GREEN);
        podlaha.setBounds(0, 0, Hra.getRozmerDisplay().getIntX(), Hra.getRozmerDisplay().getIntY());
        miestnost.setLayer(podlaha, 0);
        miestnost.add(podlaha);

        return miestnost;
    }
    private JPanel vytvorGrafikuSteny(Vektor[] rozmery, int i) {
        JPanel stena = new JPanel();
        stena.setBounds(rozmery[0].getIntX(), rozmery[0].getIntY(), rozmery[1].getIntX(), rozmery[1].getIntY());
        Color c = new Color(i,i,i);
        stena.setBackground(c);
        return stena;
    }
}
