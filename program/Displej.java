import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLayeredPane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

/**
 * Zobrazovac okna Hry
 * 
 * @author y0hn
 * @version v0.10
 */
public class Displej {
    private static final int POSUN_ROZMERU_X = 10;
    private static final int POSUN_ROZMERU_Y = 36;

    private static final int VRSTVA_PODLAHA = 0;
    private static final int VRSTVA_STENA = 1;
    public static final int VRSTVA_STRELA = 2;
    private static final int VRSTVA_VYHRA = 2;
    private static final int VRSTVA_HRAC = 3;
    private static final int VRSTVA_NEPRIATEL = 3;
    private static final int VRSTVA_UI_HRAC = 4;
    private static final int VRSTVA_UI = 5;

    private static final Font FONT = new Font("Arial", 1, 100);

    private static Rozmer2D rozmer;
    /**
     * Vrati Rozmer2D Displeja
     * @return [0,0]x[vX,vY]
     */
    public static Rozmer2D getRozmer() {
        return Displej.rozmer;
    }
    /**
     * Vrati Vektor2D stred Displeja
     * @return [vX/2, vY/2]
     */
    public static Vektor2D getStred() {
        return Displej.rozmer.ziskajStred();
    }

    private JFrame okno;
    private JLayeredPane aktivnaMiestnost;
    private JButton[] uzivatelskeRozhranie;
    private boolean restart;

    /**
     * Vytvori okno Displeja 
     * @param nazovOkna zobrazovany v hlavicke okna
     * @param rozmer Vektor2D velkosti okna
     */
    public Displej(String ikonaOkna, String nazovOkna, Vektor2D rozmerOkna) {
        Displej.rozmer = new Rozmer2D(Vektor2D.ZERO, rozmerOkna);
        Vektor2D velkost = new Vektor2D(POSUN_ROZMERU_X, POSUN_ROZMERU_Y);
        velkost = velkost.sucet(Displej.rozmer.getVelkost());
        Rozmer2D skutocnyRozmer2D = new Rozmer2D(Displej.rozmer.getPozicia(), velkost);

        this.restart = false;

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

        this.uzivatelskeRozhranie = this.vytvorGrafikuUI();
    }
    /**
     * Vrati Hlavne Zobrazovacie Okno
     * @return hlave okno
     */
    public JFrame getOkno() {
        return this.okno;
    }
    public boolean ziskajRestart() {
        boolean restartuj = this.restart;
        if (restartuj) {
            this.restart = false;
        }
        return restartuj;
    }
    /**
     * Vytvori Hracovi objekt na Displeji 
     * @param h objekt Hraca
     */
    public void nastavHraca(Hrac objektHraca) {
        JPanel grafika = new JPanel();
        this.aktivnaMiestnost.setLayer(grafika, VRSTVA_HRAC);
        this.aktivnaMiestnost.add(grafika);
        objektHraca.getTelo().setGrafika(grafika, Color.GREEN);
        
        JPanel[] zivotyUI = new JPanel[objektHraca.getTelo().getMaxZdravie()];
        Rozmer2D rozmerZivota = Hrac.GRAFIKA_ZIVOTOV_HRACA;
        Vektor2D velkostZivota = new Vektor2D(rozmerZivota.getVelkostX() / zivotyUI.length, rozmerZivota.getVelkostY());
        rozmerZivota = new Rozmer2D(rozmerZivota.getPozicia(), velkostZivota);

        for (int i = 0; i < zivotyUI.length; i++) {
            JPanel zivot = new JPanel();
            zivot.setBackground(Color.PINK);
            zivot.setBounds(rozmerZivota.vytvorRectangle());
            this.aktivnaMiestnost.setLayer(zivot, VRSTVA_UI_HRAC);
            this.aktivnaMiestnost.add(zivot);
            zivotyUI[i] = zivot;

            Vektor2D novaPozica = new Vektor2D(rozmerZivota.getPoziciaX() + rozmerZivota.getIntVeX(), rozmerZivota.getPoziciaY());
            rozmerZivota.setPozicia(novaPozica);
        }
        objektHraca.setUI(zivotyUI);
    }
    /**
     * Zmeni Miestnost vykreslovanu na Displej
     * @param m nova aktivna Mistnost
     */
    public void zmenAktivnuMiestnost(Miestnost m, Hrac h) {
        if (this.aktivnaMiestnost != null) {
            this.okno.remove(this.aktivnaMiestnost);
        }
        this.aktivnaMiestnost = this.vytvorGrafikuMiestnosti(m);
        this.aktivnaMiestnost.setLayout(null);
        this.okno.setContentPane(this.aktivnaMiestnost);
        
        JPanel grafika = h.getTelo().getGrafika();
        if (null != grafika) {
            this.aktivnaMiestnost.setLayer(grafika, VRSTVA_HRAC);
            this.aktivnaMiestnost.add(grafika);

            for (JPanel cast : h.getUI()) {
                this.aktivnaMiestnost.setLayer(cast, VRSTVA_UI_HRAC);
                this.aktivnaMiestnost.add(cast);
            }
            for (JButton tlacitko : this.uzivatelskeRozhranie) {
                this.aktivnaMiestnost.setLayer(tlacitko, VRSTVA_UI);
                this.aktivnaMiestnost.add(tlacitko);
            }
        }
    }
    /**
     * Nastavi konecnu grafiku podla stavu hry
     * @param stav 
     */
    public void nastavGrafikuPreStavHry(StavHry stav, boolean zapni) {
        Rozmer2D r = zapni ? Displej.getRozmer() : Rozmer2D.ZERO;
        this.uzivatelskeRozhranie[stav.ordinal()].setBounds(r.vytvorRectangle());
    }
        
