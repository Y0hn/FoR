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
 * @version v0.11
 */
public class Displej {    
    public static final int VRSTVA_STRELA = 2;

    private static final int VRSTVA_PODLAHA = 0;
    private static final int VRSTVA_STENA = 1;
    private static final int VRSTVA_ROH = 2;
    private static final int VRSTVA_PLOCHA = 2;
    private static final int VRSTVA_HRAC = 3;
    private static final int VRSTVA_NEPRIATEL = 3;
    private static final int VRSTVA_UI_HRAC = 4;
    private static final int VRSTVA_UI = 5;

    private static final Font FONT = new Font("Arial", 1, 100);
    private static final int POSUN_ROZMERU_X = 10;
    private static final int POSUN_ROZMERU_Y = 36;
    private static final double VELKOST_ROHU = 1.05;

    private static final String GRAFIKA_HRAC = "assets/player.png";
    private static final String GRAFIKA_NEPIRATEL = "assets/enemy.png";

    private final JFrame okno;
    private final JButton[] uzivatelskeRozhranie;
    private JLayeredPane aktivnaMiestnost;
    private boolean restart;

    /**
     * Vytvori okno Displeja 
     * @param ikonaOkna cesta ku Ikone okna
     * @param nazovOkna zobrazovany v hlavicke okna
     * @param rozmer Vektor2D velkosti okna
     */
    public Displej(String ikonaOkna, String nazovOkna, Rozmer2D rozmer) {
        Vektor2D velkost = new Vektor2D(POSUN_ROZMERU_X, POSUN_ROZMERU_Y);
        velkost = rozmer.getVelkost().sucet(velkost);
        Rozmer2D skutocnyRozmer2D = new Rozmer2D(rozmer.getPozicia(), velkost);

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
    /**
     * Ziska hodnotu restartu iba 1 krat
     * @return PRAVDA ak sa ma Displej restartovat
     */
    public boolean ziskajRestart() {
        boolean restartuj = this.restart;
        if (restartuj) {
            this.restart = false;
        }
        return restartuj;
    }
    /**
     * Vytvori Hracovi objekt na Displeji 
     * @param objektHraca
     */
    public void nastavHraca(Hrac objektHraca) {
        OtacanaGrafika grafika = new OtacanaGrafika(GRAFIKA_HRAC);
        this.aktivnaMiestnost.setLayer(grafika, VRSTVA_HRAC);
        this.aktivnaMiestnost.add(grafika);
        objektHraca.getTelo().setGrafika(grafika, Color.GREEN);
        
        JPanel[] zivotyUI = new JPanel[objektHraca.getTelo().getMaxZdravie()];
        Rozmer2D rozmerZivota = Hrac.GRAFIKA_ZIVOTOV_HRACA;
        Vektor2D velkostZivota = new Vektor2D(rozmerZivota.getVelkostX() / zivotyUI.length, rozmerZivota.getVelkostY());
        rozmerZivota = new Rozmer2D(rozmerZivota.getPozicia(), velkostZivota);

        for (int i = 0; i < zivotyUI.length; i++) {
            JPanel zivot = new JPanel();
            zivot.setBackground(Color.RED);
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
     * @param h objekt Hraca
     */
    public void zmenAktivnuMiestnost(Miestnost m, Hrac h) {
        if (this.aktivnaMiestnost != null) {
            this.okno.remove(this.aktivnaMiestnost);
        }
        this.aktivnaMiestnost = this.vytvorGrafikuMiestnosti(m);
        this.aktivnaMiestnost.setLayout(null);
        this.okno.setContentPane(this.aktivnaMiestnost);
        
        OtacanaGrafika grafika = h.getTelo().getGrafika();
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
     * @param stav prideleny Stav Hry pre grafiku
     * @param zapni ak PRAVDA zapne grafiku inak vypne
     */
    public void nastavGrafikuPreStavHry(StavHry stav, boolean zapni) {
        Rozmer2D r = zapni ? Hra.ROZMER_OKNA : Rozmer2D.ZERO;
        this.uzivatelskeRozhranie[stav.ordinal()].setBounds(r.vytvorRectangle());
    }
        
    private JLayeredPane vytvorGrafikuMiestnosti(Miestnost m) {
        JLayeredPane miestnost = new JLayeredPane();
        miestnost.setBounds(Hra.ROZMER_OKNA.vytvorRectangle());

        for (Rozmer2D[] rozmery : m.getRozmery2D()) {
            for (Rozmer2D r : rozmery) {
                JPanel stena = this.vytvorGrafikuMuru(r);
                miestnost.setLayer(stena, VRSTVA_STENA);
                miestnost.add(stena);
            }
        }

        for (Nepriatel nepriatel : m.getNepriatelia()) {
            OtacanaGrafika grafika = new OtacanaGrafika(GRAFIKA_NEPIRATEL);
            miestnost.setLayer(grafika, VRSTVA_NEPRIATEL);
            miestnost.add(grafika);
            nepriatel.setGrafika(grafika);
        }

        JPanel podlaha = new JPanel();
        podlaha.setBackground(Color.GRAY); // docasne
        podlaha.setBounds(Hra.ROZMER_OKNA.vytvorRectangle());
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

        SpecialnaPlocha plocha = m.getSpecialnaPlocha();
        if (plocha != null) {
            JPanel grafikaPlochy = new JPanel();
            grafikaPlochy.setBackground(plocha.getFarba());
            grafikaPlochy.setBounds(plocha.getRozmer().vytvorRectangle());
            miestnost.setLayer(grafikaPlochy, VRSTVA_PLOCHA);
            miestnost.add(grafikaPlochy);
        }

        Vektor2D velkostRohu = new Vektor2D(Stena.SIRKA_STENY, Stena.SIRKA_STENY);
        velkostRohu = velkostRohu.sucinSoSkalarom(Displej.VELKOST_ROHU); 
        Rozmer2D[] rozmeryRohov = new Rozmer2D[]{
            new Rozmer2D(Vektor2D.ZERO, velkostRohu),
            new Rozmer2D(new Vektor2D(Hra.ROZMER_OKNA.getVelkostX() - velkostRohu.getX(), 0), velkostRohu),
            new Rozmer2D(new Vektor2D(0, Hra.ROZMER_OKNA.getVelkostY()  - velkostRohu.getY()), velkostRohu),
            new Rozmer2D(Hra.ROZMER_OKNA.getVelkost().rozdiel(velkostRohu), velkostRohu),
        };

        for (int i = 0; i < 4; i++) {
            label = new JLabel();
            String cesta = i == 0 || 3 == i ? "assets/cornerL.png" : "assets/cornerR.png";
            label.setIcon(new ImageIcon(cesta));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setVerticalAlignment(SwingConstants.CENTER);
            label.setBounds(rozmeryRohov[i].vytvorRectangle());
            miestnost.setLayer(label, VRSTVA_ROH);
            miestnost.add(label);
        }

        return miestnost;
    }
    private JPanel vytvorGrafikuMuru(Rozmer2D rozmer) {
        JPanel stena = new JPanel();
        stena.setBounds(rozmer.vytvorRectangle());
        stena.setBackground(Color.DARK_GRAY);
        return stena;
    }
    private JButton[] vytvorGrafikuUI() {
        JButton[] tlacitka = new JButton[StavHry.values().length - 1];
        
        for (int i = 0; i < StavHry.values().length; i++) {
            StavHry sh = StavHry.values()[i]; 
            if (sh != StavHry.HRA) {                
                JButton jb = new JButton();

                jb.setFocusable(false);
                jb.setContentAreaFilled(false);
                jb.setRolloverEnabled(false);
                jb.setBorderPainted(false);
                jb.setFocusPainted(false);
                jb.setOpaque(true);

                jb.setBounds(Rozmer2D.ZERO.vytvorRectangle());
                jb.setBackground(sh.getFarbaPozadia());
                jb.setLayout(null);
                jb.addActionListener(a -> {
                    this.restart = true;
                });
                tlacitka[i] = jb;

                JLabel jl = new JLabel();
                jl.setFont(sh.getFont());
                jl.setBounds(Hra.ROZMER_OKNA.vytvorRectangle());
                jl.setHorizontalAlignment(sh.getZarovananieX());
                jl.setVerticalAlignment(sh.getZarovananieY());
                jl.setForeground(sh.getFarbaTextu());
                jl.setText(sh.getText());
                jb.add(jl, BorderLayout.CENTER);
            }
        }
        return tlacitka;
    }
}
