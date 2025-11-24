import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Color;
//import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JLayeredPane;

/**
 * Zobrazovac okna Hry
 * 
 * @author y0hn
 * @version v0.6
 */
public class Displej {
    private static Rozmer2D rozmer;
    /**
     * Vrati Rozmer2D Displeja
     * @return [0,0]x[vX,vY]
     */
    public static Rozmer2D getRozmer() {
        return rozmer;
    }

    private JFrame okno;
    private JPanel hrac;
    private JLayeredPane aktivnaMiestnost;

    /**
     * Vytvori okno Displeja 
     * @param nazovOkna zobrazovany v hlavicke okna
     * @param rozmer Vektor2D velkosti okna
     */
    public Displej(String ikonaOkna, String nazovOkna, Vektor2D rozmerOkna) {
        Displej.rozmer = new Rozmer2D(Vektor2D.zero(), rozmerOkna);
        this.okno = new JFrame();

        // hlavicka okna
        if (!ikonaOkna.equals("")) {
            this.okno.setIconImage(new ImageIcon(ikonaOkna).getImage());
        }
        if (!nazovOkna.equals("")) {
            this.okno.setTitle(nazovOkna);
        }

        // nastavenie suradnic a velkost okna
        this.okno.setBounds(0, 0, rozmerOkna.getIntX() + 13, rozmerOkna.getIntY() + 36);
        this.okno.setResizable(false);
        
        // ukoncenie Hry pri zatvoreni okna
        this.okno.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        this.okno.setVisible(true);
        //System.out.println(rozmer.getPozicia() + " + " + rozmer.getVelkost());
    }
    /**
     * Vrati Hlavne Zobrazovacie Okno
     * @return hlave okno
     */
    public JFrame getOkno() {
        return this.okno;
    }
    /**
     * Vytvori Hracovi objekt na Displeji 
     * @param h objekt Hraca
     */
    public void nastavHraca(Hrac h) {
        this.hrac = new JPanel();

        Vektor2D pozicia = h.getTelo().getPozicia();
        double polomer = h.getTelo().getPolomer();
        pozicia = new Vektor2D(pozicia.getX() - polomer, pozicia.getY() - polomer);

        int velkost = (int)Math.round(polomer) * 2;
        this.hrac.setBounds(pozicia.getIntX(), pozicia.getIntY(), velkost, velkost);
        this.hrac.setBackground(Color.red);
        this.aktivnaMiestnost.setLayer(this.hrac, 3);
        this.aktivnaMiestnost.add(this.hrac);
        this.hrac = (JPanel)this.aktivnaMiestnost.getComponentAt(pozicia.getIntX(), pozicia.getIntY());
    }
    /**
     * Obnovi Hracovi poziciu na Displeji 
     * @param h objekt Hraca
     */
    public void obnovHraca(Hrac h) {
        Vektor2D pozicia = h.getTelo().getPozicia();
        this.hrac.setLocation(pozicia.getIntX(), pozicia.getIntY());
    }
    /**
     * Zmeni Miestnost vykreslovanu na Displej
     * @param m nova aktivna Mistnost
     */
    public void zmenAktivnuMiestnost(Miestnost m) {
        if (this.aktivnaMiestnost != null) {
            this.okno.remove(this.aktivnaMiestnost);
        }
        this.aktivnaMiestnost = this.vytvorGrafikuMiestnosti(m);
        this.aktivnaMiestnost.setLayout(null);
        this.okno.setContentPane(this.aktivnaMiestnost);
    }
    
    private JLayeredPane vytvorGrafikuMiestnosti(Miestnost m) {
        JLayeredPane miestnost = new JLayeredPane();
        miestnost.setBounds(Displej.getRozmer().vytvorRectangle());

        for (int i = 0; i < 4; i++) {
            Rozmer2D[] rozmery = m.getStena(Smer.toSmer(i)).getRozmery();

            for (int ii = 0; ii < rozmery.length; ii++) {
                // planovana zmena
                JPanel stena = this.vytvorGrafikuMuru(rozmery[ii], (ii + 1) * (i + 1) * 10);
                miestnost.setLayer(stena, 1);
                miestnost.add(stena);
            }
        }

        JPanel podlaha = new JPanel();
        podlaha.setBackground(Color.GREEN); // docasne
        podlaha.setBounds(Displej.getRozmer().vytvorRectangle());
        miestnost.setLayer(podlaha, 0);
        miestnost.add(podlaha);

        return miestnost;
    }

    private JPanel vytvorGrafikuMuru(Rozmer2D rozmer, int i) {
        JPanel stena = new JPanel();
        stena.setBounds(rozmer.vytvorRectangle());
        Color c = new Color(i, i, i); // docasne
        stena.setBackground(c);
        return stena;
    }
}