    private JLayeredPane vytvorGrafikuMiestnosti(Miestnost m) {
        JLayeredPane miestnost = new JLayeredPane();
        miestnost.setBounds(Displej.rozmer.vytvorRectangle());

        for (Rozmer2D[] rozmery : m.getRozmery2D()) {
            for (Rozmer2D r : rozmery) {
                JPanel stena = this.vytvorGrafikuMuru(r);
                miestnost.setLayer(stena, VRSTVA_STENA);
                miestnost.add(stena);
            }
        }

        for (Nepriatel nepriatel : m.getNepriatelia()) {
            JPanel grafika = new JPanel();
            miestnost.setLayer(grafika, VRSTVA_NEPRIATEL);
            miestnost.add(grafika);
            nepriatel.setGrafika(grafika);
        }

        JPanel podlaha = new JPanel();
        podlaha.setBackground(Color.GRAY); // docasne
        podlaha.setBounds(Displej.getRozmer().vytvorRectangle());
        podlaha.setLayout(new BorderLayout());
        miestnost.setLayer(podlaha, VRSTVA_PODLAHA);
        miestnost.add(podlaha);

        JLabel label = new JLabel();
        label.setText(m.getIndex() + "");
        label.setForeground(Color.DARK_GRAY);
        label.setLayout(new BorderLayout());
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);    
        label.setFont(FONT);    
        podlaha.add(label, BorderLayout.CENTER);

        Rozmer2D vyhra = m.getVyhernaPlocha();
        if (vyhra != null) {
            JPanel vyhernaPlocha = new JPanel();
            vyhernaPlocha.setBackground(Color.YELLOW);
            vyhernaPlocha.setBounds(vyhra.vytvorRectangle());
            miestnost.setLayer(vyhernaPlocha, VRSTVA_VYHRA);
            miestnost.add(vyhernaPlocha);
        }

        return miestnost;
    }
    private JPanel vytvorGrafikuMuru(Rozmer2D rozmer) {
        JPanel stena = new JPanel();
        stena.setBounds(rozmer.vytvorRectangle());
        stena.setBackground(Color.BLACK);
        return stena;
    }
    private JButton[] vytvorGrafikuUI() {
        JButton[] tlacitka = new JButton[StavHry.values().length - 1];
        
        for (int i = 0; i < StavHry.values().length; i++) {
            StavHry sh = StavHry.values()[i]; 
            if (!sh.getText().equals("")) {                
                JButton jb = new JButton();
                jb.setBackground(sh.getFarbaPozadia());
                jb.setBounds(Displej.getRozmer().vytvorRectangle());
                jb.setHorizontalAlignment(SwingConstants.CENTER);
                jb.setVerticalAlignment(SwingConstants.CENTER);
                jb.setLayout(null);

                jb.setFocusable(false);
                jb.setContentAreaFilled(false);
                jb.setRolloverEnabled(false);
                jb.setBorderPainted(false);
                jb.setFocusPainted(false);
                jb.setOpaque(true);
                jb.setBounds(Rozmer2D.ZERO.vytvorRectangle());
                jb.addActionListener(a -> {
                    this.restart = true;
                });
                tlacitka[i] = jb;

                JLabel jl = new JLabel();
                jl.setFont(FONT);
                jl.setBounds(Displej.getRozmer().vytvorRectangle());
                jl.setHorizontalAlignment(SwingConstants.CENTER);
                jl.setVerticalAlignment(SwingConstants.CENTER);
                jl.setForeground(sh.getFarbaTextu());
                jl.setText(sh.getText());
                jb.add(jl, BorderLayout.CENTER);
            }
        }
        return tlacitka;
    }
}
