import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JLayeredPane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

/**
 * Zobrazovac okna Hry
 * 
 * @author y0hn
 * @version v0.7
 */
public class Displej {
    private static final int POSUN_ROZMERU_X = 10;
    private static final int POSUN_ROZMERU_Y = 36;
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
        Vektor2D velkost = new Vektor2D(POSUN_ROZMERU_X, POSUN_ROZMERU_Y);
        velkost = velkost.sucet(Displej.rozmer.getVelkost());
        Rozmer2D skutocnyRozmer2D = new Rozmer2D(Displej.rozmer.getPozicia(), velkost);

        this.okno = new JFrame();

        // hlavicka okna
        if (!ikonaOkna.equals("")) {
            this.okno.setIconImage(new ImageIcon(ikonaOkna).getImage());
        }
        if (!nazovOkna.equals("")) {
            this.okno.setTitle(nazovOkna);
        }

        // nastavenie suradnic a velkost okna
        this.okno.setBounds(skutocnyRozmer2D.vytvorRectangle());
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
        double priemer = h.getTelo().getPolomer() * 2;

        Rozmer2D rozmerHraca = new Rozmer2D(pozicia, new Vektor2D(priemer, priemer));

        this.hrac.setBounds(rozmerHraca.vytvorRectangle());
        this.hrac.setBackground(Color.red);
        this.aktivnaMiestnost.setLayer(this.hrac, 3);
        this.aktivnaMiestnost.add(this.hrac);

        // ziska spatnu referenciu
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
        
        if (this.hrac != null) {
            this.aktivnaMiestnost.setLayer(this.hrac, 3);
            this.aktivnaMiestnost.add(this.hrac);
        }
    }
        
    private JLayeredPane vytvorGrafikuMiestnosti(Miestnost m) {
        JLayeredPane miestnost = new JLayeredPane();
        miestnost.setBounds(Displej.rozmer.vytvorRectangle());

        for (Rozmer2D[] rozmery : m.getRozmery2D()) {
            for (Rozmer2D r : rozmery) {
                JPanel stena = this.vytvorGrafikuMuru(r);
                miestnost.setLayer(stena, 1);
                miestnost.add(stena);
            }
        }

        JPanel podlaha = new JPanel();
        podlaha.setBackground(Color.GREEN); // docasne
        podlaha.setBounds(Displej.getRozmer().vytvorRectangle());
        podlaha.setLayout(new BorderLayout());

        JLabel label = new JLabel();
        label.setText(m.getIndex() + "");
        label.setForeground(Color.BLUE);
        label.setLayout(new BorderLayout());;
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);    
        label.setFont(new Font("Arial", 1, 100));    
        podlaha.add(label, BorderLayout.CENTER);

        miestnost.setLayer(podlaha, 0);
        miestnost.add(podlaha);

        return miestnost;
    }
    private JPanel vytvorGrafikuMuru(Rozmer2D rozmer) {
        JPanel stena = new JPanel();
        stena.setBounds(rozmer.vytvorRectangle());
        stena.setBackground(Color.BLACK);
        return stena;
    }
}
