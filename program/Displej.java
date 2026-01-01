import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import java.util.Hashtable;

/**
 * Zobrazovac okna Hry
 * 
 * @author y0hn
 * @version v0.13
 */
public class Displej {    
    public static final int VRSTVA_STRELA = 3;

    private static final int VRSTVA_PODLAHA = 0;
    private static final int VRSTVA_STENA = 1;
    private static final int VRSTVA_ROH = 2;
    private static final int VRSTVA_PLOCHA = 2;
    private static final int VRSTVA_HRAC = 4;
    private static final int VRSTVA_NEPRIATEL = 4;
    private static final int VRSTVA_UI_HRAC = 5;
    private static final int VRSTVA_UI = 6;

    private static final int POSUN_ROZMERU_X = 10;
    private static final int POSUN_ROZMERU_Y = 36;

    private static final Font FONT_MIESTNOST = new Font("Arial", 1, 200);
    private static final double VELKOST_ROHU = 1.05;

    private static final String GRAFIKA_HRAC = "assets/player.png";
    private static final String GRAFIKA_NEPIRATEL = "assets/enemy.png";

    private final JFrame okno;
    private final Hashtable<StavHry, JComponent> uzivatelskeRozhranie;
    private JLayeredPane aktivnaMiestnost;
    private boolean restart;
    private boolean[] hlavnaPonuka;
    private boolean spatDoMenu;

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

        this.uzivatelskeRozhranie = new Hashtable<StavHry, JComponent>();
        for (StavHry sh : StavHry.values()) {
            this.uzivatelskeRozhranie.put(sh, sh.getGrafika(0));

            if (sh == StavHry.MENU) {
                int pocet = sh.getVsetkyGrafiky().length;
                this.hlavnaPonuka = new boolean[pocet];

                for (int i = 1; i < pocet; i++) {
                    final int index = i;
                    sh.getGrafika(i).addActionListener(a -> {
                        this.hlavnaPonuka[index] = true;
                    });
                }
                
            } else if (StavHry.HRA != sh) {
                sh.getGrafika(0).addActionListener(a -> {
                    this.restart = true;
                });

                if (StavHry.PAUZA == sh) {
                    this.spatDoMenu = false;
                    sh.getGrafika(1).addActionListener(a -> {
                        this.spatDoMenu = true;
                    });
                }
            } 
        }
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
     * @return PRAVDA ak sa ma Displej restartovat, hodnota sa nastavi FALSE
     */
    public boolean ziskajRestart() {
        boolean restartuj = this.restart;
        if (restartuj) {
            this.restart = false;
        }
        return restartuj;
    }
    /**
     * Ziska hodnotu navratu do Hlavnej Ponuky iba 1 krat
     * @return PRAVDA ak bolo tlacidlo stlacene, hodnota sa nastavi FALSE
     */
    public boolean ziskajSpatDoMenu() {
        boolean spat = this.spatDoMenu;
        if (spat) {
            this.spatDoMenu = false;
        }
        return spat;
    }
    /**
     * Ziska hodnotu vstupov v hlavnej ponuke 1x krat
     * @param index tlacidla
     * @return PRAVDA ak bolo tlacidlo stlacene, hodnota sa nastavi FALSE
    */
    public boolean ziskajHlavnuPonuku(int index) {
        boolean ponuka = this.hlavnaPonuka[index];
        if (ponuka) {
            this.hlavnaPonuka[index] = false;
        }
        return ponuka;
    }
    /**
     * Vytvori Hracovi objekt na Displeji 
     * @param objektHraca
     */
    public void nastavHraca(Hrac objektHraca) {
        OtacanaGrafika grafika = new OtacanaGrafika(GRAFIKA_HRAC);

        // Vytvori prazdnu Miestnost
        if (this.aktivnaMiestnost == null) {
            this.aktivnaMiestnost = new JLayeredPane();
            this.aktivnaMiestnost.setBackground(Color.BLACK);
            this.aktivnaMiestnost.setBounds(Hra.ROZMER_OKNA.vytvorRectangle());
            this.aktivnaMiestnost.setLayout(null);
            this.okno.setContentPane(this.aktivnaMiestnost);
        
            // Premigruje Uzivatelske Rozhranie
            for (JComponent grafikaUI : this.uzivatelskeRozhranie.values()) {
                this.aktivnaMiestnost.setLayer(grafikaUI, VRSTVA_UI);
                this.aktivnaMiestnost.add(grafikaUI);
            }
        }

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
            for (JComponent grafikaUI : this.uzivatelskeRozhranie.values()) {
                this.aktivnaMiestnost.setLayer(grafikaUI, VRSTVA_UI);
                this.aktivnaMiestnost.add(grafikaUI);
            }
        }
    }
    /**
     * Nastavi grafiku pre vsetky aktivne Strely
     * @param objektHraca
     */
    public void nastavGrafikuStriel(Hrac objektHraca) {
        for (Strela s : objektHraca.ziskajStrely()) {
            OtacanaGrafika og = s.getGrafika();
            this.aktivnaMiestnost.setLayer(og, VRSTVA_STRELA);
            this.aktivnaMiestnost.add(og);
            og.obnovObraz();
        }
    }
    /**
     * Nastavi konecnu grafiku podla stavu hry
     * @param stav prideleny Stav Hry pre grafiku
     * @param zapni ak PRAVDA zapne grafiku inak vypne
     */
    public void nastavGrafikuPreStavHry(StavHry stav, boolean zapni) {
        Rozmer2D r = Rozmer2D.ZERO;
        if (zapni) {
            r = Hra.ROZMER_OKNA;
        }
        this.uzivatelskeRozhranie.get(stav).setBounds(r.vytvorRectangle());
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
        label.setFont(FONT_MIESTNOST);    
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
}
